var SWI;
if (SWI === undefined || SWI === null) {
	SWI = {};
}
SWI.contentPath = '';
SWI.currentUserName = '';
SWI.currentUserId = null;
SWI.version = '0.1';
SWI.lastUpdate = '2016-5-28';

var SWIutil = function(id, setting) {
	this.id = id;
	this.setting = setting;
}

SWI.util = new SWIutil('util_swi', null);

/**
 * 格式化表单项参数
 */
SWIutil.prototype.formatFormParam = function(form) {
	var param = [];
	var o = {};
	$.each(form.serializeArray(), function() {
		if (this.value) {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		}
	});
	for ( var key in o) {
		// 过滤autocomplete的辅助输入框
		if (key.indexOf('_txt') < 0) {
			if (o[key]) {
				var a = {};
				if (o[key] instanceof Array) {
					a['r'] = 'between';
					a['name'] = key;
					a['value'] = o[key];
				} else {
					a[key] = o[key];
				}
				param.push(a);
			}
		}
	}
	return param;
}

/**
 * 修改表单项参数
 */
SWIutil.prototype.changeFormParam = function(source, name, type) {
	$.each(source, function(i, value) {
		if (value['r'] == undefined && value[name] !== undefined) {
			if (type == 'like') {
				value['value'] = '%' + value[name] + '%';
			} else {
				value['value'] = value[name];
			}
			delete value[name];
			value['r'] = type;
			value['name'] = name;
		}
	});
}

/**
 * 获取contextPath
 */
SWIutil.prototype.getContextPath = function() {
	var result = SWI.contentPath;
	if (!result) {
		var pathName = document.location.pathname;
		var index = pathName.substr(1).indexOf("/");
		result = pathName.substr(0, index + 1);
	}
	return result;
}

/**
 * 营销员下拉菜单自动完成处理部分
 */
SWIutil.prototype.autocompleteSalesman = function(name) {
	$.ajax({
		url : this.getContextPath() + "/public/tool/getSalesmanArr.htmls",
		type : "POST",
		dataType : "json",
		success : function(ret) {
			$(":input[name=" + name + "_txt]").autocomplete({
				minLength : 0,
				source : ret,
				select : function(event, ui) {
					$(":input[name=" + name + "_txt]").val(ui.item.value + ui.item.name);
					$(":input[name=" + name + "]").val(ui.item.id);
					return false;
				}
			})
		}
	});
}

/**
 * 初始化页面时执行的内容
 */
SWIutil.prototype.init = function() {
	this.loadDicInfo();
	this.loadParameterInfo();
	$(document).ready(function() {
		if (window.dwr && dwr.engine) {
			dwr.engine.setErrorHandler(function(msg, ex) {
				layer.alert(msg, {
					icon : 2,
					title : '提示信息'
				});
			});
		}
		if (window.layer && layer.config) {
			layer.config({
				offset : function() {
					return top.window.document.body.scrollTop + (window.screen.height - $(this).height()) / 2;
				}
			});
		}
	});
}

/**
 * 根据优惠策略、支付方式等条件计算实收金额
 * 
 * @param strategy
 *            优惠策略
 * @param payType
 *            支付方式
 * @param money
 *            应收金额
 * @param cardDiscount
 *            充值卡折扣
 * @return 实收金额
 */
SWIutil.prototype.calculateRealPay = function(strategy, payType, money, cardDiscount) {
	var discount = 1;
	var reduce = 0;
	var realPay = 0;
	eval('var config = ' + this.getParameter('venueStrategy', strategy));

	if (config.discount !== undefined && config.discount !== null) {
		if (isNaN(config.discount)) {
			if (typeof config.discount == 'string') {
				discount = eval(config.discount);
			} else {
				$.each(config.discount, function(ind, value) {
					if (payType == value.payType) {
						discount = value.discount;
						return false;
					}
				});
			}
		} else {
			discount = config.discount;
		}
		realPay = money * discount;
	} else if (config.reduce !== undefined && config.reduce !== null) {
		if (isNaN(config.reduce)) {
			$.each(config.reduce, function(ind, value) {
				if (money >= value.money) {
					reduce = value.reduce;
					return false;
				}
			});
		}
		realPay = money - reduce;
	}
	return Math.round(realPay);
}

