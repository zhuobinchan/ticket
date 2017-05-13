genDicJs([ '0104', '0207', '0223', '0224', '0225' ]);
var userMap = genMaps("from User order by name,id");
var seatMap = genMaps("from Seat order by name,id");
var salesmanMap = genMaps("from Salesman order by name,id")
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
		name : 'salesmanId'
	}, {
		title : '应收金额',
		type : 'text',
		name : 'receivable',
		id : 'receivable'
	} ], [ {
		title : '优惠策略',
		type : 'select',
		name : 'strategy',
		dic : '0225'
	}, {
		title : '支付方式',
		type : 'select',
		name : 'payType',
		dic : '0207'
	}, {
		title : '实收金额',
		type : 'text',
		name : 'realPay',
		id : 'realPay'
	} ], [ {
		title : '卡号',
		type : 'text',
		name : 'code',
		id : 'code'
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
						if (saveOrder('0227002') == false)
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
						if (saveOrder('0227004') == false)
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
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'new',
					title : '新增商品',
					width : 100,
					handlers : {
						click : function() {
							if (!currentSalesId) {
								if (saveOrder('0227001') == false)
									return;
							}
							$('#fmVenueSalesDetail').parent().show();
							$('#fmVenueSalesDetail')[0].reset();
							layer.open({
								title : "新增商品",
								type : 1,
								skin : 'layui-layer-lan', // 加上边框
								area : [ '400px', '300px' ], // 宽高
								content : $("#fmVenueSalesDetail"),
								btn : [ "确定", "取消" ],
								yes : function(index, layero) {
									if ($('#fmVenueSalesDetail').valid()) {
										var goodsId = $('#goodsId').val();
										var count = $('#count').val();
										var price = $('#price').val();
										var total = $('#total').val();
										var remark = $('#pgRemark').val();
										VenueDwr.saveVenueSalesDetail(currentSalesId, goodsId, count, price, total,
												remark, null, function(ret) {
													layer.close(index);
													layer.msg('新增商品消费成功', {
														icon : 1,
														title : '提示信息'
													}, function(ind) {
														layer.close(ind);
														updateReceivable();
													});
													grid.load({
														data : {
															salesId : currentSalesId
														}
													});
												});
									}
								},
								end : function(ret) {
								}
							});
						}
					}
				},
				{
					id : 'modify',
					title : '修改商品',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length != 1) {
								layer.alert("请仅选择要1条修改的记录", {
									icon : 2,
									title : '提示信息'
								});
							} else {
								$('#fmVenueSalesDetail').parent().show();
								$('#fmVenueSalesDetail')[0].reset();
								var pgId = rows.attr("recordId");
								if (pgId) {
									VenueDwr.getVenueSalesDetail(pgId, function(ret) {
										$('#bigId').val(ret.bigId)
										$('#bigId').change();
										$('#smallId').val(ret.smallId);
										$('#smallId').change();
										$('#goodsId').val(ret.goodsId);
										$('#count').val(ret.count);
										$('#price').val(ret.price);
										$('#total').val(ret.total);
										$('#pgRemark').val(ret.remark);
									});
								}
								layer.open({
									title : "修改商品",
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '400px', '300px' ], // 宽高
									content : $("#fmVenueSalesDetail"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmVenueSalesDetail').valid()) {
											var goodsId = $('#goodsId').val();
											var count = $('#count').val();
											var price = $('#price').val();
											var total = $('#total').val();
											var remark = $('#pgRemark').val();
											VenueDwr.saveVenueSalesDetail(currentSalesId, goodsId, count, price, total,
													remark, pgId, function(ret) {
														layer.close(index);
														layer.alert('修改商品消费成功', {
															icon : 1,
															title : '提示信息'
														}, function(ind) {
															layer.close(ind);
															updateReceivable();
														});
														grid.load({
															data : {
																salesId : currentSalesId
															}
														});
													});
										}
									},
									end : function(ret) {
									}
								});
							}
						}
					}
				},
				{
					id : 'delete',
					title : '删除商品',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定删除吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									VenueDwr.deleteVenueSalesDetail(ids, function(ret) {
										layer.alert("成功删除商品消费 " + ret + " 个，失败 " + (rows.length - ret) + " 个",
												function(ind) {
													layer.close(ind);
													updateReceivable();
												});
										grid.load({
											data : {
												salesId : currentSalesId
											}
										});
									});
								});
							}
						}
					}
				} ]
	}
};

