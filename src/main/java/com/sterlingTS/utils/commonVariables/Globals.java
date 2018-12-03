package com.sterlingTS.utils.commonVariables;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonUtils.DriverScriptUtils;
import com.sterlingTS.utils.commonUtils.ExcelUtils;
import com.sterlingTS.utils.commonUtils.ProtectedEncryption;
import com.sterlingTS.utils.commonUtils.database.DAO;
import com.sterlingTS.utils.config.configProperty;


public class Globals {
	
		public static Logger log = Logger.getLogger(Globals.class);
	//This is the list of System Variables
    //Declared as 'public', so that it can be used in other classes of this project
    //Declared as 'static', so that we do not need to instantiate a class object
    //Declared as 'final', so that the value of this variable can be changed
    // 'String' & 'int' are the data type for storing a type of value	
		public static boolean DISPLAY_CREDENTIAL_POPUP = true;
		public static String EXECUTION_MACHINE = "CurrentUser";
		public static String TC_OUTPUT_VAL = "";
		public static String suiteName = null;
		public static String testCaseID = null;
		public static final String GAT_Email_DL = "Ramesh.Tahiliani@sterlingts.com";
		public static final String GAT_Email_Signature = "<i>Sincerely,<br>Global Automation Team</i><br><br><br>";
		//Framework Folder variables
		public static String TestDir=""; //C:\STAF_Selenium\SeleniumFramework
		
		//Encryption Keys
		public static String password = "GAT@sterlingts.com";
		public static byte[] salt = new String("12345678").getBytes();
		public static int iterationCount = 40000;
		public static int keyLength = 128;
		
		//public static SecretKeySpec key = ProtectedEncryption.createSecretKey(password.toCharArray(),salt, iterationCount, keyLength);

		
		//Run Config Excel Path
	    public static String RunConfigPath ="";// "C:\\STAF_Selenium\\SeleniumFramework\\RunConfig.xlsx";
	    public static String PlatformName="";
		
		//browser drivers exe paths
	    public static String ChromeDriverEXEPath = ""; //"C:\\STAF_Selenium\\SeleniumFramework\\browserDrivers\\chromedriver.exe";
	    public static  String IEDriverEXEPath = "";
	    public static String FIREFOXEXEPath="";
	    public static String FileDownloaderEXEPath="";
	    
	    public static String VerifypopupAndHandleViewDownloadEXEPath="";
	    public static String SaveFileEXEPath="";
	    
		public static String SWest_DBServerName = "";
		public static String SWest_DBServerPort = "";
		public static final String VV_DBServerName = "QATLSQL29";
		public static final String VV_DBServerPort = "55297";
		public static String VV_DBName="";
		
		
		public static  String sqlAuthDLLPath= "";
		
	
		// TestData and OR file path
		public static String Path_TestData = "";
		public static String VV_InhouseApp_OR ="";
		public static String CFA_OR = "";
		public static String CI_OR = "";
		public static String MIPS_OR = "";
		public static String AG_OR = "";
		public static String Path_DataEngine="";
		public static String MaintainBrowserSession = "";
		//WebDriver object
		public static WebDriver driver;
		//Logger object
		//public static Logger log;
		
		public static ExcelUtils runConfigXLS;//run config reference object
		public static ExcelUtils testSuiteXLS; // platform test data reference object
		public static ExcelUtils mipsResultXLS; 
		
		//src packages global variables
		public static final String AppLib = "appLib";
 
	    //Constants variables for the Pass results & Fail result
	    public static final String KEYWORD_FAIL = "FAIL";
	    public static final String KEYWORD_PASS = "PASS";
	    public static final int INT_FAIL = 999;
	    
	    //var to pass whether events should be reported to customized reports
	    public static final boolean Report = true; 
	    
	    public static final int GlobalDetailsRowOffset = 4;
	    public static final int ScenarioRowOffset = 10;
		
		//Test data sheet and column names
		public static final String GlobalTestData = "ExecutionControlDetails"; // store the GlobalTestData sheet name
		public static final String ScenarioManager = GlobalTestData; //store the Scenario Manager sheet name
		public static final String Field_Name="Field_Name";
		public static final String Field_Value="Field_Value";
		public static final String Type_Value="Type";
		
