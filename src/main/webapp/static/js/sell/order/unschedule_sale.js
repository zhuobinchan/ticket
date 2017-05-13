genDicJs([ '0202', '0207', '0216', '0217', '0220', '0226', '0232' ]);
var userMap = genMaps("from User order by name,id");
var formSetting = {
	po : 'TicketSale',
	type : "query",
	fields : [ [ {
		title : "售票员",
		type : "select",
		name : "createUserId",
		map : userMap
	}, {
		name : 'createTime',
		title : "售票日期",
		type : 'datescale'
	} ] ],
	queryRow : {
		align : 'center',
		buttons : [ {
			id : 'query',
			title : '查询',
			handlers : {
				click : function(e, queryForm) {
					queryData(e, queryForm);
				}
			}
		}, {
			id : 'resetBtn',
			title : '重置',
			handlers : {
				click : function() {
					form.reset();
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'ticketSaleGrid',
	po : "TicketSale",
	title : '不定期售票记录',
	fixWidth : true,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "createTime desc"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'saleNo',
		descs : "销售单号",
		align : 'center',
		width : 110
	}, {
		name : 'createTime',
		descs : "售票时间",
		align : 'center',
		width : 180
	}, {
		name : 'createUserId',
		descs : "售票人",
		type : 'select',
		align : 'center',
		width : 80
	}, {
		name : 'payType',
		descs : "支付方式",
		type : "select",
		align : 'center',
		width : 80
	}, {
		name : 'strategyName',
		descs : "门票策略",
		align : 'center',
		width : 100
	}, {
		name : 'ticketNum',
		descs : "售票数量",
		type : 'int',
		align : 'center',
		width : 70
	}, {
		name : 'cheapPrice',
		descs : "每张立减",
		type : 'int',
		align : 'center',
		width : 70
	}, {
		name : 'offsetPrice',
		descs : "原票抵扣",
		type : 'int',
		align : 'center',
		width : 70
	}, {
		name : 'discount',
		descs : "折扣比例",
		type : 'int',
		align : 'center',
		width : 70
	}, {
		name : 'showAmount',
		descs : "票面总额",
		align : 'center',
		formatter : "sumSaleShowAmount",
		width : 100
	}, {
		name : 'realAmount',
		descs : "实收总额",
		align : 'center',
		formatter : "sumSaleRealAmount",
		width : 100
	}, {
		name : 'sallerCode',
		descs : "营销编号",
		width : 120,
		align : 'center'
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'sale',
			title : '不定期售票',
			width : 100,
			handlers : {
				click : function() {
					var that = this;
					wins = new dhtmlXWindows();
					var windowWidth = 600;
					var windowHeight = 360;
					var cardWindow = wins.createWindow('unscheduleSale', ($(document.body).width() - windowWidth) / 2,
							top.window.document.body.scrollTop - 80 + (window.screen.height - windowHeight) / 2,
							windowWidth, windowHeight);
					cardWindow.setText('不定期售票');
					cardWindow.denyResize();
					cardWindow.denyMove();
					cardWindow.setModal(true);
					var form = cardWindow.attachForm();
					form.loadStruct([ {
						type : 'label',
						list : [ {
							type : "settings",
							position : "label-left",
							labelWidth : 80,
							inputWidth : 150,
							offsetLeft : 40
						}, {
							type : "combo",
							label : "票价",
							name : "ticketPrice",
							required : true
						}, {
							type : "input",
							label : "座位数",
							name : "ticketCount",
							required : true,
							validate : "positiveNumber",
							value : '1'
						}, {
							type : "input",
							label : "每张折扣%",
							name : "discount",
							validate : "zeroTo100",
							value : '100'
						}, {
							type : "input",
							label : "每张优惠",
							name : "reduce",
							validate : "greaterZero",
							value : '0'
						}, {
							type : "combo",
							label : "结算方式",
							name : "payType",
							userdata : {
								dic : '0207'
							},
							required : true
						}, {
							type : "combo",
							label : "表演场次",
							name : "showNumber",
							required : true
						}, {
							type : "newcolumn"
						}, {
							type : "combo",
							label : "营销员",
							name : "salesmanId"
						}, {
							type : "input",
							label : "应收金额",
							readonly : true,
							name : 'receivable',
							validate : "positiveNumber",
							value : "0"
						}, {
							type : "input",
							label : "实收金额",
							readonly : true,
							name : 'realPay',
							validate : "positiveNumber",
							value : "0"
						}, {
							type : "input",
							label : "备注",
							name : 'remarks'
						}, {
							type : "combo",
							label : "打印方式",
							name : "priceShowType"
						} ]
					}, {
						type : "block",
						blockOffset : 0,
						list : [ {
							type : "button",
							value : "取消",
							name : 'cancel',
							offsetLeft : 230,
							offsetTop : 20
						}, {
							type : "newcolumn"
						}, {
							type : "button",
							value : "确定",
							name : 'submit',
							offsetTop : 20
						} ]
					} ], 'json');
					positiveNumber = function(val) {
						return val > 0;
					}
					greaterZero = function(val) {
						return val >= 0;
					}
					zeroTo100 = function(val) {
						return val >= 0 && val <= 100;
					}

					SWI.util.addOptionsToFormDicCombo(form);
					var salesmanCombo = form.getCombo('salesmanId');
					var payTypeCombo = form.getCombo('payType');
					payTypeCombo.selectOption(0);
					var priceShowTypeCombo = form.getCombo('priceShowType');
					SWI.util.addDicComboOptions([ SWI.util.dicByCode['0209001'], SWI.util.dicByCode['0209002'],
							SWI.util.dicByCode['0209004'] ], priceShowTypeCombo);
					priceShowTypeCombo.selectOption(0);
					SWI.util.addSalesmanComboOptions(salesmanCombo);

					var priceCombo = form.getCombo('ticketPrice');
					var priceArray = SWI.util
							.genArray('select id, price from Area where price > 0 group by price order by price desc');
					$.each(priceArray, function(ind, data) {
						priceArray[ind] = [ data[0], data[1] + '' ];
					});
					priceCombo.addOption(priceArray);
					priceCombo.readonly(true);
					priceCombo.selectOption(0);
					priceCombo.attachEvent('onChange', function(value, text) {
						updateReceiable();
					});

					var showCombo = form.getCombo('showNumber');
					var showMap = genMaps("from ShowNumber where status = '0202001'");
					var showArray = [];
					$.each(showMap, function(id, data) {
						showArray.push([ id, data.name ]);
					});
					showCombo.addOption(showArray);
					showCombo.readonly(true);
					showCombo.selectOption(0);

					form.attachEvent('onChange', function(name, value) {
						if (name == 'ticketCount' || name == 'discount' || name == 'reduce') {
							updateReceiable();
						}
					});

					form.setFocusOnFirstActive();
					form.attachEvent('onButtonClick', function(name) {
						if (name === 'submit') {
							form.validate();
						} else if (name === 'cancel') {
							cardWindow.close();
						}
					});
					form.attachEvent('onAfterValidate', function(status) {
						if (status) {
							var param = {};
							param.strategy = 13; // 不定期售票
							var cheapPrice = form.getItemValue('reduce');
							var totalSize = form.getItemValue('ticketCount');
							var sallerCode = salesmanCombo.getSelectedText();
							var descs = form.getItemValue('remarks');
							if ($.trim(sallerCode) != "") {
								param.sallerCode = sallerCode.split(' ')[0];
							}
							if ($.trim(descs) != "") {
								param.descs = descs;
							}
							param.cheapPrice = cheapPrice;
							param.payType = payTypeCombo.getSelectedValue();

							param.offsetPrice = 0;
							param.discount = form.getItemValue('discount');
							param.realPrice = form.getItemValue('realPrice');
							param.totalSize = totalSize;
							param.priceShowType = priceShowTypeCombo.getSelectedValue();
							param.isShowSeat = false;
							param.hasTea = "0103002";
							param.giftNum = 0;
							param.status = "0203001";
							param.showNumberId = showCombo.getSelectedValue();
							// 获取座位ID和对应票价
							var areaPrice = {};
							var seatIds = [];
							for (var i = 0; i < totalSize; i++) {
								seatIds.push(null);
							}
							areaPrice[priceCombo.getSelectedValue()] = {
								seatIds : seatIds,
								price : form.getItemValue('realPay') / totalSize
							};
							param.areaPrice = areaPrice;
							BaseDwr.saleTickets(JSON.stringify(param), function(ret) {
								if (ret.status === 0) {
									SWI.util.printTicket(ret.list);
									cardWindow.close();
									grid.reload();
									SWI.util.adjustPageHeight();
								} else if (ret.status === 1) {
									dhtmlx.alert("营销编号不存在");
								} else {
									dhtmlx.alert("出票失败");
								}
							});
						}
					});

					updateReceiable = function() {
						var discount = form.getItemValue('discount') / 100;
						var reduce = form.getItemValue('reduce');
						var ticketCount = form.getItemValue('ticketCount');
						form.setItemValue('receivable', priceCombo.getSelectedText() * ticketCount);
						form.setItemValue('realPay',
								(Math.round(priceCombo.getSelectedText() * discount / 10) * 10 - reduce) * ticketCount);
					}

					updateReceiable();
				}
			}
		} ]
	}
};
var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
var wins;
function queryData(e, form) {
	var param = SWI.util.formatFormParam(form);
	param.push({
		strategyName : '不定期售票'
	});
	grid.load({
		data : param
	});

	SWI.util.adjustPageHeight();
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	queryData(event, $('#formview form'));

});
