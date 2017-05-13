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
					SWI.util.exportExcel4MemberCard(queryForm, '0216002');
				}
			}
		} ]
	}
}
var gridSetting = {
	id : 'memberCardViewGrid',
	po : "MemberCardView",
	title : '消费卡记录',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "updateTime desc, code"
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
							$('#fmNewConsumerCard').parent().show();
							$('#fmNewConsumerCard')[0].reset();
							$('#payType').val('0207003');
							layer.open({
								title : "消费卡发卡",
								type : 1,
								skin : 'layui-layer-lan', // 加上边框
								area : [ '400px', '300px' ], // 宽高
								content : $("#fmNewConsumerCard"),
								btn : [ "确定", "取消" ],
								yes : function(index, layero) {
									if ($('#fmNewConsumerCard').valid()) {
										var cardId = $('#cardId').val();
										var count = $('#count').val();
										var memberId = $('input:radio:checked[name="defaultMember"]').val();
										var money = $('#money').val();
										var payType = $('#payType').val();
										MemberDwr.newMemberCard(cardId, count, null, "0215003", "0216002", null,
												encrypt("123456"), null, memberId, money, payType, function(ret) {
													layer.close(index);
													layer.alert("消费卡成功发卡" + ret + " 张，失败 " + (count - ret) + " 张", {
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
								$('#payTypeRe').val('0207003');
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
											var payType = $('#payTypeRe').val();
											var ids = [];
											rows.each(function(i, v) {
												ids[i] = $(v).attr("recordId");
											})
											MemberDwr.rechargeMemberCard(ids, money, payType, null, function(ret) {
												layer.alert("消费卡成功充值 " + ret + " 张，失败 " + (rows.length - ret) + " 张", {
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
					id : 'merge',
					title : '合并',
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length >= 2) {
								layer.alert("请选择要1条修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								$('#fmMergeMemberCard').parent().show();
								$('#fmMergeMemberCard')[0].reset();
								if (rows.length == 1) {
									$('#receiveId').val(rows.find('td[name="code"]').attr('value'));
								}
								layer.open({
									title : '消费卡余额合并',
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '300px', '200px' ], // 宽高
									content : $("#fmMergeMemberCard"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmMergeMemberCard').valid()) {
											var receiveId = $('#receiveId').val();
											var sendId = $('#sendId').val();
											var money = $('#mergeMoney').val();
											MemberDwr.mergeConsumerCard(receiveId, sendId, money, function(ret) {
												layer.alert((ret == "1" ? "余额合并成功" : ret), {
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
											var reason = $('#reason').find('option:selected').text();
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
										// 虚拟会员不能修改资料
										if (oldMemberId == 1) {
											oldMemberId = null;
										} else {
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
					width : 50,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要删除的记录", {
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
		type : '0216002'
	});
	SWI.util.changeFormParam(param, 'memberName', 'like');
	SWI.util.changeFormParam(param, 'memberMobile', 'like');
	SWI.util.changeFormParam(param, 'code', 'like');
	grid.load({
		data : param
	});

	ajustHeight();
}

function pickedFunc() {
	$dp.$('formview_playDate_2').value = $dp.$('formview_playDate').value;
}

var grid, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	queryData(event, $('#formview form'));
	$('#formview_playDate').attr("onclick", "WdatePicker({onpicked:pickedFunc})")

	dicSelect(dic0219, 'reason');
	dicSelect(dic0102, 'memberGender');
	dicSelect(dic0217, 'memberIdcardType');
	dicSelect(dic0207, 'payType');
	dicSelect(dic0207, 'payTypeRe');
});