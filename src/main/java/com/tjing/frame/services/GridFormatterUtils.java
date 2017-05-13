package com.tjing.frame.services;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tjing.bussiness.model.Ticket;
import com.tjing.bussiness.model.TicketSale;
import com.tjing.frame.model.Dic;
import com.tjing.frame.util.SimpleDao;

@Component
public class GridFormatterUtils {
	@Autowired
	private DataCache dataCache;
	@Autowired
	private SimpleDao simpleDao;
	public String getStrategyNameByTicketId(Integer id){
		Ticket ticket = simpleDao.getEntity(Ticket.class, id);
		TicketSale ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
		return ts.getStrategyName();
	}
	public String getPayTypeByTicketId(Integer id){
		Ticket ticket = simpleDao.getEntity(Ticket.class, id);
		TicketSale ts = simpleDao.getEntity(TicketSale.class, ticket.getSaleId());
		Map<String, Dic> dicMap = dataCache.getDicsFromCache();
		return dicMap.get(ts.getPayType()).getText();
	}
	//票面总额
	public String sumSaleShowAmount(Integer id){
		Map<Integer,Object> paramMap = new HashMap<Integer,Object>(); 
		paramMap.put(0, id);
		Object amount = simpleDao.getUniqueByHql("select sum(price) from Ticket where saleId=? and status!='0203006'", paramMap);
		amount = amount==null?0:amount;
		return amount.toString();
	}
	//实收总额
	public String sumSaleRealAmount(Integer id){
		Map<Integer,Object> paramMap = new HashMap<Integer,Object>(); 
		paramMap.put(0, id);
		Object amount = simpleDao.getUniqueByHql("select sum(realPrice) from Ticket where saleId=? and status!='0203006' ", paramMap);
		return amount==null?"0":amount.toString();
	}
}
