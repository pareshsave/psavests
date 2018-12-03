package com.sterlingTS.utils.commonUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;

import com.sterlingTS.seleniumUI.seleniumConnection;
import com.sterlingTS.utils.commonVariables.Globals;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Secur32Util;

public class CommonHelpMethods {
	
	public static Logger log = Logger.getLogger(CommonHelpMethods.class);
	
	public static SecretKeySpec createKey() throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeySpec key = ProtectedEncryption.createSecretKey(Globals.password.toCharArray(),
				Globals.salt, Globals.iterationCount, Globals.keyLength);
		
		return key;
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
    		taskkill("chromeDriver.exe");
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
	
	public void unzipJarINFolder()
	{
		try{
			String srcPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String destPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"refenceFile";
			log.info("Source Path is : "+"\t"+srcPath);
			log.info("All Framework Jar Unziped Successfully in to "+"\t"+destPath);
			unzip(srcPath,destPath);
			log.info("Source Path is : "+"\t"+srcPath);
			log.info("All Framework Jar Unziped Successfully in to "+"\t"+destPath);
		}catch(Exception ie){
			log.error("Framework Jar has not been unziped properly ...");
		}
		
	}
	
	public void unzipFrameworkFile()
	{
		try{
		//String [] fileArray = {"fetchEmail.vbs","getXML.vbs","postXML.vbs","shareFolder.vbs","sqljdbc_auth.dll"};
		String srcPath = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"refenceFile"+File.separator+"FrameworkJar.zip";
		String destPath = System.getProperty("user.dir")+File.separator+"jars";
		
		unzip(srcPath,destPath);
		log.info("Source Path is : "+"\t"+srcPath);
		log.info("All vbs File Unziped Successfully in to "+"\t"+destPath);
		}catch(Exception ie)
		{
			log.error("All vbs file have not been unziped properly ...");
		}
	}
	

}
