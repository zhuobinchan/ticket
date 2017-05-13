package com.tjing.frame.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

/**
   * JAVA版本的自动生成有规则的订单号(或编号)
   * 生成的格式是: 200908010001 前面几位为当前的日期,后面五位为系统自增长类型的编号
   * 原理: 
   *      1.获取当前日期格式化值;
   *      2.读取文件,上次编号的值+1最为当前此次编号的值
   *      (新的一天会重新从1开始编号)
   *      
   *      存储自动编号值的文件如下(文件名: EveryDaySerialNumber.dat)
   */

public class OrderCodeGen2 {
    
    public static void main(String[] args) throws InterruptedException {
        SerialNumber2 serial = new FileEveryDaySerialNumber2(6, "EveryDaySerialNumber2.dat");
       // while(true) {
            System.out.println(genCode("4",5));
           // TimeUnit.SECONDS.sleep(2);
       // }
    }
    public static String genCode(String prefix,int length){
    	SerialNumber2 serial = new FileEveryDaySerialNumber2(length, "EveryDaySerialNumber2.dat");
    	String s = serial.getSerialNumber(prefix);
    	return s;
    }
}

abstract  class SerialNumber2 {

    public synchronized String getSerialNumber(String pccode) {
        return process(pccode);
    }
    protected abstract String process(String pccode);
}


abstract class EveryDaySerialNumber2 extends SerialNumber2 {
    
    protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    protected final static String pattern = "yyMMdd";
    protected DecimalFormat df = null;
    
    public EveryDaySerialNumber2(int width) {
        if(width < 1) {
            throw new IllegalArgumentException("Parameter length must be great than 1!");
        }
        char[] chs = new char[width];
        for(int i = 0; i < width; i++) {
            chs[i] = '0';
        }
        df = new DecimalFormat(new String(chs));
    }
    
    protected String process(String pccode) {
        DateTime todayDt = new DateTime();
        int n = getOrUpdateNumber(todayDt, 1);
        return todayDt.toString("yyMMdd")+pccode+format(n);
    }
    
    protected String format(Date date) {
        return sdf.format(date);
    }
    protected String format(int num) {
        return df.format(num);
    }
    
    /**
     * 获得序列号，同时更新持久化存储中的序列
     * @param current 当前的日期
     * @param start   初始化的序号
     * @return 所获得新的序列号
     */
    protected abstract int getOrUpdateNumber(DateTime current, int start);
}


class FileEveryDaySerialNumber2 extends EveryDaySerialNumber2 {

    /**
     * 持久化存储的文件
     */    
    private File file = null;
    
    /**
     * 存储的分隔符
     */
    private final static String FIELD_SEPARATOR = ",";   

    public  FileEveryDaySerialNumber2(int width, String filename) {
        super(width);
        file = new File(filename);
    }

    @Override
    protected int getOrUpdateNumber(DateTime current, int start) {
        String date = current.toString(pattern);
        int num = start;
        if(file.exists()) {
            List<String> list = FileUtil.readList(file);        
            String[] data = list.get(0).split(FIELD_SEPARATOR);
            if(date.equals(data[0])) {
                num = Integer.parseInt(data[1]);
            }
        }
        FileUtil.rewrite(file, date + FIELD_SEPARATOR + (num + 1));
        return num;
    }        
}


class FileUtil2 {

    public static void rewrite(File file, String data) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {        
            if(bw != null) {
               try { 
                   bw.close();
               } catch(IOException e) {
                   e.printStackTrace();
               }
            }            
        }
    }
    
    public static List<String> readList(File file) {
        BufferedReader br = null;
        List<String> data = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(file));
            for(String str = null; (str = br.readLine()) != null; ) {
                data.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
               try { 
                   br.close();
               } catch(IOException e) {
                   e.printStackTrace();
               }
            }
        }
        return data;
    }
}



 


