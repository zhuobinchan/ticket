<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/colorpicker.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/datepicker.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/uniform.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/select2.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-media.css" />
<link href="<%=path %>/static/special/font-awesome/css/font-awesome.css" rel="stylesheet" />
<SCRIPT type="text/javascript">
    <!--
    var data = parent.data;
    genDicJs(['0207']);
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = genMaps("from Area order by code");
    var closeWin = function(){
    	var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.close(index);
    }
    var openCustomerWin = function(){
 	   layer.open({
 		   id:'customerwin',
    		title:'选择客户',
		    type: 2,
		    skin: 'layui-layer-rim', //加上边框
		    area: ['750px', '480px'], //宽高
		    content: [g_path+'/public/common/r2.htmls?page=base/bussiness/customer_win',"no"],
		    btn:["确定","关闭"],
		    yes:function(index,layero){
		    	var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		    	var row = iframeWin.grid.getSelectedRows();
		    	if(row.length!=1){
		    		layer.msg("请选择客户");
		    	}else{
		    		var json = $.parseJSON(row.attr("hidevalues"));
					$("#costumerName").val(json.shortname);
					$("#customerId").val(json.id);
					$("#customerCheap").val(row.find("td[name=cheapPrice]").attr("value")); 
					recalcu();
			    	layer.close(index);
		    	}
				
		    }
		});
 }
    var recalcu = function(){
    	var realAmount = 0; 
		$('td[name=realPrice]').each(function(i,td){
			var cp = $(td).siblings('td[name=price]').html();
			var size = $(td).siblings('td[name=size]').html();
			var customerCheap = $('#customerCheap').val();
			var cheapPrice = $('#cheapPrice').val();
			var realPrice = cp - cheapPrice - customerCheap;
			$(td).html(realPrice);
			var money = size*realPrice;
			realAmount += money;
			$(td).siblings('td[name=money]').html(money);
		})
		$("td[name=realAmount]").html(realAmount);
    }
    $(function(){
    	var table = $('#itemTable');
    	var totalSize = 0,amount = 0;
    	var arr = _.map(data,function(value,key){
    		var area = areas[key];
    		if(area){
    			var size = data[key].length; 
        		totalSize += size;
        		var money = size * area.price ;
        		amount += money;
        		return '<tr name="itemtr"><td align="center" width="140">'+area.name+'</td><td style="text-align:center" width="70" name="price">'+area.price 
        		+'</td><td style="text-align:center" name="size" width="70">'+size+'</td><td style="text-align:center" name="rowamount" width="70">'
        		+money+'</td><td align="center" width="70" name="realPrice">'+area.price+'</td><td align="center" name="money">'+money+'</td></tr>';
    		}
    	})
    	
    	for(var i=0;i<arr.length;i++){
    		$(arr[i]).appendTo(table);
    	}
    	$('<tr style="color:green;font-weight:bold"><td align="center">总计</td><td align="center">---</td><td name="totalSize" align="center">'+totalSize
    		+'</td><td align="center" name="amount">'+amount+'</td><td align="center">---</td><td align="center" name="realAmount">'+amount+'</td></tr>').appendTo(table);
    	//拼凑门票策略下拉框
    	var select = joinSelect(strategys,"strategy").appendTo($('div[name=strategy]')).change(function(e){
    		var json = strategys[this.value];
    		$('#cheapPrice').val(json.cheapPrice);
    		recalcu();
    	});
    	dicSelect(dic0207).appendTo($('div[name=payType]')).attr("id","payType").change(function(e){
    		if(this.value==="0207003"){
    			$("#foregift").val(0);
    		}
    	});
    	$('#cheapPrice').keyup(function(e){
    		if(e.keyCode>=47&&e.keyCode<=57){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	$('#cancel').click(function(e){//按钮
    	    closeWin();
    	})
    	$('#reset').click(function(e){//按钮
    	    $("form :input").val("");
    		$("a.select2-choice span").each(function(i,v){
    			v.innerHTML = "";
    		});
    	})
    	$('#cheapPrice').focus(function(e){
    		$(this).attr("val",this.value);
    		if(this.value==0){
    			this.value = "";
    		}else{
    			this.select();
    		}
    	}).blur(function(e){
    		if(this.value==="")
    			this.value = $(this).attr("val");
    	})
    	$('#customerCheap').focus(function(e){
    		$(this).attr("val",this.value);
    		if(this.value==0){
    			this.value = "";
    		}else{
    			this.select();
    		}
    	}).blur(function(e){
    		if(this.value==="")
    			this.value = $(this).attr("val");
    	})
    	$('#foregift').focus(function(e){
    		$(this).attr("val",this.value);
    		if(this.value==0){
    			this.value = "";
    		}else{
    			this.select();
    		}
    	}).blur(function(e){
    		if(this.value==="")
    			this.value = $(this).attr("val");
    	})
    	$(".btn-success").click(function(e){
    		var strategy = $("#strategy").val();
    		if(strategy==""){
    			layer.msg("请选择售票方式");
    			return;
    		}
    		if($(parent.document).find("td[picked=yes]").length===0){
    			layer.alert("选择的座位已失效，请重新选择座位");
    			return;
    		}
    		var param = {};
    		var customerId =  $('#customerId').val();
    		var cheapPrice = $("#cheapPrice").val();
    		var totalSize = $("td[name=totalSize]").html();
    		var sallerCode = $("#sallerCode").val();
    		var descs = $("#descs").val();
    		var foregift = $("#foregift").val();
    		if($.trim(sallerCode)!=""){
    			param.sallerCode = sallerCode;
    		}
    		if($.trim(descs)!=""){
    			param.descs = descs;
    		}
    		var payType = $('#payType').val();
    		if(payType==="0207004"){
    			if(customerId===""){
    				layer.msg("请选择冲抵的客户账户");
    				return;
    			}
    		}
    		
    		param.foregift = foregift;
    		param.customerId = customerId;
    		param.cheapPrice = cheapPrice;
    		param.customerCheap = $("#customerCheap").val();
    		param.strategy = strategy;
    		param.totalSize = totalSize;
    		param.amount = table.find("td[name=amount]").html();
    		param.realAmount = table.find("td[name=realAmount]").html();
    		param.playDate = $(parent.document).find("#playDate").val();
    		param.showNumberId = $(parent.document).find("#showNumber").val();
    		BaseDwr.reserveTickets(JSON.stringify(data),JSON.stringify(param),function(ret){
    			parent.ticketArr = ret;
    			closeWin();
    		});
    	})
    	$("#costumerName").focus(function(e){
    		openCustomerWin();
    	})
    })


    //-->
</SCRIPT>
<style type="text/css">
.widget-content td{
	text-align: center;
}
table#bussiness td{
	padding: 0;
}
.span11{
	padding: 0;margin: 2px!important;
}
</style>
</head>

<body style="margin:0;padding: 0">
<div id="content">
<div id="content-header">
  <div id="breadcrumb" style="border:1px solid"><a href="#" class="current">Common elements</a> </div>
  <h1><span style="color:red">预订</span>门票</h1>
</div>
<div class="container-fluid" style="padding: 0;">
  <div class="row-fluid" style="margin: 0">

    <div class="span6" style="width:68%">
                <div class="widget-box" style="width:100%">
          <div class="widget-title"> <span class="icon"><i class="icon-th"></i> </span>
            <h5>座位列表</h5>
            <span class="label label-info">仔细核对</span> </div>
          <div class="widget-content nopadding">
            <table class="table table-bordered table-striped" id="itemTable">
              <thead>
                <tr>
                  	<th>片区</th>
					<th>价格</th>
					<th>人数</th>
					<th>应收金额</th>
					<th>优惠价</th>
					<th>实收金额</th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
    </div>
        <div class="span6" style="width:29%">
      <div class="widget-box">
        <div class="widget-title"> <span class="icon"> <i class="icon-align-justify"></i> </span>
          <h5>业务信息</h5>
        </div>
        <div class="widget-content">
        <form style="display: inline;">
        <input type="hidden" value="" id="customerId"/>
          <table class="table-bordered table-striped" id="bussiness" style="padding: 0">
          <tr>
          <td width="90">售票方式 :</td><td><div class="controls" name="strategy"></div></td>
          </tr>
          <tr>
          <td>每张立减 :</td><td><input type="text" name="cheapPrice" id="cheapPrice" class="span11" value="0" placeholder="每张立减" /></td>
          </tr>
          <tr>
          <td>客户名称 :</td><td><input type="text" id="costumerName" name="costumerName" class="span11" placeholder="客户名称" /></td>
          </tr>
          <tr>
          <td>客户优惠 :</td><td><input type="text" id="customerCheap" name="customerCheap" class="span11" value="0" placeholder="客户优惠" /></td>
          </tr>
          <tr>
          <td>营销编号 :</td><td><input type="text" class="span11" id="sallerCode" name="sallerCode" placeholder="营销编号" /></td>
          </tr>
          <tr>
          <td>押金金额 :</td><td><input type="text" class="span11" id="foregift" name="foregift" value="0" placeholder="押金收款 " /></td>
          </tr>
          <tr>
          <td width="90">付款方式 :</td><td><div class="controls" name="payType"></div></td>
          </tr>
         
          <tr>
          <td>备注信息 :</td><td> <textarea class="span11" id="descs" name="descs" style="height:60px" placeholder="留言 备忘 提醒 标记" ></textarea></td>
          </tr>
          </table>
            <div class="form-actions">
              <button type="button" class="btn btn-success">预订</button>
              <button type="reset" class="btn btn-danger" id="reset">重置</button>
              <button type="button" class="btn btn-danger" id="cancel">取消</button>
            </div>
            </form>
        </div>
      </div><!-- 第一行：售票信息结束 -->
    </div>
  </div>
</div></div>

<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script src="<%=path %>/static/special/js/jquery.min.js"></script> 
<script src="<%=path %>/static/special/js/bootstrap.min.js"></script> 
<script src="<%=path %>/static/special/js/masked.js"></script> 
<script src="<%=path %>/static/special/js/jquery.uniform.js"></script> 
<script src="<%=path %>/static/special/js/select2.min.js"></script> 
<script src="<%=path %>/static/special/js/matrix.form_common.js"></script> 
</body>
</html>
