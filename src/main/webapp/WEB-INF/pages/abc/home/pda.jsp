<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
  </head>
  
  <body>
    <table align="center" style="font-size:20px">
    	<tr>
    	<td>票号</td>
    	<td><input id='proofNo' style="height:50px;line-height:50px;text-align:center;font-size:  14px;" type="text" size="13"/></td>
    	</tr>
    	<tr>
    	<td colspan="2" style="text-align: center;color: red;font-size:14px"><div id='showCode'></div></td>
    	</tr>
    	<tr>
    	<td colspan="2" style="text-align: center;color: red;font-size:14px">
    	成功人数：<span id="successTotal">0</span>人
    	</td>
    	</tr>
    	<tr style="display:none">
    	<td colspan="2" style="text-align: center;color: red;font-size:14px;">
    	剩余人数：<span id="allowNum">0</span>人
    	</td>
    	</tr>
    	<tr>
    	<td colspan="2" style="text-align: center;color: red;font-size:14px">
    	<label id="tips" style="color:green"></label>
    	</td>
    	</tr>
    	<tr>
    	<td colspan="2" style="text-align: center;color: red;font-size:14px"><div id='useTime'></div></td>
    	</tr>
    	<tr>
    	<td colspan="2" style="text-align: center;color: red;font-size:14px"><button onclick="reflush()">刷新</button></td>
    	</tr>
    </table>
    <script type="text/javascript" src="/jquery/jquery.js"></script>
    <script type="text/javascript">
    	function reflush(){
    		$("#successTotal").html("0");
    		$('#allowNum').html(0).closest("tr").hide();
			$('#tips').html("");
			$('#useTime').html("");
			$('#showCode').html("");
			$('#proofNo').val("");
    	}
    	$(function(){
    		$("#proofNo").focus().keydown(function(e){
    			if(e.keyCode===13){
    				if(this.value!==''){
    					var proofNo = this.value;
    					this.value = "";
    					BaseDwr.checkTicket(proofNo,function(ret){
    						$('#showCode').html(proofNo);
    						$('#successTotal').html(ret.successTotal);
    						$('#tips').html(ret.responseMsg);
    						$('#useTime').html("");
    						if(ret.useTime){
    							$('#useTime').html("使用时间：</br>"+ret.useTime);
    						}
    						if(typeof(ret.allowNum)!=='undefined'){
    							$('#allowNum').html(ret.allowNum).closest("tr").show();
    						}else{
    							$('#allowNum').html(0).closest("tr").hide();
    						}
    						setTimeout(function(){
    							$('#proofNo').val("");
    						},1000);
    		    		});
    				}
    				
    			}
    		})
    	})
    </script>
    <script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
  </body>
</html>
