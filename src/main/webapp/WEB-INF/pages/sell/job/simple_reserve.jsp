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
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-media.css" />
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
        		+money+'</td></tr>';
    		}
    	})
    	
    	for(var i=0;i<arr.length;i++){
    		$(arr[i]).appendTo(table);
    	}
    	$('<tr style="color:green;font-weight:bold"><td align="center">总计</td><td align="center">---</td><td name="totalSize" align="center">'+totalSize
    		+'</td><td align="center" name="amount">'+amount+'</td></tr>').appendTo(table);
    	$('#cancel').click(function(e){//按钮
    	    closeWin();
    	})
    	$('#reset').click(function(e){//按钮
    	    $("form :input").val("");
    		$("a.select2-choice span").each(function(i,v){
    			v.innerHTML = "";
    		});
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
    	$('#memberNo').keyup(function(e){
			if(e.keyCode==13){
				validMemberNo(e);
				
			}
		}).blur(function(e){
			validMemberNo(e);
		})
		function validMemberNo(e){
    		if($('#memberNo').val()!=""){
				$.ajax({
    				url:g_path+'/public/tool/getSalesmanByMemberNo.htmls',
    				data:{memberNo:$('#memberNo').val()},
    				async:false,
    				success:function(ret){
        				if(ret.status!=1){
        					layer.msg("无效卡号");
        					$('#memberNo').val("");
        					$('#memberMobile').val('').attr("readonly", false);
        					$('#memberName').val('').attr("readonly", false);
        					$('#memberId').val("");
        					$('#sallerCode').val("").attr("disabled",false);
        				}else{
        					$('#sallerCode').val(ret.sallerCode).attr("disabled",true);
        					$('#memberMobile').val(ret.memberMobile).attr("readonly", true);
        					$('#memberName').val(ret.memberName).attr("readonly", true);
        					$('#memberId').val(ret.memberId);
        				}
    				}
    			})
			}
    	}
        $('#memberMobile').change(function(e){
        	MemberDwr.getMemberInfoByMobile($('#memberMobile').val(), {
        		callback: function(ret) {
    				$('#memberName').val(ret.name).attr("readonly", true);
    				$('#memberId').val(ret.id);
        		},
        		errorHandler: function(msg) {
        			$('#memberName').attr("readonly", false);
        			$('#memberId').val('');
        		}
        	});
    	});
    	$(".btn-success").click(function(e){
    		if($(parent.document).find("td.picked").length===0){
    			layer.alert("选择的座位已失效，请重新选择座位");
    			return;
    		}
    		var param = {};
    		var totalSize = $("td[name=totalSize]").html();
    		var descs = $("#descs").val();
    		var sallerCode = $("#sallerCode").val();
    		var memberNo = $("#memberNo").val();
    		var foregift = $("#foregift").val();
    		/* if($.trim(descs)===""&&$.trim(sallerCode)===""){
    			layer.msg("请输入备注信息或者营销编号");
    			return;
    		} */
    		if($.trim(descs)!=""){
    			param.descs = descs;
    		}
    		if($.trim(sallerCode)!=""){
    			param.sallerCode = sallerCode;
    		}
    		if($.trim(memberNo)!=""){
    			param.memberNo = memberNo;
    		}
    		param.memberId = $('#memberId').val();
    		param.memberName = $('#memberName').val();
    		param.memberMobile = $('#memberMobile').val();
     		param.foregift = foregift;
    		param.totalSize = totalSize;
    		param.amount = table.find("td[name=amount]").html();
    		param.reserveType = "0214001";
    		
    		param.playDate = $(parent.document).find("#playDate").val();
    		param.showNumberId = $(parent.document).find("#showNumber").val();
    		BaseDwr.reserveTickets(JSON.stringify(data),JSON.stringify(param),function(ret){
    			if(ret===0){
    				layer.msg("营销编号不存在");
    			}else if(ret==2){
    				layer.msg("会员卡号不存在");
    			}else if(ret===-1){
    				layer.msg("售票失败，请联系管理员");
    			}else{
    				parent.ticketArr = 1;
        			closeWin();
    			}
    			
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
    <div style="width:460px;float: left">
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
                </tr>
              </thead>
            </table>
          </div>
        </div>
    </div>
        <div style="width:320px;float: right">
      <div class="widget-box">
        <div class="widget-title"> <span class="icon"> <i class="icon-align-justify"></i> </span>
          <h5>业务信息</h5>
        </div>
        <div style="padding-top:10px;padding-left: 10px">
        <form style="display: inline;">
        <input type="hidden" value="" id="customerId"/>
          <table class="table-bordered table-striped" id="bussiness" style="padding: 0;width: 99%">
          <tr style="display: none">
          <td width="100" align="center">押金金额 :</td><td align="left"><input type="hidden" class="span11" id="foregift" name="foregift" value="0"/></td>
          </tr>
          <tr>
          <td align="center">会员卡号 :</td><td><input type="text" class="span11" id="memberNo" name="memberNo" /></td>
          </tr>
          <tr>
          <td align="center">营销编号 :</td><td><input type="text" class="span11" id="sallerCode" name="sallerCode"/></td>
          </tr>
          <tr>
          <td align="center">顾客手机 :</td><td><input type="text" class="span11" name="memberMobile" id="memberMobile" /></td>
          </tr>
          <tr>
          <td align="center">顾客姓名 :</td><td><input type="text" class="span11" name="memberName" id="memberName" /><input type="hidden" name="memberId" id="memberId"/></td>
          </tr>
          <tr>
          <td align="center">备注信息 :</td><td align="left"><input type="text" class="span11" name="descs" id="descs" /></td>
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
<script type='text/javascript' src='<%=path %>/dwr/interface/MemberDwr.js'></script>
</body>
</html>
