<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/dhtmlx/codebase/dhtmlx.css" />
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=<%=Math.random()%>"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=<%=Math.random()%>"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type='text/javascript' src='<%=path%>/static/dhtmlx/codebase/dhtmlx.js'></script>
<jsp:include page="/WEB-INF/pages/base/bussiness/include.jsp" />
<style type="text/css">
#list td,th{white-space: nowrap;}
	#list td,th{white-space: nowrap;padding-left: 5px;padding-right: 5px}
	.total {
		font-size: 10pt;
	}
</style>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0204']);
    var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
    var formSetting = {
    		po:'TicketSale',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"售票员",type:"select",name:"createUserId",map:userMap},{title:"售票日期",type:"datescale",name:"createTime"},
    	  {title:"演出日期",type:"datescale",name:"playDate"}
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
		                 			    			 form.reset();
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'print',
		                 			    	 title:'打印',
		                 			    	 hide:true,
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			print();
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'excel',
		                 			    	 title:'导出',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			$("<iframe name='ef' style=\"display: none\"></iframe>").appendTo($("body"));
		    										$("<form style='display:none' target='ef' action='${pageContext.request.contextPath}/public/stat/excelTicketSaleList.htmls'></form>").append($("<input>").attr("name","queryString").val(JSON.stringify(data))).append($("<input>").attr("name","orderbyString").val("createTime,id")).appendTo($('body')).submit();
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
	var gridSetting = {
			id:'ticketSaleGrid',
			po:"TicketSale",
			title:'销售统计',
			fixWidth : true,
			height:'auto',
			className:"mtable",
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "createTime,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'saleNo',
				   descs:"销售单号",
				   align:'center',
				   width:110
			   },{
				   name: 'createTime',
				   descs:"售票日期",
				   size:10,
				   align:'center',
				   width:100
			   } ,{
				   name: 'playDate',
				   descs:"演出日期",
				   align:'center',
				   width:100
			   },{
				   name: 'createUserId',
				   descs:"售票人",
				   align:'center',
				   type:'select',
				   width:80
			   } ,{
				   name: 'payType',
				   descs:"支付方式",
				   type:"select",
				   align:'center',
				   width:80
			   }  ,{
				   name: 'strategyName',
				   descs:"门票策略",
				   align:'center',
				   width:100
			   }  
			    ,{
				   name: 'ticketNum',
				   descs:"售票数量",
				   type:'int',
				   align:'center',
				   width:70
			   } ,{
				   name: 'printNum',
				   descs:"用票数量",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'cheapPrice',
				   descs:"每张立减",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'offsetPrice',
				   descs:"原票抵扣",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'discount',
				   descs:"折扣比例",
				   type:'int',
				   align:'center',
				   width:70
			   } ,{
				   name: 'showAmount',
				   descs:"原价总额",
				   align:'center',
				   formatter:"sumSaleShowAmount",
				   width:120
			   },{
				   name: 'realAmount',
				   descs:"实收总额",
				   align:'center',
				   formatter:"sumSaleRealAmount",
				   width:120
			   } ,{
				   name: 'sallerCode',
				   descs:"营销编号",
				   width:120,
				   align:'center'
			   } ,{
				   name: 'memberNo',
				   descs:"会员卡号",
				   align:'center',
				   width:120
			   },{
				   name: 'priceShowType',
				   descs:"打印方式",
				   type:'select',
				   align:'center',
				   width:120
			   }     
			],toolbar:{align:'right',buttons:[
				{
					 id:'balance',
					 title:'结算',
					 width:100,
					 handlers:{
						 click: function(e){
							 layer.confirm("确定结算吗？",{offset:[$(e.target).offset().top,$('body').width/2]},function(){
								 BaseDwr.balanceSale(function(ret){
									 if(ret==0){
										 layer.msg("已结算");
										 queryData(event,$('#formview form'));
									 }else{
										 layer.msg("结算失败");
									 }
								 });
							 });
						 }
					 }
				},{
					 id:'modify',
					 title:'修改结算方式',
					 width:100,
					 handlers:{
						 click: function(){
							var rows = grid.getSelectedRows();
							if(rows.length===0){
								layer.msg("请选择要修改的记录",{icon: 1,title:'提示信息'})
							}else{
								layer.open({
									title:false,
									type: 2,
									skin: 'layui-layer-lan', //加上边框
									area: ['600px', '300px'], //宽高
									content: [g_path+'/public/common/r2.htmls?page=sell/query/modi_ticket_sale',"no"],
									btn:["确定","取消"],
									yes:function(index,layero){
										var payType = $(layero.selector+" iframe")[0].contentWindow.document.getElementById("payType").value;
										var arr = [];
										rows.each(function(i,v){
											arr[i] = parseInt($(v).attr("recordId"));
										})
										BaseDwr.changePayType(arr,payType,function(ret){
											layer.msg("支付方式修改成功",{icon: 1,title:'提示信息'})
											layer.close(index);
											grid.reload();
										});
									},
									end:function(ret){
									}
								});
							}
						 }
					 }
				},{
					 id:'modifySalesman',
					 title:'修改营销员',
					 width:100,
					 handlers:{
						 click: function(){
							var rows = grid.getSelectedRows();
							if(rows.length===0){
								layer.msg("请选择要修改的记录",{icon: 1,title:'提示信息'})
							}else{
								var that = this;
								wins = new dhtmlXWindows();
								var windowWidth = 300;
								var windowHeight = 200;
								var cardWindow = wins.createWindow('changeSalesman', ($(document.body).width() - windowWidth) / 2,
										top.window.document.body.scrollTop - 80 + (window.screen.height - windowHeight) / 2,
										windowWidth, windowHeight);
								cardWindow.setText('修改营销员');
								cardWindow.denyResize();
								cardWindow.denyMove();
								cardWindow.setModal(true);
								var form = cardWindow.attachForm();
								form.loadStruct([ {
									type : 'label',
									list : [ {
										type : "settings",
										position : "label-left",
										labelWidth : 80,
										inputWidth : 150,
										offsetLeft : 20
									}, {
										type : "combo",
										label : "营销员",
										name : "salesmanId",
										required : true
									} ]
								}, {
									type : "block",
									blockOffset : 0,
									list : [ {
										type : "button",
										value : "取消",
										name : 'cancel',
										offsetLeft : 80,
										offsetTop : 20
									}, {
										type : "newcolumn"
									}, {
										type : "button",
										value : "确定",
										name : 'submit',
										offsetTop : 20
									} ]
								} ], 'json');

								var salesmanCombo = form.getCombo('salesmanId');
								SWI.util.addSalesmanComboOptions(salesmanCombo);

								form.setFocusOnFirstActive();
								form.attachEvent('onButtonClick', function(name) {
									if (name === 'submit') {
										form.validate();
									} else if (name === 'cancel') {
										cardWindow.close();
									}
								});
								form.attachEvent('onAfterValidate', function(status) {
									if (status) {
										var arr = [];
										rows.each(function(i,v){
											arr[i] = parseInt($(v).attr("recordId"));
										})
										BaseDwr.changeSalesman(arr, salesmanCombo.getSelectedValue(), function(ret){
											dhtmlx.alert("营销员修改成功");
											cardWindow.close();
											grid.reload();
										});
									}
								});
							}
						 }
					 }
				}                               
			]}
		};
    var roleString = '${sessionScope.roleString}';
	var userId = '${sessionScope.userInfo.id}';
	var data = [];
    function queryData(e,queryForm){
    	var param = {};
    	var playDate = queryForm.find(":input[name=playDate]:eq(0)").val();
    	var playDate2 = queryForm.find(":input[name=playDate]:eq(1)").val();
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	if(playDate===''&&createTime===""){
    		layer.msg("请限定售票日期或者演出日期")
    		return;
    	}
    	data = [{"status":"0211001"}];
    	if(roleString.indexOf("financial")==-1&&roleString.indexOf("saller_charge")==-1){
    		data.push({createUserId: userId});
    		param.createUserId = userId;
    	}else{
    		var createUserId = queryForm.find(":input[name=createUserId]").val();
    		if(createUserId!=''){
        		data.push({createUserId: createUserId});
        		param.createUserId = createUserId;
        	} 
    	}
    	if(createTime2!==""){
    		data.push({r:"between",name:'createTime',value:[createTime,createTime2]});
    		param.startDate = createTime;
        	param.startDate2 = createTime2;
    	}
    	if(playDate2!==""){
    		data.push({r:"between",name:'playDate',value:[playDate,playDate2]});
    		param.playDate = playDate;
        	param.playDate2 = playDate2;
    	}
    	
    	grid.load({data:data});
    	
    	$.ajax({
    		url:'<%=path%>/public/tool/calcuTicketData.htmls',
    		data: {param:JSON.stringify(param)},
    		dataType:"json",
    		type:"POST",
    		success:function(ret){
    			$('#totalNum').html(ret.totalNum);
    			$('#plusNum').html(ret.plusNum);
    			$('#cashAmount').html(ret.cashAmount);
    			$('#cardAmount').html(ret.cardAmount);
    			$('#memberAmount').html(ret.memberAmount);
    			$('#amount').html(ret.amount);
    			$('#offsetNum').html(ret.offsetNum);
    			$('#changeDateNum').html(ret.changeDateNum);
    			$('#rememberAmount').html(ret.rememberAmount);
    			$('#freeAmount').html(ret.freeAmount);
    			$('#otherAmount').html(ret.otherAmount);
    			$('#changeSeatNum').html(ret.changeSeatNum);
    			$('#freeNum').html(ret.freeNum);
    			$('#minFeeNum').html(ret.minFeeNum);
    			$('#cheapNum').html(ret.cheapNum);
    			$('#backNum').html(ret.backNum);
    			$('#backAmount').html(ret.backAmount);
    			$('#usePaperNum').html(ret.usePaperNum);
    			$('#shouldNum').html(ret.shouldNum);
    		}
    	})
    	ajustHeight();
    }
    function pickedFunc(){
    	$dp.$('formview_playDate_2').value=$dp.$('formview_playDate').value;
    }
    function CreateOneFormPage(){
    	var startTime = $('#formview_createTime').val();
    	var endTime= $('#formview_createTime_2').val();
    	LODOP=getLodop();  
    	LODOP.SET_PRINT_PAGESIZE(2,2100,2970,'');
    	var div = jQuery('#list');
    	var strStyle="<style>td,th {border:1px #000 solid;border-collapse: collapse;font-size:7pt;text-align:center}</style>";
    	var strFormHtml=$("#list div.rcon2").html();//获取内容
    	var table = $(strFormHtml);
    	table.find("tr th:eq(1)").remove();
    	table.find("col:eq(1)").remove();
    	table.find("tr").each(function(i,v){
    		$(v).find("td:eq(1)").remove();
    	})
    	strFormHtml = strStyle+table[0].outerHTML;
    	var strFormHtml2=strStyle+$("#total div.rcon").html();//获取内容
    	var today = getCurrentTime();
    	var time = "23:59:59";
    	if(today==endTime){
    		time = getCurrentTime("HH:mm");
    	}
    	LODOP.ADD_PRINT_HTM(10,5, div.width(), 39,"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>田汉大剧院</div>");
    	LODOP.ADD_PRINT_HTM(40,5, div.width(), 39,"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>销售统计表</div>");
    	LODOP.ADD_PRINT_HTM(70,10, div.width(), 39,"<div style='width:100%;font-size:9pt;'>制表日期："+today+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制表人：${sessionScope.userInfo.name}</div>");
    	LODOP.ADD_PRINT_TABLE(90,5,div.width(),"BottomMargin:15mm",strFormHtml2);
    	LODOP.ADD_PRINT_TABLE(136,5,div.width(),'90%',strFormHtml);
    	LODOP.ADD_PRINT_TEXT(15, 50, div.width(),25,"查询条件【"+startTime+" 至 " +endTime+" "+time+"】");
    	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",5);
    	//LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
    	/* LODOP.ADD_PRINT_HTM((pageHeight*0.1)+"mm","11mm", div.width(), 39,
    			"<div style='width:"+div.width()+";text-align:center;font-size:9pt'><span tdata='pageNO'>##</span>/<span tdata='pageCount'>##</span></div>");
    	*///LODOP.SET_PRINT_STYLEA(0,"ItemType",1); 

    	LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
    };
    function print() {
    	CreateOneFormPage();
    	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Auto-Width");
    	LODOP.PREVIEW();
    };
    
	var grid,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		queryData(event,$('#formview form'));
		$('#formview_playDate').attr("onclick","WdatePicker({onpicked:pickedFunc})");
		$('#list').width(top.rightWidth);
		//按钮权限
    	var forbitBtns = $.parseJSON('${forbitBtns}');
		for(var i=0;i<forbitBtns.length;i++){
			$("#"+forbitBtns[i]).hide();
		}
		setTimeout(function(){
			$('#print').show();
		},1500);
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div class="rbox1" id="total">
<div class="rcon">
<table width="99%" align="center" class='mtable4 total' border="0" cellpadding="0" cellspacing="0">
<tr>
<td width="100">现金：<span id="cashAmount">0</span></td>
<td width="100">信用卡：<span id="cardAmount">0</span></td>
<td width="100">会员卡：<span id="memberAmount">0</span></td>
<td width="100">挂账：<span id="rememberAmount">0</span></td>
<td width="100">免单：<span id="freeAmount">0</span></td>
<td width="100">退票：<span id="backAmount">0</span></td>
<td colspan="2">总额：<span id="amount">0</span></td>
<td width="100">用票数：<span id="usePaperNum">0</span></td>
</tr>
<tr>

<td>原价：<span id="shouldNum">0</span></td>
<td>浮动：<span id="plusNum">0</span></td>
<td width="100">抵扣票数：<span id="offsetNum">0</span></td>
<td>改签：<span id="changeDateNum">0</span></td>
<td>换座：<span id="changeSeatNum">0</span></td>
<td>免票：<span id="freeNum">0</span></td>
<td width="100">最低消费：<span id="minFeeNum">0</span></td>
<td width="98">优惠：<span id="cheapNum">0</span></td>
<td width="100">退票：<span id="backNum">0</span></td>

</tr>
</table>
</div>
</div>
<div id="list" class="rbox2" style="overflow:scroll;border:0px solid"></div>
<div class="rbox2">
<div class="rcon">
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
</div>

</div>

<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type='text/javascript' src='<%=path %>/static/js/LodopFuncs.js'></script>
</body>
</html>