		public static String MainSheet = ""; // store the main sheet name of RunConfig XL
		public static String BusinessFlow = "BusinessFlow"; // store the Business flow sheet name
		public static String TestData = "TestData"; //stores the test data sheet name
		
		public static final String BROWSER_TYPE_COL_NAME="BrowserType";
		public static final String TestCaseIDColName = "TestCaseID"; // store the TestCaseID column name
		public static final String FlowIDColName = "TC_FLOW_ID"; //stores the FlowID column name
		public static final String MainSheet_ExecuteFlagColname = "ExecuteFlag";
		public static final String PlatFormColName= "Project";
		public static final String TestSuiteColName="TestSuite";
		public static final String ScenarioColName="Scenario";
		public static final String ExecuteFlagColName="ExecuteFlag";
		public static final String ExecuteTCs_ofStatusColName="ExecuteTCs_ofStatus";
		public static final String TestCaseDescrColName = "TestCaseDescr";
		public static final String TestCaseSceanrioColName =  "TestScenarios";
		public static final String TCCountColName =  "ManualTCcount";
		public static final String StatusColName =  "Status";
		public static String LastRunFolder =  "";
		public static String suiteRunStatus = "";
		
		public static int IterationStartValue= -1;
		public static int IterationEndValue= -1;
		public static String currentTestSuiteColValue="";
		public static String currentScenarioColValue="";
		public static String currentExecuteTCsofStatusColValue="";		
		
		public static String fromEmailID = "";
		
		// VV Specific sheet names
		public static final String VV_MainSheet = "VV_MainSheet"; // store the main sheet name
		public static final String VV_BusinessFlow = "VV_BusinessFlow"; // store the Business flow sheet name
		public static final String VV_TestData = "VV_TestData"; //stores the test data sheet name
		
		//MIPS
		public static String MIPS_IntegrationResultFolder="";
		public static String MIPS_IntegrationResultExcelPath="";
		public static ArrayList<Integer> MIPS_TestDataToBeExecuted ;
		public static final String HIT ="HIT";
		public static final String CLEAR = "CLEAR";
		public static final String ERROR = "ERROR";
		public static String MIPS_ResultSheetName = "Test_Data";
		public static int MIPS_FNAME_COL_INDEX;
		public static int MIPS_LNAME_COL_INDEX;
		public static int MIPS_MIDNAME_COL_INDEX;
		public static int MIPS_DOB_COL_INDEX;
		public static int MIPS_SSN_COL_INDEX;
		public static int MIPS_CourtName_COL_INDEX;
		public static int MIPS_Region_COL_INDEX;
		public static int MIPS_County_COL_INDEX;
		public static int MIPS_Screening_Type_COL_INDEX;
		public static int MIPS_Vendor_COL_INDEX;
		public static int MIPS_Edited_LastName_COL_INDEX;
		public static int MIPS_Edited_FirstName_COL_INDEX;
		public static int MIPS_Formated_DOB_COL_INDEX;
		public static int MIPS_WebSiteResults_COL_INDEX;
		public static int MIPS_UtilityResults_COL_INDEX;
		public static int MIPS_TestCaseResults_COL_INDEX;
		public static int MIPS_OrderID_COL_INDEX;
		public static int MIPS_URLResults_COL_INDEX;
		public static int MIPS_CurrentTestDataRow;
		public static int MIPS_Expected_Utility_Result_COL_INDEX;
		
