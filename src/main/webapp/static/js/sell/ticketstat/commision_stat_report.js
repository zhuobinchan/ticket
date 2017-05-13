var deptMap = genMaps("from Organization where tier = 2 order by orderno");
var formSetting = {
	po : 'TicketSale',
	type : "query",
	hiddens : [ "id" ],
	fields : [ [ {
		title : '部门',
		type : 'select',
		name : 'dept',
		map : deptMap
	}, {
		title : "售票日期",
		type : "datescale",
		name : "createTime"
	}, {
		title : "演出日期",
		type : "datescale",
		name : "playDate"
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
			id : 'print',
			title : '打印',
			handlers : {
				click : function() {
					SWI.util.printReport($('#formview_createTime').val(), $('#formview_createTime_2').val());
				}
			}
		}, {
			id : 'excel',
			title : '导出',
			handlers : {
				click : function() {
					SWI.util.exportExcelReport();
				}
			}
		} ]
	}
}

var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, queryForm) {
	var param = {};
	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
	if (createTime) {
		param.createTime = createTime;
	}
	if (createTime2) {
		param.createTime2 = createTime2;
	}
	var playDate = queryForm.find(":input[name=playDate]:eq(0)").val();
	var playDate2 = queryForm.find(":input[name=playDate]:eq(1)").val();
	if (playDate) {
		param.playDate = playDate;
	}
	if (playDate2) {
		param.playDate2 = playDate2;
	}
	var dept = queryForm.find(":input[name=dept]").val();
	if (dept) {
		param.dept = dept;
	}

	SWI.util.showReport(param, "/public/stat/querySalesmanCommissionStat2.htmls");
}

var form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
	queryData(event, $('#formview form'));
	// 按钮权限
	var forbitBtns = $.parseJSON('${forbitBtns}');
	for (var i = 0; i < forbitBtns.length; i++) {
		$("#" + forbitBtns[i]).hide();
	}
});