genDicJs([ '0202', '0207', '0217', '0226' ]);
var userMap = genMaps("from User order by name,id");
var formSetting = {
	po : 'MemberCardConsumerView',
	type : "query",
	fields : [ [ {
		title : "消费场所",
		type : "select",
		name : "branch",
		dic : '0226'
	}, {
		name : 'saleNo',
		title : "销售编号",
		type : 'text'
	}, {
		title : "经办人",
		type : "select",
		name : "updateUserId",
		map : userMap
	} ], [ {
		title : "消费日期",
		type : "datescale",
		name : "createTime"
	}, {
		title : "卡号",
		type : 'text',
		name : "code"
	}, {
		title : "会员姓名",
		type : "text",
		name : "memberName"
	} ], [ {
		title : "会员手机",
		type : "text",
		name : "memberMobile"
	}, {
		title : "状态",
		type : 'select',
		name : "status",
		dic : '0202'
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
					SWI.util.exportExcel4MemberCardConsumer(queryForm);
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'consumerGrid',
	po : "MemberCardConsumerView",
	title : '消费流水记录',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "updateTime desc"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'saleNo',
		descs : "销售编号",
		align : 'center',
		width : 120
	}, {
		name : 'cardId',
		descs : "会员卡号",
		type : 'select',
		align : 'center',
		width : 100
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
		descs : "消费日期",
		type : 'datetime',
		align : 'center',
		width : 100
	}, {
		name : 'money',
		descs : "本次消费",
		align : 'center',
		width : 80
	}, {
		name : 'balance',
		descs : "本次余额",
		align : 'center',
		width : 80
	}, {
		name : 'branch',
		descs : "消费场所",
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
		name : 'updateUserId',
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

});