		//AG-IFN
		public static int AG_TestCaseDescr_COL_INDEX;
		public static int AG_SWestOrderID_COL_INDEX;
		public static int AG_OrderedDate_COL_INDEX;
		public static int AG_LastName_COL_INDEX;
		public static int AG_FirstName_COL_INDEX;
		public static int AG_SSN_COL_INDEX;
		public static int AG_DOB_COL_INDEX;
		public static int AG_AddressLine1_COL_INDEX;
		public static int AG_City_COL_INDEX;
		public static int AG_State_COL_INDEX;
		public static int AG_ZIP_COL_INDEX;
		public static int AG_Exp_CaseNumber_COL_INDEX;
		public static int AG_Exp_Charges_DOB_COL_INDEX;
		public static int AG_SALARY_COL_INDEX;
		public static int AG_MIDNAME_COL_INDEX;
		public static int AG_Jurisdiction_COL_INDEX;
		public static int AG_packageName_COL_INDEX;
		public static int AG_Max_Timeout_inHrs_COL_INDEX;
		public static int AG_CurrrentOrderCreationStatus_COL_INDEX;
		public static int AG_POSITIONID_COL_INDEX;
	    //Below variables are to be used as global variables
		public static final String VV = "VV";
	    public static String FlowID = null; // used for storing current FlowID
		public static String TestCaseID = null; // used for storing current test case id
		public static String CurrentPlatform = "";
		public static String Iteration =""; // stores value from the Iteration column for MIPS platform only
		
		//RowID for Test Case in TestData sheet
		public static int TestDataRowID;
		
		//the below var would be used to store the status of execution of the keyword in BusinessFlow sheet.
		//By default this is set to Pass.Once keywords are executed, this value will be modified accordingly.
		public static String KeywordExecutionStatus = KEYWORD_PASS;
		
		public static HashMap<String, String> TestDataCollection =  new HashMap<String, String>(); //HashMaps can store ONLY objects and CANNOT store primitive datatypes
		public static HashMap<String, String> OutputParam =  new HashMap<String, String>();
		public static HashMap<String, String> EmailParam =  new HashMap<String, String>();
		//Results code 
	    public static final String KEYWORD_WARNING = "WARNING";
	    
	    //11 Feb 2016 - Making TC TimeStamps as Global
  		public static Date tcStart_TimeStamp;
  		public static Date tcEnd_TimeStamp;
  		
  		//12 Feb 2016 - Adding Scenario Level Global TimeStamps and Execution Time
  		public static Date Scenario_Start_TimeStamp;
  		public static Date Scenario_End_TimeStamp;
  		public static String Scenario_Execution_Time = "";
  		
  		//Result Headers details 
  		public static final int[] summaryColumnNumbers 		= {0,1,2,3,4,5,6};
  		public static final  int[] summaryColumnWidth 		= {7000,2500,20000,2500,2000,2000,2000};
  		public static final int[] pfResulColumnNumbers = {0,1,2,3,4,5};
  		public static final int[] pfResulColumnWidth = {5000,5000,8000,7000,8000,3000};
  		public static final String[] pfResultHeaders 	= {"Platform","TestSuite","Scenario","ExecuteTCs_ofStatus","BrowserType","Status"};
  		public static final String[] summaryHeaders 	= {"Test_Scenario","TC_ID","TC_Description","TC_Status","ExecutionTime_Minutes","ManualEffort","AutomationSavings","ManualTCCount","OUTPUT"};
  		public static final String[] tcHeaders 			= {"Flow_ID","Component","Step_Name","Step_Description","Step_Result","TimeStamp","Snapshot_Path"};
  		
//  		/6/9-Anand- Adding execution time global vars
  		public static Date Execution_Start_TimeStamp;
  		public static Date Execution_End_TimeStamp;
  		public static String Execution_Execution_Time = "";
  		
  		public static ExcelUtils tcResultSheet;
  		public static int tcResultSheet_lastRowNumber = 1;
  		public static String tcExecutionStatus = KEYWORD_PASS;
  		public static String pfExecutionStatus = KEYWORD_PASS;
  		public static String dummyPFStaus=""; 
  		public static int tcExecutionStarted = 0;
//  		public static int pfExecutionInProgressFlag; // used for over pf overall status 
  		public static String Component = ""; // used for storing current Component
	    
  		public static ExcelUtils tcSummaryResultSheet;
  		public static int tcSummaryResultSheet_lastRowNumber = 1;
  		public static String testScenario = "";
  		public static String tcDescription= "";
  		
