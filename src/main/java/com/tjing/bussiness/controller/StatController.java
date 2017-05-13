package com.tjing.bussiness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tjing.frame.model.Organization;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.ToolHelper;

@RestController
@RequestMapping("/public/stat")
public class StatController {
	
	static Logger logger = Logger.getLogger(StatController.class); 
	
	@Autowired
	private DbServices dbServices;
	@SuppressWarnings("unused")
	@RequestMapping("/createTypeStat")
	public List<Object[]> createTypeStat() {
		List<Object[]> list = Lists.newArrayList();
		String sql = "select ts.strategy_Name,t.price,t.real_price,count(t.id) "
				+ " ,sum(t.price),sum(t.real_price) from bi_ticket t join bi_ticket_sale ts"
				+ " on t.sale_id=ts.id group by ts.strategy_id,t.real_price";
		Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		return result;
	}
	// 交班表
	@SuppressWarnings("unused")
	@RequestMapping("/createUserStat")
	public List<Object[]> createUserStat(String paramString) {
		
		logger.info("param : " + paramString);
		
		Map<String,Object> paramMap = (Map<String,Object>)JSON.parse(paramString); 
	    for (String key : paramMap.keySet()) { 
	      System.out.println(key+":"+paramMap.get(key)); 
	    } 
		
		List<Object[]> list = Lists.newArrayList();
		String sql = "select ts.create_user_name as userName,y.name as strategyName,t.price,t.real_price,count(t.id) "
				+ " ,sum(t.price),sum(t.real_price) from bi_ticket t join bi_ticket_sale ts"
				+ " on t.sale_id=ts.id join bi_show_number sn on sn.id=ts.show_number_id join bi_ticket_strategy y "
				+ "on y.id=ts.strategy_id where 1=1 ";
		
		if(paramMap.containsKey("createUserId")){
			sql = sql + "  and  ts.create_user_id = " + paramMap.get("createUserId");
		}
		
		if(paramMap.containsKey("showNumberId")){
			sql = sql + "   and ts.show_number_id = "+ paramMap.get("showNumberId");
		}
		
		if(paramMap.containsKey("startCreateTime") && paramMap.containsKey("endCreateTime")){
			sql = sql + "   and str_to_date(ts.create_time,'%Y-%m-%d')  >= str_to_date('" + paramMap.get("startCreateTime") + "','%Y-%m-%d') ";
			sql = sql + "	and str_to_date(ts.create_time,'%Y-%m-%d')  <= str_to_date('" + paramMap.get("endCreateTime") + "','%Y-%m-%d') ";
		}
		
		
		
		 
		 sql = sql + " group by ts.create_user_id,y.id,t.real_price";
		Map<Integer, Object> paramMapValue = new HashMap<Integer, Object>();
		List<Object[]> result = dbServices.findListBySql(sql, paramMapValue);
		 
		return result;
	}
	@SuppressWarnings("unused")
	@RequestMapping("/queryDayAmount")
	public List<Object[]> queryDayAmount() {
		List<Object[]> list = Lists.newArrayList();
		String sql = "select date_format(create_time,'%Y-%m-%d'),sum(real_price) from bi_ticket group by date_format(create_time,'%Y-%m-%d')";
		Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		return result;
	}
	// 售票结算表
	@RequestMapping("/queryTicketSaleBalance")
	public List<Object[]> queryTicketSaleBalance(String paramString) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		int i = 0;
		if (jobj.containsKey("createUserId")) {
			where += " and ts.create_User_Id=?";
			paramMap.put(i++, jobj.getInteger("createUserId"));
		}
		String sql = "select (select name from tj_user where ts.create_user_id=id) as userName,date_format(ts.create_time,'%Y-%m-%d') as createTime"
				+ ",date_format(balance_time,'%Y-%m-%d %H:%i'),(select text from tj_dic where ts.price_show_type=code),"
				+ "date_format(ts.play_date,'%Y-%m-%d')," + "count(t.id),sum(t.price), sum(t.real_price)  "
				+ "from bi_ticket t join bi_ticket_sale ts" + " on t.sale_id=ts.id where ts.status='0211002' " + where
				+ " group by date_format(ts.play_date,'%Y-%m-%d'), ts.balance_time,ts.create_user_id,"
				+ "ts.price_show_type order by ts.create_user_id"
				+ ",date_format(ts.create_time,'%Y-%m-%d'),ts.price_show_type";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		return result;
	}
	// 售票结算表(流水式)
	@RequestMapping("/queryTicketSaleBalanceList")
	public List<Object[]> queryTicketSaleBalanceList(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		int i = 0;
		if (jobj.containsKey("createTime")) {
			where += " and ts.create_time>=date_format(?,'%Y-%m-%d')";
			paramMap.put(i++, jobj.getString("createTime"));
		}
		if (jobj.containsKey("createTime2")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=date_format(?,'%Y-%m-%d')";
			paramMap.put(i++, jobj.getString("createTime2"));
		}
		if (jobj.containsKey("createUserId")) {
			where += " and ts.create_User_Id=?";
			paramMap.put(i++, jobj.getInteger("createUserId"));
		}
		String theatersn = CodeHelper.getCurrentUser().getTheaterSn();
		if (StringUtils.isNotBlank(theatersn)) {
			where += " and ts.theater_sn=?";
			paramMap.put(i++, theatersn);
		}
		String sql = "select date_format(ts.create_time,'%Y-%m-%d') as createTime,(select name from tj_user where ts.create_user_id=id) as userName"
				+ ",sum(case ts.pay_type when '0207001' then t.real_price else 0 end) as cash,"
				+ "sum(case ts.pay_type when '0207002' then t.real_price else 0 end) as card,"
				+ "ifnull(sum(case when (ts.member_no is null or ts.member_no='') then 0 else t.real_price end),0),"// 会员卡
				+ "ifnull(sum(case ts.pay_type when '0207004' then t.real_price else 0 end),0),"// 挂账
				+ "ifnull(sum(case ts.pay_type when '0207003' then t.price else 0 end),0),"// 免单
				+ "ifnull(sum(case ts.pay_type when '0207005' then t.real_price else 0 end),0),"// 微信
				+ "ifnull(sum(case ts.pay_type when '0207006' then t.real_price else 0 end),0),"// 支付宝
				+ "ifnull(sum(case ts.pay_type when '0207010' then t.real_price else 0 end),0),"// 充值卡
				+ "ifnull(sum(case ts.pay_type when '0207011' then t.real_price else 0 end),0),"// 美团
				+ "ifnull(sum(case ts.pay_type when '0207012' then t.real_price else 0 end),0),"// 百度
				+ "count(t.id),"// 有效票
				+ "count(case ts.offset_price when 0 then null else 1 end),"// 抵扣票数
				+ "count(case ts.price_show_type when '0209003' then 1 else null end),"// 浮动
				+ "count(case t.status when '02030031' then 1 else null end),"// 退票数
				+ "count(case when (t.parent_id is null) then null else 1 end),"// 换座票数
				+ "count(case ts.pay_type when '0207003' then 1 else null end),"// 免单票数
				+ "count(case t.strategy_id when 9 then 1 else null end),"// 最低消费
				+ "count(case when (ts.cheap_price=0 && ts.discount=0) then null else 1 end),"// 优惠
				+ "count(case t.status when '0203003' then 1 else null end) "// 改签票数
				+ "from bi_ticket t join bi_ticket_sale ts"
				+ " on t.sale_id=ts.id where t.status!='0203006' and ts.status='0211002' " + where
				+ " group by ts.balance_time,ts.create_user_id" + " order by ts.balance_time,ts.create_user_id"
				+ ",date_format(ts.create_time,'%Y-%m-%d')";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		sql = "select count(case t.status when '0203006' then 1 else null end) "// 退票数
				+ "from bi_ticket t join bi_ticket_sale ts" + " on t.sale_id=ts.id where ts.status='0211002' " + where
				+ " group by ts.balance_time,ts.create_user_id" + " order by ts.balance_time,ts.create_user_id"
				+ ",date_format(ts.create_time,'%Y-%m-%d')";

		List<Object> result2 = dbServices.findListBySql(sql, paramMap);
		int n = 0;
		for (Object[] objs : result) {
			objs[15] = result2.get(n++);
		}
		request.getSession().setAttribute("data", result);
		return result;
	}
	// 营销人员提成明细
	@RequestMapping("/querySalesmanCommission")
	public List<Object[]> querySalesmanCommission(HttpServletRequest request) {
		JSONObject param = CodeHelper.getRequestParams(request);
		String where = "(ts.member_no='' or ts.member_no is null)";
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();
		int i = 0;
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("createTime"));
		}
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("createTime2"));
		}
		if (param.containsKey("playDate")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("playDate"));
		}
		if (param.containsKey("playDate2")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("playDate2"));
		}
		if (param.containsKey("createUserId")) {
			where += " and create_user_id=?";
			paramMap.put(i++, param.getInteger("createUserId"));
		}
		if (param.containsKey("deptId")) {
			where += " and (m.org_id=? or m.dept_id=?)";
			paramMap.put(i++, param.getInteger("deptId"));
			paramMap.put(i++, param.getInteger("deptId"));
		}
		String sql = "select ts.sale_no,date_format(ts.create_time,'%Y-%m-%d'),m.code,m.name,"
				+ "(select name from tj_organization where id=m.dept_id),"
				+ "(select name from tj_organization where id=m.org_id),"
				+ "(select id from tj_organization where id=(select org_id from tj_user where id=ts.create_user_id)),"// 预订场所
				+ "(select name from tj_user where id=ts.create_user_id),"
				+ "(select sum(real_price) from bi_ticket where sale_id=ts.id) as realAmount,"// 消费金额
				+ "concat(m.commision_rate,'%'),"
				+ "m.commision_rate*(select sum(real_price) from bi_ticket where sale_id=ts.id)/100,"
				+ "(select count(id) from bi_ticket where sale_id=ts.id) as ticketNum"// 票数
				+ " from bi_ticket_sale ts inner join bi_salesman m on ts.saller_code=m.code where " + where
				+ " order by ts.create_time";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		for (Object[] objs : result) {
			Integer orgId = Integer.valueOf(objs[6].toString());
			Organization org = dbServices.getEntity(Organization.class, orgId);
			if (org.getTier().intValue() > 1) {
				org = dbServices.getEntity(Organization.class, org.getParentId());
				if (org.getTier().intValue() > 1) {
					org = dbServices.getEntity(Organization.class, org.getParentId());
				}
			}
			objs[6] = org.getName();
		}
		request.getSession().setAttribute("data", result);
		return result;
	}
	// 营销人员提成统计
	@RequestMapping("/querySalesmanCommissionStat")
	public List<Object[]> querySalesmanCommissionStat(HttpServletRequest request) {
		JSONObject param = CodeHelper.getRequestParams(request);
		String where = "(ts.member_no='' or ts.member_no is null)";
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();
		int i = 0;
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("createTime"));
		}
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("createTime2"));
		}
		if (param.containsKey("playDate")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("playDate"));
		}
		if (param.containsKey("playDate2")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("playDate2"));
		}
		if (param.containsKey("createUserId")) {
			where += " and create_user_id=?";
			paramMap.put(i++, param.getInteger("createUserId"));
		}
		String sql = "select m.code,m.name," + "(select o.name from tj_organization o where o.id=m.dept_id) as orgName,"
				+ "(select name from tj_organization where id=m.org_id),"
				+ "(select name from tj_user where id=ts.create_user_id),"// 操作员
				+ "sum((select sum(real_price) from bi_ticket where sale_id=ts.id)) as realAmount,"// 消费金额
				+ "concat(m.commision_rate,'%'),"
				+ "sum(m.commision_rate*(select sum(real_price) from bi_ticket where sale_id=ts.id)/100),"// 提成金额
				+ "sum((select count(id) from bi_ticket where sale_id=ts.id)) as ticketNum"// 票数
				+ " from bi_ticket_sale ts inner join bi_salesman m on ts.saller_code=m.code where " + where
				+ " group by ts.saller_code,ts.create_user_id order by ts.create_time";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		request.getSession().setAttribute("data", result);
		return result;
	}

	// 营销人员提成统计2
	@RequestMapping("/querySalesmanCommissionStat2")
	public List<Object[]> querySalesmanCommissionStat2(String paramString, HttpServletRequest request) {
		JSONObject param = JSON.parseObject(paramString);
		String where = "(ts.member_no='' or ts.member_no is null)";
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();
		int i = 0;
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("createTime"));
		}
		if (param.containsKey("createTime2")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("createTime2"));
		}
		if (param.containsKey("playDate")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("playDate"));
		}
		if (param.containsKey("playDate2")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("playDate2"));
		}
		if (param.containsKey("dept")) {
			where += " and m.dept_id=?";
			paramMap.put(i++, param.getInteger("dept"));
		}
		String sql = "select m.code,m.name," + "(select o.name from tj_organization o where o.id=m.dept_id) as orgName,"
				+ "(select name from tj_organization where id=m.org_id),"
				+ "sum((select sum(real_price) from bi_ticket where sale_id=ts.id)) as realAmount,"// 消费金额
				+ "concat(m.commision_rate,'%'),"
				+ "sum(m.commision_rate*(select sum(real_price) from bi_ticket where sale_id=ts.id)/100),"// 提成金额
				+ " sum(case ts.pay_type when '0207003' then ts.ticket_num else 0 end), " // 免单票数
				+ "sum((select count(id) from bi_ticket where sale_id=ts.id)) as ticketNum"// 票数
				+ " from bi_ticket_sale ts inner join bi_salesman m on ts.saller_code=m.code where " + where
				+ " group by ts.saller_code order by ts.saller_code";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"营销编号", "营销员", "服务部门", "工作单位", "消费金额", "提成比例", "提成金额", "免单票数", "票数"});

		// 表尾
		Object[] total = new Object[]{"合计", "", "", "", 0, "", 0, 0, 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

	/**
	 * @作者：曾平
	 * @日期：2016年4月7日 上午8:34:07
	 * @用途：列出有会员卡的销售记录的提成明细
	 */
	@RequestMapping("/queryMemberCommission")
	public List<Object[]> queryMemberCommission(HttpServletRequest request) {
		JSONObject param = CodeHelper.getRequestParams(request);
		String where = "(ts.member_no<>'' and ts.member_no is not null)";
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();
		int i = 0;
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("createTime"));
		}
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("createTime2"));
		}
		if (param.containsKey("playDate")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("playDate"));
		}
		if (param.containsKey("playDate2")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("playDate2"));
		}
		if (param.containsKey("createUserId")) {
			where += " and create_user_id=?";
			paramMap.put(i++, param.getInteger("createUserId"));
		}
		String sql = "select ts.sale_no,date_format(ts.create_time,'%Y-%m-%d'),ts.member_no,m.code,m.name,"
				+ "(select o.name from tj_organization o where o.id=m.dept_id) as orgName,"
				+ "(select name from tj_organization where id=m.org_id),"// 工作单位
				+ "(select id from tj_organization where id=(select org_id from tj_user where id=ts.create_user_id)),"// 预订场所
				+ "(select name from tj_user where id=ts.create_user_id),"
				+ "(select sum(real_price) from bi_ticket where sale_id=ts.id) as realAmount,"// 消费金额
				+ "concat(m.commision_rate,'%'),"
				+ "m.commision_rate*(select sum(real_price) from bi_ticket where sale_id=ts.id)/100,"
				+ "(select count(id) from bi_ticket where sale_id=ts.id) as ticketNum"// 票数
				+ " from bi_ticket_sale ts inner join bi_salesman m on ts.saller_code=m.code where " + where
				+ " order by ts.create_time";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		for (Object[] objs : result) {
			Integer orgId = Integer.valueOf(objs[7].toString());
			Organization org = dbServices.getEntity(Organization.class, orgId);
			if (org.getTier().intValue() > 1) {
				org = dbServices.getEntity(Organization.class, org.getParentId());
				if (org.getTier().intValue() > 1) {
					org = dbServices.getEntity(Organization.class, org.getParentId());
				}
			}
			objs[7] = org.getName();
		}
		request.getSession().setAttribute("data", result);
		return result;
	}
	/**
	 * @作者：曾平
	 * @日期：2016年4月7日 上午8:35:44
	 * @用途：按日期、营销员、操作员合计提成金额
	 */
	@RequestMapping("/queryMemberCommissionStat")
	public List<Object[]> queryMemberCommissionStat(HttpServletRequest request) {
		JSONObject param = CodeHelper.getRequestParams(request);
		String where = "(ts.member_no<>'' and ts.member_no is not null)";
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();
		int i = 0;
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("createTime"));
		}
		if (param.containsKey("createTime")) {
			where += " and date_format(ts.create_time,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("createTime2"));
		}
		if (param.containsKey("playDate")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')>=?";
			paramMap.put(i++, param.getString("playDate"));
		}
		if (param.containsKey("playDate2")) {
			where += " and date_format(ts.play_date,'%Y-%m-%d')<=?";
			paramMap.put(i++, param.getString("playDate2"));
		}
		if (param.containsKey("createUserId")) {
			where += " and create_user_id=?";
			paramMap.put(i++, param.getInteger("createUserId"));
		}
		String sql = "select m.code,m.name," + "(select o.name from tj_organization o where o.id=m.dept_id) as orgName,"
				+ "(select name from tj_organization where id=m.org_id),"
				+ "(select name from tj_user where id=ts.create_user_id),"// 操作员
				+ "sum((select sum(real_price) from bi_ticket where sale_id=ts.id)) as realAmount,"// 消费金额
				+ "concat(m.commision_rate,'%'),"
				+ "sum(m.commision_rate*(select sum(real_price) from bi_ticket where sale_id=ts.id)/100),"// 提成金额
				+ "sum((select count(id) from bi_ticket where sale_id=ts.id)) as ticketNum"// 票数
				+ " from bi_ticket_sale ts inner join bi_salesman m on ts.saller_code=m.code" + " where " + where
				+ " group by ts.saller_code,ts.create_user_id order by ts.create_time";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		request.getSession().setAttribute("data", result);
		return result;
	}

	/**
	 * 查询订座表
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryOrderSeat")
	public List<Object[]> queryOrderSeat(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = " where 1 = 1 ";
		if (jobj.containsKey("createTime")) {
			where += " and DATE_FORMAT(b.play_date,'%Y-%m-%d') = '" + jobj.getString("createTime") + "' ";
		}
		if (jobj.containsKey("memberMobile")) {
			where += " and m.mobileno like '%" + jobj.getString("memberMobile") + "%' ";
		}
		if (jobj.containsKey("memberName")) {
			where += " and m.`name` like '%" + jobj.getString("memberName") + "%' ";
		}
		String sql = "select name, ticket_num, seat, mobile, saled from ( "
				+ " select name, ticket_num, seat, create_time, (CASE when uu REGEXP '[a-z]' then '' else uu end) mobile, '' as saled from ( "
				+ " select GROUP_CONCAT(seat) seat, create_time, sum(ticket_num) ticket_num, name, IFNULL(mobileno ,UUID()) as uu from (  "
				+ " select GROUP_CONCAT(s.`name`) seat, r.create_time, r.ticket_num, r.id, r.`name`, r.mobileno "
				+ " from "
				+ " (select b.id, b.ticket_num, b.create_time, m.`name`, m.mobileno from bi_ticket_reserve b left join bi_member m on member_id = m.id "
				+ where + " ) r,  " + " bi_ticket t, bi_seat s  " + " where t.reserve_id = r.id and t.seat_id = s.id "
				+ " group by r.id) c " + " group by uu )d " + " UNION "
				+ " select name, ticket_num, seat, create_time, (CASE when uu REGEXP '[a-z]' then '' else uu end) mobile, '己购' as saled from ( "
				+ " select GROUP_CONCAT(seat) seat, create_time, sum(ticket_num) ticket_num, name, IFNULL(mobileno ,UUID()) as uu from (  "
				+ " select GROUP_CONCAT(s.`name`) seat, r.create_time, r.ticket_num, r.id, r.`name`, r.mobileno "
				+ " from  "
				+ " (select b.id, b.ticket_num, b.create_time, m.`name`, m.mobileno from bi_ticket_sale b left join bi_member m on member_id = m.id "
				+ where + " ) r,  " + " bi_ticket t, bi_seat s  " + " where t.sale_id = r.id and t.seat_id = s.id "
				+ " group by r.id) c " + " group by uu )d " + " order by create_time ) e";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"客人姓名", "人数", "台号", "电话", "备注"});

		// 表尾
		Object[] total = new Object[]{"合计", 0, "", "", ""};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}
}
