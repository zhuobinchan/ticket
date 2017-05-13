<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css"
	href="<%=path%>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path%>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=path%>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
<SCRIPT type="text/javascript">
<!--
	var setting = {
		check : {
			enable : true,
			chkboxType : {
				"Y" : "p",
				"N" : "s"
			}
		},
		data : {
			key : {
				title : "title"
			},
			simpleData : {
				enable : true
			}
		}
	};
	var gridSetting = {
		id : 'roleGrid',
		po : "Role",
		title : '角色列表',
		height : 'auto',
		service : "uiServices!listSimple",
		pageInfo : {
			records : -1,//auto表示根据容器高度自动判断
			orderby : "name,id"
		},
		fields : [ {
			name : 'id',
			hide : true
		}, {
			name : 'name',
			descs : "角色名称",
			align : 'center',
			editable : true,
			width : 200
		}, {
			name : 'descs',
			descs : "角色描述",
			align : 'center',
			editable : true,
			width : 260
		} ],
		toolbar : {
			align : 'right',
			buttons : [
					{
						id : 'add',
						title : '增加',
						handlers : {
							click : function() {
								var newRow = grid.newRow();
							}
						}
					},
					{
						id : 'save',
						title : '保存',
						handlers : {
							click : function() {
								grid.save(function(ret) {
									if (ret == -1) {
										layer.alert("数据保存失败，请重试！");
									} else {
										layer.msg("数据已保存！");
										grid.reload(ret.id);
									}

								});
							}
						}
					},
					{
						id : 'delete',
						title : '删除',
						handlers : {
							click : function() {
								grid
										.deletes(
												"deleteMappedRoleNavis",
												function(ret) {
													if (ret == -1) {
														layer.msg("删除失败，请重试！");
													} else {
														layer
																.msg(
																		"删除"
																				+ ret.recordNum
																				+ "条记录！",
																		function(
																				ret) {
																			grid
																					.reload();
																			DwrTool
																					.getRoleNaviList(
																							-1,
																							function(
																									zNodes) {
																								$.fn.zTree
																										.init(
																												$("#treeDemo"),
																												setting,
																												zNodes);
																							});
																		});
													}
												});

							}
						}
					} ]
		}
	};
	var grid;
	$(document).ready(
			function() {
				
				grid = new GridView("list", gridSetting, "list");
				grid.init();
				grid.load();
				DwrTool.getRoleNaviList(-1, function(zNodes) {
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				});
				grid.rowClick(function(id) {
					DwrTool.getRoleNaviList(id, function(zNodes) {
						$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					});
				});
				$("#savePrivileges").click(
						function(e) {
							e.preventDefault();
							var rows = grid.getSelectedRows();
							if (rows.length == 0) {
								layer.msg("请选择角色记录");
							} else {
								var treeObj = $.fn.zTree
										.getZTreeObj("treeDemo");
								var nodes = treeObj.getCheckedNodes(true);
								var ids = [];
								$(nodes).each(function(i, node) {
									ids.push(node.id);
								})
								DwrTool.mapRoleNavi(
										$(rows[0]).attr("recordId"), ids,
										function(ret) {
											layer.msg(ret, {
												icon : 1,
												title : '提示信息'
											})
										});
							}

						});
						
//				var heigth = $(document).height() * 0.5;
//				$("#role").css('height',heigth);
//				alert($("#role").css('height'));
				
			});
//-->
</SCRIPT>
</head>

<body>
	<div class="rtit">角色管理</div>
	<div class="rcon">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="border-collapse: collapse">
			<tr>
				<td width="60%" valign="top"
					style="border: 1px #dbdbdb solid; padding: 0px;"><div
						id="list"></div></td>
				<td style="border: 1px #dbdbdb solid; padding: 0px;" valign="top">
					<div class="rtit2">
						<div class="rtit2l">菜单权限</div>
						<div class="rtit2r">
							<a href="#" id="savePrivileges" class="btn7">保存权限</a>
						</div>
						<div class="clear"></div>
					</div> <!-- div style="overflow:scroll; width:100%; height:200;"-->
					<div id="role"
						style="width: 100%;height:300px;  line-height: 100px; overflow: auto; overflow-x: hidden;">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<link rel="stylesheet"
		href="<%=path%>/static/ztree/css/metroStyle/metroStyle.css"
		type="text/css">
		<script type="text/javascript"
			src="<%=path%>/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
		<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
		<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
		<script type='text/javascript'
			src='<%=path%>/dwr/interface/DwrTool.js'></script>
</body>
</html>