  		public static ExcelUtils pfResultSheet;
  		public static int pfResultSheet_lastRowNumber = 1;  		
  		
  		public static String MachineName ="";
  		public static String MachineUserName ="";
  		
  		public static String tcExecution_Time= "";
  		public static String tcManualEffort= "";
  		public static String tcAutomationSavings= "";
  		
  		public static String executionResultsPath= "";
  		public static String currentPlatform_Path="";
  		public static String currentPlatform_ExcelPath = "";
  		public static String currentPlatform_HTMLPath = "";
  		public static String currentPlatform_SnapshotPath = "";
  		
  		// 11 Feb 2016 - Adding variables for TC Level Results paths
  		public static String TC_Results_Excel_Path 	= "";
  		public static String TC_Results_HTML_Path 	= "";
  		
  		//12 Feb 2016  - Adding variables for Scenario Level Results paths
  		public static String tcSummaryResult_Excel_Path 	= "";
  		public static String tcSummaryResult_HTML_Path 	= "";
  		
  		//17 Feb 2016  - Adding variables for Platform Level Results paths
  		public static String Platform_Results_Excel_Path 	= "";
  		public static String Platform_Results_HTML_Path 	= "";
  		
  		//23 Feb 2016 - Count variables as GLobal
	  	public static int gblintPassCounter 				= 0;
	  	public static int gblintFailCounter 				= 0;
		//int intNoRunCounter							= 0;
	  	public static int gblintWarningCounter				= 0;
	  	public static double gbldblTotManualEffort			= 0;
	  	public static double gbldblTotalExecTime 			= 1;
	  	public static double gbldblTotAutomationSavings	= 0;
	  	public static int TEST_CASE_EXECUTED_COUNTER=0;

	  	public static int TotalTCPASSED 				= 0;
	  	public static int TotalTCFAILED 				= 0;
	  	public static int TotalTCNORUN 				= 0;
	  	public static int TotalTCWARNING 				= 0;
	  	
  		public static int ScenarioManger_currentRow= 0;
  		public static String Result_Link_FilePath="";// this var is used to link filePaths to step reporter events in results
  		// End of Results
		
  		
	  	public static ArrayList<String[]> scenarioManagerSheetList;
  		public static ArrayList<String[]> mainSheetTCList;
  		public static String currentRunPath;
  		public static String SharedPath;
		
		// Global test Data run config variables
	    public static String releaseName = "";
	    public static String ExecutionCycle = "";
	    public static String Env_To_Execute_ON = "";
	    public static String BrowserType = "";
	    public static String BrowserList = "";
	    public static String WebAdminBrowserType = "";
	    public static String SendEmails = "";
	    public static String SendStatusEmail_ForTCStatus = "";
	    public static String SendEmailAlert_ForStepStatus = "";
	    public static String StatusEmailToList = "";
	    public static String StatusEmailBCCList = GAT_Email_DL;
	    public static String VV_StatusEmailToList = "";
	    public static String CI_StatusEmailToList = "";
	    public static String MIPS_StatusEmailToList = "";
	    public static String AG_IFN_StatusEmailToList = "";
	    public static String AG_IFN_FailureAlert_DL="";
	    public static String StatusEmailCCList = "";
	    public static String StatusEmailOnlyStartAndComplete = "";
	    public static String CaptureScreenShotForDoneStatus = "";
	    public static String setTimeZoneTo = "";
	    public static String AdminClientExeName = "";
	    public static String AdminClientExePath = "";
	    public static String ScreeningEditionURL = "";
	    public static String AdminClientUserName = "";
	    public static String AdminClientPassword = "";
	    public static String VV_InHouseURL = "";
	    public static String VV_AppURL = "";
//	    public static String VV_Staff_Username = "";
//	    public static String VV_Staff_Password = "";
	    public static String STAFF_USERNAME="";
	    
	    public static String STAFF_USER_PSSWORD= "";
	    
