<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
	.axis path,
	.axis line{
		fill: none;
		stroke: black;
		shape-rendering: crispEdges;
	}

	.axis text {
		font-family: sans-serif;
		font-size: 11px;
	}

	.MyRect {
		fill: ForestGreen;
	}

	.MyText {
		fill: white;
		text-anchor: middle;
	}
</style>
</head>

<body style="overflow: scroll;">
<div id="formview" class="rbox1"></div>
<hr style="border:3px dotted #F8F8FF" width="100%" size=3/>
<div id="stat" class="rbox2">
<div class="rcon2" style="height: 1000px"></div>
</div>
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css?1" />
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/d3.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script>
var num = 0;
var amounts = [];
var days = [];
$.ajax({
	url:'<%=path%>/public/stat/queryDayAmount.htmls',
	type:"POST",
	dataType:"json",
	async:false,
	success:function(ret){
		num = ret.length;
		for(var i=0;i<num;i++){
			var record = ret[i];
			amounts[i] = record[1];
			days.push(record[0].split("-").join("/"));
		}
	}
})
//画布大小
	var width = 980;
	var height = 600;

	//在 body 里添加一个 SVG 画布	
	var svg = d3.select("#stat .rcon2")
		.append("svg")
		.attr("width", width)
		.attr("height", height);

	//画布周边的空白
	var padding = {left:60, right:30, top:20, bottom:20};

	var xWidth = width - padding.left - padding.right;	
	var xAva = xWidth/days.length;//x轴的平均宽度
	//x轴的比例尺
	var xScale = d3.scale.ordinal()
		//.domain(d3.range(amounts.length))
		.domain(days)
		.rangeRoundBands([0, xWidth]);
	//y轴的比例尺
	var yScale = d3.scale.linear()
		.domain([0,d3.max(amounts)])
		.range([height - padding.top - padding.bottom, 0]);

	//定义x轴
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom");
		
	//定义y轴
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("left").ticks(30);

	//矩形之间的空白
	var rectPadding = 20;

	//添加矩形元素
	var rects = svg.selectAll(".MyRect")
		.data(amounts)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
			
			return xAva * i + rectPadding/2;
		} )
		.attr("y",function(d){
			return yScale(d);
		})
		.attr("width", xScale.rangeBand() - rectPadding )
		.attr("height", function(d){
			return height - padding.top - padding.bottom - yScale(d);
		});

	//添加文字元素
	var texts = svg.selectAll(".MyText")
		.data(amounts)
		.enter()
		.append("text")
		.attr("class","MyText")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
			return xAva*i + rectPadding/2;
		} )
		.attr("y",function(d){
			return yScale(d);
		})
		.attr("dx",function(){
			return (xScale.rangeBand() - rectPadding)/2;//文字在柱状图的 位置
		})
		.attr("dy",function(d){
			return 20;
		})
		.text(function(d){
			return "￥"+d;
		});

	//添加x轴
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + (height - padding.bottom) + ")")
		.call(xAxis); 
		
	//添加y轴
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(yAxis);
	
    genDicJs(['0204']);
    var userMap = genMaps("from User order by name,id");
    var showNumberMap = genMaps("from ShowNumber order by orderno,id");
    var formSetting = {
    		po:'TicketSale',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"演出场次",type:"select",name:"showNumberId",map:showNumberMap},
    	  {title:"售票员",type:"pop",name:"createUserId",map:userMap},
    	  {title:"售票日期",type:"datescale",name:"createTime"}]
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
		                 			     }
		                 			 ]}	
    }
    function queryData(){
    	var table = $('#list table');
    	table.find("tr:gt(0)").remove();
    	
    	$.ajax({
    		url:"<%=path%>/public/stat/createTypeStat.htmls",
    		type:"POST",
    		dataType:"json",
    		success:function(ret){
    			for(var i=0;i<ret.length;i++){
    				var objs = ret[i];
    				var tr = $("<tr>").appendTo(table);
    				for(var j=0;j<objs.length;j++){
    					var td = $("<td align='center'>").html(objs[j]).appendTo(tr);
    				}
    				
    			}
    			var showNumbers = _.map(ret,_.first);
    			showNumbers = _.uniq(showNumbers);
    			for(var i=0;i<showNumbers.length;i++){
    				var trs = table.find('tr:contains('+showNumbers[i]+')');
    				var stys = _.filter(ret,function(num){//过滤门票策略的行数
    					return _.first(num) === showNumbers[i];
    				});
    				var uniqStrategys = _.map(stys,function(num){
    					return num[1];
    				});
    				uniqStrategys = _.uniq(uniqStrategys);
    				for(var j=0;j<uniqStrategys.length;j++){
    					var rows2=_.filter(stys,function(num){
    						return num[1]===uniqStrategys[j];
    					})
    					trs2 = trs.filter(function(){
    						return $(this).find("td:eq(1)").html() === uniqStrategys[j];
    					});
    					trs2.each(function(k,tr){
    						if(k==0){
    							$(tr).find("td:eq(1)").attr("rowspan",trs2.length);
    						}else{
    							$(tr).find("td:eq(1)").remove();
    						}
    					})
    				}
    				trs.each(function(i,tr){
    					if(i===0){
    						$(tr).find("td:eq(0)").attr('rowspan',trs.length);
    					}else{
    						$(tr).find("td:eq(0)").remove();
    					}
    					
    				})
    				
    			}
    			
    		}
    	})
    }
	var form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	queryData();
    	$(top.document).find("iframe[name=mainFrame]").height(800);
    });
</script>

</body>