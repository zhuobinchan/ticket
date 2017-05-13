package com.tjing.bussiness.excelview;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
import org.dom4j.Element;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;

public class ExcelViewModel extends AbstractExcelView {
	protected DbServices dbServices = null;
	protected ReportAid ra = new ReportAid();
	protected HSSFCellStyle style = null;
	protected HSSFCellStyle styleDouble = null;
	protected int rowIndex = 0;
	@SuppressWarnings("rawtypes")
	protected void parseLList(String dt,List<Element> nodes, HSSFSheet hs, List list)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		HSSFRow hr = null;
		ClassInfo classInfo = CodeHelper.getClassInfo(dt);
        Map<String, FieldInfo> fieldsMap = classInfo.getFileds();
        for(Object jd : list){
        	hr = ra.getRow(hs, rowIndex++);
        	
        	for(int j=0;j<nodes.size();j++){
        		Element node = nodes.get(j);
        		String name = node.attributeValue("name");
        		parseRowData(hr, fieldsMap, jd, j, name);
        	}
        }
        
	}
	@SuppressWarnings("rawtypes")
	protected void parseLList(String dt,String[] props, HSSFSheet hs, int rowIndex, List list)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		HSSFRow hr = null;
		ClassInfo classInfo = CodeHelper.getClassInfo(dt);
		Map<String, FieldInfo> fieldsMap = classInfo.getFileds();
		for(Object jd : list){
			hr = ra.getRow(hs, rowIndex++);
			for(int j=0;j<props.length;j++){
				String name = props[j];
				parseRowData(hr, fieldsMap, jd, j, name);
			}
		}
	}
	protected void parseLList(HSSFSheet hs, int rowIndex, List<Object[]> list)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		HSSFRow hr = null;
		for(Object[] objs : list){
			hr = ra.getRow(hs, rowIndex++);
			parseRowData(hs, hr, objs);
		}
	}
	public void parseRowData(HSSFRow hr, Map<String, FieldInfo> fieldsMap,
			Object jd, int j, String name) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		HSSFCell hc;
		Map<Integer, Object> paramMap;
		FieldInfo fi = fieldsMap.get(name);
		JSONObject jobj = JSON.parseObject(JSON.toJSONString(jd));
		String value = jobj.getString(name);
		hc = ra.getCell(hr, j);
		if(StringUtils.isEmpty(fi.getDic())){
			if(fi.getType()!=null&&fi.getType().equals("Integer")){
				ra.setCellValue(hc, value, style);
			}else{
				ra.setCellValue(hc, value, styleDouble);
			}
			
		}else{
			if(fi.getDic().indexOf(" from")==-1){
				ra.setCellValue(hc, dbServices.getDicTextByCode(value), style);
			}else{
				paramMap = new HashMap<Integer,Object>();
				paramMap.put(0, value);
				Object[] objs = (Object[]) dbServices.findUniqueBySql(fi.getDic(), paramMap);
				if(objs!=null&&objs.length>1)
					ra.setCellValue(hc, objs[1].toString(), style);
				else{
					ra.setCellValue(hc, "", style);
				}
			}
		}
	}
	public void parseRowData(HSSFSheet hs,HSSFRow hr, Object[] objs) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		for(int i=0;i<objs.length;i++){
			ra.setCellValue(hs, hr, i, objs[i] == null ? "" : objs[i].toString(), style);
		}
	}
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
