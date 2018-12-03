package com.sterlingTS.utils.commonUtils;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonVariables.Globals;

public class HtmlUtils extends Globals{
	
	protected static Logger log = Logger.getLogger(HtmlUtils.class);
	
	public static char c 									= (char)34;
	public static String LINE_BREAK="<br>";
	public static String START_TABLE="<table border=1 bordercolor=" + "#000000 id=table1 width=900 height=31 cellspacing=0 bordercolorlight=" + "#FFFFFF>";
	public static String END_TABLE="</table>";
	public static String START_TR="<tr>";
	public static String HEADER_ROW_COLOR="bgcolor = #BBBBBB";

	public static String HEADER_FONT_START="<p align=center><font color=#000000 size=4 face= "+ c+"Copperplate Gothic Bold"+c + ">&nbsp;";
	public static String HEADER_FONT_END = "</font><font face= " + c+"Copperplate Gothic Bold"+c + "></font> </p>";
	public static String END_TD="</td>";
	public static String END_TR="</tr>";
	public static String COL_HEADER_TR="<tr bgcolor=#5CB3FF >";
	public static String COL_TD="<td width=" + "400>";
	public static String COL_TD_VAL_FORMAT="<p align=" + "center><b><font color = #000000 face=" + "Arial Narrow " + "size=" + "2" + ">";
	public static String END_BOLD="</b>";

	public static String PASS_CELL="<td bgcolor =#008000 width=" + "200>";
	public static String FAIL_CELL = "<td bgcolor=#c48793 width=" + "200>";
	public static String NORUN_CELL="<td bgcolor=#ffbf80 width=" + "200>";
	public static String CELL_TEXT_FORMAT_PARA="<p align=" + "center><font color=#000000 face=" + "Verdana " + "size=" + "2" + ">";
	public static String Start_BLOCK_QUOTES="<blockquote>";
	public static String END_BLOCK_QUOTES="</blockquote>";
	public static String END_BODY="</body>";
	public static String END_HTML="</html>";
	public static String START_HTML="<html>";
	public static String START_HEAD="<head>";
	public static String END_HEAD="</head>";
	public static String START_BODY="<body bgcolor = #FFFFFF>";
	
	public static File CreateFile (String FileName) throws Exception{
	
		APP_LOGGER.startFunction("CreateFile - String");
		File file 			= new File(FileName);
		file.createNewFile();
		return file;
	} 

	public static BufferedWriter CreateFile_for_Writing(String FileName) throws Exception{
		
		APP_LOGGER.startFunction("CreateFile_for_Writing - String");

			File  objFile		= CreateFile(FileName);
			FileWriter fw 		= new FileWriter(objFile,false);
			BufferedWriter bw 	= new BufferedWriter(fw);
			return bw;
	} 
	
