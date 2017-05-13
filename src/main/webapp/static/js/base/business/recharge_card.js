genDicJs([ '0102', '0204', '0207', '0217', '0219' ]);
var userMap = genMaps("from User order by name,id");
var formSetting = {
	po : 'MemberCardView',
	type : "query",
	fields : [ [ {
		title : "卡号",
		type : 'text',
		name : "code"
	}, {
		title : "操作员",
		type : "select",
		name : "createUserId",
		map : userMap
	}, {
		title : "发卡日期",
		type : "datescale",
		name : "createTime"
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
					SWI.util.exportExcel4MemberCard(queryForm, '0216003');
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'memberCardViewGrid',
	po : "MemberCardView",
	title : '充值卡记录',
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
		name : 'balance',
		descs : "卡余额",
		align : 'center',
		width : 80
	}, {
		name : 'lastRechargeWay',
		descs : "最后充值方式",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'createUserId',
		descs : "操作员",
		align : 'center',
		type : 'select',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'new',
					title : '发卡',
					width : 50,
					handlers : {
						click : function() {
							$('#fmNewRechargeCard').parent().show();
							$('#fmNewRechargeCard')[0].reset();
							layer.open({
								title : "充值卡发卡",
								type : 1,
								skin : 'layui-layer-lan', // 加上边框
								area : [ '400px', '300px' ], // 宽高
								content : $("#fmNewRechargeCard"),
								btn : [ "确定", "取消" ],
								yes : function(index, layero) {
									if ($('#fmNewRechargeCard').valid()) {
										var cardId = $('#cardId').val();
										var count = $('#count').val();
										MemberDwr.newMemberCard(cardId, count, null, "0215004", "0216003", null,
												encrypt("123456"), null, null, null, null, function(ret) {
													layer.close(index);
													layer.alert("充值卡成功发卡" + ret + " 张，失败 " + (count - ret) + " 张", {
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
					id : 'recharge',
					title : '充值',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								$('#fmMemberCardRecharge').parent().show();
								$('#fmMemberCardRecharge')[0].reset();
								layer.open({
									title : '充值',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '200px' ], // 宽高
									content : $("#fmMemberCardRecharge"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmMemberCardRecharge').valid()) {
											var money = $('#moneyRecharge').val();
											var payType = $('#payType').val();
											var salesmanId = $('#paySalesmanId').val();
											var ids = [];
											rows.each(function(i, v) {
												ids[i] = $(v).attr("recordId");
											})
											MemberDwr.rechargeMemberCard(ids, money, payType, salesmanId,
													function(ret) {
														layer.alert("充值卡成功充值 " + ret + " 张，失败 " + (rows.length - ret)
																+ " 张", {
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
					id : 'loss',
					title : '挂失',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要挂失的卡号", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定挂失吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									MemberDwr.changeCardStatus(ids, "0218003", "0218001", function(ret) {
										layer.alert("挂失会员卡成功 " + ret + " 张，失败 " + (rows.length - ret) + " 张");
										grid.reload();
									});
								});
							}
						}
					}
				},
				{
					id : 'resume',
					title : '恢复',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要恢复的卡号", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定恢复吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									MemberDwr.changeCardStatus(ids, "0218001", "0218003", function(ret) {
										layer.alert("恢复会员卡成功 " + ret + " 张，失败 " + (rows.length - ret) + " 张");
										grid.reload();
									});
								});
							}
						}
					}
				},
				{
					id : 'changePassword',
					title : '修改密码',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length != 1) {
								layer.alert("请仅选择要一张卡", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								$('#fmChangePassword').parent().show();
								$('#fmChangePassword')[0].reset();
								layer.open({
									title : '修改密码',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '200px' ], // 宽高
									content : $("#fmChangePassword"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmChangePassword').valid()) {
											var cardId = rows.attr("recordId");
											var oldPassword = $('#oldPassword').val();
											var newPassword = $('#newPassword').val();
											var confirmPassword = $('#confirmPassword').val()
											if (newPassword != confirmPassword) {
												layer.alert("新密码与确认密码不一致，请重新输入");
											} else {
												MemberDwr.changeCardPassword(cardId, encrypt(newPassword),
														encrypt(oldPassword), function(ret) {
															layer.alert((ret), {
																icon : 1,
																title : '提示信息'
															})
															layer.close(index);
															grid.reload();
														});
											}
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
					id : 'restore',
					title : '恢复密码',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要恢复的卡号", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定恢复初始密码吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									MemberDwr.restoreCardPassword(ids, encrypt("123456"), function(ret) {
										layer.alert("恢复初始密码成功 " + ret + " 张，失败 " + (rows.length - ret) + " 张");
										grid.reload();
									});
								});
							}
						}
					}
				},
				{
					id : 'changeCard',
					title : '换卡',
					width : 50,
					handlers : {
						click : function() {
							$('#fmChangeCard').parent().show();
							var rows = grid.getSelectedRows();
							if (rows.length != 1) {
								layer.alert("请仅选择要一张卡", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								$('#fmChangeCard').parent().show();
								$('#fmChangeCard')[0].reset();
								if (rows.length == 1) {
									$('#oldCardId').val(rows.find('td[name="code"]').attr('value'));
								}
								layer.open({
									title : '换卡',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '200px' ], // 宽高
									content : $("#fmChangeCard"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmChangeCard').valid()) {
											var oldCardId = $("#oldCardId").val();
											var newCardId = $("#newCardId").val();
											var reason = $('#tdReason select').find('option:selected').text();
											MemberDwr.changeCard(oldCardId, newCardId, reason, function(ret) {
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
				},
				{
					id : 'memberInfo',
					title : '会员资料',
					width : 50,
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
											MemberDwr.createMember(cardId, name, gender, mobile, telephone, email,
													address, birthday, company, idType, idCard, hobby, remark,
													oldMemberId, function(ret) {
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
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定删除吗？只能没有关联会员资料的卡片。", function() {
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
function queryData(e, form) {
	var param = SWI.util.formatFormParam(form);
	param.push({
		type : '0216003'
	});
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

	dicSelect(dic0207, 'payType');
	dicSelect(dic0219, 'reason');
	dicSelect(dic0102, 'memberGender');
	dicSelect(dic0217, 'memberIdcardType');

	SWI.util.autocompleteSalesman('paySalesmanId');
});