	    public static String MIP_URL="";
	    public static String PRISM_URL1="";
	    public static String PRISM_URL2="";
	    public static String PRISM_UID="";
	    public static String PRISM_PWD="";
	    public static String VV_BatchProcessing_URL = "";
	    public static String VV_APIOrderresponse = "";
	    public static String AG_URL = "";
	    public static String AG_UserName = "";
	    public static String AG_Password = "";
	    public static String AG_INTG_Account = "";
	    public static String AG_INTG_SoapServer = "";
	    public static String AG_INTG_UserName = "";
	    public static String AG_INTG_UserPwd = "";
	    public static String AG_INTG_TPAccount = "";
	    public static String AG_INTG_TP_Username = "";
	    public static String AG_INTG_TP_UserPWD = "";

	    //Selenium Config properties
        public static long gPageLoadTimeout=180;
        public static long gImplicitlyWait=20;
        public static long gPollingInterval=2;
        public static long gScrollOffset=1000;
        
//      DB-CHANGES--------Start of DB variables
        public static DAO automatonDB=null; 
        public static final String DBServerURL ="jdbc:sqlserver://DATLQAAUTO01\\AUTODASHBOARD;databaseName=automationExecutionResults;"; // to use windows authentication include the following code- integratedSecurity=true;"; 
        public static final String DBServerUsername="stafdbuser";
        public static final String DBServeruserPwd="a1s2d3f4@1234";
        public static final String ServerScreenshotPath = "\\\\DATLQAAUTO01\\ExecutionSnapshots\\";
        public static boolean EnterResultsInDB = true;
        public static String currentTimestampFolderName ="";
        public static int ReleaseID = 1;
        public static int PlatformID = 1;
        public static int TestSuiteID = 1;
        public static int TestCaseStatusID;
        public static int EnvID;
        public static int ManualTCCount = 0;
        public static int ExecutionID = 0;
        public static int ScenarioID = 1;
        
        public static final String InsertReleaseDetailsSQL = "INSERT INTO ReleaseDetails(ReleaseName ) VALUES (?)";
        public static final String CheckDBForReleaseNameSQL = "Select * from ReleaseDetails where ReleaseName =?";
        
        public static final String FetchPlatformIDSQL = "Select * from PlatformDetails where PlatformName =?";
        public static final String InsertPlatformDetailsSQL = "INSERT INTO PlatformDetails(PlatformName ) VALUES (?)";
        
        public static final String FetchTestSuiteIDSQL = "Select * from TestSuiteDetails where TestSuiteName =?";
        public static final String InsertTestSuiteDetailsSQL = "INSERT INTO TestSuiteDetails(TestSuiteName ) VALUES (?)";
        
        public static final String FetchStatusIDSQL = "Select * from StatusDetails where StatusName =?";
        public static final String InsertStatusDetailsSQL = "INSERT INTO StatusDetails(StatusName ) VALUES (?)";
        
        public static final String FetchEnvIDSQL = "Select * from EnvDetails where EnvName =?";
        public static final String InsertEnvDetailsSQL = "INSERT INTO EnvDetails(EnvName ) VALUES (?)";
        
        public static final String InsertTCDetailsSQL ="INSERT INTO TestCaseDetails(ScenarioID,TCID,TCDescription,"
				+ "TestScenario,ManualTCCount,StatusID,ExecutionTimestamp)"
				+" VALUES (?,?,?,?,?,?,?)";
        
        public static final String SelectExecutionIDFromTCSQL = "Select ExecutionID FROM TestCaseDetails "
				+ "WHERE ExecutionTimestamp=? AND ScenarioID=? AND TCID=? "
				+ "AND StatusID=? ORDER BY ExecutionID ASC";
        
        public static final String UpdateTCStatusSQL = "UPDATE TestCaseDetails SET"
				+ " StatusID=?,"
				+ "ManualExecutionTime=?,"
				+ "AutomationExecutionTime=? "
				+ "WHERE ExecutionID=?";
        
