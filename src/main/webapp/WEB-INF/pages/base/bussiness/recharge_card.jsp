<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>充值卡</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path%>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
<jsp:include page="include.jsp" />
<style type="text/css">
#list td, th {
	white-space: nowrap;
}

#list2 td, th {
	white-space: nowrap;
}

.ui-autocomplete {
	z-index: 198910151;
}
</style>
<script type="text/javascript" src="<%=path%>/static/js/base/business/recharge_card.js"></script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2"></div>
	<div class="rbox2">
		<div class="rcon"></div>
	</div>
	<div style="display: none;">
		<form id="fmNewRechargeCard" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td width="100">卡号<span style="color: red;">*</span></td>
								<td width="150">
									<!-- id和name必须同时写上 --> <input id="cardId" name="cardId" type="text" class="required" minlength="6"
									maxlength="18" style="width: 100%;"></input>
								</td>
							</tr>
							<tr>
								<td>数量<span style="color: red;">*</span></td>
								<td><input id="count" name="count" type="number" value="1" min="1" max="500" class="required"
									style="width: 100%;"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<form id="fmMemberCardRecharge" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>充值金额<span style="color: red;">*</span></td>
								<td><input id="moneyRecharge" name="moneyRecharge" type="number" value="0" min="0" max="1000000"
									class="required" style="width: 100%"></input></td>
							</tr>
							<tr>
								<td>充值方式<span style="color: red;">*</span></td>
								<td><select id="payType" name="payType" style="width: 100%;" class="required"></select></td>
							</tr>
							<tr>
								<td>营销员<span style="color: red;">*</span></td>
								<td><input uitype="autocomplete" type="text" name="paySalesmanId_txt" id="paySalesmanId_txt"
									class="ui-autocomplete-input required" autocomplete="off" style="width: 100%"> <input
										uitype="autocomplete" type="hidden" name="paySalesmanId" id="paySalesmanId"></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<form id="fmChangePassword" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>原密码<span style="color: red;">*</span></td>
								<td><input id="oldPassword" name="oldPassword" type="password" class="required" minlength="6" maxlength="6"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>新密码<span style="color: red;">*</span></td>
								<td><input id="newPassword" name="newPassword" type="password" class="required" minlength="6" maxlength="6"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>确认密码<span style="color: red;">*</span></td>
								<td><input id="confirmPassword" name="confirmPassword" type="password" class="required" minlength="6"
									maxlength="6" equalTo="#newPassword" style="width: 100%;"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<form id="fmChangeCard" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>原卡号<span style="color: red;">*</span></td>
								<td><input id="oldCardId" name="oldCardId" type="text" class="required" minlength="8" maxlength="18"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>新卡号<span style="color: red;">*</span></td>
								<td><input id="newCardId" name="newCardId" type="text" class="required" minlength="8" maxlength="18"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>换卡原因<span style="color: red;">*</span></td>
								<td><select id="reason" name="reason" class="required" style="width: 100%"></select></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<form id="fmMemberInfo" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>关联卡号<span style="color: red;">*</span></td>
								<td><input id="memberCardId" name="memberCardId" type="text" readonly="readonly" class="required"
									minlength="8" maxlength="18" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>姓名<span style="color: red;">*</span></td>
								<td><input id="memberName" name="memberName" type="text" class="required" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>性别</td>
								<td><select id="memberGender" name="memberGender" style="width: 100%;"></select></td>
							</tr>
							<tr>
								<td>手机号码<span style="color: red;">*</span></td>
								<td><input id="memberMobileno" name="memberMobileno" type="number" class="required" minlength="11"
									maxlength="11" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>固定电话</td>
								<td><input id="memberTelephone" name="memberTelephone" type="text" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>电子邮箱</td>
								<td><input id="memberEmail" name="memberEmail" type="text" class="email" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>地址</td>
								<td><input id="memberAddress" name="memberAddress" type="text" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>生日</td>
								<td><input id="memberBirthday" name="memberBirthday" type="text" onclick="WdatePicker()" class="dateISO"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>工作单位</td>
								<td><input id="memberCompany" name="memberCompany" type="text" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>证件类别</td>
								<td><select id="memberIdcardType" name="memberIdcardType" style="width: 100%;"></select></td>
							</tr>
							<tr>
								<td>证件号码</td>
								<td><input id="memberIdcardNo" name="memberIdcardNo" type="text" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>爱好</td>
								<td><input id="memberHobby" name="memberHobby" type="text" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>备注</td>
								<td><input id="memberRemark" name="memberRemark" type="text" style="width: 100%;"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<input id="memberId" name="memberId" type="hidden" />
		</form>
	</div>

	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/BaseDwr.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/MemberDwr.js'></script>
</body>
</html>