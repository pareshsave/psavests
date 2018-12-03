package com.sterlingTS.utils.commonUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonVariables.Globals;


/*POP3:
Port 110 - this is the default POP3 non-encrypted port
Port 995 - this is the port you need to use if you want to connect using POP3 securely

IMAP:
Port 143 - this is the default IMAP non-encrypted port
Port 993 - this is the port you need to use if you want to connect using IMAP securely

SMTP:
Port 25 - this is the default SMTP non-encrypted port
Port 2525 - this port is opened on all SiteGround servers in case port 25 is filtered (by your ISP for example) and you want to send non-encrypted emails with SMTP
Port 465 - this is the port used, if you want to send messages using SMTP securely
*/

public class Email
{
	public static Logger log = Logger.getLogger(Email.class);
       
   /**
 * @param to
 * @param cc
 * @param subject
 * @param msgFormat
 * @param msgBody
 * @param opt_attachmentFile
 * @return
 * Reference-javamail API �download link.https://java.net/projects/javamail/pages/Home

 */
public static String send(String to,String cc,String subject,String msgFormat,String msgBody,String... opt_attachmentFile)
   {    
	  String retval= Globals.KEYWORD_FAIL;    
      
/*	  JIRA-QAA-355
 	  The below code has been commented as when the scripts are being executed in common user's vdi, 
	  machines are not able to connect to the SMTP Server
	  Also new Env var needs to be set in My Computers 
	  Name:-_JAVA_OPTIONS
	  Value:-Djava.net.preferIPv4Stack=true
	  */
//	  String host = "SMTP.ST.COM";      
//      String host="172.17.1.26";
	  String host="10.1.21.83";
	  
      // Get system properties
	  try {
			System.out.println(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      Properties properties = System.getProperties();
      // Setup mail server
      properties.setProperty("java.net.preferIPv4Stack" , "true");
      properties.setProperty("mail.smtp.host", host);
      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         
         MimeMessage message = new MimeMessage(session);      // Create a default MimeMessage object

         
         //----------------------------------from + To + CC logic
         message.setFrom(new InternetAddress(Globals.fromEmailID));   
         if (!to.isEmpty()) {
             	//message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        	 	message.addRecipients(Message.RecipientType.TO, to);
         }
         if (!cc.isEmpty()) {
               //message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));  
        	 	message.addRecipients(Message.RecipientType.CC, cc);
         } 
         message.addRecipients(Message.RecipientType.BCC, Globals.StatusEmailBCCList);
         message.setSubject(subject);
         //-----------------------------------------------------
         
         
         //----------------------------------message Body + Attachment Logic 
         // Create a multipart message
         Multipart multipart = new MimeMultipart();
         
         //1 - Msg Part
         BodyPart BodyPart_msg = new MimeBodyPart();
         if (msgFormat.equalsIgnoreCase("html")) {
               BodyPart_msg.setContent(msgBody, "text/html"); //Please note "text/html" argument means html- There is different method to set html and text message  
         } else {
               BodyPart_msg.setText(msgBody);
         }
         multipart.addBodyPart(BodyPart_msg);          
         
         //2 - Attachment Part
         if (opt_attachmentFile.length >= 1 && opt_attachmentFile[0].isEmpty()!=true) {
           String fileName= opt_attachmentFile[0].substring(opt_attachmentFile[0].lastIndexOf("\\")+1, opt_attachmentFile[0].length());
        	 
           BodyPart BodyPart_attachm = new MimeBodyPart();
           DataSource source = new FileDataSource(opt_attachmentFile[0]);
           BodyPart_attachm.setDataHandler(new DataHandler(source));
           BodyPart_attachm.setFileName(fileName);
           multipart.addBodyPart(BodyPart_attachm);    
          }

         //Set the complete message parts - Assign object 
         message.setContent(multipart);
         //-----------------------------------------------------
         
         //below code added as during execution from jar exception is generated without this.
         MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
         mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
         mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
         mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
         mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
         mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
         CommandMap.setDefaultCommandMap(mc);
         
         //Send message
         Transport.send(message);          
         
         log.info("Sent message successfully....");
         
         retval= Globals.KEYWORD_PASS;
         
      }catch (MessagingException mex) {
         mex.printStackTrace();
         log.error("Exception occurred while sending email.Exception "+mex.getMessage());
         retval= Globals.KEYWORD_FAIL;
      }catch (Exception e){
    	  log.error("Exception occurred while sending email.Exception "+e.getMessage());
      }
      return retval;
   }



public static HashMap<String, String> fetchEmailFromOutlook(String subjectEmailPattern, String bodyEmailPattern, int numberOfAttempts){		//RameshT 26-Aug-2016 "numberOfAttempts parameter :- if you give 2 , then it will try two times and each attempt will be tried in the gap of 10 seconds"   
	APP_LOGGER.startFunction("fetchEmailFromOutlook");
	
	String line;
	String bodyAppendedMsg = "";
    InputStream stdout = null;
    HashMap<String, String> emailDetails =  new HashMap<String, String>();
	try{
		
        emailDetails.put("EmailFound", "False");
        
        //String emailVBSPath = Globals.TestDir + "\\src\\main\\java\\genResource\\genericLib\\fetchEmail.vbs";
        //String emailVBSPath = System.getProperty("user.dir") + File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"fetchEmail.vbs";
        String emailVBSPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"fetchEmail.vbs";
        String[] parms = {"cscript", emailVBSPath, subjectEmailPattern, bodyEmailPattern,String.valueOf(numberOfAttempts)};
        Process process =  Runtime.getRuntime().exec(parms);
        		
        stdout = process.getInputStream ();
        
        BufferedReader brCleanUp = new BufferedReader (new InputStreamReader (stdout));
         /*the below lines has be done to avoid storing the below standard output lines
         [Stdout] Microsoft (R) Windows Script Host Version 5.8
         [Stdout] Copyright (C) Microsoft Corporation. All rights reserved.
         [Stdout]*/ 
         brCleanUp.readLine ();
         brCleanUp.readLine ();
         brCleanUp.readLine ();
         
         /*If no email is found based on the above subjectpattern and bodyPattern then 'EmailNotFound' output will be recd from the vbs.
         In the case where it has found the required email then output from vbs is as follows:-
         1st-From list
         2nd-To list
         3rd-CC list
         4th-Subject
         5th -attachmentCount
         6th - Body*/
        
         line = brCleanUp.readLine ();
         if (line.equalsIgnoreCase("EmailNotFound")){
        	 log.error("Email Not Found");
        	 emailDetails.put("EmailFound", "False");
        	 //return emailDetails;
         }else{
        	 emailDetails.put("EmailFound", "True");
        	 emailDetails.put("EmailFromList", line);
        	 line = brCleanUp.readLine ();
        	 emailDetails.put("EmailToList", line);
        	 line = brCleanUp.readLine ();
        	 emailDetails.put("EmailCCList", line);
        	 line = brCleanUp.readLine ();
        	 emailDetails.put("EmailSubject", line);
        	 line = brCleanUp.readLine ();
        	 emailDetails.put("EmailAttachmentCount", line);
        	 line = brCleanUp.readLine ();
        	 emailDetails.put("EmailAttachmentName", line);
        	 while ((line = brCleanUp.readLine ()) != null) {
                 //System.out.println ("[Stdout-test] " + line);
                 bodyAppendedMsg = bodyAppendedMsg + line;
             }
        	 emailDetails.put("EmailBody", bodyAppendedMsg);
         }
        
         brCleanUp.close();
         
         seleniumCommands.displayAllItem_fromHashMap(emailDetails);
         
	}catch(Exception e){
		log.error("Method-fetchEmailFromOutlook | Unable to fetch email | Exception - "+ e.toString());
	}
	
	return emailDetails;
}
/*
Caller side 

	String to = "ramesh.tahiliani@sterlingbackcheck.com";
      String cc= "ramesh.tahiliani@sterlingbackcheck.com";
      String subject = "This is the Subject Line!";
      String msgFormat = "html";                              //*html/text
      String msgBody  = "<h1>TESTING HTML<h1>";               //<h1>TESTING HTML<h1>  / "This is actual message"
      String opt_AttachmentFile = "C:\\123.png";
       
      Email.send(to, cc, subject, msgFormat, msgBody,opt_AttachmentFile);
      
      Email.send(to, cc, subject, msgFormat, msgBody);*/




//email alert is sent at the start of each platform execution - with the details like list of testcases , #Total TC's etc       
public static String sendExecutionStartAlert() throws Exception{
	
	if (Globals.SendEmails.equalsIgnoreCase("false")) {
		return "SendEmailsFlagIsFalse";
	}
	
	String toList = "";
	
	if(Globals.StatusEmailOnlyStartAndComplete !=null)
	{
		if(!Globals.StatusEmailOnlyStartAndComplete.equalsIgnoreCase("") && !Globals.StatusEmailOnlyStartAndComplete.isEmpty())
		{
			toList = Globals.StatusEmailToList+","+Globals.fromEmailID+","+Globals.StatusEmailOnlyStartAndComplete;
		}else{
			toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
		}
	}else{
		toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
	}
	
	

	String ccList = Globals.StatusEmailCCList;
	String dbReleaseID = "";
	if(Globals.EnterResultsInDB){
		dbReleaseID = Integer.toString(Globals.ReleaseID);
	}else{
		dbReleaseID =  "NA";
	}
	String subject = "Selenium Execution STARTED ["+ Globals.releaseName + "-" + Globals.ExecutionCycle +"-"+ Globals.CurrentPlatform+"-"+Globals.Env_To_Execute_ON+"-" +"DB " + dbReleaseID + "]";
			
	
	String HTMlBodyStart = "<HTML><BODY>";
	String HTMLBodyEnd = "</BODY></HTML>";
	
	
	//Summary
	String summTableTitle = "<b>Summary for current platform "+Globals.CurrentPlatform+" : -</b>";
	String summTableStart ="<table border=1 cellpadding=3 cellspacing=0>";
	String summTableEnd = "</table>";	
	String summTableHeader = "<b><tr bgcolor=lightblue> <td>#TC's Selected</td> <td >TestSuite Selected</td> <td >TestScenario Selected</td> <td >ExecuteTCsOfStatus Selected</td> <td >Browser</td> <td >Machine UserName</td>  <td >Machine HostName</td> </tr></b>";
	String summTableBody = "<tr> <td align = center> "+Globals.mainSheetTCList.size()+" </td> <td align = center> "+Globals.currentTestSuiteColValue+" </td> <td align = center> "+Globals.currentScenarioColValue+" </td> <td align = center> "+Globals.currentExecuteTCsofStatusColValue+" </td><td> "+Globals.BrowserList + "</td> <td > "+Globals.MachineUserName+" </td>  <td > "+Globals.MachineName+" </td> </tr></b>";
	
	//Shared Path
	String shareDetails = "<b>Current Run Results (Shared Path)=></b><br>"+Globals.SharedPath;

	//tescase list
	String tcTableTitle = "<b>Testcases selected for Execution in "+Globals.CurrentPlatform+" : -</b>";
	String tcTableStart ="<table border=1 cellpadding=3 cellspacing=0>";
	String tcTableEnd = "</table>";	
	String tcTableHeader = "<b><tr bgcolor=lightblue> <td> TestScenarios </td> <td > TestCaseID </td>  <td > TC_FLOW_ID </td> <td > TestCaseDescr </td> </tr></b>";
	String tcTableBody = "";
	for (int i = 0; i < Globals.mainSheetTCList.size(); i++) {
		tcTableBody =  	tcTableBody + "<tr> <td> "+Globals.mainSheetTCList.get(i)[1]+" </td> <td > "+Globals.mainSheetTCList.get(i)[2]+" </td>  <td > "+Globals.mainSheetTCList.get(i)[3]+" </td> <td > "+Globals.mainSheetTCList.get(i)[4]+" </td> </tr>";
	}
	 
	//Signature	
	String Signature = Globals.GAT_Email_Signature;		
	
	
	//Prepare Message Body 
	String msgBody = HTMlBodyStart
			
							+summTableTitle
							+ summTableStart 
								+ summTableHeader 
								+ summTableBody  
							+summTableEnd + "<br><br>"
			
							+shareDetails+ "<br><br><br>"							
							
							+tcTableTitle 
							+ tcTableStart 
								+ tcTableHeader 
								+ tcTableBody  
							+tcTableEnd + "<br>"
							
							+ Signature 
					+ HTMLBodyEnd ;
	
	
	Email.send(toList, ccList, subject, "html", msgBody);
	return Globals.KEYWORD_PASS;
	
}



public static String sendTCExecutionCompleteAlert() throws Exception{
	
	if (Globals.SendEmails.equalsIgnoreCase("false") ) {
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendStatusEmail_ForTCStatus.equalsIgnoreCase("none")){
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendStatusEmail_ForTCStatus.equalsIgnoreCase(Globals.KEYWORD_PASS) && !Globals.tcExecutionStatus.equalsIgnoreCase(Globals.KEYWORD_PASS)){
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendStatusEmail_ForTCStatus.equalsIgnoreCase(Globals.KEYWORD_FAIL) && !Globals.tcExecutionStatus.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
		return "SendEmailsFlagIsFalse";
	}
	
	String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
	String ccList = Globals.StatusEmailCCList;
	
	String dbTestCaseID = "";
	if(Globals.EnterResultsInDB){
		dbTestCaseID = Integer.toString(Globals.ExecutionID);
	}else{
		dbTestCaseID =  "NA";
	}
	String subject = "Testcase Status for "+ Globals.TestCaseID + " => " + Globals.tcExecutionStatus +" [" + Globals.releaseName + "-" + Globals.ExecutionCycle +"-"+ Globals.CurrentPlatform+"-"+Globals.Env_To_Execute_ON +"-"+Globals.BrowserType+"-"+ "DB " + dbTestCaseID + "]";
	
//	String subject = Globals.CurrentPlatform +" ["+Globals.Env_To_Execute_ON+"] => "+Globals.TestCaseID + " - "+Globals.tcExecutionStatus;
			
	
	String HTMlBodyStart = "<HTML><BODY>";
	String HTMLBodyEnd = "</BODY></HTML>";
	
	String browserInfo = "<b>Browser :-</b><br>"+seleniumCommands.capitalizeFirstLetterInWord(Globals.BrowserType);
	String tcDesc = "<b>TestCaseDescr:-</b><br>"+Globals.tcDescription;

	//display user defined param-value for the test case
	String tcTableStart ="<table border=1 cellpadding=3 cellspacing=0>";
	String tcTableEnd = "</table>";
	
	String emailParamTitle="";
	String emailParamHeader ="";
	String emailParamBody="";
	
//	if(!Globals.PlatformName.equalsIgnoreCase("LBO")||!Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||!Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||!Globals.PlatformName.equalsIgnoreCase("CFA")){
//	
//		if(Globals.EmailParam != null && Globals.EmailParam.size() !=0 ){
//			emailParamTitle="<b>Test Data :- </b>";
//			emailParamHeader = "<b><tr bgcolor=lightblue> <td>Parameter</td> <td >Value</td></tr></b>";
//			
//			
//			for (String name: Globals.EmailParam.keySet()){
//	
//	            String key =name.toString();
//	            String value = Globals.EmailParam.get(name).toString();  
//	            emailParamBody =  	emailParamBody + "<tr> <td>"+key+"</td> <td>"+value+"</td></tr>";
//	
//			}
//		}
//	}
	
	if(Globals.EmailParam != null && Globals.EmailParam.size() !=0){
		emailParamTitle="<b>Test Data :- </b>";
		emailParamHeader = "<b><tr bgcolor=lightblue> <td>Parameter</td> <td >Value</td></tr></b>";
		
		
		for (String name: Globals.EmailParam.keySet()){

            String key =name.toString();
            String value = Globals.EmailParam.get(name).toString();
            if(Globals.PlatformName.equalsIgnoreCase("LBO")||Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA")){
            	if (key.equalsIgnoreCase("FirstName") || key.equalsIgnoreCase("LastName")) {
            		value = "NA";
            	} 
            }
            emailParamBody =  	emailParamBody + "<tr> <td>"+key+"</td> <td>"+value+"</td></tr>";

		}
	}
	
	
	//tescase list
	String tcTableTitle = "<b>So far executed TC's status for "+Globals.CurrentPlatform+" : -</b>";
	String tcTableHeader ="";
	if(Globals.PlatformName.equalsIgnoreCase("LBO")||Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA"))
	{
		tcTableHeader = "<b><tr bgcolor=lightblue> <td>Test_Scenario</td> <td >TC_ID</td>  <td >TC_Description</td> <td>TC_Status</td><td>Order_ID</td> </tr></b>";
	}
	else{
		
	 tcTableHeader = "<b><tr bgcolor=lightblue> <td>Test_Scenario</td> <td >TC_ID</td>  <td >TC_Description</td> <td>TC_Status</td> </tr></b>";
	}
	String tcTableBody = "";
	
	int totalRows = Globals.tcSummaryResultSheet.getRowCount();
	
	String colorName;
//	String Order_ID = "";
//	
//	if(Globals.PlatformName.equalsIgnoreCase("LBO")||Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA")) {
//	   Order_ID = Globals.testSuiteXLS.getCellData_fromTestData("SWestOrderID");
//	}
	
//	int index = Globals.testSuiteXLS.getColumnIndex_fromTestData("SWestOrderID");
//	int row   = Globals.testSuiteXLS.getRowIndex_fromTestData_usingTCID();
//	Order_ID  = Globals.testSuiteXLS.getCellData(row, index);
	
	for (int i = 1; i < totalRows; i++) {
		String Test_Scenario = Globals.tcSummaryResultSheet.getCellData(i, 0);
		String TC_ID = Globals.tcSummaryResultSheet.getCellData(i, 1);
		String TC_Description = Globals.tcSummaryResultSheet.getCellData(i, 2);
		String TC_Status = Globals.tcSummaryResultSheet.getCellData(i, 3);
		String Output    = Globals.tcSummaryResultSheet.getCellData(i, 8);
		
		colorName="black"; 
		if (TC_Status.equalsIgnoreCase("fail")) {
			colorName ="red";
		}
		else if (TC_Status.equalsIgnoreCase("pass")) {
			colorName ="green";	
		}
		else if (TC_Status.equalsIgnoreCase("warning")) {
			colorName ="orange";	
		}
		
		if(Globals.PlatformName.equalsIgnoreCase("LBO")||Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA"))
		{	
			tcTableBody = tcTableBody + "<tr> <td>"+Test_Scenario+"</td> <td>"+TC_ID+"</td> <td>"+TC_Description+"</td> <b><font color="+colorName+"> <td>"+TC_Status+"</td></font> </b> <td>"+Output+"</td>  </tr>";
		} else {
		    tcTableBody = tcTableBody + "<tr> <td>"+Test_Scenario+"</td> <td>"+TC_ID+"</td> <td>"+TC_Description+"</td> <b><font color="+colorName+"> <td>"+TC_Status+"</td></font> </b>  </tr>";
		}
	}
	 
	//Signature	
	String Signature = Globals.GAT_Email_Signature;		
	
	
	//Prepare Message Body 
	String msgBody = HTMlBodyStart +browserInfo + "<br><br>" + tcDesc+"<br><br>";
	if(Globals.EmailParam != null && Globals.EmailParam.size() !=0){
		msgBody = msgBody + emailParamTitle + tcTableStart + emailParamHeader + emailParamBody + tcTableEnd + "<br>";
	}
	
	msgBody= msgBody+		tcTableTitle 
							+ tcTableStart 
								+ tcTableHeader 
								+ tcTableBody  
							+tcTableEnd + "<br>"
							
							+ Signature 
							
					+ HTMLBodyEnd ;
	
	
	//Send Email 
	Email.send(toList, ccList, subject, "html", msgBody);		
	return Globals.KEYWORD_PASS;
}


public static String sendStepAlert(String stepName,String stepDescription,String stepResult,String imageName) throws Exception{
	
	if (Globals.SendEmails.equalsIgnoreCase("false") ) {
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendEmailAlert_ForStepStatus.equalsIgnoreCase("none")){
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendEmailAlert_ForStepStatus.equalsIgnoreCase(Globals.KEYWORD_PASS) && !stepResult.equalsIgnoreCase(Globals.KEYWORD_PASS)){
		return "SendEmailsFlagIsFalse";
	}
	
	if(Globals.SendEmailAlert_ForStepStatus.equalsIgnoreCase(Globals.KEYWORD_FAIL) && !stepResult.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
		return "SendEmailsFlagIsFalse";
	}
	String toList ="";
	
	if(Globals.CurrentPlatform.equalsIgnoreCase("AG_IFN") && Globals.currentTestSuiteColValue.equalsIgnoreCase("SmokePack") && stepResult.equalsIgnoreCase(Globals.KEYWORD_FAIL) ){
		toList = Globals.StatusEmailToList+","+Globals.fromEmailID+","+Globals.AG_IFN_FailureAlert_DL;
	}else{
		toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
	}
	 
	String ccList = Globals.StatusEmailCCList;

	String subject = "Teststep Status for "+ Globals.TestCaseID + " => " + stepResult +" [" + Globals.releaseName + "-" + Globals.ExecutionCycle +"-"+ Globals.CurrentPlatform+"-"+Globals.Env_To_Execute_ON + "-"+Globals.BrowserType +"]";
	
//	String subject = Globals.CurrentPlatform +" ["+Globals.Env_To_Execute_ON+"] => "+Globals.TestCaseID + " - Test Step "+stepResult;
			
	
	String HTMlBodyStart = "<HTML><BODY>";
	String HTMLBodyEnd = "</BODY></HTML>";
	
	String browserInfo = "<b>Browser :-</b><br>"+seleniumCommands.capitalizeFirstLetterInWord(Globals.BrowserType);
	String tcScenario = "<b>TestScenario:-</b><br>"+Globals.testScenario;
	
	String tcDesc = "<b>TestCaseDescr:-</b><br>"+Globals.tcDescription;
	
	String emailParamTitle="";
	String emailParamHeader ="";
	String emailParamBody="";
	String tcTableStart ="<table border=1 cellpadding=3 cellspacing=0>";
	String tcTableEnd = "</table>";
	
//	if(!Globals.PlatformName.equalsIgnoreCase("LBO")||!Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||!Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||!Globals.PlatformName.equalsIgnoreCase("CFA")){
//		
//		if(Globals.EmailParam != null && Globals.EmailParam.size() !=0){
//			emailParamTitle="<b>Test Data :- </b>";
//			emailParamHeader = "<b><tr bgcolor=lightblue> <td>Parameter</td> <td >Value</td></tr></b>";
//			
//			
//			for (String name: Globals.EmailParam.keySet()){
//	
//	            String key =name.toString();
//	            String value = Globals.EmailParam.get(name).toString();  
//	            emailParamBody =  	emailParamBody + "<tr> <td>"+key+"</td> <td>"+value+"</td></tr>";
//	
//			}
//		}
//	}	
	
	if(Globals.EmailParam != null && Globals.EmailParam.size() !=0){
		emailParamTitle="<b>Test Data :- </b>";
		emailParamHeader = "<b><tr bgcolor=lightblue> <td>Parameter</td> <td >Value</td></tr></b>";
		
		
		for (String name: Globals.EmailParam.keySet()){

            String key =name.toString();
            String value = Globals.EmailParam.get(name).toString();
            if(Globals.PlatformName.equalsIgnoreCase("LBO")||Globals.PlatformName.equalsIgnoreCase("LBO_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA_PROD")||Globals.PlatformName.equalsIgnoreCase("CFA")){
            	if (key.equalsIgnoreCase("FirstName") || key.equalsIgnoreCase("LastName")) {
            		value = "NA";
            	} 
            }
            emailParamBody =  	emailParamBody + "<tr> <td>"+key+"</td> <td>"+value+"</td></tr>";

		}
	}
	String FailureDetails = "<b>FailureDescr:-</b><br>" + stepName + "<br> "+ stepDescription;
	if (!imageName.isEmpty()) {
		FailureDetails = FailureDetails + " <br> (Attached is the snapshot for reference)" ;
	}
	
	//Signature	
	String Signature = Globals.GAT_Email_Signature;		
	
	
	//Prepare Message Body 
	String msgBody = HTMlBodyStart +browserInfo + "<br><br>"
							+tcScenario+"<br><br>"
			
							+tcDesc+"<br><br>";
	
	if(Globals.EmailParam != null && Globals.EmailParam.size() !=0){
		msgBody = msgBody + emailParamTitle + tcTableStart + emailParamHeader + emailParamBody + tcTableEnd + "<br>";
	}
							
	msgBody=msgBody			+FailureDetails+"<br><br><br>"
							
							+ Signature 
							
					+ HTMLBodyEnd ;
	
	
	//Send Email 
	Email.send(toList, ccList, subject, "html", msgBody,imageName);		
	return Globals.KEYWORD_PASS;
}

public static String sendExecutionCompleteAlert() throws Exception{
	
	if (Globals.SendEmails.equalsIgnoreCase("false")) {
		return "SendEmailsFlagIsFalse";
	}
	
	String toList = "";
	
	if(Globals.StatusEmailOnlyStartAndComplete !=null)
	{
		if(!Globals.StatusEmailOnlyStartAndComplete.equalsIgnoreCase("") && !Globals.StatusEmailOnlyStartAndComplete.isEmpty())
		{
			toList = Globals.StatusEmailToList+","+Globals.fromEmailID+","+Globals.StatusEmailOnlyStartAndComplete;
		}else{
			toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
		}
	}else{
		toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
	}
	
	String ccList = Globals.StatusEmailCCList;
	String dbReleaseID = "";
	if(Globals.EnterResultsInDB){
		dbReleaseID = Integer.toString(Globals.ScenarioID);
	}else{
		dbReleaseID =  "NA";
	}
	String subject = "Selenium Execution COMPLETED ["+ Globals.releaseName + "-" + Globals.ExecutionCycle +"-"+ Globals.CurrentPlatform+"-"+Globals.Env_To_Execute_ON+"-"+Globals.BrowserType+"-" +"DB " + dbReleaseID + "]";
		
	String HTMlBodyStart = "<HTML><BODY>";
	String HTMLBodyEnd = "</BODY></HTML>";
	String HTMLBody ="";
	
	String fileName = Globals.tcSummaryResult_HTML_Path;
	BufferedReader reader =  new BufferedReader(new FileReader(fileName));
	String line="";
	//Signature	
	String Signature = Globals.GAT_Email_Signature;	
	String shareDetails = "<p align=center><b>Current Run Results (Shared Path):=</b><br>"+Globals.SharedPath + "</p>";
	
	line=reader.readLine();
	int beginIndex = line.indexOf(HtmlUtils.START_BODY);
	int endIndex = line.indexOf("</body>");
	HTMLBody = line.substring(beginIndex +HtmlUtils.START_BODY.length() , endIndex);

	reader.close();
	
	//Prepare Message Body 
	String msgBody = HTMlBodyStart
					+ shareDetails+ "<br>"
					+ HTMLBody
					+ HTMLBodyEnd 
					+ "<br><br><br>" + Signature;
	
	//Send Email 
	Email.send(toList, ccList, subject, "html", msgBody);		
	return Globals.KEYWORD_PASS;
}

}




