var SWI;
if (SWI === undefined || SWI === null) {
	SWI = {};
}
if (SWI.venue === undefined || SWI.venue === null) {
	SWI.venue = {};
}
SWI.venue.orderGoods = function(id, setting) {
	this.id = id;
	this.setting = setting;
}

SWI.venue.orderGoods.prototype.addChooseGoods = function(goods) {
	var id = goods.id + '';
	var ids = this.chooseGrid.getAllRowIds().split(',');
	if ($.inArray(id, ids) >= 0) {
		this.chooseGrid.cells(id, 1).setValue(parseInt(this.chooseGrid.cells(id, 1).getValue()) + 1);
		// 提示+1
		var cell = this.chooseGrid.cells(id, 1).cell;
		var x = window.dhx.absLeft(cell);
		var y = window.dhx.absTop(cell);
		var width = cell.offsetWidth;
		var height = cell.offsetHeight;
		this.repeatPop.show(x, y, width, height);
	} else {
		this.chooseGrid.addRow(id, goods.name + ',1,' + goods.price + ',' + this.unitChinese[goods.unit] + ','
				+ goods.price);
	}
}

/**
 * 初始化页面
 */
SWI.venue.orderGoods.prototype.initPage = function() {
	var myLayout = new dhtmlXLayoutObject(document.body, "3L");
	myLayout.cells("a").setText('可选商品');
	var scanToolbar = myLayout.cells("a").attachToolbar();
	scanToolbar.addText('info', 0, '扫描商品条形码');
	var scanInput = scanToolbar.addInput('scanGoods', 1, '', 200);

	var gtTab = myLayout.cells("a").attachSidebar({
		width : 100,
		template : 'text'
	});
	var smallType = genMaps("from GoodsType where parentId != 0 and status = '0202001' order by orderno");
	// 单位中文化
	this.unitChinese = {};
	var that = this;
	$.each(SWI.util.dicByType['0222'].items, function(ind, obj) {
		that.unitChinese[obj.code] = obj.text;
	});
	// 提示重新选择商品
	this.repeatPop = new dhtmlXPopup();
	this.repeatPop.attachHTML('<span style="color: red;">+1</span>');

	var goodsMap = genMaps("from Goods where status = '0202001' order by typeId, orderno");
	$.each(smallType, function(typeId, type) {
		gtTab.addItem({
			id : typeId,
			text : type.name
		});
		var goodsGrid = gtTab.cells(typeId).attachGrid();
		goodsGrid.setHeader('商品名称,单价,单位');
		goodsGrid.setInitWidthsP('50, 25, 25');
		goodsGrid.setColTypes('ro,ro,ro');
		goodsGrid.enableAutoWidth(true);
		goodsGrid.init();

		$.each(goodsMap, function(goodsId, goods) {
			if (goods.typeId == typeId) {
				goodsGrid.addRow(goodsId, goods.name + ',' + goods.price + ',' + that.unitChinese[goods.unit]);
			}
		});
		goodsGrid.attachEvent("onRowDblClicked", function(id, ind) {
			that.addChooseGoods(goodsMap[id]);
		});
	});
	gtTab.goToNextItem();

	myLayout.cells("b").setText('已选商品');
	var chooseToolbar = myLayout.cells("b").attachToolbar();
	chooseToolbar.setAlign('right');
	chooseToolbar.setIconsPath(SWI.util.getContextPath() + '/static/dhtmlx/codebase/imgs/dhxtree_material/');
	chooseToolbar.addButton('deleteChoose', 0, '删除已选', 'iconCheckGray.gif');
	chooseToolbar.attachEvent('onClick', function(id) {
		var ids = that.chooseGrid.deleteSelectedRows();
		that.updateReceivable();
	});
	this.chooseGrid = myLayout.cells("b").attachGrid();
	this.chooseGrid.setHeader('商品名称,数量,单价,单位,合计');
	this.chooseGrid.setInitWidthsP('40,15,15,15,15');
	this.chooseGrid.setColTypes('ro,ed,ro,ro,ro');
	this.chooseGrid.enableAutoWidth(true);
	this.chooseGrid.enableMultiselect(true);
	this.chooseGrid.init();
	this.chooseGrid.attachEvent("onCellChanged", function(rId, cInd, nValue) {
		if (cInd === 1) {
			that.chooseGrid.cells(rId, 4).setValue(
					parseInt(that.chooseGrid.cells(rId, 1).getValue())
							* parseFloat(that.chooseGrid.cells(rId, 2).getValue()));
		}
		that.updateReceivable();
	});

	myLayout.cells("c").setText('下单');
	myLayout.cells("c").setHeight(300);
	var form = myLayout.cells("c").attachForm();
	this.form = form;
	form.loadStruct([ {
		type : 'settings',
		position : 'label-left',
		labelWidth : 'auto',
		inputWidth : 'auto'
	}, {
		type : 'label',
		list : [ {
			type : "settings",
			position : "label-left",
			labelWidth : 80,
			inputWidth : 120,
			offsetLeft : 10
		}, {
			type : "combo",
			label : "座位",
			name : "seatId"
		}, {
			type : "combo",
			label : "优惠策略",
			name : "strategy",
			userdata : {
				dic : '0225'
			}
		}, {
			type : "input",
			label : "应收金额",
			readonly : true,
			name : 'receivable',
			validate : "positiveNumber",
			value : "0"
		}, {
			label : '补差价方式',
			type : 'combo',
			name : 'fillPayType',
			userdata : {
				dic : '0207'
			}
		}, {
			label : '卡号',
			type : 'input',
			readonly : true,
			name : 'cardCode'
		}, {
			type : "newcolumn"
		}, {
			type : "combo",
			label : "点单员",
			name : "salesmanId",
			required : true
		}, {
			type : "combo",
			label : "支付方式",
			name : "payType",
			userdata : {
				dic : '0207'
			}
		}, {
			type : "input",
			label : "实收金额",
			readonly : true,
			name : 'realPay',
			value : "0"
		}, {
			label : '补差价金额',
			type : 'input',
			readonly : true,
			name : 'fill',
			value : "0"
		}, {
			label : '卡余额',
			type : 'input',
			readonly : true,
			name : 'cardBalance',
			value : "0"
		} ]
	}, {
		type : "block",
		blockOffset : 0,
		list : [ {
			type : "button",
			value : "取消",
			name : 'cancel',
			offsetLeft : 200,
			offsetTop : 20
		}, {
			type : "newcolumn"
		}, {
			type : "button",
			value : "下单",
			name : 'order',
			offsetTop : 20
		} ]
	} ], 'json');
	positiveNumber = function(val) {
		return val > 0;
	}
	SWI.util.addOptionsToFormDicCombo(form);
	this.seatCombo = form.getCombo('seatId');
	SWI.util.addSeatComboOptions(this.seatCombo);
	this.salesmanCombo = form.getCombo('salesmanId');
	SWI.util.addSalesmanComboOptions(this.salesmanCombo);
	this.strategyCombo = form.getCombo('strategy');
	this.strategyCombo.deleteOption('0225003');
	this.payTypeCombo = form.getCombo('payType');
	this.receivableInput = form.getInput('receivable');
	this.realPayInput = form.getInput('realPay');
	this.fillPayTypeCombo = form.getCombo('fillPayType');
	SWI.util
			.addDicComboOptions([ SWI.util.dicByCode['0207001'], SWI.util.dicByCode['0207002'] ], this.fillPayTypeCombo);
	this.fillInput = form.getInput('fill');
	this.cardCodeInput = form.getInput('cardCode');
	form.attachEvent('onButtonClick', function(name) {
		if (name === 'order') {
			form.validate();
		} else if (name === 'cancel') {
			dhtmlx.confirm('确定取消吗？', function(result) {
				if (result) {
					that.initData();
				}
			});
		}
	});
	form.attachEvent('onAfterValidate', function(status) {
		if (status) {
			dhtmlx.confirm('确定下单吗？', function(result) {
				if (result) {
					that.saveOrder();
				}
			});
		}
	});
	scanToolbar.attachEvent('onEnter', function(id, value) {
		if (value) {
			if (id === 'scanGoods') {
				$.each(goodsMap, function(goodsId, goods) {
					if (goods.barCode && goods.barCode == value) {
						that.addChooseGoods(goods);
						scanToolbar.setValue(id, '');
						return false;
					}
				});
			}
		}
	});
	// 优惠策略决定支付方式
	this.strategyCombo.attachEvent('onChange', function(value, text) {
		if (value == "0225002") {
			var dicArray = [ SWI.util.dicByCode['0207010'] ];
			SWI.util.addDicComboOptions(dicArray, that.payTypeCombo);
			that.payTypeCombo.selectOption(0);
			that.showCheckCard();
		} else if (value == "0225001" || value == "0225004") {
			dicArray = [ SWI.util.dicByCode['0207001'], SWI.util.dicByCode['0207002'] ];
			SWI.util.addDicComboOptions(dicArray, that.payTypeCombo);
			that.payTypeCombo.selectOption(0);
			form.setItemValue('cardCode', '');
			form.setItemValue('cardBalance', 0);
		} else if (value == "0225005") {
			dicArray = [ SWI.util.dicByCode['0207003'] ];
			SWI.util.addDicComboOptions(dicArray, that.payTypeCombo);
			that.payTypeCombo.selectOption(0);
			form.setItemValue('cardCode', '');
			form.setItemValue('cardBalance', 0);
		}
		that.updateReceivable();
	});

	this.initData();
}

