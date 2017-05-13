genDicJs([ '0204' ]);
var userMap = genMaps("from User order by name,id");
var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
var formSetting = {
	po : 'TicketSale',
	type : "query",
	hiddens : [ "id" ],
	fields : [ [ {
		title : "订座日期",
		type : "date",
		name : "createTime"
	}, {
		title : "客户手机",
		type : "text",
		name : "memberMobile"
	}, {
		title : "客户姓名",
		type : "text",
		name : "memberName"
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
	if (createTime) {
		param.createTime = createTime;
	}
	var memberMobile = queryForm.find(":input[name=memberMobile]").val();
	if (memberMobile) {
		param.memberMobile = memberMobile;
	}
	var memberName = queryForm.find(":input[name=memberName]").val();
	if (memberName) {
		param.memberName = memberName;
	}

	SWI.util.showReport(param, "/public/stat/queryOrderSeat.htmls");

	// 合并相同的台号、排号
	$('#list table td').each(function(i, td) {
		var vs = $(td).html().split(',');
		var tp = {};
		if (vs.length > 1) {
			$(vs).each(function(ind, value) {
				var index = value.indexOf('台');
				if (index < 0) {
					index = value.indexOf('排');
				}
				if (index >= 0) {
					var name = value.substring(0, index + 1);
					var number = value.substring(index + 1, value.length - 1);
					if (!tp[name]) {
						tp[name] = [];
					}
					tp[name].push(number);
				}
			});

			var nv = '';
			$.each(tp, function(key, seats) {
				if (nv) {
					nv += '； ';
				}
				nv += key + seats.sort().join(',') + '号';
			});
			$(td).html(nv);
		}
	});
}

var form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());

	queryData(event, $('#formview form'));
});
