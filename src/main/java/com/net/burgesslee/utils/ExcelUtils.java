package com.net.burgesslee.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.net.burgesslee.service.impl.Xls2OracleServiceImpl;

/**
 * Excel处理工具类
 * 
 * @author Burgess
 */
@SuppressWarnings("unused")
public class ExcelUtils {

    /**
     * 读取excel数据
     * @param path
     */
    public static ArrayList<Map<String,String>> readExcelToObj(String path) {

        Workbook wb = null;
        ArrayList<Map<String,String>> result = null;
        try {
            wb = WorkbookFactory.create(new File(path));
            result = readExcel(wb, 0, 1, 0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取excel文件
     * @param wb
     * @param sheetIndex sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine 去除最后读取的行
     */
    private static ArrayList<Map<String,String>> readExcel(Workbook wb,int sheetIndex, int startReadLine, int tailLine) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = null;
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        for(int i=startReadLine; i<sheet.getLastRowNum()-tailLine+1; i++) {
            row = sheet.getRow(i);
            Map<String,String> map = new HashMap<String,String>();
            if(row == null){
            	continue;
            }
            for(Cell c : row) {
                String returnStr = "";

                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                //判断是否具有合并单元格
                if(isMerge) {
                	if(0 == c.getCellType()) {//判断单元格的类型是否则NUMERIC类型
                    	if(HSSFDateUtil.isCellDateFormatted(c)){//日期类型的处理
                    		Date date = c.getDateCellValue();
                    		DateFormat formater = new SimpleDateFormat("yyyyMMdd");
                    		returnStr = formater.format(date);
                    		Xls2OracleServiceImpl.reportTime = returnStr;
                    		System.out.println("reportTime=" + returnStr);
                    	}
                	}
                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    returnStr = rs;
                }else if(0 == c.getCellType()) {//判断单元格的类型是否则NUMERIC类型
                	if(HSSFDateUtil.isCellDateFormatted(c)){//日期类型的处理
                		Date date = c.getDateCellValue();
                		DateFormat formater = new SimpleDateFormat("yyyyMMdd");
                		returnStr = formater.format(date);
                		Xls2OracleServiceImpl.reportTime = returnStr;
                		System.out.println("reportTime=" + returnStr);
                	}
                }else {
//                    System.out.print(c.getRichStringCellValue()+"++++ ");
                	c.setCellType(Cell.CELL_TYPE_STRING);
                    returnStr = c.getRichStringCellValue().getString();
                }
                
                if(returnStr.contains("(") || returnStr.contains(")")){
                	returnStr.replaceAll("[(]", "[（]").replaceAll("[)]", "[）]");
                }else if(returnStr == null || "".equals(returnStr)){
                	returnStr = "0";
                }else if(returnStr.contains("\n")){
//                	returnStr = returnStr.replaceAll("\n", "\\\\n");
                	returnStr = returnStr.replaceAll("\n", "_");
                }
                /*
                if(!isInteger(returnStr)){//不是整数
                	continue;
                }
                */
                if(c.getColumnIndex()==0){
                    map.put("单位",returnStr);
                }else if(c.getColumnIndex()==1){
                    map.put("作业项目",returnStr);
                }else if(c.getColumnIndex()==2){
                    map.put("白班",returnStr);
                }else if(c.getColumnIndex()==3){
                    map.put("夜班",returnStr);
                }else if(c.getColumnIndex()==4){
                    map.put("合计",returnStr);
                }else if(c.getColumnIndex() == 5){
                	map.put("生产量",returnStr);
                }else if(c.getColumnIndex()==6){
                    map.put("同比",returnStr);
                }else if(c.getColumnIndex()==7){
                    map.put("环比",returnStr);
                }else if(c.getColumnIndex()==8){
                    map.put("单位2",returnStr);
                }else if(c.getColumnIndex()==9){
                    map.put("作业项目2",returnStr);
                }else if(c.getColumnIndex() == 11){
                	map.put("白班2",returnStr);
                }else if(c.getColumnIndex() == 12){
                	map.put("夜班2",returnStr);
                }else if(c.getColumnIndex() == 13){
                	map.put("合计2",returnStr);
                }else if(c.getColumnIndex() == 14){
                	map.put("生产量2",returnStr);
                }else if(c.getColumnIndex() == 15){
                	map.put("同比2",returnStr);
                }else if(c.getColumnIndex() == 16){
                	map.put("环比2",returnStr);
                }
            }
            result.add(map);
        }
        return result;

    }
    
    /**
     * 方法二：推荐，速度最快
     * 判断是否为整数 
     * @param str 传入的字符串 
     * @return 是整数返回true,否则返回false 
     */
     public static boolean isInteger(String str) {  
           Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
           return pattern.matcher(str).matches();  
     }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     * @return
     */
	private static boolean isMergedRow(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row == firstRow && row == lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     * @return
     */
    private static boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == Cell.CELL_TYPE_STRING){

            return cell.getStringCellValue();

        }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }
    /**
     * 从excel读取内容
     */
    public static void readContent(String fileName)  {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx"))
            isE2007 = true;
        try {
            InputStream input = new FileInputStream(fileName);  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                System.out.println("Row #" + row.getRowNum());  //获得行号从0开始
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.println("Cell #" + cell.getColumnIndex());
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(cell.getNumericCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            System.out.println(cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            System.out.println(cell.getCellFormula());
                            break;
                        default:
                            System.out.println("unsuported sell type======="+cell.getCellType());
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}