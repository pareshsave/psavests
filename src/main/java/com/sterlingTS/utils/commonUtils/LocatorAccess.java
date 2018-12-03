package com.sterlingTS.utils.commonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.sterlingTS.utils.commonVariables.Globals;

public class LocatorAccess {
	
	public static Logger log = Logger.getLogger(LocatorAccess.class);
    static Properties prop;
    
    public LocatorAccess()
    {
    	 prop = new Properties();
    	 String [] propertyFileName = listOfAllLocatorFile();
    	 LoadORFiles(propertyFileName);
    }
    
    //------ 
    /**
     * load the single / multiple properties files returned by fetchORFiles function for the corresponding platform.
     * @return String PASS/FAIL based on the execution.
     */
    public String LoadORFiles(String [] propertyFileName) {
        
    	if(!propertyFileName[0].contains(".properties"))
    	{
    		log.info("Invalid Property file name");
    		return Globals.KEYWORD_FAIL;
    	}
                
                if(propertyFileName[0].isEmpty() ||propertyFileName[0].equals("") || propertyFileName[0].equalsIgnoreCase("NA") || propertyFileName[0] == null ){
                                log.info(" No OR files were loaded");
                                return Globals.KEYWORD_PASS;
                }
               //prop = new Properties();
        try {
                
                FileInputStream fis ;
                for (int i = 0; i < propertyFileName.length; i++) {
                           // fis = new FileInputStream(propertyFileName[i]);
                	System.out.println(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"locators"+File.separator+propertyFileName[i]);
                	fis = new FileInputStream(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"locators"+File.separator+propertyFileName[i]);
                            getProp().load(fis);
                            fis.close(); 
                }
                                               
                
        }catch (IOException e) {
            log.error(e.getMessage());
            return Globals.KEYWORD_FAIL;
        }
        return Globals.KEYWORD_PASS;
    }

    //-----This Function to  returns a By object that is used by the Selenium browser driver object - based on property and name

    public static Properties getProp() {
		return prop;
	}

	public static void setProp(Properties prop) {
		LocatorAccess.prop = prop;
	}

	//19 Mar 2016 - Removing Throws Exception
    //public static By getLocator(String strElement) throws Exception {
    public static By getLocator(String strElement)  {
    	//System.out.println("DSU address is "+"\t"+getProp());
         
        // retrieve the specified object from the object list
        String locator = getProp().getProperty(strElement);
        System.out.println("Locator is :"+"\t"+locator);
         
        // extract the locator type and value from the object
        String locatorType = locator.split(":",2)[0];
        String locatorValue = locator.split(":",2)[1];
        
        // for testing and debugging purposes
        log.info(strElement + " - Retrieving object of type '" + locatorType + "' and value '" + locatorValue + "' from the OR");
         
        // return a instance of the By class based on the type of the locator
        // this By can be used by the browser object in the actual test
        if(locatorType.toLowerCase().equals("id"))
            return By.id(locatorValue);
        else if(locatorType.toLowerCase().equals("name"))
            return By.name(locatorValue);
        else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
            return By.className(locatorValue);
        else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
            return By.tagName(locatorValue);
        else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
            return By.linkText(locatorValue);
        else if(locatorType.toLowerCase().equals("partiallinktext"))
            return By.partialLinkText(locatorValue);
        else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
            return By.cssSelector(locatorValue);
        else if(locatorType.toLowerCase().equals("xpath"))
            return By.xpath(locatorValue);
        else
                //19 Mar 2016 - converting new Exception to new RunTimeException
                //throw new Exception("Unknown locator type '" + locatorType + "'");
                throw new RuntimeException("Unknown locator type '" + locatorType + "'");
        
    }
    
    public static String [] listOfAllLocatorFile()
    {
    	
    	List<String> results = new ArrayList<String>();
  		File[] files = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"Resources"+File.separator+"locators").listFiles();
  		//If this pathname does not denote a directory, then listFiles() returns null. 

  		for (File file : files) {
  		    if (file.isFile()) {
  		    	results.add(file.getName());
  		    }
  		}
  		String [] fileList = new String[results.size()];
  		log.info(results.size()+"\t"+"Property Files are present");
  		for(int i=0;i<results.size();i++)
  		{
  			fileList[i] = results.get(i);
  			log.info("Property File "+"\t"+fileList[i]);
  		}
    	
  		return fileList;
    	
    }
 


}
