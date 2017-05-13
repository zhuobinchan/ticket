<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<SCRIPT type="text/javascript">
    <!--
	var ticketId = '${param.ticketId}';
	genDicJs(['0207']);
	
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = genMaps("from Area order by code");
    var closeWin = function(){
    	var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.close(index);
    }
    var recalcu = function(){
		var realPrice = 0;
		var size = 1;
		if($('#payType').val()!=="0207003"){
			var cp = $('td#areaPrice').html();
			var discount = $('#discount').val();
			var minPrice = $('#realPrice').val();
			realPrice = cp - $('#cheapPrice').val() - $('#offsetPrice').val();
			if(discount>0){
				realPrice -= (cp*(100-discount)/100)
			}else if(minPrice>0){
				realPrice = minPrice;
			}
			realPrice = Math.floor(realPrice*0.1)*10;
		}
		$('td#encloseRealPrice').html(realPrice);
    }
    var reserveId,kaName = '${param.kaName}';
    $(function(){
    	$.ajax({
    		url:"<%=path%>/public/tool/getReserveInfoByTicketId.htmls",
    		data:{ticketId:ticketId},
    		dataType:"json",
    		type:"POST",
    		success:function(ret){
    			reserveId = ret.id;
    			$('#sallerCode').val(ret.sallerCode);
    			$('#descsTd').html(ret.descs);
    			$('#createUserNameTd').html(ret.createUserName);
    			$('#memberNo').val(ret.memberNo);
    			$('#createTimeTd').html(new Date(ret.createTime).format("yyyy-MM-dd HH:mm"));
    			$('#playDateTd').html(new Date(ret.playDate).format("yyyy-MM-dd"));
    		}
    	})
    	var areaId = $(parent.document).find("td[ticketId="+ticketId+"]").attr("areaId");
    	var area = areas[areaId];
    	var size = area.defaultNum;
    	$('#areaName').html(area.name +"（${param.kaName}）");
    	$('#userNum').val(size).focus(function(){
    		this.select();
    	}).blur(function(){
    		if($.trim(this.value)==""||this.value==0){
    			this.value = size;
    		}
    	});
    	if(kaName!=="长"&&kaName!=="湖"){
    		if(kaName.length==2){
        		size = 18;
        		$('#areaPrice').html(area.price);
            	$('#encloseRealPrice').html(area.price);
        	}else{
        		if(kaName.indexOf("1")!=-1){
            		$('#areaPrice').html(2580);
                	$('#encloseRealPrice').html(2580);
        		}else{
        			$('#areaPrice').html(2380);
                	$('#encloseRealPrice').html(2380);
        		}
        	}
    	}else{
    		if(kaName==="长"){
        		$('#areaPrice').html(2680);
            	$('#encloseRealPrice').html(2680);
    		}else{
    			$('#areaPrice').html(2280);
            	$('#encloseRealPrice').html(2280);
    		}	
    	}
    	dicSelect(dic0207).width(150).appendTo($('div[name=payType]')).attr("id","payType").val("0207001");
    	//拼凑门票策略下拉框
    	var select = joinSelect(strategys,"strategy").width(150).appendTo($('div[name=strategy]')).change(function(e){
    		var json = strategys[this.value];
    		if(json.valueType==="0208001"||json.valueType==="0208003"){
    			if(json.discount){
    				$('#discount').val(json.discount).closest("tr").show();
        			$('#cheapPrice').val(0).closest("tr").hide();
        			$('#offsetPrice').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
    			}else if(json.offsetPrice){
        			$('#offsetPrice').val(json.offsetPrice).closest("tr").show();
        			$('#cheapPrice').val(0).closest("tr").hide();
        			$('#discount').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
        		}else if(json.price){//指定价格
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
        $(":input[name=priceShowType]").change(function(e){
    		if($("#priceShowType3").is(":checked")){
    			$('#fudong').show();
    		}else{
    			$('#fudong').hide();
    		}
    	});
        $('#cheapPrice').keyup(function(e){
        	if((e.keyCode>=47&&e.keyCode<=57)||e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
        $('#discount').keyup(function(e){
        	if((e.keyCode>=47&&e.keyCode<=57)||e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
        $('#offsetPrice').keyup(function(e){
        	if((e.keyCode>=47&&e.keyCode<=57)||e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
        $('#closewin.btn-danger').click(function(e){
    	    closeWin();
    	})
        $('#back.btn-danger').click(function(e){
        	layer.confirm("确定退订吗？",{btn:["确定","取消"]},function(index){
				   BaseDwr.unreserveAll(reserveId,function(ret){
					   parent.synccolor(parent.$('#playDate').val(),[areaId]);
					   layer.close(index);
					   closeWin();
				   });  
			   });
    	})
    	$.ajax({
        	url:"${pageContext.request.contextPath}/public/tool/getCustomerArr.htmls",
        	type:"POST",
        	dataType:"json",
        	success:function(ret){
        		$("#customerName").autocomplete({
        			minLength: 0,
	      		    source: ret,
	      		    select: function( event, ui ) {
	      		    	$("#customerName").val(ui.item.name );
	      		    	$("#customerId").val( ui.item.id );
	      		        return false;
	      		    }
                });
        	}
        })
    	$('#memberNo').keyup(function(e){
			if(e.keyCode==13){
				if($('#memberNo').val()!=""){
					$.ajax({
	    				url:g_path+'/public/tool/getSalesmanByMemberNo.htmls',
	    				data:{memberNo:$('#memberNo').val()},
	    				async:false,
	    				success:function(ret){
	    					if(ret.status!=1){
	    						layer.msg("无效卡号");
	    						$('#memberNo').val("");
	    						$('#sallerCode').val("").attr("disabled",false);
	    					}else{
	    						$('#sallerCode').val(ret.sallerCode).attr("disabled",true);
	    					}
	    				}
	    			})
				}
				
			}
		})
    	$(".btn-success").click(function(e){
    		var strategy = $("#strategy").val();
    		if(strategy==""){
    			layer.msg("请选择售票方式");
    			return;
    		}
    		var param = {};
    		param.strategy = strategy;
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
    		param.offsetPrice = $("#offsetPrice").val();
    		param.userNum = $("#userNum").val();
    		param.discount = $("#discount").val();
    		param.realPrice = $("#realPrice").val();
    		param.memberNo = $("#memberNo").val();
    		if(param.memberNo!=""){
    			if(sallerCode==""){
    				layer.msg("营销编号不能为空");
    				return;
    			}
    		}
    		param.totalSize = totalSize;
    		var priceShowType = $(':radio[name=priceShowType]:checked').val();
    		param.priceShowType = priceShowType;
    		if(priceShowType==="0209003"){
    			param.plusShowPrice = $("#fudong").val();
    		}
    		var customerName = $("#customerName").val();
    		if(customerName){
    			param.customerId = $("#customerId").val();
    		}
    		param.isShowSeat = $("#isShowSeat").is(":checked");
    		param.hasTea = $("#hasTea").is(":checked")?"0103001":"0103002";
    		var giftNum = $("#giftNum").val();
    		param.giftNum = giftNum;
    		param.status = "0203001";
    		param.playDate = $(parent.document).find("#playDate").val();
    		param.showNumberId = $(parent.document).find("#showNumber").val();
    		$("button").attr("disabled",true);
    		BaseDwr.saleBoxReserved(reserveId,JSON.stringify(param),function(ret){
    			if(ret.status===0){
    				parent.ticketArr = ret.list;
        			closeWin();
    			}else if(ret.status===1){
    				$("button").attr("disabled",false);
    				layer.msg("营销编号不存在");
    			}else{
    				layer.msg("出票失败");
    			}
    		});
    	})
    })
    //-->
</SCRIPT>
<style type="text/css">
.widget-content td{
	text-align: center;
}
table#itemTable td{
	padding-left: 40px;
}
.span11{
	padding: 0;margin: 2px!important;
}
tr{
white-space:nowrap;
}
</style>
</head>

<body style="margin:0;padding: 0">
<div id="content">
<div class="container-fluid" style="padding: 0;">
  <div class="row-fluid" style="padding:0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top"><td style="padding-right: 10px;width:63%">
                <div class="widget-box" style="padding:5px;width:100%;border:0 solid">
            <table class="table table-bordered table-striped" id="itemTable">
                <tr>
                  	<td width="100">包厢名称</td><td id="areaName"></td>
                  	</tr>
                  	<tr>
					<td>包厢价格</td>
					<td id="areaPrice">1</td>
					</tr>
                  	<tr>
					<td>实际价格</td>
					<td id="encloseRealPrice">1</td>
					</tr>
                  	<tr>
					<td>演出时间</td>
					<td id="playDateTd">&nbsp;</td>
					</tr>
                  	<tr>
					<td>备注信息</td>
					<td id="descsTd">&nbsp;</td>
					</tr>
                  	<tr>
					<td>预订时间</td>
					<td id="createTimeTd">&nbsp;</td>
					</tr>
                  	<tr>
					<td>操作员</td>
					<td id="createUserNameTd">&nbsp;</td>
					</tr>
            </table>
        </div>
    </td><td rowspan="2">
      <div class="widget-box">
        <div class="widget-content" style="height: 400px">
          <table class="table-bordered table-striped" id="bussiness" style="padding: 0">
          <tr>
          <td width="90">售票方式 :</td><td><div class="controls" name="strategy" style="padding-top:2px"></div></td>
          </tr>
          <tr>
          <td>每张立减 :</td><td><input type="text" name="cheapPrice" id="cheapPrice" class="span11" value="0" /></td>
          </tr>
          <tr>
          <td>抵扣票价 :</td><td><input type="text" name="offsetPrice" id="offsetPrice" class="span11" value="0" /></td>
          </tr>
          <tr>
          <td>折扣比例 % :</td><td><input type="text" name="discount" id="discount" class="span11" value="0" /></td>
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
          <td>备注信息 :</td><td><input type="text" class="span11" name="descs" id="descs" /></td>
          </tr>
          <tr>
          <td width="90">付款方式 :</td><td><div class="controls" name="payType"></div></td>
          </tr>
          </table>
            <div class="form-actions">
              <button type="button" class="btn btn-success">出售</button>
              <button type="button" id="back" class="btn btn-danger">退订</button>
              <button type="button" id="closewin" class="btn btn-danger">取消</button>
            </div>
        </div>
      </div><!-- 第一行：售票信息结束 -->
    </td></tr>
    <tr>
    <td align="right" style="padding-right:20px;padding-left:20px;font-size:14px">
    <table border="0" style="width:95%" id="setting">
    	<tr>
    	<td height="30px"><label for="hasTea"><input type="checkbox" checked name="hasTea" id="hasTea"/>含茶</label></td>
    	<td><label for="isShowSeat"><input type="checkbox" checked name="isShowSeat" id="isShowSeat"/>显示座位</label></td>
    	<td align="right" valign="top">赠送水果：</td>
    	<td colspan="3">
    	<input type="text" name="giftNum" value="1" style="width:30px;height: 10px;" id="giftNum"/> 份</td>
    	
    	</tr>
    	<tr>
    	<td height="30px" width="100"><label for="priceShowType1"><input type="radio" checked="checked" name="priceShowType" id="priceShowType1" value="0209001"/>正常打印</label></td>
    	<td width="100"><label for="priceShowType2"><input type="radio" name="priceShowType" id="priceShowType2" value="0209002"/>原价打印</label></td>
    	<td width="80"><label for="priceShowType3"><input type="radio" name="priceShowType" id="priceShowType3" value="0209003"/>浮动打印</label></td>
    	<td width="80" align="left"><input type="text" name="fudong" style="display:none;width:30px;height: 10px;" id="fudong"/></td>
    	<td align="right" valign="top">看台人数：</td>
    	<td>
    	<input type="text" name="userNum" style="width:30px;height: 10px;" id="userNum"/></td>
    	</tr>
    </table>
    </td>
    </tr>
    </table>
  </div>
</div></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
</body>
</html>
