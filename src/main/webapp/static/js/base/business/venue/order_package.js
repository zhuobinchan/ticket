genDicJs([ '0104', '0207', '0223', '0224', '0225' ]);
var userMap = genMaps("from User order by name,id");
var seatMap = genMaps("from Seat order by name,id");
var salesmanMap = genMaps("from Salesman order by name,id")
var packageMap = genMaps("from Packages where status = '0202001' order by name,id")
var formSetting = {
	po : 'VenueSales',
	type : "query",
	fields : [ [ {
		title : "座位",
		type : 'select',
		name : "seatId",
		id : "seatId",
		map : seatMap
	}, {
		title : '点单员',
		type : 'autocomplete',
		name : 'salesmanId',
		id : 'salesmanId',
		map : salesmanMap
	}, {
		title : '应收金额',
		type : 'text',
		name : 'receivable',
		id : 'receivable'
	} ], [ {
		title : '卡号',
		type : 'text',
		name : 'code',
		id : 'code'
	}, {
		title : '支付方式',
		type : 'select',
		name : 'payType',
		id : 'payType',
		dic : '0207'
	}, {
		title : '实收金额',
		type : 'text',
		name : 'realPay',
		id : 'realPay'
	} ], [ {
		title : '套餐',
		type : 'select',
		name : 'packageId',
		id : 'packageId',
		map : packageMap
	}, {
		title : '补差价支付方式',
		type : 'select',
		name : 'fillPayType',
		dic : '0207'
	}, {
		title : '补差价金额',
		type : 'text',
		name : 'fill',
		id : 'fill'
	} ] ],
	queryRow : {
		align : 'center',
		buttons : [ {
			id : 'order',
			title : '下单',
			handlers : {
				click : function(e, queryForm) {
					layer.confirm("确定下单吗？", function(index) {
						layer.close(index);
						if (saveOrder('0227002', $('#packageId').val()) == false)
							return;
						layer.alert('下单成功，开始下一单', function(ind) {
							layer.close(ind);
							form.reset();
							initialize();
						});
					});
				}
			}
		}, {
			id : 'resetBtn',
			title : '取消',
			handlers : {
				click : function() {
					layer.confirm("确定取消此订单吗？", function(index) {
						layer.close(index);
						if (saveOrder('0227004', $('#packageId').val()) == false)
							return;
						layer.alert('取消成功，开始下一单', function(ind) {
							layer.close(ind);
							form.reset();
							initialize();
						});
					});
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'packagesGrid',
	po : "VenueSalesDetail",
	title : '消费商品管理',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : -1, // auto表示根据容器高度自动判断
		orderby : "goodsId"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'goodsId',
		descs : "商品名称",
		type : 'select',
		align : 'center',
		width : 100
	}, {
		name : 'goodsCount',
		descs : "数量",
		align : 'center',
		width : 80
	}, {
		name : 'price',
		descs : "单价",
		align : 'center',
		width : 80
	}, {
		name : 'total',
		descs : "总价",
		align : 'center',
		width : 80
	} ]
};

var grid, form, currentSalesId, cardDiscount, cardBalance, packageRealPay;

/**
 * 初始化
 */
function initialize() {
	currentSalesId = null;
	cardDiscount = 1;
	cardBalance = 0;
	packageRealPay = 0;

	grid.load({
		data : {
			salesId : currentSalesId
		}
	});

	$('[name="receivable"]').val(0);
	$('[name="realPay"]').val(0);
	$('#fill').val(0);
	$('[name="payType"]').val("0207001");
	$('[name="fillPayType"]').val("0207001");
}

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();

	$('[name="packageId"]').attr('id', 'packageId');
	$('[name="payType"]').attr('id', 'payType');
	$('#packageId').change(function() {
		if (saveOrder('0227001', this.value) == false) {
			$(this).prop('selectedIndex', 0);
			return;
		} else {
			VenueDwr.getPackageInfo(this.value, function(ret) {
				packageRealPay = !ret.price ? 0 : ret.price;
				updateReceivable();
			});
		}
	});
	$('#payType').change(function() {
		if (this.value == "0207010") {
			showCheckCard();
		} else if (this.value == "0225001" || this.value == "0225004") {
			typeMap = genMaps("from Dic where code in ('0207001','0207002') order by orderno");
			$('#payType').empty();
			joinSelect4Dic(typeMap, 'payType');
			$('#payType').val("0207001");
			$('[name="code"]').val('');
		}
		updateReceivable();
	});

	SWI.util.autocompleteSalesman('salesmanId');
	$('#code').attr('readOnly', 'readOnly');
	$('#receivable').attr('readOnly', 'readOnly');
	$('#realPay').attr('readOnly', 'readOnly');
	$('#fill').attr('readOnly', 'readOnly');

	initialize();
});

function updateReceivable() {
	var totals = grid.table.find('td[name="total"]');
	var sum = 0;
	for (var i = 0; i < totals.length; i++) {
		sum += parseFloat(totals[i].innerText);
	}
	$('#receivable').val(sum);

	// 计算实收金额
	var payType = $('#payType').val();
	var strategy = payType == '0207003' ? '0225005' : '0225003';
	var realPayMoney = SWI.util.calculateRealPay(strategy, payType, packageRealPay, cardDiscount);
	$('#realPay').val(realPayMoney);
	if (payType == '0207010' && (cardBalance < realPayMoney)) {
		$('#fill').val(realPayMoney - cardBalance);
	} else {
		$('#fill').val(0);
	}
}

/**
 * 确认充值卡号和密码的窗口
 */
function showCheckCard() {
	$('#fmCheckCard').parent().show();
	$('#fmCheckCard')[0].reset();
	layer.open({
		title : "请输入卡号和密码",
		type : 1,
		skin : 'layui-layer-lan', // 加上边框
		area : [ '400px', '200px' ], // 宽高
		content : $("#fmCheckCard"),
		btn : [ "确定", "取消" ],
		yes : function(index, layero) {
			if ($('#fmCheckCard').valid()) {
				var code = $('#cardId').val();
				var password = encrypt($('#password').val());
				MemberDwr.checkPassword(code, password, function() {
					layer.close(index);
					MemberDwr.getCardLevel(code, function(ret) {
						if (ret.venueDiscount) {
							cardDiscount = ret.venueDiscount;
							cardBalance = ret.balance;
							$('[name="code"]').val(code);
							layer.alert('该卡场内消费优惠 ' + (cardDiscount * 10) + ' 折，余额 ' + ret.balance + ' 元', {
								icon : 1,
								title : '提示信息'
							});
							updateReceivable();
						} else {
							layer.alert('该卡不能用于场内消费', {
								icon : 1,
								title : '提示信息'
							});
						}
					});
					layer.alert('密码验证成功', {
						icon : 1,
						title : '提示信息'
					}, function(ind) {
						layer.close(ind);
						updateReceivable();
					});
				});
			}
		},
		end : function(ret) {
		}
	});
}

/**
 * 保存订单
 */
function saveOrder(status, packageId) {
	var salesmanId = $('[name="salesmanId"]').val();
	var seatId = $('[name="seatId"]').val();
	var payType = $('[name="payType"]').val();
	var code = $('[name="code"]').val();
	var receivable = $('[name="receivable"]').val();
	var realPay = parseFloat($('[name="realPay"]').val());
	var fillPayType = $('[name="fillPayType"]').val();
	var fill = parseFloat($('#fill').val());
	if (code) {
		cardBalance = cardBalance - realPay + fill;
	}
	if (!salesmanId) {
		layer.msg("请选择营销员");
		return false;
	}
	if (!payType) {
		layer.msg('请选择支付方式');
		return false;
	}
	var strategy = '0225003';
	if (payType == '0207003') {
		strategy = '0225005';
	}
	VenueDwr.saveVenueSales(seatId, salesmanId, receivable, realPay, payType, code, strategy, packageId, null, status,
			fill, fillPayType, null, currentSalesId, function(ret) {
				currentSalesId = ret;
				// 下单时打印小票
				if (status == '0227002') {
					VenueDwr.getVenueSales(currentSalesId, function(re) {
						SWI.util.printReceipt(dic0104['0104001'].text, re.saleNo, re.createTime, $('#payType').find(
								'option:selected').text(), realPay, receivable, $('[name="salesmanId_txt"]').val(),
								SWI.currentUserName, ($('#seatId').val() ? $('#seatId').find('option:selected').text()
										: ''), $('#list table tr'), code, cardBalance);
					});
				}

				VenueDwr.saveVenueSalesPackage(ret, packageId, function() {
					grid.load({
						data : {
							salesId : currentSalesId
						}
					});
					updateReceivable()
				});
			});
	return true;
}