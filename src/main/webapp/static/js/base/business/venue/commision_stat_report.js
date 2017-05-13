genDicJs([ '0204' ]);
var userMap = genMaps("from User order by name,id");
var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
var formSetting = {
	po : 'TicketSale',
	type : "query",
	hiddens : [ "id" ],
	fields : [ [ {
		title : "统计日期",
		type : "datescale",
		name : "createTime"
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
	param.createTime = createTime;
	param.createTime2 = createTime2;

	SWI.util.showReport(param, "/public/venuestat/queryCommisionStat.htmls");
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