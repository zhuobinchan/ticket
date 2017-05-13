<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0202', '0228']);
    var deptMap = genMaps("from Organization order by name,id");
    
    var orgMap = _.filter(deptMap,function(v){
    	return v.tier == 1;
    });
    var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
    var formSetting0 = {
    		po:'TicketSale',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"营销编号",type:"text",name:"code"},{title:"手机号码",type:"text",name:"mobileno"},
    	         {title:"所属单位",type:"select",name:"orgId",map:orgMap}
    	  ]
    	],
    	queryRow:{align:'center',buttons:[
		                 			   {
		                 			    	 id:'query',
		                 			    	 title:'查询',
		                 			    	 handlers:{
		                 			    		 click: function(e,queryForm){
		                 			    			 queryData(e,queryForm);
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'reset',
		                 			    	 title:'重置',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 form0.reset();
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
    function queryData(e,queryForm){
    	var code = queryForm.find(":input[name=code]").val();
    	var mobileno = queryForm.find(":input[name=mobileno]").val();
    	var orgId = queryForm.find(":input[name=orgId]").val();
    	var data = [];
    	if(code!==""){
    		data.push({r:"like",name:'code',value:code+"%"});
    	}
    	if(mobileno!==""){
    		data.push({r:"like",name:'mobileno',value: mobileno+"%"});
    	}
    	if(orgId!==""){
    		data.push({r:"=",name:'orgId',value: orgId});
    	}
    	grid.load({data:data});
    	ajustHeight();
    }
    var formSetting = {
    		po:'Salesman',
    		title:"营销人员",
    		hiddens:["id"],
    	fields:[[{
    	   title:"营销编号",type:"text",name:"code"
    	},{
    	   title:"人员名称",type:"text",name:"name"
    	},{title:"人员状态",type:"select",dic:"0202",name:"status"}]
    	,[{title:"手机号码",type:"text",name:"mobileno"},{title:"入职日期",type:"date",name:"joinDate"},
    	  {title:"提成比例",type:"int",name:"commisionRate"}],
    	  [{title:"所属部门",type:"pop",name:"deptId",fn:'selectOrg',map:orgMap}
    	  ,{disabled:true,title:"所属单位",type:"select",name:"orgId",map:orgMap}
    	  ,{title:"员工类别",type:"select",dic:"0228",name:"type"}]
    	],
		toolbar:{align:'right',buttons:[
		                 			   {
		                 			    	 id:'add',
		                 			    	 title:'增加',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			
		                 			    			if($('tr[name=newrow]').length===0){
		                 			    				 var newRow = grid.newRow();
			                 			    			 setFormDefault(newRow);
	                 			    				}
		                 			    			
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'save',
		                 			    	 title:'保存',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			if(grid.getSelectedRows().length===0&&$('#list tr[name=newrow]').length!==1){
	                 			    					layer.msg("请选择或者新增记录");
	                 			    					return ;
	                 			    				}
		                 			    			 form.save(function(ret) {
		                 			    				
		                 			    				if (ret == -1) {
		                 			    					layer.alert("数据保存失败，请重试！");
		                 			    				} else {
		                 			    					layer.msg("数据已保存！");
		                 			    					grid.reload(ret.id);
		                 			    					ajustHeight();
		                 			    				}

		                 			    			});
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			     , {
		                 			    	 id:'delete',
		                 			    	 title:'删除',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 grid.deletes("deleteMappedRoleNavis",function(ret){
		                 			    				 if(ret==-1){
		                 			    					 layer.msg("删除失败，请重试！");
		                 			    				 }else{
		                 			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
		                 			    						 grid.reload();
		                 			    						 DwrTool.getRoleNaviList(-1,function(zNodes){
		                 			    							$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		                 			    						});
		                 			    					 });
		                 			    				 }
		                 			    			 });
		                 			    			 
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
    function setFormDefault(newRow){
    	form.reset();
		grid.setCellHtml(newRow,"status","0202001");
		var now = getCurrentTime("yyyy-MM-dd");
		grid.setCellHtml(newRow,"joinDate",now);
		form.setValue("status","0202001");
		form.setValue("commisionRate","10");
		form.setValue("joinDate",now);
		form.setValue("type", "0228001");
    }
	var gridSetting = {
			id:'salesman',
			po:"Salesman",
			title:'营销人员列表',
			height:'auto',
			//noPaddingRight:true,
			service:"uiServices!listSimple",
			pageInfo: {
				records: 12,//auto表示根据容器高度自动判断
				orderby : "code,joinDate,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'code',
				   descs:"营销编号",
				   align:'center',
				   width:120
			   },{
				   name: 'name',
				   descs:"人员名称",
				   align:'center',
				   width:100
			   },{
				   name: 'deptId',
				   descs:"所属部门",
				   type:"select",
				   align:'center',
				   width:150
			   } ,{
				   name: 'orgId',
				   descs:"所属单位",
				   type:"select",
				   align:'center',
				   width:150
			   } ,{
				   name: 'mobileno',
				   descs:"手机号码",
				   align:'center',
				   width:150
			   }   ,{
				   name: 'commisionRate',
				   descs:"提成比例",
				   align:'center',
				   width:120
			   }    
			   ,{
				   name: 'joinDate',
				   descs:"入职日期",
				   type:'date',
				   align:'center',
				   width:120
			   } ,{
				   name: 'status',
				   descs:"记录状态",
				   type:'select',
				   align:'center'
			   } 
			]
		};
    function selectOrg(e,obj,title){
    	popSelectWin(obj,title,g_path+"/public/common/r2.htmls?page=abc/org/org_win",function(index,layero){
    		var nodes = $(layero.selector+" iframe")[0].contentWindow.treeObj.getSelectedNodes();
	    	if(nodes.length>0){
	    		obj.value = nodes[0].name;
	    		$(obj).siblings("input:hidden").val(nodes[0].id);
	    		var id = nodes[0].id;
	    		var pid = deptMap[id].parentId;
	    		if(pid!==null){
	    			id = pid;
	    			pid = deptMap[pid].parentId;
	    			if(pid!==null){
	    				id = pid;
	    				pid = deptMap[pid].parentId;
	    				if(pid===null){
	    					$('#formview :input[name=orgId]').val(id);
	    				}
	    			}else{
	    				$('#formview :input[name=orgId]').val(id);
	    			}
	    		}else{
	    			$('#formview :input[name=orgId]').val(nodes[0].id);
	    		}
	    	}
	    	layer.close(index);
    	},300,450);
    }
	var grid,form,form0;
    $(document).ready(function(){
    	form0 = new FormView("formview0",formSetting0);
    	form0.init();
    	form = new FormView("formview",formSetting);
    	form.init();
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		grid.rowClick(function(id){
			if(id){
				form.loadData(id, "Salesman")
			}else{
				setFormDefault($(this));
			}
			
		});
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview0" class="rbox1"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox2"></div>

<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>