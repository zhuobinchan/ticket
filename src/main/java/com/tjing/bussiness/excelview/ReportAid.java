package com.tjing.bussiness.excelview;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;


public class ReportAid {
	public HSSFCellStyle createCellStyle(HSSFWorkbook hw,Font font){
		HSSFCellStyle style = hw.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setFont(font);
		return style;
	}
	public HSSFCellStyle createTitleCellStyle(HSSFWorkbook hw){
		Font titlefont = hw.createFont();
        titlefont.setFontHeight((short) 320);
		HSSFCellStyle style = hw.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setFont(titlefont);
		return style;
	}
	public HSSFCellStyle createCellStyle(HSSFWorkbook hw){
		HSSFCellStyle style = hw.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}
	public HSSFCellStyle createCellStyle(HSSFWorkbook hw,String pattern){
		HSSFCellStyle style = createCellStyle(hw);
        HSSFDataFormat fmt = hw.createDataFormat();
        style.setDataFormat(fmt.getFormat(pattern));
		return style;
	}
	public HSSFCellStyle createHeadCellStyle(HSSFWorkbook hw){
		HSSFCellStyle style = hw.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		Font font = hw.createFont();
	    font.setFontHeight((short) 200);
	    font.setFontName("方正书宋简体");
	    style.setFont(font);
		return style;
	}
	public void setCellStyle(HSSFCellStyle style,HSSFRow hr,int length){
		for(int i=0;i<length;i++){
			HSSFCell hc = getCell(hr, i);
			hc.setCellStyle(style);
		}
	}
	public void setCellStyle(HSSFCellStyle style,HSSFCell hc,int length){
		HSSFSheet hs = hc.getSheet();
		for(int i=0;i<length;i++){
			HSSFRow hr = getRow(hs, hc.getRowIndex());
			getCell(hr, hc.getColumnIndex()+i).setCellStyle(style);
		}
	}
	public void addMergedRegion(HSSFSheet hs,int top,int bottom,int left,int right,HSSFCellStyle style){
		 hs.addMergedRegion(new CellRangeAddress(top,bottom,left,right));
		 for(int i=top;i<=bottom;i++){
			 HSSFRow hr = getRow(hs, i);
			 for(int j=left;j<=right;j++){
				 HSSFCell hc = getCell(hr, j);
				 hc.setCellStyle(style);
			 }
		 }
	}
	public HSSFCell setCellValue(HSSFSheet hs,HSSFRow hr,int hcIndex,String value,HSSFCellStyle style){
		//value = Html2Text.html2Text(value);
		HSSFCell hc = hr.getCell(hcIndex);
		if(hc==null)
			hc = hr.createCell(hcIndex);
		if(NumberUtils.isNumber(value)){
			if(value.indexOf(".")==-1){
				try {
					hc.setCellValue(Integer.valueOf(value));
				} catch (NumberFormatException e) {
					hc.setCellValue(value	);
				}
				/*BigDecimal b = new BigDecimal(value);
				hc.setCellValue(b.toPlainString());*/
			}else{
				hc.setCellValue(Double.parseDouble(value));
			}
			
		}else{
			hc.setCellValue(value);
		}
		hc.setCellStyle(style);
		return hc;
	}
	public HSSFCell setCellValue(HSSFCell hc,String value,HSSFCellStyle style){
		if(StringUtils.isEmpty(value)){
			hc.setCellValue("");
		}else{
			try{
				hc.setCellValue(Integer.valueOf(value));
			}catch(NumberFormatException e){
				try{
					hc.setCellValue(Float.valueOf(value));
				}catch(NumberFormatException e2){
					hc.setCellValue(value);
				}catch(NullPointerException e2){
					hc.setCellValue("");
				}
			}catch(NullPointerException e){
				hc.setCellValue("");
			}
		}
		
		hc.setCellStyle(style);
		return hc;
	}
	public HSSFCell setCellValue(HSSFCell hc,int hcIndex,Date value,HSSFCellStyle style){
		hc.setCellValue(value);
		hc.setCellStyle(style);
		return hc;
	}
	public String getStringCellValue(Cell hc) {
		if (hc == null)
			return "";
		String strCell = "";
		switch (hc.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = hc.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(hc.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(hc.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}
	public String getStringCellValue(HSSFRow hr,int cellIdx) {
		HSSFCell hc = hr.getCell(cellIdx);
		if (hc == null)
			return "";
		String strCell = "";
		switch (hc.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = hc.getStringCellValue().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(hc)){
				java.util.Date d = hc.getDateCellValue();   
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
				strCell = sdf.format(d);
			}else{
				strCell = String.valueOf(hc.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(hc.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}
	public HSSFRow getRow(HSSFSheet hs,int rowIndex){
		HSSFRow hr = hs.getRow(rowIndex);
		if(hr==null){
			hr = hs.createRow(rowIndex);
		}
		return hr;
	}
	public HSSFCell getCell(HSSFRow hr,int cellIndex){
		HSSFCell hc = hr.getCell(cellIndex);
		if(hc==null){
			hc = hr.createCell(cellIndex);
		}
		return hc;
	}
	public HSSFCell getCell(HSSFRow hr,int cellIndex,HSSFCellStyle style){
		HSSFCell hc = hr.getCell(cellIndex);
		if(hc==null){
			hc = hr.createCell(cellIndex);
		}
		hc.setCellStyle(style);
		return hc;
	}
	/**  
	 * 获取合并单元格的值  
	 * @return  
	 */  
	public String getMergedRegionValue(HSSFSheet hs ,int rowIndex , int column){  
		if(!isMergedRegion(hs, rowIndex, column)){
			return this.getCellValue(this.getRow(hs,rowIndex).getCell(column));
		}
	    int sheetMergeCount = hs.getNumMergedRegions();   
	    for(int i = 0 ; i < sheetMergeCount ; i++){   
	        CellRangeAddress ca = hs.getMergedRegion(i);   
	        int firstColumn = ca.getFirstColumn();   
	        int lastColumn = ca.getLastColumn();   
	        int firstRow = ca.getFirstRow();   
	        int lastRow = ca.getLastRow();   
	        if(rowIndex >= firstRow && rowIndex <= lastRow){   
	            if(column >= firstColumn && column <= lastColumn){   
	            	HSSFRow fRow = hs.getRow(firstRow);   
	                HSSFCell fCell = fRow.getCell(firstColumn);   
	                return getCellValue(fCell);   
	            }   
	        }   
	    }   
	    return null ;   
	}   
	  
	/**  
	 * 判断指定的单元格是否是合并单元格  
	 * @param sheet  
	 * @param row  
	 * @param column  
	 * @return  
	 */  
	public boolean isMergedRegion(HSSFSheet sheet , int row , int column){   
	    int sheetMergeCount = sheet.getNumMergedRegions();   
	       
	    for(int i = 0 ; i < sheetMergeCount ; i++ ){   
	        CellRangeAddress ca = sheet.getMergedRegion(i);   
	        int firstColumn = ca.getFirstColumn();   
	        int lastColumn = ca.getLastColumn();   
	        int firstRow = ca.getFirstRow();   
	        int lastRow = ca.getLastRow();   
	           
	        if(row >= firstRow && row <= lastRow){   
	            if(column >= firstColumn && column <= lastColumn){   
	                   
	                return true ;   
	            }   
	        }   
	    }   
	       
	    return false ;   
	}   
	/**  
	 * 获取单元格的值  
	 * @param cell  
	 * @return  
	 */  
	public String getCellValue(HSSFCell cell){   
	       
	    if(cell == null) return "";   
	       
	    if(cell.getCellType() == Cell.CELL_TYPE_STRING){   
	           
	        return this.getStringCellValue(cell);  
	           
	    }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){   
	           
	        return String.valueOf(cell.getBooleanCellValue());   
	           
	    }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){   
	           
	        return cell.getCellFormula() ;   
	           
	    }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){  
	    	 short format = cell.getCellStyle().getDataFormat();  
	    	   SimpleDateFormat sdf = null;  
	    	   if(format == 14 || format == 31 || format == 57 || format == 58){  
	    	        //日期  
	    	        sdf = new SimpleDateFormat("yyyy/MM/dd");  
	    	   }else if (format == 20 || format == 32) {  
	    	        //时间  
	    	        sdf = new SimpleDateFormat("HH:mm");  
	    	   }else if(format==22){
	    		   sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
	    	   }
	    	   double value = cell.getNumericCellValue();  
	    	   Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);  
	    	   if(date!=null&&sdf!=null){
	    	    	
	    	    	String result = sdf.format(date);  
	 	    	    return result;
	    	   }
	    	   double v = cell.getNumericCellValue();
	    	   if(v==(int)v){
	    		   int v2 = (int)v;
	    		   return String.valueOf(v2); 
	    	   }
	    	   return String.valueOf(v);   
	    }   
	    return "";   
	}  
	 public static float getExcelCellAutoHeight(String str, float fontCountInline) {
	        float defaultRowHeight = 15.00f;//每一行的高度指定
	        float defaultCount = 0.00f;
	        for (int i = 0; i < str.length(); i++) {
	            float ff = getregex(str.substring(i, i + 1));
	            defaultCount = defaultCount + ff;
	        }
	        float ret = ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;
	        return ret;//计算
	    }

	    public static float getregex(String charStr) {
	        if(charStr==" ")
	        {
	            return 0.5f;
	        }
	        // 判断是否为字母或字符
	        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
	            return 0.5f;
	        }
	        // 判断是否为全角

	        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        //全角符号 及中文
	        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
	            return 1.00f;
	        }
	        return 0.5f;

	    }

	    /**  
	     * 设置下载文件中文件的名称  
	     *   
	     * @param filename  
	     * @param request  
	     * @return  
	     */    
	public String encodeFilename(String filename, HttpServletRequest request) {
		try {
			filename = URLEncoder.encode(filename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return filename;
	}
}
