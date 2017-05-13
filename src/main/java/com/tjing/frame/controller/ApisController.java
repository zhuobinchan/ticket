package com.tjing.frame.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.LockedSeat;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.model.Theater;
import com.tjing.bussiness.model.Ticket;
import com.tjing.bussiness.object.AreaSeatData;
import com.tjing.bussiness.object.WeixinSale;
import com.tjing.bussiness.services.BaseDwr;
import com.tjing.frame.services.DataCache;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;

@Controller
@RequestMapping(value="/outside")
public class ApisController {
	@Autowired
	private DbServices dbServices;
	@Autowired
	private BaseDwr baseDwr;
	@Autowired
	DataCache dataCache;
	//读取座位的状态，分别在屏幕上用颜色设置不同的颜色
	@RequestMapping(value="/getSeatStatusMap")
	public ResponseEntity<JSONObject> getSeatStatusMap(String mobileno,String play_date,Integer shownumber_id,String theater_sn) throws ClassNotFoundException{
		JSONObject err = new JSONObject();
		err.put("errcode", 0);
		err.put("errmsg", "请求成功");
		if(StringUtils.isEmpty(mobileno)){
			err.put("errcode", 1001);
			err.put("errmsg", "mobileno不能为空");
		}
		if(StringUtils.isEmpty(play_date)){
			err.put("errcode", 1001);
			err.put("errmsg", "play_date不能为空");
		}
		if(shownumber_id==null){
			err.put("errcode", 1001);
			err.put("errmsg", "showNumber_id不能为空");
		}
		JSONObject resObject = new JSONObject();
		if(err.getInteger("errcode")>0){
			resObject.put("code", err);
			ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
			return responseEntity;
		}
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, play_date);
		paramMap.put(1, shownumber_id);
		paramMap.put(2, theater_sn);
		List<Ticket> tickets = dbServices.findListBySql("select * from bi_ticket where date_format(play_date,'%Y-%m-%d')=? and show_number_id=? and theater_sn=?", paramMap,Ticket.class);
		List<JSONObject> pickedSeatInfo = Lists.newArrayList();
		List<JSONObject> reservedSeatInfo = Lists.newArrayList();
		List<JSONObject> saledSeatInfo = Lists.newArrayList();
		JSONObject jobj = new JSONObject();
		for(Ticket t : tickets){
			jobj = new JSONObject();
			if(t.getStatus().equals("0203001")){
				Seat seat = dbServices.getEntity(Seat.class, t.getSeatId());
				jobj.put("row_no", seat.getRowNum());
				jobj.put("col_no", seat.getColumnNum());
				jobj.put("area_code", seat.getAreaCode());
				reservedSeatInfo.add(jobj);
			}else if(t.getStatus().equals("0203002")){
				Seat seat = dbServices.getEntity(Seat.class, t.getSeatId());
				jobj.put("row_no", seat.getRowNum());
				jobj.put("col_no", seat.getColumnNum());
				jobj.put("area_code", seat.getAreaCode());
				saledSeatInfo.add(jobj);
			}
		}
		paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, play_date);
		paramMap.put(1, theater_sn);
		System.out.println(play_date+ " "+theater_sn);
		List<LockedSeat> lockedSeats = dbServices.findListByHql("from LockedSeat where date_format(playDate,'%Y-%m-%d')=? and theaterSn=? order by lockTime", paramMap);
		List<JSONObject> lockedSeatInfo = Lists.newArrayList();
		System.out.println("锁定座位数："+lockedSeats.size());
		for(LockedSeat ls : lockedSeats){
			jobj.put("row_no", ls.getRowNum());
			jobj.put("col_no", ls.getColumnNum());
			jobj.put("area_code", ls.getAreaCode());
			if(ls.getCode().equals(mobileno)){
				pickedSeatInfo.add(jobj);
			}else{
				lockedSeatInfo.add(jobj);
			}
		}
		Collection<List<Integer>> seatCollection = CodeHelper.userLockedSeatIdSet.values();
		for(List<Integer> l : seatCollection){
			for(Integer id : l){
				Seat seat = dbServices.getEntity(Seat.class,id);
				jobj.put("row_no", seat.getRowNum());
				jobj.put("col_no", seat.getColumnNum());
				jobj.put("area_code", seat.getAreaCode());
				lockedSeatInfo.add(jobj);
			}
		}
		WeixinSale ws = new WeixinSale(saledSeatInfo, reservedSeatInfo, lockedSeatInfo, pickedSeatInfo);
		resObject.put("code", err);
		resObject.put("data", ws);
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
		return responseEntity;
	}
	@RequestMapping(value="/lockSeats")
	public ResponseEntity<JSONObject> lockSeats(String mobileno,String play_date,Integer shownumber_id,String theater_sn,String[] area_codes,Integer[] row_nos,Integer[] col_nos) throws ClassNotFoundException{
		JSONObject err = new JSONObject();
		err.put("errcode", 0);
		err.put("errmsg", "请求成功");
		if(StringUtils.isEmpty(mobileno)){
			err.put("errcode", 1001);
			err.put("errmsg", "mobileno不能为空");
		}
		if(StringUtils.isEmpty(play_date)){
			err.put("errcode", 1001);
			err.put("errmsg", "play_date不能为空");
		}
		if(shownumber_id==null){
			err.put("errcode", 1001);
			err.put("errmsg", "showNumber_id不能为空");
		}
		if(area_codes.length!=row_nos.length||area_codes.length!=col_nos.length){
			err.put("errcode", 1001);
			err.put("errmsg", "座位坐标不准确，请核对");
		}
		JSONObject resObject = new JSONObject();
		if(err.getInteger("errcode")>0){
			resObject.put("code", err);
			ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
			return responseEntity;
		}
		List<Theater> theaters = dbServices.findList("Theater", "{sn:'"+theater_sn+"'}", "");
		Theater theater = theaters.get(0);
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		Date time = new Date();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		Date playDate = DateTime.parse(play_date, dtf).toDate();
		for(int n=0;n<row_nos.length;n++){
			paramMap.put(0, col_nos[n]);
			paramMap.put(1, row_nos[n]);
			paramMap.put(2, area_codes[n]);
			paramMap.put(3, theater_sn);
			paramMap.put(4, play_date);
			String hql = "from LockedSeat where columnNum=? and rowNum=? and areaCode=? and theaterSn=? and date_format(playDate,'%Y-%m-%d')=?";
			List<LockedSeat> lockedSeats = dbServices.findListByHql(hql, paramMap);
			if(lockedSeats.size()==0){
				paramMap.put(3, theater.getId());
				paramMap.remove(4);
				hql = "from Seat where columnNum=? and rowNum=? and areaId in(select id from Area where code=? and belongto=?)";
				List<Seat> seats = dbServices.findListByHql(hql, paramMap);
				Seat seat = seats.get(0);
				LockedSeat ls = new LockedSeat();
				ls.setCode(mobileno);
				ls.setColumnNum(col_nos[n]);
				ls.setRowNum(row_nos[n]);
				ls.setSeatId(seat.getId());
				ls.setLockTime(time);
				ls.setTheaterSn(theater_sn);
				ls.setAreaCode(seat.getAreaCode());
				ls.setPlayDate(playDate);
				dbServices.persist(ls);
			}
		}
		ResponseEntity<JSONObject> responseEntity = getSeatStatusMap(mobileno, play_date, shownumber_id, theater_sn);
		return responseEntity;
	}
	
	@RequestMapping(value="/unlockSeats")
	public ResponseEntity<JSONObject> unlockSeats(String mobileno,String play_date,Integer shownumber_id,String theater_sn,String[] area_codes,Integer[] row_nos,Integer[] col_nos) throws ClassNotFoundException{
		JSONObject err = new JSONObject();
		err.put("errcode", 0);
		err.put("errmsg", "请求成功");
		if(StringUtils.isEmpty(mobileno)){
			err.put("errcode", 1001);
			err.put("errmsg", "mobileno不能为空");
		}
		if(StringUtils.isEmpty(play_date)){
			err.put("errcode", 1001);
			err.put("errmsg", "play_date不能为空");
		}
		if(shownumber_id==null){
			err.put("errcode", 1001);
			err.put("errmsg", "showNumber_id不能为空");
		}
		if(area_codes.length!=row_nos.length||area_codes.length!=col_nos.length){
			err.put("errcode", 1001);
			err.put("errmsg", "座位坐标不准确，请核对");
		}
		JSONObject resObject = new JSONObject();
		if(err.getInteger("errcode")>0){
			resObject.put("code", err);
			ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
			return responseEntity;
		}
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		for(int n=0;n<row_nos.length;n++){
			paramMap.put(0, col_nos[n]);
			paramMap.put(1, row_nos[n]);
			paramMap.put(2, area_codes[n]);
			paramMap.put(3, theater_sn);
			paramMap.put(4, play_date);
			String hql = "delete from LockedSeat where columnNum=? and rowNum=? and areaCode=? and theaterSn=? and date_format(playDate,'%Y-%m-%d')=?";
			dbServices.executeByHql(hql, paramMap);
		}
		
		ResponseEntity<JSONObject> responseEntity = getSeatStatusMap(mobileno, play_date, shownumber_id, theater_sn);
		return responseEntity;
	}
	

	@RequestMapping(value="/saleTickets")
	public ResponseEntity<JSONObject> saleTickets(AreaSeatData data,String play_date,String mobileno,Integer shownumber_id,HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException{
		JSONObject err = new JSONObject();
		err.put("errcode", 0);
		err.put("errmsg", "请求成功");
		if(StringUtils.isEmpty(mobileno)){
			err.put("errcode", 1001);
			err.put("errmsg", "mobileno不能为空");
		}
		if(StringUtils.isEmpty(play_date)){
			err.put("errcode", 1001);
			err.put("errmsg", "play_date不能为空");
		}
		if(shownumber_id==null){
			err.put("errcode", 1001);
			err.put("errmsg", "showNumber_id不能为空");
		}
		Map<String,List<String>> map = data.getSeat_map();
		Map<String,Integer> priceMap = data.getPrice_map();
		if(priceMap.size()==0){
			err.put("errcode", 1001);
			err.put("errmsg", "price_map不能为空");
		}
		Set<String> areaCodes = map.keySet();
		JSONObject areaOutJson = new JSONObject();
		JSONObject areaJson = new JSONObject();
		String theatersn = data.getTheater_sn();
		if(theatersn==null){
			err.put("errcode", 1001);
			err.put("errmsg", "theatersn不能为空");
		}
		
		JSONObject resObject = new JSONObject();
		if(err.getInteger("errcode")>0){
			resObject.put("code", err);
			ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
			return responseEntity;
		}
		
		List<Theater> theaters = dbServices.findList("Theater", "{sn:'" + theatersn + "'}", "");
		Theater theater = theaters.get(0);
		JSONObject param = new JSONObject();
		//{"5":["1694","1693"],"6":["21","20"]}
		//{"strategy":"2","cheapPrice":"0","payType":"0207001","offsetPrice":"0","discount":"0","realPrice":"0",
		//	"memberNo":"","memberId":"","memberName":"","memberMobile":"","totalSize":"4","priceShowType":"0209001",
//		"isShowSeat":true,"hasTea":"0103001","giftNum":"0","status":"0203001","playDate":"2016-06-26","showNumberId":"1",
//		"areaPrice":{"5":{"seatIds":["1694","1693"],"price":"280"},"6":{"seatIds":["21","20"],"price":"280"}}}
		param.put("strategy", 2);
		param.put("cheapPrice", 0);
		param.put("playDate", play_date);
		param.put("payType", "0207001");
		param.put("offsetPrice", 0);
		param.put("discount", 0);
		param.put("realPrice", 0);
		param.put("priceShowType", "0209001");
		param.put("isShowSeat", true);
		param.put("hasTea", "0103001");
		param.put("giftNum", 0);
		param.put("status", "0203001");
		param.put("showNumberId", 1);
		int totallSize = 0;
		JSONObject priceJson = new JSONObject();
		for(String areaCode : areaCodes){
			List<Area> areas = dbServices.findList("Area", "{code:'"+areaCode+"',belongto:"+theater.getId()+"}", "");
			if(areas.size()==0){
				err.put("errcode", 1001);
				err.put("errmsg", "区域不存在");
				resObject.put("code", err);
				ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
				return responseEntity;
			}else{
				Area area = areas.get(0);
				List<String> seatPositions = map.get(areaCode);
				Integer price = priceMap.get(areaCode);
				areaJson.put("price",price);
				JSONArray arr = new JSONArray();
				HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
				for(String info : seatPositions){
					String[] ps = StringUtils.split(info,",");
					paramMap.put(0,Integer.valueOf(ps[0]));
					paramMap.put(1,Integer.valueOf(ps[1]));
					paramMap.put(2, area.getId());
					String hql = "from Seat where rowNum=? and columnNum=? and areaId=?";
					List<Seat> list = dbServices.findListByHql(hql, paramMap);
					for(Seat seat : list){
						arr.add(seat.getId());
						totallSize++;
					}
				}
				areaJson.put("seatIds", arr);
				areaOutJson.put(area.getId()+"", areaJson);	
				priceJson.put("seatIds", arr);
				param.put("totallSize",totallSize);
				param.put("areaPrice",areaOutJson);
			}
			baseDwr.saleTickets(JSON.toJSONString(param),request,response);
		}
		ResponseEntity<JSONObject> responseEntity = getSeatStatusMap(mobileno, play_date, shownumber_id, theatersn);
		return responseEntity;
	}
	
	@RequestMapping(value="/clearExpiredLockedSeat")
	public ResponseEntity<JSONObject> clearExpiredLockedSeat(HttpServletRequest request,HttpServletResponse response) throws ClassNotFoundException{
		JSONObject err = new JSONObject();
		err.put("errcode", 0);
		err.put("errmsg", "请求成功");
		JSONObject resObject = new JSONObject();
		resObject.put("code", err);
		JSONObject param = new JSONObject();
		ResponseEntity<JSONObject> responseEntity = new ResponseEntity<JSONObject>(resObject,HttpStatus.OK);
		return responseEntity;
	}
	
}
