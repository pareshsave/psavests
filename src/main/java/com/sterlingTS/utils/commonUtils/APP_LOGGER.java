package com.sterlingTS.utils.commonUtils;

import org.apache.log4j.Logger;


public class APP_LOGGER  {
	
	protected static Logger log = Logger.getLogger(APP_LOGGER.class);


// This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
public static void startTestCase(String sTestCaseName){

	log.info("****************************************************************************************");
	log.info("****************************************************************************************");
	log.info("$$$$$$$$$$$$      Start of of Test Case Execution ---"+sTestCaseName+ "     $$$$$$$$$$$$");
	log.info("****************************************************************************************");
	log.info("****************************************************************************************");

   }

//This is to print log for the ending of the test case
public static void endTestCase(String sTestCaseName){
	log.info("****************************************************************************************");
	log.info("****************************************************************************************");
	log.info("$$$$$$$$$$$$        End  of Test Case Execution ---"+sTestCaseName+ "     $$$$$$$$$$$$   ");
	log.info("****************************************************************************************");
	log.info("****************************************************************************************");
   }

public static void startKeywordExecution(String keywordName){
	log.info(">>>>>>>>  Start of Keyword ---"+keywordName+ " >>>>>>>>");
	   }
public static void endKeywordExecution(String keywordName){
	log.info("<<<<<<<< End of Keyword ---"+keywordName+ "  <<<<<<<<");
	   }

public static  void startFunction(String sFunctionName){
//	log.debug("*******   Start of Function ---"+sFunctionName+ "   *******");
	   }
public static void endFunction(String sFunctionName){
	   
//	Globsals.log.debug("*******   End of Function ---"+sFunctionName+ "   *******");
	   
	   }

// Need to create these methods, so that they can be called  


}