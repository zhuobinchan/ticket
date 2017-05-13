package com.tjing.bussiness.services;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.Block;
import com.tjing.bussiness.model.Customer;
import com.tjing.bussiness.model.Floor;
import com.tjing.bussiness.model.MemberCard;
import com.tjing.bussiness.model.Partition;
import com.tjing.bussiness.model.Pcdesc;
import com.tjing.bussiness.model.Recharge;
import com.tjing.bussiness.model.Salesman;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.model.ShowNumber;
import com.tjing.bussiness.model.Ticket;
import com.tjing.bussiness.model.TicketReserve;
import com.tjing.bussiness.model.TicketSale;
import com.tjing.bussiness.model.TicketStrategy;
import com.tjing.bussiness.object.PrintInfo;
import com.tjing.bussiness.object.SaleReturn;
import com.tjing.bussiness.support.MessagePush;
import com.tjing.frame.model.User;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.OrderCodeGen;
import com.tjing.frame.util.SimpleDao;

@Service
public class BaseDwr {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private MessagePush messagePush;
	@Autowired
	private MemberDwr memberDwr;
	
	public String genSeats(Integer areaId){
		Area area = simpleDao.getEntity(Area.class, areaId);
		DbServices.cache.invalidate(area.getId());
		int totalRowNum = area.getTotalRowNum();
		int totalColumnNum = area.getTotalColumnNum();
		int fromRowNum = area.getFromRowNum();
		int fromColumnNum = area.getFromColumnNum();
		int n = 0;
		Map<Integer,Object> paramMap = Maps.newHashMap();
		paramMap.put(0, areaId);
		simpleDao.executeHql("delete from Seat where areaId=?", paramMap);
		for(int i=fromRowNum;i<=(fromRowNum+totalRowNum-1);i++){
			for(int j=fromColumnNum;j<=(fromColumnNum+totalColumnNum-1);j++){
				n++;
				Seat seat = new Seat();
				seat.setAreaId(areaId);
				seat.setRowNum(i);
				seat.setColumnNum(j);
				seat.setAreaCode(area.getCode());
				seat.setName(i+"排"+j+"号");
				seat.setType("0230001");
				seat.setPrice(area.getPrice());
				simpleDao.persist(seat);
				if(n%10==0){
					simpleDao.flush();
					simpleDao.clear();
				}
			}
		}
		return "生成座位成功";
	}
	public List<Area> queryAllAreas(){
		List<Area> list = simpleDao.findList("Area", "", "name,id");
		return list;
	}
	public List<Seat> queryAreaSeats(Integer areaId){
		List<Seat> list = simpleDao.findList("Seat", "{areaId:"+areaId+"}", "name,id");
		return list;
	}
	//取消全部预订
	public String unreserveAll(Integer reserveId){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		JSONArray ticketArr = new JSONArray();//需要同步的ticket
		JSONObject attObj = new JSONObject();//用户，时间等附加信息
		User user = CodeHelper.getCurrentUser();
		Integer userId = user.getId();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		try {
			
			TicketReserve ticketReserve = simpleDao.getEntity(TicketReserve.class, reserveId);
			String playDate = new DateTime(ticketReserve.getPlayDate()).toString("yyyy-MM-dd");
			String keyPrefix = playDate + ";" + ticketReserve.getShowNumberId() + ";";
			attObj.put("playDate", playDate);
			attObj.put("showNumberId", ticketReserve.getShowNumberId());
			attObj.put("type", 4);
			attObj.put("createUserId", userId);
			List<Ticket> list = simpleDao.findList("Ticket", "{reserveId:'"+reserveId+"',status:'0203002'}", "");
			List<Integer> ids = Lists.newArrayList();
			
			for(Ticket ticket : list){
				ids.add(ticket.getId());
				ticket.setStatus("0203005");
				JSONObject retObj = new JSONObject();
				retObj.put("ticketId", ticket.getId());
				retObj.put("seatId", ticket.getSeatId());
				ticketArr.add(retObj);
				codeHelper.unlockSeat(playDate, ticketReserve.getShowNumberId(), keyPrefix, userId, ticket.getSeatId());
			}
			messagePush.sendMessage2(attObj, ticketArr, request, response);
			if(StringUtils.isEmpty(ticketReserve.getDescs())&&StringUtils.isEmpty(ticketReserve.getSallerCode())){
				simpleDao.deleteByIds("delete from Ticket where id in(:ids)", ids);
				simpleDao.deleteEntity(ticketReserve);
			}else{
				ticketReserve.setTicketNum(0);
				ticketReserve.setStatus("0203005");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		return "1";
	}
	//取消预订
	@SuppressWarnings("unused")
	public String unreserve(Integer[] arr){
		int ret = 1;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		User user = CodeHelper.getCurrentUser();
		Integer userId = user.getId();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		try {
			List<Integer> ids = Lists.newArrayList();
			Integer reserveId = null;
			int ticketNum = 0 ;
			Map<String,Object> paramMap = Maps.newHashMap();
			for(int i=0;i<arr.length;i++){
				ids.add(arr[i]);
			}
			paramMap.put("idList", ids);
			List<Ticket> list = simpleDao.getListByHqlWithStrParams("from Ticket where id in (:idList)", paramMap);
			TicketReserve ticketReserve = null;
			String keyPrefix = null;
			String playDate = null;
			for(Ticket ticket : list){//将门票设置为退订
				if(ticketReserve==null){
					ticketReserve = simpleDao.getEntity(TicketReserve.class, ticket.getReserveId());
					playDate = new DateTime(ticketReserve.getPlayDate()).toString("yyyy-MM-dd");
					keyPrefix = playDate + ";" + ticketReserve.getShowNumberId() + ";";
				}
				reserveId = ticket.getReserveId();
				ticket.setStatus("0203005");
				ticketNum++;
				codeHelper.unlockSeat(playDate, ticketReserve.getShowNumberId(), keyPrefix, userId, ticket.getSeatId());
				messagePush.sendMessage(userId, new Integer[]{ticket.getSeatId()}, new DateTime(ticketReserve.getPlayDate()).toString("yyyy-MM-dd"),ticketReserve.getShowNumberId(),4, request, response);
			}
			List<Ticket> list2 = simpleDao.findList("Ticket", "{reserveId:'"+ticketReserve.getId()+"',status:'0203002'}", "");//剩余预订票数
			if(StringUtils.isEmpty(ticketReserve.getDescs())&&StringUtils.isEmpty(ticketReserve.getSallerCode())){
				simpleDao.deleteByIds("delete from Ticket where id in(:ids)", ids);
			}
			if(list2.size()==(arr.length-1)){
				if(StringUtils.isEmpty(ticketReserve.getDescs())&&StringUtils.isEmpty(ticketReserve.getSallerCode())){
					simpleDao.deleteEntity(ticketReserve);
					ret = 0;
				}else{
					ticketReserve.setTicketNum(0);
					ticketReserve.setStatus("0203005");
					ret = 0;
				}
			}else{
				ticketReserve.setTicketNum(ticketReserve.getTicketNum()-ticketNum);
			}
			
			
		} catch (Exception e) {
			ret = -1;
			e.printStackTrace();
		}
		return ret+"";
	}
	//退票
	public Set<Integer> cancelTickets(Integer[] ticketIds){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		
		User user = CodeHelper.getCurrentUser();
		Set<Integer> areaIds = new HashSet<Integer>();
		HashMap<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("ids", ticketIds);
		List<Ticket> list = simpleDao.getListByHqlWithStrParams("from Ticket where id in (:ids)", paramMap);
		TicketSale ts = simpleDao.getEntity(TicketSale.class, list.get(0).getSaleId());
		String playDate = new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd");
		String keyPrefix = playDate + ";" + ts.getShowNumberId() + ";";
		
		// 消费卡充值卡退款处理准备
		String cardCode = ts.getMemberNo();
		String payType = ts.getPayType();
		float sum = 0;
		
		for(Ticket ticket : list){
			if(ticket.getStatus().equals("0203001")){
				areaIds.add(ticket.getAreaId());
				ticket.setStatus("0203006");
				
				// 计算退款总额
				sum += ticket.getRealPrice();
				
				codeHelper.unlockSeat(playDate, ts.getShowNumberId(), keyPrefix, user.getId(), ticket.getSeatId());
				messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),ts.getShowNumberId(),4, request, response);
			}
		}
		
		// 消费卡充值卡退款处理
		if (cardCode != null && !"".equals(cardCode) && "0207010".equals(payType)) {
			memberDwr.cancelCardConsume(ts.getSaleNo(), sum);
		}
		return areaIds;
	}
	//改签
	public Set<Integer> changeDate(Integer[] ticketIds){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		User user = CodeHelper.getCurrentUser();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		HashMap<String,Object> paramMap = Maps.newHashMap();
		paramMap.put("ids", ticketIds);
		List<Ticket> list = simpleDao.getListByHqlWithStrParams("from Ticket where id in (:ids)", paramMap);
		TicketSale ts = simpleDao.getEntity(TicketSale.class, list.get(0).getSaleId());
		String playDate = new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd");
		String keyPrefix = playDate + ";" + ts.getShowNumberId() + ";";
		Set<Integer> areaIds = new HashSet<Integer>();
		for(Ticket ticket : list){
			if(ticket.getStatus().equals("0203001")){
				areaIds.add(ticket.getAreaId());
				ticket.setStatus("0203003");
				codeHelper.unlockSeat(playDate, ts.getShowNumberId(), keyPrefix, user.getId(), ticket.getSeatId());
				messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),ts.getShowNumberId(),4, request, response);
			}
		}
		return areaIds;
	}
	//改签包厢
	public Integer changeDateBox(Integer ticketId){
		Ticket ticket = simpleDao.getEntity(Ticket.class, ticketId);
		if(ticket.getStatus().equals("0203001")){
			ticket.setStatus("0203003");
		}
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		User user = CodeHelper.getCurrentUser();
		TicketSale ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
		messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),ts.getShowNumberId(),4, request, response);
		return 0;
	}
	//改签
	public Set<Integer> changeDateAll(Integer saleId){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		User user = CodeHelper.getCurrentUser();
		HashMap<Integer,Object> paramMap = Maps.newHashMap();
		paramMap.put(0, saleId);
		List<Ticket> list = simpleDao.getListByHql("from Ticket where saleId=? and status='0203001'", paramMap);
		TicketSale ts = null;
		Set<Integer> areaIds = new HashSet<Integer>();
		for(Ticket ticket : list){
			ticket.setStatus("0203003");
			Area area = simpleDao.getEntity(Area.class, ticket.getAreaId());
			areaIds.add(area.getId());
			if(ts==null){
				ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
			}
			messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),ts.getShowNumberId(),4, request, response);
		}
		return areaIds;
	}
	//根据座位的id改签该销售记录下的所有记录
	public Set<String> changeDateAllBrother(Integer ticketId){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		User user = CodeHelper.getCurrentUser();
		Ticket ticketEntity = simpleDao.getEntity(Ticket.class, ticketId);
		HashMap<Integer,Object> paramMap = Maps.newHashMap();
		paramMap.put(0, ticketEntity.getSaleId());
		List<Ticket> list = simpleDao.getListByHql("from Ticket where saleId=? and status='0203001'", paramMap);
		TicketSale ts = null;
		Set<String> areaIds = new HashSet<String>();
		for(Ticket ticket : list){
			ticket.setStatus("0203003");
			Area area = simpleDao.getEntity(Area.class, ticket.getAreaId());
			areaIds.add(area.getCode());
			if(ts==null){
				ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
			}
			messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),ts.getShowNumberId(),4, request, response);
		}
		return areaIds;
	}

	/**
	 * @todo 打印所有预订票
	 * @param id
	 * @param paramString
	 * @return
	 */
	public synchronized SaleReturn printReservedAll(Integer id,String paramString){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		SaleReturn saleReturn = new SaleReturn();
		saleReturn.setStatus(0);
		JSONObject param = JSON.parseObject(paramString);
		Date currentDate = new Date();
		TicketSale ticketSale = null;
		User user = CodeHelper.getCurrentUser();
		TicketReserve tr = simpleDao.getEntity(TicketReserve.class, id);
		JSONArray ticketArr = new JSONArray();//需要同步的ticket
		JSONObject attObj = new JSONObject();//用户，时间等附加信息
		attObj.put("descs", tr.getDescs());
		attObj.put("sallerCode", tr.getSallerCode());
		attObj.put("createUserId", user.getId());
		attObj.put("createUserName", user.getName());
		attObj.put("createTime", currentDate);
		attObj.put("playDate", new DateTime(tr.getPlayDate()).toString("yyyy-MM-dd"));
		attObj.put("showNumberId", tr.getShowNumberId());
		attObj.put("type", 2);
		tr.setStatus("0205002");
		List<Ticket> tickets = simpleDao.findList("Ticket", "{reserveId:"+id+",status:'0203002'}", "");
		List<PrintInfo> list = Lists.newArrayList();
		ticketSale = newTicketSale(param,currentDate, user, tr);
		if(param.containsKey("sallerCode")){
			List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
			if(mans.size()==1){
				ticketSale.setSallerCode(param.getString("sallerCode"));
			}else{
				saleReturn.setStatus(1);//营销编号不存在
				return saleReturn;
			}
		}
		Integer strategyId = param.getInteger("strategy");
		TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
		ticketSale.setStrategyId(strategyId);
		ticketSale.setStrategyName(strategy.getName());
		int plusShowPrice = param.containsKey("plusShowPrice")?param.getIntValue("plusShowPrice"):0;
		ticketSale.setPlusShowPrice(plusShowPrice);
		simpleDao.persist(ticketSale);
		ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class,tr.getShowNumberId());
		List<Integer> seatIdList = Lists.newArrayList();
		String giftstr = param.getString("hasTea").equals("0103001")?"茶水  " : "";
		Integer giftNum = param.getInteger("giftNum");
		if(giftNum>0){
			giftstr += "水果"+giftNum+"份";
		}
		int realPrice = param.getIntValue("realPrice");
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		for(Ticket ticket : tickets){
			seatIdList.add(ticket.getSeatId());
			ticket.setStatus("0203001");
			ticket.setTheaterSn(theatersn);
			ticket.setSaleId(ticketSale.getId());
			ticket.setStrategyId(ticketSale.getStrategyId());
			ticket.setStrategyName(ticketSale.getStrategyName());
			realPrice = calcuRealPrice(ticketSale, strategy, realPrice, ticket);
			ticket.setRealPrice(realPrice);
			Area area = simpleDao.getEntity(Area.class, ticket.getAreaId());
			addPrintObject(currentDate, ticketSale, list, strategy, showNumber,realPrice, giftstr, ticket, area);
			JSONObject retObj = new JSONObject();
			retObj.put("ticketId", ticket.getId());
			retObj.put("seatId", ticket.getSeatId());
			ticketArr.add(retObj);
		}
		
		saleReturn.setList(list);
		messagePush.sendMessage2(attObj, ticketArr, request, response);
	//	messagePush.sendMessage(user.getId(), seatIds, new DateTime(tr.getPlayDate()).toString("yyyy-MM-dd"),tr.getShowNumberId(),2, request, response);
		return saleReturn;
	}
	public synchronized SaleReturn printReserved(Integer reserveId,Integer[] ticketIds,String paramString){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		SaleReturn saleReturn = new SaleReturn();
		saleReturn.setStatus(0);
		JSONObject param = JSON.parseObject(paramString);
		Date currentDate = new Date();
		TicketSale ticketSale = null;
		User user = CodeHelper.getCurrentUser();
		TicketReserve tr = simpleDao.getEntity(TicketReserve.class, reserveId);
		List<PrintInfo> list = Lists.newArrayList();
		JSONArray ticketArr = new JSONArray();//需要同步的ticket
		JSONObject attObj = new JSONObject();//用户，时间等附加信息
		attObj.put("descs", tr.getDescs());
		attObj.put("sallerCode", tr.getSallerCode());
		attObj.put("createUserId", user.getId());
		attObj.put("createUserName", user.getName());
		attObj.put("createTime", currentDate);
		attObj.put("playDate", new DateTime(tr.getPlayDate()).toString("yyyy-MM-dd"));
		attObj.put("showNumberId", tr.getShowNumberId());
		attObj.put("type", 2);
		ticketSale = newTicketSale(param, currentDate, user, tr);
		if(param.containsKey("sallerCode")){
			List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
			if(mans.size()==1){
				ticketSale.setSallerCode(param.getString("sallerCode"));
			}else{
				saleReturn.setStatus(1);//营销编号不存在
				return saleReturn;
			}
		}
		Integer strategyId = param.getInteger("strategy");
		TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
		ticketSale.setStrategyId(strategyId);
		ticketSale.setStrategyName(strategy.getName());
		int plusShowPrice = param.containsKey("plusShowPrice")?param.getIntValue("plusShowPrice"):0;
		ticketSale.setPlusShowPrice(plusShowPrice);
		simpleDao.persist(ticketSale);
		ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class,tr.getShowNumberId());
		List<Integer> seatIdList = Lists.newArrayList();
		Map<String,Object> paramMap = Maps.newHashMap();
		ArrayList<Integer> idList = Lists.newArrayList(ticketIds);
		paramMap.put("idList", idList);
		List<Ticket> ticketList = simpleDao.getListByHqlWithStrParams("from Ticket where id in (:idList) and status='0203002'", paramMap);
		int realPrice = param.getIntValue("realPrice");
		String giftstr = param.getString("hasTea").equals("0103001")?"茶水  " : "";
		Integer giftNum = param.getInteger("giftNum");
		if(giftNum>0){
			giftstr += "水果"+giftNum+"份";
		}
		String keyPrefix = param.getString("playDate") + ";" + ticketSale.getShowNumberId() + ";";
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		for(Ticket ticket : ticketList){
			codeHelper.unlockSeat(param.getString("playDate"),ticketSale.getShowNumberId(),keyPrefix, user.getId(),ticket.getSeatId());
			seatIdList.add(ticket.getSeatId());
			ticket.setStatus("0203001");
			ticket.setTheaterSn(theatersn);
			ticket.setSaleId(ticketSale.getId());
			ticket.setStrategyId(ticketSale.getStrategyId());
			ticket.setStrategyName(ticketSale.getStrategyName());
			realPrice = calcuRealPrice(ticketSale, strategy, realPrice, ticket);
			ticket.setRealPrice(realPrice);
			Area area = simpleDao.getEntity(Area.class, ticket.getAreaId());
			addPrintObject(currentDate, ticketSale, list, strategy, showNumber,realPrice, giftstr, ticket, area);
			JSONObject retObj = new JSONObject();
			retObj.put("ticketId", ticket.getId());
			retObj.put("seatId", ticket.getSeatId());
			ticketArr.add(retObj);
		}
		List<Ticket> rest = simpleDao.findList("Ticket", "{reserveId:"+reserveId+",status:'0203001'}", "");
		if(rest.size()==0){
			tr.setStatus("0205002");
		}
		saleReturn.setList(list);
		messagePush.sendMessage2(attObj, ticketArr, request, response);
		//messagePush.sendMessage(user.getId(), seatIds, new DateTime(tr.getPlayDate()).toString("yyyy-MM-dd"), tr.getShowNumberId(),2, request, response);
		return saleReturn;
	}
	
	//出售预订票时，设置数据
	private TicketSale newTicketSale(JSONObject param,Date currentDate, User user, TicketReserve tr) {
		TicketSale ticketSale = newCommonProperty(param, currentDate, user);
		ticketSale.setReserveId(tr.getId());
		ticketSale.setPlayDate(tr.getPlayDate());
		ticketSale.setSallerCode(tr.getSallerCode());
		ticketSale.setShowNumberId(tr.getShowNumberId());
		ticketSale.setTicketNum(tr.getTicketNum());
		if(param.containsKey("customerId")){
			Customer customer = simpleDao.getEntity(Customer.class, param.getInteger("customerId"));
			if(customer!=null){
				ticketSale.setCustomerName(customer.getShortname());
				ticketSale.setCustomerId(customer.getId());
			}
		}
		if(param.getBooleanValue("isShowSeat")){
			ticketSale.setIsShowSeat("0103001");
		}else{
			ticketSale.setIsShowSeat("0103002");
		}
		return ticketSale;
	}
	private TicketSale newCommonProperty(JSONObject param, Date currentDate,User user) {
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		TicketSale ticketSale = new TicketSale();
		ticketSale.setTheaterSn(theatersn);
		String pccode = user.getPccode();
		Map<Integer,Object> paramMap = new HashMap<Integer,Object>(); 
		String saleNo = "";
		if(pccode==null){
			paramMap.put(0, user.getFetureLetter());
			saleNo = user.getFetureLetter() + DateTime.now().toString("-yyMMdd");
		}else{
			paramMap.put(0, pccode);
			saleNo = DateTime.now().toString("yyMMdd")+user.getPccode();
		}
		paramMap.put(1, DateTime.now().toString("yyyy-MM-dd"));
		Pcdesc pcdesc = simpleDao.getUniqueByHql("from Pcdesc where pccode=? and date_format(createDate,'%Y-%m-%d')=?", paramMap);
		if(pcdesc==null){
			pcdesc = new Pcdesc();
			pcdesc.setSnno(0);
			pcdesc.setPccode(user.getFetureLetter());
			pcdesc.setCreateDate(currentDate);
			simpleDao.persist(pcdesc);
		}
		int snNo = pcdesc.getSnno() ==null ? 0: pcdesc.getSnno();
	    DecimalFormat df = new DecimalFormat("0000");
		saleNo += df.format(++snNo);
		pcdesc.setSnno(snNo);
		ticketSale.setSaleNo(saleNo);
		ticketSale.setGiftNum(param.getInteger("giftNum"));
		ticketSale.setHasTea(param.getString("hasTea"));
		ticketSale.setMemberNo(param.getString("memberNo"));
		ticketSale.setPriceShowType(param.getString("priceShowType"));
		ticketSale.setDiscount(param.getInteger("discount"));
		ticketSale.setCreateTime(currentDate);
		ticketSale.setCreateUserId(user.getId());
		ticketSale.setCreateUserName(user.getName());
		ticketSale.setDescs(param.getString("descs"));
		ticketSale.setCheapPrice(param.getInteger("cheapPrice"));
		ticketSale.setOffsetPrice(param.getInteger("offsetPrice"));
		ticketSale.setPayType(param.getString("payType"));
		return ticketSale;
	}
	//直接出售门票时，设置数据
	private TicketSale newTicketSale(JSONObject param,Date currentDate, User user) {
		TicketSale ticketSale = newCommonProperty(param, currentDate, user);
		ticketSale.setPlayDate(param.getDate("playDate"));
		ticketSale.setShowNumberId(param.getInteger("showNumberId"));
		ticketSale.setSallerCode(param.getString("sallerCode"));
		ticketSale.setTicketNum(param.getInteger("totalSize"));
		if(param.containsKey("customerId")){
			Customer customer = simpleDao.getEntity(Customer.class, param.getInteger("customerId"));
			if(customer!=null){
				ticketSale.setCustomerName(customer.getShortname());
				ticketSale.setCustomerId(customer.getId());
			}
		}
		if(param.getBooleanValue("isShowSeat")){
			ticketSale.setIsShowSeat("0103001");
		}else{
			ticketSale.setIsShowSeat("0103002");
		}
		return ticketSale;
	}
	public SaleReturn saleTickets(String paramString,HttpServletRequest request,HttpServletResponse response){
		
		SaleReturn saleReturn = new SaleReturn();
		try {
			saleReturn.setStatus(0);
			Date currentDate = new Date();
			JSONObject param = JSON.parseObject(paramString);
			User user = CodeHelper.getCurrentUser();
			TicketSale ts = newTicketSale(param,currentDate, user);
			Integer strategyId = param.getInteger("strategy");
			TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
			ts.setStrategyId(strategyId);
			ts.setStrategyName(strategy.getName());
			String status = param.getString("status");
			int plusShowPrice = param.containsKey("plusShowPrice")?param.getIntValue("plusShowPrice"):0;
			ts.setPlusShowPrice(plusShowPrice);
			List<PrintInfo> list = Lists.newArrayList();
			if(param.containsKey("sallerCode")){
				List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
				if(mans.size()==1){
					ts.setSallerCode(param.getString("sallerCode"));
				}else{
					saleReturn.setStatus(1);//营销编号不存在
					return saleReturn;
				}
			}
			// 绑定会员信息
			Integer memberId = param.getInteger("memberId");
			if (memberId == null) {
				String memberName = param.getString("memberName");
				String memberMobile = param.getString("memberMobile");
				if (StringUtils.isNotEmpty(memberMobile) && StringUtils.isNotEmpty(memberName)) {
					memberId = memberDwr.saveMember(null, memberName, null, memberMobile, null, null, null, null, null, 
							null, null, null, null, null);
				}
			}
			ts.setMemberId(memberId);
			simpleDao.persist(ts);
			
			JSONArray ticketArr = new JSONArray();//需要同步的ticket
			JSONObject attObj = new JSONObject();//用户，时间等附加信息
			attObj.put("descs", ts.getDescs());
			attObj.put("sallerCode", ts.getSallerCode());
			attObj.put("createUserId", user.getId());
			attObj.put("createUserName", user.getName());
			attObj.put("createTime", currentDate);
			attObj.put("playDate", param.getString("playDate"));
			attObj.put("showNumberId", ts.getShowNumberId());
			attObj.put("type", 2);
			JSONObject areaPrice = param.getJSONObject("areaPrice");
			Set<String> keys = areaPrice.keySet();
			ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class, ts.getShowNumberId());
			if(showNumber==null){
				return null;
			}
			List<Integer> seatIdList = Lists.newArrayList();
			CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
			int realPrice = 0;//param.getIntValue("realPrice");
			float sum = 0;
			String giftstr = param.getString("hasTea").equals("0103001")?"茶水  " : "";
			Integer giftNum = param.getInteger("giftNum");
			if(giftNum>0){
				giftstr += "水果"+giftNum+"份";
			}
			String keyPrefix = param.getString("playDate") + ";" + ts.getShowNumberId() + ";";
			String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
			for(String key : keys){
				//JSONArray arr = jobj.getJSONArray(key);
				JSONObject areaObject = JSON.parseObject(areaPrice.getString(key));
				JSONArray arr = areaObject.getJSONArray("seatIds");
				Area area = simpleDao.getEntity(Area.class, Integer.valueOf(key));
				for(int i=0;i<arr.size();i++){
					Integer id = arr.getInteger(i);
					seatIdList.add(id);//前台更新状态
					codeHelper.unlockSeat(param.getString("playDate"),ts.getShowNumberId(),keyPrefix, user.getId(),id);
					Seat seat = simpleDao.getEntity(Seat.class, id);
					Ticket ticket = new Ticket();
					ticket.setTheaterSn(theatersn);
					ticket.setNumber(OrderCodeGen.genCode("",5));
					ticket.setPrice(area.getPrice());
					ticket.setAreaId(area.getId());
					ticket.setAreaName(area.getName());
					ticket.setStrategyId(ts.getStrategyId());
					ticket.setStrategyName(ts.getStrategyName());
					//realPrice = calcuRealPrice(ts, strategy, realPrice, ticket);
					realPrice = areaObject.getInteger("price");
					sum += realPrice;
					ticket.setRealPrice(realPrice);
					ticket.setSaleId(ts.getId());
					ticket.setSeatId(id);
					ticket.setSeatName(seat == null ? "" : seat.getName());
					ticket.setAllowNum(1);
					ticket.setTotalNum(1);
					ticket.setStatus(status);
					ticket.setCreateTime(currentDate);
					ticket.setCreateUserId(user.getId());
					ticket.setPlayDate(ts.getPlayDate());
					ticket.setShowNumberId(ts.getShowNumberId());
					simpleDao.persist(ticket);
					addPrintObject(currentDate, ts, list,strategy, showNumber,realPrice, giftstr, ticket, area);
					JSONObject retObj = new JSONObject();
					retObj.put("ticketId", ticket.getId());
					retObj.put("seatId", id);
					ticketArr.add(retObj);
					
				}
			}
			
			// 消费卡充值卡的扣款
			if (ts.getMemberNo() != null && !"".equals(ts.getMemberNo())) {
				MemberCard card = memberDwr.getCardByCode(ts.getMemberNo());
				memberDwr.cardConsume(card.getId(), sum, ts.getSaleNo(), "0226001", null);
			}
			
			Integer[] seatIds = new Integer[seatIdList.size()];
			seatIds = seatIdList.toArray(seatIds);
			messagePush.sendMessage2(attObj, ticketArr, request, response);
			saleReturn.setList(list);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return saleReturn;
	}
	private int calcuRealPrice(TicketSale ts, TicketStrategy strategy,
			int realPrice, Ticket ticket) {
		if(realPrice==0&&!strategy.getValueType().equals("0208002")){//不是免费售票方式
			realPrice = ticket.getPrice() - ts.getOffsetPrice() - ts.getCheapPrice();
			if(ts.getDiscount()>0){
				 realPrice -= realPrice * (100-ts.getDiscount())*0.01;
				 realPrice = (int) (Math.floor(realPrice*0.1)*10);
			}
		}
		return realPrice;
	}
	private void addPrintObject(Date currentDate, TicketSale ticketSale,
			List<PrintInfo> list, TicketStrategy strategy,
			ShowNumber showNumber, int realPrice, String giftstr,Ticket ticket, Area area) {
		String seatName = "待定";
		if(ticketSale.getIsShowSeat().equals("0103001")){
			if(area.getId()==1||area.getId()==3){
				seatName = area.getName()+ticket.getSeatName();
			}else{
				seatName = area.getFloorNum()+"楼"+ticket.getSeatName();
				Block block = simpleDao.getEntity(Block.class, area.getBlockId());
				if (block != null) {
					Partition part = simpleDao.getEntity(Partition.class, block.getPartitionId());
					if (part != null) {
						Floor floor = simpleDao.getEntity(Floor.class, part.getFloorId());
						if (floor != null) {
							seatName = floor.getName() + ticket.getSeatName();
						}
					}
				}
			}
			
		}
		if(realPrice>0||strategy.getMustPrint().equals("021201")){
			ticketSale.setPrintNum(ticketSale.getPrintNum()+1);
			String showPrice = realPrice+"";
			if(ticketSale.getPriceShowType().equals("0209002")){
				showPrice = "Y" + ticket.getPrice();
			}else if(ticketSale.getPriceShowType().equals("0209003")){
				showPrice = "H" + (realPrice+ticketSale.getPlusShowPrice()) + "";
			} else if ("0209004".equals(ticketSale.getPriceShowType())) {
				showPrice = area.getName();
				int index = Math.max(showPrice.indexOf("("), showPrice.indexOf("（"));
				if (index >= 0) {
					showPrice = showPrice.substring(0, index);
				}
			}
			String playDate = "";
			if (ticketSale.getPlayDate() == null) {
				if ("不定期售票".equals(ticketSale.getStrategyName())) {
					playDate = "售票日期：" + new DateTime(currentDate).toString("yyyy-MM-dd") + "（半年内有效）";
				}
			} else {
				playDate = new DateTime(ticketSale.getPlayDate()).toString("yyyy-MM-dd");
			}
			String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
			PrintInfo printInfo = new PrintInfo(theatersn,ticketSale.getSaleNo(),seatName, ticketSale.getStrategyName(),showPrice
					,1,ticket.getNumber(), playDate ,showNumber.getName(),
					new DateTime(currentDate).toString("yyyy-MM-dd HH:mm"),giftstr);
			list.add(printInfo);
		}
	}
	//出售包厢
	private void addPrintObject3(Date currentDate, TicketSale ts,List<PrintInfo> list,TicketStrategy strategy, 
			ShowNumber showNumber, int realPrice, String giftstr,Integer userNum, Ticket ticket, String seatName) {
		if(realPrice>0||strategy.getMustPrint().equals("021201")){
			String showPrice = ticket.getRealPrice()+"";
			if(ts.getPriceShowType().equals("0209002")){
				showPrice = ticket.getPrice() + "";
			}else if(ts.getPriceShowType().equals("0209003")){
				showPrice = "H" + (realPrice+ts.getPlusShowPrice());
			}
			ts.setPrintNum(ts.getPrintNum()+1);
			String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
			PrintInfo printInfo = new PrintInfo(theatersn,ts.getSaleNo(),seatName, ticket.getStrategyName(),showPrice,userNum,ticket.getNumber(),
					ts.getPlayDate() == null ? null : new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"),
					showNumber.getName(), new DateTime(currentDate).toString("yyyy-MM-dd HH:mm"),giftstr);
			list.add(printInfo);
		}
	}
	/**
	 * 
	 * @作者：曾平
	 * @日期：2016年4月13日 上午12:04:51
	 * @用途：预售包厢出票
	 */
	private void addPrintObject4(Date currentDate, TicketSale ticketSale,List<PrintInfo> list, TicketStrategy strategy,
			ShowNumber showNumber, String giftstr, int realPrice,Integer userNum, Ticket ticket, String showPrice, Area area) {
		if(realPrice>0||strategy.getMustPrint().equals("021201")){
			ticketSale.setPrintNum(ticketSale.getPrintNum()+1);
			PrintInfo printInfo = new PrintInfo(CodeHelper.getCurrentUser().getTheaterSn(),ticketSale.getSaleNo(),area.getName(), ticketSale.getStrategyName(),showPrice,userNum,ticket.getNumber(),
					new DateTime(ticketSale.getPlayDate()).toString("yyyy-MM-dd"),showNumber.getName(), 
					new DateTime(currentDate).toString("yyyy-MM-dd HH:mm"),giftstr);
			list.add(printInfo);
		}
	}
	public SaleReturn saleBox(Integer seatId,String paramString){
		SaleReturn saleReturn = new SaleReturn();
		saleReturn.setStatus(0);
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		Date currentDate = new Date();
		JSONObject param = JSON.parseObject(paramString);
		Integer userNum = param.getInteger("userNum");
		User user = CodeHelper.getCurrentUser();
		TicketSale ts = newTicketSale(param,currentDate, user);
		Integer strategyId = param.getInteger("strategy");
		TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
		ts.setStrategyId(strategyId);
		ts.setStrategyName(strategy.getName());
		ts.setTicketNum(1);
		String status = param.getString("status");
		ts.setTheaterSn(user.getTheaterSn());
		int plusShowPrice = param.containsKey("plusShowPrice")?param.getIntValue("plusShowPrice"):0;
		ts.setPlusShowPrice(plusShowPrice);
		List<PrintInfo> list = Lists.newArrayList();
		if(param.containsKey("sallerCode")){
			List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
			if(mans.size()==1){
				ts.setSallerCode(param.getString("sallerCode"));
			}else{
				saleReturn.setStatus(1);//营销编号不存在
				return saleReturn;
			}
		}
		simpleDao.persist(ts);
		ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class, ts.getShowNumberId());
		if(showNumber==null){
			return null;
		}
		int price = param.getIntValue("price");
		int realPrice = param.getIntValue("realPrice");
		String giftstr = param.getString("hasTea").equals("0103001")?"茶水  " : "";
		Integer giftNum = param.getInteger("giftNum");
		if(giftNum>0){
			giftstr += "水果"+giftNum+"份";
		}
		String keyPrifix = param.getString("playDate") + ";" + ts.getShowNumberId() + ";";
		String areaName = param.getString("areaName");
		Seat seat = simpleDao.getEntity(Seat.class, seatId);
		Area area = simpleDao.getEntity(Area.class, Integer.valueOf(seat.getAreaId()));
		Ticket ticket = new Ticket();
		ticket.setSaleId(ts.getId());
		ticket.setNumber(OrderCodeGen.genCode("",5));
		ticket.setSeatId(seatId);
		ticket.setTotalNum(userNum);
		ticket.setAllowNum(userNum);
		ticket.setPrice(price);
		ticket.setTheaterSn(user.getTheaterSn());
		ticket.setAreaId(area.getId());
		ticket.setAreaName(areaName);
		ticket.setStrategyId(ts.getStrategyId());
		ticket.setStrategyName(ts.getStrategyName());
		realPrice = calcuRealPrice(ts, strategy, realPrice, ticket);
		ticket.setRealPrice(realPrice);
		ticket.setAllowNum(userNum);
		ticket.setTotalNum(userNum);
		ticket.setStatus(status);
		ticket.setCreateTime(currentDate);
		ticket.setCreateUserId(user.getId());
		ticket.setPlayDate(ts.getPlayDate());
		ticket.setShowNumberId(ts.getShowNumberId());
		simpleDao.persist(ticket);
		codeHelper.unlockSeat(param.getString("playDate"),ts.getShowNumberId(),keyPrifix, user.getId(),ticket.getSeatId());
		String seatName = "";
		if(param.getBooleanValue("isShowSeat")){
			seatName = areaName;
		}
		addPrintObject3(currentDate, ts, list,strategy, showNumber, realPrice,
				giftstr, userNum, ticket, seatName);
		
		Integer[] seatIds = new Integer[]{seatId};
		
		messagePush.sendMessage(user.getId(), seatIds, param.getString("playDate"), ts.getShowNumberId(),2, request, response);
		saleReturn.setList(list);
		return saleReturn;
	}
	
	//出售预订的包厢
	public SaleReturn saleBoxReserved(Integer reserveId,String paramString){
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		SaleReturn saleReturn = new SaleReturn();
		saleReturn.setStatus(0);
		JSONObject param = JSON.parseObject(paramString);
		Date currentDate = new Date();
		TicketSale ticketSale = null;
		User user = CodeHelper.getCurrentUser();
		TicketReserve tr = simpleDao.getEntity(TicketReserve.class, reserveId);
		tr.setStatus("0205002");
		tr.setTheaterSn(theatersn);
		List<Ticket> tickets = simpleDao.findList("Ticket", "{reserveId:"+reserveId+",status:'0203002'}", "");
		List<PrintInfo> list = Lists.newArrayList();
		ticketSale = newTicketSale(param,currentDate, user, tr);
		if(param.containsKey("sallerCode")){
			List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
			if(mans.size()==1){
				ticketSale.setSallerCode(param.getString("sallerCode"));
			}else{
				saleReturn.setStatus(1);//营销编号不存在
				return saleReturn;
			}
		}
		Integer strategyId = param.getInteger("strategy");
		TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
		ticketSale.setStrategyId(strategyId);
		ticketSale.setStrategyName(strategy.getName());
		int plusShowPrice = param.containsKey("plusShowPrice")?param.getIntValue("plusShowPrice"):0;
		ticketSale.setPlusShowPrice(plusShowPrice);
		simpleDao.persist(ticketSale);
		ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class,tr.getShowNumberId());
		List<Integer> seatIdList = Lists.newArrayList();
		String giftstr = param.getString("hasTea").equals("0103001")?"茶水  " : "";
		Integer giftNum = param.getInteger("giftNum");
		if(giftNum>0){
			giftstr += "水果"+giftNum+"份";
		}
		int realPrice = param.getIntValue("realPrice");
		Integer userNum = param.getInteger("userNum");
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
		String keyPrifix = param.getString("playDate") + ";" + ticketSale.getShowNumberId() + ";";
		for(Ticket ticket : tickets){
			seatIdList.add(ticket.getSeatId());
			ticket.setStatus("0203001");
			ticket.setTotalNum(userNum);
			ticket.setAllowNum(userNum);
			ticket.setTheaterSn(theatersn);
			ticket.setSaleId(ticketSale.getId());
			ticket.setStrategyId(ticketSale.getStrategyId());
			ticket.setStrategyName(ticketSale.getStrategyName());
			realPrice = calcuRealPrice(ticketSale, strategy, realPrice, ticket);
			ticket.setRealPrice(realPrice);
			String showPrice = realPrice+"";
			if(ticketSale.getPriceShowType().equals("0209002")){
				showPrice = ticket.getPrice() + "";
			}else if(ticketSale.getPriceShowType().equals("0209003")){
				showPrice = (realPrice+plusShowPrice) + "";
			}
			Area area = simpleDao.getEntity(Area.class, ticket.getAreaId());
			addPrintObject4(currentDate, ticketSale, list, strategy,
					showNumber, giftstr, realPrice, userNum, ticket, showPrice,
					area);
			codeHelper.unlockSeat(param.getString("playDate"),ticketSale.getShowNumberId(),keyPrifix, user.getId(),ticket.getSeatId());
		}
		Integer[] seatIds = new Integer[seatIdList.size()];
		seatIds = seatIdList.toArray(seatIds);
		messagePush.sendMessage(user.getId(), seatIds, new DateTime(tr.getPlayDate()).toString("yyyy-MM-dd"),tr.getShowNumberId(),2, request, response);
		saleReturn.setList(list);
		return saleReturn;

	}
	
	@SuppressWarnings("static-access")
	public int reserveTickets(String jobjString,String paramString){
		try {
			WebContext ctx = WebContextFactory.get();
			HttpServletRequest request = ctx.getHttpServletRequest();
			HttpServletResponse response = ctx.getHttpServletResponse();
			Date currentDate = new Date();
			JSONObject jobj = JSON.parseObject(jobjString);
			JSONObject param = JSON.parseObject(paramString);
			JSONArray ticketArr = new JSONArray();//需要同步的ticket
			JSONObject attObj = new JSONObject();//用户，时间等附加信息
			User user = CodeHelper.getCurrentUser();
			TicketReserve tr = new TicketReserve();
			tr.setCreateTime(currentDate);
			tr.setCreateUserId(user.getId());
			tr.setStatus("0205001");
			tr.setCreateUserName(user.getName());
			tr.setTicketNum(param.getInteger("totalSize"));
			tr.setAmount(param.getInteger("amount"));
			tr.setShowNumberId(param.getInteger("showNumberId"));
			tr.setPlayDate(param.getDate("playDate"));
			tr.setForegift(param.getInteger("foregift"));
			tr.setTheaterSn(user.getTheaterSn());
			tr.setReserveType(param.getString("reserveType"));
			if(param.containsKey("descs")){
				attObj.put("descs", param.getString("descs"));
				tr.setDescs(param.getString("descs"));
			}
			if(param.containsKey("sallerCode")){
				List<Salesman> mans = simpleDao.findList("Salesman", "{code:'" +param.getString("sallerCode")+"'}" , "");
				if(mans.size()==1){
					attObj.put("sallerCode", param.getString("sallerCode"));
					tr.setSallerCode(param.getString("sallerCode"));
				}else{
					return 0;//营销编号不存在
				}
			}
			// 绑定会员信息
			Integer memberId = param.getInteger("memberId");
			if (memberId == null) {
				String memberName = param.getString("memberName");
				String memberMobile = param.getString("memberMobile");
				if (StringUtils.isNotEmpty(memberMobile) && StringUtils.isNotEmpty(memberName)) {
					memberId = memberDwr.saveMember(null, memberName, null, memberMobile, null, null, null, null, null, 
							null, null, null, null, null);
				}
			}
			tr.setMemberId(memberId);

			if(param.containsKey("memberNo")){
				List<MemberCard> mans = simpleDao.findList("MemberCard", "{code:'" +param.getString("memberNo")+"',status:'0218001'}" , "");
				if(mans.size()==1){
					tr.setMemberNo(mans.get(0).getCode());
				}else{
					return 1;//会员卡号不存在
				}
			}
			String keyPrifix = param.getString("playDate") + ";" + tr.getShowNumberId() + ";";
			simpleDao.persist(tr);
			Set<String> keys = jobj.keySet();
			int num = 0;
			
			attObj.put("createUserId", user.getId());
			attObj.put("createUserName", user.getName());
			attObj.put("createTime", currentDate);
			attObj.put("playDate", param.getString("playDate"));
			attObj.put("showNumberId", tr.getShowNumberId());
			attObj.put("type", 3);
			attObj.put("reserveType", tr.getReserveType());
			CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
			String theatersn = user.getTheaterSn();
			for(String key : keys){
				JSONArray arr = jobj.getJSONArray(key);
				Area area = simpleDao.getEntity(Area.class, Integer.valueOf(key));
				for(int i=0;i<arr.size();i++){
					Integer id = arr.getInteger(i);
					key = keyPrifix + id;
					if(codeHelper.lockedSeatSet.containsKey(key)){
						num++;
						Seat seat = simpleDao.getEntity(Seat.class, id);
						codeHelper.unlockSeat(param.getString("playDate"),tr.getShowNumberId(),key, user.getId(),id);
						Ticket ticket = new Ticket();
						ticket.setNumber(OrderCodeGen.genCode("",5));
						ticket.setPrice(area.getPrice());
						ticket.setAreaId(area.getId());
						ticket.setAreaName(area.getName());
						ticket.setReserveId(tr.getId());
						ticket.setSeatId(id);
						ticket.setTheaterSn(theatersn);
						ticket.setSeatName(seat.getName());
						ticket.setStatus("0203002");
						ticket.setCreateTime(currentDate);
						ticket.setCreateUserId(user.getId());
						ticket.setPlayDate(tr.getPlayDate());
						ticket.setShowNumberId(tr.getShowNumberId());
						simpleDao.persist(ticket);
						JSONObject retObj = new JSONObject();
						retObj.put("ticketId", ticket.getId());
						retObj.put("seatId", id);
						ticketArr.add(retObj);
					}
					
				}
			}
			if(num==0){
				simpleDao.deleteEntity(tr);
			}
//			Integer[] seatIds = new Integer[seatIdList.size()];
//			seatIds = seatIdList.toArray(seatIds);
			messagePush.sendMessage2(attObj, ticketArr, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	public List<PrintInfo> changeSeat(int seatId,String number,Integer realPrice,Integer cheapPrice,Integer strategyId,String descs,Integer giftNum,String isShowSeat,String hasTea,String priceShowType){
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		HttpServletResponse response = ctx.getHttpServletResponse();
		Seat seat = simpleDao.getEntity(Seat.class, seatId);
		Area area = simpleDao.getEntity(Area.class, seat.getAreaId());
		List<Ticket> tickets = simpleDao.findList("Ticket", "{number:'"+number+"',status:'0203001'}", "");
		User user = CodeHelper.getCurrentUser();
		TicketStrategy strategy = simpleDao.getEntity(TicketStrategy.class, strategyId);
		List<PrintInfo> infos = Lists.newArrayList();
		Date currentDate = new Date();
		if(tickets.size()>=1){
			try {
				Ticket ticket = tickets.get(0);
				ticket.setStatus("0203003");
				TicketSale ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
				simpleDao.evict(ts);
				TicketSale ts2 = (TicketSale) BeanUtils.cloneBean(ts);
				ts2.setCheapPrice(cheapPrice);
				ts2.setStrategyId(strategyId);
				ts2.setStrategyName(strategy.getName());
				ts2.setSallerCode("");
				ts2.setDescs(descs);
				ts2.setId(null);
				ts2.setCreateTime(currentDate);
				simpleDao.persist(ts2);
				CodeHelper codeHelper = new CodeHelper(request, response, messagePush);
				String playDate = new DateTime(ticket.getPlayDate()).toString("yyyy-MM-dd");
				String keyPrifix = playDate + ";" + ticket.getShowNumberId() + ";";
				simpleDao.evict(ticket);
				Ticket ticket2 = new Ticket();
				ticket2 = (Ticket) BeanUtils.cloneBean(ticket);
				ticket2.setId(null);
				ticket2.setStatus("0203001");
				ticket2.setSaleId(ts2.getId());
				ticket2.setRealPrice(realPrice);
				ticket2.setPrice(area.getPrice());
				ticket2.setSeatId(seatId);
				ticket2.setSeatName(seat.getName());
				ticket2.setAreaId(seat.getAreaId());
				ticket2.setAreaName(area.getName());
				ticket2.setStrategyId(strategyId);
				ticket2.setStrategyName(strategy.getName());
				ticket2.setParentId(ticket.getId());
				ticket2.setCreateTime(currentDate);
				simpleDao.merge(ticket);
				simpleDao.persist(ticket2);
				ticket.setId(null);
				JSONArray ticketArr = new JSONArray();//需要同步的ticket
				JSONObject attObj = new JSONObject();//用户，时间等附加信息
				attObj.put("descs", ts2.getDescs());
				attObj.put("sallerCode", ts2.getSallerCode());
				attObj.put("createUserId", user.getId());
				attObj.put("createUserName", user.getName());
				attObj.put("createTime", currentDate);
				attObj.put("playDate",playDate);
				attObj.put("showNumberId", ts2.getShowNumberId());
				attObj.put("type", 2);
				attObj.put("ticketId",ticket2.getId());
				ShowNumber showNumber = simpleDao.getEntity(ShowNumber.class, ticket2.getShowNumberId());
				String giftstr = hasTea.equals("0103001")?"茶水  " : "";
				if(giftNum>0){
					giftstr += "水果"+giftNum+"份";
				}
				codeHelper.unlockSeat(playDate,ticket.getShowNumberId(),keyPrifix, user.getId(),ticket.getSeatId());
				codeHelper.unlockSeat(playDate,ticket2.getShowNumberId(),keyPrifix, user.getId(),seatId);
				JSONObject retObj = new JSONObject();
				retObj.put("ticketId", ticket2.getId());
				retObj.put("seatId", ticket2.getSeatId());
				ticketArr.add(retObj);
				messagePush.sendMessage2(attObj, ticketArr, request, response);//新座位标识为已售出
				messagePush.sendMessage(user.getId(), new Integer[]{ticket.getSeatId()}, new DateTime(ticket.getPlayDate()).toString("yyyy-MM-dd"),ticket.getShowNumberId(),4, request, response);//旧座位标识为空
				addPrintObject(currentDate, ts2, infos,strategy, showNumber,realPrice, giftstr, ticket2, area);
			} catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return infos;
	}
	/**
	 * 客户充值
	 */
	public String recharge(Integer money,Integer id){
		User user = CodeHelper.getCurrentUser();
		Customer customer = simpleDao.getEntity(Customer.class, id);
		Recharge rc = new Recharge();
		rc.setCreateTime(new Date());
		rc.setCreateUserId(user.getId());
		rc.setCreateUserName(user.getName());
		rc.setCustomerId(id);
		rc.setRechargeAmount(money);
		simpleDao.persist(rc);
		customer.setBalance(customer.getBalance()+money);
		customer.setRechargeAmount(money);
		customer.setRechargeTime(rc.getCreateTime());
		return "1";
	}
	/**
	 * 修改支付方式
	 */
	public String changePayType(Integer[] ids,String payType){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ids", ids);
		List<TicketSale> list = simpleDao.getListByHqlWithStrParams("from TicketSale where id in(:ids)", paramMap);
		for(TicketSale ts : list){
			ts.setPayType(payType);
		}
		return "1";
	}

	/**
	 * 售票后修改销售记录的营销员编号
	 * 
	 * @param ids 待修改的销售记录
	 * @param salesmanId 更新后的营销员ID
	 * @return
	 */
	public String changeSalesman(Integer[] ids, Integer salesmanId){
		Salesman salesman = simpleDao.getEntity(Salesman.class, salesmanId);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ids", ids);
		List<TicketSale> list = simpleDao.getListByHqlWithStrParams("from TicketSale where id in(:ids)", paramMap);
		for(TicketSale ts : list){
			ts.setSallerCode(salesman.getCode());
		}
		return "1";
	}

	public Integer balanceSale(){
		try {
			User user = CodeHelper.getCurrentUser();
			Map<Integer,Object> paramMap = new HashMap<Integer,Object>(); 
			paramMap.put(0, new Date());
			paramMap.put(1, user.getId());
			simpleDao.executeHql("update TicketSale set status='0211002',balanceTime=? where status='0211001' and balanceTime is null and createUserId=? and date_format(createTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')", paramMap);
			return 0;
		} catch (Exception e) {
			
			e.printStackTrace();
			return 1;
		}
	}
	public JSONObject checkTicket(String number){
		JSONObject jobj = new JSONObject();
		List<Ticket> tickets = simpleDao.findList("Ticket", "{status:'0203001',number:'"+number+"'}", "");
		if(tickets.size()==0){
			jobj.put("status", 0);
			jobj.put("responseMsg", "票号不存在");
			return jobj;
		}
		Ticket ticket = tickets.get(0);
		if(ticket.getAllowNum().intValue()==0){
			jobj.put("status", 2);
			jobj.put("responseMsg", "该票已使用");
			jobj.put("useTime", new DateTime(ticket.getInTime()).toString("yyyy-MM-dd HH:mm:ss"));
			return jobj;//已使用
		}
		Date now = DateTime.parse(new DateTime(new Date()).toString("yyyy-MM-dd")).toDate();
		if(ticket.getPlayDate().compareTo(now)<0){
			jobj.put("status", 3);
			jobj.put("responseMsg", "该票已过期");
			return jobj;//已过期
		}
		if(ticket.getPlayDate().compareTo(new Date())>0){
			jobj.put("status", 4);
			jobj.put("responseMsg", "未到演出日期");
			return jobj;//未到演出日期
		}
		jobj.put("responseMsg", "检票成功");
		jobj.put("status", 1);
		jobj.put("successTotal", 1);
		ticket.setAllowNum(ticket.getAllowNum()-1);
		ticket.setInTime(new Date());
		ticket.setCheckUserId(CodeHelper.getCurrentUser().getId());
		jobj.put("allowNum", ticket.getAllowNum());
		return jobj;//已使用
	}
	
	public int syncAllSeatName(int belongto){
		List<Area> areas = simpleDao.findList("Area", "{belongto:" + belongto + "}","");
		int i = 0;
		for(Area area : areas){
			List<Seat> list = simpleDao.findList("Seat", "{areaId:"+area.getId() + "}","");
			for(Seat seat : list){
				if(StringUtils.isNotBlank(seat.getMappedName())){
					if(!seat.getMappedName().equals("隐藏")&&seat.getMappedName().indexOf("茶")==-1&&seat.getMappedName().indexOf("VS")==-1&&seat.getMappedName().indexOf("VT")==-1&&seat.getMappedName().indexOf("湖")==-1&&seat.getMappedName().indexOf("长")==-1){
						String name = StringUtils.substringBefore(seat.getName(), "排")+"排"+seat.getMappedName()+"号";
						seat.setName(name);
						simpleDao.merge(seat);
						if(i%10==0){
							simpleDao.flush();
						}
						i++;
					}
				}
			}
		}
		return 1;
	}
	public int syncSeatName(int areaId){
		List<Seat> list = simpleDao.findList("Seat", "{areaId:"+areaId + "}","");
		int i = 0;
		for(Seat seat : list){
			if(StringUtils.isNotBlank(seat.getMappedName())){
				if(!seat.getMappedName().equals("隐藏")&&seat.getMappedName().indexOf("茶")==-1&&seat.getMappedName().indexOf("VS")==-1&&seat.getMappedName().indexOf("VT")==-1&&seat.getMappedName().indexOf("湖")==-1&&seat.getMappedName().indexOf("长")==-1){
					String name = StringUtils.substringBefore(seat.getName(), "排")+"排"+seat.getMappedName()+"号";
					seat.setName(name);
					simpleDao.merge(seat);
					if(i%10==0){
						simpleDao.flush();
					}
				}
			}
		}
		return 1;
	}
}