/**
 * 初始化数据
 */
SWI.venue.orderGoods.prototype.initData = function() {
	this.cardDiscount = 1;
	this.cardBalance = 0;

	this.form.setItemValue('cardCode', '');
	this.form.setItemValue('cardBalance', 0);
	this.fillPayTypeCombo.selectOption(0);
	this.strategyCombo.selectOption(0);
	this.seatCombo.unSelectOption();
	if (this.salesmanCombo.getOptionsCount() == 1) {
		this.salesmanCombo.selectOption(0);
	} else {
		this.salesmanCombo.unSelectOption();
	}
	this.chooseGrid.clearAll();
	this.form.setItemValue("receivable", 0);
	this.form.setItemValue("realPay", 0);
	this.form.setItemValue('fill', 0);
}

/**
 * 更新实收、应收金额
 */
SWI.venue.orderGoods.prototype.updateReceivable = function() {
	var sum = 0;
	var grid = this.chooseGrid;
	grid.forEachRow(function(id) {
		sum += parseFloat(grid.cells(id, 4).getValue());
	});
	this.form.setItemValue("receivable", sum);

	// 计算实收金额
	var strategy = this.strategyCombo.getSelectedValue();
	var payType = this.payTypeCombo.getSelectedValue();
	var realPay = this.form.getItemValue("realPay");
	var realPayMoney = SWI.util.calculateRealPay(strategy, payType, sum, this.cardDiscount);
	this.form.setItemValue('realPay', realPayMoney);
	if (strategy == '0225002' && (this.cardBalance < realPayMoney)) {
		this.form.setItemValue('fill', realPayMoney - this.cardBalance);
	} else {
		this.form.setItemValue('fill', 0);
	}
}