        public static final String InsertTestStepDetailsSQL = "INSERT INTO TestStepDetails(ExecutionID,FlowID,"
				+ "StepName,StepDescription,StatusID,ExecutionTimestamp,OutputFilePath)"
				+" VALUES (?,?,?,?,?,?,?)";
        
        
        public static final String InsertSceanrioDetailSQL ="INSERT INTO ScenarioExecutionDetails(ReleaseID,PlatformID,EnvID,"
				+ "TestSuiteID,TestScenario,ExecutionCycle,MachineName,UserName,AutomationTool,ExecutionTimestamp)"
				+" VALUES (?,?,?,?,?,?,?,?,?,?)";
        
        public static final String SelectSceanrioDetailsSQL = "Select ScenarioID FROM ScenarioExecutionDetails "
				+ "WHERE ExecutionTimestamp=? AND ReleaseID=? AND PlatformID=? "
				+ "AND ExecutionCycle=? AND MachineName =? AND EnvID=? ORDER BY ScenarioID ASC";
        
        
//        ------------End of DB Variables
        public static String currentPlatform_XMLResults = "";
     	//xml
	  	public static String vvInputReferenceXML = "";
    		
    		    //VV Specific Global variables
	    public static String VV_SoapServer = "";
	    public static String VV_API_UserName = "";
	    public static String VV_API_UserPwd = "";
    	public static String currentAPIOrderRequestPath = "";	
    	public static String VV_InvitationTitle = "Invitation Title";
    	public static String VV_VolunterInstructions = "Test Instructions for Volunteer";
    	public static String VV_PollURL ="";
    	public static final String Volunteer_DL_Name = "test -'DL";
    	public static final String Volunteer_City="Test City";
    	public static final String Volunteer_AddressLine="test address !@#$%^&*() ':`Line";
    	public static final String Volunteer_Phone = "9012121234";
    	public static final String Volunteer_AliasName = "alias '-Name";
    	public static final String Volunteer_Gender = "Male";
    	public static final String  Volunteer_ReferenceEmail = "referenceTesting@sterlingts.com";
    	public static final String  Volunteer_ReferenceRelationship = "Colleague's- Friend";
    	public static boolean Volunteer_MVRAuthorized = true;
    	public static final long VV_Report_MaxTimeout = 120;
    	
    	//AbsHire,VV username and password
    	public static final String ABSHIRE_USERNAME = "vvautomation";
    	public static final String ABSHIRE_PASSWORD = "Qa34uT0mAt1onP@$5";
    	public static final String VV_USERNAME = "MercuryWeb";
    	public static final String VV_PASSWORD = "Mz\\6Iy`16";
    	
    		 //Load StaticTestData Properties
        public static Properties staticTestDataConfig ;  
        
