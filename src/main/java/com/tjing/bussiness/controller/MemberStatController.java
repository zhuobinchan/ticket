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
import com.tjing.bussiness.excelview.MemberCardConsumerExcelView;
import com.tjing.bussiness.excelview.MemberCardExcelView;
import com.tjing.bussiness.excelview.MemberCardRechargeExcelView;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.ToolHelper;

@RestController
@RequestMapping("/public/memberstat")
public class MemberStatController {
	@Autowired
	private DbServices dbServices;

	@Autowired
	private MemberCardExcelView excelView;

	@Autowired
	private MemberCardRechargeExcelView rechargeView;
	
	@Autowired
	private MemberCardConsumerExcelView consumerView;

	/**
	 * 计算卡余额合计
	 * 
	 * @param paramString
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryCardBalanceSum")
	public List<Object[]> queryCardBalanceSum(String paramString, HttpServletRequest request) {
		JSONObject jobj = JSON.parseObject(paramString);
		HashMap<Integer, Object> paramMap = new HashMap<Integer, Object>();

		String where = "";
		if (jobj.containsKey("name")) {
			where += " and name = '" + jobj.getString("name") + "' ";
		}
		String sql = "select d.text, c.total "
				+ " from tj_dic d, (select name, sum(balance) total from bi_member_card where status in ('0218001', '0218003') "
				+ where + " group by name) c " + " where d.code = c.name";
		List<Object[]> result = dbServices.findListBySql(sql, paramMap);
		// 表头
		result.add(0, new Object[]{"卡名称", "余额合计"});

		// 表尾
		Object[] total = new Object[]{"合计", 0};
		result = ToolHelper.calculateReportTotal(result, total);
		request.getSession().setAttribute("data", result);
		return result;
	}

	@RequestMapping(value = "/exportMemberCardExcel")
	public ModelAndView exportMemberCardExcel(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(excelView, model);
	}

	@RequestMapping(value = "/exportMemberCardRechargeExcel")
	public ModelAndView exportMemberCardRechargeExcel(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(rechargeView, model);
	}

	@RequestMapping(value = "/exportMemberCardConsumerExcel")
	public ModelAndView exportMemberCardConsumerExcel(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(consumerView, model);
	}

}
