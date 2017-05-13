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
<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<jsp:include page="/WEB-INF/pages/base/bussiness/include.jsp" />
<script type='text/javascript' src='<%=path%>/static/js/LodopFuncs.js'></script>
<script type='text/javascript' src='<%=path%>/static/js/date.js'></script>
<SCRIPT type="text/javascript">
    <!--
    var data = parent.data;
    genDicJs(['0207']);
    var strategys = genMaps("from TicketStrategy where name <> '不定期售票' order by orderno");
    // 优惠算法
    var strategyAlgorithm = null;
    // 消费/充值卡折扣
    var cardDiscount = 1;
    var areas = genMaps("from Area order by code");
    var closeWin = function(){
    	var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.close(index);
    }
    var recalcu = function(){
    	var amount = 0; 
		$('td[name=realPrice]').each(function(i,td){
			var realPrice = 0;
			var size = $(td).siblings('td[name=size]').html();
			if($('#payType').val()!=="0207003"){//非免单
				var cp = $(td).siblings('td[name=price]').html();
				var discount = $('#discount').val();
				var minPrice = $('#realPrice').val();
				realPrice = cp - $('#cheapPrice').val() - $('#offsetPrice').val();
				if(discount>0){
					realPrice -= (cp*(100-discount)/100)
					realPrice = Math.floor(realPrice*0.1)*10;
				}else if(minPrice>0){
					realPrice = minPrice;
				}else if (strategyAlgorithm !== null) {
					if (strategyAlgorithm.reduce !== undefined && strategyAlgorithm.reduce !== null) {
						$.each(strategyAlgorithm.reduce, function(ind, value) {
							if (cp >= value.price) {
								realPrice = cp - value.reduce;
								return false;
							}
						});
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

    var getCardDiscount = function() {
    	var code = $('#memberNo').val();
    	var password = encrypt($('#cardPassword').val());
		MemberDwr.checkPassword(code, password, function() {
			MemberDwr.getCardLevel(code, function(ret) {
				$('#discount').val(ret.ticketDiscount * 100);
				if (ret.ticketDiscount == 1) {
					$('#discount').closest("tr").hide();	
				} else {
					$('#discount').closest("tr").show();
				}
				$('#cheapPrice').val(ret.ticketReduce);
				if (ret.ticketReduce > 0) {
					$('#cheapPrice').closest("tr").show();
				} else {
					$('#cheapPrice').closest("tr").hide();
				}
				$('#cardBalance').val(ret.balance);
				recalcu();
			});
		});
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
        		return '<tr name="itemtr" areaid=' + key + ' seatids="'+ data[key].join(',')+'"><td align="center">'+area.name+'</td><td style="text-align:center" name="price">'+area.price 
        		+'</td><td style="text-align:center" name="size">'+size+'</td><td style="text-align:center">'
        		+money+'</td><td align="center" name="realPrice">'+area.price+'</td><td align="center" name="money">'+money+'</td></tr>';
    		}
    	})
    	
    	for(var i=0;i<arr.length;i++){
    		$(arr[i]).appendTo(table);
    	}
    	$('<tr style="color:green;font-weight:bold"><td align="center">总计</td><td align="center">---</td><td name="totalSize" align="center">'+totalSize
    		+'</td><td align="center" name="totalMoney">'+amount+'</td><td align="center">---</td><td align="center" name="amount">'+amount+'</td></tr>').appendTo(table);
		dicSelect(dic0207).width(150).appendTo($('div[name=payType]')).attr("id","payType").val("0207001");
    	//拼凑门票策略下拉框
    	var select = joinSelect(strategys,"strategy").width(150).appendTo($('div[name=strategy]')).change(function(e){
    		var json = strategys[this.value];
    		eval('strategyAlgorithm = ' + (json.algorithm == '' ? null : json.algorithm));
    		if(json.valueType==="0208001"||json.valueType==="0208003"){//0208001按座位计价，0208003补差价
    			if(json.discount){
    				
    				$('#discount').val(json.discount).closest("tr").show();
        			$('#cheapPrice').val(0).closest("tr").hide();
        			$('#offsetPrice').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
    			}else if(json.offsetPrice){//抵扣金额，差额换票
        			$('#offsetPrice').val(json.offsetPrice).closest("tr").show();
        			$('#cheapPrice').val(0).closest("tr").show();
        			$('#discount').val(0).closest("tr").hide();
        			$('#realPrice').val(0).closest("tr").hide();
        		}else if(json.price){//最低消费,realPrice设置一个值，其他的后台计算
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
    		if ($('#cheapPrice').val() == 0) {
    			$('#cheapPrice').closest("tr").hide();
    		}
    		if ($('#strategy').find('option:selected').text().indexOf('充值卡') >= 0) {
    			$('#cardPassword').closest("tr").show();
    			$('#cardBalance').closest("tr").show();
    			$('#sallerCode').closest("tr").hide();
    			$('#payType').val("0207010");
    			$('#payType option[value=0207010]').attr("disabled",false);
    			$('#payType option[value!=0207010]').attr("disabled",true);
    		} else {
    			$('#cardPassword').closest("tr").hide();
    			$('#cardBalance').closest("tr").hide();
    			$('#sallerCode').closest("tr").show();
    		}
    		recalcu();
    	}).val(2).trigger("change");
    	
    	$('#cheapPrice').change(function() {
    		recalcu();
    	});
    	// 大小键盘输入数字都要有响应
    	$('#cheapPrice').keyup(function(e){
    		if((e.keyCode >= 47 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	$('#discount').keyup(function(e){
    		if((e.keyCode >= 47 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	$('#offsetPrice').keyup(function(e){
    		if((e.keyCode >= 47 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode===8){
    			recalcu();
    		}
    	}).keypress(function(){return onlyDigit(event)});
    	
		$('#cardPassword').keyup(function(e){
			if(e.keyCode==13){
				getCardDiscount(e);
			}
		}).blur(function(e){
			getCardDiscount(e);
		});

    	$('#cancel.btn-danger').click(function(e){
    	    closeWin();
    	})
    	$('#set.btn-danger').click(function(e){
    	    $('#setting').toggle();
    	})
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
    	}).keypress(function(){return onlyDigit(event)});
    	
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
    		param.discount = $("#discount").val();
    		param.realPrice = $("#realPrice").val();
    		param.memberNo = $("#memberNo").val();
    		if ($('#strategy').find('option:selected').text().indexOf('卡') >= 0 && param.memberNo=="") {
    			layer.msg('卡号不能为空');
    			return;
    		}
    		if(param.memberNo!=""){
    			if($('#sallerCode').is(':visible') && sallerCode==""){
    				layer.msg("营销编号不能为空");
    				return;
    			}
    		}
    		param.memberId = $('#memberId').val();
    		param.memberName = $('#memberName').val();
    		param.memberMobile = $('#memberMobile').val();
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
    		// 获取座位ID和对应票价
    		var areaPrice = {};
    		$('#itemTable tr').each(function(ind, tr) {
    			tr = $(tr);
    			var areaId = tr.attr('areaid');
    			if (areaId) {
    				areaPrice[areaId] = {
    							seatIds : tr.attr('seatids').split(','),
    							price : tr.children('[name=realPrice]').text()
    						};
    			}
    		});
    		param.areaPrice = areaPrice;
    		BaseDwr.saleTickets(JSON.stringify(param),function(ret){
    			if(ret.status===0){
    				parent.ticketArr = ret.list;
    				if (param.payType == '0207010') {
    					if (ret.list.length > 0) {
    						var serial = ret.list[0]['saleNo'];
    	    				SWI.util.printCardReceipt(SWI.util.dicByCode['0104001'].text, SWI.util.dicByCode['0220001'].text, 
    	    						serial, '${applicationScope.pccode }', new Date(), $("td[name=amount]").html(), SWI.currentUserName, 
    	    						param.memberNo, $('#cardBalance').val());
    					}
    				}
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
table#bussiness td{
	padding: 0;
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
  <div class="row-fluid" style="margin: 0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top"><td style="padding-right: 10px;width:68%">
                <div class="widget-box" style="width:100%;margin-left: 5px">
          <div class="widget-content nopadding">
            <table class="table table-bordered table-striped" id="itemTable">
              <thead>
                <tr>
                  	<th width="120">片区</th>
					<th width="70">价格</th>
					<th width="70">人数</th>
					<th width="70">原价金额</th>
					<th width="70">优惠价</th>
					<th>实收金额</th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
    </td><td rowspan="2">
      <div class="widget-box">
        <div class="widget-content">
          <table class="table-bordered table-striped" id="bussiness" style="padding: 0">
          <tr>
          <td width="90">售票方式 :</td><td><div class="controls" name="strategy"></div></td>
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
          <tr style="display: none;">
          <td>密码 :</td><td><input type="password" class="span11" name="cardPassword" id="cardPassword"  /></td>
          </tr>
          <tr style="display: none;">
          <td>卡余额 :</td><td><input type="text" class="span11" name="cardBalance" id="cardBalance" readonly="readonly" /></td>
          </tr>
          <tr>
          <td>营销编号 :</td><td><input type="text" class="span11" name="sallerCode" id="sallerCode"/></td>
          </tr>
          <tr>
          <td>客户名称 :</td><td><input type="text" class="span11" name="customerName" id="customerName"/><input type="hidden" name="customerId" id="customerId"/></td>
          </tr>
          <tr>
          <td>顾客手机 :</td><td><input type="text" class="span11" name="memberMobile" id="memberMobile" /></td>
          </tr>
          <tr>
          <td>顾客姓名 :</td><td><input type="text" class="span11" name="memberName" id="memberName" /><input type="hidden" name="memberId" id="memberId"/></td>
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
              <button type="button" id="set" class="btn btn-danger">设置</button>
              <button type="button" id="cancel" class="btn btn-danger">取消</button>
            </div>
        </div>
      </div><!-- 第一行：售票信息结束 -->
    </td></tr>
    <tr>
    <td align="right" style="padding-right:20px;padding-left:20px">
    <%@include file="/WEB-INF/pages/sell/job/print_setting.jspf"%>
    </td>
    </tr>
    </table>
  </div>
</div></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/MemberDwr.js'></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
</body>
</html>