        public static void loadGlobalTestData() throws IOException{
           
            Properties selConfig = new Properties();
		    FileInputStream selCFile = new FileInputStream("GlobalTestData.properties");
		    selConfig.load(selCFile);
		    selCFile.close();
		    
	 
		    PlatformName            =    selConfig.getProperty("PlatformName");
		    log.info("Global test Data | ApplicationName - "+ PlatformName);
 
		    StatusEmailOnlyStartAndComplete = selConfig.getProperty("StatusEmail_OnlyStartAndComplete");
		    log.info("Global test Data | StatusEmail_OnlyStartAndComplete - "+ StatusEmailOnlyStartAndComplete);
		    
    	    StatusEmailToList           =   selConfig.getProperty("StatusEmailToList");
    	    log.info("Global test Data | StatusEmailToList - "+ StatusEmailToList);
    	    
    	    AG_IFN_FailureAlert_DL 	= 	selConfig.getProperty("AG_IFN_FailureAlert_DL");
    	    log.info("Global test Data | AG_IFN AG_IFN_FailureAlert_DL - "+ AG_IFN_FailureAlert_DL);
    	    
    	    StatusEmailCCList 			= 	selConfig.getProperty("StatusEmailCCList");
    	    log.info("Global test Data | StatusEmailCCList - "+ StatusEmailCCList);
    	   
    	    CaptureScreenShotForDoneStatus = selConfig.getProperty("CaptureScreenShotForDoneStatus");
    	    log.info("Global test Data | CaptureScreenShotForDoneStatus - "+ CaptureScreenShotForDoneStatus);
   	    
    	    setTimeZoneTo 				= 	selConfig.getProperty("setTimeZoneTo");
    	    log.info("Global test Data | setTimeZoneTo - "+ setTimeZoneTo);
    	    
    	    MaintainBrowserSession      =   selConfig.getProperty("MaintainBrowserSession");
    	    log.info("Global test Data | MaintainBrowserSession - "+ MaintainBrowserSession);
     	    
     	    String absHireDB 			= 	selConfig.getProperty(Env_To_Execute_ON+"_"+"Swest_DBServer_Port");
    	    if(absHireDB ==  null || absHireDB.isEmpty() || absHireDB.equals("") ){
    	    	log.error("Global test Data | absHireDB is NULL. Please check the value in GlobalTestData properties file");
    	    }else{
    	    	 SWest_DBServerName 		= 	absHireDB.split(",")[0];
         	    SWest_DBServerPort 			= 	absHireDB.split(",")[1];
    	    }
    	     VV_DBName 						=	selConfig.getProperty(Env_To_Execute_ON+"_"+"VV_DBName");
    	    
    	    SendEmails 					= selConfig.getProperty("SendEmails").trim();
    	    log.info("Global test Data | SendEmails - "+ SendEmails);
    	    
    	    SendStatusEmail_ForTCStatus = selConfig.getProperty("SendStatusEmail_ForTCStatus");
    		if(SendStatusEmail_ForTCStatus == null){
    			SendStatusEmail_ForTCStatus="All";
    		}else{
    			SendStatusEmail_ForTCStatus.trim();
    		}
    	    
    	    log.info("Global test Data | SendStatusEmail_ForTCStatus - "+ SendStatusEmail_ForTCStatus);
    	    
    	    SendEmailAlert_ForStepStatus = selConfig.getProperty("SendEmailAlert_ForStepStatus");
    	    if(SendEmailAlert_ForStepStatus == null){
    	    	SendEmailAlert_ForStepStatus="Fail";
    		}else{
    			SendEmailAlert_ForStepStatus.trim();
    		}
    	    log.info("Global test Data | SendEmailAlert_ForStepStatus - "+ SendEmailAlert_ForStepStatus);
    	    
    	    String storeResultsInDb = selConfig.getProperty("StoreResultsInDB");
    	    if(storeResultsInDb.trim().equalsIgnoreCase("No") || storeResultsInDb.trim().equalsIgnoreCase("false") ){
    	    	EnterResultsInDB = false;
    	    }
        }

        
	    //Loading Global App Setting
        /**
         * @throws Exception 
         * 
         */
        public static void loadExecutionSettings() throws Exception{
        	
        	int RowNum = GlobalDetailsRowOffset;
        	int colOffset = 2;
        	releaseName = runConfigXLS.getCellData(RowNum, colOffset++, Globals.GlobalTestData);
        	log.info("Global test Data | releaseName - "+ releaseName);
    	    
        	ExecutionCycle = runConfigXLS.getCellData(RowNum, colOffset++, Globals.GlobalTestData);
    	    log.info("Global test Data | ExecutionCycle - "+ ExecutionCycle);
    	    
    	    if(!Globals.EXECUTION_MACHINE.equalsIgnoreCase("jenkins"))
    	    {
	    	    Env_To_Execute_ON = "ENV_"+runConfigXLS.getCellData(RowNum, colOffset++, Globals.GlobalTestData);
	    	    Globals.loadURLs(Env_To_Execute_ON);
	    	    log.info("Global test Data | Env_To_Execute_ON - "+ Env_To_Execute_ON);
    	    }else{
    	    	if(Globals.Env_Name!=null)
    	    	{
    	    		Env_To_Execute_ON = Globals.Env_Name;
    	    		Globals.loadURLs(Globals.Env_Name);
    	    		log.info("Global test Data | Env_To_Execute_ON - "+ Globals.Env_Name);
    	    	}else{
    	    		log.error("Please provide Enviornment details ...");
    	    	}
    	    }
    	    
//    	    BrowserType = runConfigXLS.getCellData(RowNum,colOffset++, Globals.GlobalTestData);
//    	    log.info("Global test Data | BrowserType - "+ BrowserType);

    	       	    
    	        	    
        }

     
        

