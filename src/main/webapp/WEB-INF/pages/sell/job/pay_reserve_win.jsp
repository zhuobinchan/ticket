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
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<SCRIPT type="text/javascript">
    <!--
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = top.areas;
    genDicJs(['0207']);
    var index = parent.layer.getFrameIndex(window.name);
    var closeWin = function(){
	    parent.layer.close(index);
    }
    var recalcu = function(){
    	var amount = 0; 
		$('td[name=realPrice]').each(function(i,td){
			var realPrice = 0;
			var size = $(td).siblings('td[name=size]').html();
			if($('#payType').val()!=="0207003"){
				var cp = $(td).siblings('td[name=price]').html();
				var discount = $('#discount').val();
				var minPrice = $('#realPrice').val();
				realPrice = cp - $('#cheapPrice').val() - $('#offsetPrice').val();
				if(minPrice>0){
					realPrice = minPrice;
				}else{
					if(discount>0){
						realPrice -= (cp*(100-discount)/100)
						realPrice = Math.floor(realPrice*0.1)*10;
					}
				}
			}
			$(td).html(realPrice);
			var money = size*realPrice;
			amount += money;
			$(td).siblings('td[name=money]').html(money);
		})
		$("td[name=amount]").html(amount);
    }
    $(function(){
    	$("#giftNum").val(0).focus(function(e){
    		if(this.value==0){
    			$(this).val("");
    		}else{
    			this.select();
    		}
    	}).blur(function(e){
    		if(this.value===""){
    			this.value = 0;
    		}
    	})
    	var isall = '${param.all}';
    	var ticketArr = [];
    	var id = "";
    	if(parent.grid){
    		var rows = parent.grid.getSelectedRows();
        	id = rows.attr("recordId");
    	}else{
    		id = parent.reserveId;
    	}
    	
    	if(isall==2){
    		var data = {};
    		var ses = parent.grid2.getSelectedRows();
    		var ticketArr = [];
    		ses.each(function(i,r){
    			ticketArr[i] = $(r).attr("recordId");
    		})
    		$.ajax({
        		url:'<%=path%>/public/tool/queryTicketReserveSome.htmls',
        		type:"POST",
        		dataType:"json",
        		data:{id:id,'ticketArr':ticketArr},
        		async:false,
        		success:function(ret){
        			data = ret;
        		}
        	})
    	}else{//全部
    		$.ajax({
        		url:'<%=path%>/public/tool/queryTicketReserve.htmls',
        		type:"POST",
        		dataType:"json",
        		async:false,
        		data:{id:id},
        		success:function(ret){
        			data = ret;
        		}
        	})
    	}
    	if(rows){
    		$('#memberNo').val($(parent.document).find("td[name=memberNo]").html());
    		$('#sallerCode').val(rows.find("td[name=sallerCode]").attr("value"));
    	}else{
    		$('#sallerCode').val($(parent.document).find("td[name=sallerCode]").html());
    		$('#memberNo').val($(parent.document).find("td[name=memberNo]").html());
    	}
    	
    	var table = $('#itemTable');
    	var totalSize = 0,amount = 0;
    	var arr = _.map(data,function(value,key){
    		var area = areas[key];
    		if(area){
    			var size = data[key].length; 
        		totalSize += size;
        		var money = size * area.price ;
        		amount += money;
        		return '<tr name="itemtr"><td align="center">'+area.name+'</td><td style="text-align:center" name="price">'+area.price 
        		+'</td><td style="text-align:center" name="size">'+size+'</td><td style="text-align:center">'
        		+money+'</td><td align="center" name="realPrice">'+area.price+'</td><td align="center" name="money">'+money+'</td></tr>';
    		}
    	})
    	
    	for(var i=0;i<arr.length;i++){
    		$(arr[i]).appendTo(table);
    	}
    	$('<tr style="color:green;font-weight:bold"><td align="center">总计</td><td align="center">---</td><td name="totalSize" align="center">'+totalSize
    		+'</td><td align="center">'+amount+'</td><td align="center">---</td><td align="center" name="amount">'+amount+'</td></tr>').appendTo(table);
    	dicSelect(dic0207).width(150).appendTo($('div[name=payType]')).attr("id","payType").val("0207001");
    	//拼凑门票策略下拉框
    	var select = joinSelect(strategys,"strategy").width(150).appendTo($('div[name=strategy]')).change(function(e){
    		var json = strategys[this.value];
    		if(json.valueType==="0208001"){
   				if(json.discount){
    				$('#discount').val(json.discount).closest("tr").show();
        			$('#cheapPrice').val(0).closest("tr").hide();
        			$('#offsetPrice').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
    			}else if(json.price){
        			$('#realPrice').val(json.price).closest("tr").show();
        			$('#offsetPrice').val(0).closest("tr").hide();
        			$('#cheapPrice').val(0).closest("tr").hide();
        			$('#discount').val(0).closest("tr").hide();
        		}else{
        			$('#cheapPrice').val(json.cheapPrice).closest("tr").show();
        			$('#offsetPrice').val(0).closest("tr").hide();
        			$('#discount').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
        		}
    			$('#payType').val("0207001");
    			$('#payType option[value!=0207003]').attr("disabled",false);
    			$('#payType option[value=0207003]').attr("disabled",true);
    		}else if(json.valueType==="0208003"){
    			$('#offsetPrice').val(json.offsetPrice).closest("tr").show();
    			$('#cheapPrice').val(0).closest("tr").show();
    			$('#discount').val(0).closest("tr").hide();
    			$('#realPrice').val(0).closest("tr").hide();
    		}else{//免费
    			$('#cheapPrice').val(0).closest("tr").hide();
    			$('#offsetPrice').val(0).closest("tr").hide();
    			$('#discount').val(0).closest("tr").hide();
    			$('#realPrice').val(0).closest("tr").hide();
    			$('#payType').val("0207003");
    			$('#payType option[value=0207003]').attr("disabled",false);
    			$('#payType option[value!=0207003]').attr("disabled",true);
    		}
    		recalcu();
    	}).val(2).trigger("change");
    	$('#cheapPrice').keyup(function(e){
    		if(e.keyCode>=47&&e.keyCode<=57){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	$('#cancel.btn-danger').click(function(e){
    	    closeWin();
    	})
    	$('#set.btn-danger').click(function(e){
    	    $("#setting").toggle();
    	})
    	$(".btn-success").click(function(e){
    		var strategy = $("#strategy").val();
    		if(strategy==""){
    			layer.msg("请选择售票方式");
    			return;
    		}
    		var param = {};
    		var cheapPrice = $("#cheapPrice").val();
    		var totalSize = $("td[name=totalSize]").html();
    		var sallerCode = $("#sallerCode").val();
    		var descs = $("#descs").val();
    		if($.trim(sallerCode)!=""){
    			param.sallerCode = sallerCode;
    		}
    		if($.trim(descs)!=""){
    			param.descs = descs;
    		}
    		param.cheapPrice = cheapPrice;
    		param.payType = $('#payType').val();
    		param.discount = $('#discount').val();
    		param.strategy = strategy;
    		param.offsetPrice = $("#offsetPrice").val();
    		param.totalSize = totalSize;
    		param.status = "0203001";
    		param.memberNo = $("#memberNo").val();
    		if(param.memberNo!=""){
    			if(sallerCode==""){
    				layer.msg("营销编号不能为空");
    				return;
    			}
    		}
    		var customerName = $("#customerName").val();
    		if(customerName){
    			param.customerId = $("#customerId").val();
    		}
    		param.priceShowType = $(':radio[name=priceShowType]:checked').val();
    		if(param.priceShowType==="0209003"){
    			var fudong = $("#fudong").val();
    			if(fudong===''){
    				alert("浮动金额不能为空");
    				return;
    			}
    			param.plusShowPrice = fudong;
    		}
    		param.realPrice = $("#realPrice").val();//最低消费
    		param.isShowSeat = $("#isShowSeat").is(":checked");
    		param.hasTea = $("#hasTea").is(":checked")?"0103001":"0103002";
    		var giftNum = $("#giftNum").val();
    		param.giftNum = giftNum;
    		if(isall==2){
    			var rows2 = parent.grid2.getSelectedRows();
    			var arr = [];
    			rows2.each(function(i,tr){
    				arr.push($(tr).attr("recordId"));
    			})
    			BaseDwr.printReserved(id,arr,JSON.stringify(param),function(ret){
    				if(ret.status===0){
        				parent.parent.ticketArr = ret.list;
            			closeWin();
        			}else if(ret.status===1){
        				$("button").attr("disabled",false);
        				layer.msg("营销编号不存在");
        			}else{
        				layer.msg("出票失败");
        			}
        		});
    		}else{
    			BaseDwr.printReservedAll(id,JSON.stringify(param),function(ret){
    				if(ret.status===0){
        				parent.parent.ticketArr = ret.list;
            			closeWin();
        			}else if(ret.status===1){
        				$("button").attr("disabled",false);
        				layer.msg("营销编号不存在");
        			}else{
        				layer.msg("出票失败");
        			}
        		});
    		}
    		
    	})
    	$(":input[name=priceShowType]").change(function(e){
    		if($("#priceShowType3").is(":checked")){
    			$('#fudong').show();
    		}else{
    			$('#fudong').hide();
    		}
    	});
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
<div class="container-fluid" style="padding: 0;">
  <div class="row-fluid" style="margin: 0">
<table width="100%" border="0">
<tr valign="top"><td style="padding-right: 10px;width:65%">
                <div class="widget-box" style="width:100%">
          <div class="widget-content nopadding">
            <table class="table table-bordered table-striped" id="itemTable">
              <thead>
                <tr>
                  	<th width="140">片区</th>
					<th width="60">价格</th>
					<th width="60">人数</th>
					<th width="60">原价金额</th>
					<th width="60">优惠价</th>
					<th>实收金额</th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
        </td><td>
              <div class="widget-box">
        <div class="widget-content">
          <table class="table-bordered table-striped" id="bussiness" style="padding: 0">
          <tr>
          <td width="90">售票方式 :</td><td><div class="controls" name="strategy"></div></td>
          </tr>
          <tr>
          <td>每张立减 :</td><td><input type="text" name="cheapPrice" id="cheapPrice" class="span11" value="0"/></td>
          </tr>
          <tr>
          <td>抵扣票价 :</td><td><input type="text" name="offsetPrice" id="offsetPrice" class="span11" value="0"/></td>
          </tr>
          <tr>
          <td>折扣比例 % :</td><td><input type="text" name="discount" id="discount" class="span11" value="0"/></td>
          </tr>
          <tr>
          <td>最低消费 :</td><td><input type="text" name="realPrice" id="realPrice" class="span11" value="0" /></td>
          </tr>
          <tr>
          <td>会员卡号 :</td><td><input type="text" class="span11" name="memberNo" id="memberNo"  /></td>
          </tr>
          <tr>
          <td>营销编号 :</td><td><input type="text" class="span11" name="sallerCode" id="sallerCode"/></td>
          </tr>
          <tr>
          <td>客户名称 :</td><td><input type="text" class="span11" name="customerName" id="customerName"/><input type="hidden" name="customerId" id="customerId"/></td>
          </tr>
          <tr>
          <td>备注信息 :</td><td><input type="text" class="span11" name="descs" id="descs"/></td>
          </tr>
          <tr>
          <td width="90">付款方式 :</td><td><div class="controls" name="payType"></div></td>
          </tr>
          </table>
            <div class="form-actions">
              <button type="button" class="btn btn-success">出售</button>
              <button type="button" id="set" class="btn btn-danger">设置</button>
              <button type="button" id="cancel" class="btn btn-danger">取消</button>
            </div>
        </div>
      </div>
      </td></tr>
      <tr>
    <td colspan="2" align="right" style="padding-right:20px">
    <%@include file="/WEB-INF/pages/sell/job/print_setting.jspf"%>
    </td>
    </tr>
      </table>
<!-- 第一行：售票信息结束 -->
  </div>
</div></div>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>
