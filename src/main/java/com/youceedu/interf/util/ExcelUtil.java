package com.youceedu.interf.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	//创建全局变量
	private String fileName=null;
	private Workbook wb =null;
	
	public ExcelUtil(String fileName){
		this.fileName = fileName;
	}
	//读取Excel
	//判断Excel格式
	public Workbook getwork(){
		try {
			InputStream inputstream = new FileInputStream(fileName);
			if (fileName.endsWith(".xlsx")) {
				wb = new XSSFWorkbook(inputstream);
			}else {
				wb = new HSSFWorkbook(inputstream);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wb;
	}
	
	//指定某个sheet
	public Sheet getsheet(int sheetNum){
		wb = getwork();
		Sheet sheet = wb.getSheetAt(sheetNum);
		return sheet;
		
	}
	
	//指定某个row
	public Object getcellvalues(int sheetNum,int rowNum,int cellNum){
		Object result = null;
		Row row = getsheet(sheetNum).getRow(rowNum);
		Cell cell = row.getCell(cellNum);
		result = getfromcell(cell);
		
		return result;
		
	}
	
	//判断cell格式
	public Object getfromcell(Cell cell){
		Object values = null;
		if (cell.getCellType()==cell.CELL_TYPE_BLANK) {
			values = "";
		}else if (cell.getCellType()==cell.CELL_TYPE_STRING) {
			values = cell.getStringCellValue().trim();
		}else if (cell.getCellType()==cell.CELL_TYPE_NUMERIC) {
			values = cell.getNumericCellValue();
		}else if (cell.getCellType()==cell.CELL_TYPE_BOOLEAN) {
			values = cell.getBooleanCellValue();
		}else if (cell.getCellType()==cell.CELL_TYPE_FORMULA) {
			values = cell.getCellFormula();
		}else {
			values = cell.getDateCellValue();
		}
		return values;
		
	}
	
	//使用数组进行存储行和列的值
	public Object[][] getArrayCell(int sheetNum){
		
		Object[][] testcase = null;
		int lastRowNum = getsheet(sheetNum).getLastRowNum();
		testcase = new Object[lastRowNum][10];
		
		for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
			Row row = getsheet(sheetNum).getRow(rowIndex);
			if (row==null) {
				continue;
			}
			for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
				Cell cell = row.getCell(cellIndex);
				if (cell==null) {
					testcase[rowIndex-1][cellIndex]="";
				}else {
					testcase[rowIndex-1][cellIndex]=getfromcell(cell);
				}
			}
		}
		return testcase;
		
	}
	
	public static void main(String[] args) {
		 ExcelUtil excelUtil = new ExcelUtil("D:\\Learing_Documents\\app_testcase.xlsx");
		 Object[][] objects = excelUtil.getArrayCell(0);
		 System.out.println(objects[1][1]);
	}

}
