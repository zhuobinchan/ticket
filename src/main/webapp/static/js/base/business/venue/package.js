genDicJs([ '0222', '0223', '0224' ]);
var userMap = genMaps("from User order by name,id");
var typeMap = genMaps("from GoodsType where parentId != 0 order by orderno")
var formSetting = {
	po : 'Packages',
	type : "query",
	fields : [ [ {
		title : "名称",
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
	id : 'packagesGrid',
	po : "Packages",
	title : '套餐管理',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : 10, // auto表示根据容器高度自动判断
		orderby : "name, id"
	},
	fields : [ {
		name : 'id',
		hide : true
	}, {
		name : 'name',
		descs : "套餐名称",
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
		descs : "价格",
		align : 'center',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'new',
			title : '新增套餐',
			width : 100,
			handlers : {
				click : function() {
					$('#fmPackageInfo').parent().show();
					$('#fmPackageInfo')[0].reset();
					layer.open({
						title : "新增套餐",
						type : 1,
						skin : 'layui-layer-lan', // 加上边框
						area : [ '400px', '300px' ], // 宽高
						content : $("#fmPackageInfo"),
						btn : [ "确定", "取消" ],
						yes : function(index, layero) {
							if ($('#fmPackageInfo').valid()) {
								var name = $('#name').val();
								var unit = $('#unit').val();
								var price = $('#price').val();
								var remark = $('#remark').val();
								VenueDwr.savePackage(name, unit, price, remark, null, function(ret) {
									layer.close(index);
									layer.alert('新增套餐成功', {
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
			id : 'modify',
			title : '修改套餐',
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
						$('#fmPackageInfo').parent().show();
						$('#fmPackageInfo')[0].reset();
						var packId = rows.attr("recordId");
						if (packId) {
							VenueDwr.getPackageInfo(packId, function(ret) {
								$('#name').val(ret.name);
								$('#unit').val(ret.unit);
								$('#price').val(ret.price);
								$('#remark').val(ret.remark);
							});
						}
						layer.open({
							title : "修改套餐",
							type : 1,
							skin : 'layui-layer-lan', // 加上边框
							area : [ '400px', '300px' ], // 宽高
							content : $("#fmPackageInfo"),
							btn : [ "确定", "取消" ],
							yes : function(index, layero) {
								if ($('#fmPackageInfo').valid()) {
									var name = $('#name').val();
									var unit = $('#unit').val();
									var price = $('#price').val();
									var remark = $('#remark').val();
									VenueDwr.savePackage(name, unit, price, remark, packId, function(ret) {
										layer.close(index);
										layer.alert('修改套餐成功', {
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
			title : '删除套餐',
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
							VenueDwr.deletePackage(ids, function(ret) {
								layer.alert("成功删除套餐 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
								grid.reload();
							});
						});
					}
				}
			}
		} ]
	}
};

var gridSetting2 = {
	id : 'packageGoodsViewGrid',
	po : "PackageGoodsView",
	title : '套餐商品关联管理',
	fixWidth : false,
	height : 'auto',
	className : "mtable",
	service : "uiServices!listSimple",
	pageInfo : {
		records : -1, // auto表示根据容器高度自动判断
		orderby : "goodsId"
	},
	fields : [ {
		name : 'packageId',
		hide : true
	}, {
		name : 'goodsName',
		descs : "商品",
		align : 'center',
		width : 100
	}, {
		name : 'goodsPrice',
		descs : "商品单价",
		align : 'center',
		width : 100
	}, {
		name : 'goodsCount',
		descs : "商品数量",
		align : 'center',
		width : 80
	}, {
		name : 'goodsTotal',
		descs : "商品总价",
		align : 'center',
		width : 100
	}, {
		name : 'remark',
		descs : "备注",
		align : 'center',
		width : 80
	} ],
	toolbar : {
		align : 'right',
		buttons : [ {
			id : 'new',
			title : '新增商品',
			width : 100,
			handlers : {
				click : function() {
					$('#fmPackageGoods').parent().show();
					$('#fmPackageGoods')[0].reset();
					layer.open({
						title : "新增商品",
						type : 1,
						skin : 'layui-layer-lan', // 加上边框
						area : [ '400px', '300px' ], // 宽高
						content : $("#fmPackageGoods"),
						btn : [ "确定", "取消" ],
						yes : function(index, layero) {
							if ($('#fmPackageGoods').valid()) {
								var packageId = grid2.settings.data.packageId;
								var goodsId = $('#goodsId').val();
								var count = $('#count').val();
								var remark = $('#pgRemark').val();
								VenueDwr.savePackageGoods(packageId, goodsId, count, remark, null, function(ret) {
									layer.close(index);
									layer.alert(ret, {
										icon : 1,
										title : '提示信息'
									})
									grid2.reload();
									calculateTotal();
								});
							}
						},
						end : function(ret) {
						}
					});
				}
			}
		}, {
			id : 'modify',
			title : '修改商品',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid2.getSelectedRows();
					if (rows.length != 1) {
						layer.alert("请仅选择要1条修改的记录", {
							icon : 2,
							title : '提示信息'
						});
					} else {
						$('#fmPackageGoods').parent().show();
						$('#fmPackageGoods')[0].reset();
						var pgId = rows.attr("recordId");
						if (pgId) {
							VenueDwr.getPackageGoods(pgId, function(ret) {
								$('#bigId').val(ret.bigId)
								$('#bigId').change();
								$('#smallId').val(ret.smallId);
								$('#smallId').change();
								$('#goodsId').val(ret.goodsId);
								$('#count').val(ret.count);
								$('#pgRemark').val(ret.remark);
							});
						}
						layer.open({
							title : "修改商品",
							type : 1,
							skin : 'layui-layer-lan', // 加上边框
							area : [ '400px', '300px' ], // 宽高
							content : $("#fmPackageGoods"),
							btn : [ "确定", "取消" ],
							yes : function(index, layero) {
								if ($('#fmPackageGoods').valid()) {
									var packageId = grid2.settings.data.packageId;
									var goodsId = $('#goodsId').val();
									var count = $('#count').val();
									var remark = $('#pgRemark').val();
									VenueDwr.savePackageGoods(packageId, goodsId, count, remark, pgId, function(ret) {
										layer.close(index);
										layer.alert(ret, {
											icon : 1,
											title : '提示信息'
										})
										grid2.reload();
										calculateTotal();
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
			title : '删除商品',
			width : 100,
			handlers : {
				click : function() {
					var rows = grid2.getSelectedRows();
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
							VenueDwr.deletePackageGoods(ids, function(ret) {
								layer.alert("成功删除套餐商品关联 " + ret + " 个，失败 " + (rows.length - ret) + " 个");
								grid2.reload();
								calculateTotal();
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
	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
	var name = queryForm.find(":input[name=name]").val();
	var id = queryForm.find(":input[name=id]").val();
	if (createTime === "") {
		layer.msg("请限定发卡日期")
		return;
	}
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

	grid.load({
		data : data
	});

	ajustHeight();
}

var grid, grid2, form;

$(document).ready(function() {
	form = new FormView("formview", formSetting);
	form.init();
	grid = new GridView("list", gridSetting, "list");
	grid.init();
	grid2 = new GridView("list2", gridSetting2, "list2");
	grid2.init();
	grid.rowClick(function(id) {
		grid2.load({
			data : {
				packageId : id
			}
		});
		calculateTotal();
	});
	queryData(event, $('#formview form'));
	$('#formview_playDate').attr("onclick", "WdatePicker({onpicked:pickedFunc})")

	dicSelect(dic0222, 'unit');
	dicSelect(dic0223, 'discount');
	dicSelect(dic0223, 'dispatch');
	dicSelect(dic0223, 'process');
	dicSelect(dic0224, 'area');

	var bigMap = genMaps("from GoodsType where parentId = 0 order by orderno");
	joinSelect(bigMap, "bigId");
	$('#bigId').change(function() {
		var smallMap = genMaps("from GoodsType where parentId = " + this.value + " order by orderno");
		var smallValue = $('#smallId').val();
		$('#smallId').empty();
		joinSelect(smallMap, "smallId");
	});
	$('#smallId').change(function() {
		var goodsMap = genMaps("from Goods where typeId = " + this.value + " order by orderno");
		var goodsValue = $('#goodsId').val();
		$('#goodsId').empty();
		joinSelect(goodsMap, "goodsId");
	});
	
});

function calculateTotal() {
	var total = 0;
	var tds = $('#list2 table td[name="goodsTotal"]');
	for (var i = 0; i < tds.length; i++) {
		if (!isNaN(tds[i].innerHTML)) {
			total += parseFloat(tds[i].innerHTML);
		}
	}
	var th = $('#list2 table th[name="goodsTotal"]');
	th.text('商品总价 ' + total + ' 元');
	
	var count = 0;
	tds = $('#list2 table td[name="goodsCount"]');
	for (i = 0; i < tds.length; i++) {
		if (!isNaN(tds[i].innerHTML)) {
			count += parseInt(tds[i].innerHTML);
		}
	}
	th = $('#list2 table th[name="goodsCount"]');
	th.text('商品数量 ' + count + ' 份');
}