	// 21 Mar 2016 - Changing Optional Parameter - In signature and Passing in called Functions, Adding Throw e for recovery	
	//public static String Create_TC_Html_Report(String FileName, boolean...reportEventFlag)
	public static String Create_TC_Html_Report(String FileName, String...optValidationType)
	{
		APP_LOGGER.startFunction("Create_TC_Html_Report - String");
		String retval		= Globals.KEYWORD_PASS;
		
		try{
			
			String TestCaseExcelReportPath 	= ""; 
			//String ExcelWorkBookPath 		= "C:\\staf_selenium\\SeleniumFramework\\executionResults\\CO-001.xlsx";
			String ResultsSheetName 		= "stepResults";
	    	BufferedWriter bw 				= CreateFile_for_Writing(FileName); //29th Feb 2016
			
	    	  	
	    	  	String strProjectName 	 				= Globals.CurrentPlatform;
	    	  	String CurrentTestCaseName				= Globals.TestCaseID; 
	    	  	String CurrentTestCaseDescription 		= Globals.tcDescription; 
	    	  	TestCaseExcelReportPath 				= Globals.TC_Results_Excel_Path;
	    	  
	    		DateFormat dtTCStartTime 				= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    		String TestCaseStartTime 				= dtTCStartTime.format(Globals.tcStart_TimeStamp);
	    		
	    		//String OverallStatus 					= "PASS";
	    		String OverallStatus 					= Globals.tcExecutionStatus;

	    		String CaptureScreenShotForDoneStatus 	= Globals.CaptureScreenShotForDoneStatus;	//RameshT 20 June'16 - removed the hardcoding of TRUE


	    		//Colour Constants
	    		String strHeadColor 		 			= "#12579D";
	    		String strSettColor 					= "#BCE1FB";
	    		String strContentBGColor 				= "#FFFFFF";

	    		//Pass/Fail Counters
	    	  	int intPassCounter 						= 0;
	    		int	intFailCounter 						= 0;
	    		int intWarningCounter					= 0;
	    		int TotalNoIteration 					= 1;
	    		
	    			    	  
	    	  // Writing the Html Headers to the Notepad file... - Start

	    	  bw.write(START_HTML);
	    	      bw.write(START_HEAD);
	    	          bw.write("<meta http-equiv=" + "Content-Language" + "content=" + "en-us>");
	    	          bw.write("<meta http-equiv="+ "Content-Type" + "content=" + "text/html; charset=windows-1252" + ">");
	    	          bw.write("<title> Test Case Automation Execution Results</title>");
	    	          bw.write("<script>");
	    	          bw.write("top.window.moveTo(0, 0);");
	    	          bw.write("window.resizeTo(screen.availwidth, screen.availheight);");
	    	          bw.write("</script>");                        
	    	      bw.write(END_HEAD);

	    	      bw.write(START_BODY);
	    	      bw.write(Start_BLOCK_QUOTES);    
	    	      
	  			//Writing the Html Body - Headers to the Notepad file... - Start
	    	    bw.write("<p align = center>"+START_TABLE);
		  	        bw.write(START_TR);
	                    bw.write("<td COLSPAN = 6 "+ HEADER_ROW_COLOR + ">");
	                      bw.write(HEADER_FONT_START + strProjectName + " - " + CurrentTestCaseName + " ("  + CurrentTestCaseDescription +  ") " + HEADER_FONT_END);
	                      bw.write(END_TD);
	  	            bw.write(END_TR);
	  	            
	  	                
		            bw.write(START_TR);
	                    bw.write("<td COLSPAN = 6 "+ HEADER_ROW_COLOR + ">");
	                        bw.write(HEADER_FONT_START+ "DATE: " +  TestCaseStartTime + HEADER_FONT_END);
	                    bw.write(END_TD);                                                                     
                    bw.write(END_TR);
	            
			            bw.write(START_TABLE);                                                     
				            bw.write(COL_HEADER_TR);
				            
				            	String ColorCode = "";
				            	if(OverallStatus.equalsIgnoreCase("PASS"))
				            	{
									ColorCode = "#008000"; //'color=" + "#008000" 
				            	}
				            	else if(OverallStatus.equalsIgnoreCase("FAIL"))
					            {
									ColorCode = "#FF0000";						            			
					            }
				            	else if(OverallStatus.equalsIgnoreCase("DONE"))
				            	{
									ColorCode = "#000000";		//#B2BEB5
				            	}
				            	else if(OverallStatus.equalsIgnoreCase("WARNING"))
				            	{
									ColorCode = "#FFFF00c";
				            	}
				            	else
				            	{
				            		ColorCode = "#8A4117";
				            	}
								
		                        bw.write("<td colspan =2>");
		            				bw.write("<p align=justify><b><font color=" + strHeadColor + " size=2 face= Verdana>"+ "&nbsp;"+ "Testcase Status : <font color = "+ColorCode +" size=2 face= Verdana>" + OverallStatus );		
		                        bw.write(END_TD);                                                                   
		                        
		                        bw.write("<td colspan =2>");
		                            bw.write("<p align=right><b><font color=" + strHeadColor + " size=2 face= Verdana>"+ "&nbsp;"+ "Total Iteration : " +  TotalNoIteration);
		                        bw.write(END_TD);
				            bw.write(END_TR);
		    			bw.write(END_TABLE);                                                 
    
		    			
	// Header Table Start			
   bw.write("<p align = center> " + START_TABLE);
        bw.write(COL_HEADER_TR);

        
            bw.write(COL_TD);
            	bw.write(COL_TD_VAL_FORMAT + "Component"+END_BOLD);
            bw.write(END_TD);

            bw.write(COL_TD);
                bw.write(COL_TD_VAL_FORMAT + "Step Name"+END_BOLD);
            bw.write(END_TD);
            
            bw.write(COL_TD);
                bw.write(COL_TD_VAL_FORMAT + "Description"+END_BOLD);
            bw.write(END_TD);
            
            bw.write(COL_TD);
                bw.write(COL_TD_VAL_FORMAT + "Status"+END_BOLD);
            bw.write(END_TD);
            
            bw.write(COL_TD);
                bw.write(COL_TD_VAL_FORMAT + "Time"+END_BOLD);
            bw.write(END_TD);
        bw.write(END_TR);
    //End of Header

        ExcelUtils ExcelWorkSheet 			= new ExcelUtils(TestCaseExcelReportPath);
//        Anand 3/2 -  Commented the below code as this constructor creates a new Excel file, as this is not required here
//        ExcelUtils ExcelWorkSheet 				= new ExcelUtils(TestCaseExcelReportPath,ResultsSheetName);
//		String retpath 							= ExcelWorkSheet.path;
		
		int ResultsSheetRowCount 				= ExcelWorkSheet.getRowCount(ResultsSheetName);

        
		int iFlow_IDColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Flow_ID");
		int iComponentColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Component");
		int istepNameColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Step_Name");
		int istepDescriptionColIndex			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Step_Description");
		int istepResultColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Step_Result");
		int itimeStampColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Timestamp");
		int isnapshotPathColIndex				= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Snapshot_Path"); 

		String strFlow_ID_ColValue				= "";
		String strComponent_ColValue			= "";
		String strStep_Name_ColValue 			= "";
		String strStep_Description_ColValue		= "";
		String strStep_Result_ColValue			= "";
		String strTimestamp_ColValue			= "";
		String strSnapshot_Path_ColValue		= ""; 
		//String strIteration 					= "";
		String strPrevFlow_ID_ColValue			= ""; 	
        
		
		// Need to use Array or Hash Map for Storing Data 

		for (int i = 1; i < ResultsSheetRowCount; i++){

			
			try {
				
				strFlow_ID_ColValue									= ExcelWorkSheet.getCellData(i,iFlow_IDColIndex,ResultsSheetName);
				strComponent_ColValue								= ExcelWorkSheet.getCellData(i,iComponentColIndex,ResultsSheetName);
				strStep_Name_ColValue 								= ExcelWorkSheet.getCellData(i,istepNameColIndex,ResultsSheetName);
				strStep_Description_ColValue						= ExcelWorkSheet.getCellData(i,istepDescriptionColIndex,ResultsSheetName);
				strStep_Result_ColValue								= ExcelWorkSheet.getCellData(i,istepResultColIndex,ResultsSheetName);
				strTimestamp_ColValue								= ExcelWorkSheet.getCellData(i,itimeStampColIndex,ResultsSheetName);
				strSnapshot_Path_ColValue							= ExcelWorkSheet.getCellData(i,isnapshotPathColIndex,ResultsSheetName); 

				
					if (strPrevFlow_ID_ColValue.equalsIgnoreCase(strFlow_ID_ColValue) == false){
					// Header Table Start			
						strPrevFlow_ID_ColValue	= strFlow_ID_ColValue;
						bw.write("<tr bgcolor=" + strSettColor + ">");
				            bw.write("<td COLSPAN = 6 width=" + "400");
			                	bw.write(CELL_TEXT_FORMAT_PARA + "Flow_Id: " + strFlow_ID_ColValue + END_BOLD);
				            bw.write(END_TD);
			        	bw.write(END_TR);
				    //End of Header
					}
				
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Exception - " + e.toString() + " Occurred for Results Row - " + i);
					continue; //Any exceptions, the iteration is incremented and the remaining code is executed only for Next Iteration.
				}
		
		
			//Flow ID = ?
        int boolExportEvent=0;
        int intRowCounter = 0;
        
        if(CaptureScreenShotForDoneStatus.equalsIgnoreCase("Yes") &&  strStep_Result_ColValue.equalsIgnoreCase("Done")){
        	boolExportEvent=1;
        }
		else if(strStep_Result_ColValue.equalsIgnoreCase("Info") == false && strStep_Result_ColValue.equalsIgnoreCase("Done") == false){ 
			boolExportEvent=1;
		}

        String strTime 							= strTimestamp_ColValue;
        
		if(boolExportEvent == 1){
            //If IsNull(strIteration) Then
			if(strFlow_ID_ColValue == null){
                bw.write("<tr bgcolor =" + strContentBGColor + ">");
                    bw.write("<td COLSPAN = 6>");
                        bw.write("<p align=center><b><font size=2 face= Verdana>"+ "&nbsp;"+ strStep_Description_ColValue + ":&nbsp;&nbsp;" +  strTime  + "&nbsp");
                    bw.write("</td>");
                bw.write("</tr>");
                intRowCounter = intRowCounter + 1;
			}
            else{
                bw.write("<tr bgcolor =" + strContentBGColor + ">");
	                bw.write("<td width=" + "400>");
	                	bw.write("<p align=" + "center><font face=" + "Verdana " + "size=" + "2" + ">"  +  strComponent_ColValue);
	                bw.write("</td>");                                                                        

	                
	                String strRelativePathForScreenshot = "";
	                
	                bw.write("<td width=" + "400>");
        				//Get ScreenShot File Name
	                
	                	//29th Feb 2016 - Commenting & Taking inside if condition. To Add Screenshot only if Not Blank.
	                
	                
                        if (strStep_Result_ColValue.equalsIgnoreCase("INFO") == false){
                        	//29th Feb 2016 - To Add Screenshot only if Not Blank.
                        	//strSnapshot_Path_ColValue = GenUtils.CheckAndTrimString(strSnapshot_Path_ColValue, optValidationType);
                        	strSnapshot_Path_ColValue = seleniumCommands.CheckAndTrimString(strSnapshot_Path_ColValue);
                        	if(strSnapshot_Path_ColValue != ""){  
        	                	File ScreenShotFile 			= new File(strSnapshot_Path_ColValue);
        	                	
        	                	//Anand commented the below line to update linking of XML files as well        	                	
//        	                	strRelativePathForScreenshot 	= relFolder + ScreenShotFile.getName();
        	                	String parentPath = ScreenShotFile.getParent();
        	            		String[] folders = parentPath.split("\\\\");
        	            		String parentFolder = folders[folders.length-1];
        	            		
        	            		strRelativePathForScreenshot 	= "..\\"+ parentFolder+ File.separator + ScreenShotFile.getName();
        	                									
                        		bw.write("<p align=center><a href='" + strRelativePathForScreenshot + "'><b><font face=" + "verdana" + "size=" + "2" + ">" + strStep_Name_ColValue  + "</font></b></a></p>");
                        	}else{
            					bw.write("<p align=" + "center><font face=" + "Verdana " + "size=" + "2" + ">"  +  strStep_Name_ColValue );
                        	}		
        				}
        				else{
        					bw.write("<p align=" + "center><font face=" + "Verdana " + "size=" + "2" + ">"  +  strStep_Name_ColValue );
        				}
                          
                        
	                bw.write("</td>");
	                
	                bw.write("<td width=" + "500>");
                        bw.write("<p align=" + "center><font face=" + "Verdana " + "size=" + "2" + ">"  +  strStep_Description_ColValue);
	                bw.write("</td>");
	                
	                bw.write("<td width=" + "400>");
		                if (strStep_Result_ColValue.equalsIgnoreCase("PASS")){
                            bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#008000" + ">" + strStep_Result_ColValue + "</font></b>");
                            intPassCounter=intPassCounter + 1;      
		                }
		                else if (strStep_Result_ColValue.equalsIgnoreCase("FAIL")){
                            bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FF0000" + ">" + strStep_Result_ColValue + "</font></b>");
                            intFailCounter=intFailCounter + 1;
		                }
		                else if (strStep_Result_ColValue.equalsIgnoreCase("DONE")){
            				bw.write("<p align=" + "center" + ">" + "<font face=" + "Verdana " + "size=" + "2" + " color=" + "#000000" + ">" + strStep_Result_ColValue + "</font>");
		                }
		                else if (strStep_Result_ColValue.equalsIgnoreCase("WARNING")){
            				bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FFC200" + ">" + strStep_Result_ColValue + "</font></b>");
            				intWarningCounter=intWarningCounter+1;
		                }
		                else{
                            bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#8A4117" + ">" + strStep_Result_ColValue + "</font></b>");                       
		                }
	                bw.write("</td>");
	                
	                bw.write("<td width=" + "400>");
                        bw.write("<p align=" + "center><font face=" + "Verdana " + "size=" + "2" + ">"  +  strTime);
	                bw.write("</td>");
		        bw.write("</tr>");
            }
		}
	} //For Loop
    

    
	int TotalVerificationPoints = intPassCounter + intFailCounter + intWarningCounter;
	
		bw.write("<tr  bgcolor =" + strSettColor + ">");
            bw.write("<td colspan=2 width=" + "1200>");
                bw.write("<p align=justify><b><font color=" + strHeadColor + "  size=2 face= Verdana>"+ "&nbsp;"+ "No. Of Verification Points :&nbsp;&nbsp;" + TotalVerificationPoints  + "&nbsp;");
            bw.write("</td>");

			bw.write("<td colspan=1 width=" + "400>");
                bw.write("<p align=justify><b><font color=" + strHeadColor + "  size=2 face= Verdana>"+ "&nbsp;"+ "Passed :&nbsp;&nbsp;" +  intPassCounter + "&nbsp;");
            bw.write("</td>");
             
            bw.write("<td colspan =1 width=" + "400>");                
                bw.write("<p align=justify><b><font color=" + strHeadColor + "  size=2 face= Verdana>"+ "&nbsp;"+ "Failed :&nbsp;&nbsp;" +  intFailCounter + "&nbsp;");
            bw.write("</td>");
			
			bw.write("<td colspan=1 width=" + "400>");                
                bw.write("<p align=justify><b><font color=" + strHeadColor + "  size=2 face= Verdana>"+ "&nbsp;"+ "Warning :&nbsp;&nbsp;" +  intWarningCounter + "&nbsp;");
            bw.write("</td>");
        bw.write("</tr>"); 
    bw.write("</table>");

    	bw.write("</blockquote>");                              
    bw.write("</body>");
bw.write("</html>");  
	    	      
	    	  // Writing the Html Headers to the Notepad file... - End
	    	  
			bw.close();
	    	  
//		  System.out.println("Data successfully appended at the end of file");

	       }catch(Exception e){
//	    	   System.out.println("Exception occurred: - " + e.toString());
//	    	   System.out.println("Exception occurred: - ");
	    	   e.printStackTrace();
	    	   retval = KEYWORD_FAIL;
	      }
		return retval;
	}		

