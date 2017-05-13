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

<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<SCRIPT type="text/javascript">
    <!--
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = top.areas;
    var oldAreaId = null;
    var seatId = '${param.seatId}';
    var td = $(parent.document).find("td[oid="+seatId+"]");
    
    var index = parent.layer.getFrameIndex(window.name);
    var closeWin = function(){
	    parent.layer.close(index);
    }
    function myJoinSelect(strategys,id){
    	strategys = _.filter(strategys,function(obj){
    		return obj.valueType==="0208003";
    	})
    	var str = _.map(strategys,function(obj,key){
			return "<option value='"+obj.id+"'>" + obj.name + "</option>";
		})
		return $('<select id="'+id+'">'+str+'</select>')//.append(str);
	}
    var playDate = $(parent.document).find("#playDate").val();
    $(function(){
    	$('#number').focus().bind("focus",function(e){
    		this.select();
    	});
    	$("#newSeatName").val(td.attr("text"));
    	$("#newPlayDate").val(playDate);
    	var area = areas[td.attr("areaId")];
    	$("#newArea").val(area.name);
    	$("#price").val(area.price);
    	$("#realPrice").val(area.price);
    	//拼凑门票策略下拉框
    	var select2 = joinSelect(strategys,"strategy2").appendTo($('td[name=strategy2]')).attr("disabled",true);
    	
    	var select = myJoinSelect(strategys,"strategy").appendTo($('td[name=strategy]'));
    	
    	$('#number').keyup(function(e){
    		var keycode = e.keyCode;
    		if(keycode===13){
    			$.ajax({
    				url:'<%=path%>/public/tool/queryTicketInfoForChange.htmls',
    				data:{number:this.value},
    				type:"POST",
    				dataType:"json",
    				success:function(ret){
    					$('#cheapPrice').attr("disabled",true);
    					$('#cheapPrice2').closest("tr").hide();
    					$('#offsetPrice2').closest("tr").hide();
    					$('#discount2').closest("tr").hide();
    					if(ret.responseStatus==0){
    						layer.msg("票号不存在");
    						$('#number').val("").focus();
    						$(".btn-success").attr("disalbed",true);
    					}else{
    						$('#cheapPrice').attr("disabled",false);
    						$('#oldSeatName').val(ret.seatName);
    						$('#oldPlayDate').val(ret.playDate);
    						$('#strategy2').val(ret.strategyId);
    						$('#oldAreaName').val(ret.areaName);
    						oldAreaId = ret.areaId;
    						$('#sallerCode').val(ret.sallerCode);
    						if(ret.cheapPrice>0){
    							$('#cheapPrice2').val(ret.cheapPrice);
    							$('#cheapPrice2').closest("tr").show();
    						}
    						if(ret.offsetPrice>0){
    							$('#offsetPrice2').val(ret.offsetPrice);
    							$('#offsetPrice2').closest("tr").show();
    						}
    						if(ret.discount>0){
    							$('#discount2').val(ret.discount);
    							$('#discount2').closest("tr").show();
    						}
    						
    						$('#price2').val(ret.price);
    						$('#discount2').val(ret.discount);
    						if(ret.hasTea==="0103001"){
    							$('#hasTea').attr("checked",true);
    						}else{
    							$('#hasTea').attr("checked",false);
    						}
    						if(ret.isShowSeat==="0103001"){
    							$('#isShowSeat').attr("checked",true);
    						}else{
    							$('#isShowSeat').attr("checked",false);
    						}
    						$('#gift').val(ret.giftNum);
    						$('#realPrice').val(ret.price);
    						$('#realPrice2').val(ret.realPrice);
    						var chajia = area.price - ret.realPrice;
    						$('#chajia').val(chajia);
    						if(ret.responseStatus==2){
        						layer.msg("门票已过期");
        						$('#number').val("").focus();
        						$(".btn-success").attr("disabled",true);
        					}else{
        						if(chajia<0){
        							layer.msg("差价不能小于0");
        							$(".btn-success").attr("disabled",true);
        						}else{
        							$(".btn-success").attr("disabled",false);
        						}
        						
        					}
    						
    					}
    				}
    			})
    		}
    	})
    	$("#cheapPrice").keyup(function(e){
    		//16042300016317
    		var cheapPrice = $("#cheapPrice").val();
    		var price = $("#price").val();
    		if(cheapPrice>0){
    			$("#chajia").val(area.price-cheapPrice-$('#realPrice2').val());
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	$(".btn-success").click(function(e){
    		var number = $("#number").val();
    		var descs = $("#descs").val();
    		var giftNum = $("#gift").val();
    		var chajia = $('#chajia').val();
    		var cheapPrice = $('#cheapPrice').val();
    		var isShowSeat = $(":input[name=isShowSeat]").is(":checked")?"0103001":"0301002";
    		var hasTea = $(":input[name=hasTea]").is(":checked")?"0103001":"0301002";
    		var priceShowType = $(":input[name=priceShowType]:checked").val();
    		BaseDwr.changeSeat(seatId,number,chajia,cheapPrice,$("#strategy").val(),descs
    				,giftNum,isShowSeat,hasTea,priceShowType,function(ret){
    			if(ret==2){
    				layer.msg("票号不存在");
    			}else{
    				if(chajia>0){
    					parent.ticketArr = ret;
    				}else{
    					parent.synccolor(playDate,[td.attr('areaId'), oldAreaId]);
    				}
        			closeWin();
    			}
    		});
    	})
    	$('#cancel').click(function(e){
    	    closeWin();
    	})
    	$('#set').click(function(e){
    	    $('#setting').toggle();
    	})
    })
   
    //-->
</SCRIPT>
<style type="text/css">
.widget-content td{
	text-align: center;
}
.span11{
	padding: 0;margin: 2px!important;width:80%!important
}
</style>
</head>

<body style="margin:0;padding: 0">
<div id="content"><br/>
<table border="0" width="100%">
	<tr>
          <td width="90" colspan="2" style="font-size:20px;padding-left: 30px">门票号码 :
          <input type="text" tabindex="1" name="number" style="width:180px" id="number"/>
          <span id="tips" style="color:red;font-size:15px;text-align: left"></span>
          </td>
    </tr>
    <tr valign="top">
		<td style="padding-right: 10px;width:50%">
      <div class="widget-box">
        <div class="widget-title"> <span class="icon"> <i class="icon-align-justify"></i> </span>
          <h5>新票信息</h5>
        </div>
        <div class="widget-content">
          <table class="table-bordered table-striped"  style="padding:0;width:100%">
          <tr>
          <td width="90">演出日期:</td><td style="width:200px"><input type="text" class="span11" name="newPlayDate" id="newPlayDate" disabled /></td>
          </tr>
          <tr>
          <td>座位片区 :</td><td><input type="text" class="span11" name="newArea" id="newArea" disabled/></td>
          </tr>
          <tr>
          <td>座位序列 :</td><td><input type="text" class="span11" name="newSeatName" id="newSeatName" disabled placeholder="新座位序列" /></td>
          </tr>
          
          <tr>
          <td align="center">售票方式 :</td><td name="strategy"></td>
          </tr>
          <tr>
          <td>门票价格 :</td><td><input type="text" class="span11" name="price" id="price" readOnly /></td>
          </tr>
          <tr>
          <td>每张立减 :</td><td><input type="text" class="span11" name="cheapPrice" id="cheapPrice" value="0" disabled/></td>
          </tr>
          <tr>
          <td style="color:red">应补差价 :</td><td><input type="text" style="color:red;font-weight:bold" class="span11" name="chajia" id="chajia" readOnly /></td>
          </tr>
          <tr>
          <td>备注信息 :</td><td><input type="text" class="span11" name="descs" id="descs" /></td>
          </tr>
          <tr>
    <td align="right" colspan="2" style="padding-right:20px;padding-left:20px">
    <table border="0" id="setting" style="display: none">
    	
    	<tr>
    	<td><label for="hasTea"><input type="checkbox" name="hasTea" id="hasTea"/>含茶</label></td>
    	<td><label for="isShowSeat"><input type="checkbox" checked name="isShowSeat" id="isShowSeat"/>显示座位</label></td>
    	<td align="right" valign="top">赠送水果：</td>
    	<td>
    	<input type="text" name="gift" style="width:30px;height: 10px;" id="gift"/>份</td>
    	</tr>
    	<tr>
    	<td width="100"><label for="priceShowType1"><input type="radio" checked="checked" name="priceShowType" id="priceShowType1" value="0209001"/>实价打印</label></td>
    	<td width="100"><label for="priceShowType2"><input type="radio" name="priceShowType" id="priceShowType2" value="0209002"/>原价打印</label></td>
    	<td width="80"><label for="priceShowType3"><input type="radio" name="priceShowType" id="priceShowType3" value="0209003"/>浮动打印</label></td>
    	<td width="80" align="left"><input type="text" id="fudong" style="display:none;width:30px;height:20px;padding:0"/></td>
    	</tr>
    </table>
    </td>
    </tr>
          </table>
        </div>
      </div>
      </td>
      <td>
            <div class="widget-box" style="width:100%">
        <div class="widget-title"> <span class="icon"> <i class="icon-align-justify"></i> </span>
          <h5>旧票信息</h5>
        </div>
        <div class="widget-content">
         <table class="table-bordered table-striped"  style="padding:0;width:100%">
          <tr>
          <td width="90">演出日期 :</td><td><input type="text" class="span11" readOnly name="oldPlayDate" id="oldPlayDate"/></td>
          </tr>
          <tr>
          <td>座位片区 :</td><td><input type="text" class="span11" readOnly name="oldAreaName" id="oldAreaName"/></td>
          </tr>
          <tr>
          <td>座位序列 :</td><td><input type="text" class="span11" readOnly name="oldSeatName" id="oldSeatName"/></td>
          </tr>
          <tr>
          <td width="90" align="center">售票方式 :</td><td align="center" name="strategy2"></td>
          </tr>
          <tr style="display:none">
          <td>每张立减 :</td><td><input type="text" class="span11" name="cheapPrice" id="cheapPrice2" readOnly /></td>
          </tr>
          <tr style="display:none">
          <td>折扣比例% :</td><td><input type="text" class="span11" name="discount" id="discount2" readOnly /></td>
          </tr>
         <tr style="display:none">
          <td>抵扣票价 :</td><td><input type="text" class="span11" name="offsetPrice" id="offsetPrice2" readOnly /></td>
          </tr>
           <tr>
          <td>原票价格 :</td><td><input type="text" class="span11" name="price" id="price2" readOnly /></td>
          </tr>
          <tr>
          <td>实际价格 :</td><td><input type="text" class="span11" name="realPrice" id="realPrice2" readOnly /></td>
          </tr>
          </table>
            <div class="form-actions">
              <button type="button" class="btn btn-success" disabled>换座</button>
              <button type="button" id="set" class="btn btn-danger">设置</button>
              <button type="button" id="cancel" class="btn btn-danger">取消</button>
            </div>
        </div>
      </div>
      </td>
      </tr></table>
</div>
    <SCRIPT type="text/javascript">
    <!--
    $(":input[name=priceShowType]").change(function(e){
    		if($("#priceShowType3").is(":checked")){
    			$('#fudong').show();
    		}else{
    			$('#fudong').hide();
    		}
    	});
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
        //-->
</SCRIPT>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script src="<%=path %>/static/special/js/jquery.min.js"></script> 
<script src="<%=path %>/static/special/js/bootstrap.min.js"></script> 
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
</body>
</html>
