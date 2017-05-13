genDicJs([ '0102', '0204', '0207', '0217', '0219', '0220' ]);
var userMap = genMaps("from User order by name,id");
var salesMap = genMaps("from Salesman order by name,id");
var formSetting = {
	po : 'MemberCardRechargeView',
	type : "query",
	fields : [ [ {
		title : "操作员",
		type : "select",
		name : "userId",
		map : userMap
	}, {
		title : "营销员",
		type : "autocomplete",
		name : "salesmanId"
	}, {
		title : "充值日期",
		type : "datescale",
		name : "createTime"
	} ], [ {
		title : "充值方式",
		type : "select",
		name : "payType",
		dic : '0207'
	}, {
		title : "网点",
		type : "select",
		name : "branch",
		dic : '0220'
	}, {
		title : "卡号",
		type : 'text',
		name : "code"
	} ], [ {
		title : "会员姓名",
		type : "text",
		name : "memberName"
	}, {
		title : "会员手机",
		type : "text",
		name : "memberMobile"
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
		}, {
			id : 'export',
			title : '导出',
			handlers : {
				click : function(e, queryForm) {
					SWI.util.exportExcel4MemberCardRecharge(queryForm);
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'memberCardRechargeViewGrid',
	po : "MemberCardRechargeView",
	title : '充值历史记录',
	fixWidth : false,
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
		name : 'cardId',
		descs : "会员卡号",
		align : 'center',
		type : 'select',
		width : 120
	}, {
		name : 'memberName',
		descs : "会员姓名",
		align : 'center',
		width : 80
	}, {
		name : 'memberMobile',
		descs : "会员手机",
		align : 'center',
		width : 80
	}, {
		name : 'createTime',
		descs : "充值日期",
		size : 10,
		align : 'center',
		width : 100
	}, {
		name : 'rechargeAmount',
		descs : "充值金额",
		align : 'center',
		width : 80
	}, {
		name : 'payType',
		descs : "充值方式",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'branch',
		descs : "网点",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'salesmanId',
		descs : "营销员",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'userId',
		descs : "操作员",
		align : 'center',
		type : 'select',
		width : 80
	} ]
};
var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, form) {
	var param = SWI.util.formatFormParam(form);
	SWI.util.changeFormParam(param, 'memberName', 'like');
	SWI.util.changeFormParam(param, 'memberMobile', 'like');
	SWI.util.changeFormParam(param, 'code', 'like');
	grid.load({
		data : param
	});

	ajustHeight();
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	queryData(event, $('#formview form'));
	$('#formview_playDate').attr("onclick", "WdatePicker({onpicked:pickedFunc})")

	SWI.util.autocompleteSalesman('salesmanId');
});