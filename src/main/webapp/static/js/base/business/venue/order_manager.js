genDicJs([ '0104', '0207', '0223', '0224', '0225', '0227' ]);
var userMap = genMaps("from User order by name,id");
var seatMap = genMaps("from Seat order by name,id");
var salesmanMap = genMaps("from Salesman order by name,id")
var formSetting = {
	po : 'VenueSales',
	type : "query",
	fields : [ [ {
		title : "销售编号",
		type : 'text',
		name : "saleNo",
		id : 'saleNo'
	}, {
		title : '点单员',
		type : 'autocomplete',
		name : 'salesmanId',
		id : 'salesmanId'
	}, {
		title : "座位",
		type : 'select',
		name : "seatId",
		id : 'seatId',
		map : seatMap
	} ], [ {
		title : '状态',
		type : 'select',
		name : 'status',
		id : 'status',
		dic : '0227'
	}, {
		title : '支付方式',
		type : 'select',
		name : 'payType',
		id : 'payType',
		dic : '0207'
	}, {
		title : '优惠策略',
		type : 'select',
		name : 'strategy',
		id : 'strategy',
		dic : '0225'
	} ], [ {
		title : '下单时间',
		type : 'datescale',
		name : 'createTime',
		id : 'createTime'
	} ] ],
	queryRow : {
		align : 'center',
		buttons : [ {
			id : 'order',
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
	id : 'salesGrid',
	po : "VenueSales",
	title : '场内消费单管理',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "id desc"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'saleNo',
		descs : "销售编号",
		align : 'center',
		width : 100
	}, {
		name : 'seatId',
		descs : "座位",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'salesmanId',
		descs : "点单员",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'receivable',
		descs : "应收金额",
		align : 'center',
		width : 80
	}, {
		name : 'realPay',
		descs : "实收金额",
		align : 'center',
		width : 80
	}, {
		name : 'payType',
		descs : "支付方式",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'status',
		descs : "状态",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'strategy',
		descs : "优惠策略",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'cardId',
		descs : "充值卡",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'fillDifference',
		descs : "补差价",
		align : 'center',
		width : 80
	}, {
		name : 'packageId',
		descs : "套餐",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'createTime',
		descs : "下单时间",
		align : 'center',
		type : 'datetime',
		width : 80
	}, {
		name : 'createUserId',
		descs : "经办人",
		align : 'center',
		type : 'select',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'cancel',
					title : '取消',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要取消的消费单", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定取消吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									VenueDwr.cancelVenueSales(ids, function(ret) {
										layer.alert("成功取消场内消费单 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
										queryData(event, $('#formview form'));
										$.each(ids, function(index, value) {
											VenueDwr.getVenueSales(value, function(re) {
												SWI.util.printReceipt(dic0104['0104001'].text, re.saleNo,
														re.createTime, '退款', re.realPay, re.receivable,
														re.salesmanName, re.createUserName, re.seatName,
														$('#list2 table tr'));
											});
										});
									});
								});
							}
						}
					}
				}, {
					id : 'settle',
					title : '结算',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要结算的消费单", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定结算吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									VenueDwr.settleVenueSales(ids, function(ret) {
										layer.alert("成功结算场内消费单 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
										queryData(event, $('#formview form'));
									});
								});
							}
						}
					}
				} ]
	}
};

var gridSetting2 = {
	id : 'detailGrid',
	po : "VenueSalesDetail",
	title : '场内消费关联商品',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 10, // auto表示根据容器高度自动判断
		orderby : "goodsId"
	},
	fields : [ {
		name : 'salesId',
		hide : true
	}, {
		name : 'goodsId',
		descs : "商品",
		align : 'center',
		type : 'select',
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

var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, form) {
	var param = {};
	var createTime = form.find(":input[name=createTime]:eq(0)").val();
	var createTime2 = form.find(":input[name=createTime]:eq(1)").val();
	if (createTime) {
		param.createTime = createTime;
	}
	if (createTime2) {
		param.createTime2 = createTime2;
	}

	SWI.util.showReport(param, "/public/venuestat/queryStatusStat.htmls");

	grid.load({
		data : SWI.util.formatFormParam(form)
	});
	// grid2.removeAll();

	ajustHeight();
}

var grid, grid2, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
	grid = new GridView("list1", gridSetting, "list1");
	grid.init();
	grid2 = new GridView("list2", gridSetting2, "list2");
	grid2.init();
	grid.rowClick(function(id) {
		grid2.load({
			data : {
				salesId : id
			}
		});
	});

	queryData(event, $('#formview form'));

	SWI.util.autocompleteSalesman('salesmanId');
});
