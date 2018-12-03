package com.sterlingTS.seleniumUI;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sterlingTS.utils.commonUtils.APP_LOGGER;
import com.sterlingTS.utils.commonUtils.CommonHelpMethods;
import com.sterlingTS.utils.commonUtils.Email;
import com.sterlingTS.utils.commonUtils.ExcelUtils;
import com.sterlingTS.utils.commonUtils.LocatorAccess;
import com.sterlingTS.utils.commonUtils.ProtectedEncryption;
import com.sterlingTS.utils.commonVariables.Globals;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Secur32Util;



public class seleniumCommands extends Globals{
	
	public static Logger log = Logger.getLogger(seleniumCommands.class);
	private static WebDriver driver;
	public seleniumCommands(WebDriver driver)
	{
		this.driver = driver;
	}
	
	/**************************************************************************************************
	 * Method to open url by first launching the desired browser
	 * @param browserType - String value corresponding to the browser.IE/CHROME/FIREFOX
	 * @param url -  URL which needs to redirected to.
	 * @return TRUE if URL is launched / FALSE , if unable to launch URL
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String launchURL(String browserType, String url){
		APP_LOGGER.startFunction("launchURL");
		String retval=Globals.KEYWORD_FAIL;
		String browserLaunched=Globals.KEYWORD_PASS;
		String pageTitle="";
		try{
			//browserLaunched=browserLaunch(browserType);
			if (browserLaunched.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				System.out.println("Driver is"+"\t"+driver);
				
				driver.get(url); // opens the url 
				pageTitle=driver.getTitle(); // one method to sync the page
				log.debug("URL Launched. Title obtained "+pageTitle);
				retval=Globals.KEYWORD_PASS;
			}else {
				
				log.error("launchURL | Unable to launch URL - "+url);
			}
			
		}catch(Exception e){
			log.error("launchURL | Exception occurred - " + e.toString());
		}
		
		return retval;
	}

	/**************************************************************************************************
	 * Method to close all browsers.
	 * @return void
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static void closeAllBrowsers(){
		APP_LOGGER.startFunction("closeAllBrowsers");
		
			try{
				if(driver == null || driver.toString().contains("null"))
				{
					
				throw new Exception("******All Browser windows are closed *******");
				
				}
				
				//driver.close();
				driver.quit();
				driver = null;
				
			}catch(Exception e){
				log.debug("******All Browser windows are closed *******");	              
				}
	
	}
	
	/**************************************************************************************************
	 * Method to click on an element using javascript
	 * @param elementName -  Name of the element corresponding in OR
	 * @return PASS, if the element is clicked specified/ FAIL, otherwise
	 * @author Navisha
	 * @created 2/16/2017
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String clickElementUsingJavaScript(String elementName) {
		APP_LOGGER.startFunction("clickElementUsingJavaScript");
			String retval=Globals.KEYWORD_FAIL;
			WebElement element=null;
		try{
			element=createWebElement(elementName);
			retval=clickElementUsingJavaScript(element);
			

		}catch (Exception e){
			log.debug("Method - clickElementUsingJavaScript | Exception occurred - "+e.toString());
			
		}
		return retval;			
	}
	
	public static String clickElementUsingJavaScript(WebElement element) throws Exception{
		String retval=Globals.KEYWORD_FAIL;
		try{
			
			JavascriptExecutor js = (JavascriptExecutor)Globals.driver;
			js.executeScript("arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible';arguments[0].click();",element);
			retval=Globals.KEYWORD_PASS;
		
		}catch (Exception e){
			log.debug("Method - clickElementUsingJavaScript | Exception occurred - "+e.toString());
			throw e;
		}
		return retval;			
	}
	

	
	/**************************************************************************************************
	 * Method to check whether a WebElement is displayed and is enabled
	 * @param element - WebElement on whom isEnabled is checked
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if enabled /FALSE if object is not enabled
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/

	
	public static String isEnabled(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isEnabled");
		String retval=Globals.KEYWORD_FAIL;
		boolean elementEnabled =false;
		
		try{
			retval=isDisplayed(element, reportEventFlag); // first check for isDisplayed
			
			if (retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				elementEnabled=element.isEnabled(); //check for isEnabled
				if (elementEnabled == true){
					retval = Globals.KEYWORD_PASS;
					log.info(element.toString() + " | Enabled = "+elementEnabled);
				}else{
					retval = Globals.KEYWORD_FAIL;
					log.info(element.toString() + " | Enabled =  "+elementEnabled);
				}
				
			}else{
				//System.out.println(elementEnabled);
				retval = Globals.KEYWORD_FAIL;
				log.info(element.toString() + " is NOT Displayed ");
			}
			
			if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	            //reporter
				log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
				//TODO call reporterfunction
				System.out.println("report");
	     
			}
		}catch(Exception e){
			log.error("Method -isEnabled .Exception  " + e.toString() + " occured for Element -"+element.toString() + "while checking of isEnabled ");
		}
		 
		return retval;
	}
	
	/**************************************************************************************************
	 * Method which converts String elementName into WebElement and checks for isDisplayed and isEnabled
	 * @param elementName- String argument corresponding to element Name in OR properties files
	 * @param reportEventFlag
	 * @return TRUE if enabled /FALSE if object is not enabled
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String isEnabled(String elementName,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isEnabled");
		String retval=Globals.KEYWORD_FAIL;
		
		try{
			WebElement element = createWebElement(elementName); // felemnt for webelement based on the element name in OR
			retval=isEnabled(element,reportEventFlag);
		}catch(Exception e){
			log.error("Exception " + e.toString() + " occured for Element -"+elementName + "while checking of isEnabled ");
			retval=Globals.KEYWORD_FAIL;
		}
		return retval;
	}
	
	/**************************************************************************************************
	 * Method which checks whether a webelement is displayed in UI
	 * @param element - WebElement object which need to be checked whether its displayed in UI
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if displayed /FALSE if object is not displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String isDisplayed(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isDisplayed");
		String retval=Globals.KEYWORD_FAIL;
	
		boolean elementFound = false;
		
		try{
			elementFound = element.isDisplayed();
			
			if(elementFound == true){
				
				retval=Globals.KEYWORD_PASS; 
				
			}else{
				retval=Globals.KEYWORD_FAIL; 
			}
			log.info("Element - " + element.toString() +" | Displayed = " + retval );
			
			if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	            //reporter
				log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
				if (retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
					seleniumCommands.STAF_ReportEvent("Pass", "Element Displayed", element.toString() + " is displayed on UI", 0);
				}else{
					seleniumCommands.STAF_ReportEvent("Fail", "Element Displayed", element.toString() + " is NOT displayed on UI", 0);
				}
				
			} 

		}catch(Exception e){
			log.error("Method - isDisplayed | Element -"+element.toString() + " Doesnt exists in UI | Exception - " + e.toString());
		}
		
		return retval;
		
	}	
	/**************************************************************************************************
	 * Method which converts the string elementName into WebElement and then checks for isDisplayed
	 * @param elementName -  String argument corresponding to element Name in OR properties files
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if element is displayed / FALSE , if the element is not displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String isDisplayed(String elementName,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isDisplayed");
		String retval;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			if (element == null ) {
				return Globals.KEYWORD_FAIL;
			}
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
		
		retval=isDisplayed(element, reportEventFlag);
		
		return retval;
	}
	//----------------------------------
	
	/**************************************************************************************************
	 * Method to check whether an element is selected.Mainly used for radio buttons/checkboxes/weblist
	 * @param element -  WebElement object which need to be checked whether its displayed and selected in UI
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if element is selected / FALSE , if the element is not selected
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String isSelected(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isSelected");
		String retval=Globals.KEYWORD_FAIL;
		boolean elementSelected = false;
		boolean elementFound = false; 
		
		try{
			elementFound=element.isDisplayed();
			
			if(elementFound == true){
				
				log.info("Element - " + element.toString() +" | Displayed = " + elementFound );
				elementSelected= element.isSelected();
				if (elementSelected == true){
					
					log.info("Element - " + element.toString() +" | Displayed = True and Selected = " + retval );
					retval=Globals.KEYWORD_PASS;
					
				}else{
					
					log.info("Element - " + element.toString() +" | Displayed = True and Selected = " + retval );
					retval=Globals.KEYWORD_FAIL;				
				}
				
				
			}else{
				retval=Globals.KEYWORD_FAIL; 
				log.error("Element - " + element.toString() +" |  Displayed = FAIL" );
			}
			
			
			if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	            //reporter
				log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
				//TODO call reporterfunction
//				System.out.println("report");
	     
			} 

		}catch (Exception e){
			log.error("Method - isSelected |Element -"+element.toString() + " Doesnt exists in UI | Exception - " + e.toString());
			
		}
		
		return retval;
	}
	
	/**************************************************************************************************
	 * Method which converts string element name into a webelement and then checks for isSelected
	 * @param elementName - String argument corresponding to element Name in OR properties files
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if element is displayed / FALSE , if the element is not displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String isSelected(String elementName,boolean...reportEventFlag){
		APP_LOGGER.startFunction("isSelected");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=isSelected(element, reportEventFlag);
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
		
		
		
		return retval;
	}
	//----------------------------------
	
	/**************************************************************************************************
	 * Method to highlight a webelement using JavascriptExecutor
	 * @param element - WebElement on which highlight will be performed
	 * @return - TRUE if element is displayed / FALSE , if the element is not displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String highlight(WebElement element){
		APP_LOGGER.startFunction("highlight");
		
		String retval= Globals.KEYWORD_FAIL;
		
		try{
			retval=isDisplayed(element);
			
			if (retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				//Creating JavaScriptExecuter Interface
	            JavascriptExecutor js = (JavascriptExecutor)driver;
	            for (int iCnt = 0; iCnt < 3; iCnt++) {
	               //Execute javascript
	                  
	            	  js.executeScript("arguments[0].style.border='4px groove green'", element);
                      Thread.sleep(500);
                      js.executeScript("arguments[0].style.border=''", element);  
	                  
	           	   
	            }
			}else{
			retval=Globals.KEYWORD_FAIL;
				
			}
		}catch(Exception e){
       	   	log.error("Unable to Highlight Element -"+element.toString() + "| Exception - " + e.toString());
       	   	retval=Globals.KEYWORD_FAIL;
       	   
          }
		
	  return retval;
	}
	
	
	/**************************************************************************************************
	 * Method to highlight a webelement using JavascriptExecutor after converting string element name into a webelement
	 * @param elementName - String argument corresponding in OR
	 * @return TRUE if element is displayed / FALSE , if the element is not displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String highlight(String elementName){
		APP_LOGGER.startFunction("highlight");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=highlight(element);
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			return Globals.KEYWORD_FAIL;
		}
		
		return retval;
	}
	
	//----------------------------------
	
	/**************************************************************************************************
	 * Method to create a WebElement from the string element name based on the locator in OR properties file
	 * @param elementName - String argument corresponding the element  in OR properties file
	 * @return WebElement based on OR properties
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static WebElement createWebElement(String elementName){
		APP_LOGGER.startFunction("createWebElement");
		
		WebElement element=null;
		
		try {	
			//if (driver.findElements(PropertyLoader.getLocator(elementName)).size()>0 && driver.findElement(PropertyLoader.getLocator(elementName)).isDisplayed()==true){		//Ramesh T 20-July-16
			if (driver.findElements(LocatorAccess.getLocator(elementName)).size()>0 ){
				element = driver.findElement(LocatorAccess.getLocator(elementName));
				log.info("Element -"+elementName + " is present");
			}else{
				log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI ");
			
			}
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
		return element;
	}
	
	//----------------------------------
	
	/**************************************************************************************************
	 * Method to set value on a web element.Alos check for isDisplayed and isEnabled
	 * @param element - WebElement on which value needs to be Set
	 * @param value - String value which needs to be set
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String setValue(WebElement element, String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValue - WebElement");
		String retval=Globals.KEYWORD_FAIL;
		String elementText;
		String elementDisplayed=isEnabled(element, reportEventFlag);
		
		try{
			if (value != null && elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				 element.clear(); // clearing any value which is already present
				 log.info("Element -"+element.toString() + "| 1st attempt to clear Data");
				 
				 elementText=element.getAttribute("value").trim();
				 log.info("Element -"+element.toString() + "| Verifying Data is cleared");
				 
				 if (elementText.isEmpty()){
					 log.info("Element -"+element.toString() + "| Data is cleared");
					
				 }else{
					 log.error("Element -"+element.toString() + "| 2nd Attempt to clear data is cleared");
					 element.clear();
					 
				 }
				 
				 element.sendKeys(value); // set the value
				 log.info("Element -"+element.toString() + "| Data entered. Value-"+value);
				 
				 elementText=element.getAttribute("value").trim(); // retrieve the value that has bee set
				 
				 if (elementText.equals(value)){
					 log.info("Element -"+element.toString() + "| Data is Set. Value-"+value);
					 retval=Globals.KEYWORD_PASS;
				 }else{
					 log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+value);
					 retval=Globals.KEYWORD_FAIL;
				 }
			 }else{ // element is either not displayed/enabled
				 log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+value.toString());
				 retval=Globals.KEYWORD_FAIL;
			 }
			
			if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	            //reporter
				log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
				//TODO call reporterfunction
//				System.out.println("report");
	     
			}
		}catch (Exception e){
			log.error("Method - setValue | Element -"+element.toString() + " | Exception - " + e.toString());
			
		}
	
		 
			 
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to set value on a web element after having created a webelement from the string element name.Also check for isDisplayed and isEnabled
	 * @param elementName - String - element name corresponding to element in the OR
	 * @param value - String value which needs to be set
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String setValue(String elementName, String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValue - String");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=setValue(element, value, reportEventFlag);
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	
	/**************************************************************************************************
	 * Method to set value on a web element.Alos check for isDisplayed and isEnabled
	 * @param element - WebElement on which value needs to be Set
	 * @param value - String value which needs to be set
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	 * @author Ayas
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String setValueSecure(WebElement element, String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValueSecure - WebElement");
		String retval=Globals.KEYWORD_FAIL;
		String elementText;
		String elementDisplayed=isEnabled(element, reportEventFlag);
		
		try{
			if (value != null && elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				 element.clear(); // clearing any value which is already present
				 log.info("Element -"+element.toString() + "| 1st attempt to clear Data");
				 
				 elementText=element.getAttribute("value").trim();
				 log.info("Element -"+element.toString() + "| Verifying Data is cleared");
				 
				 if (elementText.isEmpty()){
					 log.info("Element -"+element.toString() + "| Data is cleared");
					
				 }else{
					 log.error("Element -"+element.toString() + "| 2nd Attempt to clear data is cleared");
					 element.clear();
					 
				 }
				 
				 element.sendKeys(ProtectedEncryption.decrypt(value, CommonHelpMethods.createKey())); // set the Secure
				 log.info("Element -"+element.toString() + "| Data entered. Value-"+ProtectedEncryption.decrypt(value, CommonHelpMethods.createKey()));
				 
				 elementText=element.getAttribute("value").trim(); // retrieve the value that has bee set
				 
				 if (elementText.equals(ProtectedEncryption.decrypt(value, CommonHelpMethods.createKey()))){
					 log.info("Element -"+element.toString() + "| Data is Set. Value-"+ProtectedEncryption.decrypt(value, CommonHelpMethods.createKey()));
					 retval=Globals.KEYWORD_PASS;
				 }else{
					 log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+ProtectedEncryption.decrypt(value, CommonHelpMethods.createKey()));
					 retval=Globals.KEYWORD_FAIL;
				 }
			 }else{ // element is either not displayed/enabled
				 log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+value.toString());
				 retval=Globals.KEYWORD_FAIL;
			 }
			
			if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	            //reporter
				log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
				//TODO call reporterfunction
//				System.out.println("report");
	     
			}
		}catch (Exception e){
			log.error("Method - setValue | Element -"+element.toString() + " | Exception - " + e.toString());
			
		}
	
		 
			 
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to set value on a web element after having created a webelement from the string element name.Also check for isDisplayed and isEnabled
	 * @param elementName - String - element name corresponding to element in the OR
	 * @param value - String value which needs to be set
	 * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	 * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	 * @author Ayas
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String setValueSecure(String elementName, String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValue - String");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=setValueSecure(element, value, reportEventFlag);
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	/******************************************************************************
	 * This method is used to press the "Enter" key from the KeyBoard
	 * @param element - WebElement required
	 * @param reportEventFlag
	 * @return String of Globals.KEYWORD_PASS
	 * @author Ayas
	 *********************************************************************************/
	public static String setValueEnter(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValue - WebElement");
		String retval=Globals.KEYWORD_FAIL;
		String elementText;
		String elementDisplayed=isEnabled(element, reportEventFlag);
		
		try{
			
				 
				 element.sendKeys(Keys.ENTER); // set the value
				 log.info("Element -"+element.toString() + "| Data entered. Value-"+Keys.ENTER);
				 
				 retval = Globals.KEYWORD_PASS;
			
		}catch (Exception e){
			log.error("Method - setValue | Element -"+element.toString() + " | Exception - " + e.toString());
			
		}
	
		 
			 
		return retval;
	}
	/***********************************************************************************
	 * This method is used to press the "Enter" key from the KeyBoard
	 * @param elementName - String - element name corresponding to element in the OR
	 * @param reportEventFlag
	 * @return Retrival value as KEYWARD_PASS/FAIL
	 * @author Ayas
	 ***********************************************************************************/
	