/**
 * 根据hql语句生成数组
 * 
 * @param hql
 * @returns
 */
SWIutil.prototype.genArray = function(hql) {
	var obj = null;
	$.ajax({
		url : g_path + '/public/util/genList.htmls',
		data : {
			hql : hql
		},
		dataType : 'json',
		type : 'POST',
		async : false,
		success : function(ret) {
			obj = ret;
		}
	})
	return obj;
}

/**
 * 把form里面的数据字典Combo全部加上option
 */
SWIutil.prototype.addOptionsToFormDicCombo = function(form) {
	var that = this;
	form.forEachItem(function(name) {
		if (form.getItemType(name) === 'combo') {
			var dicCode = form.getUserData(name, 'dic');
			if (dicCode) {
				var dicObj = that.dicByType[dicCode];
				if (dicObj && dicObj.items) {
					that.addDicComboOptions(dicObj.items, form.getCombo(name));
				}
			}
		}
	});
}

/**
 * 往数据字典Combo加上option
 */
SWIutil.prototype.addDicComboOptions = function(arr, combo) {
	combo.clearAll();
	var options = [];
	$.each(arr, function(ind, obj) {
		options.push([ obj.code, obj.text ]);
	});
	combo.addOption(options);
	combo.readonly(true);
}

/**
 * 加载参数配置
 */
SWIutil.prototype.loadParameterInfo = function() {
	if (top.parameterByType === undefined || top.parameterByType === null) {
		var map = genMaps('from Parameter');
		top.parameterByType = {};
		$.each(map, function(id, data) {
			if (!top.parameterByType[data.type]) {
				top.parameterByType[data.type] = {};
			}
			top.parameterByType[data.type][data.value] = data;
		});
	}
	this.parameterByType = top.parameterByType;
}

/**
 * 获取系统参数
 * 
 * @param type
 * @param value
 */
SWIutil.prototype.getParameter = function(type, value, number) {
	var config;
	if (number === undefined || number === null) {
		number = 1;
	}
	if (this.parameterByType[type] && this.parameterByType[type][value]) {
		config = this.parameterByType[type][value]['config' + number];
	}
	return config;
}

/**
 * 分别按code和type加载数据字典
 */
SWIutil.prototype.loadDicInfo = function() {
	if (top.dicByCode === undefined || top.dicByCode === null || top.dicByType === undefined || top.dicByType === null) {
		// 按code加载数据字典
		if (top.dicByCode === undefined || top.dicByCode === null) {
			var map = genMaps('from Dic');
			top.dicByCode = {};
			$.each(map, function(id, value) {
				top.dicByCode[value.code] = value;
			});
		}
		// 按type加载数据字典
		if (top.dicByType === undefined || top.dicByType === null) {
			top.dicByType = {};
			var typeMap = genMaps('from Dic where tier = 2');
			$.each(typeMap, function(typeId, type) {
				type.items = [];
				$.each(map, function(id, value) {
					if (type.id == value.parentId) {
						type.items.push(value);
					}
				});
				type.items.sort(function(a, b) {
					return (a.orderno == b.orderno) ? (a.code - b.code) : (a.orderno - b.orderno);
				});
				top.dicByType[type.code] = type;
			});
		}
	}
	this.dicByCode = top.dicByCode;
	this.dicByType = top.dicByType;
}

/**
 * 加载营销员信息到combo
 */
SWIutil.prototype.addSalesmanComboOptions = function(combo) {
	if (top.allSalesman === undefined || top.allSalesman === null) {
		top.allSalesman = genMaps("from Salesman where status = '0202001' order by code");
	}
	combo.clearAll();
	var options = [];
	$.each(top.allSalesman, function(id, obj) {
		options.push([ id, obj.code + ' ' + obj.name ]);
	});
	combo.addOption(options);
	combo.enableFilteringMode(true);
}

/**
 * 加载座位信息到combo
 */
SWIutil.prototype.addSeatComboOptions = function(combo) {
	if (top.allSeats === undefined || top.allSeats === null) {
		top.allSeats = this
				.genArray("select s.id, concat(f.name, s.name) from Seat s, Area a, Block b, Partition p, Floor f, Theater t "
						+ " where s.type = '0230001' and s.areaId = a.id and a.blockId = b.id and b.partitionId = p.id and p.floorId = f.id "
						+ " and f.theaterId = t.id order by t.id, f.orders, s.rowNum, s.columnNum");
	}
	combo.clearAll();
	var options = [];
	$.each(top.allSeats, function(ind, obj) {
		options.push([ obj[0], obj[1] ]);
	});
	combo.addOption(options);
	combo.enableFilteringMode(true);
}

