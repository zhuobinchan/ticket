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
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<link rel="stylesheet" href="<%=path %>/static/jquery-ui/jquery-ui.theme.min.css" />
<SCRIPT type="text/javascript">
    <!--
	genDicJs(['0207']);
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = top.areas;
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
				realPrice = Math.floor(realPrice*0.1)*10;
			}else if(minPrice>0){
				realPrice = minPrice;
			}
			
		}
		$('td#encloseRealPrice').html(realPrice);
    }
    var kaName = '${param.name}';
    $(function(){
    	var tdObj = $(parent.document).find("td[kaName="+kaName+"]"); 
    	var areaId = tdObj.attr("areaId");
    	var seatId = tdObj.attr("oid");
    	var area = areas[areaId];
    	var size = area.defaultNum;
    	if(kaName!=="长"&&kaName!=="湖"){
    		if(kaName.length==2){
        		size = 18;
        		$('#areaPrice').html(area.price);
            	$('#encloseRealPrice').html(area.price);
            	tdObj.closest("table").find("button[kaName^="+kaName+"]").each(function(i,v){
    				if($(v).prop("disabled")){
    					$('button.btn-success').attr("disabled",true);
    					return false;
            		}
            	});
        	}else{
        		$('button.btn-success').attr("disabled",tdObj.closest("table").find("button[kaName="+kaName+"]").prop("disabled"));
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
    	
    	$('#areaName').html(area.name+" （${param.name}）");
    	$('#userNum').val(size).focus(function(){
    		this.select();
    	}).blur(function(){
    		if($.trim(this.value)==""||this.value==0){
    			this.value = size;
    		}
    	});
    	
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
        $('.btn-danger').button().click(function(e){
    	    closeWin();
    	})
    	$('#fudong').blur(function(e){
    			if($.trim(this.value).length>0){
    				var totalMoney = $('#areaPrice').text();
    				var amount = $('#encloseRealPrice').text();
    				if((amount+this.value)>totalMoney){
    					this.value = "";
    					alert("打印价格不能超出原价金额");
    				}
    			}
    		}).keypress(function(){return onlyDigit(event)});
    	
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
    	$(".btn-success").button().click(function(e){
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
    		param.areaName = $('#areaName').html();
    		param.realPrice = $("#realPrice").val();
    		param.price = $('#areaPrice').html();
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
    			var fudong = $("#fudong").val();
    			if(fudong===''){
    				alert("浮动金额不能为空");
    				return;
    			}
    			param.plusShowPrice = fudong;
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
    		
    		BaseDwr.saleBox(seatId,JSON.stringify(param),function(ret){
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
	input{
		width: 140px;
	}
	.sarrow{
		width:40px;
	}
</style>
</head>

<body>
<table width="100%" border="1" style="border-collapse: collapse;" cellpadding="5" cellspacing="0">
	<tr>
		<td colspan="4">
		<table cellpadding="0" cellspacing="0">
		<tr valign="top">
		<td width="420">
			<table id="itemTable" style="border:1px solid" width="400">
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
            </table>
		</td>
		<td align="center">
		  <table id="bussiness" border="1" style="border-collapse: collapse;" cellpadding="5">
          <tr>
          <td width="90">售票方式 :</td><td><div class="controls" name="strategy"></div></td>
          </tr>
          <tr>
          <td>每张立减 :</td><td><input type="text" name="cheapPrice" id="cheapPrice" value="0" /></td>
          </tr>
          <tr>
          <td>抵扣票价 :</td><td><input type="text" name="offsetPrice" id="offsetPrice" value="0" /></td>
          </tr>
          <tr>
          <td>折扣比例 % :</td><td><input type="text" name="discount" id="discount" value="0" /></td>
          </tr>
          <tr>
          <td>最低消费 :</td><td><input type="text" name="realPrice" id="realPrice" value="0" /></td>
          </tr>
          <tr>
          <td>会员卡号 :</td><td><input type="text" name="memberNo" id="memberNo"  /></td>
          </tr>
          <tr>
          <td>营销编号 :</td><td><input type="text" name="sallerCode" id="sallerCode"/></td>
          </tr>
          <tr>
          <td>客户名称 :</td><td><input type="text" name="customerName" id="customerName"/><input type="hidden" name="customerId" id="customerId"/></td>
          </tr>
          <tr>
          <td>备注信息 :</td><td><input type="text" name="descs" id="descs" /></td>
          </tr>
          <tr>
          <td width="90">付款方式 :</td><td><div class="controls" name="payType"></div></td>
          </tr>
          </table>
		</td>
		</tr>
		</table>
		</td>
	</tr>
	
    	<tr>
    	<td height="30px" width="180"><input class="sarrow" type="checkbox" checked name="hasTea" id="hasTea"/><label for="hasTea">含茶</label></td>
    	<td width="180"><input class="sarrow" type="checkbox" checked name="isShowSeat" id="isShowSeat"/><label for="isShowSeat">显示座位</label></td>
    	<td colspan="2">
    	赠送水果<input type="text" name="giftNum" value="1" style="width:40px;text-align: center" id="giftNum"/> 份</td>
    	</tr>
    	<tr>
    	<td height="30px">
    	<input class="sarrow" type="radio" checked="checked" name="priceShowType" id="priceShowType1" value="0209001"/><label for="priceShowType1">正常打印</label></td>
    	<td><input class="sarrow" type="radio" name="priceShowType" id="priceShowType2" value="0209002"/><label for="priceShowType2">原价打印</label></td>
    	<td align="left" width="180">
    	<input class="sarrow" type="radio" name="priceShowType" id="priceShowType3" value="0209003"/><label for="priceShowType3">浮动打印</label>
    	<input type="text" name="fudong" style="display:none;width:30px;" id="fudong"/>
    	</td>
    	<td width="200" align="left" valign="middle">看台人数
    	<input type="text" name="userNum" style="width:40px;text-align: center" id="userNum"/></td>
    	</tr>

	<tr>
		<td colspan="4" align="center" style="padding: 10px">
		<button type="button" class="btn btn-success">出售</button>
        <button type="button" class="btn btn-danger">取消</button>
		</td>
	</tr>
</table>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
</body>
</html>
