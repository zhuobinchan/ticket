genDicJs([ '0222', '0223', '0224', '0229' ]);
var userMap = genMaps("from User order by name,id");
var typeMap = genMaps("from GoodsType where parentId != 0 order by orderno")
var formSetting = {
	po : 'Goods',
	type : "query",
	fields : [ [ {
		title : "商品类别",
		type : 'select',
		name : "typeId",
		map : typeMap
	}, {
		title : "商品名称",
		type : 'text',
		name : "name"
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
	id : 'goodsGrid',
	po : "Goods",
	title : '商品管理',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 15, // auto表示根据容器高度自动判断
		orderby : "typeId, orderno"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'typeId',
		descs : "商品类别",
		align : 'center',
		type : 'select',
		width : 100
	}, {
		name : 'name',
		descs : "商品名称",
		align : 'center',
		width : 100
	}, {
		name : 'unit',
		descs : "单位",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'price',
		descs : "单价",
		align : 'center',
		width : 80
	}, {
		name : 'discount',
		descs : "是否打折",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'code',
		descs : "编码",
		align : 'center',
		width : 80
	}, {
		name : 'commision',
		descs : "提成比例",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'dispatch',
		descs : "是否配送",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'process',
		descs : "是否加工",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'cost',
		descs : "成本",
		align : 'center',
		width : 80
	}, {
		name : 'area',
		descs : "出品区域",
		align : 'center',
		type : 'select',
		width : 80
	}, {
		name : 'barCode',
		descs : "条形码",
		align : 'center',
		width : 80
	}, {
		name : 'orderno',
		descs : "排序",
		align : 'center',
		width : 50
	} ],
	toolbar : {
		align : 'right',
		buttons : [
				{
					id : 'new',
					title : '新增',
					width : 100,
					handlers : {
						click : function() {
							$('#fmGoodsInfo').parent().show();
							$('#fmGoodsInfo')[0].reset();
							$('#order').parent().parent().hide();
							layer.open({
								title : "新增",
								type : 1,
								skin : 'layui-layer-lan', // 加上边框
								area : [ '400px', '530px' ], // 宽高
								content : $("#fmGoodsInfo"),
								btn : [ "确定", "取消" ],
								yes : function(index, layero) {
									if ($('#fmGoodsInfo').valid()) {
										var typeId = $('#smallId').val();
										var name = $('#name').val();
										var unit = $('#unit').val();
										var price = $('#price').val();
										var discount = $('#discount').val();
										var dispatch = $('#dispatch').val();
										var code = $('#code').val();
										var process = $('#process').val();
										var cost = $('#cost').val();
										var area = $('#area').val();
										var barCode = $('#barCode').val();
										var remark = $('#remark').val();
										var commision = $('#commision').val();
										VenueDwr.saveGoods(typeId, name, unit, price, discount, dispatch, code,
												process, cost, area, barCode, null, remark, commision, null, function(
														ret) {
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
				},
				{
					id : 'modify',
					title : '修改',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length != 1) {
								layer.alert("请仅选择要1条修改的记录", {
									icon : 2,
									title : '提示信息'
								});
							} else {
								$('#fmGoodsInfo').parent().show();
								$('#fmGoodsInfo')[0].reset();
								$('#order').parent().parent().show();
								var goodsId = rows.attr("recordId");
								if (goodsId) {
									VenueDwr.getGoodsInfo(goodsId, function(ret) {
										$('#bigId').val(ret.bigTypeId)
										$('#bigId').change();
										$('#smallId').val(ret.typeId);
										$('#name').val(ret.name);
										$('#unit').val(ret.unit);
										$('#price').val(ret.price);
										$('#discount').val(ret.discount);
										$('#dispatch').val(ret.dispatch);
										$('#code').val(ret.code);
										$('#process').val(ret.process);
										$('#cost').val(ret.cost);
										$('#area').val(ret.area);
										$('#barCode').val(ret.barCode);
										$('#order').val(ret.order);
										$('#remark').val(ret.remark);
										$('#commision').val(ret.commision);
									});
								}
								layer.open({
									title : "修改",
									type : 1,
									skin : 'layui-layer-lan', // 加上边框
									area : [ '400px', '530px' ], // 宽高
									content : $("#fmGoodsInfo"),
									btn : [ "确定", "取消" ],
									yes : function(index, layero) {
										if ($('#fmGoodsInfo').valid()) {
											var typeId = $('#smallId').val();
											var name = $('#name').val();
											var unit = $('#unit').val();
											var price = $('#price').val();
											var discount = $('#discount').val();
											var dispatch = $('#dispatch').val();
											var code = $('#code').val();
											var process = $('#process').val();
											var cost = $('#cost').val();
											var area = $('#area').val();
											var barCode = $('#barCode').val();
											var order = $('#order').val();
											var remark = $('#remark').val();
											var commision = $('#commision').val();
											VenueDwr.saveGoods(typeId, name, unit, price, discount, dispatch, code,
													process, cost, area, barCode, order, remark, commision, goodsId,
													function(ret) {
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
					title : '删除',
					width : 100,
					handlers : {
						click : function() {
							var rows = grid.getSelectedRows();
							if (rows.length === 0) {
								layer.alert("请选择要修改的记录", {
									icon : 2,
									title : '提示信息'
								})
							} else {
								layer.confirm("确定删除吗？", function() {
									var ids = [];
									rows.each(function(i, v) {
										ids[i] = $(v).attr("recordId");
									})
									VenueDwr.deleteGoods(ids, function(ret) {
										layer.alert("成功删除商品 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
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
function queryData(e, queryForm) {
	var param = {};
	var typeId = queryForm.find(":input[name=typeId]").val();
	var name = queryForm.find(":input[name=name]").val();
	var data = [ {
		status : "0202001"
	} ];
	param.type = "0216002";
	/*
	 * 暂时不明白 if (roleString.indexOf("financial") == -1) { data.push({ createUserId : userId }); param.createUserId =
	 * userId; } else {
	 */
	if (name != '') {
		data.push({
			r : "like",
			name : "name",
			value : "%" + name + "%"
		});
		param.name = name;
	}
	if (typeId) {
		data.push({
			typeId : typeId
		});
	}

	grid.load({
		data : data
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

	dicSelect(dic0222, 'unit');
	dicSelect(dic0223, 'discount');
	dicSelect(dic0223, 'dispatch');
	dicSelect(dic0223, 'process');
	dicSelect(dic0224, 'area');
	dicSelect(dic0229, 'commision');

	var bigMap = genMaps("from GoodsType where parentId = 0 order by orderno");
	joinSelect(bigMap, "bigId");
	$('#bigId').change(function() {
		var smallMap = genMaps("from GoodsType where parentId = " + this.value + " order by orderno");
		var smallValue = $('#smallId').val();
		$('#smallId').empty();
		joinSelect(smallMap, "smallId");
	});
});