/**
 * 显示报表
 * 
 * @param param
 * @param path
 */
SWIutil.prototype.showReport = function(param, path) {
	var table = $('#list table');
	table.empty();
	$.ajax({
		url : SWI.util.getContextPath() + path,
		type : "POST",
		dataType : "json",
		async : false,
		data : {
			paramString : JSON.stringify(param)
		},
		success : function(ret) {
			if (ret == '') {
				table.append($("<tr><td colspan='4'>没有数据！</td></tr>"));
				return;
			}
			var tr = null;

			// 表头
			var titles = ret[0];
			tr = $("<tr>").appendTo(table);
			for (var i = 0; i < titles.length; i++) {
				tr.append($('<th>').html(titles[i]));
			}

			// 表格数据
			for (i = 1; i < ret.length - 1; i++) {
				var fields = ret[i];
				tr = $("<tr>").appendTo(table);

				for (var j = 0; j < fields.length; j++) {
					tr.append($("<td align='center' style='white-space:normal;'>").html(fields[j]));
				}
			}

			// 表尾
			var tails = ret[ret.length - 1];
			tr = $("<tr>").appendTo(table).css({
				fontWeight : 'bold'
			});
			for (i = 0; i < titles.length; i++) {
				tr.append($('<td align="center">').html(tails[i]));
			}

			$('div#list').css('height', '');
			$('div#list2').css('height', '');
			$('div#list3').css('height', '');
			ajustHeight2();
		}
	});
}

/**
 * 导出刚刚查询的报表
 * 
 * @param title
 *            报表标题
 */
SWIutil.prototype.exportExcelReport = function(title) {
	if (!title) {
		title = $(document).attr('title');
	}
	$("<iframe name='ef' style=\"display: none\"></iframe>").appendTo($("body"));
	$(
			"<form target='ef' action='" + SWI.util.getContextPath() + "/public/venuestat/exportExcelReport.htmls'>"
					+ "<input type='hidden' id='title' name='title' value='" + title + "'/></form>")
			.appendTo($('body')).submit();
}

/**
 * 导出Excel文件
 */
SWIutil.prototype.exportExcel = function(form, path, title, po) {
	if (!title) {
		title = $(document).attr('title');
	}
	var fields = '';
	var o = {};
	if (po !== undefined && po !== null) {
		o = po;
	}
	var value = '';
	$.each(form.serializeArray(), function() {
		if (this.value) {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		}
	});
	for ( var key in o) {
		// 过滤autocomplete的辅助输入框
		if (key.indexOf('_txt') < 0) {
			if (o[key]) {
				var a = {};
				if (o[key] instanceof Array) {
					value = o[key].join(';');
				} else {
					value = o[key];
				}
				fields += '<input type="hidden" id="' + key + '" name="' + key + '" value="' + value + '" />';
			}
		}
	}
	$("<iframe name='ef' style=\"display: none\"></iframe>").appendTo($("body"));
	$(
			"<form target='ef' action='" + path + "'>" + fields
					+ "<input type='hidden' id='title' name='title' value='" + title + "'/></form>")
			.appendTo($('body')).submit();
}

/**
 * 导出会员卡数据
 */
SWIutil.prototype.exportExcel4MemberCard = function(form, type, title) {
	var o = {};
	if (type) {
		o['type'] = type;
	}
	var path = SWI.util.getContextPath() + "/public/memberstat/exportMemberCardExcel.htmls";
	this.exportExcel(form, path, title, o);
}

/**
 * 导出会员卡充值数据
 * 
 * @param form
 * @param title
 */
SWIutil.prototype.exportExcel4MemberCardRecharge = function(form, title) {
	var path = SWI.util.getContextPath() + "/public/memberstat/exportMemberCardRechargeExcel.htmls";
	this.exportExcel(form, path, title, null);
}

/**
 * 导出会员卡消费数据
 * 
 * @param form
 * @param title
 */
