package com.sterlingTS.utils.commonUtils;


import org.openqa.selenium.WebDriver;

import com.sterlingTS.seleniumUI.seleniumConnection;
import com.sterlingTS.utils.commonVariables.Globals;

public class BaseCommon {
	
	public static WebDriver driver = null;
	
	public static WebDriver getDriverConnection()
	{
		System.out.println("Global Driver is :"+"\t"+Globals.driver);
			if(Globals.driver == null || Globals.driver.toString().contains("null"))
			{
			driver = seleniumConnection.browserLaunch(Globals.BrowserType);
			Globals.driver=driver;
			return driver;
			}else{
				return Globals.driver;
			}
		
	}
	
	public static void init()
	{
		driver = getDriverConnection();
	}
	

}
