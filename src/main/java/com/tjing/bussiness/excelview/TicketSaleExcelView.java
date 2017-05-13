package com.tjing.bussiness.excelview;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Criteria;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.bussiness.model.TicketSale;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.Constant;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.services.GridFormatterUtils;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DbAid;
@Component
public class TicketSaleExcelView extends ExcelViewModel{
	@Autowired
	private GridFormatterUtils gridFormatterUtils;
	@Autowired
	private DbAid dbAid;
	@Autowired
	private DbServices dbServices;
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	protected void buildExcelDocument(Map<String, Object> model,HSSFWorkbook hw, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String exPath = Constant.FULLPATH + "/WEB-INF/conf/ex.xml";
		File exFile = new File(exPath);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(exFile);
		Element root = doc.getRootElement();
		Element excelElement = (Element) root.selectSingleNode("/excels/excel[@id='ticketsale_stat']");
		List<Element> nodes = excelElement.selectNodes("cols/col");
		Element titleElement = (Element) excelElement.selectSingleNode("title");
		ReportAid ra = new ReportAid();
		String title = titleElement.getText();
		HSSFSheet hs = hw.createSheet("统计表");
		
		int rowIndex = 0;
		HSSFRow hr = ra.getRow(hs, rowIndex++);
		hr.setHeight((short)600);
        HSSFCell hc = ra.getCell(hr, 0);
        Font titlefont = hw.createFont();
        titlefont.setFontHeight((short) 320);
        style = ra.createCellStyle(hw);
      	ra.addMergedRegion(hs, 0, 0, 0, nodes.size()-1, style);
        HSSFCellStyle titleStyle = ra.createCellStyle(hw,titlefont);
        ra.setCellStyle(titleStyle, hr, nodes.size());
        setText(hc, title);   
        HSSFCellStyle infStyle = ra.createCellStyle(hw);
        infStyle.setFillForegroundColor((short)22); 
        infStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        hr = ra.getRow(hs, rowIndex++);
        hc = ra.getCell(hr, 0);
        ra.setCellValue(hc, "制表日期",infStyle);
        hc = ra.getCell(hr, 1);
        ra.setCellValue(hc, new DateTime().now().toString("yyyy-MM-dd"),infStyle);
        
        hc = ra.getCell(hr, 2);
        ra.setCellValue(hc, "制表人", infStyle);
        hc = ra.getCell(hr, 3);
        ra.setCellValue(hc, CodeHelper.getCurrentUser().getName(), infStyle);
        ra.setCellValue(hs, hr, 4, "", infStyle);
        ra.setCellValue(hs, hr, 5, "", infStyle);
        ra.setCellValue(hs, hr, 6, "", infStyle);
        ra.setCellValue(hs, hr, 7, "", infStyle);
        ra.setCellValue(hs, hr, 8, "", infStyle);
        ra.setCellValue(hs, hr, 9, "", infStyle);
        ra.setCellValue(hs, hr, 10, "", infStyle);
        ra.setCellValue(hs, hr, 11, "", infStyle);
        ra.setCellValue(hs, hr, 12, "", infStyle);
        ra.setCellValue(hs, hr, 13, "", infStyle);
        ra.setCellValue(hs, hr, 14, "", infStyle);
        ra.setCellValue(hs, hr, 15, "", infStyle);
        JSONObject retJson = (JSONObject)request.getSession().getAttribute("retJson");
        HSSFCellStyle headerStyle = ra.createHeadCellStyle(hw);
        hr = ra.getRow(hs, rowIndex++);
        ra.setCellValue(hs, hr, 0, "现金", style);
        ra.setCellValue(hs, hr, 1, retJson.getString("cashAmount"), style);
        ra.setCellValue(hs, hr, 2, "信用卡", style);
        ra.setCellValue(hs, hr, 3, retJson.getString("cardAmount"), style);
        ra.setCellValue(hs, hr, 4, "会员卡", style);
        ra.setCellValue(hs, hr, 5, retJson.getString("memberAmount"), style);
        ra.setCellValue(hs, hr, 6, "挂账", style);
        ra.setCellValue(hs, hr, 7, retJson.getString("rememberAmount"), style);
        ra.setCellValue(hs, hr, 8, "免单", style);
        ra.setCellValue(hs, hr, 9, retJson.getString("freeAmount"), style);
        ra.setCellValue(hs, hr, 10, "退票", style);
        ra.setCellValue(hs, hr, 11, retJson.getString("backAmount"), style);
        ra.setCellValue(hs, hr, 12, "总额", style);
        ra.setCellValue(hs, hr, 13, retJson.getString("amount"), style);
        ra.setCellValue(hs, hr, 14, "用票数", style);
        ra.setCellValue(hs, hr, 15, retJson.getString("usePaperNum"), style);
        hr = ra.getRow(hs, rowIndex++);
        ra.setCellValue(hs, hr, 0, "原价", style);
        ra.setCellValue(hs, hr, 1, retJson.getString("shouldNum"), style);
        ra.setCellValue(hs, hr, 2, "浮动", style);
        ra.setCellValue(hs, hr, 3, retJson.getString("plusNum"), style);
        ra.setCellValue(hs, hr, 4, "抵扣票数", style);
        ra.setCellValue(hs, hr, 5, retJson.getString("offsetNum"), style);
        ra.setCellValue(hs, hr, 6, "改签", style);
        ra.setCellValue(hs, hr, 7, retJson.getString("changeDateNum"), style);
        ra.setCellValue(hs, hr, 8, "换座", style);
        ra.setCellValue(hs, hr, 9, retJson.getString("changeSeatNum"), style);
        ra.setCellValue(hs, hr, 10, "免票", style);
        ra.setCellValue(hs, hr, 11, retJson.getString("freeNum"), style);
        ra.setCellValue(hs, hr, 12, "最低消费", style);
        ra.setCellValue(hs, hr, 13, retJson.getString("minFeeNum"), style);
        ra.setCellValue(hs, hr, 14, "优惠", style);
        ra.setCellValue(hs, hr, 15, retJson.getString("cheapNum"), style);
        ra.addMergedRegion(hs, rowIndex, rowIndex, 2, nodes.size()-1, style);
        hr = ra.getRow(hs, rowIndex++);
        ra.setCellValue(hs, hr, 0, "退票", style);
        ra.setCellValue(hs, hr, 1, retJson.getString("backNum"), style);
        String[] fieldNames = new String[nodes.size()];
        String queryString = request.getParameter("queryString");
        String orderbyString = request.getParameter("orderbyString");
        List<TicketSale> list = dbServices.getListByQueryString(queryString,orderbyString);
        ra.addMergedRegion(hs, rowIndex, rowIndex++, 0, nodes.size()-1, style);
        hr = ra.getRow(hs, rowIndex++);
        for(int i=0;i<nodes.size();i++){
        	Element colElement = nodes.get(i);
        	hc = ra.getCell(hr, i);
        	hc.setCellValue(colElement.getText());
        	ra.setCellValue(hs, hr, i, colElement.getText(), headerStyle);
        	String widthString = colElement.attribute("width").getStringValue();
        	int width = Integer.parseInt(widthString);
        	hs.setColumnWidth(i, width*256);
        	fieldNames[i] = colElement.attribute("name").getStringValue();
        }
        for(TicketSale ts : list){
        	 hr = ra.getRow(hs, rowIndex++);
        	 ra.setCellValue(hs, hr, 0, ts.getSaleNo(), style);
        	 ra.setCellValue(hs, hr, 1, new DateTime(ts.getCreateTime()).toString("yyyy-MM-dd"), style);
        	 ra.setCellValue(hs, hr, 2, new DateTime(ts.getPlayDate()).toString("yyyy-MM-dd"), style);
        	 ra.setCellValue(hs, hr, 3, ts.getCreateUserName(), style);
        	 ra.setCellValue(hs, hr, 4, dbServices.getDicTextByCode(ts.getPayType()), style);
        	 ra.setCellValue(hs, hr, 5, ts.getStrategyName(), style);
        	 ra.setCellValue(hs, hr, 6, ts.getTicketNum()+"", style);
        	 ra.setCellValue(hs, hr, 7, ts.getPrintNum()+"", style);
        	 ra.setCellValue(hs, hr, 8, ts.getCheapPrice()+"", style);
        	 ra.setCellValue(hs, hr, 9, ts.getOffsetPrice()+"", style);
        	 ra.setCellValue(hs, hr, 10, ts.getDiscount()+"", style);
        	 ra.setCellValue(hs, hr, 11, gridFormatterUtils.sumSaleShowAmount(ts.getId()), style);
        	 ra.setCellValue(hs, hr, 12, gridFormatterUtils.sumSaleRealAmount(ts.getId()), style);
        	 ra.setCellValue(hs, hr, 13, ts.getSallerCode(), style);
        	 ra.setCellValue(hs, hr, 14, ts.getMemberNo(), style);
        	 ra.setCellValue(hs, hr, 15, dbServices.getDicTextByCode(ts.getPriceShowType()), style);
        }
        String filename = ra.encodeFilename(title, request)+".xls";//处理中文文件名  
        response.setContentType("application/vnd.ms-excel");     
        response.setHeader("Content-disposition", "attachment;filename=" + filename);     
        OutputStream ouputStream = response.getOutputStream();     
        hw.write(ouputStream);     
        ouputStream.flush();     
        ouputStream.close();    
		
	}
}
