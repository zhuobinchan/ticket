genDicJs([ '0215', '0216', '0233' ]);
var formSetting = {
	po : 'MemberCardType',
	hiddens : [ 'id', 'createTime', 'updateTime', 'createUserId', 'updateUserId' ],
	fields : [ [ {
		title : "会员卡名称",
		type : "select",
		name : "name",
		dic : '0215'
	}, {
		title : "会员卡类别",
		type : "select",
		name : "type",
		dic : '0216'
	}, {
		title : "充值累积金额",
		type : "text",
		name : "cumulation"
	} ], [ {
		title : "购票折扣",
		type : "text",
		name : "ticketDiscount"
	}, {
		name : 'ticketReduce',
		title : "购票减免",
		type : 'text'
	}, {
		name : 'ticketAlgorithm',
		title : "购票优惠算法",
		type : 'text'
	} ], [ {
		name : 'ticketPointMethod',
		title : "购票积分方法",
		dic : '0233',
		type : 'select'
	}, {
		name : 'ticketPointRatio',
		title : "购票积分比率",
		type : 'text'
	}, {
		title : "场内消费折扣",
		type : "text",
		name : "venueDiscount"
	} ], [ {
		name : 'venueReduce',
		title : "场内消费减免",
		type : 'text'
	}, {
		name : 'venueAlgorithm',
		title : "场内消费优惠算法",
		type : 'text'
	}, {
		name : 'venuePointMethod',
		title : "场内消费积分方法",
		dic : '0233',
		type : 'select'
	} ], [ {
		name : 'venuePointRatio',
		title : "场内消费积分比率",
		type : 'text'
	}, {
		title : "备注",
		type : "text",
		name : "remark"
	} ] ]
}

var gridSetting = {
	id : 'memberCardTypeGrid',
	po : "MemberCardType",
	title : '会员卡类别',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : -1, // auto表示根据容器高度自动判断
		orderby : "type, name"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'name',
		descs : "会员卡名称",
		align : 'center',
		type : 'select',
		width : 120
	}, {
		name : 'type',
		descs : "会员卡类别",
		align : 'center',
		type : 'select',
		width : 120
	}, {
		name : 'cumulation',
		descs : "充值累积金额",
		align : 'center',
		width : 80
	}, {
		name : 'ticketDiscount',
		descs : "购票折扣",
		align : 'center',
		width : 80
	}, {
		name : 'ticketReduce',
		descs : "购票减免",
		align : 'center',
		width : 80
	}, {
		name : 'ticketPointMethod',
		descs : "购票积分方法",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'venueDiscount',
		descs : "场内消费折扣",
		align : 'center',
		width : 80
	}, {
		name : 'venueReduce',
		descs : "场内消费减免",
		align : 'center',
		width : 80
	}, {
		name : 'venuePointMethod',
		descs : "场内消费积分方法",
		align : 'center',
		type : 'select',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'add',
			title : '增加',
			handlers : {
				click : function() {
					var newRow = grid.newRow();
					setFormDefault(newRow);
				}
			}
		}, {
			id : 'save',
			title : '保存',
			handlers : {
				click : function() {
					// if (!form.getValue("code")) {
					// layer.msg("会员卡号不能为空");
					// return;
					// }
					form.setValue('updateTime', getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					form.setValue('updateUserId', currentUserId);
					form.save(function(ret) {
						if (ret == -1) {
							layer.alert("数据保存失败，请重试！");
						} else {
							layer.alert("数据已保存！");
							grid.reload(ret.id);
							ajustHeight();
						}

					});
				}
			}
		}, {
			id : 'delete',
			title : '删除',
			handlers : {
				click : function() {
					var rows = grid.getSelectedRows();
					if (rows.length === 0) {
						layer.msg("请选择要删除的记录", {
							icon : 2,
							title : '提示信息'
						})
					} else {
						layer.confirm("确定删除吗？", {
							offset : 100
						}, function() {
							var ids = [];
							rows.each(function(i, v) {
								ids[i] = $(v).attr("recordId");
							});
							MemberDwr.deleteMemberCardType(ids, function(ret) {
								layer.alert("删除会员卡类别设置记录成功 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
								grid.reload();
							});
						});
					}
				}
			}
		} ]
	}
};

var grid, form;

function setFormDefault(newRow) {
	form.reset();
	var now = getCurrentTime("yyyy-MM-dd HH:mm:ss");
	// grid.setCellHtml(newRow, "updateUserId", currentUserId);
	// grid.setCellHtml(newRow, "updateTime", now);
	form.setValue("createTime", now);
	form.setValue("createUserId", currentUserId);
	form.setValue('updateTime', now);
	form.setValue('updateUserId', currentUserId);
}

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	grid.load();
	grid.rowClick(function(id) {
		if (id) {
			form.loadData(id, "MemberCardType");
		} else {
			setFormDefault($(this));
		}
	});

});