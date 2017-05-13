package com.tjing.bussiness.excelview;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
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

import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.ToolHelper;

/**
 * 充值卡（包括会员卡、消费卡）消费记录Excel文件生成类
 *
 */
@Component
public class MemberCardConsumerExcelView extends ExcelViewModel {
	@Autowired
	private DbServices services;

	@SuppressWarnings({"unchecked", "static-access"})
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook hw, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		int k = 0;
		Enumeration<String> names = request.getParameterNames();
		String where = "";
		String title = "";
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			name = ToolHelper.formatFieldName(name);
			if ("title".equals(name)) {
				title = value;
			} else if ("create_time".equals(name)) {
				String[] values = value.split(";");
				if (values.length == 2) {
					where += " and DATE_FORMAT(v.create_time, '%Y-%m-%d') between ? and ? ";
					map.put(k++, values[0]);
					map.put(k++, values[1]);
				}
			} else {
				where += " and v.";
				if ("code".equals(name) || "member_name".equals(name) || "member_mobile".equals(name)) {
					where += name + " like ? ";
					value = "%" + value + "%";
				} else {
					where += name + "=? ";
				}
				map.put(k++, value);
			}
		}
		String sql = "SELECT v.sale_no, v.CODE, v.member_name, v.member_mobile, DATE_FORMAT(v.create_time, '%Y-%m-%d'), v.money, "
				+ " v.balance, b.text branch, s.text status, u.name user_name "
				+ " FROM bi_member_card_consumer_view v "
				+ " LEFT JOIN (select * from tj_dic where code like '0226%') b on v.branch = b.code "
				+ " LEFT JOIN (select * from tj_dic where code like '0202%') s on v.status = s.code, tj_user u "
				+ " where v.create_user_id = u.id " + where + " order by create_time desc";
		List<Object[]> list = services.findListBySql(sql, map);
		Object[] titles = new Object[]{"销售编号", "卡号", "会员姓名", "会员手机", "消费日期", "本次消费", "本次余额", "消费场所", "状态", "操作员"};

		ReportAid ra = new ReportAid();
		HSSFSheet hs = hw.createSheet("充值数据");
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
		parseLList(hs, rowIndex, list);
		HSSFCellStyle styleRate = ra.createCellStyle(hw);
		styleRate.setDataFormat(hw.createDataFormat().getFormat("0.0%"));
		styleDouble = ra.createCellStyle(hw);
		styleDouble.setDataFormat(hw.createDataFormat().getFormat("0.0"));
		HSSFCellStyle infDouble = ra.createCellStyle(hw);
		infDouble.setDataFormat(hw.createDataFormat().getFormat("0.00"));
		infDouble.setFillForegroundColor((short) 22);
		infDouble.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		rowIndex += list.size();

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