	public static String setValueEnter(String elementName,boolean...reportEventFlag){
		APP_LOGGER.startFunction("setValue - String");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=setValueEnter(element, reportEventFlag);
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	public static String getWebText(String elementName)
	{
		APP_LOGGER.startFunction("setValue - String");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;
		
		try {
			element=createWebElement(elementName);
			retval=element.getText();
		}
		catch (Exception e){
			log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to obtain the row count of the web table excluding the headers.Also check for isDisplayed
	 * @param elementTableName -  String element name corresponding to the element in the OR
	 * @return int - Globals.INT_FAIL if no rows could be obtained / Row count if table exists.
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static int getRowCount_tbl(String elementTableName){
		
		APP_LOGGER.startFunction("getRowCount_tbl - String");
		
		try {

			WebElement elementTable=createWebElement(elementTableName);
			return getRowCount_tbl(elementTable,"./tbody/tr");
					
		}catch (Exception e){
			log.error("Method getRowCount_tbl | Exception - " + e.toString());
			return Globals.INT_FAIL;	
			
		}
		
				
	}
	
	/**************************************************************************************************
	 * Method to obtain the row count of the web table excluding the headers.
	 * @param elementTableName -  String element name corresponding to the element in the OR
	 * @param relativePathOfTableRow -String - relative of table row e.g. './tbody/tr' etc
	 * @return int - Globals.INT_FAIL if no rows could be obtained / Row count if table exists.
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static int getRowCount_tbl(String elementTableName,String relativePathOfTableRow){
		
		APP_LOGGER.startFunction("getRowCount_tbl - String");
		
		try {

			WebElement elementTable=createWebElement(elementTableName);
			return getRowCount_tbl(elementTable,relativePathOfTableRow);
					
		}catch (Exception e){
			log.error("Method getRowCount_tbl | Exception - " + e.toString());
			return Globals.INT_FAIL;	
			
		}
		
				
	}
	/**************************************************************************************************
	 * Method to obtain the row count of the web table excluding the headers where structure is table/tbody/tr only
	 * @param elementTable -  WebElement  table element only till <table> tag
	 * @return int - Globals.INT_FAIL if no rows could be obtained / Row count if table exists.
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static int getRowCount_tbl(WebElement elementTable){
		APP_LOGGER.startFunction("getRowCount_tbl - WebElement");
		int retval=Globals.INT_FAIL;
		
		try{
			retval = getRowCount_tbl(elementTable,"./tbody/tr");
						
		}catch(Exception e){
			log.error("Method getRowCount_tbl | Exception - " + e.toString());
		}
		return retval;
		
		
		
	}
	
	/**************************************************************************************************
	 * Method to obtain the row count of the web table excluding the headers.
	 * @param elementTable -  WebElement  table element only till <table> tag
	 * @param relativePathOfTableRow -String - relative of table row e.g. './tbody/tr' etc
	 * @return int - Globals.INT_FAIL if no rows could be obtained / Row count if table exists.
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static int getRowCount_tbl(WebElement elementTable,String relativePathOfTableRow){
		APP_LOGGER.startFunction("getRowCount_tbl - WebElement");
		String tempRetval=Globals.KEYWORD_FAIL;
		int retval=Globals.INT_FAIL;
		
		try{
			tempRetval = isDisplayed(elementTable);
			if (tempRetval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				log.debug("WebTable is not displayed - "+elementTable.toString());
				return retval;
				
			}
			
			List<WebElement> rows = elementTable.findElements(By.xpath(relativePathOfTableRow));
			retval = rows.size();
			log.debug("Method - getRowCount_tbl | Rows - "+retval );
		}catch(Exception e){
			log.error("Method getRowCount_tbl | Exception - " + e.toString());
		}
		return retval;
		
		
		
	}
	
	
	/***************************************************************************************************
	 * Method to get the Web Element text
	 * @param elementName - WebElement
	 * @return String - Globals.KEYWORD_FAIL if no element found
	 * @author Ayas
	 ****************************************************************************************************/
	public static String getWebElementText(WebElement elementName)
	{
		APP_LOGGER.startFunction("getRowCount_tbl - String");
		
		try {
			
			return elementName.getText();
					
		}catch (Exception e){
			log.error("Method Browser text | Exception - " + e.toString());
			return Globals.KEYWORD_FAIL;	
			
		}	
	}
	
	
	/**************************************************************************************************
	 * Method to wait till user specified time interval for an element to be displayed.Default time interval is defined by Globals.gImplicitlyWait.
	 * This method will poll based on the interval defined in Globals.gPollingInterval seconds.
	 * Method uses FluentWait - visibilityOf method 
	 * @param element -  WebElement on which the method has to wait
	 * @param timeOutinSeconds - long - time interval in seconds till which the method until the element is displayed
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author Anand Unnikrishnan
	 * @created 1/25/2016
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String waitTillElementDisplayed(WebElement element,long timeOutinSeconds){
		APP_LOGGER.startFunction("waitTillElementDisplayed - WebElement");
		String retval=Globals.KEYWORD_PASS;
		
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
		
		if (timeOutinSeconds <= 0.00){
			timeOutinSeconds = Globals.gImplicitlyWait;
		}
		
		wait.withTimeout(timeOutinSeconds, TimeUnit.SECONDS);
        wait.pollingEvery(Globals.gPollingInterval, TimeUnit.SECONDS);
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(InvalidSelectorException.class);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			log.info("Method waitTillElementDisplayed | Object "+element.toString()+" displayed within Timeputperiod = " + timeOutinSeconds );
			
		} catch (Exception e) {
			
			retval=Globals.KEYWORD_FAIL;
			log.error("Method waitTillElementDisplayed.Waiting period timeout - "+timeOutinSeconds +" and object "+ element.toString() +" is not present | Exception - " + e.toString());
		}
		
		return retval;
	}
	
	
	/**************************************************************************************************
	 * Method to wait till user specified time interval for an element to be displayed.Default time interval is defined by Globals.gImplicitlyWait.
	 * This method will poll based on the interval defined in Globals.gPollingInterval seconds.
	 * Method uses Fluent Wiat - visibilityOfElementLocated to locate the element.
	 * @param element -  WebElement on which the method has to wait
	 * @param timeOutinSeconds - long - time interval in seconds till which the method until the element is displayed
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String waitTillElementDisplayed(String elementName,long timeOutinSeconds){
		APP_LOGGER.startFunction("waitTillElementDisplayed - String elementName - "+elementName);
		String retval=Globals.KEYWORD_PASS;
		
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
		
		if (timeOutinSeconds <= 0.00){
			timeOutinSeconds = Globals.gImplicitlyWait;
		}
		
		wait.withTimeout(timeOutinSeconds, TimeUnit.SECONDS);
        wait.pollingEvery(Globals.gPollingInterval, TimeUnit.SECONDS);
        wait.ignoring(NoSuchElementException.class);
        
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorAccess.getLocator(elementName)));
			log.info("Method waitTillElementDisplayed | Object "+elementName+" displayed within Timeputperiod = " + timeOutinSeconds );
			
		} catch (Exception e) {
			
			retval=Globals.KEYWORD_FAIL;
			log.error("Method waitTillElementDisplayed.Waiting period timeout - "+timeOutinSeconds +" and object "+ elementName +" is not present | Exception - " + e.toString());
		}
		
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to select a value from a drop down list by visible text
	 * @param element -  WebElement on which a value needs to be selected
	 * @param value - String - value that needs to be selected from the drop down list
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String selectValue_byVisibleText(WebElement element,String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("selectValue_byVisibleText - WebElement ");
		String retval=Globals.KEYWORD_FAIL;
		String tempVal=	Globals.KEYWORD_FAIL;
		Select select=null;
		
		try {
			if(seleniumCommands.isEnabled(element, reportEventFlag).equalsIgnoreCase(Globals.KEYWORD_PASS)){
				select = new Select(element);
						
				/*for(WebElement option:select.getOptions()){
					String optionVal = option.getText();
					if(optionVal.equalsIgnoreCase(value)){
						select.selectByVisibleText(value);
						Thread.sleep(1000);
						tempVal = Globals.KEYWORD_PASS;
						break;
					}
				}*/
				
				if(select.getFirstSelectedOption().equals(value)){
					log.debug("Unable to select - "+value + " as this values is already selected in the dropdown");
				}else{
					try{
						select.selectByVisibleText(value);
						tempVal = Globals.KEYWORD_PASS;
					}
					catch(NoSuchElementException e){// NoSuchElementException - If no matching option elements are found in dropdown
						log.error("Unable to select - "+value + " as this values doesnt exists in the dropdown");
						return Globals.KEYWORD_FAIL;
					}
				}
				
				if(tempVal.equalsIgnoreCase(Globals.KEYWORD_PASS)){
					WebElement selectedOption = select.getFirstSelectedOption();
					String selectedVal=selectedOption.getText();
					log.debug("Dropdown value selected - "+value );
					if(selectedVal.equalsIgnoreCase(value)){
						log.debug("Dropdown value selected and verified after selection - "+value );
						retval=Globals.KEYWORD_PASS;
					}
					
				}else{
					log.error("Unable to select Value from dropdown - "+value);
				}
				
			}			
		}catch (Exception e){
			log.error(element.toString() + "Unable to select dropdown value -"+value + " | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to select a value from a drop down list by visible text
	 * @param elementName -  Name of the element corresponding in OR
	 * @param value - String - value that needs to be selected from the drop down list
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String selectValue_byVisibleText(String elementName, String value,boolean...reportEventFlag){
		APP_LOGGER.startFunction("selectValue_byVisibleText - WebElement ");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;	
		try {
			element=createWebElement(elementName);
			retval= selectValue_byVisibleText(element, value, reportEventFlag);
					
		}catch (Exception e){
			log.error(element.toString() + "Unable to select dropdown value -"+value + " | Exception - " + e.toString());
			
		}
				
		return retval;
	}

	
	/**************************************************************************************************
	 * Method to check a checkbox
	 * @param element -  WebElement-CheckBox element which needs to be checked
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String checkCheckBox(WebElement element){
		APP_LOGGER.startFunction("checkCheckBox - WebElement ");
		String retval=Globals.KEYWORD_FAIL;
		String tempVal=Globals.KEYWORD_FAIL;
		try{
			tempVal = isSelected(element);
			if(tempVal.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				
				element.click();
				log.debug("Checkbox being selected");
				
				if(isSelected(element).equalsIgnoreCase(Globals.KEYWORD_PASS)){
					log.info("Checkbox Selected");
					retval=Globals.KEYWORD_PASS;
					
				}else {
					log.error("Checkbox Not Selected");
				}
				
			}else{
				log.info("Checkbox Already selected Selected");
				retval=Globals.KEYWORD_PASS;
			}
		}catch (Exception e){
			log.error(element.toString() + "Unable to select checkbox | Exception - " + e.toString());
		}
		
		return retval;	
	}
	
	/**************************************************************************************************
	 * Method to check a checkbox
	 * @param elementName -  String- Name of the checkbox corresponding in the OR
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String checkCheckBox(String elementName){
		APP_LOGGER.startFunction("checkCheckBox - String ");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element=null;	
		try {
			element=createWebElement(elementName);
			retval= checkCheckBox(element);
					
		}catch (Exception e){
			log.error(element.toString() + "Unable to select checkbox | Exception - " + e.toString());
			
		}
				
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to verify the expected page title with the current page title
	 * @param expectedTitle -  String- Expected title of the page
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String verifyPageTitle(String expectedTitle){
		APP_LOGGER.startFunction("verifyPageTitle  ");
		String retval=Globals.KEYWORD_FAIL;
		String currentPageTitle="";
		try{
			currentPageTitle=driver.getTitle().toLowerCase().trim();
			expectedTitle = expectedTitle.toLowerCase().trim();
			if(currentPageTitle.contains(expectedTitle)){
				retval=Globals.KEYWORD_PASS;
				log.debug("Page title verified | Expected - "+expectedTitle + " | Actual - "+currentPageTitle);
			}else{
				retval=Globals.KEYWORD_FAIL;
				log.error("Page title MISMATCH | Expected - "+expectedTitle + " | Actual - "+currentPageTitle);
			}
		}catch(Exception e){
			log.error("Method-verifyPageTitle | Exception - "+ e.toString());
		}
		return retval;
	}

	/**************************************************************************************************
	 * Method to wait till user specified time interval for an element to be displayed.
	 * This method will poll based on the interval defined in Globals.gPollingInterval seconds.
	 * For loop is used to check whether the object is displayed or not
	 * @param elementName -  String-name of the element corresponding in the OR
	 * @param timeOutinSeconds - long - time interval in seconds till which the method until the element is displayed
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String waitforElementToDisplay(String elementName,long timeOutInSeconds){
		APP_LOGGER.startFunction("waitforElementToDisplay");
		String retval=Globals.KEYWORD_FAIL;
		int pollingInterval=(int) Globals.gPollingInterval;
		By locator = null;
		try
		{	// our execution configures implicitwait timeout to be 20 secs.Hence the below for loop would be executed for 20 sec * timeout specified.
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		
			System.out.println("Start waitforElementToDisplay>>>>>>>>> " + elementName );
			locator = LocatorAccess.getLocator(elementName);
			for(int i=1;i<=timeOutInSeconds;i=i+pollingInterval){

				try{
					
					if(driver.findElements(locator).size()>0 && driver.findElement(locator).isDisplayed()==true){
						retval=Globals.KEYWORD_PASS;
						log.debug("Element - "+elementName+ " displayed. Time Waited - "+i);
						break;
					}
					else{
						
						Thread.sleep(pollingInterval*1000);
						
					}
				}catch(Exception e){
					continue;
				}
				
					
			}

		}
		catch(Exception e)
		{
			log.error("Method-waitforElementToDisplay | Exception - "+ e.toString());
		}
		finally {
			// Resetting the implicit wait timeout period as per the selenium config properties
			driver.manage().timeouts().implicitlyWait(Globals.gImplicitlyWait, TimeUnit.SECONDS);
		}
		return retval;
	}	
	/**************************************************************************************************
	 * Method to move the mouse cursor to an element
	 * @param element -  WebElement - Element to which the mouse cursor will be moved
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String mouseMoveToElement(WebElement element, boolean...reportEventFlag){
		APP_LOGGER.startFunction("mouseMoveToElement");
		String retval=Globals.KEYWORD_FAIL;
		
		try{
			retval=isDisplayed(element, reportEventFlag);
			if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				Actions mouseAction = new Actions(driver);
				mouseAction.moveToElement(element).build().perform();
				Thread.sleep(2000);
				
			}else{
				retval=Globals.KEYWORD_FAIL;
				log.error("Unable to mouse Move To Element as Element is not displayed");
			}
			
		}catch(Exception e){
			log.error("Method-mouseMoveToElement | Exception - "+ e.toString());
		}
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to click on an WebElement using Action class
	 * @param element -  WebElement - Element to which the mouse cursor will be moved
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String clickElementUsingAction(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("clickElementThroughAction");
		String retval=Globals.KEYWORD_FAIL;
		
		try{
			retval=isDisplayed(element, reportEventFlag);
			if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				Actions mouseAction = new Actions(driver);
				mouseAction.click(element).build().perform();
				Thread.sleep(1000);
				
			}else{
				retval=Globals.KEYWORD_FAIL;
				log.error("Method- clickElementThroughAction | Unable to click on Element as Element is not displayed");
			}
			
		}catch(Exception e){
			log.error("Method-clickElementThroughAction | Exception - "+ e.toString());
		}
		return retval;
	}
	
	/**************************************************************************************************
	 * Method to double click on an WebElement using Action class
	 * @param element -  WebElement - Element to which the mouse cursor will be moved
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String dblClickElementUsingAction(WebElement element,boolean...reportEventFlag){
		APP_LOGGER.startFunction("dblClickElementThroughAction");
		String retval=Globals.KEYWORD_FAIL;
		
		try{
			retval=isDisplayed(element, reportEventFlag);
			if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				Actions mouseAction = new Actions(driver);
				mouseAction.doubleClick(element).build().perform();
				Thread.sleep(1000);
				
			}else{
				retval=Globals.KEYWORD_FAIL;
				log.error("Method- dblClickElementThroughAction | Unable to double click on Element as Element is not displayed");
			}
			
		}catch(Exception e){
			log.error("Method-dblClickElementThroughAction | Exception - "+ e.toString());
		}
		return retval;
	}
	

	/**************************************************************************************************
	 * Method to display all the items for a given HashMap<String,String>
	 * @param hm -  HashMap<String,String> -Hashmap whose key,value pair needs to be displayed
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static void displayAllItem_fromHashMap(HashMap<String,String> hm){
		
		for (String name: hm.keySet()){

            String key =name.toString();
            String value = hm.get(name).toString();  
//            System.out.println(key + " " + value);  
            log.debug(" Hashmap Key "+ key + "| Value "+value);

		} 
	}
	/**************************************************************************************************
	 * Method to uncheck a checkbox
	 * @param element -  WebElement- Checkbox element which needs to be unchecked
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String uncheckCheckBox(WebElement element){
		APP_LOGGER.startFunction("uncheckCheckBox - WebElement ");
		String retval=Globals.KEYWORD_FAIL;
		String tempVal=Globals.KEYWORD_FAIL;
		try{
			tempVal = isSelected(element);
			if(tempVal.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				
				log.info("Checkbox Already Unchecked");
				retval=Globals.KEYWORD_PASS;
				
			}else{
				
				element.click();
				log.debug("Checkbox being selected");
				
				if(isSelected(element).equalsIgnoreCase(Globals.KEYWORD_FAIL)){
					log.info("Checkbox unchecked");
					retval=Globals.KEYWORD_PASS;
				}
			
			}
		}catch (Exception e){
			log.error(element.toString() + "Unable to UN-CHECK checkkbox | Exception - " + e.toString());
		}
		
		return retval;	
	}
	
	/**************************************************************************************************
	 * Method to uncheck a checkbox
	 * @param elementName -  String- Checkbox element name corresponding in the OR
	 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String uncheckCheckBox(String elementName){
		APP_LOGGER.startFunction("uncheckCheckBox - String ");
		String retval=Globals.KEYWORD_FAIL;
		WebElement element = null;

		try{
			element = createWebElement(elementName);
			retval  = uncheckCheckBox(element);
			
		}catch (Exception e){
			log.error(element.toString() + "Unable to UN-CHECK checkkbox | Exception - " + e.toString());
		}
		
		return retval;	
	}
	
	/**************************************************************************************************
	 * Method to 3 character month name into full month name
	 * @param monthPrefix -  String- 3 character month name e.g Jan/Feb
	 * @return String-Full month Name if successful, null otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String convertToFullMonthName(String monthPrefix){
		APP_LOGGER.startFunction("convertToFullMonthName - String ");
		String monthNameInFull =null;
		
		switch(monthPrefix){
	    case "Jan":
	    	monthNameInFull = "January";
	       break; //optional
	    case "Feb" :
	    	monthNameInFull = "February";
	       break; //optional
	    case "Mar" :
	    	monthNameInFull = "March";
	       break;
	    case "Apr" :
	    	monthNameInFull = "April";
	       break;
	    case "May" :
	    	monthNameInFull = "May";
	       break;
	    case "Jun" :
	    	monthNameInFull = "June";
	       break;
	    case "Jul" :
	    	monthNameInFull = "July";
	       break;
	    case "Aug" :
	    	monthNameInFull = "August";
	       break;
	    case "Sep" :
	    	monthNameInFull = "September";
	       break;
	    case "Oct" :
	    	monthNameInFull = "October";
	       break;
	    case "Nov" :
	    	monthNameInFull = "November";
	       break;
	    case "Dec" :
	    	monthNameInFull = "December";
	       break;
	    default : //Optional
	    	log.error( "Unable to convert into month Name- Input: " + monthPrefix);
		}
		return monthNameInFull;
	}
	
	/**************************************************************************************************
	 * Method to full month name into number
	 * @param fullMonthName -  String-Full month name like January/February
	 * @return String- month number like 1 for January etc if successful, null otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String convertFullMonthNameToNumber(String fullMonthName){
		APP_LOGGER.startFunction("convertToFullMonthName - String ");
		String monthNumber=null;
		
		switch(fullMonthName){
	    case "January":
	    	monthNumber = "1";
	       break; //optional
	    case "February" :
	    	monthNumber = "2";
	       break; //optional
	    case "March" :
	    	monthNumber = "3";
	       break;
	    case "April" :
	    	monthNumber = "4";
	       break;
	    case "May" :
	    	monthNumber = "5";
	       break;
	    case "June" :
	    	monthNumber = "6";
	       break;
	    case "July" :
	    	monthNumber = "7";
	       break;
	    case "August" :
	    	monthNumber = "8";
	       break;
	    case "September" :
	    	monthNumber = "9";
	       break;
	    case "October" :
	    	monthNumber = "10";
	       break;
	    case "November" :
	    	monthNumber = "11";
	       break;
	    case "December" :
	    	monthNumber = "12";
	       break;
	    default : //Optional
	    	log.error( "Unable to convert into month Name- Input: " + fullMonthName);
		}
		return monthNumber;
	}
	
	/**************************************************************************************************
	 * Method to generate dynamic string based on the lengthTobeGenerated based on timestamp
	 * @param lengthTobeGenerated -  int-Length of the dynamic string which needs to be generated
	 * @return String- lengthTobeGenerated character String if successful, null otherwise
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String runtimeGeneratedStringValue(int lengthTobeGenerated){
		
		String generatedString= null;
		String retval = null;
		
		Date dNow = new Date( );
		//hh - hour , mm-minutes ss-seconds , S- miliseconds(3 digits) 
		SimpleDateFormat ft =  new SimpleDateFormat ("hhmmssSyyyyMMddS"); //totoal 20 characters
        generatedString  = ft.format(dNow);
	    if(lengthTobeGenerated >= generatedString.length()){
	    	retval = generatedString;
	    }else{
	    	 retval=generatedString.substring(0, lengthTobeGenerated);
	    }
       return retval;
	   
	}
	
	
	/**************************************************************************************************
	 * Method to update and fetch the test data value based on the runtime value generation flag
	 * @param flagColumnName -  String-ColumnName in test data for testDataColName where either Yes/No value can be present.
	 * @param testDataColName String - testDataColName whose value needs to be updated if flagColumnName value is yes, else existing value will be fetched
	 * @return String- Runtime value for the column or the existing value of the test data column
	 * @author aunnikrishnan
	 * @created
	 * @LastModifiedBy
	 ***************************************************************************************************/
	public static String updateAndFetchRuntimeValue(String flagColumnName,String testDataColName,int lengthOfStringTobeGen){
		String dynamicVal = null;
		
		if(Globals.testSuiteXLS.getCellData_fromTestData(flagColumnName).equalsIgnoreCase("Yes")){
			
			dynamicVal = RandomStringUtils.randomAlphabetic(lengthOfStringTobeGen);
						
			Globals.testSuiteXLS.setCellData_inTestData(testDataColName, dynamicVal);
			log.debug(" Method-updateAndFetchRuntimeValue | Flag - "+flagColumnName +"| " +testDataColName +" Dynamic Value - "+dynamicVal);
		}else{
			dynamicVal = Globals.testSuiteXLS.getCellData_fromTestData(testDataColName);
			log.debug(" Method-updateAndFetchRuntimeValue | Flag - "+flagColumnName +"| " +testDataColName +" Static Value - "+dynamicVal);
		}
		return dynamicVal;
	}






	//----------------------------------
		/**
		 * Method which verifies Property value for the Property Passed as param for the WebElement Passed as param.
		 * @param element 			- WebElement the property for which needs to be verified.
		 * @param PropertyName 		- Property for the Element that needs to be verified.
		 * @param ExpPropertyValue 	- Expected Property Value for the Property passed.
		 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
		 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
		 * @author 					- Mandeep Kohli
		 * @created 				- Jan 2016
		 * @LastModifiedBy 			- Mandeep Kohli
		 * @LastModifiedDate 		- Feb 2016
		 */

		public static String verifyProperty(WebElement element, String PropertyName, String ExpPropertyValue, boolean...reportEventFlag)
		{
			APP_LOGGER.startFunction("verifyProperty - WebElement");
			String retval			= Globals.KEYWORD_FAIL;
			String ActPropertyValue = null;
			
			// using reportEventFlag
			try{
				
					// Property Name Passed is blank then Log Error
					if(PropertyName == null || PropertyName == "" || PropertyName.isEmpty())
					{
						log.error("Element -" + element.toString() + "| Please Pass Correct Property Name to the Function. Property Name is Blank Or NULL ");
						retval	= Globals.KEYWORD_FAIL;
						return retval;
					}
					
					String elementDisplayed = isDisplayed(element, reportEventFlag);
					// If Exception then what if exists - then no need for else
					if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
						 retval = Globals.KEYWORD_FAIL;	
						 return retval;
					}	
					
					// Converting Expected Property value to Space if null or empty
					ExpPropertyValue = CheckAndTrimString(ExpPropertyValue, reportEventFlag);
					if (ExpPropertyValue == null) 
						{
							retval = Globals.KEYWORD_FAIL;	
							return retval;			
						}
					
					try
					{
						//ActPropertyValue = element.getAttribute(" + PropertyName + ").trim(); //what if this returns null ? 
						ActPropertyValue = element.getAttribute(PropertyName).trim(); //what if this returns null ?
					}
					
					catch (Exception e)
					{
						log.error("Property - " + PropertyName + " may not be suported by getAttribute method & cannot be retrieved for Element - "  + element.toString() + " | Exception - " + e.toString());
						retval = Globals.KEYWORD_FAIL;	
						return retval;			
					}
					
					// Converting Actual Property value to Space if null or empty
					// Converting Expected Property value to Space if null or empty
					ActPropertyValue = CheckAndTrimString(ActPropertyValue, reportEventFlag);
					if (ActPropertyValue == null) 
						{
							retval = Globals.KEYWORD_FAIL;	
							return retval;			
						}
					
					//if(ActPropertyValue == ExpPropertyValue ){
					if(ActPropertyValue.equalsIgnoreCase(ExpPropertyValue)){	
							 log.info("Field :- " + element.toString() + " | Property Values Match. Property :- " + PropertyName + "Expected Value :- " + ExpPropertyValue + ". Actual Value :- " + ActPropertyValue );
							 retval = Globals.KEYWORD_PASS;
					}else{
							 log.info("Field :- " + element.toString() + " | Property Values DO NOT Match. Property :- " + PropertyName + "Expected Value :- " + ExpPropertyValue + ". Actual Value :- " + ActPropertyValue );
							 retval = Globals.KEYWORD_FAIL;
					}
					
					//reporter has been already logged above.
					if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
			            //reporter
						log.info("verifyProperty result for Element - " + element.toString()+ " is being reported in RESULTS  ");
						//TODO call reporterfunction
//						System.out.println("report");
					} 
						 
					return retval;
				}//try end
			catch(Exception e)
				{
					log.error("Main Try - Property - " + PropertyName + " cannot be retrieved for Element - "  + element.toString() + " | Exception - " + e.toString());
					retval = Globals.KEYWORD_FAIL;	
					return retval;			
				}
			}
		
		/**
		 * Method which verifies Property value for the Property Passed as param for the WebElement Name Passed as param.
		 * @param elementName		- WebElement Name in the Object Repositories Properties File - the property for which needs to be verified 
		 * @param PropertyName 		- Property for the Element that needs to be verified.
		 * @param ExpPropertyValue 	- Expected Property Value for the Property passed.
		 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
		 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
		 * @author 					- Mandeep Kohli
		 * @created 				- Jan 2016
		 * @LastModifiedBy 			- Mandeep Kohli
		 * @LastModifiedDate 		- Feb 2016
		 */

		public static String verifyProperty(String elementName, String PropertyName, String ExpPropertyValue, boolean...reportEventFlag){
			APP_LOGGER.startFunction("verifyProperty - String");
			String retval		= Globals.KEYWORD_FAIL;
			WebElement element	= null;
			
			try {
				element = createWebElement(elementName);
				retval 	= verifyProperty(element, PropertyName, ExpPropertyValue, reportEventFlag);
				return retval;
			}
			catch (Exception e){
				log.error("Element -"+ elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
				retval		= Globals.KEYWORD_FAIL;
				return retval;
			}
					
		}
	
		/**
		 * Method which verifies Multiple Properties for the WebElement Passed.
		 * @param elementName				- WebElement for which Properties need to be verified.
		 * @param ExpPropertyValue Array 	- Expected Property Values for the Properties passed.
		 * @param reportEventFlag 			- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
		 * @return 							- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
		 * @author 							- Mandeep Kohli
		 * @created 						- Jan 2016
		 * @LastModifiedBy 					- Mandeep Kohli
		 * @LastModifiedDate 				- Feb 2016
		 */
		
		public static String verifyProperties(WebElement element, String[] PropertyNames, String[] ExpPropertyValues, boolean...reportEventFlag){
			APP_LOGGER.startFunction("verifyProperties - WebElement");
			String retval			= Globals.KEYWORD_FAIL;
			String retvalForAll		= Globals.KEYWORD_PASS;

			try{
			
					String elementDisplayed = isDisplayed(element, reportEventFlag);
					// If Exception then what if exists - then no need for else
					if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
						 retval = Globals.KEYWORD_FAIL;	
						 return retval;
					}	
		
					for (int i = 0; i < PropertyNames.length; i++){
							retval	= Globals.KEYWORD_FAIL;
							retval 	= verifyProperty(element, PropertyNames[i], ExpPropertyValues[i], reportEventFlag);
								
							if(retval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
								retvalForAll = Globals.KEYWORD_FAIL;
							}
						}
					
					//reporter has been already logged above.
					if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
			            //reporter
						log.info("verifyProperties result for Element - " + element.toString()+ " is being reported in RESULTS  ");
						//TODO call reporterfunction
//						System.out.println("report");
					}
					return retvalForAll;
			}//try End
			catch(Exception e)
			{
				log.error("Main Try - VerifyProperties - cannot be retrieved for Element - "  + element.toString() + " | Exception - " + e.toString());
				retvalForAll = Globals.KEYWORD_FAIL;	
				return retvalForAll;			
			}
			
		}	
	