        public static void loadStaticTestDataConfig() throws Exception {
        	
        	staticTestDataConfig = new Properties();
 		    FileInputStream staticTestDataFile = new FileInputStream(Globals.TestDir +File.separator+ "src"+File.separator+"test"+File.separator+"Resources"+File.separator+"config"+File.separator+"StaticTestData.properties");
 		    staticTestDataConfig.load(staticTestDataFile);
 		    staticTestDataFile.close();
        }
        
        public void loadOSSpecificFiles() throws IOException{
        	//unzip browserDriver.zip file
    		String browserDriverPath = Globals.TestDir +File.separator+"jars"+File.separator+"browserDrivers";
    		seleniumCommands.unzip(System.getProperty("user.dir")+File.separator+"jars"+File.separator+"refenceFile"+File.separator+ "browserDrivers.zip", Globals.TestDir +File.separator+"jars");
    		//unzip AutoITscript.zip file
    		String autoITscriptPath = Globals.TestDir +File.separator+ "src"+File.separator+"test"+File.separator+"Resources"+File.separator+"refFile";
    		seleniumCommands.unzip(autoITscriptPath +File.separator+ "FileDownloader.zip", autoITscriptPath);
    		Globals.FileDownloaderEXEPath=autoITscriptPath +File.separator+"FileDownloader"+File.separator+"FileDownloader.exe";
    		Globals.VerifypopupAndHandleViewDownloadEXEPath=autoITscriptPath +File.separator+"FileDownloader"+File.separator+"VerifypopupandHandleViewDownload.exe";
    		Globals.SaveFileEXEPath=autoITscriptPath +File.separator+"FileDownloader"+File.separator+"Savefile.exe";
    		
    		Globals.ChromeDriverEXEPath=browserDriverPath +File.separator+"chromedriver.exe";
    		Globals.FIREFOXEXEPath = browserDriverPath +File.separator+"geckodriver.exe";
    		log.info("Chrome Driver path -"+Globals.ChromeDriverEXEPath);
    		String baseSQLPath = Globals.TestDir +File.separator+ "jars"+File.separator;
    		String sqlDllZipPath="";
    		if(seleniumCommands.isMachine64Bit()){ // wrong ie driver version hampers execution and screenshot abilities
    			baseSQLPath = baseSQLPath + "Microsoft_JDBC_Driver";
    			Globals.sqlAuthDLLPath =    baseSQLPath+File.separator+"sqljdbc_auth.dll";
    			Globals.IEDriverEXEPath = 	browserDriverPath +File.separator+"IEDriverServer32.exe";
    			sqlDllZipPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"refenceFile"+File.separator+"sqljdbc_auth_64.zip";
      		}else{
      			baseSQLPath = baseSQLPath + "Microsoft_JDBC_Driver_32";
      			Globals.sqlAuthDLLPath =    baseSQLPath +File.separator+"sqljdbc_auth.dll";
      			Globals.IEDriverEXEPath = 	browserDriverPath +File.separator+"IEDriverServer32.exe";
      			sqlDllZipPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"refenceFile"+File.separator+"sqljdbc_auth_32.zip";
      		}
    		
    		seleniumCommands.unzip(sqlDllZipPath, baseSQLPath);
    		
        }
        
        
        
        public static String Env_Name = "";

        public static Map urlMaps;
        //public static Map dbMaps;
        
        public static void loadURLs(String env_Value)
        {
        	urlMaps = configProperty.getPropertiesMap(env_Value);
        }
//        public static void loadDBProps(String db_Value)
//        {
//        	dbMaps = configProperty.getPropertiesMap(db_Value);
//        }
        
        public static String getEnvPropertyValue(String propertyKey)
        {
        	return urlMaps.get(propertyKey).toString();
        }
        
//        public static String getDBEnvPropertyValue(String propertyKey)
//        {
//        	return dbMaps.get(propertyKey).toString();
//        }
        
          
}
