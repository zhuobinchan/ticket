package com.tjing.frame.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.Customer;
import com.tjing.bussiness.model.Member;
import com.tjing.bussiness.model.MemberCard;
import com.tjing.bussiness.model.Salesman;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.model.Ticket;
import com.tjing.bussiness.model.TicketReserve;
import com.tjing.bussiness.model.TicketSale;
import com.tjing.bussiness.object.SaleInfo;
import com.tjing.bussiness.support.MessagePush;
import com.tjing.frame.model.Dic;
import com.tjing.frame.model.User;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.RequestObject;
import com.tjing.frame.object.ResponseObject;
import com.tjing.frame.services.DataCache;
import com.tjing.frame.services.DataPerfect;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;

@Controller
@RequestMapping(value="/public/tool")
public class ToolController {
	@Autowired  
	private  HttpServletRequest request;  
	private  HttpServletResponse response;  
	@Autowired
	private DataPerfect dataperfect;
	@Autowired
	private DbServices dbServices;
	@Autowired
	private MessagePush messagePush;
	@Autowired
	DataCache dataCache;
	@ModelAttribute
	public void setResponse(HttpServletResponse response){
		this.response = response;
	}
	@RequestMapping(value="/gendicjs")
	public ResponseEntity<List<Dic>> genDicJs(@RequestParam(value="data[]") String[] data){
		Map<String, Dic> dicGroupMap = dataCache.getDicsFromCache();
		List<Dic> dics = Lists.newLinkedList();
		for(String p : data){
			Dic dic = dicGroupMap.get(p);
			dics.add(dic);
		}
		ResponseEntity<List<Dic>> responseEntity = new ResponseEntity<List<Dic>>(dics,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/genMaps")
	public ResponseEntity<Map<Object,Object>> genMaps(@RequestParam String hql) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		List<Object> list = dbServices.findListByHql(hql, null);
		Map<Object,Object> map = Maps.newLinkedHashMap();
		for(Object obj : list){
			Object id = MethodUtils.invokeMethod(obj, "getId");
			map.put(id, obj);
		}
		ResponseEntity<Map<Object,Object>> responseEntity = new ResponseEntity<Map<Object,Object>>(map,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/genCurrentTime")
	public ResponseEntity<String> genCurrentTime(@RequestParam String pattern){
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(DateTime.now().toString(pattern),HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/saveOrUpdateRecords")
	public ResponseEntity<ResponseObject> saveOrUpdateRecords(RequestObject requestObject){
		ResponseObject res = new ResponseObject();
		String methodName = requestObject.getMethod();
		if(StringUtils.isNotEmpty(methodName)){
			try {
				MethodUtils.invokeMethod(dataperfect, methodName, new Object[]{requestObject});
			} catch (NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			if(!requestObject.isPromise()){
				ResponseEntity<ResponseObject> responseEntity = new ResponseEntity<ResponseObject>(res,HttpStatus.OK);
				if(StringUtils.isNotEmpty(requestObject.getMessage())){
					res.setMessage(requestObject.getMessage());
				}
				return responseEntity;
			}
		}
		
		String returnId = dbServices.saveOrupdateRecords(requestObject.getPoName(), requestObject.getData(), requestObject.getMethod());
		res.setMessage("保存成功");
		if(requestObject!=null){
			res.setId(returnId);
			res.setRecordNum(requestObject.getData().size());
		}
		ResponseEntity<ResponseObject> responseEntity = new ResponseEntity<ResponseObject>(res,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/deleteRecords")
	public ResponseEntity<ResponseObject> deleteRecords(RequestObject requestObject){
		ResponseObject res = new ResponseObject();
		String methodName = requestObject.getMethod();
		if(StringUtils.isNotEmpty(methodName)){
			try {
				MethodUtils.invokeMethod(dataperfect, methodName, new Object[]{requestObject});
			} catch (NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			if(!requestObject.isPromise()){
				ResponseEntity<ResponseObject> responseEntity = new ResponseEntity<ResponseObject>(res,HttpStatus.OK);
				if(StringUtils.isNotEmpty(requestObject.getMessage())){
					res.setMessage(requestObject.getMessage());
				}
				return responseEntity;
			}
		}
		
		List<String> idlist = requestObject.getArr();
		ClassInfo classInfo = CodeHelper.getClassInfo(requestObject.getPoName());
		int deleteNum = 0;
		if(classInfo.getIdType().equals("Integer")){
			List<Integer> ids = Lists.newArrayList();
			for(String id : idlist){
				ids.add(Integer.valueOf(id));
			}
			deleteNum = dbServices.deleteByIds(requestObject.getPoName(),ids);
		}else{
			//ID为字符串
		}
		if(requestObject!=null){
			res.setId(requestObject.getId());
			res.setRecordNum(deleteNum);
		}
		ResponseEntity<ResponseObject> responseEntity = new ResponseEntity<ResponseObject>(res,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/loadFormData")
	public ResponseEntity<JSONObject> loadFormData(RequestObject requestObject) throws ClassNotFoundException{
		ClassInfo classInfo = CodeHelper.getClassInfo(requestObject.getPoName());
		Class<?> clazzObj = Class.forName(classInfo.getFullName());
		JSONObject jobj = null;
		if(StringUtils.isEmpty(requestObject.getId())){
			jobj = new JSONObject();
		}else{
			Object entity = dbServices.getEntity(clazzObj, Integer.valueOf(requestObject.getId()));
			jobj = JSON.parseObject(JSON.toJSONString(entity));
		}
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(jobj,HttpStatus.OK);
		return responseEntity;
	}
	//读取座位的状态，分别在屏幕上用颜色设置不同的颜色
	@RequestMapping(value="/getSeatStatusMap")
	public ResponseEntity<SaleInfo> getSeatStatusMap(String playDate,Integer showNumberId) throws ClassNotFoundException{
		User user = CodeHelper.getCurrentUser();
		int myid = user.getId();
		String theatersn = user.getTheaterSn();
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, playDate);
		paramMap.put(1, showNumberId);
		paramMap.put(2, theatersn);
		List<Ticket> tickets = dbServices.findListBySql("select * from bi_ticket where date_format(play_date,'%Y-%m-%d')=? and show_number_id=? and theater_sn=?", paramMap,Ticket.class);
		List<TicketReserve> reserves = dbServices.findList("TicketReserve", "{playDate: '"+playDate+"',showNumberId:"+showNumberId+",theaterSn:'"+theatersn+"'}","");
		List<TicketSale> sales = dbServices.findList("TicketSale", "{playDate: '"+playDate+"',showNumberId:"+showNumberId+",theaterSn:'"+theatersn+"'}","");
		List<Integer> pickedSeatIds = Lists.newArrayList();
		Set<Integer> userIdSet = CodeHelper.userLockedSeatIdSet.keySet();
		List<Integer> lockedSeatIds = Lists.newArrayList();
		List<Integer> myLocked = CodeHelper.userLockedSeatIdSet.get(myid);
		if(myLocked!=null&&myLocked.size()>0){
			pickedSeatIds.addAll(CodeHelper.userLockedSeatIdSet.get(myid));
		}
		for(Integer userId : userIdSet){
			if(userId!=myid){
				lockedSeatIds.addAll(CodeHelper.userLockedSeatIdSet.get(userId));
			}
		}
		SaleInfo saleInfo = new SaleInfo(tickets, null, lockedSeatIds,pickedSeatIds, null,reserves,sales);
		ResponseEntity<SaleInfo> responseEntity = new ResponseEntity<SaleInfo>(saleInfo,HttpStatus.OK);
		return responseEntity;
	}
	//用于售票主屏
	@RequestMapping(value="/queryReservedTickets")
	public ResponseEntity<List<Ticket>> queryReservedTickets(String playDate,Integer showNumberId) throws ClassNotFoundException{
		List<Ticket> list = dbServices.findList("Ticket", "{playDate: '"+playDate+"',showNumberId:"+showNumberId+",stuts:'0203002'}","");
		ResponseEntity<List<Ticket>> responseEntity = new ResponseEntity<List<Ticket>>(list,HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * @todo 锁定座位或者解锁座位
	 * @param playDate
	 * @param showNumberId
	 * @param seatIds
	 * @return
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value="/toggleReservedSeat")
	public ResponseEntity<Map<Integer,Integer>> toggleReservedSeat(String playDate,Integer showNumberId,@RequestParam(value="seatIds[]")Integer[] seatIds) throws ClassNotFoundException{
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		Map<Integer,Integer> map = Maps.newHashMap();
		for(Integer seatId : seatIds){
			Integer num = 1;
			String key = playDate+";"+showNumberId+";";
			User user = CodeHelper.lockedSeatSet.get(key+seatId);
			if(user==null){
				num = 0; //可以锁定
				codeHelper.lockSeat(playDate,showNumberId,key,seatId);
			}else{
				if(user.getId()==CodeHelper.getCurrentUser().getId()){ 
					num = 2;//自己锁定了，取消
					codeHelper.unlockSeat(playDate,showNumberId,key, user.getId(),seatId);
				}else{
					DateTime d1 = DateTime.now();
					DateTime d2 = new DateTime(user.getLastLoginTime());
					if(d2.plusMinutes(30).isBefore(d1)){
						codeHelper.lockSeat(playDate,showNumberId,key,seatId);
						num = 0;//别人锁定了，但是超时了
					}else{
						num = 1;//别人锁定了，不能再选择
					}
				}
			}
			map.put(seatId, num);
		}
		ResponseEntity<Map<Integer,Integer>> responseEntity = new ResponseEntity<Map<Integer,Integer>>(map,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/unlockAll")
	public ResponseEntity<Map<Integer,Integer>> unlockAll(String playDate,Integer showNumberId) throws ClassNotFoundException{
		Map<Integer,Integer> map = Maps.newHashMap();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		User user = CodeHelper.getCurrentUser();
		String key = playDate+";"+showNumberId+";";
		codeHelper.unlockSeat(playDate,showNumberId,key, user.getId());
		ResponseEntity<Map<Integer,Integer>> responseEntity = new ResponseEntity<Map<Integer,Integer>>(map,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/getLockedUser")
	public ResponseEntity<User> getLockedUser(String playDate,Integer showNumberId,Integer seatId) throws ClassNotFoundException{
		String key = playDate+";"+showNumberId+";"+seatId;
		User user = CodeHelper.lockedSeatSet.get(key);
		ResponseEntity<User> responseEntity = new ResponseEntity<User>(user,HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value="/queryCustomerList")
	public ResponseEntity<List<Customer>> queryCustomerList() throws ClassNotFoundException{
		Map<Integer,Object> paramMap = new HashMap<Integer,Object>(); 
		List<Customer> list = dbServices.findListByHql("from Customer order by spell,code", paramMap);
		ResponseEntity<List<Customer>> responseEntity = new ResponseEntity<List<Customer>>(list,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/genCustomerCode")
	public ResponseEntity<String> genCustomerCode() throws ClassNotFoundException{
		Object obj = dbServices.getUniqueByHql("select Max(code) from Customer",null);
		String code = "000001";
		if(StringUtils.isNotBlank(obj.toString())){
			code = obj.toString();
			String prefix = CodeHelper.getCodePrefixString(code);
			if(prefix.length()>0){
				Integer number = Integer.valueOf(code.replace(prefix, "1"));
				code = prefix+(((number+1)+"").substring(1));
			}else{
				Integer number = Integer.valueOf(1+code);
				code = prefix+(((number+1)+"").substring(1));
			}
			
		}
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(code,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/queryTicketReserve")
	public ResponseEntity<Map<Object,List<Integer>>> queryTicketReserve(Integer id) throws ClassNotFoundException{
		Map<Object,List<Integer>> jobj = Maps.newHashMap();
		List<Ticket> tickets = dbServices.findList("Ticket", "{reserveId:"+id+"}", "");
		for(Ticket ticket : tickets){
			if(!jobj.containsKey(ticket.getAreaId())){
				List<Integer> arr = Lists.newArrayList();
				jobj.put(ticket.getAreaId(),arr);
				
			}
			jobj.get(ticket.getAreaId()).add(ticket.getSeatId());
		}
		ResponseEntity<Map<Object,List<Integer>>> responseEntity = new ResponseEntity<Map<Object,List<Integer>>>(jobj,HttpStatus.OK);
		return responseEntity;
	}
	//根据门票ID获取预订信息
	@RequestMapping(value="/getTicketReserveByTicketId")
	public ResponseEntity<TicketReserve> getTicketReserveByTicketId(Integer id) throws ClassNotFoundException{
		Ticket ticket = dbServices.getEntity(Ticket.class, id);
		TicketReserve tr = dbServices.getEntity(TicketReserve.class, ticket.getReserveId());
		ResponseEntity<TicketReserve> responseEntity = new ResponseEntity<TicketReserve>(tr,HttpStatus.OK);
		return responseEntity;
	}
	//根据门票ID获取售票信息
	@RequestMapping(value="/getTicketSaleByTicketId")
	public ResponseEntity<TicketSale> getTicketSaleByTicketId(Integer id) throws ClassNotFoundException{
		Ticket ticket = dbServices.getEntity(Ticket.class, id);
		TicketSale tr = dbServices.getEntity(TicketSale.class, ticket.getSaleId());
		ResponseEntity<TicketSale> responseEntity = new ResponseEntity<TicketSale>(tr,HttpStatus.OK);
		return responseEntity;
	}
	//只打印部分预订票
	@RequestMapping(value="/queryTicketReserveSome")
	public ResponseEntity<Map<Object,List<Integer>>> queryTicketReserveSome(@RequestParam Integer id,@RequestParam(value="ticketArr[]") Integer[] data) throws ClassNotFoundException{
		Map<Object,List<Integer>> jobj = Maps.newHashMap();
		Map<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("ids", data);
		List<Ticket> tickets = dbServices.getListByHqlWithStrParams("from Ticket where id in (:ids)", paramMap);
		for(Ticket ticket : tickets){
			if(!jobj.containsKey(ticket.getAreaId())){
				List<Integer> arr = Lists.newArrayList();
				jobj.put(ticket.getAreaId(),arr);
				
			}
			jobj.get(ticket.getAreaId()).add(ticket.getSeatId());
		}
		ResponseEntity<Map<Object,List<Integer>>> responseEntity = new ResponseEntity<Map<Object,List<Integer>>>(jobj,HttpStatus.OK);
		return responseEntity;
	}
	//改签时查询门票信息
	@RequestMapping(value="/queryTicketInfoForChange")
	public ResponseEntity<JSONObject> queryTicketInfoForChange(String number){
		List<Ticket> list = dbServices.findList("Ticket", "{number:'" + number + "'}", "createTime desc");
		JSONObject jobj = new JSONObject();
		jobj.put("responseStatus", 0);//不存在
		if(list.size()>0){
			Ticket ticket = list.get(0);
			DateTime dt1 = DateTime.parse(new DateTime(ticket.getPlayDate()).toString("yyyy-MM-dd"));
			DateTime dt2 = DateTime.parse(new DateTime(new Date()).toString("yyyy-MM-dd"));
			
			jobj = JSON.parseObject(JSON.toJSONString(ticket));
			TicketSale ts = dbServices.getEntity(TicketSale.class, ticket.getSaleId());
			jobj.put("descs", ts.getDescs());
			jobj.put("sallerCode", ts.getSallerCode());
			jobj.put("cheapPrice", ts.getCheapPrice());
			jobj.put("offsetPrice", ts.getOffsetPrice());
			jobj.put("seatName", ticket.getSeatName());
			jobj.put("areaName", ticket.getAreaName());
			jobj.put("areaId", ticket.getAreaId());
			jobj.put("discount", ts.getDiscount());
			jobj.put("giftNum", ts.getGiftNum());
			jobj.put("hasTea", ts.getHasTea());
			jobj.put("isShowSeat", ts.getIsShowSeat());
			jobj.put("priceShowType", ts.getPriceShowType());
			if(dt1.compareTo(dt2)<0){
				jobj.put("responseStatus", 2);
			}else{
				jobj.put("responseStatus", 1);
			}
		}
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(jobj,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/getCustomerArr")
	public ResponseEntity<JSONArray> getCustomerArr(){
		List<Customer> list = dbServices.findList("Customer", "", "spell,name");
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		for(Customer c : list){
			obj = new JSONObject();
			obj.put("value",c.getShortname() + c.getCode()+c.getSpell());
			obj.put("name", c.getShortname());
			obj.put("id", c.getId());
			arr.add(obj);
		}
		ResponseEntity<JSONArray> responseEntity = new ResponseEntity<JSONArray>(arr,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/getSalesmanArr")
	public ResponseEntity<JSONArray> getSalesmanArr(){
		List<Salesman> list = dbServices.findList("Salesman", "", "code,name");
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		for(Salesman c : list){
			obj = new JSONObject();
			obj.put("value",c.getCode());
			obj.put("name", c.getName());
			obj.put("id", c.getId());
			arr.add(obj);
		}
		ResponseEntity<JSONArray> responseEntity = new ResponseEntity<JSONArray>(arr,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/getSalesmanByMemberNo")
	public ResponseEntity<JSONObject> getSalesmanByMemberNo(String memberNo){
		JSONObject retJson = new JSONObject();
		retJson.put("status", 2);//无效
		List<MemberCard> list = dbServices.findList("MemberCard", "{code:'" + memberNo + "'}", "code,name");
		if(list.size()==0){
			retJson.put("status", 0);
		}else{
			MemberCard member = null;
			for(MemberCard m : list){
				if(m.getStatus().equals("0218001")){
					member = m;
				}
			}
			if(member!=null){
				Salesman salesman = dbServices.getEntity(Salesman.class, member.getSalesmanId());
				retJson.put("status", 1);
				if(salesman==null){
					retJson.put("sallerCode", "");
				}else{
					retJson.put("sallerCode", salesman.getCode());
				}
				// 获取会员信息
				Member mem = dbServices.getEntity(Member.class, member.getMemberId());
				String memberMobile = "";
				String memberName = "";
				if (mem != null) {
					memberMobile = mem.getMobileno();
					memberName = mem.getName();
				}
				retJson.put("memberMobile", memberMobile);
				retJson.put("memberName", memberName);
				retJson.put("memberId", member.getMemberId());
			}
		}
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(retJson,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/getReserveInfoByTicketId")
	public ResponseEntity<TicketReserve> getReserveInfoByTicketId(Integer ticketId){
		Ticket ticket = dbServices.getEntity(Ticket.class, ticketId);
		TicketReserve tr = dbServices.getEntity(TicketReserve.class, ticket.getReserveId());
		ResponseEntity<TicketReserve> responseEntity = new ResponseEntity<TicketReserve>(tr,HttpStatus.OK);
		return responseEntity;
	}
	//统计售票数据
	@RequestMapping(value="/calcuTicketData")
	public ResponseEntity<JSONObject> calcuTicketData(String param){
		JSONObject jobj = JSON.parseObject(param);
		String startDate = jobj.getString("startDate");
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		String where =  "date_format(ts.create_time,'%Y-%m-%d')>=? and ts.status='0211001'";
		int i = 0;
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(i++, startDate);
		if(param.contains("createUserId")){
			where += " and ts.create_user_id=?";
			paramMap.put(i++, jobj.getInteger("createUserId"));
		}
		
		if(StringUtils.isNotBlank(theatersn)){
			where += " and ts.theater_sn=?";
			paramMap.put(i++, theatersn);
		}
		String sql = "select count(t.id),count(case ts.plus_show_price when 0 then null else 1 end), "//浮动打印
				+"ifnull(sum(case when (ts.pay_type='0207001'&&t.status!='0203006') then t.real_price else 0 end),0),"//现金
				+"ifnull(sum(case when (ts.pay_type='0207002'&&t.status!='0203006') then t.real_price else 0 end),0),"//信用卡
				+"ifnull(sum(case when ((ts.member_no is not null && ts.member_no!='')&&t.status!='0203006') then t.real_price else 0 end),0),"//会员卡
				+"ifnull(sum(case when (t.status!='0203006') then t.real_price else 0 end),0),"//总金额
				+"ifnull(sum(case when (ts.pay_type='0207004'&&t.status!='0203006') then t.real_price else 0 end),0),"//挂账
				+"ifnull(sum(case when (ts.pay_type='0207003'&&t.status!='0203006') then t.price else 0 end),0),"//免单计算原票价
				
				+"count(case when ((ts.offset_price is null||ts.offset_price=0)||t.status='0203006') then null else 1 end),"//抵扣票数
				+"count(case t.status when '0203003' then 1 else null end),"//改签票数，改签和退票不可能重合
				+"count(case when (t.parent_id is null||t.status='0203006') then null else 1 end),"//换座票数
				+"count(case when (ts.pay_type='0207003'&&t.status!='0203006'&&ts.strategy_id!=3) then 1 else null end),"//免单票数，不计业务售票
				+"count(case when ((select price from bi_ticket_strategy where id=t.strategy_id)>0&&t.status!='0203006') then 1 else null end),"//最低消费
				+"count(case when (ts.cheap_price=0&&t.status!='0203006') then null else 1 end),"//优惠
				+"count(case t.status when '0203006' then 1 else null end),"//退票数
				+"ifnull(sum(case t.status when '0203006' then t.price else 0 end),0),"//退票
				+"count(case ts.price_show_type when '0209002' then 1 else null end)"//原价打印
				+ " from bi_ticket t join bi_ticket_sale ts on t.sale_id=ts.id where "+where;
		
		Object[] objs = (Object[]) dbServices.findUniqueBySql(sql, paramMap);
		//System.out.println(JSON.toJSONString(objs));
		JSONObject retJson = new JSONObject();
		Integer sum = Integer.valueOf(objs[5].toString());
		int cashAmount = Integer.valueOf(objs[2].toString()); 
		int cardAmount = Integer.valueOf(objs[3].toString()); 
		int memberAmount = Integer.valueOf(objs[4].toString()); 
		int rememberAmount = Integer.valueOf(objs[6].toString()); 
		int freeAmount = Integer.valueOf(objs[7].toString()); 
		retJson.put("totalNum", objs[0]);
		retJson.put("plusNum", objs[1]);
		retJson.put("cashAmount", cashAmount);
		retJson.put("cardAmount", cardAmount);
		retJson.put("memberAmount", memberAmount);
		retJson.put("amount", sum);
		retJson.put("rememberAmount", rememberAmount);
		retJson.put("freeAmount", freeAmount);
		retJson.put("offsetNum", objs[8]);
		retJson.put("changeDateNum", objs[9]);
		retJson.put("changeSeatNum", objs[10]);
		retJson.put("freeNum", objs[11]);
		retJson.put("minFeeNum", objs[12]);
		retJson.put("cheapNum", objs[13]);
		retJson.put("backNum", objs[14]);
		retJson.put("backAmount", objs[15]);
		
		retJson.put("shouldNum", objs[16]);
		sql = "select sum(print_num),1 from bi_ticket_sale ts where "+where;
		objs = (Object[]) dbServices.findUniqueBySql(sql, paramMap);
		retJson.put("usePaperNum", objs[0]);
		request.getSession().setAttribute("retJson", retJson);
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(retJson,HttpStatus.OK);
		return responseEntity;
	}
	//画座位图需要，暂时作废了
	@RequestMapping(value="/getSeatLists")
	public ResponseEntity<JSONObject> getSeatLists(int belongto){
		JSONObject jobj = new JSONObject();
		List<Area> areas = dbServices.findList("Area", "{belongto:"+belongto+"}","");
		HashMap<String,List<Seat>> map = Maps.newHashMap();
		for(Area area : areas){
			List<Seat> seats = dbServices.findList("Seat", "{areaId:"+area.getId()+"}","");
			map.put(area.getCode(), seats);
		}
		jobj = (JSONObject) JSON.toJSON(map);
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(jobj,HttpStatus.OK);
		return responseEntity;
	}
}