		/**
		 * Method which verifies Multiple Properties for the WebElement Name Passed.
		 * @param elementName				- WebElement Name in the Object Repositories Properties File - the property for which needs to be verified 
		 * @param PropertyNames Array		- Properties for the Element that needs to be verified.
		 * @param ExpPropertyValue Array 	- Expected Property Values for the Properties passed.
		 * @param reportEventFlag 			- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
		 * @return 							- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
		 * @author 							- Mandeep Kohli
		 * @created 						- Jan 2016
		 * @LastModifiedBy 					- Mandeep Kohli
		 * @LastModifiedDate 				- Feb 2016
		 */
	
		public static String verifyProperties(String elementName, String[] PropertyNames, String[] ExpPropertyValues, boolean...reportEventFlag){
			APP_LOGGER.startFunction("verifyProperties - String");
			String retval		= Globals.KEYWORD_FAIL;
			WebElement element	= null;
			
			try {
				element = createWebElement(elementName);
				retval 	= verifyProperties(element, PropertyNames, ExpPropertyValues, reportEventFlag);
				return retval;
			}
			catch (Exception e){
				log.error("Element -"+ elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
				retval		= Globals.KEYWORD_FAIL;
				return retval;
			}
					
		}
		
		//----------------------------------
		
			/**
			 * Method which verifies the Expected Text with the text retrieved through getText method for the WebElement Passed.
			 * @param element 			- WebElement for which Text Needs to be verified using gettext.
			 * @param ExpectedText 		- Expected Text for WebElement retrieved using gettext.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */
		
			public static String verifyText(WebElement element, String ExpectedText, boolean...reportEventFlag){
				APP_LOGGER.startFunction("verifyText - WebElement");
				String retval			= Globals.KEYWORD_FAIL;
				String ActElementText 	= null;

				// using reportEventFlag
				try{
					
					String elementDisplayed = isDisplayed(element, reportEventFlag);
					// If Exception then what if exists - then no need for else
					if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
						 retval = Globals.KEYWORD_FAIL;	
						 return retval;
					}	
				
					// Converting Expected Property value to Space if null or empty
					ExpectedText = CheckAndTrimString(ExpectedText, reportEventFlag);
					if (ExpectedText == null) 
						{
							retval = Globals.KEYWORD_FAIL;	
							return retval;			
						}
					
					try {
						ActElementText = element.getText().trim(); //what if this returns null ? 
					}
					catch (Exception e){
						log.error("Text cannot be retrieved for Element - "  + element.toString() + " | Exception - " + e.toString());
						retval = Globals.KEYWORD_FAIL;	
						return retval;
					}
				
					ActElementText = CheckAndTrimString(ActElementText, reportEventFlag);
					if (ActElementText == null) 
						{
							retval = Globals.KEYWORD_FAIL;	
							return retval;			
						}

					//if(ActElementText == ExpectedText ){
					if(ActElementText.equalsIgnoreCase(ExpectedText)){	
							
							 log.info("Field :- " + element.toString() + " | Element Text Matches. Expected Text :- " + ExpectedText + ". Actual Text :- " + ActElementText );
							 retval = Globals.KEYWORD_PASS;
					}else{
						log.info("Field :- " + element.toString() + " | Element Text DO NOT Match. Expected Text :- " + ExpectedText + ". Actual Text :- " + ActElementText );
						 retval = Globals.KEYWORD_FAIL;
					}
				
					//reporter has been already logged above.
					if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
			            //reporter
						log.info("verifyText result for Element - " + element.toString()+ " is being reported in RESULTS  ");
						//TODO call reporterfunction
//						System.out.println("report");
					} 
					return retval;
				}//try end
				catch(Exception e)
				{
					log.error("Main Try - verifyText - cannot be done for Element - "  + element.toString() + " | Exception - " + e.toString());
					retval = Globals.KEYWORD_FAIL;	
					return retval;			
				}
		}
			
			/**
			 * Method which verifies the Expected Text with the text retrieved through getText method for the WebElement Name Passed.
			 * @param elementName 		- WebElement Name in the OR Properties file for which Text Needs to be verified using gettext.
			 * @param ExpectedText 		- Expected Text for WebElement retrieved using gettext.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */

			public static String verifyText(String elementName, String ExpectedText, boolean...reportEventFlag){
				APP_LOGGER.startFunction("verifyText - String");
				String retval 		= Globals.KEYWORD_FAIL;
				WebElement element	= null;
				
				try {
					element = createWebElement(elementName);
					retval 	= verifyText(element, ExpectedText, reportEventFlag);
					return retval;
				}
				catch (Exception e){
					log.error("Element -"+ elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
					retval 		= Globals.KEYWORD_FAIL;
					return retval;
				}
			}
		

			//----------------------------------
			
			/**
			 * Method which verifies the Expected Text with the text retrieved through Innertext method for the WebElement Passed.
			 * @param element 			- WebElement for which Text Needs to be verified using Innertext.
			 * @param ExpectedText 		- Expected Innertext Property Value for the element.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */
			
			public static String verifyInnerText(WebElement element, String ExpectedText, boolean...reportEventFlag){
				APP_LOGGER.startFunction("verifyInnerText - WebElement");
				String retval			= Globals.KEYWORD_FAIL;
				String ActElementText 	= null;
				// using reportEventFlag
				try{
					
							String elementDisplayed = isDisplayed(element, reportEventFlag);
							// If Exception then what if exists - then no need for else
							if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
								 retval = Globals.KEYWORD_FAIL;	
								 return retval;
							}	
						
							// Converting Expected Property value to Space if null or empty
							ExpectedText = CheckAndTrimString(ExpectedText, reportEventFlag);
		
							try {
								ActElementText = element.getAttribute("innertext").trim();
							}
							catch (Exception e){
								log.error("Text cannot be retrieved for Element - "  + element.toString() + " | Exception - " + e.toString());
								 retval = Globals.KEYWORD_FAIL;	
								 return retval;
							}
								
							ActElementText = CheckAndTrimString(ActElementText, reportEventFlag);
							if (ActElementText == null) 
								{
									retval = Globals.KEYWORD_FAIL;	
									return retval;			
								}
				
							//if(ActElementText == ExpectedText ){
							if(ActElementText.equalsIgnoreCase(ExpectedText)){	
									
									 log.info("Field :- " + element.toString() + " | Element Text Matches. Expected Text :- " + ExpectedText + ". Actual Text :- " + ActElementText );
									 retval=Globals.KEYWORD_PASS;
							}else{
								log.info("Field :- " + element.toString() + " | Element Text DO NOT Match. Expected Text :- " + ExpectedText + ". Actual Text :- " + ActElementText );
								 retval=Globals.KEYWORD_FAIL;
								}
				
							//reporter has been already logged above.
							if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
					            //reporter
								log.info("verifyProperty result for Element - " + element.toString()+ " is being reported in RESULTS  ");
								//TODO call reporterfunction
//								System.out.println("report");
							} 
					return retval;
				}//try end
				catch(Exception e)
				{
					log.error("Main Try - verifyText - cannot be done for Element - "  + element.toString() + " | Exception - " + e.toString());
					retval = Globals.KEYWORD_FAIL;	
					return retval;			
				}
		}
			
			/**
			 * Method which verifies the Expected Text with the text retrieved through Innertext method for the WebElement Passed.
			 * @param elementName 		- WebElement Name in the OR Properties file for which Text Needs to be verified using Innertext.
			 * @param ExpectedText 		- Expected Innertext Property Value for the element.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */
	
			public static String verifyInnerText(String elementName, String ExpectedText, boolean...reportEventFlag){
				APP_LOGGER.startFunction("verifyInnerText - String");
				String retval		= Globals.KEYWORD_FAIL;
				WebElement element	= null;
				
				try {
					element = createWebElement(elementName);
					retval 	= verifyInnerText(element, ExpectedText, reportEventFlag);
					return retval;
				}
				catch (Exception e){
					log.error("Element -"+ elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
					retval		= Globals.KEYWORD_FAIL;
					return retval;
				}
			}
		
		
			/**
			 * Method which retrieves the Column Count for the WebElement Table Passed.
			 * @param element 			- WebElement Table for which Headers need to be verified which has tbale header i.e th
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- column count or -1 .
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Anand Unnikrishnan
			 * @LastModifiedDate 		- Aug 2016
			 */			
			
		// to take actual & exp prop comparison inside if
			public static int getColumnCount_tbl(WebElement Table, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getColumnCount_tbl - WebElement");
				int retval		= -1;

				try{
					
					retval = getColumnCount_tbl(Table,"./*/tr[1]/th",reportEventFlag);
					 
				}catch(Exception e){
					log.error("Main Try - getColumnCount_tbl - Column Count cannot be retrieved for  - "  + Table.toString() + " | Exception - " + e.toString());
					retval = -1;	
								
				}
				return retval;
		}
			/**************************************************************************************************
			 * Method to obtain the column count  count of the web table 
			 * @param element - WebElement Table for which Headers need to be verified.
			 * @param relativePathOfTableRow - String - relative of table row e.g. './tr[1]/th' etc
			 * @return int - Globals.INT_FAIL if no rows could be obtained / Row count if table exists.
			 * @author aunnikrishnan
			 * @created
			 * @LastModifiedBy
			 ***************************************************************************************************/
			public static int getColumnCount_tbl(WebElement Table,String relativePathOfTableRow, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getColumnCount_tbl - WebElement");
				int retval		= -1;
				
				try{
					
							String elementDisplayed = isDisplayed(Table, reportEventFlag);
							// If Exception then what if exists - then no need for else
							if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
								 retval = -1;	
								 return retval;
							}	
				
					    	try{

					    	List<WebElement> tCols = Table.findElements(By.xpath(relativePathOfTableRow)); 
							
					    	retval = tCols.size();

					  		}
							catch (Exception e){
				       			log.error("Column Count could Not be retrieved for Table - " + Table.toString() + " | Exception - " + e.toString());
				       			retval=-1;
				       			return retval;
							}
			
							//reporter has been already logged above.
							if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
					            //reporter
								log.info("getColumnCount_tbl result for Table - " + Table.toString()+ " is being reported in RESULTS  ");
								//TODO call reporterfunction
//								System.out.println("report");
							} 
				
			}//try end
			catch(Exception e)
			{
				log.error("Main Try - getColumnCount_tbl - Column Count cannot be retrieved for  - "  + Table.toString() + " | Exception - " + e.toString());
				retval = -1;	
							
			}
				return retval;	
		}
		
			/**
			 * Method which retrieves the Column Count for the String Table Name Passed.
			 * @param elementName 		- WebElement Table Name in the OR properties file for which Headers need to be verified.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- column count or -1 .
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Anand Unnikrishnan
			 * @LastModifiedDate 		- Aug 2016
			 */			
	
			public static int getColumnCount_tbl(String TableName, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getColumnCount_tbl - String");

				int retval		= -1;
				WebElement Table=null;
				
				try {
					Table = createWebElement(TableName);
					retval 	= getColumnCount_tbl(Table,"./*/tr[1]/th", reportEventFlag);
					return retval;
				}
				catch (Exception e){
					log.error("Table -"+ TableName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
					retval = -1;	
					return retval;
				}
			}
	
			public static int getColumnCount_tbl(String TableName,String relativePathOfTableRow, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getColumnCount_tbl - String");

				int retval		= -1;
				WebElement Table=null;
				
				try {
					Table = createWebElement(TableName);
					retval 	= getColumnCount_tbl(Table, relativePathOfTableRow, reportEventFlag);
					
				}
				catch (Exception e){
					log.error("Table -"+ TableName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
					retval = -1;	
					
				}
				
				return retval;
			}
	
	
			/**
			 * Method which retrieves the Column Count for the String Table Name Passed.
			 * @param Table 			- WebElement Table for which Headers need to be returned.
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- Dynamic Array of Headers or null.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */			
			
			public static List<String> getHeaders_tbl(WebElement Table, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getHeaders_tbl - WebElement");
				//int retval		= -1;
				int colcount 	= -1;
				//String[] HeaderNames ;
				
				try{
					
							String elementDisplayed = isDisplayed(Table, reportEventFlag);
							// If Exception then what if exists - then no need for else
							if (elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
								 return null;
							}	
			    			List<String> HeaderNames 	= new ArrayList<String>();

							try{
							    	//List<WebElement> tCols 		= Table.findElements(By.xpath("./tbody/tr[1]/th")); //tr[2]/td
							    	colcount 					= getColumnCount_tbl(Table, reportEventFlag);
							        
							    	if(colcount == -1){
										 return null;
							    	}
//							    	System.out.println("My Table Column Count is :- "+ colcount);
							  	}
							catch (Exception e)
								{
						       			log.error("Column Count could Not be retrieved for Table - " + Table.toString() + " | Exception - " + e.toString());
						       			return null;
								}
					  		
					  		String strHeaderName = "";
							for(int i=1; i <= colcount; i++)
					  		{
					  			try
					  			{
					  				//HeaderNames[i] =  Table.findElement(By.xpath("./tbody/tr[1]/th[i]")).getText();
					  				//HeaderNames =  Table.findElements(By.xpath("./tbody/tr[1]/th]")).getText();
			  						strHeaderName = Table.findElement(By.xpath("./tbody/tr[1]/th[" + i + "]")).getText().trim();
			  						HeaderNames.add(strHeaderName);
									  				//System.out.println("Header Name for Column *" + i + "* = " + HeaderNames[i]);
					  			}
					  			catch(Exception e)
					  			{
					       			log.error("Column Headers could Not be retrieved for Table - " + Table.toString() + " | Exception - " + e.toString());
					       			return null;
					  			}
					  		}
			
							//reporter has been already logged above.
							if (reportEventFlag.length > 0 && reportEventFlag[0] == true) {
				            //reporter
							log.info("getColumnCount_tbl result for Table - " + Table.toString()+ " is being reported in RESULTS  ");
							//TODO call reporterfunction
//							System.out.println("report");
							}
//							System.out.println("Header names from inside Funciton getHeaders_tbl = " + HeaderNames );
							return(HeaderNames);
					}// try End
				catch(Exception e){
					log.error("Main Try - getHeaders_tbl - Header Names cannot be retrieved for  - "  + Table.toString() + " | Exception - " + e.toString());
					return null;			
				}
				
			}
	
			/**
			 * Method which retrieves the Column Count for the String Table Name Passed.
			 * @param TableName			- WebElement TableName in the OR Properties file for which Headers need to be returned 
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- Dynamic Array of Headers or null.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */			
	
			public static List<String> getHeaders_tbl(String TableName, boolean...reportEventFlag){
				APP_LOGGER.startFunction("getHeaders_tbl - String");

				WebElement Table			= null;
				//arrHeaderNames = GenUtils.getHeaders_tbl(Table);
				
				try {
					Table 			= createWebElement(TableName);
					List<String> arrHeaderNames = new ArrayList<String>();
			    	arrHeaderNames	= getHeaders_tbl(Table, reportEventFlag);
					return arrHeaderNames;
				}
				catch (Exception e){
					log.error("Table -"+ TableName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
					return null;
				}
			}
	
	
			/**
			 * Method which converts null, empty or space to empty string or returns the Trimmed String if string not empty
			 * @param StringToConv		- String that needs to be checked for empty, space or null or just be trimmed. 
			 * @param reportEventFlag 	- OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
			 * @return 					- TRUE if Actual Property Value matches with the Expected Property Value/ FALSE if they do not Match.
			 * @author 					- Mandeep Kohli
			 * @created 				- Jan 2016
			 * @LastModifiedBy 			- Mandeep Kohli
			 * @LastModifiedDate 		- Feb 2016
			 */
			
			public static String CheckAndTrimString(String StringToConv, boolean...reportEventFlag)
			{
				APP_LOGGER.startFunction("CheckAndTrimString");
				String strTrimmed = null;
				
				//Converting Expected Property value to Space if null or empty
				try{
						if (StringToConv == null || StringToConv == "" || StringToConv.isEmpty()){
							strTrimmed = "";
						}
						else
						{
							strTrimmed = StringToConv.trim();
						}
						return strTrimmed;		
					}
				catch(Exception e)
					{
						log.info("Could Not Convert Null or Empty String. Error :- " + e.toString() + " is being reported in RESULTS  ");
						strTrimmed = null;
						return strTrimmed;		
					}
				
			}
	
	//Creats dir + sub directories - if already present then does not create and does not even throws error
		public static void mkDirs(String dirStructurePath){
			File abc = new File(dirStructurePath);
	    	abc.mkdirs();
		}
		
		//Creats dir - if already present then does not create and does not even throws error
			public static void mkDir(String dirStructurePath){
				File abc = new File(dirStructurePath);
		    	abc.mkdir();
			}
			
		
		//returns timestamp in format dd/MM/yyyy hh:mm:ss AM/PM
		public static String getCurrentTimeStamp(){		
			Date date = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	    	String formattedDate = sdf.format(date);
	    	return formattedDate;		
		}  
		
		//returns Unique Name like : 21_01_2016_15_03_30_AM
		public static String getUniqueName_usingCurrTimestamp(){
			String timestamp =  getCurrentTimeStamp();
			String UniqueName = timestamp.replaceAll("[/|:]| ", "_");
			return UniqueName;
		}
		
		//returns Unique Name like : 21_01_2016_15_03_30_666_AM	(up to Millisecond units)
		public static String getUniqueName_usingCurrTimestamp_uptoMS(){
			
			Date date = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SS a");
	    	String formattedDate = sdf.format(date);
			String UniqueName = formattedDate.replaceAll("[/|:]| ", "_");
			return UniqueName;
		}
		
		
		public static String takePageSnapShot() throws Exception{
			String newImageName = getUniqueName_usingCurrTimestamp_uptoMS();
			String DestPath = Globals.currentPlatform_SnapshotPath+File.separator+newImageName+".png";
//			System.out.println(DestPath);
			
			//check for browser type.For Chrome only viewport screenshot are taken by default rather than heap screenshot.
			
//			if(Globals.BrowserType.toLowerCase().contains("chrome")){
//				
//				long height =(long) ((JavascriptExecutor)driver).executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
//				int initOffset = (int)Globals.gScrollOffset;
//				int offset = 0;
//			    int  noOfScrolls=0;
//			    noOfScrolls = (int)height/initOffset;
//			    int i;
//			     
//			    String screenshotFileName;
//			    String[] images;
//			    
//			   	//check to see noOfScroll is Zero
//			    if(noOfScrolls == 0){
//			    	images =new String[1];
//			    	File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//			    	screenshotFileName = Globals.currentPlatform_SnapshotPath+"\\temp.png";
//			    	FileUtils.copyFile(scrFile, new File(screenshotFileName));
//			 		images[0]=screenshotFileName;
//    		    }else{
//    		    	images =new String[noOfScrolls];
//    		    	for(i=0;i<noOfScrolls;i++){
//   			    	 
//   			    	 File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//   			    	 screenshotFileName = Globals.currentPlatform_SnapshotPath+"\\temp"+i+".png";
//   			    	 FileUtils.copyFile(scrFile, new File(screenshotFileName));
//   			 		 images[i]=screenshotFileName;
//   			 		 
//   			    	 ((JavascriptExecutor)driver).executeScript("scroll(0,"+offset+")");
//   			    	 offset = offset + initOffset;
//   			    	  
//   			     	}
//    		    }
//			    
//			    
//			    
//			  //total height of all images
//			     int resultImgHeight=0;
//			     int resultImgWidth = 0;
//			     int y=0;
//			     
//			     for(String image : images){
//			         BufferedImage bi = ImageIO.read(new File(image));
//			         resultImgHeight = resultImgHeight + bi.getHeight();
//			         resultImgWidth = bi.getWidth();
//			     }
//			     BufferedImage result = new BufferedImage(resultImgWidth,resultImgHeight,BufferedImage.TYPE_INT_RGB);
//			     Graphics g = result.getGraphics();
//			   
//			     int newOffset=0;
//			     for(String image : images){
//			    	 File img = new File(image);
//			         BufferedImage bi = ImageIO.read(img);
//			         y=bi.getHeight();
//			
//			         g.drawImage(bi,0, newOffset, null);
//			         newOffset = newOffset + y;
//			         img.delete();
//			         
//			     }
//			     ImageIO.write(result,"png",new File(DestPath));
//			    
//			}else{
				
				TakesScreenshot snap = (TakesScreenshot)driver;
			    File TempFile = snap.getScreenshotAs(OutputType.FILE);	    
			    File DestFile=new File(DestPath);
			    FileUtils.copyFile(TempFile,DestFile);
//			}
			
			return DestPath;
		}
		
		
		
		//takeSnapshotFlag :: 0-No Snapshot Required, 1-Current Focused Page Snapshot , 2- Specific Passed Object  Snapshot (option 2 is not yet built)
		//3-option to link any file pTH MENTIONED IN Globals.Result_Link_FilePath
		public static void STAF_ReportEvent(String stepResult,String stepName, String stepDescription,int takeSnapshotFlag) {
			
			try {
				String imageName;
				if (takeSnapshotFlag==1) {
					imageName = takePageSnapShot();
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
				
//				DB-CHANGES
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
		
		public static void clickWhenElementIsClickable(String elementName,int timeOutInSeconds) {
			
			try{
				FluentWait<WebDriver> btnWait = new FluentWait<WebDriver>(driver);
				WebElement btnNext = driver.findElement(LocatorAccess.getLocator(elementName));
				
				btnWait.withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
				btnWait.pollingEvery(Globals.gPollingInterval, TimeUnit.SECONDS);
				btnWait.ignoring(NoSuchElementException.class);
				btnWait.ignoring(InvalidSelectorException.class);
				
				btnWait.until(ExpectedConditions.elementToBeClickable(btnNext));
				btnNext.click();
				Thread.sleep(1000);

			}catch (Exception e){
				log.debug("Method - clickWhenElementIsClickable | Exception occurred - "+e.toString());
				
			}
						
		}
		
		
		public static void clickWhenElementIsClickable(WebElement element,int timeOutInSeconds) throws Exception{
			
			try{
				FluentWait<WebDriver> btnWait = new FluentWait<WebDriver>(driver);
				//WebElement btnNext = driver.findElement(PropertyLoader.getLocator(elementName));
				
				btnWait.withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
				btnWait.pollingEvery(Globals.gPollingInterval, TimeUnit.SECONDS);
				btnWait.ignoring(NoSuchElementException.class);
				btnWait.ignoring(InvalidSelectorException.class);
				
				btnWait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
				Thread.sleep(1000);

			}catch (Exception e){
				log.debug("Method - clickWhenElementIsClickable | Exception occurred - "+e.toString());
				throw e;
			}
						
		}
		
		
		public static void clickWhenElementIsClickable_andSyncPage(String elementName,int timeOutInSeconds) throws Exception{
			
			try{
				FluentWait<WebDriver> btnWait = new FluentWait<WebDriver>(driver);
				WebElement btnNext = driver.findElement(LocatorAccess.getLocator(elementName));
				
				btnWait.withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
				btnWait.pollingEvery(Globals.gPollingInterval, TimeUnit.SECONDS);
				btnWait.ignoring(NoSuchElementException.class);
				btnWait.ignoring(InvalidSelectorException.class);
				
				btnWait.until(ExpectedConditions.elementToBeClickable(btnNext));
				btnNext.click();
				Thread.sleep(1000);
				driver.getTitle();
				Thread.sleep(3000);

			}catch (Exception e){
				log.debug("Method - clickWhenElementIsClickable | Exception occurred - "+e.toString());
				throw e;
			}
						
		}
		
		
		
		/**
		 * @param elementTable - WebElement corresponding to the table object.
		 * @param row - int value for the row
		 * @param column - int value for the column
		 * @return String - return the cell data for Row and Column 
		 * @author aunnikrishnan
		 * @throws Exception
		 */
		public static String getCellData_tbl(WebElement elementTable,int row, int column) throws Exception{
			APP_LOGGER.startFunction("getCellData_tbl - WebElement ");
			String tempRetval=Globals.KEYWORD_FAIL;
			
			try {
				
				tempRetval = isDisplayed(elementTable);
				if(tempRetval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
					log.debug("Method - getCellData_tbl | Table is not displayed. "+elementTable.toString());
					return Globals.KEYWORD_FAIL;
				}
				String cellData;
				
				/* int rowCount;
				int colCount;
				
				
				rowCount = getRowCount_tbl(elementTable);
				colCount = getColumnCount_tbl(elementTable);
				
				if(row > rowCount){
					log.debug("Method - getCellData_tbl | Data for the row to be fetched is greater than the table rowcount |RowCount "+rowCount + " Row val -"+row);
					return Globals.KEYWORD_FAIL;
				}
				
				if(column > colCount){
					log.debug("Method - getCellData_tbl | Data for the Column to be fetched is greater than the table Column Count | ColCount "+colCount + " Col val -"+column);
					return Globals.KEYWORD_FAIL;
				}*/
				
				cellData = elementTable.findElement(By.xpath(".//tr["+row+"]/td["+column+"]")).getText();
				log.debug("Method - getCellData_tbl | Row - "+row+ " | Col -"+column + " | CellData - "+cellData);
				return cellData;
										
			}catch (Exception e){
				log.error( " Method - getCellData_tbl | Exception - " + e.toString());
				throw e;
			}
					
			
		}
		
		
	    //returns Minutes differene with 2 decimal places number between 2 date timestamps
	    public static String getTotalMinsDiff(Date date1, Date date2) throws InterruptedException{
	    	
	    	try {
	    		long diff = date2.getTime() - date1.getTime();        	
	        	
	    		long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
	        	
	        	double totalMinutes =  (double) totalSeconds/60;
	        	
	        	DecimalFormat formatter = new DecimalFormat("#0.00");
	        	return formatter.format(totalMinutes);
	        	
			} catch (Exception e) {
				log.error("Class GenUtils | Method getTotalMinsDiff | Exception desc : "+e.toString());
				e.printStackTrace();
				throw e;
			}
	    	
	    	
	    }
	    
	    public static String getPriorDate(String format,int noOfYears){
			 	
			    return  getPriorDate(format,"years",noOfYears);
		 }
	    
	    public static String getTodaysDate(){
	    	
	    	String format ="MM/dd/YYYY";
	    	Format formatter = new SimpleDateFormat(format);
	    	Date result = new java.util.Date();
	    	
	    	String retval = Globals.KEYWORD_FAIL; 
		    retval = formatter.format(result); 
		    String dd="";
		    String mm="";
		    String yyyy="";
		    mm=retval.split("/")[0];
		    dd=retval.split("/")[1];
		    yyyy=retval.split("/")[2];
		    
		    if(dd.startsWith("0")){
		    	dd= dd.replace("0","");
		    }
		    
		    if(mm.startsWith("0")){
		    	mm= mm.replace("0","");
		    }
		    
		    retval = mm+"/"+dd+"/"+yyyy;
		    return retval ;
		    
	    }
		
	    //Written by Smita to verify, whether the function is calculating days,or months, or years on passing 'type' & 'value' to it
	    
	    public static String getPriorDate(String format,String type,int value){
		 	Format formatter = new SimpleDateFormat(format);
		    Calendar cal = Calendar.getInstance();
		    
		    if(type.equalsIgnoreCase("days"))
		    {
		    	cal.add(Calendar.DATE, value);
		    	
		    }else if(type.equalsIgnoreCase("months")){
		    	
		    	cal.add(Calendar.MONTH, value);
		    	
		    }else if(type.equalsIgnoreCase("years")){
		    	
		    	cal.add(Calendar.YEAR, value);
		    }
		    
		    Date result = cal.getTime();
		    String retval = Globals.KEYWORD_FAIL; 
		    retval = formatter.format(result);
		    return retval;
	 }
	    public static String getLoggedInUserFullName(){
	    	String fullName = Secur32Util.getUserNameEx(Secur32.EXTENDED_NAME_FORMAT.NameDisplay);
	    	return fullName;
	    }
	    
	    public static String getLocalHostName() throws UnknownHostException {
	    	String hostname = InetAddress.getLocalHost().getHostName();
	    	return hostname;
	    }	
		
		/**************************************************************************************************
		 * Method to check current processor type 32/64 bit
		 * @return  boolean- true if machine is 64bit / false , otherwise
		 * @author aunnikrishnan
		 * @created 2/22/2016
		 * @LastModifiedBy
		***************************************************************************************************/
	    public static boolean isMachine64Bit(){
	    	
	    	boolean is64bit = false;
			
	    	if (System.getProperty("os.name").contains("Windows")) { //check whether OS is Windows
			    is64bit = (System.getenv("ProgramFiles(x86)") != null); // check whether This folder exists.This system var return C:\Program Files (x86) in case for 64 bit processor
			} else {
			    is64bit = (System.getProperty("os.arch").indexOf("64") != -1);//non windows OS
			}
			return is64bit;
	    }
	    
	    public static String getStateCode(String stateName){
	    	String retval = Globals.KEYWORD_FAIL;
	    	retval = Globals.staticTestDataConfig.getProperty(stateName);
	    	return retval;
	    }
	    
	    /**************************************************************************************************
		 * Method to integer to full month name
		 * @param monthPrefix -  String- 3 character month name e.g Jan/Feb
		 * @return String-Full month Name if successful, null otherwise
		 * @author aunnikrishnan
		 * @created
		 * @LastModifiedBy
		 ***************************************************************************************************/
		public static String convertIntToFullMonthName(String sMonth){
			APP_LOGGER.startFunction("convertIntToFullMonthName - String ");
			String monthNameInFull =null;
			
			switch(sMonth){
		    case "01": case "1":
		    	monthNameInFull = "January";
		       break; //optional
		    case "02" :case "2":
		    	monthNameInFull = "February";
		       break; //optional
		    case "03" :case "3":
		    	monthNameInFull = "March";
		       break;
		    case "04" : case "4":
		    	monthNameInFull = "April";
		       break;
		    case "05" : case "5":
		    	monthNameInFull = "May";
		       break;
		    case "06" : case "6":
		    	monthNameInFull = "June";
		       break;
		    case "07" : case "7":
		    	monthNameInFull = "July";
		       break;
		    case "08" : case "8":
		    	monthNameInFull = "August";
		       break;
		    case "09" : case "9": 
		    	monthNameInFull = "September";
		       break;
		    case "10" :
		    	monthNameInFull = "October";
		       break;
		    case "11" :
		    	monthNameInFull = "November";
		       break;
		    case "12" :
		    	monthNameInFull = "December";
		       break;
		    default : //Optional
		    	log.error( "Unable to convert into month Name- Input: " + sMonth);
			}
			return monthNameInFull;
		}
		
		
		public static void unzip(String zipFilePath, String destDirectory) throws IOException {
	        File destDir = new File(destDirectory);
	        if (!destDir.exists()) {
	            destDir.mkdir();
	        }
	        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	        ZipEntry entry = zipIn.getNextEntry();
	        // iterates over entries in the zip file
	        while (entry != null) {
	            String filePath = destDirectory + File.separator + entry.getName();
	            File newFile = new File(filePath);
	            if (!entry.isDirectory() && !newFile.exists()) {
	                // if the entry is a file, extracts it
	                extractFile(zipIn, filePath);
	            } else {
	                // if the entry is a directory, make the directory
	                File dir = new File(filePath);
	                dir.mkdir();
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
	    }
	    /**
	     * Extracts a zip entry (file entry)
	     * @param zipIn
	     * @param filePath
	     * @throws IOException
	     */
	    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
	        byte[] bytesIn = new byte[4096];
	        int read = 0;
	        while ((read = zipIn.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
	        bos.close();
	    }
	    
	    public static void cleanUpEnvironment(){
	    	try{
	    		taskkill("chromedriver.exe");
	    		taskkill("IEDriverServer32.exe");
	    		taskkill("IEDriverServer64.exe");
	    		
	    	}catch(Exception e){
	    		 log.error("Exception occurred while CleaningUpEnvironment - Exception:-"+e.toString());
	    	}
	    
	    }
	    
	    public static void taskkill(String strProcessName) throws Exception {
		    String strCmdLine = null;
		    Process pr =null;
		    strCmdLine = String.format("taskkill /F /IM  %s\" ", strProcessName);

		    Runtime rt = Runtime.getRuntime();
		    pr = rt.exec(strCmdLine);
		    pr.destroy();
		    		    
		}
	    
	    public static void clickLink_NavigateAndVerify(String objName2beCLicked,String objName2BeVerified,String reporterStepName){
			
			String retval = Globals.KEYWORD_FAIL;
			String tempRetval=Globals.KEYWORD_FAIL;
			ArrayList<String> tabs =null;
			try{
				seleniumCommands.clickWhenElementIsClickable(objName2beCLicked, 5);
				
				tabs = new ArrayList<String> (driver.getWindowHandles());
				
				if (tabs.size() > 1){
					driver.switchTo().window(tabs.get(1));
				}
				//Handle security error for socialnetworking
				tempRetval=seleniumCommands.waitforElementToDisplay("login_securityerrhandle_btn",5);
//				tempRetval = isDisplayed(securityerrhandle);
				if(tempRetval.equalsIgnoreCase(Globals.KEYWORD_PASS))
				{
					seleniumCommands.clickWhenElementIsClickable("login_securityerrhandle_btn",5);
					
				}
			
				
				if(seleniumCommands.isDisplayed(objName2BeVerified).equalsIgnoreCase(Globals.KEYWORD_PASS)){
					retval = Globals.KEYWORD_PASS;
					
				}
				
			}catch(Exception e){
				driver.switchTo().defaultContent();
			}
			
			if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
				seleniumCommands.STAF_ReportEvent("Pass", reporterStepName, "Link clicked,navigated and verified sucessfully", 1);
			}else{
				seleniumCommands.STAF_ReportEvent("Fail", reporterStepName, "Link clicked,navigated and verification Failed", 1);
			}
			
			if (tabs.size() > 1){
				for(int i = 1;i<tabs.size();i++){
					driver.switchTo().window(tabs.get(i));
					driver.close();
				}
			}
			
			driver.switchTo().window(tabs.get(0));
			
			
			
		}

	    //Todo - temp function...this should be removed once we have implemented reporting in isDisplayed function
	    public static void isDisplayed_withCustomReporting(String elementName,String reporterStepName){
	    	String retval = Globals.KEYWORD_FAIL;
	    	
	    	retval=isDisplayed(elementName);
	    	if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
	    		seleniumCommands.scrollIntoView(elementName);
	    		seleniumCommands.STAF_ReportEvent("Pass", reporterStepName, "Object is displayed on UI - "+elementName, 1);
			}else{
				seleniumCommands.STAF_ReportEvent("Fail", reporterStepName, "Object Not displayed in UI -"+elementName, 1);
			}
	    }
	    
	    public static String verifyPlaceholderText(String  elementName,String reporterStepName,String expectedPlaceholderText){

	    		WebElement element = createWebElement(elementName);
	    		return verifyPlaceholderText(element,reporterStepName,expectedPlaceholderText);

	    }
	    
	    public static String verifyPlaceholderText(WebElement element,String reporterStepName,String expectedPlaceholderText){
	    	String retval = Globals.KEYWORD_FAIL;
	    	String tempretval = Globals.KEYWORD_FAIL;
	    	boolean validationPassed = false;
	    	String actualtext="";
	    	
	    	try{
	    		tempretval = seleniumCommands.isDisplayed(element);
		    	if (tempretval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
		    		actualtext = element.getAttribute("placeholder").trim();
		    		
		    		if(actualtext.equals(expectedPlaceholderText)){
		    			validationPassed = true;
		    		}
		    	}
	    	}catch(Exception e){
	    		validationPassed = false;
	    		
	    	}
	    	
	    	if(validationPassed){
	    		seleniumCommands.STAF_ReportEvent("Pass", reporterStepName, "Placeholder text matched as expected.Expected -"+expectedPlaceholderText, 0);
	    		retval = Globals.KEYWORD_PASS;
			}else{
				seleniumCommands.STAF_ReportEvent("Fail", reporterStepName, "Placeholder text doesnt match.Exp -"+expectedPlaceholderText + " Actual -"+actualtext, 0);
			}
	    	
	    	return retval;
	    }
	    
		/**************************************************************************************************
		 * Method to select a value from a drop down list by index
		 * @param element -  WebElement on which a value needs to be selected
		 * @param indexOfElement - int - index value that needs to be selected from the drop down list
		 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
		 * @author aunnikrishnan
		 * @created 3/15/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
	    
	    public static String selectValue_byIndex(WebElement element,int indexOfElement,boolean...reportEventFlag){
			APP_LOGGER.startFunction("selectValue_byIndex - WebElement ");
			String retval=Globals.KEYWORD_FAIL;
			Select select=null;
			
			try{
					if(seleniumCommands.isEnabled(element, reportEventFlag).equalsIgnoreCase(Globals.KEYWORD_PASS)){
						select = new Select(element);
									
						select.selectByIndex(indexOfElement);
						Thread.sleep(1000);
						WebElement selectedOption = select.getFirstSelectedOption();
						retval=selectedOption.getText();
						
					}
			}catch(NoSuchElementException e){// NoSuchElementException - If no matching option elements are found in dropdown
				log.error("Unable to select index- "+indexOfElement + " as this index doesnt exists in the dropdown");
				return Globals.KEYWORD_FAIL;
			}catch (Exception e){
				log.error(element.toString() + "Unable to select dropdown index -"+indexOfElement + " | Exception - " + e.toString());
			}
					
			return retval;
		}
	    
	    
		
		/**************************************************************************************************
		 * Method to select a value from a drop down list by index
		 * @param elementName -  Name of the element corresponding in OR
		 * @param indexOfElement - int - index value that needs to be selected from the drop down list
		 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
		 * @author aunnikrishnan
		 * @created 3/15/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
		public static String selectValue_byIndex(String elementName, int indexOfElement,boolean...reportEventFlag){
			APP_LOGGER.startFunction("selectValue_byIndex - String ");
			String retval=Globals.KEYWORD_FAIL;
			WebElement element=null;	
			try {
				element=createWebElement(elementName);
				retval= selectValue_byIndex(element, indexOfElement, reportEventFlag);
						
			}catch (Exception e){
				log.error(element.toString() + "Unable to select dropdown index -"+indexOfElement + " | Exception - " + e.toString());
				
			}
					
			return retval;
		}
		
		
		/**************************************************************************************************
		 * Method to bring an element into view
		 * @param element	-Webelement - element which has to be bought in view
		 * @return void
		 * @author aunnikrishnan
		 * @created 3/21/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
		
		public static void  scrollIntoView(WebElement element){
			
			if(isDisplayed(element).equalsIgnoreCase(Globals.KEYWORD_PASS)){
				Actions actions = new Actions(driver);
				actions.moveToElement(element);
				actions.perform();
			}else{
				log.error("unable to scroll into view element .Element -"+element.toString());
			}
			
		}
		
		
		/**************************************************************************************************
		 * Method to bring an element into view
		 * @param element	-String - element anem corresponding to OR
		 * @return void
		 * @author aunnikrishnan
		 * @created 3/21/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
		public static void  scrollIntoView(String elementName){ 
			WebElement element =null;
			try {
				element=createWebElement(elementName);
				scrollIntoView(element);
						
			}catch (Exception e){
				log.error("unable to scroll into view element .Element -"+element.toString());
				
			}
		}
		

		/**************************************************************************************************
		 * Method to set the time zone 
		 * @return void
		 * @author rtahiliani
		 * @created 5/25/2016
		 ***************************************************************************************************/
		public static void  setTimeZone() {
			
			String inpTimeZone = Globals.setTimeZoneTo;	
			
			try {
				
				//Verify TimeZone is already Set - Else SET it 
				String currentTimeZone = TimeZone.getDefault().getDisplayName();
				if (currentTimeZone == inpTimeZone) {	
					log.info("Pass - TimeZone is already set to "+inpTimeZone);}
				else{												
					   String[] parms = {"tzutil", "/s", inpTimeZone};
					   Process process;
					
						process = Runtime.getRuntime().exec(parms);
					
					   Thread.sleep(1000);
					   
					   String TimeZoneID;
					   if (inpTimeZone.equalsIgnoreCase("Eastern Standard Time")) {
						   	TimeZoneID = "America/New_York";}
					   else {
							TimeZoneID = "IST";
					   }
					   
					   TimeZone.setDefault(TimeZone.getTimeZone(TimeZoneID));
				   
					   //cross verify 
					   String[] parms1 = {"tzutil", "/g"};
					   process = Runtime.getRuntime().exec(parms1); 		   
					   BufferedReader brCleanUp = new BufferedReader (new InputStreamReader(process.getInputStream()));
					   String consoleOutput = brCleanUp.readLine();
					   if (consoleOutput.equalsIgnoreCase(inpTimeZone) ) {
						   log.info("Pass - TimeZone set to "+inpTimeZone);
					   }
					   else{
						   log.error("Fail - Unable to set TimeZone to "+inpTimeZone);
					   }
				}
			} catch (Exception e) {
				log.error("unable to set time Zone to -"+inpTimeZone);
			} 
		}
		
		
		public static void waitForPageLoad() {

			WebDriverWait wait = new WebDriverWait(driver, 30);

		    wait.until(new ExpectedCondition<Boolean>() {
		        public Boolean apply(WebDriver wdriver) {
		            return ((JavascriptExecutor) driver).executeScript(
		                "return document.readyState"
		            ).equals("complete");
		        }
		    });
		}
		
		
		
		public static void verifyExpectedValue( double ExpectedValue, double ActualValue , String stepName)
		   {
		      if(ExpectedValue == ActualValue){
		    	  seleniumCommands.STAF_ReportEvent("Pass", stepName, "Actual Value is as Expected. Val = "+ExpectedValue, 1); 
		      }else{
		    	  seleniumCommands.STAF_ReportEvent("Fail", stepName, "Actual Value is as Not as Expected.Exp = "+ExpectedValue+ " Actual ="+ActualValue, 1);
		      }
		    }
		
		public static double parseDouble(String strValue){
			
			double retval;
			if(strValue == null || strValue.isEmpty() || strValue.equals("")){
				retval = 0; 
			}else{
				retval = Double.parseDouble(strValue);
			}
				return retval;
				
			
		}
		
		public static String verifyDropdownItems(String elementName, String[] expectedItems, String reportingStepName){
			WebElement element =  seleniumCommands.createWebElement(elementName);
			return verifyDropdownItems(element,expectedItems,reportingStepName);
		}
		
		public static String verifyDropdownItems(WebElement element, String[] expectedItems, String reportingStepName){
			String retval = Globals.KEYWORD_FAIL;
			String tempretval = Globals.KEYWORD_FAIL;
			
			tempretval =  isDisplayed(element);
			if(tempretval.equalsIgnoreCase(Globals.KEYWORD_FAIL)){
				seleniumCommands.STAF_ReportEvent("Fail", reportingStepName, "Dropdwon element is not displayed",1);
				return retval;
			}
			
			int expCount = expectedItems.length;
			if(expCount < 1){
				seleniumCommands.STAF_ReportEvent("Fail", reportingStepName, "Expected items array passed has no items",0);
				return retval;
				
			}
			
			List<WebElement> ddElements = element.findElements(By.xpath("./option[not(@disabled='')]"));
			int ddItemsCount = 0;
			int i = 0;
			boolean mismatchInDD = false;
			
			String ddItemText = "";
			ddItemsCount = ddElements.size();
			
			if (expCount !=  ddItemsCount){
				seleniumCommands.STAF_ReportEvent("Fail", reportingStepName, "Mismatch between expected dropdown items count and Actual Count.Exp-"+expCount+"Actual-"+ddItemsCount,0);
				return retval;
				
			}else{
				for (WebElement ddElement : ddElements) {
					ddItemText= ddElement.getText().trim();
					if(!expectedItems[i].equalsIgnoreCase(ddItemText)){
						mismatchInDD = true;
						break;
						
						
					}
					i=i+1;
				}
				if(mismatchInDD){
					seleniumCommands.STAF_ReportEvent("Fail", reportingStepName, "Dropdown items do not match as Expected",0);
				}else{
					seleniumCommands.STAF_ReportEvent("Pass", reportingStepName, "Dropdown items matches as Expected",0);
					retval = Globals.KEYWORD_PASS;
				}
			}
			
			return retval;
		}
		
		public  static java.sql.Timestamp getDBCurrentTimeStamp() {

			java.util.Date today = new java.util.Date();
			return new java.sql.Timestamp(today.getTime());

		}
		
		public static String isSelected_withCustomReporting(String elementName,String reporterStepName){
			APP_LOGGER.startFunction("isSelected");
			String retval=Globals.KEYWORD_FAIL;
			WebElement element=null;
			
			try {
				element=createWebElement(elementName);
				retval=isSelected(element);
				if(retval.equalsIgnoreCase(Globals.KEYWORD_PASS)){
					seleniumCommands.scrollIntoView(elementName);
					seleniumCommands.STAF_ReportEvent("Pass", reporterStepName, "Object is selected on UI - "+ elementName, 1);
		    		retval = Globals.KEYWORD_PASS;
				}else{
					seleniumCommands.STAF_ReportEvent("Fail", reporterStepName, "Object Not displayed in UI -"+elementName, 1);
				}
			}
			catch (Exception e){
				seleniumCommands.STAF_ReportEvent("Fail", reporterStepName, "Unable to verify if objects is selcted.Exception-"+e.toString(), 1);
				
			}
			
			
			
			return retval;
		}
		
		public static String capitalizeFirstLetterInWord(String text){
			if(text.isEmpty() || text ==null){
				return null;
			}
			
			String retval ="";
			if(text.length() ==1){
				retval =  text.toUpperCase();
			}else{
				retval = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
				
			}
			
			return retval;
		}
		
	    public static String[] differences(String[] first, String[] second) {
	        String[] sortedFirst = Arrays.copyOf(first, first.length); 
	        String[] sortedSecond = Arrays.copyOf(second, second.length); 
	        Arrays.sort(sortedFirst); //sort both the arrays
	        Arrays.sort(sortedSecond); 

	       int firstIndex = 0;
	        int secondIndex = 0;

	        LinkedList<String> diffs = new LinkedList<String>();  

	        while (firstIndex < sortedFirst.length && secondIndex < sortedSecond.length) { 
	            int compare = (int) Math.signum(sortedFirst[firstIndex].compareTo(sortedSecond[secondIndex]));

	            switch(compare) {
	            case -1:
	                diffs.add(sortedFirst[firstIndex]);
	                firstIndex++;
	                break;
	            case 1:
	                diffs.add(sortedSecond[secondIndex]);
	                secondIndex++;
	                break;
	            default:
	                firstIndex++;
	                secondIndex++;
	            }
	        }

	        if(firstIndex < sortedFirst.length) {
	        	appendList(diffs, sortedFirst, firstIndex);
	        } else if (secondIndex < sortedSecond.length) {
	        	appendList(diffs, sortedSecond, secondIndex);
	        }

	        String[] strDups = new String[diffs.size()];

	        return diffs.toArray(strDups);
	    }

	    private static void appendList(LinkedList<String> diffs, String[] sortedArray, int index) {
	        while(index < sortedArray.length) {
	            diffs.add(sortedArray[index]);
	            index++;
	        }
	    }
	    /**************************************************************************************************
		 * Method to select a value from a drop down list by Value property
		 * @param element -  WebElement on which a value needs to be selected
		 * @param indexOfElement - int - index value that needs to be selected from the drop down list
		 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
		 * @author aunnikrishnan
		 * @created 8/8/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
	    
	    public static String selectValue_byValue(WebElement element,String value,boolean...reportEventFlag){
			APP_LOGGER.startFunction("selectValue_byValue - WebElement ");
			String retval=Globals.KEYWORD_FAIL;
			Select select=null;
			
			try{
					if(seleniumCommands.isEnabled(element, reportEventFlag).equalsIgnoreCase(Globals.KEYWORD_PASS)){
						select = new Select(element);
									
						select.selectByValue(value);
						Thread.sleep(1000);
						WebElement selectedOption = select.getFirstSelectedOption();
						String actualValue = selectedOption.getAttribute("value");
						if(value.equalsIgnoreCase(actualValue)){
							retval= Globals.KEYWORD_PASS;
						}else{
							retval=Globals.KEYWORD_FAIL;	
						}
						
						
					}
			}catch(NoSuchElementException e){// NoSuchElementException - If no matching option elements are found in dropdown
				log.error("Unable to select value- "+value + " as this value doesnt exists in the dropdown");
				return Globals.KEYWORD_FAIL;
			}catch (Exception e){
				log.error(element.toString() + "Unable to select dropdown value -"+value + " | Exception - " + e.toString());
			}
					
			return retval;
		}
	    
	    
		
		/**************************************************************************************************
		 * Method to select a value from a drop down list by index
		 * @param elementName -  Name of the element corresponding in OR
		 * @param indexOfElement - int - index value that needs to be selected from the drop down list
		 * @return PASS, if the element is displayed within the timeout specified/ FAIL, otherwise
		 * @author aunnikrishnan
		 * @created 3/15/2016
		 * @LastModifiedBy
		 ***************************************************************************************************/
		public static String selectValue_byValue(String elementName, String value,boolean...reportEventFlag){
			APP_LOGGER.startFunction("selectValue_byValue - String ");
			String retval=Globals.KEYWORD_FAIL;
			WebElement element=null;	
			try {
				element=createWebElement(elementName);
				retval= selectValue_byValue(element, value, reportEventFlag);
						
			}catch (Exception e){
				log.error(element.toString() + "Unable to select dropdown value -"+value + " | Exception - " + e.toString());
				
			}
					
			return retval;
		}

		public static String copyFileTo(String srcFilePath,String destFolderPath){
			String retval = Globals.KEYWORD_FAIL;
			File srcFile = new File(srcFilePath);
	    	File destDir = new File(destFolderPath);
	    	String fileName = srcFile.getName();
	    	
	    	File destFile = new File(fileName);
	    	try {
	    		if(destFile.exists()){
	    			destFile.delete();
	    		}
				FileUtils.copyFileToDirectory(srcFile, destDir);
				
				
			} catch (Exception e) {
				log.error("Unable to copy file to requested folder due to exception -"+e.toString());
			}
	    	
	    	return retval;
		}
		
		public static void fetchIterationRange(ExcelUtils excelObj,String sheetName) {
			
			String iterationValue = Globals.Iteration;
			int lowerRange = 0;
			int upperRange = 0;
				
			if(iterationValue.equals("") || iterationValue.isEmpty() || iterationValue == null){ // case where range of keyword needs to be excuted
				lowerRange =1;
				upperRange = excelObj.getRowCount(sheetName);
			}else if(iterationValue.contains("-")){ // case where range of keyword needs to be excuted
				lowerRange = Integer.parseInt(iterationValue.split("-")[0]);
				upperRange = Integer.parseInt(iterationValue.split("-")[1]);
			}else if(iterationValue.equalsIgnoreCase("All")){ // case where all test data and orders needs to be created
				lowerRange =1;
				upperRange = excelObj.getRowCount(sheetName)-1;
				
			}else if(iterationValue.equalsIgnoreCase("Unique")){ // case where first 7 test data needs to be used
				lowerRange = 1;
				upperRange = 7;
				
			}else if(iterationValue.equalsIgnoreCase("Default")){ // case where all test data and orders needs to be created
				lowerRange = 1;
				upperRange = 5;
				
			}else if(iterationValue.equalsIgnoreCase("0")){ //case where ZERO  test data and orders needs to be created
				lowerRange =0;
				upperRange =0;
				
			}else{//case where one test data needs to be used
				lowerRange = (int)Double.parseDouble(iterationValue);
				upperRange = lowerRange ;
			}
			
			Globals.IterationStartValue = lowerRange;
			Globals.IterationEndValue = upperRange;

		}
		
		
		public static String clickAndDownloadFile(WebElement objToBeClicked,String absPath2BeSaved,String fileName,String stepName) throws Exception{
			String retval = Globals.KEYWORD_PASS;
			int timeOutInSeconds = 60;
			clickWhenElementIsClickable(objToBeClicked, timeOutInSeconds);
			
			String absFilePath = Globals.currentRunPath+File.separator+fileName;
			File srcFile =  new File(absFilePath);
			
			int counter = 0;
			while (counter<=timeOutInSeconds) {
				if(srcFile.exists()){
					break;
				}else{
					Thread.sleep(1000);
					counter++;
				}
			}
			if(srcFile.exists() && srcFile.length() > 0) {
				
				File destFile = new File(absPath2BeSaved);
				FileUtils.moveFile(srcFile, destFile);
				Globals.Result_Link_FilePath = absPath2BeSaved;
				seleniumCommands.STAF_ReportEvent("Pass", stepName,  "File downloaded successfully.FileName-"+fileName,3);
				retval = Globals.KEYWORD_PASS;
			}else{
				seleniumCommands.STAF_ReportEvent("Fail", stepName, "File downloaded NOT successfull.FileName-"+fileName,0);
			}
			
			return retval;
			
		}
		
		public static void loadColumns_MIPS(){
			//load the column index of the test data sheet
			if(Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("mips") || Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("prism")){
				Globals.MIPS_FNAME_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "FirstName");
				Globals.MIPS_LNAME_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "LastName");
				Globals.MIPS_MIDNAME_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "MiddleName");
				Globals.MIPS_DOB_COL_INDEX 				= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "DOB");
				Globals.MIPS_SSN_COL_INDEX 				= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "SSN");
				Globals.MIPS_CourtName_COL_INDEX 		= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "CourtName");
				Globals.MIPS_Region_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Region");
				Globals.MIPS_County_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "County");
				Globals.MIPS_Screening_Type_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Screening Qualifier");
				Globals.MIPS_Vendor_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Vendor");
				Globals.MIPS_Edited_LastName_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Edited-LastName");
				Globals.MIPS_Edited_FirstName_COL_INDEX = 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Edited-FirstName");
				Globals.MIPS_Formated_DOB_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Formated-DOB");
				Globals.MIPS_WebSiteResults_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "WebSites_Result");
				Globals.MIPS_OrderID_COL_INDEX 			= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "OrderID");
				Globals.MIPS_UtilityResults_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Utility_Result");
				Globals.MIPS_TestCaseResults_COL_INDEX 	= 	Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Test_Case_Result");
				Globals.MIPS_Expected_Utility_Result_COL_INDEX = Globals.mipsResultXLS.getColumnIndex(Globals.MIPS_ResultSheetName, "Expected_Utility_Result");
			
			}else if(Globals.CurrentPlatform.toLowerCase().equalsIgnoreCase("ag_ifn")){
//				Below code is commented as now instead of vertical approach we are using horizontal approach
//				Globals.AG_TestCaseDescr_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "TestCaseDescr");
//				Globals.AG_SWestOrderID_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "SWestOrderID");
//				Globals.AG_OrderedDate_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "OrderedDate");
//				Globals.AG_LastName_COL_INDEX 				= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "LastName");
//				Globals.AG_FirstName_COL_INDEX 				= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "FirstName");
//				Globals.AG_SSN_COL_INDEX 					= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "SSN");
//				Globals.AG_DOB_COL_INDEX 					= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "DOB");
//				Globals.AG_AddressLine1_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "AddressLine1");
//				Globals.AG_City_COL_INDEX 					= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "City");
//				Globals.AG_State_COL_INDEX 					= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "State");
//				Globals.AG_ZIP_COL_INDEX 					= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "ZIP");
//				Globals.AG_Jurisdiction_COL_INDEX			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "Jurisdiction");
//				Globals.AG_SALARY_COL_INDEX 				= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "SalaryRange");
//				Globals.AG_MIDNAME_COL_INDEX 				= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "MidName");
//				Globals.AG_Exp_CaseNumber_COL_INDEX 		= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "Exp_CaseNumber");
//				Globals.AG_Exp_Charges_DOB_COL_INDEX 		= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "Exp_Charges");
//				
//				
//				Globals.AG_Jurisdiction_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "Jurisdiction");
//				Globals.AG_packageName_COL_INDEX 			= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "packageName");
//				Globals.AG_POSITIONID_COL_INDEX				= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "PostionID");
//				Globals.AG_Max_Timeout_inHrs_COL_INDEX 		= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "Max_Timeout_inHrs");
//				Globals.AG_CurrrentOrderCreationStatus_COL_INDEX 		= 	Globals.testSuiteXLS.getColumnIndex(Globals.TestData, "CurrrentOrderCreationStatus");
//				
			}
			
			
			
		}
		
		
		public static void addParamVal_InEmail(String paramName,String value){
			if(Globals.EmailParam == null){
				Globals.EmailParam =  new HashMap<String, String>();
			}
			Globals.EmailParam.put(paramName, value);
		}
		
		public static void removeAllKeys_fromHashmap(HashMap<String,String> hm){
			if(hm != null){
				hm.clear();
			}
		}
		
		
		public static String shareFolder(String ShareName,String absFilePath){
			
			if(ShareName == null || ShareName.isEmpty() || absFilePath == null || absFilePath.isEmpty()){
				return Globals.KEYWORD_FAIL;
			}
			
			//String vbsPath = Globals.TestDir + "\\src\\genResource\\genericLib\\shareFolder.vbs";
			String vbsPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"shareFolder.vbs";
			String[] parms = {"cscript", vbsPath, ShareName, absFilePath};
			
			try {
				Runtime.getRuntime().exec(parms);
				
	        	return Globals.KEYWORD_PASS; 
		     
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Globals.KEYWORD_FAIL;
			}
			
		}
		
		public static String appendToTextFile(String absoluteFilePath,String textToBeAppended){
            BufferedWriter bw = null;
            FileWriter fw = null;
            String retval = Globals.KEYWORD_FAIL;
            try {

                String data = textToBeAppended;

                File file = new File(absoluteFilePath);

                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                // true = append file
                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);

                bw.write(data);
                bw.write(System.getProperty("line.separator"));

                log.info("Text has been apended - "+textToBeAppended);
                retval = Globals.KEYWORD_PASS;

            } catch (IOException e) {
                log.error("Unable to append text - "+textToBeAppended);
                e.printStackTrace();

            } finally {

                try {

                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }
            }
            return retval;
        }

		
		

		   /**************************************************************************************************
	     * Method to bring an element into view
	     * @param element   -Webelement - element which has to be bought in view
	     * @return void
	     * @author navisha
	     * @created 4/24/2017
	     * @LastModifiedBy
	     ***************************************************************************************************/
	    
	    public static void  scrollIntoViewUsingJavaScript(WebElement element){
	        
	        if(isDisplayed(element).equalsIgnoreCase(Globals.KEYWORD_PASS)){
	        	JavascriptExecutor jsExecutor = (JavascriptExecutor) Globals.driver;
	        	jsExecutor.executeScript("arguments[0].scrollIntoView(true);",element);
	        	
	        }else{
	            log.error("unable to scroll into view element .Element -"+element.toString());
	        }
	        
	    }
	    
	    
	    /**************************************************************************************************
	     * Method to bring an element into view
	     * @param element   -String - element name corresponding to OR
	     * @return void
	     * @author navisha
	     * @created 4/24/2017
	     * @LastModifiedBy
	     ***************************************************************************************************/
	    public static void  scrollIntoViewUsingJavaScript(String elementName){ 
	        WebElement element =null;
	        try {
	            element=createWebElement(elementName);
	            scrollIntoView(element);
	                    
	        }catch (Exception e){
	            log.error("unable to scroll into view element .Element -"+element.toString());
	            
	        }
	    }
	    
	    /**************************************************************************************************
	     * Method to set value on a web element. also check for isDisplayed and isEnabled with javascript change menthod
	     * @param element - WebElement on which value needs to be Set
	     * @param value - String value which needs to be set
	     * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	     * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	     * @author psave
	     * @created
	     * @LastModifiedBy
	     ***************************************************************************************************/
	    public static String setValueJsChange(WebElement element, String value,boolean...reportEventFlag){
	        APP_LOGGER.startFunction("setValue - WebElement");
	        String retval=Globals.KEYWORD_FAIL;
	        String elementText;
	        String elementDisplayed=isEnabled(element, reportEventFlag);
	        
	        try{
	            if (value != null && elementDisplayed.equalsIgnoreCase(Globals.KEYWORD_PASS)){
	                 element.clear(); // clearing any value which is already present
	                 log.info("Element -"+element.toString() + "| 1st attempt to clear Data");
	                 
	                 elementText=element.getAttribute("value").trim();
	                 log.info("Element -"+element.toString() + "| Verifying Data is cleared");
	                 
	                 if (elementText.isEmpty()){
	                     log.info("Element -"+element.toString() + "| Data is cleared");
	                    
	                 }else{
	                     log.error("Element -"+element.toString() + "| 2nd Attempt to clear data is cleared");
	                     element.clear();
	                     
	                 }
	                 
	                 element.sendKeys(value); // set the value
	                 log.info("Element -"+element.toString() + "| Data entered. Value-"+value);
	                 
	                 JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
	                 jsExecutor.executeScript("$(arguments[0]).change();", element);
	                 log.info("Element -"+element.toString() + "| Data entered. Value-Change triggered"+value);
	                 
	                 elementText=element.getAttribute("value").trim(); // retrieve the value that has bee set
	                 
	                 if (elementText.equals(value)){
	                     log.info("Element -"+element.toString() + "| Data is Set. Value-"+value);
	                     retval=Globals.KEYWORD_PASS;
	                 }else{
	                     log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+value);
	                     retval=Globals.KEYWORD_FAIL;
	                 }
	             }else{ // element is either not displayed/enabled
	                 log.error("Element -"+element.toString() + "| Unable to set Data.Value-"+value.toString());
	                 retval=Globals.KEYWORD_FAIL;
	             }
	            
	            if (reportEventFlag.length>0 && reportEventFlag[0] == true) {
	                //reporter
	                log.info("isDiplayed result for Element - "+ element.toString()+" is being reported in RESULTS  ");
	                //TODO call reporterfunction
//	              System.out.println("report");
	         
	            }
	        }catch (Exception e){
	            log.error("Method - setValue | Element -"+element.toString() + " | Exception - " + e.toString());
	            
	        }
	    
	         
	             
	        return retval;
	    }
	    
	    /**************************************************************************************************
	     * Method to set value on a web element after having created a webelement from the string element name.Also check for isDisplayed and isEnabled with javascript change menthod
	     * @param elementName - String - element name corresponding to element in the OR
	     * @param value - String value which needs to be set
	     * @param reportEventFlag - OPTIONAL BOOLEAN ARRAY - should contain true if events should be reported in Client results
	     * @return TRUE if value is set on the element / FALSE , if unable to set value to the element
	     * @author psave
	     * @created
	     * @LastModifiedBy
	     ***************************************************************************************************/
	    public static String setValueJsChange(String elementName, String value,boolean...reportEventFlag){
	        APP_LOGGER.startFunction("setValue - String");
	        String retval=Globals.KEYWORD_FAIL;
	        WebElement element=null;
	        
	        try {
	            element=createWebElement(elementName);
	            retval=setValue(element, value, reportEventFlag);
	        }
	        catch (Exception e){
	            log.error("Element -"+elementName + " is either Not Present in OR/Doesnt exists in UI | Exception - " + e.toString());
	            
	        }
	                
	        return retval;
	    }
	    
	    
	    
	    /**************************************************************************************************
         * Method to setvalue and turn on the visibilty for the element using javascript
         * @param elementName -  Name of the element corresponding in OR
         * @param value -  Value to be set
         * @return PASS, if the element is clicked specified/ FAIL, otherwise
         * @author Navisha
         * @created 2/16/2017
         * @LastModifiedBy
         ***************************************************************************************************/
        public static String setValueUsingJavaScript(String elementName,String value) {
            APP_LOGGER.startFunction("setValueUsingJavaScript");
                String retval=Globals.KEYWORD_FAIL;
                WebElement element=null;
            try{
                element=createWebElement(elementName);
                retval=setValueUsingJavaScript(element,value);
                

            }catch (Exception e){
                log.debug("Method - setValueUsingJavaScript | Exception occurred - "+e.toString());
                
            }
            return retval;          
        }
        
        public static String setValueUsingJavaScript(WebElement element,String value) throws Exception{
            String retval=Globals.KEYWORD_FAIL;
            try{
                
                JavascriptExecutor js = (JavascriptExecutor)Globals.driver;
                js.executeScript("arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible';arguments[0].value='"+value+"';",element);
                retval=Globals.KEYWORD_PASS;
                
            }catch (Exception e){
                log.debug("Method - setValueUsingJavaScript | Exception occurred - "+e.toString());
                throw e;
            }
            return retval;          
        }
        
        /**************************************************************************************************
         * Method to check whether element is clickable or not
         * @param elementName -  Name of the element corresponding in OR
         * @return PASS, if the element is not clickable,FAIL if element is clickable
         * @author Gauri
         * @created 10/21/2018
         * @LastModifiedBy
         ***************************************************************************************************/
        public static String isElementNotClickable(String elementName, boolean...reportEventFlag) {
                APP_LOGGER.startFunction("isElementNotClickable");
                String retval=Globals.KEYWORD_FAIL;
                WebElement element=null;
            try{
                element = createWebElement(elementName);
                element.click();
               
                retval=Globals.KEYWORD_FAIL;
            }catch (Exception e){
                retval=Globals.KEYWORD_PASS;
              
              
            }          
            return retval;
        }
        

	    

}
