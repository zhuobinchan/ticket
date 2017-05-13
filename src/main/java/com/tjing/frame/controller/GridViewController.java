package com.tjing.frame.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.annotation.FieldDesc;
import com.tjing.frame.model.Dic;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.object.GridHtmlData;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageInfo;
import com.tjing.frame.object.PageResultBean;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.services.GridFormatterUtils;
import com.tjing.frame.services.UiServices;
import com.tjing.frame.services.DataCache;
import com.tjing.frame.util.BeanAid;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DomAid;
/**
 * 创建zGrid
 * @author 曾平
 *
 */
@Controller
@RequestMapping(value="/public/frame")
public class GridViewController{
	@Autowired
	private DataCache dataCache;
	@Autowired
	private UiServices uiServices;
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/gridview")
	public ResponseEntity<GridHtmlData> createGrid(GridSetting gridSetting,HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException{
		gridSetting.setFieldsStr(gridSetting.getFieldsStr().replace("$xuanze$", "select"));
		gridSetting.setQueryParams(gridSetting.getQueryParams().replace("$huozhe$", "or:"));
		JSONArray fieldArr = JSON.parseArray(gridSetting.getFieldsStr());//字段信息
		Document doc = DocumentHelper.createDocument(); 
		Element root = doc.addElement("grid");
		String serviceString = gridSetting.getService();
		//实体名称
		String modelName = gridSetting.getModelName();
		//获取service的类名
		String serviceClassName = StringUtils.substringBefore(serviceString, "!");
		//获取service的方法名
		String serviceMethodName = StringUtils.substringAfter(serviceString, "!");
		Object services = BeanAid.getBean(serviceClassName);
		PageInfo pageInfo = gridSetting.getPageInfo();
		
		PageResultBean rb = null;
		try {
			rb = (PageResultBean) MethodUtils.invokeMethod(services, serviceMethodName, gridSetting);
		} catch (NoSuchMethodException e) {
			rb = (PageResultBean) MethodUtils.invokeMethod(services, serviceMethodName, new Object[]{gridSetting,request});
		}
		List list = rb.getList();
		long totalRecordNum = rb.getTotalRecordNum();
		pageInfo.setTotalRecords(totalRecordNum);
		if(totalRecordNum==0){
			pageInfo.setTotalPageNum(1);
		}else{
			long totalPageNum = totalRecordNum%Long.parseLong(pageInfo.getRecords()+"");//取余
			long tempTotalPageNum = totalRecordNum/Long.parseLong(pageInfo.getRecords()+"");//取整
			totalPageNum = totalPageNum>0?tempTotalPageNum+1:tempTotalPageNum;
			pageInfo.setTotalPageNum(totalPageNum);
		}
		Map<String, FieldInfo> fieldInfoMap = CodeHelper.getFieldInfoMap(modelName);
		String tableStr = "";
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Object obj = list.get(i);
				JSONObject valueJsonObj = JSON.parseObject(JSON.toJSONString(obj));
				Element row = root.addElement("tr");
				row.addAttribute("height", "28");
				row.addAttribute("recordId", valueJsonObj.getString("id"));
				//row.addAttribute("height", "30");
				row.addAttribute("id", gridSetting.getId()+"_"+valueJsonObj.getString("id"));
				Element cell = row.addElement("td");
				cell.addAttribute("align", "center");
				cell.addAttribute("name", "_ordernum");
				cell.addText(i+1+"");
				if(!gridSetting.isHideCheckbox()){
					cell = row.addElement("td");
					cell.addAttribute("align", "center");
					cell.setText("<input type='checkbox' name='_rowbox'/>");
				}
				
				JSONObject hideJson = new JSONObject();
				for(int j=0;j<fieldArr.size();j++){
					JSONObject fieldJsonObj = fieldArr.getJSONObject(j);
					String propName = fieldJsonObj.getString("name");
					Boolean editable = fieldJsonObj.getBooleanValue("editable");
					FieldInfo fieldInfo = fieldInfoMap.get(propName);
					String value = valueJsonObj.getString(propName);
					value = value==null?"":value;
					if(fieldJsonObj.getBooleanValue("hide")){
						hideJson.put(propName, value);
						
					}else{
						String type = fieldJsonObj.getString("type");
						String requiredStr = fieldJsonObj.getString("required");
						boolean required = requiredStr==null?false:Boolean.parseBoolean(requiredStr);
						type = type==null?"text":type;
						cell = row.addElement("td");
						if(fieldJsonObj.getString("blur")!=null){
							cell.addAttribute("blur",fieldJsonObj.getString("blur"));
						}
						//在客户端定义数据字典
						JSONObject dicJsonObject = fieldJsonObj.getJSONObject("dicMap");
						int length = fieldJsonObj.getIntValue("length");
						int size = fieldJsonObj.getIntValue("size");
						if(type.equals("select")){
							String dic = "";
							if(propName.indexOf(".")!=-1){
								String[] methodParts = StringUtils.split(propName,".");
								String getMethod = "get"+StringUtils.capitalize(methodParts[0]);
								Object fieldObject = MethodUtils.invokeMethod(obj, getMethod);
								Field kfield = fieldObject.getClass().getDeclaredField(methodParts[1]);
								FieldDesc fi = kfield.getAnnotation(FieldDesc.class);
								dic = fi.dic();
								getMethod = "get"+StringUtils.capitalize(methodParts[1]);
								Object vo = MethodUtils.invokeMethod(fieldObject, getMethod);
								if(vo!=null){
									value = vo.toString();
								}
							}else{
								dic = CodeHelper.getDicByClassProp(modelName, propName);
							}
							String text = "";
							if(!value.equals("")){
								Map<String,Dic> dicGroup = dataCache.getDicsFromCache();
								if(dicGroup==null){
									JSONArray dicArr = uiServices.queryEntityDicValue(dic, value);
									if(dicArr==null){
										text = "";
									}else
										text = dicArr.getString(1);
								}else{
									cell.addAttribute("dic", dic);
									if(dic.indexOf("from")!=-1){
										JSONArray dicArr = uiServices.queryEntityDicValue(dic, value);
										if(dicArr!=null)
											text = dicArr.getString(1);
									}else{
										Map<String, Dic> dicMap = dicGroup.get(dic).getDicMap();
										Dic dicValue = dicMap.get(value);
										if(dicValue!=null)
											text = dicValue.getText()==null?"":dicValue.getText();
									}
								}
								
							}
							if(length>0&&text.length()>length){
								text = StringUtils.substring(text, 0,length)+"..";
							}
							cell.addText(text);
						}else if(dicJsonObject!=null){//在页面定义的select
							type = "select";
							cell.addAttribute("dic",dicJsonObject.getString("group"));
							if(StringUtils.isNotEmpty(value)){
								cell.addText(dicJsonObject.getString(value));
							}else{
								if(dicJsonObject.getString("defaultValue")!=null){
									cell.addText(dicJsonObject.getString("defaultValue"));
								}
							}
						}else if(type.equals("date")){
							cell.addText(value);
						}else if(type.equals("popText")||type.equals("pop")){   
							//type = "popText";
							String dic = CodeHelper.getDicByClassProp(modelName, propName);
							JSONArray dicArr = uiServices.queryEntityDicValue(dic, value);
							if(dicArr==null){
								cell.addText("");
							}else
								cell.addText(dicArr.getString(1));
						}else{
							//解析带.的引用，比如equip.capacity
							value = CodeHelper.getOgnlValue(obj, propName, value);
							if(fieldJsonObj.containsKey("formatter")){
								GridFormatterUtils gridFormatterUtils = BeanAid.getBean("gridFormatterUtils");
								String fmt = fieldJsonObj.getString("formatter");
								String v = (String) MethodUtils.invokeMethod(gridFormatterUtils, fmt, valueJsonObj.getInteger("id"));
								cell.addText(v);
							}else{
								if(value!=null){
									cell.addAttribute("title",value);
									if(length>0&&value.length()>length){
										value = value.substring(0,length)+"..";
									}else if(size>0&&value.length()>size){
										value = value.substring(0,size);
									}
								}
								cell.addText(value);
							}
						}
						if(StringUtils.isNotEmpty(fieldJsonObj.getString("align"))){
							cell.addAttribute("align", fieldJsonObj.getString("align"));
						}
						cell.addAttribute("defaultvalue", fieldJsonObj.getString("defaultValue"));
						cell.addAttribute("style", "word-break:break-all;");
						if(fieldInfo!=null){
							if(fieldInfo.getLength()==0&&(fieldInfo.getType().equals("Integer")||fieldInfo.getType().equals("int"))){
								cell.addAttribute("length", "4");
							}else{
								cell.addAttribute("length", fieldInfo.getLength()+"");
							}
						}
						cell.addAttribute("name", propName);
						cell.addAttribute("type", type);
						if(required){
							cell.addAttribute("required", "true");
						}
						//cell.addAttribute("title", fieldJsonObj.getString("descs"));
						if(editable){
							cell.addAttribute("dbvalue", value);
						}
						cell.addAttribute("value", value);
					}
				}
				row.addAttribute("hidevalues",hideJson.toJSONString());
			}
			tableStr = DomAid.formatXml(root, "UTF-8", false).trim();
			tableStr = StringUtils.removeStart(StringUtils.removeEnd(tableStr,"</grid>"),"<grid/>");
			tableStr = StringUtils.removeStart(tableStr, "<grid>");
			
		}else{
			int cellNum = 2;
			for(int i=0;i<fieldArr.size();i++){
				JSONObject jobj = fieldArr.getJSONObject(i);
				if(!jobj.containsKey("hide")||!jobj.getBooleanValue("hide")){
					cellNum++;
				}
			}
			tableStr = "<tr><td align='center' style='color:red;height:100px' colspan='"+cellNum+"'>暂无数据</td></tr>";
		}

		GridHtmlData gridHtmlData = new GridHtmlData(tableStr);
		gridHtmlData.setPageInfo(pageInfo);
		ResponseEntity<GridHtmlData> responseEntity = new ResponseEntity<GridHtmlData>(gridHtmlData,HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 查找指定字段的数据字典
	 */
	@RequestMapping(value="/searchFieldDic")
	public ResponseEntity<String> searchFieldDic(@RequestParam String modelName,String propName){
		String dic = CodeHelper.getDicByClassProp(modelName, propName);
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(dic,HttpStatus.OK);
		return responseEntity;
	}
}