var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, queryForm) {
	var param = {};
	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
	var name = queryForm.find(":input[name=name]").val();
	var id = queryForm.find(":input[name=id]").val();
	if (createTime === "") {
		layer.msg("请限定发卡日期")
		return;
	}
	var data = [ {
		salesId : null
	} ];
	param.type = "0216002";
	/*
	 * 暂时不明白 if (roleString.indexOf("financial") == -1) { data.push({ createUserId : userId }); param.createUserId =
	 * userId; } else {
	 */

	if (name != '') {
		data.push({
			r : "like",
			name : "name",
			value : "%" + name + "%"
		});
		param.name = name;
	}

	grid.load({
		data : data
	});

	ajustHeight();
}

var grid, form, currentSalesId, cardDiscount, cardBalance;

/**
 * 初始化
 */
function initialize() {
	currentSalesId = null;
	cardDiscount = 1;
	cardBalance = 0;

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
	$('[name="strategy"]').val("0225004");
	$('[name="strategy"]').change();
}

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();

	var bigMap = genMaps("from GoodsType where parentId = 0 order by orderno");
	joinSelect(bigMap, "bigId");
	$('#bigId').change(function() {
		var smallMap = genMaps("from GoodsType where parentId = " + this.value + " order by orderno");
		var smallValue = $('#smallId').val();
		$('#smallId').empty();
		joinSelect(smallMap, "smallId");
	});
	$('#smallId').change(function() {
		var goodsMap = genMaps("from Goods where typeId = " + this.value + " order by orderno");
		var goodsValue = $('#goodsId').val();
		$('#goodsId').empty();
		joinSelect(goodsMap, "goodsId");
	});
	$('#goodsId').change(function() {
		VenueDwr.getGoodsInfo(this.value, function(ret) {
			$('#price').val(ret.price);
			$('#total').val($('#price').val() * $('#count').val());
		});
	});
	$('#count').change(function() {
		$('#total').val($('#price').val() * $('#count').val());
	});

	$('[name="payType"]').attr('id', 'payType');
	$('[name="strategy"]').change(function() {
		if (this.value == "0225002") {
			var typeMap = genMaps("from Dic where code in ('0207010') order by orderno");
			$('#payType').empty();
			joinSelect4Dic(typeMap, 'payType');
			showCheckCard();
			$('#payType').val("0207010");
		} else if (this.value == "0225001" || this.value == "0225004") {
			typeMap = genMaps("from Dic where code in ('0207001','0207002') order by orderno");
			$('#payType').empty();
			joinSelect4Dic(typeMap, 'payType');
			$('#payType').val("0207001");
			$('[name="code"]').val('');
		} else if (this.value == "0225005") {
			typeMap = genMaps("from Dic where code in ('0207003') order by orderno");
			$('#payType').empty();
			joinSelect4Dic(typeMap, 'payType');
			$('#payType').val("0207003");
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
	$('[name="receivable"]').val(sum);

	// 计算实收金额
	var strategy = $('[name="strategy"]').val();
	var realPay = $('[name="realPay"]');
	var realPayMoney = SWI.util.calculateRealPay(strategy, $('#payType').val(), sum, cardDiscount);
	realPay.val(realPayMoney);
	if (strategy == '0225002' && (cardBalance < realPayMoney)) {
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
function saveOrder(status) {
	var salesmanId = $('[name="salesmanId"]').val();
	var seatId = $('[name="seatId"]').val();
	var payType = $('[name="payType"]').val();
	var code = $('[name="code"]').val();
	var strategy = $('[name="strategy"]').val();
	var receivable = $('[name="receivable"]').val();
	var realPay = $('[name="realPay"]').val();
	var fillPayType = $('[name="fillPayType"]').val();
	var fill = $('#fill').val();
	if (!salesmanId) {
		layer.msg("请选择营销员");
		return false;
	}
	if (!payType) {
		layer.msg('请选择支付方式');
		return false;
	}
	VenueDwr.saveVenueSales(seatId, salesmanId, receivable, realPay, payType, code, strategy, null, null, status, fill,
			fillPayType, currentSalesId, function(ret) {
				currentSalesId = ret;
				// 下单时打印小票
				if (status == '0227002') {
					VenueDwr.getVenueSales(currentSalesId, function(re) {
						SWI.util.printReceipt(dic0104['0104001'].text, re.saleNo, re.createTime, $('#payType').find(
								'option:selected').text(), realPay, receivable, $('[name="salesmanId_txt"]').val(),
								SWI.currentUserName, ($('#seatId').val() ? $('#seatId').find('option:selected').text()
										: ''), $('#list table tr'));
					});
				}
			});
	return true;
}
