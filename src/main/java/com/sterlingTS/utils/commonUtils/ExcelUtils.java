package com.sterlingTS.utils.commonUtils;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonUtils.database.DataBaseManager;
import com.sterlingTS.utils.commonVariables.Globals;



/*HSSF (Horrible Spreadsheet Format) : It is used to read and write xls format of MS-Excel files.
XSSF (XML Spreadsheet Format) : It is used for xlsx file format of MS-Excel.
HWPF (Horrible Word Processor Format) : It is used to read and write doc extension files of MS-Word.
XWPF (XML Word Processor Format) : It is used to read and write docx extension files of MS-Word.
*/	
/**
 * @author aunnikrishnan -  This class handles all excel(xlsx) related operations like reading/writing/modifying etc
 *
 */
public class ExcelUtils extends DriverScriptUtils{
	
	protected static Logger log = Logger.getLogger(ExcelUtils.class);
	
	private XSSFSheet ExcelWSheet=null;
	private XSSFWorkbook ExcelWBook =null;
	private XSSFCell Cell = null;
	private XSSFRow Row= null;
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;
	public CellStyle bg_Green = null;
	public CellStyle bg_Red =null;
	public CellStyle bg_Orange = null;
	public Font xl_Font = null;
	
	public ExcelUtils(String path) {
		
		this.path=path;
		try {
			ZipSecureFile.setMinInflateRatio(0);
			fis = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(fis);
			ExcelWSheet = ExcelWBook.getSheetAt(0);
			loadCellStyleForExcel();
//			log.info("Excel file is set to path : - "+ path);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Class ExcelUtils | Method Constructor | Exception desc : "+e.toString());
			e.printStackTrace();
		} 
		
	}

	public void loadCellStyleForExcel() throws Exception {
		
		bg_Green = ExcelWBook.createCellStyle();
		bg_Green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		bg_Green.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
		bg_Green.setFillPattern(CellStyle.SOLID_FOREGROUND);
		bg_Green.setFillPattern(CellStyle.ALIGN_FILL);
		
		bg_Red = ExcelWBook.createCellStyle();
		bg_Red.setFillForegroundColor(IndexedColors.RED.getIndex());
		bg_Red.setFillBackgroundColor(IndexedColors.RED.getIndex());
		bg_Red.setFillPattern(CellStyle.SOLID_FOREGROUND);
		bg_Red.setFillPattern(CellStyle.ALIGN_FILL);
		
		bg_Orange = ExcelWBook.createCellStyle();
		bg_Orange.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		bg_Orange.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		bg_Orange.setFillPattern(CellStyle.SOLID_FOREGROUND);
		bg_Orange.setFillPattern(CellStyle.ALIGN_FILL);
		
		xl_Font = ExcelWBook.createFont();
		xl_Font.setColor(IndexedColors.BLACK.getIndex());
		xl_Font.setBold(true);
        bg_Orange.setFont(xl_Font);
        bg_Red.setFont(xl_Font);
        bg_Green.setFont(xl_Font);
	}
	/**
	 * Method set the workbook file path
	 * @param Path - String path of the workbook
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 * @throws Exception
	 * @Deprecated
	 **/
	
	public void setExcelFile(String Path) throws Exception {
		try {
			FileInputStream ExcelFile = new FileInputStream(Path);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
		} catch (Exception e){
			log.error("Class ExcelUtils | Method setExcelFile | Exception desc : "+e.toString());
			
		}
	}
	
	/**************************************************************************************************
	 * Method to set the excel work sheet
	 * @param SheetName - String value corresponding to SheetName
	 * @return  TRUE if successful/ FALSE , otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	***************************************************************************************************/
	public String setExcelSheet(String SheetName) {
		APP_LOGGER.startFunction("setExcelSheet");
		try{
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int index = ExcelWBook.getSheetIndex(SheetName);
			
			if(index==-1){
				log.error(SheetName + " Not found " );
				return Globals.KEYWORD_FAIL;
			}
			ExcelWSheet = ExcelWBook.getSheetAt(index);
//			log.info("Sheetname has been set- "+ SheetName);
			return Globals.KEYWORD_PASS;
		}catch (Exception e){
			log.error("Exception occurred at setExcelSheet.Exception"  + e.toString()) ;
			return Globals.KEYWORD_FAIL;
		}
	}
	
