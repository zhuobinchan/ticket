package com.tjing.bussiness.tag;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.tjing.bussiness.model.Floor;
import com.tjing.bussiness.model.ShowNumber;
import com.tjing.bussiness.model.Theater;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.BeanAid;

@SuppressWarnings("serial")
public class CreateTheaterTopDesc extends TagSupport{
	private DbServices dbServices;
	private boolean showInput;
	public int doStartTag() throws JspException {
		dbServices = BeanAid.getBean(DbServices.class);
		HttpSession session = pageContext.getSession();
		Theater theater = (Theater) session.getAttribute("theater");
		JspWriter out = pageContext.getOut();
		try {
			StringBuilder buf = new StringBuilder();
			String dateString = DateTime.now().toString("yyyy-MM-dd");
			List<Floor> floors = dbServices.getFloors(theater.getId());
			buf.append("<table cellspacing='1' width='90%' align='center' class='topInfo' style='background-color: #404040;color: #fff;font-size: 14px'>")
			.append("<tr>");
			if(showInput){
				
				List<ShowNumber> sns = dbServices.findList("ShowNumber", "", "orderno");
				buf.append("<td width='130'>")
				.append("<input type='text' value='"+dateString+"' name='playDate' id='playDate' onfocus='WdatePicker({onpicking:function(){refreshSeatStatus($dp.cal.getNewDateStr())}})' align='center' size='10'/>")
				.append("</td><td>")
				.append("<select id='showNumber'>");
				for(ShowNumber sn : sns){
					buf.append("<option value='"+sn.getId()+"'>").append(sn.getName()).append("</option>");
				}
				buf.append("</select>")
				.append("</td>");
			}
			for(Floor floor : floors){
				buf.append("<td width='80' align='center'><a class='btn6' onclick='selectFloor("+floor.getOrders()+")'>")
				.append(floor.getName())
				.append("</a></td>");
			}
			
			buf.append("<td width='80' align='center'><a class='btn6' href='javascript:void(0)' id='sizeSwitch'>全屏</a></td>")
			.append("<td width='250' align='center'><span id='seatDescs'></span></td>")
			.append("<td>演出日期：<span id='topPlayDate' style='color:#CD6600;font-weight:bold;padding-right:10px'>"+dateString+"</span></td>")
			.append("</tr>")
			.append("</table>");
			out.write(buf.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setShowInput(boolean showInput) {
		this.showInput = showInput;
	}

}
