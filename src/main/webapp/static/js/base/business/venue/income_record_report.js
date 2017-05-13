genDicJs([ '0207', '0223', '0224', '0225', '0227' ]);
var userMap = genMaps("from User order by name,id");
var seatMap = genMaps("from Seat order by name,id");
var salesmanMap = genMaps("from Salesman order by name,id")
var formSetting = {
	po : 'VenueSales',
	type : "query",
	fields : [ [ {
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
	title : '收入流水',
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
		name : 'createTime',
		descs : "下单时间",
		align : 'center',
		type : 'datetime',
		width : 80
	} ]
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
		}, {
			name : 'remark',
			descs : "备注",
			align : 'center',
			width : 80
		} ]
	};

var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, form) {
	var param = SWI.util.formatFormParam(form);
	param.push({
		r : 'in',
		name : 'status',
		value : "0227002,0227003"
	});
	grid.load({
		data : param
	});
	grid2.removeAll();

	ajustHeight();
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
	grid = new GridView("list", gridSetting, "list");
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

});