	/**************************************************************************************************
	 * Method to fetch the effective row count.
	 * @param SheetName - String value corresponding to SheetName
	 * @return rowCount of sheet .Row count is obtained by accessing rows in first column till we encounter null or getLastRowNum, whichever is minimum
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/

	public int getRowCount(String SheetName){
		return getRowCount(SheetName,0);

	}
	
	
	public int getRowCount(String SheetName,int startRowOffset){
		APP_LOGGER.startFunction("getRowCount"); 
		int rowCount=Globals.INT_FAIL;
		int effectiveRowCount=Globals.INT_FAIL;
		String cellValue=null;
		int i=0;
		try {
			String retval=setExcelSheet(SheetName);
			if (retval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				return Globals.INT_FAIL;
			}
			rowCount=ExcelWSheet.getLastRowNum()+1;
			for (i = startRowOffset ;i<=rowCount;i++){
				Row=ExcelWSheet.getRow(i);
				try{
					cellValue = Row.getCell(0).getStringCellValue().trim();
					if(cellValue == null || cellValue.isEmpty()){
						break;
					}
				}catch(NullPointerException e){ // this exception is required as null values can exists in multiple ways.
					break;
				}
				catch(Exception e){
					log.error(" Check the format of test data being used - Exception generated-  "+ e.toString() + " in file "+this.path);
					break;
				}
				
			}
			
			effectiveRowCount = i;
		
			
		} catch (Exception e){
			log.error("Class ExcelUtils | Method getRowCount | Exception desc : "+e.toString());
			return Globals.INT_FAIL;
			
		}
//		log.debug(" Row Count obtained for | " + SheetName + "| is " + effectiveRowCount);
		return effectiveRowCount;
	}
	
	/**************************************************************************************************
	 * Method to fetch column Index of 'Column' present in sheet 'SheetName'.
	 * @param SheetName - String -Sheet name where column would be searched
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/

	public int getColumnIndex(String SheetName,String ColumnName){
		APP_LOGGER.startFunction("get_Column_Index");
		return getColumnIndex(SheetName,ColumnName,0);
	}
	
	
	public int getColumnIndex(String SheetName,String ColumnName,int headerRowNo){
		APP_LOGGER.startFunction("get_Column_Index");
		
		String retval;
		int col_Num=-1; // default value 
		try{
			retval= setExcelSheet(SheetName);
			
			if(retval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				log.error("Unable to set the SheetName" );
				return Globals.INT_FAIL;
			}
			
			Row=ExcelWSheet.getRow(headerRowNo);
			for(int i=0;i<Row.getLastCellNum();i++){
				if(Row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(ColumnName.trim())){
					col_Num = i;
					break;
				}
			}
			if(col_Num == -1 ){
				log.error(SheetName + " | " + ColumnName + "| Col Index - Not Found" );
				return Globals.INT_FAIL;
			}
			else {
//				log.debug(SheetName + " | " + ColumnName + "| Col Index - " + col_Num );
				return col_Num ; 

			}	
		}
		catch (Exception e) { 
			log.error("Exception Occurred whiile fetching data from excel");
			return Globals.INT_FAIL;
		}
	}
	
	/**************************************************************************************************
	 * Method to obtain column index of a 'Column' in Test Data sheet.
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getColumnIndex_fromTestData(String ColumnName){
		APP_LOGGER.startFunction("getColumnIndex_TestData");
		String TestDataSheetName = Globals.TestData;
		return getColumnIndex(TestDataSheetName,ColumnName);
		
		
	}
	
	/**************************************************************************************************
	 * Method to obtain column index of a 'Column' in MainSheet sheet.
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getColumnIndex_fromMainSheet(String ColumnName){
		APP_LOGGER.startFunction("getColumnIndex_MainSheet");
		String TestDataSheetName = Globals.MainSheet;
		return getColumnIndex(TestDataSheetName,ColumnName);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain column index of a 'Column' in BuisnessFlow sheet.
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getColumnIndex_fromBuisnessFlow(String ColumnName){
		APP_LOGGER.startFunction("getColumnIndex_BuisnessFlow");
		String TestDataSheetName = Globals.BusinessFlow;
		return getColumnIndex(TestDataSheetName,ColumnName);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain column index of a 'Column' in SceanrioManager sheet.
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getColumnIndex_fromScenarioManager(String ColumnName){
		APP_LOGGER.startFunction("getColumnIndex_ScenarioManager");
		String TestDataSheetName =Globals.GlobalTestData;		
		return getColumnIndex(TestDataSheetName,ColumnName,Globals.ScenarioRowOffset-1);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain column index of a 'Column' in GlobalTestData sheet.
	 * @param ColumnName - Column whose index needs to be found
	 * @return Integer value of Column Index is returned if Column is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getColumnIndex_fromGlobalTestData(String ColumnName){
		APP_LOGGER.startFunction("getColumnIndex_GlobalTestData");
		String TestDataSheetName =Globals.GlobalTestData;
		return getColumnIndex(TestDataSheetName,ColumnName,Globals.GlobalDetailsRowOffset-1);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain row index of a 'RowCellvalue' present in column 'ReferenceColumnName' in 'Sheet'.
	 * @param SheetName - String- The sheet in which row index needs to be found
	 * @param ReferenceColumnName - String - ColumnName where 'RowCellValue' would be searched'
	 * @param RowCellvalue - String - Cell value of the row whose index needs to be found
	 * @return Integer value of Row Index is returned if RowCellvalue is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getRowIndex(String SheetName,String ReferenceColumnName,String RowCellvalue){
		APP_LOGGER.startFunction("getRowIndex");
		
		String retval=setExcelSheet(SheetName);
		int columnIndex=getColumnIndex(SheetName,ReferenceColumnName);
		if(columnIndex == Globals.INT_FAIL || retval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
			return Globals.INT_FAIL;
		}else{
			int iRowNum=0;
			boolean rowCellValFound= false;
			try {
				
				int rowCount = getRowCount(SheetName);
				for (; iRowNum<rowCount; iRowNum++){
					if  (getCellData(iRowNum,columnIndex,SheetName).equalsIgnoreCase(RowCellvalue)){
						rowCellValFound = true;
						break;
					}
				}   
				if (rowCellValFound == true){
					return iRowNum;
				}else{
					return Globals.INT_FAIL;
				}
				 
			} catch (Exception e){
				log.error("Class ExcelUtils | Method getRowIndex | Exception desc : "+e.toString());
				return Globals.INT_FAIL;
			}
		}
		
	}
	
	/**************************************************************************************************
	 * Method to obtain row index of the current 'TestCaseID value' present in column 'TestCaseID' in 'TestDataSheet'.
	 * @return Integer value of Row Index is returned if RowCellvalue is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getRowIndex_fromTestData_usingTCID(){
		APP_LOGGER.startFunction("getRowIndex_TestData_TestCaseID");
		String SheetName=Globals.TestData;
		String ReferenceColumnName=Globals.TestCaseIDColName;
		String TestCaseIDValue= Globals.TestCaseID;
		int testCaseIDRowIndex=getRowIndex(SheetName, ReferenceColumnName,TestCaseIDValue);
		return testCaseIDRowIndex;
	}
	
	/**************************************************************************************************
	 * Method to obtain row index of the current 'TestCaseID value' present in column 'TestCaseID' in 'MainSheet'.
	 * @return Integer value of Row Index is returned if RowCellvalue is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getRowIndex_fromMainSheet_usingTCID(){
		APP_LOGGER.startFunction("getRowIndex_TestData_TestCaseID");
		String SheetName=Globals.MainSheet;
		String ReferenceColumnName=Globals.TestCaseIDColName;
		String TestCaseIDValue= Globals.TestCaseID;
		int testCaseIDRowIndex=getRowIndex(SheetName, ReferenceColumnName,TestCaseIDValue);
		return testCaseIDRowIndex;
	}
	
	/**************************************************************************************************
	 * Method to obtain row index of the current 'FlowID value' present in column 'FlowID' in 'BusinessFlow sheet'.
	 * @return Integer value of Row Index is returned if RowCellvalue is found else Globals.INT_FAIL is returned
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public int getRowIndex_fromBuisnessFlow_usingFlowID(){
		APP_LOGGER.startFunction("getRowIndex_BuisnessFlow_FlowID");
		String SheetName=Globals.BusinessFlow;
		String ReferenceColumnName=Globals.FlowIDColName;
		String TestCaseIDValue= Globals.FlowID;
		int testCaseIDRowIndex=getRowIndex(SheetName, ReferenceColumnName,TestCaseIDValue);
		return testCaseIDRowIndex;
	}
	
	
	/**************************************************************************************************
	 * Method to obtain cell data of based  'RowCellvalue' present in column 'ReferenceColumnName'  from 'ColumnName' in  'Sheet'.
	 * @param SheetName - String- The sheet in which cell data needs to be obtained
	 * @param ReferenceColumnName - String - ColumnName where 'RowCellValue' would be searched'
	 * @param ColumnName - String - Column from where cell data needs to be obtained
	 * @param RowCellvalue - String - Cell value of the row whose index needs to be found
	 * @return String value of cell else returns Globals.Keyword_Fail
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getCellData(String SheetName,String RowReferenceColName,String RowCellValue,String ColumnName ){
		APP_LOGGER.startFunction("getCellData");
		
		int rowIndex = getRowIndex(SheetName,RowReferenceColName,RowCellValue);
		int columnIndex =getColumnIndex(SheetName,ColumnName);
		String retval=setExcelSheet(SheetName);
		
		if (rowIndex != Globals.INT_FAIL && columnIndex != Globals.INT_FAIL && retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
			try {
				String cellValue=getCellData(rowIndex, columnIndex, SheetName);
				if (cellValue.equals(Globals.KEYWORD_FAIL))
					return Globals.KEYWORD_FAIL;
				else{
					return cellValue;
				}
					
			} catch (Exception e) {
				log.error("Unable to obtain cell data from | " + SheetName + " | Row "+rowIndex + "| ColumnName "+ColumnName );
				e.printStackTrace();
				return Globals.KEYWORD_FAIL;
				
			}
			
		} else
			return Globals.KEYWORD_FAIL;
	}
	
	

	
	/**************************************************************************************************
	 * Method to obtain test data of based    from 'ColumnName' in  'TestData Sheet'.
	 * @param ColumnName - String - Column from where cell data needs to be obtained
	 * @return String value of cell else returns Globals.Keyword_Fail
	 * @author aunnikrishnan
	 * @throws Exception 
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getCellData_fromTestData(String ColumnName ) {
		APP_LOGGER.startFunction("getCellData_TestData");
		String retval = Globals.KEYWORD_FAIL;
		String colVal = Globals.TestDataCollection.get(ColumnName);
		if(colVal == null || colVal.isEmpty() || colVal.equals("") ){
			retval = "";
		}else{
			if(colVal.length() > 4 && colVal.substring(0, 4).equals("out_")){
				try{
					retval = getOutputValue(colVal);
				}catch(Exception e){
					retval ="";
				}
			}else{
				retval = colVal;
			}
			
		}
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to obtain cell data of based    from 'ColumnName' in  'MainSheet'.
	 * @param ColumnName - String - Column from where cell data needs to be obtained
	 * @return String value of cell else returns Globals.Keyword_Fail
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getCellData_fromMainSheet(String ColumnName ){
		APP_LOGGER.startFunction("getCellData_MainSheet");
		String SheetName = Globals.MainSheet;
		String RowReferenceColName = Globals.TestCaseIDColName;
		String RowCellValue = Globals.TestCaseID;
		return getCellData(SheetName, RowReferenceColName, RowCellValue, ColumnName);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain cell data of based    from 'ColumnName' in  'BusinessFlow sheet'.
	 * @param ColumnName - String - Column from where cell data needs to be obtained
	 * @return String value of cell else returns Globals.Keyword_Fail
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getCellData_fromBuisnessFlow(String ColumnName ){
		APP_LOGGER.startFunction("getCellData_BuisnessFlow");
		String SheetName = Globals.BusinessFlow;
		String RowReferenceColName = Globals.FlowIDColName;
		String RowCellValue = Globals.FlowID;
		return getCellData(SheetName, RowReferenceColName, RowCellValue, ColumnName);
		
	}
	
	/**************************************************************************************************
	 * Method to obtain cell data of based    from 'Column Index' and 'RowIndex' in  'Sheet'.
	 * @param RowNum - Int- index of the row
	 * @param ColNum - Int- index of the column
	 * @param SheetName - String- Name of the sheet
	 * @return String value of cell else returns Globals.Keyword_Fail
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getCellData(int RowNum, int ColNum, String SheetName ) throws Exception{
		APP_LOGGER.startFunction("getCellData");
		String CellData = "";
		try{
			setExcelSheet(SheetName);
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			if (Cell == null){
				CellData ="";
			}else{
				if(Cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
					CellData = Double.toString(Cell.getNumericCellValue());
				}else{
					CellData = Cell.getStringCellValue().toString();	
				}
			}
			
			
			
//			log.debug("SheetName - "+SheetName+ "| Row - "+RowNum + " |Col- "+ ColNum + " | Value - " + CellData );
			return CellData;
						
		}catch (Exception e){
			log.error("SheetName - "+SheetName+ "| Row - "+RowNum + " |Col- "+ ColNum +"| Exception desc : "+e.toString());
			return Globals.KEYWORD_FAIL;
			
		}
	}


	/**************************************************************************************************
	 * Method to obtain all executable keywords for the current flow id
	 * @return List<String> containing all the kewywords for the current flow id 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public List<String> fetchAllBuisnessFlowKeyword(){
		APP_LOGGER.startFunction("fetchAllBuisnessFlowKeyword");
		List<String> keywords = new ArrayList<>();
		int flowIDRowNumber;
		flowIDRowNumber=getRowIndex_fromBuisnessFlow_usingFlowID();
		
		String keyword2Execute ="";
		int lowerRange = 0;
		int upperRange = 0;
		
		int keyword2executeColIndex = getColumnIndex_fromBuisnessFlow("Keywords_To_Execute");
		XSSFCell keyword2executeCell =ExcelWSheet.getRow(flowIDRowNumber).getCell(keyword2executeColIndex,org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
		
		if (keyword2executeCell == null) {
			keyword2Execute = "All";
		}else{
			if(keyword2executeCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
				keyword2Execute = NumberToTextConverter.toText(keyword2executeCell.getNumericCellValue()) ;
//						Integer.parseInt(keyword2executeCell.getNumericCellValue()..trim();
			}else{
				keyword2Execute = keyword2executeCell.getStringCellValue().toString().trim();	
			}
			
		}
		
		
		int columnIter =  1;
		String columnPrefix = "Keyword_";
		String columnName= columnPrefix + columnIter;
		int colIndex = getColumnIndex_fromBuisnessFlow(columnName);
		String CellValue = "";
		
		if(keyword2Execute.contains("-")){ // case where range of keyword needs to be excuted
			lowerRange = Integer.parseInt(keyword2Execute.split("-")[0]);
			upperRange = Integer.parseInt(keyword2Execute.split("-")[1]);
		}else if(keyword2Execute.equalsIgnoreCase("All")){ // case where all keywords must be executed
			lowerRange =1;
			
			CellValue=ExcelWSheet.getRow(flowIDRowNumber).getCell(colIndex,org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL).getStringCellValue();
			while (CellValue != null && !CellValue.isEmpty()){
				
				columnIter++;
				columnName= columnPrefix + columnIter;
				colIndex =	getColumnIndex_fromBuisnessFlow(columnName);
				Cell = ExcelWSheet.getRow(flowIDRowNumber).getCell(colIndex);
				if (Cell == null)
					break;
				else
					CellValue = (String)Cell.getStringCellValue();
			}
			upperRange = columnIter-1 ; // subtracting 1 as columnIter is already incremented for the blank cell value
		}else if(keyword2Execute.equalsIgnoreCase("0")){ //case where no keyword should be executed
			lowerRange =0;
			upperRange =0;
			
		}else{//case where Keyword X needs to be excuted
			lowerRange = Integer.valueOf(keyword2Execute);
			upperRange = lowerRange ;
		}
//		System.out.println(lowerRange + " ---" + upperRange);
		

		columnIter =  lowerRange;
		columnName= columnPrefix + columnIter;
		colIndex = getColumnIndex_fromBuisnessFlow(columnName);
		CellValue="";
		try {
			
			if (lowerRange != 0 && upperRange !=0 ){
				CellValue=ExcelWSheet.getRow(flowIDRowNumber).getCell(colIndex,org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL).getStringCellValue();
				while (lowerRange <= upperRange && CellValue != null && !CellValue.isEmpty() ){
					keywords.add(CellValue);
//					log.debug("Keyword stored in List | "+ CellValue);

					lowerRange++;
					columnName= columnPrefix + lowerRange;
					colIndex =	getColumnIndex_fromBuisnessFlow(columnName);
					Cell = ExcelWSheet.getRow(flowIDRowNumber).getCell(colIndex);
					if (Cell == null)
						break;
					else
						CellValue = (String)Cell.getStringCellValue();
				}
				
//				log.debug("Total of "+ keywords.size() + " kewywords have been fetched " );
				
			}else{
				log.error("No kewywords have been selected for Execution " );
			}
		}catch(NullPointerException e){
				
			log.error("Total of "+ keywords.size() + " kewywords have been fetched " );
			
		}catch(Exception e){
			log.error("Exception occured during keyword extraction ");
		}
		
			return keywords;
	}
	

	/**************************************************************************************************
	 * Method to update all the test case 'ExecuteFlage' as No in the MainSheet
	 * @return String Globals.KEYWORD_PASS if successfull else Globals.KEYWORD_FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String updateAllExecuteFlag_inMainSheet_asNo(){
		APP_LOGGER.startFunction("updateAllTestCaseExecuteFlag_AsNo");
		
		
		String retval=Globals.KEYWORD_FAIL;
		String executeFlagValue ="";
		try{
			int ExecuteFlagColIndex=getColumnIndex_fromMainSheet("ExecuteFlag");
			if (ExecuteFlagColIndex !=Globals.INT_FAIL){
				int rowCount=getRowCount(Globals.MainSheet);
				for(int i = 1;i<rowCount;i++){
					Cell = ExcelWSheet.getRow(i).getCell(ExecuteFlagColIndex);
					if (Cell == null) {
						Cell = Row.createCell(ExecuteFlagColIndex);
					}
					executeFlagValue=Cell.getStringCellValue();
									
					if (executeFlagValue.equalsIgnoreCase("Yes")){
						Cell.setCellValue("No");
					}
				}
			}
			fileOut = new FileOutputStream(Globals.RunConfigPath);
			ExcelWBook.write(fileOut);
			//fileOut.flush();
			fileOut.close();
//			log.info("updateAllExecuteFlag_inMainSheet_asNo - All Test Case's ExecuteFlag has been Update with Status=No");
			retval = Globals.KEYWORD_PASS;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("updateAllExecuteFlag_inMainSheet_asNo | Exception occurred - " + e.toString());
			e.printStackTrace();
		}
		return retval;
	}
	

	


	/**************************************************************************************************
	 * Method to fetch the execution setting value from GlobaslTestData for the specified settingName
	 * @param settingName - Name of the setting in GlobalTestData sheet
	 * @return String execution setting value 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getExecutionSettingValue(String settingName) {
		APP_LOGGER.startFunction("getExecutionSettingValue");
			
		int iFieldNameCol=getColumnIndex_fromGlobalTestData(settingName);
		String retval =  Globals.KEYWORD_FAIL;
		
		String SheetName = Globals.GlobalTestData;
			
		try {
			
			retval = getCellData(Globals.GlobalDetailsRowOffset,iFieldNameCol,SheetName);
			     			
		} catch (Exception e){
			log.error("Class ExcelUtils | Method getExecutionSettingValue | Exception desc : "+e.toString());
		
		}
		return retval;
	}


	/**************************************************************************************************
	 * Method to fetch the application setting value from GlobaslTestData for the specified settingName
	 * @param settingName - Name of the setting in GlobalTestData sheet
	 * @return String application setting value 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String getAppSettings(String settingName){
		APP_LOGGER.startFunction("getAppSettings");
		int iRowNum=0;	
		int iEnvCol=getColumnIndex_fromGlobalTestData(Globals.Env_To_Execute_ON);
		int iFieldNameCol=getColumnIndex_fromGlobalTestData(Globals.Field_Name);
		int iTypeCol = getColumnIndex_fromGlobalTestData(Globals.Type_Value);
		String SheetName = Globals.GlobalTestData;
		String fieldValue="";
		
		try {
			
			int rowCount = getRowCount(SheetName);
			for (; iRowNum<rowCount; iRowNum++){
				if  (getCellData(iRowNum,iTypeCol,SheetName).equalsIgnoreCase("Info") && getCellData(iRowNum,iFieldNameCol,SheetName).equalsIgnoreCase(settingName)){
					fieldValue = getCellData(iRowNum,iEnvCol,SheetName);
					break;
				}
			}       			
		} catch (Exception e){
			log.error("Class ExcelUtils | Method getExecutionSettingValue | Exception desc : "+e.toString());
			fieldValue ="";
		}
		return fieldValue;
		
		
	}
	/**************************************************************************************************
	 * Method to set a value in an excel sheet
	 * @param SheetName - String- Sheet where the value needs to be set
	 * @param RowNum - int-row id 
	 * @param ColNum - int -column id
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successfull else Globals.KEYWORD_FAIL, otherwise 
	 * @throws Exception
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String setCellData(String SheetName,  int RowNum, int ColNum,String cellValueToBeSet) throws Exception    {
		APP_LOGGER.startFunction("setCellData");   
		try{
			String retval=setExcelSheet(SheetName);
			if (retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
				Row  = ExcelWSheet.getRow(RowNum);
		
				Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
				
				           
				if (Cell == null) {
					Cell = Row.createCell(ColNum);
					Cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				}

				
				Cell.setCellValue(cellValueToBeSet);
								
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				//fileOut.flush();
				fileOut.close();
			}else {
				log.error("Unable to set excel sheet name - "+SheetName );
				return Globals.KEYWORD_FAIL;
			}
			
		}catch(Exception e){
			log.error("setCellData | Exception occurred - " + e.toString());
			return Globals.KEYWORD_FAIL;

		}
//		log.debug("Column Value updated with " + cellValueToBeSet +"| Sheet -" + SheetName + "| RowID - " + RowNum + "| ColID - " + ColNum);
		return Globals.KEYWORD_PASS;
	}
	
	/**************************************************************************************************
	 * Method to set a value in an excel sheet
	 * @param SheetName - String- Sheet where the value needs to be set
	 * @param RowNum - int-row id 
	 * @param ColNum - int -column id
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successfull else Globals.KEYWORD_FAIL, otherwise 
	 * @throws Exception
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String setCellData(String SheetName,  int RowNum, int ColNum,String cellValueToBeSet, CellStyle style) throws Exception    {
		APP_LOGGER.startFunction("setCellData");   
		try{
			String retval=setExcelSheet(SheetName);
			if (retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
				Row  = ExcelWSheet.getRow(RowNum);
				Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
	            
				if (Cell == null) {
					Cell = Row.createCell(ColNum);
					Cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
					Cell.setCellValue(cellValueToBeSet);
					Cell.setCellStyle(style);
				} else {
					Cell.setCellValue(cellValueToBeSet);
 					Cell.setCellStyle(style);
				}
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				//fileOut.flush();
				fileOut.close();
			}else {
				log.error("Unable to set excel sheet name - "+SheetName );
				return Globals.KEYWORD_FAIL;
			}
			
		}catch(Exception e){
			log.error("setCellData | Exception occurred - " + e.toString());
			return Globals.KEYWORD_FAIL;

		}
//		log.debug("Column Value updated with " + cellValueToBeSet +"| Sheet -" + SheetName + "| RowID - " + RowNum + "| ColID - " + ColNum);
		return Globals.KEYWORD_PASS;
	}
	
	/**************************************************************************************************
	 * Method to set a value in an excel sheet
	 * @param SheetName - String- Sheet where the value needs to be set
	 * @param ReferenceColumnName - String-COlumnName where RowCellValue needs to be searched
	 * @param RowCellvalue - String- Value of the cell to obtain the row id
	 * @param ColumnName - Column where the value needs to be set
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successfull else Globals.KEYWORD_FAIL, otherwise 
	 * @throws Exception
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String setCellData(String SheetName,  String ReferenceColumnName,String RowCellvalue, String ColumnName,String cellValueToBeSet ) throws Exception {
		APP_LOGGER.startFunction("setCellData"); 
		
		int RowNum = getRowIndex(SheetName, ReferenceColumnName, RowCellvalue);
		int ColNum = getColumnIndex(SheetName, ColumnName);
		if (RowNum != Globals.INT_FAIL && ColNum != Globals.INT_FAIL){
			return setCellData(SheetName,RowNum,ColNum,cellValueToBeSet );
		}else {
			return Globals.KEYWORD_FAIL;
		}
		
	}
	
	/**************************************************************************************************
	 * Method to set a value in an test data sheet
	 * @param ColumnName - Column where the value needs to be set
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successful else Globals.KEYWORD_FAIL, otherwise 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String setCellData_inTestData(String ColumnName, String cellValueToBeSet){
		String SheetName = Globals.TestData;
		int RowNum = Globals.TestDataRowID ;
		int ColNum = getColumnIndex_fromTestData(ColumnName);
		String CellVal = "";
		String retval = Globals.KEYWORD_FAIL;
		
		if (RowNum != Globals.INT_FAIL && ColNum != Globals.INT_FAIL) {
			try {
				
				CellVal = getCellData(RowNum, ColNum, SheetName);
				if(CellVal != null && !CellVal.isEmpty() && CellVal.length() > 4 && CellVal.substring(0, 4).equals("out_")){
					retval =  setOutputValue(CellVal, cellValueToBeSet);
				}else{
					Globals.TestDataCollection.put(ColumnName, cellValueToBeSet);
					
					CellStyle style = ExcelWBook.createCellStyle();
				    style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
				    style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
				    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				    style.setFillPattern(CellStyle.ALIGN_FILL);
				    Font font = ExcelWBook.createFont();
		            font.setColor(IndexedColors.BLACK.getIndex());
		            font.setBold(true);
		            style.setFont(font);
		            retval = setCellData(SheetName,RowNum,ColNum,cellValueToBeSet,style );
				}
				
	            
				return retval ;
				
			} catch (Exception e) {
				
				log.error("setCellData_inTestData | Exception occurred - " + e.toString());
				e.printStackTrace();
				return Globals.KEYWORD_FAIL;
						
			}
		}
		return Globals.KEYWORD_FAIL;
	}
	
	/**************************************************************************************************
	 * Method to set a value in an main sheet sheet
	 * @param ColumnName - Column where the value needs to be set
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successful else Globals.KEYWORD_FAIL, otherwise 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String setCellData_inMainSheet(String ColumnName, String cellValueToBeSet){
		String SheetName = Globals.MainSheet;
		int RowNum = getRowIndex_fromMainSheet_usingTCID();
		int ColNum = getColumnIndex_fromMainSheet(ColumnName);
		if (RowNum != Globals.INT_FAIL && ColNum != Globals.INT_FAIL) {
			try {
				return setCellData(SheetName,RowNum,ColNum,cellValueToBeSet );
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("setCellData_inMainSheet | Exception occurred - " + e.toString());
				e.printStackTrace();
				return Globals.KEYWORD_FAIL;
						
			}
		}
		return Globals.KEYWORD_FAIL;
	}
	
	/**************************************************************************************************
	 * Method to set a value in an main sheet sheet
	 * @param ColumnNum - int-column id 
	 * @param cellValueToBeSet - String- Value to be set
	 * @return String Globals.KEYWORD_PASS if successful else Globals.KEYWORD_FAIL, otherwise 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	
	public String setCellData_inMainSheet(int ColumnNum, String cellValueToBeSet){
		
		String SheetName = Globals.MainSheet;
		int RowNum = getRowIndex_fromMainSheet_usingTCID();
		if (RowNum != Globals.INT_FAIL && ColumnNum >= 0) {
			try {
				
				return setCellData(SheetName,RowNum,ColumnNum,cellValueToBeSet );
				
			} catch (Exception e) {
				log.error("setCellData_inMainSheet | Exception occurred - " + e.toString());
				e.printStackTrace();
				return Globals.KEYWORD_FAIL;
						
			}
		}
		return Globals.KEYWORD_FAIL;
	}
	
	/**************************************************************************************************
	 * Method to fetch all the test data for the current testcase id and update the same in global hashmap
	 * @return String Globals.KEYWORD_PASS if successful else Globals.KEYWORD_FAIL, otherwise 
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public String fetchAllTestData_forTCID(){
		int TCIDRowID=getRowIndex_fromTestData_usingTCID();
		Globals.TestDataRowID = TCIDRowID;
		int rowCount =  getRowCount(Globals.TestData);
		int noOfCols= ExcelWSheet.getRow(0).getLastCellNum();
		
		/*Option1-->getPhysicalNumberOfCells() and Option 2-->getLastCellNum() both can be used to find out the last column for a row.However,
		Option 1 gives the no of columns which are actually filled with contents(If the 2nd column of 10 columns is not filled you will get 9)
		Option 2 just gives you the index of last column. Hence done 'getLastCellNum()'
		*/
		try{
			if (TCIDRowID != Globals.INT_FAIL && rowCount != Globals.INT_FAIL){
				String columnValue ="";
				String columnName="";
				XSSFCell cell = null;
				XSSFComment comment=null;;
				
				for (int i = 1; i <noOfCols ;i++){
					try{
//						String columnValue = ExcelWSheet.getRow(TCIDRowID).getCell(i).getStringCellValue();
						cell = ExcelWSheet.getRow(TCIDRowID).getCell(i);
						columnName = ExcelWSheet.getRow(0).getCell(i).getStringCellValue();
						
						if (columnName.equalsIgnoreCase("SearchReqFulfillment")){ // comments columns name-SearchReqFulfillment
														
							comment = cell.getCellComment();
							if(comment !=null){
								columnValue = comment.getString().toString();
								columnValue	=columnValue.replace(comment.getAuthor()+":","").trim();
								columnValue = columnValue.replace("\n","");
							}else{
								columnValue="";
							}
							
						}else if (cell == null) {
							columnValue ="";
						}
						else{
							if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
								columnValue = NumberToTextConverter.toText(cell.getNumericCellValue()) ;
							}else{
								columnValue = cell.getStringCellValue().toString();	
							}
							
						}
						
						Globals.TestDataCollection.put(columnName, columnValue);
					}
					catch(NullPointerException e){
						log.error(" Unable to fetch Test Data for TCID -"+ Globals.TestCaseID+" | Row " + TCIDRowID + "| ColumnName "+columnName );
						continue;
					}
				}
		
			}else{
				return Globals.KEYWORD_FAIL;
			}
			//the below line can be uncommented during debugging
			//GenUtils.displayAllItem_fromHashMap(Globals.TestDataCollection);
		}catch (Exception e ){
			log.error("Exception "+ e.toString() + " has occured while fetching all test data");
			e.printStackTrace();
			return Globals.KEYWORD_FAIL;
		}
		return Globals.KEYWORD_PASS;
	}
	
	//This will create new excel and makes the sheet active to refer
		public ExcelUtils(String newExcelPath,String sheetNameToRefer) {
			
			this.path=newExcelPath;
			try {
				ExcelWBook = new XSSFWorkbook();
				ExcelWSheet = ExcelWBook.createSheet(sheetNameToRefer);
			
				loadCellStyleForExcel();
				
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
				
//				log.info("Your excel file has been generated:-"+path );
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method ExcelUtils(with 2 args) | Exception desc : "+e.getMessage());
			}
			
			
		}
		
		public void addHeaders(String[] headers){		
			try {			
				
				
				XSSFColor AquaColor = new XSSFColor(new Color(49, 132, 155));
				XSSFCellStyle style = ExcelWBook.createCellStyle();
			    style.setFillForegroundColor(AquaColor);
			    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    Font font = ExcelWBook.createFont();
	            font.setColor(IndexedColors.WHITE.getIndex());
	            font.setBold(true);
			    style.setFont(font);
			    
							
				Row = ExcelWSheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell = Row.createCell(i);
					Cell.setCellValue(headers[i]);
					Cell.setCellStyle(style);
				}	
				ExcelWSheet.createFreezePane(0, 1);
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
//				log.info("Headers are been set for .."+path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method addHeaders | Exception desc : "+e.getMessage());
			}
			
		}
		
		
		public void closeExcel(){		
			try {			
				
				ExcelWBook.close();
				
//				log.info("Excel is being closed :- "+path);
				
				ExcelWBook =null;
				Cell = null;
				Row= null;
				ExcelWSheet=null;
				path=null;
				fis = null;
				fileOut =null;
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method closeExcel | Exception desc : "+e.getMessage() + path);
			}
			
		}
		
		
		
		//in excel
		public void tcResultSheet_addStepDetails(String stepName, String stepDescription,String stepResult,String imageName){		
			try {	
				int currRowNumber;
				currRowNumber = Globals.tcResultSheet_lastRowNumber;
				Globals.tcResultSheet_lastRowNumber = currRowNumber + 1;
				Row = ExcelWSheet.createRow(currRowNumber);
				
				
				Row.createCell(0).setCellValue(Globals.FlowID);
				Row.createCell(1).setCellValue(Globals.Component);
				Row.createCell(2).setCellValue(stepName);
				Row.createCell(3).setCellValue(stepDescription);
				
				//Step Result formatting 
				Cell =Row.createCell(4);				
				if (stepResult.equalsIgnoreCase("pass")) {
					Cell.setCellStyle(bg_Green);
					Cell.setCellValue("Pass");
				} else if (stepResult.equalsIgnoreCase("fail"))  {
					Cell.setCellStyle(bg_Red);	
					Cell.setCellValue("Fail");
				}	
				else if(stepResult.equalsIgnoreCase("warning"))  {
					Cell.setCellStyle(bg_Orange);
					Cell.setCellValue("Warning");
				}
				else{
					Cell.setCellValue(stepResult);	
				}
				
				
				Row.createCell(5).setCellValue(seleniumCommands.getCurrentTimeStamp());
				Row.createCell(6).setCellValue(imageName);
				
				
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
				
//				log.info("TC Result - step details added..");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method tcResultSheet_addStepDetails | Exception desc : "+e.getMessage());
			}
			
		}
		
		
		public void tcSummaryResultSheet_addTCDetails(){		
			try {
				
				
				int currRowNumber;
				currRowNumber = Globals.tcSummaryResultSheet_lastRowNumber;
				Globals.tcSummaryResultSheet_lastRowNumber= currRowNumber + 1;
				
				Row = ExcelWSheet.createRow(currRowNumber);
				
				
				Row.createCell(0).setCellValue(Globals.testScenario);
				Row.createCell(1).setCellValue(Globals.TestCaseID);
				Row.createCell(2).setCellValue(Globals.tcDescription);
				
				//Step Result formatting 
				Cell =Row.createCell(3);				
				if (Globals.tcExecutionStatus.equalsIgnoreCase("pass")) {
					Cell.setCellStyle(bg_Green);
					Cell.setCellValue("Pass");
				} else if (Globals.tcExecutionStatus.equalsIgnoreCase("fail"))  {
					Cell.setCellStyle(bg_Red);	
					Cell.setCellValue("Fail");
				}	
				else if(Globals.tcExecutionStatus.equalsIgnoreCase("warning"))  {
					Cell.setCellStyle(bg_Orange);
					Cell.setCellValue("Warning");
				}
				
				
				Row.createCell(4).setCellValue(Globals.tcExecution_Time);
				Row.createCell(5).setCellValue(Globals.tcManualEffort);
				
				//If tc status is failed then automation savings should be zero
				double tcAutomationSavings_dbl =0;
				if(Globals.tcExecutionStatus.equalsIgnoreCase("fail")){
					Globals.tcAutomationSavings = "0.00";
				}else{
					Globals.tcAutomationSavings = String.valueOf(Double.parseDouble(Globals.tcManualEffort)-Double.parseDouble(Globals.tcExecution_Time));
					
					//13 May 2016 - Convert savings to 2 decimal places 
					tcAutomationSavings_dbl = Double.parseDouble(Globals.tcAutomationSavings);	    	
			    	DecimalFormat roundEffort	= new DecimalFormat("#0.00");
			    	tcAutomationSavings_dbl = Double.parseDouble(roundEffort.format(tcAutomationSavings_dbl));
				}
				
		    	Row.createCell(6).setCellValue(tcAutomationSavings_dbl);
		    	Row.createCell(7).setCellValue(Globals.ManualTCCount);
		    	Row.createCell(8).setCellValue(Globals.TC_OUTPUT_VAL);
		    	
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
				
//				log.info("Summary Result - TC result details added..");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method tcSummaryResultSheet_addTCDetails | Exception desc : "+e.getMessage());
			}
			
		}

		
		public void setColumnWidth(int[] columnNumber,int[] columnWidth){		
			try {			
				for (int i = 0; i < columnNumber.length; i++) {					
					ExcelWSheet.setColumnWidth(columnNumber[i], columnWidth[i]);					
				}	
				ExcelWSheet.createFreezePane(0, 1);
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
//				log.info("Columns are been resized .."+path);
			} catch (Exception e) {
				log.error("Class ExcelUtils | Method setColumnWidth | Exception desc : "+e.getMessage());
			}
			
		}
		
		
		public void pfResultSheet_addCurrentPFDetails(String Platform,String TestSuite,String Scenario,String ExecuteTCs_ofStatus,String browser){		
			try {
				
				int currRowNumber;
				currRowNumber = Globals.pfResultSheet_lastRowNumber;
				
				Row = ExcelWSheet.createRow(currRowNumber);
				
				Row.createCell(0).setCellValue(Platform);
				Row.createCell(1).setCellValue(TestSuite);
				Row.createCell(2).setCellValue(Scenario);
				Row.createCell(3).setCellValue(ExecuteTCs_ofStatus);
				Row.createCell(4).setCellValue(browser);
				
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
				
//				log.info("Platform Result - Details for current PF in excecution is added..");
			} catch (Exception e) {
			
				log.error("Class ExcelUtils | Method addCurrentPFDetails_toPFResult | Exception desc : "+e.getMessage());
			}
			
		}
		
		public void pfResultSheet_addCurrentPFStatus(){		
			try {
				
				int currRowNumber;
				currRowNumber = Globals.pfResultSheet_lastRowNumber;
				Globals.pfResultSheet_lastRowNumber= currRowNumber + 1;
				
				
				Row = ExcelWSheet.getRow(currRowNumber);
				if(Globals.TEST_CASE_EXECUTED_COUNTER == 0){
					Globals.pfExecutionStatus ="NoRun";
				}
				
				//Step Result formatting 
				Cell =Row.createCell(5);				
				if (Globals.pfExecutionStatus.equalsIgnoreCase("pass")) {
					Cell.setCellStyle(bg_Green);
					Cell.setCellValue("Pass");
				} else if (Globals.pfExecutionStatus.equalsIgnoreCase("fail"))  {
					Cell.setCellStyle(bg_Red);	
					Cell.setCellValue("Fail");
				}	
				else if(Globals.pfExecutionStatus.equalsIgnoreCase("warning"))  {
					Cell.setCellStyle(bg_Orange);
					Cell.setCellValue("Warning");
				}else{
					Cell.setCellValue("NoRun");
				}
				
				fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.close();
				
//				log.info("Platform Result - Platform status added..");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Class ExcelUtils | Method addCurrentPFDetails_toPFResult | Exception desc : "+e.getMessage());
			}
			
		}
		
		//This function gets the data based on Row and column number only 
		public String getCellData(int RowNum, int ColNum) throws Exception{
			APP_LOGGER.startFunction("getCellData");
			String CellData = "";
			try{
				Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
//				The below code has been added to handle numeric values
				if(Cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
					CellData = Double.toString(Cell.getNumericCellValue());
				}else{
					CellData = Cell.getStringCellValue().toString();	
				}
//				CellData = Cell.getStringCellValue().toString();
//				log.debug("Row - "+RowNum + " |Col- "+ ColNum + " | Value - " + CellData );
				return CellData;
							
			}catch (Exception e){
				log.error("Row - "+RowNum + " |Col- "+ ColNum +"| Exception desc : "+e.toString());
				return Globals.KEYWORD_FAIL;				
			}
		}
		

		public int getRowCount(){
			APP_LOGGER.startFunction("getRowCount"); 
			int rowCount=Globals.INT_FAIL;
			int effectiveRowCount=Globals.INT_FAIL;
			String cellValue=null;
			int i=0;
			try {
				
				rowCount=ExcelWSheet.getLastRowNum()+1;
				for (i = 0 ;i<=rowCount;i++){
					Row=ExcelWSheet.getRow(i);
					try{
						cellValue = Row.getCell(0).getStringCellValue().trim();
						if(cellValue == null || cellValue.isEmpty()){
							break;
						}
					}catch(NullPointerException e){ // this exception is required as null values can exists in multiple ways.
						break;
					}
					
				}
				
				effectiveRowCount = i;
			
				
			} catch (Exception e){
				log.error("Class ExcelUtils | Method getRowCount | Exception desc : "+e.toString());
				return Globals.INT_FAIL;
				
			}
//			log.debug(" Row Count obtained is " + effectiveRowCount);
			return effectiveRowCount;
		}
		
	public void updateTCStatus_MainSheet(String sheetName,int statusColID,int lastRunFolderColID){
		try {
			
			int currRowNumber;
			currRowNumber = getRowIndex(sheetName, "TestCaseID", Globals.TestCaseID);
			String cellValue="";
		
			CellStyle style = null;

            
			if (Globals.tcExecutionStatus.equalsIgnoreCase("pass")) {
				style = bg_Green;
				cellValue = "PASS";
				
			} else if (Globals.tcExecutionStatus.equalsIgnoreCase("fail"))  {
				style = bg_Red;
				cellValue = "FAIL";
			}	
			else if(Globals.tcExecutionStatus.equalsIgnoreCase("warning"))  {
				style = bg_Orange;
				cellValue = "WARNING";
			}
			
			setCellData(sheetName,currRowNumber,statusColID,cellValue,style);
			setCellData(sheetName,currRowNumber,lastRunFolderColID,Globals.currentPlatform_Path);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Class ExcelUtils | Method updateTCStatus_MainSheet | Exception desc : "+e.getMessage());
		}
	}
	
	public void updateTCStatus_ScenarioManager(){
		try {
			String sheetName = Globals.ScenarioManager;
			int currRowNumber;
			currRowNumber = Globals.ScenarioManger_currentRow;
			String cellValue="";
			int colIndex = 7; //column id for Status Flag
			
			CellStyle style = ExcelWBook.createCellStyle();

			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillPattern(CellStyle.ALIGN_FILL);
			
			
			Font font = ExcelWBook.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBold(true);
            style.setFont(font);

            
			if (Globals.pfExecutionStatus.equalsIgnoreCase("pass")) {
				style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
				style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
				cellValue = "PASS";
				
			} else if (Globals.pfExecutionStatus.equalsIgnoreCase("fail"))  {
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
				style.setFillBackgroundColor(IndexedColors.RED.getIndex());
				
				cellValue = "FAIL";
			}	
			else if(Globals.pfExecutionStatus.equalsIgnoreCase("warning"))  {
				style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
				style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
				cellValue = "WARNING";
			}
			
			setCellData(sheetName,currRowNumber,colIndex,cellValue,style);
		} catch (Exception e) {
			log.error("Class ExcelUtils | Method updateTCStatus_MainSheet | Exception desc : "+e.getMessage());
		}
	}
	
	
	public String updateTestCaseExecuteFlag_inMainSheet_as_Yes(String testSuite, String testScenario, String testCaseStatus){
		APP_LOGGER.startFunction("updateTestCaseExecuteFlag_inMainSheet_as_Yes");
		String testSuiteCond ="";
		String testScenarioCond = "";
		String testCaseStatusCond = "";
		
		int ExecuteFlagColIndex=getColumnIndex_fromMainSheet("ExecuteFlag");
		int testSceanrioColIndex = getColumnIndex_fromMainSheet(Globals.TestCaseSceanrioColName);
		int testCaseStatusColIndex = getColumnIndex_fromMainSheet(Globals.StatusColName);
		int testSuiteColIndex = getColumnIndex_fromMainSheet(Globals.TestSuiteColName);
		
		
		
		String executeFlagXLValue ="";
		String testScenarioXLVal ="";
		String testStatusXLVal = "";
		String testSuiteXLVal = "";
		String retval = Globals.KEYWORD_FAIL;
		
		try{
			ExcelWSheet = ExcelWBook.getSheet(Globals.MainSheet);
			
			if (ExecuteFlagColIndex !=Globals.INT_FAIL){
				int rowCount=getRowCount(Globals.MainSheet);
				for(int i = 1;i<rowCount;i++){
					
					Row  = ExcelWSheet.getRow(i);
					Cell = Row.getCell(ExecuteFlagColIndex);
					executeFlagXLValue=Cell.getStringCellValue();
//					testCaseIDXLVal = Row.getCell(TCColIndex).getStringCellValue();
					testSuiteXLVal = ExcelWSheet.getRow(i).getCell(testSuiteColIndex).getStringCellValue();
					testScenarioXLVal = ExcelWSheet.getRow(i).getCell(testSceanrioColIndex).getStringCellValue();
					testStatusXLVal = ExcelWSheet.getRow(i).getCell(testCaseStatusColIndex).getStringCellValue();
					
					if (testSuite.equalsIgnoreCase("All")){
						testSuiteCond = testSuiteXLVal;
					}else{
						testSuiteCond = testSuite;
					}
					
					if(testScenario.equalsIgnoreCase("All")){
						testScenarioCond = testScenarioXLVal;
					}else{
						testScenarioCond = testScenario ;
					}
					
					if(testCaseStatus.contains("All")){
						testCaseStatusCond = testStatusXLVal;
					}else{
						testCaseStatusCond = testCaseStatus;
					}
					
					
					if(testSuiteXLVal.equalsIgnoreCase(testSuiteCond) &&
							testScenarioXLVal.equalsIgnoreCase(testScenarioCond) &&
							testStatusXLVal.equalsIgnoreCase(testCaseStatusCond)){
						
						if (executeFlagXLValue.equalsIgnoreCase("No")){
							Cell.setCellValue("Yes");
						}
					}
					
				}
			}
			fileOut = new FileOutputStream(Globals.RunConfigPath);
			ExcelWBook.write(fileOut);
			//fileOut.flush();
			fileOut.close();
			retval = Globals.KEYWORD_PASS;
		}catch(Exception e){
			e.printStackTrace();
		}
		return retval;
	}
	
	
	// Find column count By Smita
    public int getColCount(){
           APP_LOGGER.startFunction("getColCount"); 
           int colCount=Globals.INT_FAIL;
           int retval = Globals.INT_FAIL;
           String cellValue=null;
           int i=0;
           
           try {
                 Row=ExcelWSheet.getRow(i);
                 colCount = Row.getLastCellNum();
                 for (i = 0 ;i<= colCount;i++){
                        
                        try{
                               cellValue = Row.getCell(0).getStringCellValue().trim();
                               if(cellValue == null || cellValue.isEmpty()){
                                      break;
                               }
                        }catch(NullPointerException e){ // this exception is required as null values can exists in multiple ways.
                               break;
                        }
                        
                 }
                 
                 retval = i-1;
                 
           
                 
           } catch (Exception e){
                 log.error("Class ExcelUtils | Method getColCount | Exception desc : "+e.toString());
                 retval = Globals.INT_FAIL;
                 
           }
           return retval;
    }
 
    // Add Header to existing excel sheet at the end By Smita
    public void addHeaderswithRange(int startRange,String[] headers){          
       
    	try {                
     
              int headerIter=0;          
              int EndColVal=startRange+headers.length-1;
              Row  = ExcelWSheet.getRow(0);
              
              for (int i = startRange; i <= EndColVal; i++) {
                     Cell = Row.createCell(i);
                     Cell.setCellValue(headers[headerIter]);
                     headerIter++;
              }     
              
              ExcelWSheet.createFreezePane(0, 1);
              fileOut = new FileOutputStream(path);
              ExcelWBook.write(fileOut);
              fileOut.close();
              
        } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
              log.error("Class ExcelUtils | Method addHeaderswithRange | Exception desc : "+e.getMessage());
        }
        
 }

    public void addHeaders_AtEnd(String[] headers){          
        
	try {                
 
        
		int EndColVal=getColCount();
		  
		Row=ExcelWSheet.getRow(0);
		boolean colFound = false;
		
		for (int j = 0; j <headers.length; j++) {
			
    	  for(int i=0;i<Row.getLastCellNum();i++){
			if(Row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(headers[j].trim())){
				colFound = true;
				break;
			}
    	  } 
        	  
    	  if(!colFound){
  			Cell = Row.createCell(EndColVal);
  			Cell.setCellValue(headers[j]);
  			ExcelWSheet.autoSizeColumn(EndColVal);
  			EndColVal ++;
    	  }
		}     
          
		ExcelWSheet.createFreezePane(0, 1);
		
		fileOut = new FileOutputStream(path);
		ExcelWBook.write(fileOut);
		fileOut.close();
          
    } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          log.error("Class ExcelUtils | Method addHeaderswithRange | Exception desc : "+e.getMessage());
    }
        
 }
	
	public int getRowIndex_for_iteration(String testCaseID,String iterationVal){
		APP_LOGGER.startFunction("getRowIndex_for_iteration");
		
		String retval=setExcelSheet(Globals.TestData);
		int tcColumnIndex=getColumnIndex(Globals.TestData,Globals.TestCaseIDColName);
		int iterColIndex = getColumnIndex(Globals.TestData,"Iteration");
		if(tcColumnIndex == Globals.INT_FAIL || retval.equalsIgnoreCase(Globals.KEYWORD_FAIL) ||  iterColIndex == Globals.INT_FAIL){
			return Globals.INT_FAIL;
		}else{
			int iRowNum=0;
			boolean rowCellValFound= false;
			try {
				
				String cellValForTestCase ="";
				String cellValForIter	  ="";
				
				int rowCount = getRowCount(Globals.TestData);
				for (iRowNum = 1; iRowNum<rowCount; iRowNum++){
					cellValForTestCase  = getCellData(iRowNum,tcColumnIndex,Globals.TestData);
					cellValForIter		= getCellData(iRowNum,iterColIndex,Globals.TestData);
					
					try{
						if(!cellValForIter.isEmpty() && !cellValForIter.equals("")){
							cellValForIter = Integer.toString((int)Double.parseDouble(cellValForIter));
						}
					}catch(Exception e){
						continue;
					}
					if  (cellValForTestCase.equalsIgnoreCase(testCaseID) && cellValForIter.equalsIgnoreCase(iterationVal) ){
						rowCellValFound = true;
						break;
					}
				}   
				if (rowCellValFound == true){
					return iRowNum;
				}else{
					return Globals.INT_FAIL;
				}
				 
			} catch (Exception e){
				log.error("Class ExcelUtils | Method getRowIndex_for_iteration | Exception desc : "+e.toString());
				return Globals.INT_FAIL;
			}
		}
		
	}
	
	public String setOutputValue(String parameterName,String paramValue) throws Exception {
		
		String retval =  Globals.KEYWORD_FAIL;
		int rowCount=-1;
		int i=1;
		String CellData="";
			
		boolean paramFound = false;
		try{
			setExcelSheet("OutputParam");
			rowCount = getRowCount();
			for(;i<rowCount;i++){
				Cell = ExcelWSheet.getRow(i).getCell(0);
				if (Cell != null){
					
					if(Cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						CellData = Double.toString(Cell.getNumericCellValue());
					}else{
						CellData = Cell.getStringCellValue().toString();	
					}
				}
				
				if(CellData.equals(parameterName)){
					paramFound = true;
					break;
				}
			}
			setExcelSheet("OutputParam");
			if(paramFound){
				Row = ExcelWSheet.getRow(i);
				Row.createCell(1);
				Cell = Row.getCell(1);
				Cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				Cell.setCellValue(paramValue);
			}else{
				Row = ExcelWSheet.createRow(rowCount);
				
				Row.createCell(0).setCellValue(parameterName);
				Row.createCell(1).setCellValue(paramValue);
			}
			fileOut = new FileOutputStream(path);
			ExcelWBook.write(fileOut);
			fileOut.close();
			
			retval = Globals.KEYWORD_PASS;
		}catch(Exception e){
			log.error("Unable to set output parameter value for  - "+parameterName);
			e.printStackTrace();
			throw new Exception("Unable to set output parameter value for  - "+parameterName);
		}
		return retval;
	}
	
	public String getOutputValue(String parametername) throws Exception {
		String retval =  Globals.KEYWORD_FAIL;
		int rowCount=-1;
		int i=1;
		String CellData="";
		
		try{
			setExcelSheet("OutputParam");
			rowCount = getRowCount();
			for(;i<rowCount;i++){
				Cell = ExcelWSheet.getRow(i).getCell(0);
				if (Cell != null){
					
					if(Cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						CellData = Double.toString(Cell.getNumericCellValue());
					}else{
						CellData = Cell.getStringCellValue().toString();	
					}
				}
				
				if(CellData.equals(parametername)){
					DataFormatter formatter = new DataFormatter();
					
//					if(ExcelWSheet.getRow(i).getCell(1).getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
//						retval = Double.toString(ExcelWSheet.getRow(i).getCell(1).getNumericCellValue());
//					}else{
//						retval = ExcelWSheet.getRow(i).getCell(1).getStringCellValue().toString();	
//					}
					retval =  formatter.formatCellValue( ExcelWSheet.getRow(i).getCell(1));
					break;
				}
			}
		}catch(Exception e){
			log.error("Unable to get output parameter value for  - "+parametername);
			e.printStackTrace();
			throw new Exception("Unable to get output parameter value for  - "+parametername);
		}
		return retval;
	}
	
	public static void covertXlsToXlsx(String xlsFilePath,String xlsxFilePath)throws InvalidFormatException,
    IOException 
	{
		String inpFn = xlsFilePath; 
		String outFn = xlsxFilePath; 

		InputStream in = new BufferedInputStream(new FileInputStream(inpFn));
		try {
	    Workbook wbIn = new HSSFWorkbook(in);
	    File outF = new File(outFn);
	    if (outF.exists())
	        outF.delete();
	
	    Workbook wbOut = new XSSFWorkbook();
	    int sheetCnt = wbIn.getNumberOfSheets();
	    for (int i = 0; i < sheetCnt; i++) {
	        Sheet sIn = wbIn.getSheetAt(i);
	        Sheet sOut = wbOut.createSheet(sIn.getSheetName());
	        Iterator<Row> rowIt = sIn.rowIterator();
	        while (rowIt.hasNext()) {
	            Row rowIn = rowIt.next();
	            Row rowOut = sOut.createRow(rowIn.getRowNum());
	
	            Iterator<org.apache.poi.ss.usermodel.Cell> cellIt = rowIn.cellIterator();
	            while (cellIt.hasNext()) {
	            	org.apache.poi.ss.usermodel.Cell cellIn = cellIt.next();
	            	org.apache.poi.ss.usermodel.Cell cellOut = rowOut.createCell(
	                        cellIn.getColumnIndex(), cellIn.getCellType());
	
	                switch (cellIn.getCellType()) {
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
	                    break;
	
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
	                    cellOut.setCellValue(cellIn.getBooleanCellValue());
	                    break;
	
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
	                    cellOut.setCellValue(cellIn.getErrorCellValue());
	                    break;
	
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
	                    cellOut.setCellFormula(cellIn.getCellFormula());
	                    break;
	
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
	                    cellOut.setCellValue(cellIn.getNumericCellValue());
	                    break;
	
	                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
	                    cellOut.setCellValue(cellIn.getStringCellValue());
	                    break;
	                }
	
	                {
	                    CellStyle styleIn = cellIn.getCellStyle();
	                    CellStyle styleOut = cellOut.getCellStyle();
	                    styleOut.setDataFormat(styleIn.getDataFormat());
	                }
	                cellOut.setCellComment(cellIn.getCellComment());
	            }
	        }
	    }
	    OutputStream out = new BufferedOutputStream(new FileOutputStream(
	            outF));
	    try {
	        wbOut.write(out);
	    } finally {
	        out.close();
	    }
	} finally {
	    in.close();
	}
	}

}