	public static String STAF_ExportResultSummaryToHtml(String FileName, boolean...reportEventFlag)
	{
	    
		APP_LOGGER.startFunction("STAF_ExportResultSummaryToHtml - String");
		//Project Details.
		String Scenario_ExcelWorkBookPath 		= "";
		String ResultsSheetName 				= "tcResults";


		Scenario_ExcelWorkBookPath				= Globals.tcSummaryResult_Excel_Path;
		FileName 								= Globals.tcSummaryResult_HTML_Path;

		
	  	String strProjectName 	 				= Globals.CurrentPlatform;
	
		
		//Pass/Fail Counters - Automation Script counters
	  	int intPassCounter 						= 0;
		int	intFailCounter 						= 0;
		int intNoRunCounter						= 0;
		int intWarningCounter					= 0;
		double dblTotalExecTime 				= 0	;

		//Manual Test case counters
		int intTCPassCounter 						= 0;
		int	intTCFailCounter 						= 0;
		int intTCNoRunCounter						= 0;
		int intTCWarningCounter						= 0;
		
		//Colour Constants - Default settings for theme
		String strHeadColor 		 			= "#12579D";
		String strSettColor 					= "#BCE1FB";
		String strContentBGColor 				= "#FFFFFF";
		double dblTotAutomationSavings			= 0;
		double dblTotManualEffort				= 0;
		
		
		Date curDate 							= new Date();
		String strUnit 							= "minute";
		String retval		= Globals.KEYWORD_PASS;

		String TestCaseHTMLReportPath 		= "";

		String strTest_Scenario				= "";
		String strTC_ID						= "";
		String strTC_Description 			= "";
		String strTC_Status					= "";
		String strLnkFileName				= "";
		int totalManualTCCount			=	0; 
		String strTotalManualTCCount		="";
		String strExecutionTime_Minutes		= "";
		String strManualEffort				= "";
		String strAutomationSavings			= "";
		double dblExecutionTime_Minutes		= 0;
		double dblManualEffort				= 0;
		double dblAutomationSavings			= 0;
		
		
		try{
			//29 Feb 2016
			ExcelUtils ExcelWorkSheet 		= new ExcelUtils(Scenario_ExcelWorkBookPath);
			
			int ResultsSheetRowCount 			= ExcelWorkSheet.getRowCount(ResultsSheetName);
	        
			int iTest_ScenarioColIndex			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Test_Scenario");
			int iTC_IDColIndex					= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "TC_ID");
			int iTC_DescriptionColIndex			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "TC_Description");
			int iTC_StatusColIndex				= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "TC_Status");
			int iExecutionTime_MinutesColIndex	= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "ExecutionTime_Minutes");
			int iManualEffortColIndex			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "ManualEffort");
			int iAutomationSavingsColIndex		= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "AutomationSavings"); 
    		int totalManualTCColIndex			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "ManualTCCount");
    		ArrayList<String[]> tcResultList 	= new ArrayList<String []>();;
    		
    		
    		for (int i = 1; i < ResultsSheetRowCount; i++){
    			strTest_Scenario			= ExcelWorkSheet.getCellData(i,iTest_ScenarioColIndex,ResultsSheetName);
				strTC_ID					= ExcelWorkSheet.getCellData(i,iTC_IDColIndex,ResultsSheetName);
				strTC_Description 			= ExcelWorkSheet.getCellData(i,iTC_DescriptionColIndex,ResultsSheetName);
				strTC_Status				= ExcelWorkSheet.getCellData(i,iTC_StatusColIndex,ResultsSheetName);
				strExecutionTime_Minutes	= ExcelWorkSheet.getCellData(i,iExecutionTime_MinutesColIndex,ResultsSheetName);
				strManualEffort				= ExcelWorkSheet.getCellData(i,iManualEffortColIndex,ResultsSheetName);
				strAutomationSavings		= ExcelWorkSheet.getCellData(i,iAutomationSavingsColIndex,ResultsSheetName);
				strTotalManualTCCount		= ExcelWorkSheet.getCellData(i,totalManualTCColIndex,ResultsSheetName);
				
				String[] temp={strTest_Scenario,strTC_ID,strTC_Description,strTC_Status,strExecutionTime_Minutes,strManualEffort,strAutomationSavings,strTotalManualTCCount};
				tcResultList.add(temp);
			}

	    	  	


				BufferedWriter bw = CreateFile_for_Writing(FileName);
				
				//Create the Report header
		    	  bw.write(START_HTML);
	    	      	bw.write(START_HEAD);
	    	          bw.write("<meta http-equiv=" + "Content-Language" + "content=" + "en-us>");
	    	          bw.write("<meta http-equiv="+ "Content-Type" + "content=" + "text/html; charset=windows-1252" + ">");
	    	          bw.write("<title> Test Case Automation Execution Results</title>");
	    	      bw.write(END_HEAD);
			
	    	      bw.write(START_BODY);
	    	      	bw.write(Start_BLOCK_QUOTES);    
	    	              bw.write("<p align = center>" + START_TABLE);
					
					bw.write(START_TR);
						bw.write("<td COLSPAN = 6 " + HEADER_ROW_COLOR +">");
							bw.write(HEADER_FONT_START +"Automation Execution Results - " + strProjectName   + HEADER_FONT_END);
						bw.write(END_TD);
					bw.write(END_TR);
					
					bw.write(START_TR);
						bw.write("<td COLSPAN = 3 bgcolor =" + strSettColor +">");
							
							bw.write("<center><font color=" + strHeadColor +  "size=1 face= Verdana>" + "&nbsp;" + "Date: " + curDate  + "</font></center>");
						bw.write(END_TD);
						
						bw.write("<td COLSPAN = 3 bgcolor = " + strSettColor +">");
							bw.write("<center><font color=" + strHeadColor +  "size=1 face= Verdana>" + "&nbsp;" + "Total Execution Time: " + Double.parseDouble(Globals.Scenario_Execution_Time) + " " + strUnit  + "</font></center>");
	                    bw.write(END_TD);
					bw.write(END_TR);
