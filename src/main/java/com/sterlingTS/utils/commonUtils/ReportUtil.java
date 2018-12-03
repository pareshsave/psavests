package com.sterlingTS.utils.commonUtils;

import org.apache.log4j.Logger;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonVariables.Globals;

public class ReportUtil {
	
	protected static Logger log = Logger.getLogger(ReportUtil.class);
	
	//takeSnapshotFlag :: 0-No Snapshot Required, 1-Current Focused Page Snapshot , 2- Specific Passed Object  Snapshot (option 2 is not yet built)
	//3-option to link any file pTH MENTIONED IN Globals.Result_Link_FilePath
	public static void STAF_ReportEvent(String stepResult,String stepName, String stepDescription,int takeSnapshotFlag) {
		
		try {
			String imageName;
			if (takeSnapshotFlag==1) {
				imageName = seleniumCommands.takePageSnapShot();
			}else if(takeSnapshotFlag==3){ // this option can be used for linking files other than screenshots
				imageName = Globals.Result_Link_FilePath;
			}
			else {
				imageName = "";
			}
			
			//Reset default tc Result status to Pass - to keep below logic unchaged  
			if (Globals.tcResultSheet_lastRowNumber == 1) {
				Globals.tcExecutionStatus = "Pass";					
			}
			
			if (Globals.tcExecutionStarted == 0 && !Globals.dummyPFStaus.equalsIgnoreCase("fail") &&  !Globals.dummyPFStaus.equalsIgnoreCase("warning")) {
				Globals.tcExecutionStarted = 1;
				Globals.pfExecutionStatus = "Pass";
				}
			else if(Globals.tcExecutionStarted == 0 ) {
				Globals.tcExecutionStarted = 1;	
			}
			
			Globals.tcResultSheet.tcResultSheet_addStepDetails(stepName,stepDescription,stepResult,imageName);
			
//			DB-CHANGES
			if(Globals.EnterResultsInDB){
				int testStepStatusID = Globals.automatonDB.fetchID("StatusID",stepResult.toUpperCase(),Globals.FetchStatusIDSQL,Globals.InsertStatusDetailsSQL);
				Globals.automatonDB.insertTestStepDetails(Globals.ExecutionID,Globals.FlowID,stepName,stepDescription,testStepStatusID,imageName);
			}
			
			
			
			//test case status updation
			if (!Globals.tcExecutionStatus.equalsIgnoreCase("fail")) {
				if (stepResult.equalsIgnoreCase("fail")) {
					Globals.tcExecutionStatus =Globals.KEYWORD_FAIL;			
				} else if (stepResult.equalsIgnoreCase("warning")) {
					Globals.tcExecutionStatus =Globals.KEYWORD_WARNING;			
				}
			}
			
			//platform status updation
			if (!Globals.pfExecutionStatus.equalsIgnoreCase("fail")) {
				if (stepResult.equalsIgnoreCase("fail")) {
					Globals.pfExecutionStatus =Globals.KEYWORD_FAIL;	
					Globals.dummyPFStaus = Globals.KEYWORD_FAIL;
				} else if (stepResult.equalsIgnoreCase("warning")) {
					Globals.pfExecutionStatus =Globals.KEYWORD_WARNING;
					Globals.dummyPFStaus = Globals.KEYWORD_WARNING;
				}
			}
			
			Email.sendStepAlert(stepName,stepDescription,stepResult,imageName);
								
		
		} catch (Exception e) {
			log.error("Class GenUtils | Method STAF_ReportEvent | Exception desc : "+e.toString());
			e.printStackTrace();				
		}
	
	}

}
