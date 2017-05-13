package com.tjing.frame.util;

import java.util.List;

/**
 * 工具类
 */
public class ToolHelper {
	/**
	 * 计算报表最后合计，放在最后一行
	 * 
	 * @param data
	 *            报表数据
	 * @param total
	 *            合计字段
	 * @return 数据+合计
	 */
	public static List<Object[]> calculateReportTotal(List<Object[]> data, Object[] total) {
		for (int i = 1; i < data.size(); i++) {
			for (int k = 0; k < total.length; k++) {
				if (!(total[k] instanceof String)) {
					try {
						total[k] = Float.valueOf(String.valueOf(total[k]))
								+ Float.valueOf(String.valueOf(data.get(i)[k]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		data.add(total);
		return data;
	}

	/**
	 * 格式化字段名，把HQL格式的字段名改为sql的字段名，如memberName改为member_name
	 * 
	 * @param name
	 *            待格式化的HQL字段名
	 * @return
	 */
	public static String formatFieldName(String name) {
		String result = "";
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isLowerCase(c)) {
				result += c;
			} else {
				result += "_" + Character.toLowerCase(c);
			}
		}
		return result;
	}
}
