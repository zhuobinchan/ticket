package com.tjing.bussiness.tag;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.joda.time.DateTime;
import com.tjing.bussiness.model.ShowNumber;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.BeanAid;

@SuppressWarnings("serial")
public class CreatePlayTime extends TagSupport{
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			DbServices dbServices = BeanAid.getBean(DbServices.class);
			String dateString = DateTime.now().toString("yyyy-MM-dd");
			List<ShowNumber> sns = dbServices.findList("ShowNumber", "", "orderno");
			StringBuilder buf = new StringBuilder();
			buf.append("<table align='center' style='border:1px solid' cellpadding='0' cellspacing='0'>")
			.append("<tr><td>")
			.append("<input type='text' value='"+dateString+"' name='playDate' id='playDate' onfocus='WdatePicker({onpicking:function(){refreshSeatStatus($dp.cal.getNewDateStr())}})' align='center' size='10'/>")
			.append("</td><td>")
			.append("<select id='showNumber'>");
			for(ShowNumber sn : sns){
				buf.append("<option value='"+sn.getId()+"'>").append(sn.getName()).append("</option>");
			}
			buf.append("</select>")
			.append("</td>")
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

}
