genDicJs([ '0202', '0207', '0216', '0217', '0220', '0226', '0232' ]);
var userMap = genMaps("from User order by name,id");
var dic02200226 = {};
$.each(dic0220, function(id, value) {
	dic02200226[id] = value;
});
$.each(dic0226, function(id, value) {
	dic02200226[id] = value;
});
var formSetting = {
	po : 'MemberCardPointView',
	type : "query",
	fields : [ [ {
		title : "积分场所",
		type : "select",
		name : "branch",
		dic : '02200226'
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
		title : "积分日期",
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
	}, {
		title : "卡类别",
		type : 'select',
		name : "cardType",
		dic : '0216'
	} ], [ {
		title : "积分类型",
		type : 'select',
		name : "type",
		dic : '0232'
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
	id : 'pointGrid',
	po : "MemberCardPointView",
	title : '积分记录',
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
		name : 'cardId',
		hide : true
	}, {
		name : 'type',
		descs : "类型",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'saleNo',
		descs : "销售编号",
		align : 'center',
		width : 120
	}, {
		name : 'code',
		descs : "会员卡号",
		align : 'center',
		width : 100
	}, {
		name : 'cardType',
		descs : "卡类别",
		align : 'center',
		type : 'select',
		width : 80
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
		descs : "积分日期",
		type : 'datetime',
		align : 'center',
		width : 100
	}, {
		name : 'point',
		descs : "本次积分",
		align : 'center',
		width : 80
	}, {
		name : 'balance',
		descs : "累计积分值",
		align : 'center',
		width : 80
	}, {
		name : 'branch',
		descs : "积分场所",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'content',
		descs : "兑换内容",
		align : 'center',
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
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'exchange',
					title : '兑换',
					width : 100,
					handlers : {
						click : function() {
							var cardCode = $('#formview_code').val();
							if (!cardCode) {
								dhtmlx.alert('兑换前，请查询该卡号的积分记录');
								return;
							}
							wins = new dhtmlXWindows();
							var windowWidth = 300;
							var windowHeight = 320;
							var cardWindow = wins.createWindow('exchangePoint',
									($(document.body).width() - windowWidth) / 2,
									($(document.body).height() - windowHeight) / 2, windowWidth, windowHeight);
							cardWindow.setText('请输入积分和内容');
							cardWindow.denyResize();
							cardWindow.denyMove();
							cardWindow.setModal(true);
							var form = cardWindow.attachForm();
							form.loadStruct([ {
								type : 'label',
								list : [ {
									type : "settings",
									position : "label-left",
									labelWidth : 80,
									inputWidth : 150,
									offsetLeft : 10
								}, {
									type : "input",
									label : "卡号",
									name : "cardCode",
									readonly : true
								}, {
									type : "input",
									label : "可兑换积分",
									name : "exchangeable",
									readonly : true
								}, {
									type : "input",
									label : "兑换积分",
									name : "point",
									validate : "greaterExchangeable",
									required : true
								}, {
									type : "input",
									label : "兑换内容",
									name : "content",
									required : true
								}, {
									type : "combo",
									label : "兑换场所",
									name : "branch",
									userdata : {
										dic : '0220'
									},
									required : true
								} ]
							}, {
								type : "block",
								blockOffset : 0,
								list : [ {
									type : "button",
									value : "取消",
									name : 'cancel',
									offsetLeft : 80,
									offsetTop : 20
								}, {
									type : "newcolumn"
								}, {
									type : "button",
									value : "确定",
									name : 'submit',
									offsetTop : 20
								} ]
							} ], 'json');
							greaterExchangeable = function(val) {
								return val <= form.getItemValue('exchangeable');
							}
							SWI.util.addOptionsToFormDicCombo(form);
							form.setFocusOnFirstActive();
							form.setItemValue('cardCode', cardCode);
							MemberDwr.getExchangeablePoint(cardCode, function(ep) {
								form.setItemValue('exchangeable', ep);
							});
							form.attachEvent('onButtonClick', function(name) {
								if (name === 'submit') {
									form.validate();
								} else if (name === 'cancel') {
									cardWindow.close();
								}
							});
							form.attachEvent('onAfterValidate', function(status) {
								if (status) {
									var point = form.getItemValue('point');
									var content = form.getItemValue('content');
									var branch = form.getItemValue('branch');
									MemberDwr.exchangePoint(cardCode, point, content, branch, null, function() {
										grid.reload();
										SWI.util.adjustPageHeight();
										cardWindow.close();
										dhtmlx.alert('积分兑换成功');
									});
								}
							});
						}
					}
				},
				{
					id : 'cancelExchange',
					title : '取消兑换',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length !== 1 || rows.eq(0).children('[name=type]').attr('value') == '0232001'
									|| rows.eq(0).children('[name=status]').attr('value') == '0202002') {
								dhtmlx.alert("请选择一条要取消兑换的记录，不能选择消费记录和无效记录");
							} else {
								var recordId = rows.eq(0).attr('recordid');
								dhtmlx.confirm('确定取消吗？', function(result) {
									if (result) {
										MemberDwr.cancelExchangePoint(recordId, function() {
											grid.reload();
											SWI.util.adjustPageHeight();
											dhtmlx.alert('取消积分兑换成功');
										});
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
var wins;
function queryData(e, form) {
	var param = SWI.util.formatFormParam(form);
	SWI.util.changeFormParam(param, 'memberName', 'like');
	SWI.util.changeFormParam(param, 'memberMobile', 'like');
	SWI.util.changeFormParam(param, 'code', 'like');
	grid.load({
		data : param
	});

	SWI.util.adjustPageHeight();
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	queryData(event, $('#formview form'));

});