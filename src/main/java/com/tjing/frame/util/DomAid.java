package com.tjing.frame.util;

import java.io.IOException;
import java.io.StringWriter;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DomAid {
	public static String formatXml(Element element, String charset,boolean istrans) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(charset);
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);
		xw.setEscapeText(istrans);
		try {
			xw.write(element);
			xw.flush();
			xw.close();
		} catch (IOException e) {
			System.out.println("格式化XML文档发生异常，请检查！");
			e.printStackTrace();
		}
		return sw.toString();
	}
}
