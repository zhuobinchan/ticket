genDicJs([ '0102', '0204', '0217', '0219' ]);
var userMap = genMaps("from User order by name,id");
var salesMap = genMaps("from Salesman order by name,id");
var formSetting = {
	po : 'MemberCardView',
	type : "query",
	fields : [ [ {
		title : "卡号",
		type : 'text',
		name : "code"
	}, {
		title : "营销员",
		type : "autocomplete",
		name : "salesmanId",
		map : salesMap
	}, {
		title : "发卡日期",
		type : "datescale",
		name : "createTime"
	} ], [ {
		title : "操作员",
		type : "select",
		name : "createUserId",
		map : userMap
	}, {
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
					SWI.util.exportExcel4MemberCard(queryForm, '0216001');
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'memberCardViewGrid',
	po : "MemberCardView",
	title : '会员卡记录',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "updateTime desc,code"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'code',
		descs : "会员卡号",
		align : 'center',
		width : 120
	}, {
		name : 'createTime',
		descs : "发卡日期",
		size : 10,
		align : 'center',
		width : 100
	}, {
		name : 'memberId',
		descs : "会员姓名",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'memberMobile',
		descs : "会员手机",
		align : 'center',
		width : 80
	}, {
		name : 'memberGender',
		descs : "会员性别",
		align : 'center',
		type : 'select',
		width : 50
	}, {
		name : 'name',
		descs : "卡名称",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'status',
		descs : "卡状态",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'createUserId',
		descs : "操作员",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'salesmanId',
		descs : "营销员",
		width : 120,
		type : 'select',
		align : 'center'
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'new',
					title : '发卡',
					width : 100,
					handlers : {
						click : function() {
							$('#fmNewMemberCard').parent().show();
							$('#fmNewMemberCard')[0].reset();
							layer.open({
								title : "会员卡发卡",
								type : 1,
								skin : 'layui-layer-lan', // 加上边框
								area : [ '400px', '200px' ], // 宽高
								content : $("#fmNewMemberCard"),
								btn : [ "确定", "取消" ],
								yes : function(index, layero) {
									if ($('#fmNewMemberCard').valid()) {
										var cardId = $('#cardId').val();
										var count = $('#count').val();
										var salesmanId = $('#cardSalesmanId').val();
										MemberDwr.newMemberCard(cardId, count, salesmanId, "0215001", "0216001", null,
												encrypt("123456"), null, null, null, null, function(ret) {
													layer.close(index);
													layer.alert("会员卡成功发卡 " + ret + " 张，失败 " + (count - ret) + " 张", {
														icon : 1,
														title : '提示信息'
													})
													grid.reload();
												});
									}
								},
								end : function(ret) {
								}
							});
						}
					}
				},
				{
					id : 'modify',
					title : '更换营销员',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.msg("请选择要修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								$('#fmMemberCardChangeSalesman').parent().show();
								$('#fmMemberCardChangeSalesman')[0].reset();
								layer.open({
									title : '更新营销员',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '200px' ], // 宽高
									content : $("#fmMemberCardChangeSalesman"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmMemberCardChangeSalesman').valid()) {
											var salesmanId = $('#newSalesmanId').val();
											var ids = [];
											rows.each(function(i, v) {
												ids[i] = $(v).attr("recordId");
											})
											MemberDwr.changeSalesman(ids, salesmanId, function(ret) {
												layer.alert(
														"会员卡成功更换营销员 " + ret + " 张，失败 " + (rows.length - ret) + " 张", {
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
				},
				{
					id : 'memberInfo',
					title : '会员资料',
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
								$('#fmMemberInfo').parent().show();
								$('#fmMemberInfo')[0].reset();
								if (rows.length == 1) {
									$('#memberCardId').val(rows.find('td[name="code"]').attr('value'));
									var oldMemberId = rows.find('td[name="memberId"]').attr('value');
									if (oldMemberId) {
										MemberDwr.getMemberInfo(oldMemberId, function(ret) {
											$("#memberName").val(ret.name);
											$('#memberGender').val(ret.gender);
											$("#memberMobileno").val(ret.mobile);
											$("#memberTelephone").val(ret.telephone);
											$("#memberEmail").val(ret.email);
											$("#memberAddress").val(ret.address);
											$("#memberBirthday").val(ret.birthday);
											$("#memberCompany").val(ret.company);
											$('#memberIdcardType').val(ret.idType);
											$("#memberIdcardNo").val(ret.idCard);
											$("#memberHobby").val(ret.hobby);
											$("#memberRemark").val(ret.remark);
										});
									}
								}
								layer.open({
									title : '录入会员资料',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '450px' ], // 宽高
									content : $("#fmMemberInfo"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmMemberInfo').valid()) {
											var cardId = rows.attr("recordId");
											var name = $("#memberName").val();
											var gender = $('#memberGender').val();
											var mobile = $("#memberMobileno").val();
											var telephone = $("#memberTelephone").val();
											var email = $("#memberEmail").val();
											var address = $("#memberAddress").val();
											var birthday = $("#memberBirthday").val();
											var company = $("#memberCompany").val();
											var idType = $('#memberIdcardType').val();
											var idCard = $("#memberIdcardNo").val();
											var hobby = $("#memberHobby").val();
											var remark = $("#memberRemark").val();
											var memberId = oldMemberId;
											MemberDwr.createMember(cardId, name, gender, mobile, telephone, email,
													address, birthday, company, idType, idCard, hobby, remark,
													memberId, function(ret) {
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
				}, {
					id : 'delete',
					title : '删除',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.msg("请选择要删除的记录", {
									icon : 2,
									title : '提示信息'
								});
							} else {
								layer.confirm("确定删除吗？只能删除没有关联会员资料的卡片。", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									MemberDwr.deleteMemberCard(ids, function(ret) {
										layer.alert("删除会员卡成功 " + ret + " 张，失败 " + (rows.length - ret) + " 张");
										grid.reload();
									});
								});
							}
						}
					}
				} ]
	}
};
var roleString = '${sessionScope.roleString}';
var userId = '${sessionScope.userInfo.id}';

function getParam(form) {
	var param = SWI.util.formatFormParam(form);
	param.push({
		type : '0216001'
	});
	SWI.util.changeFormParam(param, 'memberName', 'like');
	SWI.util.changeFormParam(param, 'memberMobile', 'like');
	SWI.util.changeFormParam(param, 'code', 'like');
	
	return param;
}

function queryData(e, form) {
	grid.load({
		data : getParam(form)
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

	dicSelect(dic0102, 'memberGender');
	dicSelect(dic0217, 'memberIdcardType');

	SWI.util.autocompleteSalesman('salesmanId');
	SWI.util.autocompleteSalesman('cardSalesmanId');
	SWI.util.autocompleteSalesman('newSalesmanId');
});