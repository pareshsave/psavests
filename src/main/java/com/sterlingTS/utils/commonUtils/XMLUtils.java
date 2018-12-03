package com.sterlingTS.utils.commonUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sterlingTS.seleniumUI.seleniumCommands;
import com.sterlingTS.utils.commonVariables.Globals;

public class XMLUtils {
	
	protected static Logger log = Logger.getLogger(XMLUtils.class);
	
	private File fXmlFile;
	private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder dBuilder = null; 
	private Document doc;
	private String XMLFilePath;
	private String URI;
	private String UsernName;
	private String UserPassword;
	public String Response;
	public XPath xPath =  XPathFactory.newInstance().newXPath();
	public String mipsPlatform;
	
	public XMLUtils(String strFilePath,String soapServer,String userID,String userPwd){
		this.XMLFilePath = strFilePath;
		this.URI = soapServer;
		this.UsernName = userID;
		this.UserPassword = userPwd;
		fXmlFile = new File(XMLFilePath);
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public XMLUtils(String strFilePath,String soapServer){
		this.XMLFilePath = strFilePath;
		this.URI = soapServer;
		fXmlFile = new File(XMLFilePath);
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public XMLUtils(String soapServer,String userID,String userPwd){
		//this.XMLFilePath = strFilePath;
		this.URI = soapServer;
		this.UsernName = userID;
		this.UserPassword = userPwd;
		
		
	}
	
	
	
	public XMLUtils(String strFilePath,String soapServer,String userID,String userPwd,String mipsPlatform){
		this.XMLFilePath = strFilePath;
		this.URI = soapServer;
		this.UsernName = userID;
		this.UserPassword = userPwd;
		fXmlFile = new File(XMLFilePath);
		this.mipsPlatform = mipsPlatform;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public XMLUtils(String strFilePath){

		fXmlFile = new File(strFilePath);
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	public String postXML() throws IOException{
		String line;

	    InputStream stdout = null;
	    
//	    String emailVBSPath = Globals.TestDir + "\\src\\genResource\\genericLib\\postXML.vbs";
//		String emailVBSPath = "C:\\STAF_Selenium\\SeleniumFramework\\src\\genericLib\\postXML.vbs";
	    String emailVBSPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"postXML.vbs";
		
//	    String emailVBSPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"postXML.vbs";
	    
	    //Workaround for MIP/PRISM
	    if(mipsPlatform == null || mipsPlatform.isEmpty() || mipsPlatform.equals("")){
	    	mipsPlatform ="NA";
	    }
	    
	    
	    if((UsernName == null && UserPassword == null) || (UsernName.isEmpty() && UserPassword.isEmpty()) || (UsernName.equals("") && UserPassword.equals("")))
	    {
//	    	emailVBSPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"postWithOutAuthenticationXML.vbs";
	    	emailVBSPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"postWithOutAuthenticationXML.vbs";
	    	String[] parms = {"cscript", emailVBSPath, XMLFilePath, URI,mipsPlatform};
	    	Process process =  Runtime.getRuntime().exec(parms);
	    	stdout = process.getInputStream ();
	    }else{
	    	String[] parms = {"cscript", emailVBSPath, XMLFilePath, URI,UsernName,UserPassword,mipsPlatform};
	    	Process process =  Runtime.getRuntime().exec(parms);
	    	stdout = process.getInputStream ();
	    }
		
				
		
		
		BufferedReader brCleanUp = new BufferedReader (new InputStreamReader (stdout));
		 /*the below lines has be done to avoid storing the below standard output lines
        [Stdout] Microsoft (R) Windows Script Host Version 5.8
        [Stdout] Copyright (C) Microsoft Corporation. All rights reserved.
        [Stdout]*/ 
        brCleanUp.readLine ();
        brCleanUp.readLine ();
        brCleanUp.readLine ();
        line = brCleanUp.readLine ();
        
        if (line.equalsIgnoreCase("ResponseNotFound")){
       	 	log.error("Response Not Found");
       	 	return Globals.KEYWORD_FAIL;
       	 
        }else{
        	
        	Response = "";
        	while (line != null) {
             
        		Response = Response + line;
                line = brCleanUp.readLine ();
                
            }
        	 
        }
        
        return Response;
	}
	
	public String getXML() throws IOException{
		String line;

	    InputStream stdout = null;
	    
//	    String emailVBSPath = Globals.TestDir + "\\src\\genResource\\genericLib\\getXML.vbs";
//		String emailVBSPath = "C:\\STAF_Selenium\\SeleniumFramework\\src\\genericLib\\postXML.vbs";
	    String emailVBSPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"getXML.vbs";
		
	    //Workaround for MIP/PRISM
	    //if(mipsPlatform == null || mipsPlatform.isEmpty() || mipsPlatform.equals("")){
	    	//mipsPlatform ="NA";
	   // }
	    String[] parms = {"cscript", emailVBSPath,URI,UsernName,UserPassword};
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
        line = brCleanUp.readLine ();
        
        if (line.equalsIgnoreCase("ResponseNotFound")){
       	 	log.error("Response Not Found");
       	 	return Globals.KEYWORD_FAIL;
       	 
        }else{
        	
        	Response = "";
        	while (line != null) {
             
        		Response = Response + line;
                line = brCleanUp.readLine ();
                
            }
        	 
        }
        
        return Response;
	}
	
	public static String verifyXMLResponse(){
		return Globals.KEYWORD_PASS;
	}
	
	
	public String getXMLNodeValByXPath(String NodeXPATH) throws XPathExpressionException{
		
		String retval = Globals.KEYWORD_FAIL;
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		NodeList nList = (NodeList) xPath.compile(NodeXPATH).evaluate(doc, XPathConstants.NODESET);

		
		retval = nList.item(0).getTextContent();

		return retval;
	}
	
	public String getXMLNodeValByTagName(String NodeTagName){
		return doc.getElementsByTagName(NodeTagName).item(0).getTextContent();
		
	}
	
	
	public int getNumberOfNodesPresentByTagName(String NodeTagName){
		
		return doc.getElementsByTagName(NodeTagName).getLength();
	}
	
	public String updatedXMLNodeValueByTagName(String NodeTagName, String value){
		doc.getElementsByTagName(NodeTagName).item(0).setTextContent(value);
		return Globals.KEYWORD_PASS;
	}
	
	public String updateXMLNodeValueByTagName_ForAnIndex(String NodeTagName, int index ,String value){
		doc.getElementsByTagName(NodeTagName).item(index).setTextContent(value);
		return Globals.KEYWORD_PASS;
	}
	
	public String updateXMLNodeAttributeValue(String NodeTageName,String AttributeName, String newAttributeValue){
				doc.getElementsByTagName(NodeTageName).item(0).getAttributes().getNamedItem(AttributeName).setTextContent(newAttributeValue);
				return Globals.KEYWORD_PASS;
	}
	
	public void updatedXMLNodeValueByXPATH(String NodeXPATH, String value) throws XPathExpressionException{
				
		
		
		NodeList nList = (NodeList) xPath.compile(NodeXPATH).evaluate(doc, XPathConstants.NODESET);
		
		nList.item(0).setTextContent(value);
		
	}
	
	
	public static String convertXML2String(){
		return Globals.KEYWORD_PASS;
	}
	
	public static String convertString2XML(){
		return Globals.KEYWORD_PASS;
	}

	public String saveXMLString(String xmlFilePath) throws Exception {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(Response.getBytes("utf-8"))));
//		Document doc = dBuilder.parse(xmlContents);
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		
		//generate the modified input xml and save in Results folder
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		//initialize StreamResult with File object to save to file
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();

		File file=new File(xmlFilePath);
		String encode="UTF-8";
		FileUtils.writeStringToFile(file, xmlString,encode);

		
		return Globals.KEYWORD_PASS;
	}
	
	public String verifyXMLNodeValueByTagName(String TagName, String ExpectedValue,String reportingFieldName) throws Exception {
		
		String retval=Globals.KEYWORD_FAIL;
		String actualValue ;
		actualValue	=	getXMLNodeValByTagName(TagName);
		boolean verificationPassed = false;
		
		if (ExpectedValue.equalsIgnoreCase("url")){
			if(actualValue.contains(".com") && actualValue.contains("https://")){
				verificationPassed = true;
			}
		}else if(ExpectedValue.equalsIgnoreCase("string")){
			Pattern regEx = Pattern.compile("([a-zA-Z])");
			Matcher m = null;
			m = regEx.matcher(actualValue);
			if(m.find()){
				verificationPassed = true;
			}
			
		}else if(ExpectedValue.equalsIgnoreCase("numeric")){
			Pattern regEx = Pattern.compile("([0-9])");
			Matcher m = null;
			m = regEx.matcher(actualValue);
			if(m.find()){
				verificationPassed = true;
			}
		}else{
			if(ExpectedValue.equals(actualValue)){
				verificationPassed = true;
			}
		}
		
		if(verificationPassed == true){
			seleniumCommands.STAF_ReportEvent("Pass", "Verify XML Node - "+reportingFieldName, " Value is as Expected.Val - "+actualValue, 0);
			retval=actualValue;
		}else{
			seleniumCommands.STAF_ReportEvent("Fail", "Verify XML Node - "+reportingFieldName, " Value is Not as Expected.Expected -"+ExpectedValue +" Actual-"+actualValue, 0);
		}
			
		return retval;
	}
	
	
	
public String verifyXMLNodeValueByXPATH(String TagName, String ExpectedValue,String reportingFieldName) throws Exception {
		
		String retval=Globals.KEYWORD_FAIL;
		String actualValue ;
		actualValue	=	getXMLNodeValByXPath(TagName);
		boolean verificationPassed = false;
		
		if (ExpectedValue.equalsIgnoreCase("url")){
			if(actualValue.contains(".com") && actualValue.contains("https://")){
				verificationPassed = true;
			}
		}else if(ExpectedValue.equalsIgnoreCase("string")){
			Pattern regEx = Pattern.compile("([a-zA-Z])");
			Matcher m = null;
			m = regEx.matcher(actualValue);
			if(m.find()){
				verificationPassed = true;
			}
			
		}else if(ExpectedValue.equalsIgnoreCase("url_startValue")){
			if(actualValue.contains("https://")){
				verificationPassed = true;
			}
			
		}else if(ExpectedValue.equalsIgnoreCase("numeric")){
			Pattern regEx = Pattern.compile("([0-9])");
			Matcher m = null;
			m = regEx.matcher(actualValue);
			if(m.find()){
				verificationPassed = true;
			}
		}else{
			if(ExpectedValue.equals(actualValue)){
				verificationPassed = true;
			}
		}
		
		if(verificationPassed == true){
			seleniumCommands.STAF_ReportEvent("Pass", reportingFieldName + "=>", "Value is displayed in correct format - "+actualValue, 0);
			retval=actualValue;
		}else{
			seleniumCommands.STAF_ReportEvent("Fail", reportingFieldName + "=>", "Value is Not as Expected.Expected -"+ExpectedValue +" Actual-"+actualValue, 0);
		}
			
		return retval;
	}
public String saveXMLDOM2File(String xmlFileName) throws Exception {

		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		
		//generate the modified input xml and save in Results folder
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		//initialize StreamResult with File object to save to file
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();

		File file=new File(Globals.currentPlatform_XMLResults +File.separator+xmlFileName);
		String encode="UTF-8";
		FileUtils.writeStringToFile(file, xmlString,encode);

		
		return Globals.KEYWORD_PASS;
	}


		public int getNumberOfNodesPresentByXPath(String NodeXPATH) {
			
			int retval =-1;
			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			try{
				NodeList nList = (NodeList) xPath.compile(NodeXPATH).evaluate(doc, XPathConstants.NODESET);
				retval = nList.getLength();
			}catch(Exception e){
				retval =-1;
				e.printStackTrace();
			}
					
			return retval;
		}
		
		 public String updateXMLNodeAttributeValue_ByXPATH(String NodeXPATH,String AttributeName, String newAttributeValue) throws XPathExpressionException{
             NodeList nList = (NodeList) xPath.compile(NodeXPATH).evaluate(doc, XPathConstants.NODESET);
             
             nList.item(0).getAttributes().getNamedItem(AttributeName).setTextContent(newAttributeValue);
             return Globals.KEYWORD_PASS;
}

}
