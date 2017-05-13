package com.tjing.bussiness.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tjing.bussiness.excelview.BalanceExcelView;
import com.tjing.bussiness.excelview.CommisionStatExcelView;
import com.tjing.bussiness.excelview.SalesmanCommisionExcelView;
import com.tjing.bussiness.excelview.TicketSaleExcelView;
@Controller
@RequestMapping("/public/stat")
public class Excelcontroller {
	@Autowired
	private TicketSaleExcelView ticketSaleExcelView;
	@Autowired
	private BalanceExcelView balanceExcelView;
	@Autowired
	private CommisionStatExcelView commisionStatExcelView;
	@Autowired
	private SalesmanCommisionExcelView salesmanCommisionExcelView;
	/**
	 * 曾平
	 * @todo 导出销售统计表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelTicketSaleList")
	public ModelAndView excelTicketSaleList(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(ticketSaleExcelView, model);
	}	
	/**
	 * 曾平
	 * @todo 导出结算表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelBalanceList")
	public ModelAndView excelBalanceList(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(balanceExcelView, model);
	}	
	/**
	 * 曾平
	 * @todo 导出营销提成统计
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelCommisionStat")
	public ModelAndView excelCommisionStat(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(commisionStatExcelView, model);
	}	
	/**
	 * 曾平
	 * @todo 导出营销提成明细
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelSalesmanCommision")
	public ModelAndView excelSalesmanCommision(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(salesmanCommisionExcelView, model);
	}	
	/**
	 * 曾平
	 * @todo 导出会员卡营销提成统计
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelMemberCommisionStat")
	public ModelAndView excelMemberCommisionStat(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(commisionStatExcelView, model);
	}	
	/**
	 * 曾平
	 * @todo 导出营销提成明细
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/excelMemberCommision")
	public ModelAndView excelMemberCommision(ModelMap model, HttpServletRequest request) {
		return new ModelAndView(salesmanCommisionExcelView, model);
	}	
}
