genDicJs([ '0204' ]);
var userMap = genMaps("from User order by name,id");
var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
var formSetting = {
	po : 'TicketSale',
	type : "query",
	hiddens : [ "id" ],
	fields : [ [ {
		title : "消费日期",
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
					SWI.util.printReport($('#formview_createTime').val(), $('#formview_createTime_2').val(), 1);
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

	SWI.util.showReport(param, "/public/venuestat/queryPayTypeStat.htmls");
}

var form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());

	$('#order').append('<option value="name">名称</option>');
	$('#order').append('<option value="price">单价</option>');
	$('#order').append('<option value="count">数量</option>');
	$('#order').append('<option value="total">金额</option>');

	queryData(event, $('#formview form'));
});