$(document).ready(function() {
	SWI.util.adjustPageHeight();
	$(document.body).height(window.screen.height - 150);
	var orderGoods = new SWI.venue.orderGoods();
	orderGoods.initPage();
	SWI.util.adjustPageHeight();
});

/**
 * 确认充值卡号和密码的窗口
 */
SWI.venue.orderGoods.prototype.showCheckCard = function() {
	var that = this;
	if (!this.wins) {
		this.wins = new dhtmlXWindows();
	}
	var wins = this.wins;

	var windowWidth = 300;
	var windowHeight = 220;
	var cardWindow = wins.createWindow('memberCard', ($(document.body).width() - windowWidth) / 2, ($(document.body)
			.height() - windowHeight) / 2, windowWidth, windowHeight);
	cardWindow.setText('请输入卡号和密码');
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
			offsetLeft : 10
		}, {
			type : "input",
			label : "卡号",
			name : "cardCode",
			required : true
		}, {
			type : "password",
			label : "密码",
			name : "cardPassword",
			required : true
		} ]
	}, {
		type : "block",
		blockOffset : 0,
		list : [ {
			type : "button",
			value : "取消",
			name : 'cancel',
			offsetLeft : 80,
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
			var code = form.getItemValue('cardCode');
			var password = encrypt(form.getItemValue('cardPassword'));
			MemberDwr.checkPassword(code, password, function() {
				cardWindow.close();
				MemberDwr.getCardLevel(code, function(ret) {
					if (ret.venueDiscount) {
						that.cardDiscount = ret.venueDiscount;
						that.cardBalance = ret.balance;
						that.form.setItemValue("cardCode", code);
						that.form.setItemValue("cardBalance", that.cardBalance);
						dhtmlx.alert('该卡场内消费优惠 ' + (that.cardDiscount * 10) + ' 折，余额 ' + that.cardBalance + ' 元');
						that.updateReceivable();
					} else {
						dhtmlx.alert('该卡不能用于场内消费');
					}
				});
			});
		}
	});
}

/**
 * 保存订单
 */
SWI.venue.orderGoods.prototype.saveOrder = function() {
	var that = this;
	var status = '0227002';
	var salesmanId = this.salesmanCombo.getSelectedValue();
	var seatId = this.seatCombo.getSelectedValue();
	var payType = this.payTypeCombo.getSelectedValue();
	var code = this.form.getItemValue("cardCode");
	var balance = this.form.getItemValue('cardBalance');
	var strategy = this.strategyCombo.getSelectedValue();
	var receivable = this.form.getItemValue("receivable");
	var realPay = this.form.getItemValue("realPay");
	var fillPayType = this.fillPayTypeCombo.getSelectedValue();
	var fill = this.form.getItemValue('fill');
	if (code) {
		balance = balance - realPay + fill;
	}
	var details = [];
	var grid = this.chooseGrid;
	grid.forEachRow(function(id) {
		var data = [];
		data.push(id);
		data.push(grid.cells(id, 1).getValue());
		data.push(grid.cells(id, 2).getValue());
		data.push(grid.cells(id, 4).getValue());
		details.push(data);
	});
	VenueDwr.saveVenueSales(seatId, salesmanId, receivable, realPay, payType, code, strategy, null, null, status, fill,
			fillPayType, details, null, function(salesId) {
				// 下单时打印小票
				if (status == '0227002') {
					VenueDwr.getVenueSales(salesId, function(re) {
						SWI.util
								.printReceipt(SWI.util.dicByCode['0104001'].text, re.saleNo, re.createTime,
										that.payTypeCombo.getSelectedText(), realPay, receivable, that.salesmanCombo
												.getSelectedText(), SWI.currentUserName, that.seatCombo
												.getSelectedText(), grid, code, balance);
						that.initData();
					});
				}
			});
	return true;
}
