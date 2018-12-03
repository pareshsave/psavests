package com.sterlingTS.seleniumUI;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sterlingTS.utils.commonUtils.APP_LOGGER;
import com.sterlingTS.utils.commonVariables.Globals;

public class seleniumConnection extends Globals{
	
	public static Logger log = Logger.getLogger(seleniumConnection.class);
	
	public static WebDriver browserLaunch(String browserType){
		
		APP_LOGGER.startFunction("browserLaunch");
		String retval=Globals.KEYWORD_FAIL;
		browserType=browserType.toLowerCase().trim();
		try{
			try{
				if(driver == null || driver.toString().contains("null"))
				{

					log.debug("******All Browser windows are closed *******");
					throw new Exception("All Browser windows are closed");
				
				}
			}catch(Exception e){
				log.debug("Launching browser");
				if (browserType.contains("mozilla") || browserType.contains("firefox")) { // this is updated to support both browser names from excel
					System.setProperty("webdriver.firefox.marionette",Globals.FIREFOXEXEPath );
					
					String downloadFilepath = Globals.currentRunPath;
					FirefoxProfile profile = new FirefoxProfile();
					//DesiredCapabilities caps = DesiredCapabilities.firefox();
					String allowedMimetypes = "application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/xml,application/csv, text/csv,image/png,image/jpeg,image/tiff, application/pdf, text/html, text/plain, application/download, application/octet-stream,application/x-excel, application/x-msexcel, application/excel, application/vnd.ms-excel";
					profile.setPreference("browser.download.folderList", 2);
					profile.setPreference("browser.download.dir", downloadFilepath);
					profile.setPreference( "browser.download.manager.alertOnExeOpen", false ); 
					profile.setPreference( "browser.helperApps.neverAsk.openFile", allowedMimetypes );
					profile.setPreference( "browser.helperApps.neverAsk.saveToDisk", allowedMimetypes );
					
					profile.setPreference( "browser.download.manager.showWhenStarting", false );
					profile.setPreference( "browser.download.manager.focusWhenStarting", false );
					profile.setPreference("browser.download.useDownloadDir", true);
					profile.setPreference( "browser.helperApps.alwaysAsk.force", false );
					profile.setPreference( "browser.download.manager.alertOnExeOpen", false ); 
					profile.setPreference( "browser.download.manager.closeWhenDone", false );
					profile.setPreference( "browser.download.manager.showAlertOnComplete", false );
					profile.setPreference( "browser.download.manager.useWindow", false );
					profile.setPreference( "server.sync.prefs.sync.browser.download.manager.showWhenStarting", false );
					//profile.setPreference( "browser.helperApps.neverAsk.openFile", "application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream" );
					profile.setPreference( "browser.download.panel.shown", false );
					profile.setPreference( "pdfjs.disabled", true );
					//caps.setCapability(FirefoxDriver.PROFILE,profile);     
					driver = new FirefoxDriver(profile);
					
		            
				}else if (browserType.contains("ie")){ // support for ie 9/10/11
		            
					//below code will set the default zoom setting
					System.setProperty("webdriver.ie.driver", Globals.IEDriverEXEPath);  
					DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
					//caps.setCapability("requireWindowFocus", true);
					caps.setCapability("ignoreZoomSetting", true);
					caps.setCapability("EnableNativeEvents", false);
					caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					caps.setCapability("browserstack.ie.enablePopups", "true");
	                caps.setCapability("unexpectedAlertBehaviour", "accept");
	                caps.setCapability("ignoreProtectedModeSettings", true);
	                caps.setCapability("disable-popup-blocking", true);
	                caps.setCapability("enablePersistentHover", true);
	                caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
					driver = new InternetExplorerDriver(caps);
				    
			 }else if (browserType.contains("chrome")) { //support for all chrome versions
		              System.setProperty("webdriver.chrome.driver",Globals.ChromeDriverEXEPath );
		              
		              String downloadFilepath = Globals.currentRunPath;
		              HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		              chromePrefs.put("profile.default_content_settings.popups", 0);
		              chromePrefs.put("download.default_directory", downloadFilepath);
		              
		              ChromeOptions options = new ChromeOptions();
		              HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
		              options.setExperimentalOption("prefs", chromePrefs);
		              options.addArguments("--test-type");
		              options.addArguments("--disable-extensions");
		              options.addArguments("--safebrowsing-disable-download-protection");
		              options.addArguments("--allow-running-insecure-content");
		              DesiredCapabilities cap = DesiredCapabilities.chrome();
		              cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
		              cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		              cap.setCapability(ChromeOptions.CAPABILITY, options);
		              
		              driver = new ChromeDriver(cap);
				}
				
			}
		
			
			driver.manage().window().maximize(); // maximize browser
			log.info("browserLaunch | Maximized Browser - "+ browserType);
			
			Globals.driver.manage().timeouts().pageLoadTimeout(Globals.gPageLoadTimeout,TimeUnit.SECONDS); // page timeout
			log.info("browserLaunch | PageLoadTimeout Set . Value -  - "+ Globals.gPageLoadTimeout);
	        
			Globals.driver.manage().timeouts().implicitlyWait(Globals.gImplicitlyWait, TimeUnit.SECONDS);
			log.info("browserLaunch | Implicit Wait for Object Set . Value -  - "+ Globals.gImplicitlyWait); // implicit timeout for object identification/operation
			
			Globals.driver.manage().deleteAllCookies();
			retval=Globals.KEYWORD_PASS;
			
		}catch(Exception e){
			log.error("browserLaunch | Exception occurred - " + e.toString());
		}
		
		return driver;
	}

}
