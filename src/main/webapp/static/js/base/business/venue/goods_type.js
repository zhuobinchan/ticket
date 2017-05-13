genDicJs([ '0102', '0204', '0217', '0219' ]);
var bigMap = genMaps("from GoodsType where parentId = 0 order by orderno");
var formSetting = {
	po : 'GoodsType',
	type : "query",
	fields : [ [ {
		title : "大类",
		type : 'select',
		name : "parentId",
		id : 'formParentId',
		map : bigMap
	}, {
		title : "小类名称",
		type : 'text',
		name : "name"
	}, {
		title : "说明",
		type : "text",
		name : "text"
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
	id : 'goodsTypeGrid',
	po : "GoodsType",
	title : '商品类别',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "parentId, orderno"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'parentId',
		descs : "大类",
		align : 'center',
		type : 'select',
		width : 100
	}, {
		name : 'name',
		descs : "小类名称",
		align : 'center',
		width : 100
	}, {
		name : 'text',
		descs : "小类说明",
		align : 'center',
		width : 80
	}, {
		name : 'orderno',
		descs : "排序",
		align : 'center',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'newBigType',
			title : '新增大类',
			width : 100,
			handlers : {
				click : function() {
					$('#fmBigType').parent().show();
					$('#fmBigType')[0].reset();
					$('#bigOrder').parent().parent().hide();
					layer.open({
						title : "新增大类",
						type : 1,
						skin : 'layui-layer-lan', // 加上边框
						area : [ '400px', '150px' ], // 宽高
						content : $("#fmBigType"),
						btn : [ "确定", "取消" ],
						yes : function(index, layero) {
							if ($('#fmBigType').valid()) {
								var name = $('#bigName').val();
								VenueDwr.saveGoodsType(0, name, null, null, null, null, function(ret) {
									layer.close(index);
									layer.alert(ret, {
										icon : 1,
										title : '提示信息'
									});
									initialize();
									grid.reload();
								});
							}
						},
						end : function(ret) {
						}
					});
				}
			}
		}, {
			id : 'modifyBigType',
			title : '修改大类',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid.getSelectedRows();
					if (rows.length != 1) {
						layer.alert("请仅选择要1条修改的记录", {
							icon : 2,
							title : '提示信息'
						})
					} else {
						$('#fmBigType').parent().show();
						$('#fmBigType')[0].reset();
						$('#bigOrder').parent().parent().show();
						var typeId = rows.find('td[name="parentId"]').attr('value');
						if (typeId) {
							VenueDwr.getGoodsType(typeId, function(ret) {
								$('#bigName').val(ret.name);
								$('#bigOrder').val(ret.order);
							});
						}
						layer.open({
							title : "修改大类",
							type : 1,
							skin : 'layui-layer-lan', // 加上边框
							area : [ '400px', '150px' ], // 宽高
							content : $("#fmBigType"),
							btn : [ "确定", "取消" ],
							yes : function(index, layero) {
								if ($('#fmBigType').valid()) {
									var name = $('#bigName').val();
									var order = $('#bigOrder').val();
									VenueDwr.saveGoodsType(0, name, null, order, null, typeId, function(ret) {
										layer.close(index);
										layer.alert(ret, {
											icon : 1,
											title : '提示信息'
										});
										initialize();
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
			id : 'deleteBigType',
			title : '删除大类',
			width : 100,
			handlers : {
				click : function() {
					$('#fmDeleteBigType').parent().show();
					$('#fmDeleteBigType')[0].reset();
					layer.open({
						title : "删除大类",
						type : 1,
						skin : 'layui-layer-lan', // 加上边框
						area : [ '400px', '150px' ], // 宽高
						content : $("#fmDeleteBigType"),
						btn : [ "确定", "取消" ],
						yes : function(index, layero) {
							if ($('#fmDeleteBigType').valid()) {
								var typeId = $('#deleteBigTypeId').val();
								VenueDwr.deleteGoodsType([ typeId ], function(ret) {
									layer.close(index);
									layer.alert(ret ? "删除商品大类成功" : "删除商品大类失败");
									initialize();
									grid.reload();
								});
							}
						},
						end : function(ret) {
						}
					});
				}
			}
		}, {
			id : 'newSmallType',
			title : '新增小类',
			width : 100,
			handlers : {
				click : function() {
					$('#fmSmallType').parent().show();
					$('#fmSmallType')[0].reset();
					$('#smallOrder').parent().parent().hide();
					layer.open({
						title : "新增小类",
						type : 1,
						skin : 'layui-layer-lan', // 加上边框
						area : [ '400px', '230px' ], // 宽高
						content : $("#fmSmallType"),
						btn : [ "确定", "取消" ],
						yes : function(index, layero) {
							if ($('#fmSmallType').valid()) {
								var bigId = $('#bigTypeId').val();
								var name = $('#smallName').val();
								var text = $('#smallText').val();
								VenueDwr.saveGoodsType(bigId, name, text, null, null, null, function(ret) {
									layer.close(index);
									layer.alert(ret, {
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
		}, {
			id : 'modifySmallType',
			title : '修改小类',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid.getSelectedRows();
					if (rows.length != 1) {
						layer.alert("请仅选择要1条修改的记录", {
							icon : 2,
							title : '提示信息'
						})
					} else {
						$('#fmSmallType').parent().show();
						$('#fmSmallType')[0].reset();
						$('#smallOrder').parent().parent().show();
						var typeId = rows.attr("recordId");
						if (typeId) {
							VenueDwr.getGoodsType(typeId, function(ret) {
								$('#bigTypeId').val(ret.parentId);
								$('#smallName').val(ret.name);
								$('#smallText').val(ret.text);
								$('#smallOrder').val(ret.order);
							});
						}
						layer.open({
							title : "修改小类",
							type : 1,
							skin : 'layui-layer-lan', // 加上边框
							area : [ '400px', '230px' ], // 宽高
							content : $("#fmSmallType"),
							btn : [ "确定", "取消" ],
							yes : function(index, layero) {
								if ($('#fmSmallType').valid()) {
									var bigId = $('#bigTypeId').val();
									var name = $('#smallName').val();
									var text = $('#smallText').val();
									var order = $('#smallOrder').val();
									VenueDwr.saveGoodsType(bigId, name, text, order, null, typeId, function(ret) {
										layer.close(index);
										layer.alert(ret, {
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
			}
		}, {
			id : 'delete',
			title : '删除小类',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid.getSelectedRows();
					if (rows.length === 0) {
						layer.alert("请选择要删除的记录", {
							icon : 2,
							title : '提示信息'
						})
					} else {
						layer.confirm("确定删除吗？只能删除没有子类和商品的记录。", function() {
							var ids = [];
							rows.each(function(i, v) {
								ids[i] = $(v).attr("recordId");
							})
							VenueDwr.deleteGoodsType(ids, function(ret) {
								layer.alert("删除商品小类成功 " + ret + " 条，失败 " + (rows.length - ret) + " 条");
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
		status : '0202001'
	});
	param.push({
		r : '!=',
		name : 'parentId',
		value : 0
	});
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

	initialize();
});

function initialize() {
	bigMap = genMaps("from GoodsType where parentId = 0 order by orderno");
	$('#formParentId').empty();
	$('#bigTypeId').empty();
	$('#deleteBigTypeId').empty();
	joinSelect(bigMap, "formParentId");
	joinSelect(bigMap, "bigTypeId");
	joinSelect(bigMap, "deleteBigTypeId");
}