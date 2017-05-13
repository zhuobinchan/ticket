genDicJs([ '0228', '0229' ]);
var formSetting = {
	po : 'VenueCommision',
	hiddens : [ 'id' ],
	fields : [ [ {
		title : "员工类别",
		type : "select",
		name : "type",
		dic : '0228'
	}, {
		title : "商品提成比例",
		type : "select",
		name : "goodsCommision",
		dic : '0229'
	}, {
		title : "提成比例",
		type : "text",
		name : "commision"
	} ] ]
}

var gridSetting = {
	id : 'VenueCommisionGrid',
	po : "VenueCommision",
	title : '会员卡类别',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : -1, // auto表示根据容器高度自动判断
		orderby : "goodsCommision, type"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'type',
		descs : "员工类别",
		align : 'center',
		type : 'select',
		width : 120
	}, {
		name : 'goodsCommision',
		descs : "商品提成比例",
		align : 'center',
		type : 'select',
		width : 120
	}, {
		name : 'commision',
		descs : "提成比例",
		align : 'center',
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
							VenueDwr.deleteVenueCommision(ids, function(ret) {
								layer.alert("删除场内消费提成设置记录成功 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
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
}

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	grid.load();
	grid.rowClick(function(id) {
		if (id) {
			form.loadData(id, "VenueCommision");
		} else {
			setFormDefault($(this));
		}
	});

});