package com.sterlingTS.utils.commonUtils;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonUtils.database.DAO;
import com.sterlingTS.utils.commonVariables.Globals;

/**
 * @author aunnikrishnan
 *
 */
public class DriverScriptUtils extends BaseCommon{
	
	public static Logger log = Logger.getLogger(DriverScriptUtils.class);
	
	/**
	 * runDriverScript - This function Executes Scenarios and their corresponding test cases based in Scenario Manager and Main Sheets
	 * It first checks the ExecuteFlag in the Scenario Manager sheet. If its Yes, then corresponding Platform_MainSheet is refereed to fetch the executable test cases.
	 * Once it gets the executable test cases, it then fetches the FlowID from the Platform_BuisnessFlow sheet.
	 * It executes the Keywords in BuisnessFlow sheet by searching the corresponding implementation in appLib.Platform.Keywords.java
	 * It also initializes the corresponding global variables, Load OR properties file, fetches all test data from the TestData sheet for the current test case
	 * 
	 */
	//Scenario Manager variables Initializations
	public String sceanrioManager_PlatformColValue;
	public String sceanrioManager_TestSuiteColValue;
	public String sceanrioManager_ExecuteColValue;
	public String sceanrioManager_ScenarioColValue;
	public String sceanrioManager_BrowserColValue;
	public String sceanrioManager_ExecuteTCsofStatusColValue;
	
	WebDriver driver = null;
	int sceanrioManager_PlatformColID;
	int sceanrioManager_TestSuiteColID;
	int sceanrioManager_SceanrioExecuteFlagColID;
	int sceanrioManager_SceanrioColID;
	int sceanrioManager_ExecuteTCsofStatusColID;
	int sceanrioManager_SceanrioRowCount;
	int sceanrioManager_BrowsersColID;
	
	//MainSheet variables Initializations
	String mainSheet_FlowIDColValue="";
	String mainSheet_TCIDColvalue="";
	String mainSheet_ExecuteFlagColValue="";
	String mainSheet_TCDescColValue="";
	String mainSheet_TCSceanrioColValue="";
	String mainSheet_TCCountColValue="";
	String mainSheet_ManualEffortColVal="";
	String mainSheet_TestSuitColVal="";
//	String mainSheet_TCStatusColVal="";
	String mainSheet_IterationColVal="";
	String mainSheet_LastRunFolderVal="";
	
	int mainSheet_TCIDCOlID			=	0;
	int mainSheet_FlowIDColID		=	0;
	int mainSheet_ExecuteFlagColID	=	0;
	int mainSheet_testCaseRowCount	=	0;
	int mainSheet_TCDescColID 		=	0;
	int mainSheet_TCScenarioColID	=	0;
	int mainSheet_TCCountColID 		= 	0;
	int mainSheet_ManEffortColID	=	0;
	int mainSheet_TestSuitColID 	= 	0;
	int mainSheet_TCStatusColID		=	0;
	int mainSheet_IterationColID	=	0;
	int mainSheet_lastRunFolderColID =	0;
	private String testCaseChromeStatusCond;
	private String testCaseMozillaStatusCond;
	private String testCaseIE11StatusCond;
	private String testCase10StatusCond;
	private String mainSheet_TCStatusChromeColVal;
	private String mainSheet_TCStatusMozillaColVal;
	private String mainSheet_TCStatusIE10ColVal;
	private String mainSheet_TCStatusIE11ColVal;
	private int mainSheet_TCStatusChromeColID;
	private int mainSheet_TCStatusMozillaColID;
	private int mainSheet_TCStatusIE11ColID;
	private int mainSheet_TCStatusIE10ColID;


			
	
	/**
	 * @param keywordName - Current keyword which needs to be executed eg vvLogin . Actual function name Login in applib.VV.keyword.java
	 * @return String FAIL/PASS based on the execution.
	 */
	public String executeKeywords(String keywordName) throws Exception {
		
		String keywordExecutionResult =  Globals.KEYWORD_FAIL;
		try{
	    	   	
			String platform =keywordName.split("\\.")[0].toLowerCase();
	    	String methodName = keywordName.split("\\.")[1];
	    	
	    	//Step1 - Using string funClass to convert to class format-appResource.cfa.appLib
	    	String funClass = "com.sterlingTS."+platform+".Keywords";
	    	Class<?> c = Class.forName(funClass);

	    	//Step2 - instantiate an object of the class above
	    	Object obj = c.newInstance();
	       	
	    	//Step 3 - create an object Method for the corresponding keyword
	    	Method getNameMethod  = obj.getClass().getMethod(methodName);
	    	
	    	//Step 4 - execute the method
	    	keywordExecutionResult = (String) getNameMethod .invoke(obj);
	    	
		} catch (Exception e){
			log.error("Class DriverScriptUtils | Method executeKeywords | Exception desc : "+e.toString());
			throw e;
	    }
		return keywordExecutionResult;
		
	}
	