//					bw.write(END_TABLE);
//					bw.write(LINE_BREAK + START_TABLE);		
					bw.write("<tr>");
					bw.write("<td COLSPAN = 6 "+ HEADER_ROW_COLOR +">");
						bw.write(HEADER_FONT_START + "Execution Summary "+HEADER_FONT_END);
					bw.write(END_TD);
				bw.write(END_TR);

					bw.write(COL_HEADER_TR);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Project "+END_BOLD);
						bw.write(END_TD);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "TestSuite"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "TestScenario"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "ExecuteTCs_ofStatus"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
						bw.write(COL_TD_VAL_FORMAT + "BrowserType"+END_BOLD);
					bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Status"+END_BOLD);
						bw.write(END_TD);
						
					bw.write(END_TR);
					
					bw.write("<tr bgcolor = " + strContentBGColor + ">");	
					
					bw.write(COL_TD);							
			        	bw.write(CELL_TEXT_FORMAT_PARA + Globals.CurrentPlatform );
					bw.write(END_TD);
					
					bw.write(COL_TD);
				        bw.write(CELL_TEXT_FORMAT_PARA +  Globals.currentTestSuiteColValue);
					bw.write(END_TD);			
					
					bw.write(COL_TD);
				        bw.write(CELL_TEXT_FORMAT_PARA  +  Globals.currentScenarioColValue);
					bw.write(END_TD);
					
					bw.write(COL_TD);
				        bw.write(CELL_TEXT_FORMAT_PARA  +  Globals.currentExecuteTCsofStatusColValue);
					bw.write(END_TD);
					
					bw.write(COL_TD);
			        	bw.write(CELL_TEXT_FORMAT_PARA  +  Globals.BrowserType);
		        	bw.write(END_TD);

					
					bw.write(COL_TD);
					String strStatus = Globals.pfExecutionStatus;
					if(Globals.TEST_CASE_EXECUTED_COUNTER == 0 ){
						strStatus =  "NoRun";	
					}
					
					if(strStatus.equalsIgnoreCase("PASS")){ 
							bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#008000" + ">" + strStatus + "</font></b>");
//							intPassCounter = intPassCounter + 1;
					}
					else if (strStatus.equalsIgnoreCase("FAIL")){
							bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FF0000" + ">" + strStatus + "</font></b>");
//							intFailCounter = intFailCounter + 1;
					}
					else if (strStatus.equalsIgnoreCase("WARNING")){
							bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FFC200" + ">" + strStatus + "</font></b>");
//							intWarningCounter = intWarningCounter + 1;
					}
					else{
							bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#8A4117" + ">" + strStatus + "</font></b>");
							intNoRunCounter=intNoRunCounter + 1;
					}
					bw.write(END_TD);			
				bw.write(END_TR);
					
					bw.write(END_TABLE);
					
					if(Globals.TEST_CASE_EXECUTED_COUNTER == 0 ){
						 bw.write("<p align = center>" + START_TABLE);
							
							bw.write(START_TR);
								bw.write("<td COLSPAN = 6 " + HEADER_ROW_COLOR +">");
									bw.write("<font face=" + "Verdana " + "size=" + "2" + " color=" + "#FF0000" + ">" +"No Test Case has been selected for execution."+ "</font>");
								bw.write(END_TD);
							bw.write(END_TR);
						bw.write(END_TABLE);
						bw.write(END_BLOCK_QUOTES);				
						bw.write(END_BODY);
					bw.write(END_HTML);
					
					bw.close();
						
						return Globals.KEYWORD_PASS;
						
					}

						for (int i = 0; i < tcResultList.size(); i++){
							
							try {
								strTest_Scenario			= tcResultList.get(i)[0];
								strTC_ID					= tcResultList.get(i)[1];
								strTC_Description 			= tcResultList.get(i)[2];
								strTC_Status				= tcResultList.get(i)[3];
								dblExecutionTime_Minutes	= Double.parseDouble(tcResultList.get(i)[4]);
								dblManualEffort				= Double.parseDouble(tcResultList.get(i)[5]);
								dblAutomationSavings		= Double.parseDouble(tcResultList.get(i)[6]);
								totalManualTCCount			= (int)Double.parseDouble(tcResultList.get(i)[7]);
							
								
								DecimalFormat roundEffort	= new DecimalFormat("#0.00");
								dblManualEffort = Double.parseDouble(roundEffort.format(dblManualEffort));

								
									if(strTC_Status.equalsIgnoreCase("PASS")){ 
										
										intPassCounter = intPassCounter + 1;
										intTCPassCounter = intTCPassCounter + totalManualTCCount;
									}
									else if (strTC_Status.equalsIgnoreCase("FAIL")){
										
										intFailCounter = intFailCounter + 1;
										intTCFailCounter =  intTCFailCounter + totalManualTCCount;
									}
									else if (strTC_Status.equalsIgnoreCase("WARNING")){
										
										intWarningCounter = intWarningCounter + 1;
										intTCWarningCounter =  intTCWarningCounter + totalManualTCCount;
									}
									else{
										
										intNoRunCounter=intNoRunCounter + 1;
										intTCNoRunCounter =  intTCNoRunCounter + totalManualTCCount;
									}
		
													} 
							catch (Exception e)
							{
								e.printStackTrace();
								log.info("Exception - " + e.toString() + " Occurred for Results Row - " + i);
								continue; //Any exceptions, the iteration is incremented and the remaining code is not executed
							}
							
							DecimalFormat roundEffort				= new DecimalFormat("#0.00");
							//Total Manual Effort
							dblTotManualEffort						= dblTotManualEffort + dblManualEffort;
							dblTotManualEffort						= Double.parseDouble(roundEffort.format(dblTotManualEffort));
							
							//Total Execution Time.
							dblTotalExecTime 						= dblTotalExecTime + dblExecutionTime_Minutes;
							dblTotalExecTime						= Double.parseDouble(roundEffort.format(dblTotalExecTime));
							
							//Total Automation Savings
							dblTotAutomationSavings 				= dblTotManualEffort - dblTotalExecTime;
							dblTotAutomationSavings 				= Double.parseDouble(roundEffort.format(dblTotAutomationSavings));
							

					
						}//For End
						
						
						 
						
						
		                bw.write(LINE_BREAK + START_TABLE);	
							
							bw.write(START_TR);
							bw.write("<td COLSPAN = 4 "+HEADER_ROW_COLOR + " >");
								bw.write(HEADER_FONT_START + "Automation Summary " + HEADER_FONT_END);
							bw.write(END_TD);
						bw.write(END_TR);
						
						
						
							bw.write(COL_HEADER_TR);
								
							bw.write(COL_TD);
								bw.write(COL_TD_VAL_FORMAT + "#Flows "+ END_BOLD);
							bw.write(END_TD);
						
							bw.write(COL_TD);
								bw.write(COL_TD_VAL_FORMAT + "#Pass "+ END_BOLD);
							bw.write(END_TD);
							
							bw.write(COL_TD);
								bw.write(COL_TD_VAL_FORMAT + "#Fail "+ END_BOLD);
							bw.write(END_TD);
							
							bw.write(COL_TD);
								bw.write(COL_TD_VAL_FORMAT + "#No Run "+ END_BOLD);
							bw.write(END_TD);
						
						bw.write(END_TR);
							
							bw.write("<tr bgcolor =" + strContentBGColor +">");
							
								bw.write(COL_TD);
									bw.write(CELL_TEXT_FORMAT_PARA + ( intPassCounter+intFailCounter+intWarningCounter + intNoRunCounter));
								bw.write(END_TD);
							
								bw.write(PASS_CELL);
									bw.write(CELL_TEXT_FORMAT_PARA +  intPassCounter);
								bw.write(END_TD);
						
								bw.write(FAIL_CELL);
									bw.write(CELL_TEXT_FORMAT_PARA +  intFailCounter);
								bw.write(END_TD);
								
								bw.write(NORUN_CELL);
									bw.write(CELL_TEXT_FORMAT_PARA +  intNoRunCounter);
								bw.write(END_TD);
							bw.write(END_TR);
						bw.write(END_TABLE);
						
						bw.write(LINE_BREAK + LINE_BREAK + LINE_BREAK + START_TABLE);	
						
							bw.write(START_TR);
								bw.write("<td COLSPAN = 4 "+HEADER_ROW_COLOR + " >");
									bw.write(HEADER_FONT_START +" Automated Test Case Status "+HEADER_FONT_END);
								bw.write(END_TD);
								
								bw.write("<td COLSPAN = 3 "+HEADER_ROW_COLOR + " >");
									bw.write(HEADER_FONT_START+"Automation Savings(minutes) "+HEADER_FONT_END);
								bw.write(END_TD);
							bw.write(END_TR);
							
							
							bw.write(COL_HEADER_TR);
							
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "#TC Executed"+ END_BOLD);
							    bw.write(END_TD);
							
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "#TC Pass"+ END_BOLD);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "#TC Fail"+ END_BOLD);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "#TC No Run"+ END_BOLD);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "Manual Effort"+ END_BOLD);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "Automation Execution Time"+ END_BOLD);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									bw.write(COL_TD_VAL_FORMAT + "Automation Savings"+ END_BOLD);
								bw.write(END_TD);
							bw.write(END_TR);
							
							bw.write(START_TR);
								bw.write(COL_TD);
									bw.write(CELL_TEXT_FORMAT_PARA +  (intTCFailCounter + intTCPassCounter + intTCNoRunCounter + intTCWarningCounter));
								bw.write(END_TD);
								
								bw.write(PASS_CELL);
									bw.write(CELL_TEXT_FORMAT_PARA +  intTCPassCounter);
								bw.write(END_TD);
								
								bw.write(FAIL_CELL);	
									bw.write(CELL_TEXT_FORMAT_PARA +  intTCFailCounter );
								bw.write(END_TD);
								
								bw.write(NORUN_CELL);	
									bw.write(CELL_TEXT_FORMAT_PARA +  intTCNoRunCounter );
								bw.write(END_TD);
								
								bw.write(COL_TD);	
									bw.write(CELL_TEXT_FORMAT_PARA +  dblTotManualEffort );
								bw.write(END_TD);
								
								bw.write(COL_TD);	
									bw.write(CELL_TEXT_FORMAT_PARA +  dblTotalExecTime );
								bw.write(END_TD);
								
								bw.write(COL_TD);	
									bw.write(CELL_TEXT_FORMAT_PARA +  dblTotAutomationSavings );
								bw.write(END_TD);
								
								
							bw.write(END_TR);
			
