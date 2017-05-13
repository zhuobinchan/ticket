package com.tjing.bussiness.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tjing.bussiness.excelview.CommonExcelView;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.ToolHelper;

@RestController
@RequestMapping("/public/venuestat")
public class VenueStatController {
	@Autowired
	private DbServices dbServices;

	@Autowired
	private CommonExcelView excelView;

	/**
	 * 场内消费销售统计
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/querySalesStat")
	public List<Object[]> querySalesStat(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		String order = "";
		if (jobj.containsKey("createTime")) {
			where += " and DATE_FORMAT(create_time,'%Y-%m-%d') >= '" + jobj.getString("createTime") + "' ";
		}
		if (jobj.containsKey("createTime2")) {
			where += " and DATE_FORMAT(create_time,'%Y-%m-%d') <= '" + jobj.getString("createTime2") + "' ";
		}
		if (jobj.containsKey("order")) {
			order = jobj.getString("order");
			order = " order by " + order + ("name".equals(order) ? "" : " desc ");
		}
		String sql = "select * from (select convert(max(g.name) using gbk) as name, max(t.name) as type, max(g.price) as price, sum(d.goods_count) as count, SUM(d.total) as total "
				+ " from bi_venue_sales_detail d, bi_goods g, bi_goods_type t "
				+ " where d.goods_id = g.id and g.type_id = t.id and "
				+ " sales_id in (select id from bi_venue_sales where status = '0227003' and strategy != '0225005' "
				+ where + " ) GROUP BY d.goods_id " + order + " ) goods UNION "
				+ " select * from (select convert(max(g.name) using gbk) as name, max(t.name) as type, 0 as price, sum(d.goods_count) as count, SUM(d.total) as total "
				+ " from bi_venue_sales_detail d, bi_goods g, bi_goods_type t "
				+ " where d.goods_id = g.id and g.type_id = t.id and "
				+ " sales_id in (select id from bi_venue_sales where status = '0227003' and strategy = '0225005' "
				+ where + " ) GROUP BY d.goods_id " + order + " ) frees UNION "
				+ " select * from (select convert(max(p.name) using gbk) as name, '套餐' as type, max(p.price) as price, count(s.package_id) as count, 0 as total "
				+ " from bi_venue_sales s, bi_package p "
				+ " where s.package_id = p.id and s.id in (select id from bi_venue_sales where status = '0227003' "
				+ where + " ) GROUP BY s.package_id " + order + " ) packages UNION "
				+ " select * from (select convert(CONCAT(max(d.text), '优惠') using gbk) as name, '优惠' as type, '' as price, count(s.strategy) as count, SUM(s.real_pay - s.receivable) as total "
				+ " from bi_venue_sales s, tj_dic d " + " where s.strategy = d.code and "
				+ " s.id in (select id from bi_venue_sales where status = '0227003' " + where + " ) "
				+ " GROUP BY s.strategy" + order + " ) strategys";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"商品名称", "类别", "单价", "数量", "金额"});

		// 表尾
		Object[] total = new Object[]{"合计", "", "", "", 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

	/**
	 * 导出刚刚查询的报表
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportExcelReport")
	public ModelAndView exportExcelReport(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(excelView, model);
	}

	/**
	 * 场内消费提成统计，只针对已结算的进行统计
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryCommisionStat")
	public List<Object[]> queryCommisionStat(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		if (jobj.containsKey("createTime")) {
			where += " and DATE_FORMAT(v.create_time,'%Y-%m-%d') >= '" + jobj.getString("createTime") + "' ";
		}
		if (jobj.containsKey("createTime2")) {
			where += " and DATE_FORMAT(v.create_time,'%Y-%m-%d') <= '" + jobj.getString("createTime2") + "' ";
		}
		String sql = "select s.name, round(sum(case g.commision when '0229001' then (g.price * d.goods_count * v.real_pay / v.receivable) else 0 end)), "
				+ " sum((case g.commision when '0229001' then d.goods_count else 0 end)), "
				+ " concat(round(max((select commision from bi_venue_commision where goods_commision = '0229001' and type = s.type)) * 100), '%'), "
				+ " round(sum(case g.commision when '0229001' then (g.price * d.goods_count * v.real_pay / v.receivable) * c.commision else 0 end)), "
				+ " round(sum(case g.commision when '0229002' then (g.price * d.goods_count * v.real_pay / v.receivable) else 0 end)), "
				+ " sum((case g.commision when '0229002' then d.goods_count else 0 end)), "
				+ " concat(round(max((select commision from bi_venue_commision where goods_commision = '0229002' and type = s.type)) * 100), '%'), "
				+ " round(sum(case g.commision when '0229002' then (g.price * d.goods_count * v.real_pay / v.receivable) * c.commision else 0 end)), "
				+ " round(sum(case g.commision when '0229001' then (g.price * d.goods_count * v.real_pay / v.receivable) * c.commision else 0 end)) + "
				+ " round(sum(case g.commision when '0229002' then (g.price * d.goods_count * v.real_pay / v.receivable) * c.commision else 0 end))"
				+ " from bi_salesman s, bi_venue_commision c, bi_goods g, bi_venue_sales v, bi_venue_sales_detail d "
				+ " where s.type = c.type and g.commision = c.goods_commision and g.id = d.goods_id and v.id = d.sales_id "
				+ " and s.id = v.salesmanId and v.status = '0227003' " + where + " group by s.id";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"营销人员", "普通营销金额", "普通营销数量", "普通提成比例", "普通提成金额", "棒类营销金额", "棒类营销数量", "棒类提成比例",
				"棒类提成金额", "提成合计"});

		// 表尾
		Object[] total = new Object[]{"合计", 0, 0, "", 0, 0, 0, "", 0, 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

	/**
	 * 场内消费按支付方式统计
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryPayTypeStat")
	public List<Object[]> queryPayTypeStat(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		if (jobj.containsKey("createTime")) {
			where += " and DATE_FORMAT(s.create_time,'%Y-%m-%d') >= '" + jobj.getString("createTime") + "' ";
		}
		if (jobj.containsKey("createTime2")) {
			where += " and DATE_FORMAT(s.create_time,'%Y-%m-%d') <= '" + jobj.getString("createTime2") + "' ";
		}
		String sql = "select d.text, count(1), sum(s.receivable), sum(s.real_pay), sum(s.fill_difference) "
				+ " from bi_venue_sales s, tj_dic d " + " where s.status = '0227003' and s.pay_type = d.code " + where
				+ " group by s.pay_type";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"支付方式", "数量", "应收金额", "实收金额", "充值卡补差价金额"});

		// 表尾
		Object[] total = new Object[]{"合计", 0, 0, 0, 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

	/**
	 * 场内消费按消费单状态统计
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryStatusStat")
	public List<Object[]> queryStatusStat(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		if (jobj.containsKey("createTime")) {
			where += " and DATE_FORMAT(s.create_time,'%Y-%m-%d') >= '" + jobj.getString("createTime") + "' ";
		}
		if (jobj.containsKey("createTime2")) {
			where += " and DATE_FORMAT(s.create_time,'%Y-%m-%d') <= '" + jobj.getString("createTime2") + "' ";
		}
		String sql = "select d.text, count(1), sum(s.receivable), sum(s.real_pay) "
				+ " from bi_venue_sales s, tj_dic d where s.status in ('0227002', '0227003') and s.status = d.code "
				+ where + " group by s.status";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"状态", "数量", "应收金额", "实收金额"});

		// 表尾
		Object[] total = new Object[]{"合计", 0, 0, 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

}
