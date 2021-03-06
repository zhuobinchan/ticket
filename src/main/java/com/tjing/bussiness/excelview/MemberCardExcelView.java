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
 * 会员卡、消费卡、充值卡Excel文件生成类
 *
 */
@Component
public class MemberCardExcelView extends ExcelViewModel {
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
			} else if ("point".equals(name)) {
				String[] values = value.split(";");
				if (values.length == 2) {
					where += " and v.point between ? and ? ";
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
		String sql = "SELECT v.CODE, DATE_FORMAT(v.create_time, '%Y-%m-%d'), v.member_name, v.member_mobile, g.text member_gender, "
				+ " n.text card_name, s.text card_status, v.balance, v.point, r.text pay_type, u.name user_name, m.name salesman_name "
				+ " FROM bi_member_card_view v LEFT JOIN bi_salesman m on v.salesman_id = m.id "
				+ " LEFT JOIN (select * from tj_dic where code like '0102%') g on v.member_gender = g.code "
				+ " LEFT JOIN (select * from tj_dic where code like '0207%') r on v.last_rechargege_way = r.code, "
				+ " (select * from tj_dic where code like '0215%') n, "
				+ " (select * from tj_dic where code like '0218%') s, tj_user u "
				+ " where v.name = n.code and v.status = s.code and v.create_user_id = u.id " + where
				+ " order by update_time desc, code";
		List<Object[]> list = services.findListBySql(sql, map);
		Object[] titles = new Object[]{"卡号", "发卡日期", "会员姓名", "会员手机", "会员性别", "卡名称", "卡状态", "卡余额", "卡积分", "最后充值方式",
				"操作员", "营销员"};

		ReportAid ra = new ReportAid();
		HSSFSheet hs = hw.createSheet("卡数据");
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