//							System.out.println("Data successfully appended at the end of file");
			
						bw.write(END_TABLE);
						

						bw.write(LINE_BREAK + LINE_BREAK + LINE_BREAK + START_TABLE);
						bw.write(START_TR);
						bw.write("<td COLSPAN = 7 "+HEADER_ROW_COLOR + " >");
							bw.write(HEADER_FONT_START +" Detailed Results "+HEADER_FONT_END);
						bw.write(END_TD);
						bw.write(END_TR);
						
						bw.write(COL_HEADER_TR);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "TestScenario "+END_BOLD );
						bw.write(END_TD);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Test Script ID "+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Description"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Automation Execution Time(Minutes)"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Manual Effort Estimated (Minutes)"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Automation Saving(Minutes)"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Status"+END_BOLD);
						bw.write(END_TD);
					bw.write(END_TR);
						for (int i = 0; i < tcResultList.size(); i++){
							
							try {
								strTest_Scenario			= tcResultList.get(i)[0];
								strTC_ID					= tcResultList.get(i)[1];
								strTC_Description 			= tcResultList.get(i)[2];
								strTC_Status				= tcResultList.get(i)[3];
								dblExecutionTime_Minutes	= Double.parseDouble(tcResultList.get(i)[4]);
								dblManualEffort				= Double.parseDouble(tcResultList.get(i)[5]);
								dblAutomationSavings		= Double.parseDouble(tcResultList.get(i)[6]);
								totalManualTCCount			= (int)Double.parseDouble(tcResultList.get(i)[7]);
						
//								Anand-Relative Linking of Files -  QA-682
//					    		TestCaseHTMLReportPath 		= Globals.currentPlatform_HTMLPath + "\\" + strTC_ID + ".html";
					    		TestCaseHTMLReportPath 		= strTC_ID + ".html";
								
								
								DecimalFormat roundEffort	= new DecimalFormat("#0.00");
								dblManualEffort = Double.parseDouble(roundEffort.format(dblManualEffort));

								//strLnkFileName = strTC_ID;
								strLnkFileName = TestCaseHTMLReportPath;
								
							bw.write("<tr bgcolor = " + strContentBGColor + ">");
								bw.write(COL_TD);
							        bw.write(CELL_TEXT_FORMAT_PARA  +  strTest_Scenario);
								bw.write(END_TD);
								
								bw.write(COL_TD);							
						        	bw.write("<p align=center><a href='" + strLnkFileName + "'" + "target=" + "" + "><b><font face=" + "verdana" + "size=" + "2" + ">" + strTC_ID + "</font></b></a></p>");
								bw.write(END_TD);
								
								bw.write(COL_TD);
							        bw.write(CELL_TEXT_FORMAT_PARA  +  strTC_Description);
								bw.write(END_TD);
								
								bw.write(COL_TD);
							        bw.write(CELL_TEXT_FORMAT_PARA  +  dblExecutionTime_Minutes);
								bw.write(END_TD);
								
								bw.write(COL_TD);
							        bw.write(CELL_TEXT_FORMAT_PARA  +  dblManualEffort);
								bw.write(END_TD);
	
								bw.write(COL_TD);
							        bw.write(CELL_TEXT_FORMAT_PARA  +  dblAutomationSavings);
								bw.write(END_TD);
								
								bw.write(COL_TD);
									if(strTC_Status.equalsIgnoreCase("PASS")){ 
										bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#008000" + ">" + strTC_Status + "</font></b>");
										intPassCounter = intPassCounter + 1;
										intTCPassCounter = intTCPassCounter + totalManualTCCount;
									}
									else if (strTC_Status.equalsIgnoreCase("FAIL")){
										bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FF0000" + ">" + strTC_Status + "</font></b>");
										intFailCounter = intFailCounter + 1;
										intTCFailCounter =  intTCFailCounter + totalManualTCCount;
									}
									else if (strTC_Status.equalsIgnoreCase("WARNING")){
										bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FFC200" + ">" + strTC_Status + "</font></b>");
										intWarningCounter = intWarningCounter + 1;
										intTCWarningCounter =  intTCWarningCounter + totalManualTCCount;
									}
									else{
										bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#8A4117" + ">" + strTC_Status + "</font></b>");
										intNoRunCounter=intNoRunCounter + 1;
										intTCNoRunCounter =  intTCNoRunCounter + totalManualTCCount;
									}
								bw.write(END_TD);			
							bw.write(END_TR);
											
						} 
							catch (Exception e)
							{
								e.printStackTrace();
								log.info("Exception - " + e.toString() + " Occurred for Results Row - " + i);
								continue; //Any exceptions, the iteration is incremented and the remaining code is not executed
							}
							
							DecimalFormat roundEffort				= new DecimalFormat("#0.00");
							//Total Manual Effort
							dblTotManualEffort						= dblTotManualEffort + dblManualEffort;
							dblTotManualEffort						= Double.parseDouble(roundEffort.format(dblTotManualEffort));
							
							//Total Execution Time.
							dblTotalExecTime 						= dblTotalExecTime + dblExecutionTime_Minutes;
							dblTotalExecTime						= Double.parseDouble(roundEffort.format(dblTotalExecTime));
							
							//Total Automation Savings
							dblTotAutomationSavings 				= dblTotManualEffort - dblTotalExecTime;
							dblTotAutomationSavings 				= Double.parseDouble(roundEffort.format(dblTotAutomationSavings));
							

					
						}//For End
						
						bw.write(END_TABLE);	
						bw.write(END_BLOCK_QUOTES);				
					bw.write(END_BODY);
				bw.write(END_HTML);
				
				bw.close();
				
	    		//23 Feb 2016 - TC Pass/Fail Counters , Automation Execution Counters as GLobal
	    	  	Globals.gblintPassCounter 				= intPassCounter;
	    	  	Globals.gblintFailCounter 				= intFailCounter;
	    		//int intNoRunCounter					= 0;
	    	  	Globals.gblintWarningCounter			= intWarningCounter;
	    	  	Globals.gbldblTotManualEffort			= dblTotManualEffort;
	    	  	Globals.gbldblTotalExecTime 			= dblTotalExecTime;
	    	  	Globals.gbldblTotAutomationSavings		= dblTotAutomationSavings;
	    		
	    	  	Globals.TotalTCPASSED = intTCPassCounter;
	    	  	Globals.TotalTCFAILED = intTCFailCounter;
	    	  	Globals.TotalTCNORUN = intTCNoRunCounter;
	    	  	Globals.TotalTCWARNING = intTCWarningCounter;
				//CLose TC Results excel obj 
				ExcelWorkSheet.closeExcel();
		}
		catch(Exception e){

	    	   e.printStackTrace();
	    	   retval = KEYWORD_FAIL;
	      }
		return retval;
	}	

	public static String STAF_ExportSceanrioResultsToHtml(String FileName, boolean...reportEventFlag)
	{
		APP_LOGGER.startFunction("STAF_ExportResultSummaryToHtml - String");
		String retval		= Globals.KEYWORD_PASS;

		try{
			
				
				//Project Details.
				String Scenario_ExcelWorkBookPath 		= "";
				String ResultsSheetName 				= "scenarioResults";
				
				Scenario_ExcelWorkBookPath				= Globals.Platform_Results_Excel_Path;
				FileName 								= Globals.Platform_Results_HTML_Path;
			
				//Colour Constants - Default settings for theme
	    		String strHeadColor 					= "#12579D";
	    		String strSettColor 					= "#BCE1FB";
	    		String strContentBGColor 				= "#FFFFFF";
	    		
	    		
	    		Date curDate 							= new Date();
	    		String strUnit 							= "minute";
	    		//int intTotalExecTime 					= 0;		
	    		
	    		//Pass/Fail Counters
	    	  	int intPassCounter 						= 0;
	    		int	intFailCounter 						= 0;
	    		int intNoRunCounter						= 0;
	    		int intWarningCounter					= 0;


				BufferedWriter bw = CreateFile_for_Writing(FileName);
				
				//Create the Report header
		    	  bw.write("<html>");
	    	      	bw.write("<head>");
	    	          bw.write("<meta http-equiv=" + "Content-Language" + "content=" + "en-us>");
	    	          bw.write("<meta http-equiv="+ "Content-Type" + "content=" + "text/html; charset=windows-1252" + ">");
	    	          bw.write("<title> Test Case Automation Execution Results</title>");
	    	      bw.write("</head>");
			
	    	      bw.write(START_BODY);
	    	      	bw.write(Start_BLOCK_QUOTES);    
	    	      	bw.write("<p align = center>"+START_TABLE);
				
					bw.write("<tr>");
						bw.write("<td COLSPAN = 7 "+ HEADER_ROW_COLOR +">");
							bw.write(HEADER_FONT_START +"Automation Execution Results " + HEADER_FONT_END);
						bw.write(END_TD);
					bw.write("</tr>");
					
					bw.write("<tr>");
						bw.write("<td COLSPAN = 3 bgcolor =" + strSettColor +">");
							bw.write("<p align=left><font color=" + strHeadColor +  "size=1 face= Verdana>" + "&nbsp;" + "Date: " + curDate  + "</font><font face= " + c+"Copperplate Gothic Bold"+c + "></font> </p>");
						bw.write(END_TD);
						
						bw.write("<td COLSPAN = 4 bgcolor = " + strSettColor +">");
							bw.write("<p align=center><font color=" + strHeadColor +  "size=1 face= Verdana>" + "&nbsp;" + "Total Execution Time: " + Double.parseDouble(Globals.Execution_Execution_Time) + " " + strUnit  + "</font> </p>");
	                    bw.write(END_TD);
					bw.write(END_TR);
					bw.write(END_TABLE);
					
					//Start of Release Info
					
					bw.write(START_TABLE);	
					
					bw.write(START_TR);
					bw.write("<td COLSPAN = 3 "+ HEADER_ROW_COLOR +">");
						bw.write(HEADER_FONT_START +"Global Details "+HEADER_FONT_END);
					bw.write(END_TD);
				bw.write("</tr>");
				
					bw.write(COL_HEADER_TR);
										
					bw.write(COL_TD);
						bw.write(COL_TD_VAL_FORMAT + "ReleaseName"+END_BOLD);
					bw.write(END_TD);
					
					bw.write(COL_TD);
						bw.write(COL_TD_VAL_FORMAT + "ExecutionCycle"+END_BOLD);
					bw.write(END_TD);
					
					bw.write(COL_TD);
						bw.write(COL_TD_VAL_FORMAT + "Environment"+END_BOLD);
					bw.write(END_TD);
					
					
				bw.write(END_TR);
					
					bw.write("<tr bgcolor =" + strContentBGColor +">");
						bw.write(COL_TD);
							bw.write(CELL_TEXT_FORMAT_PARA +  Globals.releaseName);
						bw.write(END_TD);
						
						bw.write(COL_TD);	
							bw.write(CELL_TEXT_FORMAT_PARA +  Globals.ExecutionCycle );
						bw.write(END_TD);
						
						bw.write(COL_TD);	
							bw.write(CELL_TEXT_FORMAT_PARA + Globals.Env_To_Execute_ON );
						bw.write(END_TD);
							
												
					bw.write(END_TR);
	
	
				bw.write(END_TABLE);
					//End of Release Info
					
					
					bw.write("<p align = center> "+ START_TABLE);
					bw.write("<tr>");
					bw.write("<td COLSPAN = 6 "+ HEADER_ROW_COLOR +">");
						bw.write(HEADER_FONT_START + "Project Execution Summary "+HEADER_FONT_END);
					bw.write(END_TD);
				bw.write(END_TR);

					bw.write(COL_HEADER_TR);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Project "+END_BOLD);
						bw.write(END_TD);
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "TestSuite"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "TestScenario"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "ExecuteTCs_ofStatus"+END_BOLD);
						bw.write(END_TD);
						
						bw.write(COL_TD);
						bw.write(COL_TD_VAL_FORMAT + "BrowserType"+END_BOLD);
					bw.write(END_TD);
						
						bw.write(COL_TD);
							bw.write(COL_TD_VAL_FORMAT + "Status"+END_BOLD);
						bw.write(END_TD);
						
					bw.write(END_TR);
					
			
				//Add the data from the Summary file to the HTML file
					
					ExcelUtils ExcelWorkSheet 		= new ExcelUtils(Scenario_ExcelWorkBookPath);
//					  Anand 3/2 -  Commented the below code as this constructor creates a new Excel file, as this is not required here
//					ExcelUtils ExcelWorkSheet 			= new ExcelUtils(Scenario_ExcelWorkBookPath,ResultsSheetName);

					int ResultsSheetRowCount 			= ExcelWorkSheet.getRowCount(ResultsSheetName);

					
					int iPlatform						= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Platform");
					int iTestSuite						= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "TestSuite");
					int iScenario						= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Scenario");
					int iExecuteTCs_ofStatus			= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "ExecuteTCs_ofStatus");
					int iStatus							= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "Status");
					int iBrowser						= ExcelWorkSheet.getColumnIndex(ResultsSheetName, "BrowserType");
					
					String strPlatform 					= "";
		    		String strTestSuite					= "";
		    		String strTC_Scenario 				= ""; 
		    		String strExecuteTCs_ofStatus 		= "";
		    		String strStatus 					= "";
		    		String strBrowser					= "";  

					for (int i = 1; i < ResultsSheetRowCount; i++){

							try {
									strPlatform 			= ExcelWorkSheet.getCellData(i,iPlatform,ResultsSheetName);
						    		strTestSuite			= ExcelWorkSheet.getCellData(i,iTestSuite,ResultsSheetName);
						    		strTC_Scenario 			= ExcelWorkSheet.getCellData(i,iScenario,ResultsSheetName);
						    		strExecuteTCs_ofStatus 	= ExcelWorkSheet.getCellData(i,iExecuteTCs_ofStatus,ResultsSheetName);
						    		strStatus 				= ExcelWorkSheet.getCellData(i,iStatus,ResultsSheetName); 
						    		strBrowser 				= ExcelWorkSheet.getCellData(i,iBrowser,ResultsSheetName);
						    		
						    		if(strStatus!=null)
						    		{
						    			Globals.suiteRunStatus  = strStatus;
						    		}
						    		
						    		String strLnkFileName	= strPlatform +File.separator+strBrowser+File.separator+"HTML_Results"+File.separator+strTestSuite+"_"+strTC_Scenario+".html";
						    		
										bw.write("<tr bgcolor = " + strContentBGColor + ">");	
											
											bw.write(COL_TD);							
									        	bw.write("<p align=center><a href='" + strLnkFileName + "'" + "target="+"" + "><b><font face=" + "verdana" + "size=" + "2" + ">" + strPlatform + "</font></b></a></p>");
											bw.write(END_TD);
											
											bw.write(COL_TD);
										        bw.write(CELL_TEXT_FORMAT_PARA +  strTestSuite);
											bw.write(END_TD);			
											
											bw.write(COL_TD);
										        bw.write(CELL_TEXT_FORMAT_PARA  +  strTC_Scenario);
											bw.write(END_TD);
											
											bw.write(COL_TD);
										        bw.write(CELL_TEXT_FORMAT_PARA  +  strExecuteTCs_ofStatus);
											bw.write(END_TD);
											
											bw.write(COL_TD);
									        	bw.write(CELL_TEXT_FORMAT_PARA  +  strBrowser);
								        	bw.write(END_TD);
					
											
											bw.write(COL_TD);
											if(strStatus.equalsIgnoreCase("PASS")){ 
													bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#008000" + ">" + strStatus + "</font></b>");
													intPassCounter = intPassCounter + 1;
											}
											else if (strStatus.equalsIgnoreCase("FAIL")){
													bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FF0000" + ">" + strStatus + "</font></b>");
													intFailCounter = intFailCounter + 1;
											}
											else if (strStatus.equalsIgnoreCase("WARNING")){
													bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#FFC200" + ">" + strStatus + "</font></b>");
													intWarningCounter = intWarningCounter + 1;
											}
											else{
													bw.write("<p align=" + "center" + ">" + "<b><font face=" + "Verdana " + "size=" + "2" + " color=" + "#8A4117" + ">" + strStatus + "</font></b>");
													intNoRunCounter=intNoRunCounter + 1;
											}
											bw.write(END_TD);			
										bw.write(END_TR);
									}catch(Exception e)
											{
												e.printStackTrace();
												log.info("Exception - " + e.toString() + " Occurred for Results Row - " + i);
												continue; //Any exceptions, the iteration is incremented and the remaining code is not executed
											}
							}//for End
									bw.write(END_TABLE);
									bw.write(END_BLOCK_QUOTES);				
								bw.write(END_BODY);
							bw.write(END_HTML);
							
							bw.close();
							
							//CLose TC Results excel obj 
							ExcelWorkSheet.closeExcel();
					}
					catch(Exception e){

				    	   e.printStackTrace();
				    	   retval = KEYWORD_FAIL;
				      }
				return retval;
	}	
	
}