	public void beforeSuiteExecutionActivities() throws Exception {
	
		//6/9 2016 - Adding Execution Level Global TimeStamps 
		Globals.Execution_Start_TimeStamp = new Date();
		//Summary result - TC execution time
		//Date tcStart_TimeStamp;
		//Date tcEnd_TimeStamp; 
		String currentUser;
		//From Email Id logic -Start
		Globals.MachineName = seleniumCommands.getLocalHostName();
		Globals.MachineUserName = seleniumCommands.getLoggedInUserFullName();
		if(Globals.EXECUTION_MACHINE.equalsIgnoreCase("jenkins"))
		{
			 currentUser = "GAT";
		}else{
			
			 currentUser = Globals.MachineUserName;
		}
//		String currentUser_EmailId=currentUser.replace(" ", ".") + "@sterlingts.com";
//		Globals.fromEmailID = currentUser_EmailId;
		//From Email ID logic-End
		
		// TODO-QAA-405- Workaround.Email address being generated varies depending upon the name and not necessarily based on first name and last name
        switch (currentUser)
        {
        case "Lakshmi Venkata Damodar Kumar Tanguturi":
               currentUser="lakshmivenkata.kumar@sterlingts.com";
               Globals.fromEmailID=currentUser;
               break;
               
        case "GAT": String currentUser_GATEmailID="GAT" + "@sterlingts.com";
        			Globals.fromEmailID = currentUser_GATEmailID;
        			break;
 
        default:
               String currentUser_EmailId=currentUser.replace(" ", ".") + "@sterlingts.com";
               Globals.fromEmailID = currentUser_EmailId;
               break;
        }
        
        //String logPropertiesPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"config"+File.separator+"Log4j.properties";

		
//		//PropertyConfigurator.configure("src/main/java/genResource/config/Log4j.properties");
//		PropertyConfigurator.configure(logPropertiesPath);
//		log= Logger.getLogger("VV");
		
		//celanup environment like killing processess etc
		seleniumCommands.cleanUpEnvironment();
		CommonHelpMethods cm = new CommonHelpMethods();
		CommonHelpMethods.mkDirs("jars");
		//CommonHelpMethods.mkDirs("jars"+File.separator+"refenceFile");
		log.info("jars Foalder is created and referenceFile foalder is also created");
		
		cm.unzipJarINFolder();
		cm.unzipFrameworkFile();
		
		Globals.TestDir = System.getProperty("user.dir"); //C:\STAF_Selenium\SeleniumFramework
		log.info("Test Dir path -"+Globals.TestDir);
		
		
		Globals.RunConfigPath = Globals.TestDir +File.separator+ "RunConfig.xlsx";
		log.info("RunConfig Excel Path -"+Globals.RunConfigPath);
		
		Globals.executionResultsPath= Globals.TestDir +File.separator+ "executionResults"+File.separator;
		
		Globals.runConfigXLS = new ExcelUtils(Globals.RunConfigPath);
        
		//Globals.loadURLs(Globals.Env_Name);
		Globals gb = new Globals();
		gb.loadOSSpecificFiles();
		Globals.loadExecutionSettings();
		Globals.loadGlobalTestData();
		seleniumCommands.setTimeZone();
        Globals.loadStaticTestDataConfig();
        LocatorAccess pload = new LocatorAccess();
        
       if ( Globals.PlatformName.equalsIgnoreCase("VV")){
    	   if(Globals.fromEmailID.equalsIgnoreCase("GAT@sterlingts.com")){
    		   Globals.fromEmailID="vvautomation@sterlingts.com";
    	   }
       }
        
        //Production credentials
        // Anand -  QAA-858 - If credentials are provided through command line arguments then below pop up should not be displayed.
        if(Globals.Env_To_Execute_ON.equalsIgnoreCase("ENV_PROD") && Globals.DISPLAY_CREDENTIAL_POPUP && (!( Globals.PlatformName.equalsIgnoreCase("VV") || Globals.PlatformName.equalsIgnoreCase("CFA")))  ){
			
        	char[] password;
        	String strPassword="";
    		JPanel panel = new JPanel();
    		JLabel labela = new JLabel("Enter username:");
    		JTextField uname = new JTextField(10);
    		panel.add(labela);
    		panel.add(uname);
    		
    		JLabel labelb = new JLabel("Enter password:");
    		JPasswordField pass = new JPasswordField(10);
    		panel.add(labelb);
    		panel.add(pass);
    		
    		//accepting credentials for first time
    		String[] options = new String[]{"Login", "Cancel"};
    		
    		int option = JOptionPane.showOptionDialog(null, panel, "Credentials For Prod environment",
    		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
    		                         null, options, options[1]);
    	
    		if(option == 0 ){ // user clicked on OK button
    			password = pass.getPassword();
    			strPassword =  new String(password);
    			String userName = uname.getText();
    			
    			if(userName.isEmpty() || strPassword.length()==0){ //2nd attempt
    				JOptionPane.showOptionDialog(null, panel, "Re-Enter Credentials For Prod environment",
                            JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[1]);	
    				
    				password = pass.getPassword();
    				strPassword =  new String(password);
    				if(uname.getText().isEmpty() || strPassword.length()==0){
    					if (Globals.SendEmails.equalsIgnoreCase("true")) {
    						String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
    						String ccList = Globals.StatusEmailCCList;
    						String subject = "***Selenium PROD Execution => User credentials not entered successfully *****";
    						String msgBody = "After 2 attempts, user has not provide credentials whoch is required for execution in Production Environment.";
    						Email.send(toList, ccList, subject, "html", msgBody);
    						
    					}
    					System.exit(0);
    				
    			
    				}else{
    				 Globals.STAFF_USER_PSSWORD=new String(password);
    				 Globals.STAFF_USERNAME=uname.getText();
    				}
    			
    		}else{
    			
    				Globals.STAFF_USER_PSSWORD=new String(password);
    				Globals.STAFF_USERNAME=userName;
    			}
    	
    			
    	
    		}else{
    			//2nd attempt - user clicked on Cancel Button
    			JOptionPane.showOptionDialog(null, panel, "Re-Enter Credentials For Prod environment",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[1]);	
    			
    			password = pass.getPassword();
    			strPassword =  new String(password);
    			if(uname.getText().isEmpty() || strPassword.length()==0){
    				if (Globals.SendEmails.equalsIgnoreCase("true")) {
						String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
						String ccList = Globals.StatusEmailCCList;
						String subject = "***Selenium PROD Execution => User credentials not entered successfully *****";
						String msgBody = "After 2 attempts, user has not provide credentials whoch is required for execution in Production Environment.";
						Email.send(toList, ccList, subject, "html", msgBody,Globals.RunConfigPath);
						
					}
    				System.exit(0);
    			
    		
    			}else{
    			 Globals.STAFF_USER_PSSWORD=new String(password);
    			 Globals.STAFF_USERNAME=uname.getText();
    			}
    			
    		}
        	
    	}
        
        //Result folder creation
        Globals.currentTimestampFolderName ="Run_"+seleniumCommands.getUniqueName_usingCurrTimestamp(); 
        Globals.currentRunPath = Globals.executionResultsPath+Globals.releaseName+"_"+Globals.ExecutionCycle+"_"+Globals.Env_To_Execute_ON+File.separator+ Globals.currentTimestampFolderName;
        seleniumCommands.mkDirs(Globals.currentRunPath);
        
    	//if release name is trial/test-No DB events would stored and email would be triggered to the user who is executing the script
    	if (Globals.releaseName.toLowerCase().contains("test") || Globals.releaseName.toLowerCase().contains("trial")){
    		Globals.EnterResultsInDB = false;
    		Globals.StatusEmailToList = Globals.fromEmailID;
    		Globals.StatusEmailCCList = Globals.fromEmailID;
    		Globals.StatusEmailBCCList = Globals.fromEmailID;
    		Globals.AG_IFN_FailureAlert_DL = Globals.fromEmailID;
    		Globals.StatusEmailOnlyStartAndComplete = Globals.fromEmailID;
    		
    	}
    	
    	//DB-CHANGES - Enter Release Details into DB
    	if(Globals.EnterResultsInDB){
    		Globals.automatonDB = new DAO(Globals.DBServerURL,Globals.DBServerUsername,Globals.DBServeruserPwd);
        	if(Globals.automatonDB.conn == null){
        		log.error(" Unable to connect to the Automation Database");
        		Globals.EnterResultsInDB = false;
        	}else{
        		Globals.ReleaseID = Globals.automatonDB.fetchID("ReleaseID",Globals.releaseName,Globals.CheckDBForReleaseNameSQL,Globals.InsertReleaseDetailsSQL);	
        	}
    		
        	
    	}else{
    		Globals.EnterResultsInDB = false;
    	}
    	
		//Platform Result Excel Creation - Start 
		Globals.Platform_Results_Excel_Path 	= Globals.currentRunPath +File.separator+"Platform_Summary.xlsx";
		Globals.Platform_Results_HTML_Path 		= Globals.currentRunPath +File.separator+"Platform_Summary.html";
		Globals.pfResultSheet =  new ExcelUtils(Globals.Platform_Results_Excel_Path,"scenarioResults");			

		Globals.pfResultSheet.addHeaders(Globals.pfResultHeaders);

		Globals.pfResultSheet.setColumnWidth(Globals.pfResulColumnNumbers, Globals.pfResulColumnWidth);
		//Platform Result Excel Creation - End 
		
		
	 
		
		 //Results Shared Path logic -Start
		String ShareName = Globals.currentRunPath.substring(Globals.currentRunPath.lastIndexOf(File.separator)+1, Globals.currentRunPath.length());
		seleniumCommands.shareFolder(ShareName,Globals.currentRunPath);
		 
//		String command= "net share "+ShareName+"="+Globals.currentRunPath;
//		Runtime.getRuntime().exec(command);
		Globals.SharedPath = "\\\\"+Globals.MachineName+File.separator+ShareName;			
		//Results Shared Path logic �End
		 
	}
	
	
	public String suiteExecution() throws Exception{
		 
		//sceanrioManager_PlatformColID				=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.PlatFormColName);
		sceanrioManager_TestSuiteColID				=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.TestSuiteColName);
		sceanrioManager_SceanrioExecuteFlagColID	=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.ExecuteFlagColName);
		
		sceanrioManager_SceanrioColID				=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.ScenarioColName);
		sceanrioManager_ExecuteTCsofStatusColID		=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.ExecuteTCs_ofStatusColName);
		sceanrioManager_BrowsersColID				=	Globals.runConfigXLS.getColumnIndex_fromScenarioManager(Globals.BROWSER_TYPE_COL_NAME);
		
		sceanrioManager_SceanrioRowCount 			= 	Globals.runConfigXLS.getRowCount(Globals.GlobalTestData,Globals.ScenarioRowOffset);
		sceanrioManager_SceanrioRowCount			=	sceanrioManager_SceanrioRowCount + Globals.ScenarioRowOffset;
		 
		
		Globals.scenarioManagerSheetList			= 	new ArrayList<String []>();
		String scenarioManager_rowNumber = "";
		
		if(Globals.suiteName != null)
		{
			// CI changes required for suite wise execution with if conditions
			String [] suiteNames = Globals.suiteName.split(",");
			for(int m=0;m<suiteNames.length;m++)
			{
				sceanrioManager_PlatformColValue 				= 	Globals.PlatformName;
				sceanrioManager_TestSuiteColValue 				= 	suiteNames[m];
				sceanrioManager_ScenarioColValue 				= 	"All";
				sceanrioManager_ExecuteTCsofStatusColValue 		= 	"All";
				sceanrioManager_BrowserColValue 				= 	"CHROME";
				sceanrioManager_ExecuteColValue 				= 	"Yes";
				scenarioManager_rowNumber 						= 	Integer.toString(10+m);
				
				String[] scenarioDetail = {sceanrioManager_PlatformColValue,sceanrioManager_TestSuiteColValue,sceanrioManager_ScenarioColValue,sceanrioManager_ExecuteColValue,sceanrioManager_ExecuteTCsofStatusColValue,scenarioManager_rowNumber,sceanrioManager_BrowserColValue};
				
				Globals.scenarioManagerSheetList.add(scenarioDetail);
			}
		}else{
		
		for (int sceanrioCounter = Globals.ScenarioRowOffset ; sceanrioCounter < sceanrioManager_SceanrioRowCount;sceanrioCounter++) { 							
			try {
				sceanrioManager_ExecuteColValue						= 	Globals.runConfigXLS.getCellData(sceanrioCounter, sceanrioManager_SceanrioExecuteFlagColID, Globals.ScenarioManager);
				
			
					if (sceanrioManager_ExecuteColValue.equalsIgnoreCase("Yes")){
						
						sceanrioManager_PlatformColValue 				= 	Globals.PlatformName;
						
						sceanrioManager_TestSuiteColValue 				= 	Globals.runConfigXLS.getCellData(sceanrioCounter, sceanrioManager_TestSuiteColID, Globals.ScenarioManager);
										
						sceanrioManager_ScenarioColValue 				= 	Globals.runConfigXLS.getCellData(sceanrioCounter, sceanrioManager_SceanrioColID, Globals.ScenarioManager);
						sceanrioManager_ExecuteTCsofStatusColValue	 	= 	Globals.runConfigXLS.getCellData(sceanrioCounter, sceanrioManager_ExecuteTCsofStatusColID, Globals.ScenarioManager);
						sceanrioManager_BrowserColValue	 				= 	Globals.runConfigXLS.getCellData(sceanrioCounter, sceanrioManager_BrowsersColID, Globals.ScenarioManager);
						scenarioManager_rowNumber = Integer.toString(sceanrioCounter) ;
						
						if(sceanrioManager_BrowserColValue.isEmpty() || sceanrioManager_BrowserColValue.equals("") || sceanrioManager_BrowserColValue == null){
							sceanrioManager_BrowserColValue = "CHROME";//default execution will be on Chrome browser
						}
						String[] scenarioDetail = {sceanrioManager_PlatformColValue,sceanrioManager_TestSuiteColValue,sceanrioManager_ScenarioColValue,sceanrioManager_ExecuteColValue,sceanrioManager_ExecuteTCsofStatusColValue,scenarioManager_rowNumber,sceanrioManager_BrowserColValue};
						
						Globals.scenarioManagerSheetList.add(scenarioDetail);
					}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		
		int scenario2BeExecutedCount = Globals.scenarioManagerSheetList.size();
		//check whether any scenario has been selected for execution
		if(scenario2BeExecutedCount == 0){
			log.error("************************ NO SCENARIO SELECTED FOR EXECUTION*****");
			if (Globals.SendEmails.equalsIgnoreCase("true")) {
				String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
				String ccList = Globals.StatusEmailCCList;
				String subject = "***Selenium Execution => Stopped . Username - "+ Globals.MachineUserName +" *****";
				String msgBody = "Execution has stopped as NO TEST SCENARIO have been selected for Execution in the ExecutionControlDetails sheet.<br><br><br>Please find the RunConfig excel attached for refernce.<br><br><br>";
				Email.send(toList, ccList, subject, "html", msgBody,Globals.RunConfigPath);
				throw new Exception("Execution has stopped as No Test Scenario have been selected for Execution in the ScenarioManager sheet");
			}
			
						
		}else{
			for ( int i=0;i<scenario2BeExecutedCount;i++){
				
				try {
				
					sceanrioManager_PlatformColValue 			=	Globals.scenarioManagerSheetList.get(i)[0];
					sceanrioManager_TestSuiteColValue 			=	Globals.scenarioManagerSheetList.get(i)[1];
					sceanrioManager_ScenarioColValue			=	Globals.scenarioManagerSheetList.get(i)[2];
					sceanrioManager_ExecuteColValue				=	Globals.scenarioManagerSheetList.get(i)[3];
					sceanrioManager_ExecuteTCsofStatusColValue 	= 	Globals.scenarioManagerSheetList.get(i)[4];
					sceanrioManager_BrowserColValue				= 	Globals.scenarioManagerSheetList.get(i)[6];
					
					if ( sceanrioManager_ExecuteColValue.equalsIgnoreCase("Yes")){
						
						
						Globals.CurrentPlatform = sceanrioManager_PlatformColValue.trim().toUpperCase();
						Globals.MainSheet = Globals.CurrentPlatform + "_MainSheet"; // sheet :VV_MainSheet
										
						Globals.ScenarioManger_currentRow = Integer.parseInt(Globals.scenarioManagerSheetList.get(i)[5]);
						
						
						log.info(sceanrioManager_PlatformColValue + " Execution is selected | Scenario - " + sceanrioManager_TestSuiteColValue );
						
						

						Globals.currentTestSuiteColValue = sceanrioManager_TestSuiteColValue;
						Globals.currentScenarioColValue = sceanrioManager_ScenarioColValue;
						Globals.currentExecuteTCsofStatusColValue = sceanrioManager_ExecuteTCsofStatusColValue;
						//Globals.Path_DataEngine	= Globals.TestDir +File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"dataExcel";
						//Globals.Path_TestData = Globals.Path_DataEngine +File.separator+Globals.CurrentPlatform+"_TestData.xlsx";
						Globals.Path_TestData = Globals.RunConfigPath;
						//Globals.Path_TestData = Globals.TestDir +File.separator+"RunConfigTest.xlsx";
						//PropertyLoader pload = new PropertyLoader(propertiFiles);	
						//fetch String[] of OR properties file that has to be loaded
						//pload.LoadORFiles(propertiFiles);
						//System.out.println("DSU address is "+"\t"+pload);
						

				    	//Platform Test Data
						//Globals.testSuiteXLS = new ExcelUtils(Globals.Path_TestData);
						Globals.testSuiteXLS = Globals.runConfigXLS;
						Globals.mainSheetTCList 	= 	new ArrayList<String []>();
						
						
						mainSheet_TCIDCOlID				=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.TestCaseIDColName);
						mainSheet_FlowIDColID			=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.FlowIDColName);
						mainSheet_ExecuteFlagColID		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.ExecuteFlagColName);
						mainSheet_testCaseRowCount		=	Globals.runConfigXLS.getRowCount(Globals.MainSheet);
						mainSheet_TCDescColID 			=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.TestCaseDescrColName);
						mainSheet_TCScenarioColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.TestCaseSceanrioColName);
						mainSheet_TCCountColID 			=	Globals.runConfigXLS.getColumnIndex_fromMainSheet(Globals.TCCountColName);
						mainSheet_ManEffortColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("ManualEffort");
						mainSheet_TestSuitColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("TestSuite");
						mainSheet_TCStatusChromeColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("Status_Chrome");
						mainSheet_TCStatusMozillaColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("Status_Mozilla");
						mainSheet_TCStatusIE11ColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("Status_IE11");
						mainSheet_TCStatusIE10ColID 		=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("Status_IE10");
						mainSheet_lastRunFolderColID 	=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("LastRunFolder");
						
						if(Globals.CurrentPlatform.equalsIgnoreCase("MIPS") || Globals.CurrentPlatform.equalsIgnoreCase("AG_IFN")  || Globals.CurrentPlatform.equalsIgnoreCase("PRISM")){
							mainSheet_IterationColID 	=	Globals.runConfigXLS.getColumnIndex_fromMainSheet("Iteration");
						}
						String testSuiteCond 		=	"";
						String testScenarioCond		= 	"";
						
						
						for (int j =1 ; j < mainSheet_testCaseRowCount;j++) { 							
								try {
									mainSheet_ExecuteFlagColValue	=	Globals.runConfigXLS.getCellData(j, mainSheet_ExecuteFlagColID, Globals.MainSheet);
									mainSheet_FlowIDColValue		=	Globals.runConfigXLS.getCellData(j, mainSheet_FlowIDColID, Globals.MainSheet);
									mainSheet_TCIDColvalue 			= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCIDCOlID, Globals.MainSheet);
									mainSheet_TCDescColValue 		= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCDescColID, Globals.MainSheet);
									mainSheet_TCSceanrioColValue 	= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCScenarioColID, Globals.MainSheet);
									mainSheet_TCCountColValue 		= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCCountColID, Globals.MainSheet);
									mainSheet_ManualEffortColVal 	= 	Globals.runConfigXLS.getCellData(j, mainSheet_ManEffortColID, Globals.MainSheet);
									mainSheet_TestSuitColVal 		= 	Globals.runConfigXLS.getCellData(j, mainSheet_TestSuitColID, Globals.MainSheet);
									mainSheet_TCStatusChromeColVal 	= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCStatusChromeColID, Globals.MainSheet);
									mainSheet_TCStatusMozillaColVal = 	Globals.runConfigXLS.getCellData(j, mainSheet_TCStatusMozillaColID, Globals.MainSheet);
									mainSheet_TCStatusIE11ColVal 	= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCStatusIE11ColID, Globals.MainSheet);
									mainSheet_TCStatusIE10ColVal 	= 	Globals.runConfigXLS.getCellData(j, mainSheet_TCStatusIE10ColID, Globals.MainSheet);
									mainSheet_LastRunFolderVal 		=   Globals.runConfigXLS.getCellData(j, mainSheet_lastRunFolderColID, Globals.MainSheet);
									
									if(Globals.CurrentPlatform.equalsIgnoreCase("MIPS") || Globals.CurrentPlatform.equalsIgnoreCase("AG_IFN")  || Globals.CurrentPlatform.equalsIgnoreCase("PRISM") ){
										mainSheet_IterationColVal 	=	Globals.runConfigXLS.getCellData(j, mainSheet_IterationColID, Globals.MainSheet);
										
									}
									
									if (!Globals.currentTestSuiteColValue.equalsIgnoreCase("Custom")){
										if (Globals.currentTestSuiteColValue.equalsIgnoreCase("All")){
											testSuiteCond = mainSheet_TestSuitColVal;
										}else{
											testSuiteCond = Globals.currentTestSuiteColValue;
										}
										
										if(Globals.currentScenarioColValue.equalsIgnoreCase("All")){
											testScenarioCond = mainSheet_TCSceanrioColValue;// this is set to ignore test scenario condition check as this will always be true
										}else{
											testScenarioCond = Globals.currentScenarioColValue ;
										}
										
										if(Globals.currentExecuteTCsofStatusColValue.contains("All")){
											testCaseChromeStatusCond 	= mainSheet_TCStatusChromeColVal;
											testCaseMozillaStatusCond 	= mainSheet_TCStatusMozillaColVal;
											testCaseIE11StatusCond 		= mainSheet_TCStatusIE11ColVal;
											testCase10StatusCond 		= mainSheet_TCStatusIE10ColVal;
										}else{
											testCaseChromeStatusCond 	= Globals.currentExecuteTCsofStatusColValue;
											testCaseMozillaStatusCond 	= Globals.currentExecuteTCsofStatusColValue;
											testCaseIE11StatusCond 		= Globals.currentExecuteTCsofStatusColValue;
											testCase10StatusCond 		= Globals.currentExecuteTCsofStatusColValue;
										}
										
										
										if(mainSheet_TestSuitColVal.equalsIgnoreCase(testSuiteCond) &&
												mainSheet_TCSceanrioColValue.equalsIgnoreCase(testScenarioCond) &&
												(mainSheet_TCStatusChromeColVal.equalsIgnoreCase(testCaseChromeStatusCond) || 
												mainSheet_TCStatusMozillaColVal.equalsIgnoreCase(testCaseMozillaStatusCond) || 
												mainSheet_TCStatusIE11ColVal.equalsIgnoreCase(testCaseIE11StatusCond) || 
												mainSheet_TCStatusIE10ColVal.equalsIgnoreCase(testCase10StatusCond) )){
											String[] row = {mainSheet_ExecuteFlagColValue,mainSheet_TCSceanrioColValue,mainSheet_TCIDColvalue,mainSheet_FlowIDColValue,mainSheet_TCDescColValue,mainSheet_TCCountColValue,mainSheet_ManualEffortColVal,mainSheet_TCSceanrioColValue,mainSheet_TestSuitColVal,mainSheet_IterationColVal,mainSheet_LastRunFolderVal,mainSheet_TCStatusChromeColVal,mainSheet_TCStatusMozillaColVal,mainSheet_TCStatusIE11ColVal,mainSheet_TCStatusIE10ColVal};
											Globals.mainSheetTCList.add(row);
											
										}
									
									}else{
										// CI required for suite option custom and testcase description
										if(Globals.testCaseID != null && !Globals.testCaseID.equalsIgnoreCase("no"))
										{
											List<String> testCaseIDList = new ArrayList<String>();
											String [] testcaseIds = Globals.testCaseID.split(",");
											for(int l=0;l<testcaseIds.length;l++)
											{
												testCaseIDList.add(testcaseIds[l]);
											}
											if(testCaseIDList.contains(mainSheet_TCIDColvalue))
											{
												String[] row = {mainSheet_ExecuteFlagColValue,mainSheet_TCSceanrioColValue,mainSheet_TCIDColvalue,mainSheet_FlowIDColValue,mainSheet_TCDescColValue,mainSheet_TCCountColValue,mainSheet_ManualEffortColVal,mainSheet_TCSceanrioColValue, mainSheet_TestSuitColVal,mainSheet_IterationColVal,mainSheet_LastRunFolderVal,mainSheet_TCStatusChromeColVal,mainSheet_TCStatusMozillaColVal,mainSheet_TCStatusIE11ColVal,mainSheet_TCStatusIE10ColVal};
												Globals.mainSheetTCList.add(row);
											}
										}else{
											if (mainSheet_ExecuteFlagColValue.equalsIgnoreCase("Yes")){
												String[] row = {mainSheet_ExecuteFlagColValue,mainSheet_TCSceanrioColValue,mainSheet_TCIDColvalue,mainSheet_FlowIDColValue,mainSheet_TCDescColValue,mainSheet_TCCountColValue,mainSheet_ManualEffortColVal,mainSheet_TCSceanrioColValue, mainSheet_TestSuitColVal,mainSheet_IterationColVal,mainSheet_LastRunFolderVal,mainSheet_TCStatusChromeColVal,mainSheet_TCStatusMozillaColVal,mainSheet_TCStatusIE11ColVal,mainSheet_TCStatusIE10ColVal};
												Globals.mainSheetTCList.add(row);
											}
										}
									}
									
									
							
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
						//check whether there are test case with ExecuteFlag=Yes.Hence size would be greater than 0
						int tc2BeExecutedCount = Globals.mainSheetTCList.size();
						//check whether any scenario has been selected for execution
						if(tc2BeExecutedCount == 0){
							log.error("********"+Globals.CurrentPlatform +" NO Test Case SELECTED FOR EXECUTION  *****");
							if (Globals.SendEmails.equalsIgnoreCase("true")) {
								String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
								String ccList = Globals.StatusEmailCCList;
								String subject = "***Selenium Execution => Stopped.Platform["+Globals.CurrentPlatform+"] Username - "+ Globals.MachineUserName +" *****";
								String msgBody = "Execution has stopped as NO TEST CASE have been selected for Execution in the "+Globals.CurrentPlatform +"_MainSheet<br>OR the scenario selected for execution doesnt have any test cases associated with it.<br><br>Please find the RunConfig excel attached for refernce<br><br><br>";
								Email.send(toList, ccList, subject, "html", msgBody,Globals.RunConfigPath);
								if(i == scenario2BeExecutedCount-1){
									return Globals.KEYWORD_FAIL;
								}else{
									continue;
								}
								
							}
						}
						
						if(!Globals.releaseName.toLowerCase().contains("test") && !Globals.releaseName.toLowerCase().contains("trial")){
			
							if(Globals.StatusEmailToList == null || Globals.StatusEmailToList.equalsIgnoreCase(""))
							{
								Globals.StatusEmailToList = Globals.GAT_Email_DL;
							}
						}
						
						Globals.BrowserList = 	sceanrioManager_BrowserColValue;
						
						Email.sendExecutionStartAlert();
						
						String[] browsersToBeExecuted = sceanrioManager_BrowserColValue.split(";");
						int browserCount =  browsersToBeExecuted.length;
						log.info(  " Number of browsers to be executed  = "+browserCount + " ---"+sceanrioManager_BrowserColValue);
						
						for (String currentBrowser : browsersToBeExecuted) {
							//12 Feb 2016 - Adding Scenario Level Global TimeStamps 
							Globals.Scenario_Start_TimeStamp = new Date();
							Globals.TEST_CASE_EXECUTED_COUNTER = 0;
							Globals.dummyPFStaus ="";
							Globals.BrowserType = currentBrowser;
							Globals.TC_OUTPUT_VAL = "";
							Globals.tcSummaryResultSheet_lastRowNumber =1; // need to reset this value to 1 podt each scenario execution
							
							try{
								
								Globals.pfResultSheet.pfResultSheet_addCurrentPFDetails(sceanrioManager_PlatformColValue, sceanrioManager_TestSuiteColValue, sceanrioManager_ScenarioColValue, sceanrioManager_ExecuteTCsofStatusColValue,currentBrowser);
//								DB-CHANGES
//								The below code will add an entry in SceanrioExecutionDetails
								if(Globals.EnterResultsInDB){
									 
									Globals.PlatformID 	= 	Globals.automatonDB.fetchID("PlatformID",Globals.CurrentPlatform,Globals.FetchPlatformIDSQL,Globals.InsertPlatformDetailsSQL);
									Globals.TestSuiteID =	Globals.automatonDB.fetchID("TestSuiteID",Globals.currentTestSuiteColValue,Globals.FetchTestSuiteIDSQL,Globals.InsertTestSuiteDetailsSQL);
									Globals.EnvID 		= 	Globals.automatonDB.fetchID("EnvID",Globals.Env_To_Execute_ON.split("_")[1],Globals.FetchEnvIDSQL,Globals.InsertEnvDetailsSQL);
									Globals.automatonDB.insertNewTestSceanrioDetails();
								}
//TODO - Need to add Browser column for scenario execution
									
									//Scenario level Summary Excel File for Current Browser
							    	Globals.currentPlatform_Path = Globals.currentRunPath+File.separator+Globals.CurrentPlatform +File.separator+ currentBrowser;
									Globals.currentPlatform_ExcelPath= Globals.currentPlatform_Path+File.separator+"Excel_Results";
							    	Globals.currentPlatform_HTMLPath = Globals.currentPlatform_Path+File.separator+"HTML_Results";
							    	Globals.currentPlatform_SnapshotPath = Globals.currentPlatform_Path+File.separator+"Snapshots";
							    	seleniumCommands.mkDirs(Globals.currentPlatform_Path);
							    	seleniumCommands.mkDir(Globals.currentPlatform_ExcelPath);
							    	seleniumCommands.mkDir(Globals.currentPlatform_HTMLPath);
							    	seleniumCommands.mkDir(Globals.currentPlatform_SnapshotPath);
							    	
									//12 Feb 2016 - Scenario Level Sheet Paths.  
									Globals.tcSummaryResult_Excel_Path 		= 	Globals.currentPlatform_ExcelPath +File.separator+Globals.currentTestSuiteColValue+"_"+Globals.currentScenarioColValue+".xlsx";
									Globals.tcSummaryResult_HTML_Path		= 	Globals.currentPlatform_HTMLPath +File.separator+Globals.currentTestSuiteColValue+"_"+Globals.currentScenarioColValue+".html";
									Globals.tcSummaryResultSheet 			=  	new ExcelUtils(Globals.tcSummaryResult_Excel_Path,"tcResults");			
									Globals.tcSummaryResultSheet.addHeaders(Globals.summaryHeaders);
									
									//Summary Result - resize Column width
									Globals.tcSummaryResultSheet.setColumnWidth(Globals.summaryColumnNumbers, Globals.summaryColumnWidth);
									
									
									String statusColVal = "";
									
									for (int tcCounter = 0; tcCounter < Globals.mainSheetTCList.size(); tcCounter++) {
										try{
											
											mainSheet_ExecuteFlagColValue 	=	Globals.mainSheetTCList.get(tcCounter)[0];
											if(!Globals.currentExecuteTCsofStatusColValue.contains("All")){
												
												if(currentBrowser.equalsIgnoreCase("chrome")){
													statusColVal = Globals.mainSheetTCList.get(tcCounter)[11];
													
												}else if(currentBrowser.equalsIgnoreCase("mozilla")){
													statusColVal = Globals.mainSheetTCList.get(tcCounter)[12];
													
												}else if(currentBrowser.equalsIgnoreCase("ie11")){
													statusColVal = Globals.mainSheetTCList.get(tcCounter)[13];
													
												}else if(currentBrowser.equalsIgnoreCase("ie10")){
													statusColVal = Globals.mainSheetTCList.get(tcCounter)[14];
													
												}
												
												if(!Globals.currentExecuteTCsofStatusColValue.equalsIgnoreCase(statusColVal)){
													continue;// if the status of Browser column is not same as that of selected in RunConfig, do not execute the test case
												}
											}
											
											Globals.TEST_CASE_EXECUTED_COUNTER=Globals.TEST_CASE_EXECUTED_COUNTER+1;
											
											//this is done to reset the platform status as Fail so that it would contain Pass status only when all TCs have executed successfully
											Globals.pfExecutionStatus = Globals.KEYWORD_FAIL;
											Globals.tcExecutionStarted = 0;
											Globals.testScenario 		= Globals.mainSheetTCList.get(tcCounter)[1];
											Globals.TestCaseID 			= Globals.mainSheetTCList.get(tcCounter)[2];
											Globals.tcDescription 		= Globals.mainSheetTCList.get(tcCounter)[4];
											
											Globals.ManualTCCount 		= (int) Double.parseDouble(Globals.mainSheetTCList.get(tcCounter)[5]);
											Globals.tcManualEffort 		= Globals.mainSheetTCList.get(tcCounter)[6];
											Globals.Iteration 			= Globals.mainSheetTCList.get(tcCounter)[9];
											Globals.LastRunFolder		= Globals.mainSheetTCList.get(tcCounter)[10];
											//DB-CHANGES
											if(Globals.EnterResultsInDB){
												Globals.TestCaseStatusID = Globals.automatonDB.fetchID("StatusID","NORUN",Globals.FetchStatusIDSQL,Globals.InsertStatusDetailsSQL);
												Globals.automatonDB.insertNewTestCaseDetails();
											}
											
											
																			
											tcStartActivities(tcCounter);
											
											APP_LOGGER.startTestCase(mainSheet_TCIDColvalue);
											
											tcExecution();
											
											APP_LOGGER.endTestCase(mainSheet_TCIDColvalue);
												
											
									}catch(Exception e ){
											
											log.error("Skipping to Next test case. | Exception occurrred - "+e.toString());

											//21 Mar 2016 - Printing the Exception Details - Even if Staf reporter 	- Start
											//continue;//Any exceptions, the iteration is incremented and the remaining code is not executed for that test case
							            	  try{
													int iLastErrMethodPosition = 0;
								            	  	
								                    
							            			System.out.println("Exception TO String" + e.toString());	
								                    System.out.println("Exception TO String" + e.getMessage());	
							                    	System.out.println("Exception Cause TO String" + e.getCause().toString());
								                    System.out.println("Exception Cause Message" + e.getCause().getMessage());
								                   
								                    if(e.toString().contains("Fail_And_Exit")){
									                	  iLastErrMethodPosition = 1; // will Only give lAst Called one
								                  	}else if(e.toString().contains("caused")){
								                  		int cpos = e.toString().indexOf("caused");
								                  		System.out.println("Casued By :- " + e.toString().substring(cpos));
								                  	}
								                  	
								                    //29 MAr 2016 - Adding Condition for Nested Exception
								                  	Throwable strMainError = e.getCause();
								                  	String strLastErrMethodDetails = "";
								                  	if (strMainError != null){
									                  	strLastErrMethodDetails 				= strMainError.toString();
								                  	}else{
									                    StackTraceElement arrStackTraceElements[] 	= e.getStackTrace();
									                    strLastErrMethodDetails 					= "Error :- ***" + e.toString() + "***" ;
									                    strLastErrMethodDetails 					= "In The Method :- ***" + arrStackTraceElements[iLastErrMethodPosition].getMethodName() + "***" ;
									                    strLastErrMethodDetails 					= strLastErrMethodDetails + " *** Present In Class :- ***" + arrStackTraceElements[iLastErrMethodPosition].getClassName() + "***" ;
									                    strLastErrMethodDetails 					= strLastErrMethodDetails + " *** At Line Number :- ***" + arrStackTraceElements[iLastErrMethodPosition].getLineNumber() + "***" ;
								                  	}
					
								                  	System.out.println(strMainError);
								                  	System.out.println("strLastErrMethodDetails" + strLastErrMethodDetails);
					
								                  	seleniumCommands.STAF_ReportEvent("Fail", "Exception Occurred - in TC", "Exception : - " + strLastErrMethodDetails, 0);
					
							            	  }catch(Exception LogException){ 
							            		  System.out.println("Exception Occurred while Recovery Logging. Exception :- " + e.toString());
							            		  //GenUtils.STAF_ReportEvent("Fail", "Exception Occurred - in TC", "Exception : - " + strLastErrMethodDetails, 0);
							            	  }	  
							            	  tcExitActivities();
							            	  continue;
							                  	//Any exceptions, the iteration is incremented and the remaining code is not executed for that test case
										}
//										finally{ //29 MAr 2016 - Moving tcExitActivities in Finally. 
											tcExitActivities();
//										}
				                    	//21 Mar 2016 - Printing the Exception Details - Even if Staf reporter 	- End
									
										
									}//End of test case loop
									Globals.pfResultSheet.pfResultSheet_addCurrentPFStatus();
								
						
							//Update status in Sceanrio Manager sheet
							Globals.runConfigXLS.updateTCStatus_ScenarioManager();
						
							}catch(Exception e){
								log.info( " Error occurred during execution on Browser - " + currentBrowser + e.toString());
								e.printStackTrace();
								continue;
							}
							//seleniumCommands.closeAllBrowsers();
							
							//12 Feb 2016 - Adding Scenario Level Execution Time - Need to MOve FIle Path Inside function
							try {
								Globals.Scenario_End_TimeStamp 		= new Date();
								Globals.Scenario_Execution_Time		= seleniumCommands.getTotalMinsDiff(Globals.Scenario_Start_TimeStamp, Globals.Scenario_End_TimeStamp);
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
						
							
							//12-May-2016 --Copy Testdata to Results Location
					    	File srcFile = new File(Globals.Path_TestData);
					    	File destDir = new File(Globals.currentPlatform_Path);
					    	try {
								FileUtils.copyFileToDirectory(srcFile, destDir);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					    	

							// 12 - Feb 2016 - Scenario Html Generation - Need to MOve Html Inside function
							//String Sce_HTML_FileName 	= "C://TestFile//Sce_HTML_FileName.html";
							String Sce_HTML_FileName 	= Globals.currentPlatform_HTMLPath;
							//Globals.currentPlatform_HTMLPath = currentPlatform_Path+"\\"+"HTML_Results";
//							String Sce_HTML_retvalFile 	= HtmlUtils.STAF_ExportResultSummaryToHtml(Sce_HTML_FileName);
							HtmlUtils.STAF_ExportResultSummaryToHtml(Sce_HTML_FileName);
//							System.out.println("From Test Execution - Retrieved = " + Sce_HTML_retvalFile );
							
							//Email Current platform Status email  
							try {
								Email.sendExecutionCompleteAlert();
							} catch (Exception e) {
								e.printStackTrace();				
							}
						}
						
						
					}
					
				}	catch(Exception e){
					e.printStackTrace();
					log.info("Skipping to Next Test Scenario in ScenarioManager.Exception - "+ e.toString()+" Occurred for Scenarion Row - "+i);
					
				}
				
				
				
			}
		}
		return Globals.KEYWORD_PASS;
		
	}
	
	
	public void afterSuiteExecutionActivities() throws IOException, InterruptedException{
		
		//6/9 Anand - Adding Execution Level Execution Time
		if(Globals.MaintainBrowserSession.equalsIgnoreCase("Yes"))
		{
			driver = getDriverConnection();
			seleniumCommands sc = new seleniumCommands(driver);
		}
		try {
			Globals.Execution_End_TimeStamp 		= new Date();
			Globals.Execution_Execution_Time		= seleniumCommands.getTotalMinsDiff(Globals.Execution_Start_TimeStamp, Globals.Execution_End_TimeStamp);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
			// 17 - Feb 2016 - Platform (All Rows of Scenario Sheet ) - HTML Generation - Need to MOve FIle Path Inside function
			String Platform_HTML_FileName 		= Globals.Platform_Results_HTML_Path;
//			String Platform_HTML_retvalFile 	= HtmlUtils.STAF_ExportSceanrioResultsToHtml(Platform_HTML_FileName);
			HtmlUtils.STAF_ExportSceanrioResultsToHtml(Platform_HTML_FileName);
//			System.out.println("From Test Execution - Retrieved = " + Platform_HTML_retvalFile );
			
			//Here we need to perform Exit activities 
			String destinationDirectory = System.getProperty("user.dir")+File.separator+"Run";
			FileUtils.deleteDirectory(new File(destinationDirectory));
			File srcDir = new File(Globals.currentRunPath);
			File destDir = new File(destinationDirectory);
			FileUtils.copyDirectory(srcDir, destDir);
			
			//Modified - Launching the Report Automatically in Chrome as results are not supported in IE driver,its supported on desktop IE though
			if(!Globals.releaseName.contains("AG UAT")){
				seleniumCommands.launchURL("chrome",Platform_HTML_FileName);
				String statusReport = Globals.suiteRunStatus;
				Globals.log.info("Automation Status is "+statusReport);
				Thread.sleep(10000);
				seleniumCommands.closeAllBrowsers();
			}else{
				seleniumCommands.closeAllBrowsers();
				
			}
			
		
		}	
	
	public void tcExitActivities(){
		
		try{
			Globals.tcEnd_TimeStamp = new Date();
			if(Globals.TEST_CASE_EXECUTED_COUNTER != 0){
				Globals.tcExecution_Time = seleniumCommands.getTotalMinsDiff(Globals.tcStart_TimeStamp, Globals.tcEnd_TimeStamp);
				
				//CLose TC Results excel obj 
				Globals.tcResultSheet.closeExcel();
				//Add TC status in Summary 
				Globals.tcSummaryResultSheet.tcSummaryResultSheet_addTCDetails();
				
				//update Status column in Platform Mainsheet in test Data as well as in runconfig
				//fetch the column index for RunConfig main Sheet
				
				String columnName="";
				
				if(Globals.BrowserType.equalsIgnoreCase("chrome")){
					columnName = "Status_Chrome";
				}else if(Globals.BrowserType.equalsIgnoreCase("mozilla")){
					columnName = "Status_Mozilla";
				}else if(Globals.BrowserType.equalsIgnoreCase("ie11")){
					columnName = "Status_IE11";
				}else if(Globals.BrowserType.equalsIgnoreCase("ie10")){
					columnName = "Status_IE10";
				}
				int statusColID = Globals.runConfigXLS.getColumnIndex_fromMainSheet(columnName);
							
				int lastRunFolderColID = Globals.runConfigXLS.getColumnIndex_fromMainSheet("LastRunFolder");
				
				//the below code ensures that projects which do not have last run column, result folder should not be updated
				if(lastRunFolderColID != -1 && lastRunFolderColID != 999 ){
					Globals.testSuiteXLS.updateTCStatus_MainSheet(Globals.MainSheet,statusColID,lastRunFolderColID);
					Globals.runConfigXLS.updateTCStatus_MainSheet(Globals.MainSheet,statusColID,lastRunFolderColID);
				}
				
			}
			

			
			
			
			
			
			//DB-CHANGES
			if(Globals.EnterResultsInDB){
				String tcStatus = Globals.tcExecutionStatus.toUpperCase();
				Globals.TestCaseStatusID = Globals.automatonDB.fetchID("StatusID",tcStatus,Globals.FetchStatusIDSQL,Globals.InsertStatusDetailsSQL);
				Globals.automatonDB.updateTestCaseDetails();
			}

			
			//TC Completion Email
			Email.sendTCExecutionCompleteAlert();
			
				
			// 12 - Feb 2016 - TC Html Generation   - Need to MOve FIle Path Inside function
			String TC_HTML_FileName 	= Globals.TC_Results_HTML_Path;
//			String TC_HTML_retvalFile 	= HtmlUtils.Create_TC_Html_Report(TC_HTML_FileName);
			HtmlUtils.Create_TC_Html_Report(TC_HTML_FileName);
//			System.out.println("From Test Execution - Retrieved = " + TC_HTML_retvalFile );
		} catch (Exception e) {
			log.error("Method -tcExitActivities.Exception occurred- "+ e.toString());
		}
		
	}
	
	public static void testSuiteExecution()
	{
		DriverScriptUtils dutil = new DriverScriptUtils();
		try{
			dutil.beforeSuiteExecutionActivities();
        
        String retval = Globals.KEYWORD_FAIL; 
        retval = dutil.suiteExecution();
        
        if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
        	dutil.afterSuiteExecutionActivities();
        }
		}catch(Exception ie)
		{
			ie.printStackTrace();
			log.error("Suite has not been executed properly ...");
		}
	}

	
	

	public void tcExecution() throws Exception {
				
		try{
			driver = getDriverConnection();
			seleniumCommands sc = new seleniumCommands(driver);
			List<String> keywordsToBeExecuted = Globals.testSuiteXLS.fetchAllBuisnessFlowKeyword(); // this will fetch All Keywords from the BuisnessFlow based on FlowID
			
			String testDataFetchRetval=Globals.testSuiteXLS.fetchAllTestData_forTCID(); //create global hashmap for test data for the current Test case so that we dont need to refer to the excel for test data columns
			
			if(testDataFetchRetval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				log.error("Unable to fetch test data for Test case id - "+ Globals.TestCaseID);
				//throw netw Exception();
				throw new RuntimeException();
			}
			
			/* Loop to execute the keywords
			keywords in BuisnessFlow sheet would be in the form say vvLogin.
			The keyword is then truncated of the platform which has been prefixed and 
			then searched in aaplib.platform.keyword.java for the method's implementation
		 	*/	
			String keywordResult = Globals.KEYWORD_PASS;
			Globals.tcStart_TimeStamp = new Date();
			
			for(String currentKeyword : keywordsToBeExecuted){
				//the keywords are only executed if Constants.KeywordExecutionStatus is Pass i.e previous keyword has successfully executed
				if (keywordResult.equalsIgnoreCase(Globals.KEYWORD_PASS)) {
					
					APP_LOGGER.startKeywordExecution(currentKeyword);
					System.out.println("---Keyword To be Executed ---"+currentKeyword);
					
					// Added the current keyword as the component name. User can override this whenever necessary.
					Globals.Component = currentKeyword;
					keywordResult = executeKeywords(currentKeyword);// this method will invoke the corresponding method's implementation 
					
					System.out.println(" Keyword status - "+keywordResult);
					
					APP_LOGGER.endKeywordExecution(currentKeyword);
				}
				
				
			}
			
			if(Globals.MaintainBrowserSession.equalsIgnoreCase("Yes"))
			{
				sc.closeAllBrowsers();
			}
		}catch(Exception e ){
			log.error("Skipping to Next test case. | Exception occurrred - "+e.toString());
			e.printStackTrace();

			try 
		    { 
				driver.quit();
				
				Alert alert = driver.switchTo().alert(); 
				alert.accept(); 
				
				throw e; //31st Mar 2016
		    }   // try 
		    catch (Exception Ex) 
		    { 
		    	log.error("Browser Exited-No Alert is present");
				 //31st Mar 2016
		    }  
			
			finally{
				driver = null;
				
			}
			
			throw e;
			
		}
		
		
	}
	
	public void tcStartActivities(int tcCounter) throws InvalidFormatException, IOException{
		
		seleniumCommands.removeAllKeys_fromHashmap(Globals.EmailParam);
		
		mainSheet_TCSceanrioColValue 	=	Globals.mainSheetTCList.get(tcCounter)[1];
		mainSheet_TCIDColvalue			=	Globals.mainSheetTCList.get(tcCounter)[2];
		mainSheet_FlowIDColValue		=	Globals.mainSheetTCList.get(tcCounter)[3];
		mainSheet_TCDescColValue		=	Globals.mainSheetTCList.get(tcCounter)[4];
		
		Globals.FlowID					=	mainSheet_FlowIDColValue;
		Globals.TestCaseID 				= 	mainSheet_TCIDColvalue;
		
		Globals.tcDescription 			= 	mainSheet_TCDescColValue; //added the below line which is being used for reporting
		Globals.testScenario 			= 	mainSheet_TCSceanrioColValue;
		
				
		//Test Case Results code 
		Globals.tcExecutionStatus = Globals.KEYWORD_FAIL;
		Globals.tcResultSheet_lastRowNumber = 1; 
		
		//11 Feb 2016 - Adding the String the TC Result Path to Global Path  
		Globals.TC_Results_Excel_Path 	= Globals.currentPlatform_ExcelPath +File.separator+ Globals.TestCaseID + ".xlsx";
		Globals.TC_Results_HTML_Path	= Globals.currentPlatform_HTMLPath +File.separator+ Globals.TestCaseID + ".html";	
		//Globals.tcResultSheet =  new ExcelUtil.s(Globals.currentPlatform_ExcelPath+"\\"+Globals.TestCaseID+".xlsx","stepResults");			
		
		Globals.tcResultSheet = new ExcelUtils(Globals.TC_Results_Excel_Path,"stepResults");			
		Globals.tcResultSheet.addHeaders(Globals.tcHeaders);
		
		//Testcase Result - resize Column width
		int[] ColumnNumbers = {0,1,2,3,4,5,6};
		int[] ColumnWidth = {7000,7000,7000,15000,3000,4600,5000};
		Globals.tcResultSheet.setColumnWidth(ColumnNumbers , ColumnWidth);
		
		if(Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("AG_IFN")){ 
			seleniumCommands.loadColumns_MIPS();
			seleniumCommands.fetchIterationRange(Globals.testSuiteXLS,Globals.TestData);
		}
		
		if(Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("mips") || Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("prism")){ // result folder creation and excel generation for MIPS
			Globals.MIPS_IntegrationResultFolder = Globals.currentPlatform_Path+File.separator+"Integration_Results";
			seleniumCommands.mkDirs(Globals.MIPS_IntegrationResultFolder);
			String srcFilePath ="";
			
			if(Globals.testScenario.equals("PVT"))
			{
				String XLSfilePath = System.getProperty("user.dir")+File.separator+  Globals.tcDescription +".xls";

				// XLS converted output file path here
				String XLSXfilePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+ "refFile"+File.separator+"TD_"+ Globals.testScenario +File.separator+ Globals.tcDescription +".xlsx";

				// calling Convert Files method
				ExcelUtils.covertXlsToXlsx(XLSfilePath, XLSXfilePath);
				
				Globals.MIPS_ResultSheetName = "Sheet1";
			}
						
			if(Globals.LastRunFolder.isEmpty() || Globals.LastRunFolder.equals("") || Globals.LastRunFolder == null){
				srcFilePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"refFile"+File.separator+"TD_"+ Globals.testScenario +File.separator+ Globals.tcDescription +".xlsx";
								
			}else{
				srcFilePath = Globals.LastRunFolder +File.separator+"Integration_Results"+File.separator+ Globals.tcDescription +".xlsx";
			}
			
			seleniumCommands.copyFileTo(srcFilePath, Globals.MIPS_IntegrationResultFolder);
			Globals.MIPS_IntegrationResultExcelPath = Globals.MIPS_IntegrationResultFolder +File.separator+ Globals.tcDescription +".xlsx";
			
			Globals.mipsResultXLS = new ExcelUtils(Globals.MIPS_IntegrationResultExcelPath);
			seleniumCommands.loadColumns_MIPS();
				
			
								
			if(Globals.testScenario.equals("PVT"))
			{
				CommonHelpMethods.fetchIterationRange(Globals.mipsResultXLS,"Sheet1");
			}else{
				CommonHelpMethods.fetchIterationRange(Globals.mipsResultXLS,"Test_Data");
			}
		}
	}
}