SWIutil.prototype.exportExcel4MemberCardConsumer = function(form, title) {
	var path = SWI.util.getContextPath() + "/public/memberstat/exportMemberCardConsumerExcel.htmls";
	this.exportExcel(form, path, title, null);
}

/**
 * 打印预览刚刚查询的报表
 * 
 * @param startTime
 *            查询开始日期
 * @param endTime
 *            查询结果日期
 * @param orient
 *            纸张方向
 * @param title
 *            报表标题
 */
SWIutil.prototype.printReport = function(startTime, endTime, orient, title) {
	if (orient == undefined || orient == null) {
		orient = 2;
	}
	if (!title) {
		title = $(document).attr('title');
	}
	LODOP = getLodop();
	LODOP.SET_PRINT_PAGESIZE(orient, 0, 0, 'A4');
	var div = jQuery('#list');
	var strStyle = "<style>td,th {border:1px #000 solid;border-collapse: collapse;font-size:9pt;text-align:center}</style>";
	var strFormHtml = $("#list div.rcon2").html();// 获取内容
	strFormHtml = strStyle + strFormHtml;
	if (startTime && endTime) {
		var today = getCurrentTime();
		var time = "23:59:59";
		if (today == endTime) {
			time = getCurrentTime("HH:mm");
		}
	}
	var top = 10;
	top = this.addReportPrintItem(top, 39, null, 'HTM', "<div style='width:auto;text-align:center;font-size:13pt'>"
			+ title + "</div>");
	LODOP.SET_PRINT_STYLEA(0, "Horient", 3);
	top = this.addReportPrintItem(top, 39, null, 'HTM', "<div style='width:100%;font-size:9pt;'>制表日期：" + today
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制表人：" + SWI.currentUserName + "</div>");
	if (startTime && endTime) {
		top = this.addReportPrintItem(top, 25, null, 'TEXT', "查询条件【" + startTime + " 至 " + endTime + " " + time + "】");
	}
	top = this.addReportPrintItem(top, '90%', null, 'TABLE', strFormHtml);

	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
	LODOP.PREVIEW();
};

/**
 * 添加报表打印项，指定上边距和高度，返回下一项的上边距
 * 
 * @param top
 *            打印项在纸张内的上边距
 * @param height
 *            打印区域的高度
 * @param margin
 *            左右页边距，单位px
 * @param style
 *            打印项的类型
 * @param data
 *            打印内容
 * @returns 下一个打印项在纸张内的上边距
 */
SWIutil.prototype.addReportPrintItem = function(top, height, margin, style, data) {
	if (!margin) {
		margin = 10;
	}
	switch (style) {
	case "HTM":
		LODOP.ADD_PRINT_HTM(top, margin, 'RightMargin:' + margin + 'px', height, data);
		break;

	case "TEXT":
		LODOP.ADD_PRINT_TEXT(top, margin, 'RightMargin:' + margin + 'px', height, data);
		break;

	case "TABLE":
		LODOP.ADD_PRINT_TABLE(top, margin, 'RightMargin:' + margin + 'px', height, data);
		break;

	default:
		LODOP.ADD_PRINT_HTM(top, margin, 'RightMargin:' + margin + 'px', height, data);
		break;
	}
	return isNaN(height) ? top : top + height;
}

/**
 * 打印场内消费小票
 * 
 * @param company
 *            公司名称
 * @param saleNo
 *            交易流水号
 * @param createTime
 *            交易时间
 * @param payType
 *            支付方式
 * @param realPay
 *            实收
 * @param receivable
 *            应收
 * @param salesman
 *            点单员
 * @param user
 *            操作员
 * @param seat
 *            座位信息
 * @param source
 *            点单明细列表
 * @param code
 *            卡号
 * @param balance
 *            卡余额
 */
SWIutil.prototype.printReceipt = function(company, saleNo, createTime, payType, realPay, receivable, salesman, user,
		seat, source, code, balance) {
	var margin = 10;
	var height = 50;
	var top = 10;
	LODOP = getLodop();
	LODOP.SET_PRINT_PAGESIZE(1, 720, 1000, '');
	var style = "<style>td {font-size:13;} .tgoods td{border:1px dashed #000}</style>"
	var table = $('<table width="100%" />');
	// 公司名称
	var tr = $('<tr/>').appendTo(table);
	tr.append('<td colspan="3" height="30" style="text-align:center;">' + company + '</td>');
	tr = $('<tr/>').appendTo(table);
	// 交易号、交易时间
	tr.append('<td colspan="2">交易号</td>');
	tr.append('<td width="40%">交易时间</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td colspan="2">' + saleNo + '</td>');
	tr.append('<td>' + createTime.format('yyyy-MM-dd HH:mm') + '</td>');
	// 商品列表
	tr = $('<tr/>').appendTo(table);
	var td = $('<td colspan="3" />');
	tr.append(td);
	var tbGoods = $('<table width="100%" border=0 rules=cols cellspacing=0 frame=hsides class="tgoods" />');
	td.append(tbGoods);

	if (source.forEachRow) {
		tr = $('<tr/>').appendTo(tbGoods);
		for (var i = 0; i < source.getColumnsNum(); i++) {
			var content = source.getColumnLabel(i);
			// 不打印单位
			if (i !== 3) {
				tr.append('<td ' + (i == 0 ? '' : 'style="text-align:right;"') + '>' + content + '</td>');
			}
		}
		source.forEachRow(function(id) {
			tr = $('<tr/>').appendTo(tbGoods);
			for (var i = 0; i < source.getColumnsNum(); i++) {
				var content = source.cells(id, i).getValue();
				// 不打印单位
				if (i !== 3) {
					tr.append('<td ' + (i == 0 ? '' : 'style="text-align:right;"') + '>' + content + '</td>');
				}
			}
		});
	} else {
		source.each(function(index, goods) {
			tr = $('<tr/>').appendTo(tbGoods);
			for (var i = 2; i < goods.children.length; i++) {
				var content = goods.children[i].innerHTML;
				tr.append('<td ' + (i == 2 ? '' : 'style="text-align:right;"') + '>' + content + '</td>');
			}
		});
	}
	// 结算方式、应收、实收、点单员、操作员、座位等
	tr = $('<tr/>').appendTo(table);
	tr.append('<td width="30%">结算方式：</td>');
	tr.append('<td colspan="2">' + payType + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>消费合计：</td>');
	tr.append('<td colspan="2">' + realPay + '元</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>应收：</td>');
	tr.append('<td colspan="2">' + receivable + '元</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>点单员：</td>');
	tr.append('<td colspan="2">' + salesman + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>操作员：</td>');
	tr.append('<td colspan="2">' + user + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>座位信息：</td>');
	tr.append('<td colspan="2">' + seat + '</td>');
	if (code) {
		tr = $('<tr/>').appendTo(table);
		tr.append('<td>卡号：</td>');
		tr.append('<td colspan="2">' + code + '</td>');
		tr = $('<tr/>').appendTo(table);
		tr.append('<td>本次结余：</td>');
		tr.append('<td colspan="2">' + balance + '</td>');
	}
	// 订座电话
	if (this.dicByCode['0104002'] && this.dicByCode['0104002'].text) {
		tr = $('<tr/>').appendTo(table);
		tr.append('<td colspan="3" style="text-align:center;">' + this.dicByCode['0104002'].text + '</td>');
	}

	var div = $('<div/>');
	div.append(table);
	top = SWI.util.addReportPrintItem(top, '100%', margin, 'TABLE', style + div.html());

	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
	LODOP.PRINT();
}

/**
 * 打印消费/充值卡消费小票
 * 
 * @param company
 *            公司
 * @param branch
 *            场所
 * @param saleNo
 *            流水号
 * @param machine
 *            机号
 * @param createTime
 *            时间
 * @param money
 *            金额
 * @param user
 *            操作员
 * @param code
 *            卡号
 * @param balance
 *            卡上次结余
 */
SWIutil.prototype.printCardReceipt = function(company, branch, saleNo, machine, createTime, money, user, code, balance) {
	var margin = 10;
	var height = 50;
	var top = 10;
	LODOP = getLodop();
	LODOP.SET_PRINT_PAGESIZE(1, 720, 1000, '');
	var style = "<style>td {font-size:13;} .tgoods td{border:1px dashed #000} .tcon td{border:0px}</style>"
	var mainTable = $('<table width="100%" />');
	// 公司名称
	var tr = $('<tr/>').appendTo(mainTable);
	tr.append('<td height="30" style="text-align:center;">' + company + '消费系统</td>');
	tr = $('<tr/>').appendTo(mainTable);
	var td = $('<td/>').appendTo(tr);
	var tbGoods = $('<table width="100%" border=0 rules=cols cellspacing=0 frame=hsides class="tgoods" />');
	td.append(tbGoods);
	// 消费场所、交易号、消费时间等
	tr = $('<tr/>').appendTo(tbGoods);
	td = $('<td/>').appendTo(tr);
	table = $('<table width="100%" class="tcon" />').appendTo(td);
	tr = $('<tr/>').appendTo(table);
	tr.append('<td width="30%">消费场所：</td>');
	tr.append('<td>' + branch + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>消费时间：</td>');
	tr.append('<td>' + createTime.format('yyyy-MM-dd HH:mm') + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>交易号：</td>');
	tr.append('<td>' + saleNo + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>机号：</td>');
	tr.append('<td>' + machine + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>操作员：</td>');
	tr.append('<td>' + user + '</td>');
	// 卡信息
	tr = $('<tr/>').appendTo(tbGoods);
	td = $('<td/>').appendTo(tr);
	table = $('<table width="100%" class="tcon" />').appendTo(td);
	tr = $('<tr/>').appendTo(table);
	tr.append('<td width="30%">卡号：</td>');
	tr.append('<td>' + code + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>上次结余：</td>');
	tr.append('<td>' + balance + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>本次消费：</td>');
	tr.append('<td>' + money + '</td>');
	tr = $('<tr/>').appendTo(table);
	tr.append('<td>本次结余：</td>');
	tr.append('<td>' + (balance - money) + '</td>');
	// 会员签名
	tr = $('<tr/>').appendTo(tbGoods);
	td = $('<td/>').appendTo(tr);
	table = $('<table width="100%" class="tcon" />').appendTo(td);
	tr = $('<tr/>').appendTo(table);
	tr.append('<td width="30%" height="60">会员确认：</td>');
	tr.append('<td>&nbsp;</td>');

	var div = $('<div/>');
	div.append(mainTable);
	top = SWI.util.addReportPrintItem(top, '100%', margin, 'TABLE', style + div.html());

	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
	LODOP.PRINT();
}

/**
 * 打印门票
 * 
 * @param arr
 * @param callback
 */
SWIutil.prototype.printTicket = function(arr, callback) {
	var that = this;
	if (typeof (callback) != 'undefined') {
		callback();
	}
	var LODOP = getLodop();
	var ticketWidth = this.getParameter('ticketSize', 'width') || 2100;
	var ticketHeight = this.getParameter('ticketSize', 'heigth') || 770;
	LODOP.SET_PRINT_PAGESIZE(1, ticketWidth, ticketHeight, '');

	var printPara = 'printTicket';
	jQuery(arr).each(function(i, infos) {
		// LODOP.SET_PRINTER_INDEX(printerName);

		// 定义变量
		var playTitle = infos['showNumber'];
		var playDate = infos['playDate'];
		var seatText = infos['seatName'];
		var ticketPrice = infos['price'];
		var ticketSerial = infos['saleNo'];

		$.each(that.parameterByType[printPara], function(id, value) {
			eval('var content = ' + that.getParameter(printPara, id));
			var left = content.left;
			var top = content.top;
			LODOP.SET_PRINT_STYLE("FontSize", content.fontSize);

			$.each(content.items, function(ind, item) {
				LODOP.ADD_PRINT_TEXT(top, left + item.left, item.width, item.height, item.label + item.text);
				if (item.fontSize) {
					LODOP.SET_PRINT_STYLEA(0, "FontSize", item.fontSize);
				}
				top += item.height;
			});
		});

		LODOP.PRINT();
	});
}

/**
 * 调整页面高度
 */
SWIutil.prototype.adjustPageHeight = function() {
	var h = $(top).height() - 80;
	var elements = $('body').children('div[class^=rbox]');
	var eh = 0;
	var element = null;
	elements.each(function(i, div) {
		eh += $(div).height();
		element = div;
	});
	h = Math.max(h, eh, $(document.body).height());
	$(top.document).find("iframe[name=mainFrame]").height(h);
	$(top.document).find("div.mainl").height(h);
}

SWI.util.init();