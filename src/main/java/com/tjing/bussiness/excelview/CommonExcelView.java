package com.tjing.bussiness.excelview;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tjing.bussiness.services.MemberDwr;
import com.tjing.bussiness.services.VenueDwr;
import com.tjing.frame.util.CodeHelper;

/**
 * 通用Excel文件生成类
 *
 */
@Component
public class CommonExcelView extends ExcelViewModel {
	@Autowired
	private VenueDwr venueDwr;

	@Autowired
	private MemberDwr memberDwr;

	@SuppressWarnings({"unchecked", "static-access"})
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook hw, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// String exPath = Constant.FULLPATH + "/WEB-INF/conf/ex.xml";
		// File exFile = new File(exPath);
		// SAXReader reader = new SAXReader();
		// Document doc = reader.read(exFile);
		// Element root = doc.getRootElement();
		// Element excelElement = (Element) root.selectSingleNode("/excels/excel[@id='balance_stat']");
		// List<Element> nodes = excelElement.selectNodes("cols/col");
		// Element titleElement = (Element) excelElement.selectSingleNode("title");
		// ra.setCellValue(hs, hr, 4, "", infStyle);
		// ra.setCellValue(hs, hr, 5, "", infStyle);
		// ra.setCellValue(hs, hr, 6, "", infStyle);
		// ra.setCellValue(hs, hr, 7, "", infStyle);
		// ra.setCellValue(hs, hr, 8, "", infStyle);
		// ra.setCellValue(hs, hr, 9, "", infStyle);
		// ra.setCellValue(hs, hr, 10, "", infStyle);
		// ra.setCellValue(hs, hr, 11, "", infStyle);
		// ra.setCellValue(hs, hr, 12, "", infStyle);
		// ra.setCellValue(hs, hr, 13, "", infStyle);
		// ra.setCellValue(hs, hr, 14, "", infStyle);
		// ra.setCellValue(hs, hr, 15, "", infStyle);
		// HSSFCellStyle headerStyle = ra.createHeadCellStyle(hw);
		// String[] fieldNames = new String[nodes.size()];
		// hr = ra.getRow(hs, rowIndex++);
		// for (int i = 0; i < nodes.size(); i++) {
		// Element colElement = nodes.get(i);
		// hc = ra.getCell(hr, i);
		// hc.setCellValue(colElement.getText());
		// ra.setCellValue(hs, hr, i, colElement.getText(), headerStyle);
		// String widthString = colElement.attribute("width").getStringValue();
		// int width = Integer.parseInt(widthString);
		// hs.setColumnWidth(i, width * 256);
		// fieldNames[i] = colElement.attribute("name").getStringValue();
		// }
		List<Object[]> list = (List<Object[]>) request.getSession().getAttribute("data");
		Object[] titles = list.get(0);

		ReportAid ra = new ReportAid();
		// String title = titleElement.getText();
		String title = request.getParameter("title");
		HSSFSheet hs = hw.createSheet("统计表");
		hs.addMergedRegion(new CellRangeAddress(0, 0, 0, titles.length - 1));
		int rowIndex = 0;
		HSSFRow hr = ra.getRow(hs, rowIndex++);
		hr.setHeight((short) 600);
		HSSFCell hc = ra.getCell(hr, 0);
		Font titlefont = hw.createFont();
		titlefont.setFontHeight((short) 320);
		style = ra.createCellStyle(hw);
		style.setWrapText(false);
		HSSFCellStyle titleStyle = ra.createCellStyle(hw, titlefont);
		ra.setCellStyle(titleStyle, hr, titles.length);
		setText(hc, title);
		HSSFCellStyle infStyle = ra.createCellStyle(hw);
		infStyle.setFillForegroundColor((short) 22);
		infStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		hr = ra.getRow(hs, rowIndex++);
		hc = ra.getCell(hr, 0);
		ra.setCellValue(hc, "制表日期", infStyle);
		ra.setCellValue(hs, hr, 1, new DateTime().now().toString("yyyy-MM-dd"), infStyle);

		hc = ra.getCell(hr, 2);
		ra.setCellValue(hc, "制表人", infStyle);
		hc = ra.getCell(hr, 3);
		ra.setCellValue(hc, CodeHelper.getCurrentUser().getName(), infStyle);
		rowIndex++;

		// 表头
		hr = ra.getRow(hs, rowIndex++);
		hc = ra.getCell(hr, 0);
		for (int i = 0; i < titles.length; i++) {
			ra.setCellValue(hs, hr, i, String.valueOf(titles[i]), infStyle);
		}

		// 表格数据
		parseLList(hs, rowIndex, list.subList(1, list.size() - 1));
		HSSFCellStyle styleRate = ra.createCellStyle(hw);
		styleRate.setDataFormat(hw.createDataFormat().getFormat("0.0%"));
		styleDouble = ra.createCellStyle(hw);
		styleDouble.setDataFormat(hw.createDataFormat().getFormat("0.0"));
		HSSFCellStyle infDouble = ra.createCellStyle(hw);
		infDouble.setDataFormat(hw.createDataFormat().getFormat("0.00"));
		infDouble.setFillForegroundColor((short) 22);
		infDouble.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rowIndex += (list.size() - 1);

		// 表尾
		Object[] tail = list.get(list.size() - 1);
		hr = ra.getRow(hs, rowIndex++);
		hc = ra.getCell(hr, 0);
		int totalIndex = 0;
		for (int i = 1; i < tail.length; i++) {
			if ("".equals(tail[i])) {
				totalIndex = i;
			} else {
				break;
			}
		}
		hs.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, totalIndex));
		ra.setCellStyle(infStyle, hr, totalIndex);
		setText(hc, String.valueOf(tail[0]));
		for (int i = totalIndex; i < tail.length; i++) {
			ra.setCellValue(hs, hr, i, String.valueOf(tail[i]), infStyle);
		}
		// hs.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
		// hr = ra.getRow(hs, rowIndex++);
		// hc = ra.getCell(hr, 0);
		//
		// ra.setCellValue(hc, "合计", infStyle);
		// ra.setCellValue(hs, hr, 1, "--", infStyle);
		// ra.setCellValue(hs, hr, 2, "--", infStyle);
		// String[] columnLetters = {"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
		// for (int i = 0; i < titles.length - 2; i++) {
		// hc = ra.getCell(hr, i + 2);
		// hc.setCellFormula("SUM(" + columnLetters[i] + "2:" + columnLetters[i] + (rowIndex - 1) + ")");
		// hc.setCellStyle(infStyle);
		// /*
		// * if(columnLetters[i].length()>1){
		// *
		// * }else{ hc = ra.getCell(hr, i+2); hc.setCellValue("---"); hc.setCellStyle(infStyle); }
		// */
		//
		// }

		// 自动调整列宽
		for (int i = 0; i < titles.length; i++) {
			hs.autoSizeColumn(i);
		}

		String filename = ra.encodeFilename(title, request) + ".xls";// 处理中文文件名
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		OutputStream ouputStream = response.getOutputStream();
		hw.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
}
