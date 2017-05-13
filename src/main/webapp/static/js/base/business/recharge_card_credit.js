genDicJs([ '0204', '0207', '0217', '0219', '0220', '0221' ]);
var userMap = genMaps("from User order by name,id");
var formSetting = {
	po : 'MemberCardCredit',
	type : "query",
	fields : [ [ {
		title : "挂账状态",
		type : 'select',
		name : "status",
		dic : '0221'
	}, {
		title : "操作员",
		type : "select",
		name : "userId",
		map : userMap
	}, {
		title : "充值日期",
		type : "datescale",
		name : "createTime"
	} ], [ {
		title : "挂账营销员",
		type : 'autocomplete',
		name : "salesmanId",
	}, {
		title : "还款营销员",
		type : 'autocomplete',
		name : "repaySalesmanId",
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
		} ]
	}
}
var gridSetting = {
	id : 'memberCardCreditGrid',
	po : "MemberCardCredit",
	title : '充值卡挂账记录',
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
		name : 'createTime',
		descs : "充值时间",
		size : 10,
		align : 'center',
		width : 100
	}, {
		name : 'rechargeAmount',
		descs : "充值金额",
		align : 'center',
		width : 80
	}, {
		name : 'creditAmount',
		descs : "挂账金额",
		align : 'center',
		width : 80
	}, {
		name : 'salesmanId',
		descs : "营销员",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'status',
		descs : "挂账状态",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'repayType',
		descs : "还款方式",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'repayBranch',
		descs : "还款网点",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'repayAmount',
		descs : "还款金额",
		align : 'center',
		width : 80
	}, {
		name : 'repayTime',
		descs : "还款时间",
		align : 'center',
		width : 80
	}, {
		name : 'repaySalesmanId',
		descs : "还款营销员",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'userId',
		descs : "操作员",
		align : 'center',
		type : 'select',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'recover',
			title : '收回',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid.getSelectedRows();
					if (rows.length != 1) {
						layer.alert("请仅选择要一张卡", {
							icon : 2,
							title : '提示信息'
						})
					} else {
						$('#fmRecoverCredit').parent().show();
						$('#fmRecoverCredit')[0].reset();
						layer.open({
							title : '收回挂账',
							type : 1,
							skin : 'layui-layer-lan', // 加上边框
							area : [ '300px', '230px' ], // 宽高
							content : $("#fmRecoverCredit"),
							btn : [ "确定", "取消" ],
							yes : function(index, layero) {
								if ($('#fmRecoverCredit').valid()) {
									var id = rows.attr("recordId");
									var money = $('#money').val();
									var payType = $('#payType').val();
									var branch = $('#branch').val()
									var salesmanId = $('#repaySalesmanId').val();
									MemberDwr.recoverCredit(id, money, payType, branch, salesmanId, function(ret) {
										layer.alert((ret), {
											icon : 1,
											title : '提示信息'
										})
										layer.close(index);
										grid.reload();
									});
								}
							},
							end : function(ret) {
							}
						});
					}
				}
			}
		} ]
	}
};

var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';
function queryData(e, form) {
	grid.load({
		data : SWI.util.formatFormParam(form)
	});

	ajustHeight();
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	$('select[name="status"]').val('0221001');
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	queryData(event, $('#formview form'));
	$('#formview_playDate').attr("onclick", "WdatePicker({onpicked:pickedFunc})")

	dicSelect(dic0207, 'payType');
	dicSelect(dic0220, 'branch');

	SWI.util.autocompleteSalesman('salesmanId');
	SWI.util.autocompleteSalesman('repaySalesmanId');
	SWI.util.autocompleteSalesman('repaySalesmanId4Layer');

});