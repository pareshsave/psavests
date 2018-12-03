package com.sterlingTS.utils.commonVariables;



public class Enum {
	
	
	public enum ProductCode
	{ 
		NA(-1,"NA"),
		CRFED (1,"Criminal - Federal") , // first param is productid, 2nd param is product name 
		CRFM(2,"Criminal - County") ,
		CRFMM(3,"Criminal - Metro") ,
		CRINT(4,"Criminal - International") ,
		CRNW(5,"National Wants Warrants") ,
		CROPTS(6,"Criminal Record Options") ,
		CRSEX(7,"Criminal - Sex Offender  ") ,
		CRST(8,"Statewide Criminal (Max 1 Jurisdiction) w/ crim verification") ,
		CVCTY(9,"Civil - County") ,
		CVFED(10,"Civil - Federal") ,
		DOBSEC(11,"DOBSecure") ,
		DR(12,"Drivers Record") ,
		DS1(13,"Drug Test") ,
		EXOIG(14,"OIG-Excluded Parties") ,
		SSC1(18,"Employment Credit Report") ,
		SSV1(19,"SSN Trace") ,
		SSV2(20,"Absolute SSN Verification") ,
		SVCCMP(22,"Managed Compliance") ,
		VCCRD(26,"Verification - Credential/License") ,
		VCCRDI(27,"Verification - Credential/License (International)") ,
		VCEDU(28,"Education Verification") ,
		VCEDUI(29,"Education Verification (International)") ,
		VCEMP(30,"Employment Verification (Extended)") ,
		VCEMPI(31,"Employment Verification (International)") ,
		VCOFF(32,"Offline Verifications") ,
		VCPER(33,"Personal/Professional Reference Verification") ,
		VCPERI(34,"Personal/Professional Reference Verification (International)") ,
		VCTRD(35,"Trade/Member Association Verification") ,
		VCTRDI(36,"Trade/Member Association Verification (International)") ,
		WC(37,"Workers Compensation") ,
		CRDOC(39,"Criminal - Incarcerations") ,
		EPLS(40,"System for Award Management (SAM)") ,
		FACIS(41,"FACIS") ,
		NPDB(42,"Practitioner Data Bank") ,
		AOA(43,"Physician Check (AOA)") ,
		ABUSE(44,"Healthcare - Neglect/Abuse") ,
		DEA(45,"Healthcare - DEA License Number") ,
		VCMED(46,"Medical Privileges") ,
		VCPLI(47,"Professional Liability Insurance") ,
		CRNAT(48,"Criminal - National") ,
		CRNATV(49,"Multi-State/Jurisdiction Criminal Records Locator") ,
		CRSEXN(50,"National Sex Offender") ,
		SSC2(51,"Credit Report (Standard)") ,
		FFIEA(52,"Federal Financial Institutions") ,
		VCEMPB(53,"Employment Verification (Basic)") ,
		VCDOT(54,"Employment Verification (DOT)") ,
		SVCHS(55,"Managed Hold Notification") ,
		OFAC(56,"Office of Foreign Assets Control") ,
		CRSEXDOJ(57,"Criminal - Sex Offender 50 State DOJ") ,
		CRFEDEX(58,"Federal Criminal Record Extended") ,
		FORMI9(59,"Form I-9") ,
		FAACRD(61,"FAA License Credential Verification") ,
		FAAEMP(62,"Aviation Employment") ,
		FAAEMPI(63,"Aviation Employment International") ,
		FAAEDU(64,"Aviation Education") ,
		FAAEDUI(65,"Aviation Education International") ,
		FAAFOIA(66,"FAA Freedom of Information Act") ,
		FAANDR(67,"FAA National Driving Registry") ,
		BISSDOC(68,"Bureau Of Industry and Security Search (DOC)") ,
//		CRFEDEX(69,"50 State Federal District Court Search") ,
		GAPV(70,"Employment Gap (Verified)") ,
		GAPNV(71,"Employment Gap (Non-Verified)") ,
		TLR(72,"License and Traffic Report") ,
		CRFMX(73,"Criminal - County (10 year search)") ,
		CRFMXV(74,"Criminal - County (15 year search)") ,
		CRFMFD(75,"Criminal - County (full disclosure)") ,
		WATCH(76,"Washington Access to Criminal History") ,
		CRNATVNS(77,"Multi-State/Juris Criminal Records Locator (w/validation, w/o Sex Offender)") ,
		VCAUTOINS(78,"Verification - Auto Insurance") ,
		TSC1(79,"Tenant Screening Credit Report") ,
		CBSV(80,"Soc. Sec. Admin. SSN Verification (CBSV)") ,
		DPL(81,"Denied Persons List") ,
		MSMJIII(82,"Multi-State/Jurisdiction Criminal Records Locator (3 year search)") ,
		MSMJV(83,"Multi-State/Jurisdiction Criminal Records Locator (5 year search)") ,
		MSMJVII(84,"Multi-State/Jurisdiction Criminal Records Locator (7 year search)") ,
		MSMJCRFM(85,"Multi-State/Jurisdiction Validator") ,
		MSMJCRST(86,"Multi-State/Jurisdiction Validator") ,
		DSDOT(87,"Drug Test - DOT") ,
		FACISV(88,"FACIS Validator") ,
		FAACAIS(89,"FAA Comprehensive Airman Information System") ,
		VCRRV(90,"Rental Reference Verification") ,
		SSCAN(91,"Canadian Credit Report") ,
		PATRIOT(92,"Patriot Search") ,
		MAR(93,"Client Matrix Application") ,
		DATAENTRYFEE(94,"Data Entry/Order Entry - Fee") ,
		CRGENERIC(95,"Generic Criminal Product") ,
		FAAEMPDOT(97,"Aviation DOT Verification") ,
		DOTCIR(98,"DOT Crash and Inspection Record") ,
		DOTPHYSICAL(99,"DOT Physical") ,
		DOTDRUGTEST(100,"DOT Breath Alcohol Test") ,
		ABUSEAD(102,"Healthcare - Adult Abuse") ,
		MEDIA(103,"Media Search") ,
		DIRSHIP(104,"Worldwide Directorship Search") ,
		VASP(105,"Virginia State Police") ,
		GLOBEX(106,"Extended Global Sanctions") ,
		MRZPASS(107,"MRZ Passport Verification") ,
		FSI(108,"Financial Stability Indicator") ,
		DRCAN(109,"Canadian Driving Record") ,
		DSCF(110,"Collection Fee") ,
		DSBAT(111,"Breath Alcohol") ,
		DSP(112,"Physical") ,
		COCS(113,"COC Shipping") ,
		DSSH(114,"Drug Special Handling") ,
		DSK(115,"Drug Kits") ,
		SSC3(116,"No QC for Credit Report (Employment)") ,
		SMSPO(117,"Social Media Search Popular") ,
		SMSC(118,"Social Media Search Complete") ,
		SMSPL(119,"Social Media Search Plus") ,
		SMMSPO(120,"Social Media Monitoring Search Popular") ,
		SMMSC(121,"Social Media Monitoring Search Complete") ,
		SMMSPL(122,"Social Media Monitoring Search Plus") ,
		CBI(123,"Colorado Bureau of Investigation Statewide Criminal History") ,
		NJST(124,"New Jersey Statewide") ,
		IDST(125,"Idaho Statewide") ,
		CRNATVPLUS(126,"Multi-State/Juris Criminal Records Locator Plus (w/validation)") ,
		ARRDIRCDT(128,"Locator Select") ,
		VCPERAUTOA(131,"Verification - Personal/Professional Reference (Fully Automated)") ,
		VCPERAUTOB(132,"Verification - Personal/Professional Reference (E-Mail/Phone Fulfillment)") ,
		VCPERAUTOC(133,"Verification - Personal/Professional Reference (International Automated)") ,
		EXOIGB(134,"Excluded Parties Business") ,
		ARRDIRCRFM(135,"ArrestDirect County Validator") ,
		ARRDIRCRST(136,"ArrestDirect Statewide Validator") ,
		VCADD(137,"Address Verification") ,
		DATAEXPORTFEE(139,"Data Export Fees") ,
		FINGERPRINTINGFEE(141,"Fingerprinting Fees") ,
		I9SETUPFEE(142,"I9 Setup Fees") ,
		CDLIS(146,"Commercial Drivers License Locator") ,
		RDT(147,"Registered Drug Test") ,
		VCEMPAUTOA(149,"Verification - Employment (Fully Automated)") ,
		VCEMPAUTOB(150,"Verification - Employment (E-Mail/Phone Fulfillment)") ,
		VCEMPAUTOC(151,"Verification - Employment (International Automated)") ,
//		FACIS(152,"FACIS") ,
		CRSD(154,"Self Disclosed Criminal History information") ,
		DOTFMCSA(156,"FMCSA PSP DOT Crash and Inspection Records") ,
		UDR(157,"Unscorable Drivers Record") ,
		VCEDUAUTO(164,"Automated Education") ,
		LDP(166,"Limited Denial of Participation (HUD Programs)") ,
		CRNATUO(178,"Enhanced Nationwide – Updates Only") ,
		VCINTRVW(187,"Interview") ,
		FNGRPRN(188,"Finger Printing") ,
		EMPVER48(190,"Employment Verification - 48 Hours") ,
		EMPLOYMENT(191,"Employment Verification Technology") ,
		EDUCATION(192,"Education Verification Technology") ,
		PERSONALREF(193,"Personal/Professional Reference Verification Technology") ,
		AMAP(194,"AMA Master Profile") ,
		SRV(195,"Selective Service Registration Verification") ,
		GDBFAA(196,"GA Dept. of Banking and Finance Admin Actions") ,
		CREMP(197,"Employment- CREMP") ,
		CREDU(198,"Education- CREDU") ,
		IDR(199,"Insurance Drivers Record") ,
		CVCTYU(200,"Civil - County (7 years Upper Level Only)") ,
		FPTPA(218,"Finger Printing - TPA") ,
		FPSCP(219,"Fingerprinting - Sterling Channels w/ Pre-Enrollment") ,
		FPSCNP(220,"Fingerprinting - Sterling Channels no Pre-Enrollment") ,
		NYOMIG(232,"NYOMIG - New York State Office of the Medicaid Inspector General") ,
		BNKGLB(236,"Bankruptcy Search for Global Screening") ,
		FRSGLB(238,"Financial Regulatory Search for Global Screening") ,
		CRIMGLB(239,"Comprehensive Criminal for Global Screening") ,
		EMPGLB(240,"Employment Verification for Global Screening") ,
		EDUGLB(241,"Education Verification for Global Screening") ,
		CIVLITGLB(242,"Civil Litigation for Global Screening") ,
		CREDITGLB(243,"Credit for Global Screening") ,
		CRDNTLGLB(244,"Professional Credential for Global Screening") ,
		REFGLB(245,"Professional and Personal References for Global Screening") ,
		DIRSHPGLB(246,"Directorship Search for Global Screening") ,
		ADVMEDGLB(247,"Adverse Media Search for Global Screening") ,
		IDVGLB(248,"ID Verification for Global Screening") ,
		DLVGLB(249,"Driving License Verification for Global Screening") ,
		RTWGLB(250,"Right-to-work Verification for Global Screening") ,
		ADVGLB(251,"Address Verification for Global Screening") ,
		GLXGLB(252,"GlobeX for Global Screening") ,
		SSPINFO(253,"SSN Profile Information") ,
		ROC(254,"Employee Credit Report (ROC)") ,
		ROCFICO(255,"Employee Credit Report (ROCFICO)") ,
		ROCU(256,"Employee Credit Report (ROCU)");
		
		public int productID;
		public String productName;
		ProductCode(int productID,String productName){
			this.productID = productID;
			this.productName = productName;
		}
		
		
	}
	
	public enum SearchReqScore 
	{
		NA(-1),//added for returning invalid score
		UNPERFORMABLE(0),
		CANCELLED(1),
		HOLD(2),
		CONSIDER(50),
		REVIEW(51),
		CLEAR(100),
		PASSED(101),
		COMPLETE(102),
		LEVEL1(103),
		LEVEL2(104),
		LEVEL3(105);
		
		public int ScoreID;
		SearchReqScore(int ScoreID){
			this.ScoreID = ScoreID;
		}
		
	}
	
	public enum SearchReqStatus
		{
			NA(-1),//added for returning invalid status
			QUEUE(0),
	        QCQUEUE(1),
	        IPQUEUE(2),
	        CCQUEUE(3),
	        VRQUEUE(4),
	        SIRVQUEUE(5),
	        VERQUEUE(6),
	        DSQUEUE(7),
	        VERREVIEWQUEUE(8),
    
	        ROUTED(10),
	        INPROCESS(11),
	        OUTSOURCED(12),
	        SUBMITTEDRESEARCH(3),
	        VENDORRESEARCHCOMPLETE(14),
	        SERVICEREQUEST(15),
	        INDISPUTE(16),
	        RDTQUEUE(17),
    
	        QC(20),
	        QCREVIEW(21),
	        IP(30),
	        CC(40),
	        VR(41),
	        SIRV(42),
	        VER(43),
	        DS(44),
    
	        VCEDUCONTACTQUEUE(46),
	        VCEDUAUTOQUEUE(47),
	        AUTODNCSELFQUEUE(48),
	        MISSINGINFORMATION(50),
	        AEDISPUTEQUEUE(51),
	        EMPVER48QUEUE(52),
	        EMP_3_CLOSEQUEUE(53),
	        EMP_3_2_CLOSEQUEUE(54),
	        EMP_3_2_PROOF_CLOSEQUEUE(55),
	        EMP_3_2_AI_1_CLOSEQUEUE(56),
	        EMP_3_2_AI_2_CLOSEQUEUE(57),
	        EDU_3_CLOSEQUEUE(58),
	        EDU_3_ACCRED_CLOSEQUEUE(59),
	        EDU_3_2_PROOF_CLOSEQUEUE(60),
	        EDU_3_2_AUTH_DIPLOMA_CLOSEQUEUE(61),
	        EDU_3_2_AI_2C_LOSEQUEUE(62),
	        PER_3_CLOSEQUEUE(63),
	        PER_3_2_CLOSEQUEUE(64),
	        PER_3_2_AI_2_CLOSEQUEUE(65),
	        PRINTSVCAAFINALELEC(66),
	        PRINTSVCAAPREELEC(67),
	        BISHOPSQUEUE(68),
	        BISHOPSQCQUEUE(69),
	        AUTOJURISDICTIONQUEUE(72),
	        CRIMFAXSUCCESSQUEUE(75),
	        CRIMFAXFAILEDQUEUE(78),
	        CRIMFAXSUCCESS(79),
	        CRIMFAXFAILED(80),
	        COMPLETE(100),
	        BGORDERIGNORE(103),
	        ARCHIVE(105),
	        DELETED(220);
				
            
            public int StatusID;
            SearchReqStatus(int StatusID){
    			this.StatusID = StatusID;
    		}

	}
	

	
	public enum OrderScore 
	{
		NA(-1),//added for returning invalid score
		PENDING(0),
		CANCELLED(8),
		CLEAR(10),
		PASSED(11),
		LEVEL1(12),
		LEVEL2(19),
		CONSIDER(20),
		REVIEW(21),
		LEVEL3(22);
  
		
		public int ScoreID;
		OrderScore(int ScoreID){
			this.ScoreID = ScoreID;
		}
		
	}
	
	public enum OrderStatus 
	{
		NA(-1),//added for returning invalid score
		DRAFT(0),
        PENDING(5),
        QCQUEUE(6),
        COMPLETE(10),
        ADVERSEACTION(11),
        DISPUTE(12),
        ARCHIVE(20),
        EXPORTED(21),
        HOLD(22),
        REVIEW(23),
        ORDERSQCQUEUE(24),
        INPROCESS(25),
        PREDRAFT(1);

  	
		public int StatusID;
		OrderStatus(int StatusID){
			this.StatusID = StatusID;
		}
		
	}
	
	public enum VV_Products 
	{
		NA("NA",-1,0.00),//added for returning invalid score
		L1("Level 1: Basic Criminal History Record Locator Search",1,13.00),
        L2("Level 2: Advanced Criminal History Record Locator Search",2,25.00),
        L3("Level 3: Complete Criminal History Record Locator Search",3,45.00),
        CREDIT("Consumer Credit Check",6,16.00),
        MVR("Motor Vehicle Record Check",4,13.50),
        ABUSE("Neglect/Abuse Registry",11,12.00),
        OIG("OIG-GSA Excluded Parties",10,11.00),
        REF("Reference Interview",5,18.00),
        SSNPROF("SSN Profile",13,3.50),
        CBSV("SSN Verification (CBSV)",12,5.00),
        ID("ID Confirm",14,3.50),
        LS("Locator Select",15,4.00),
        MAR("Managed Adverse Action",8,4.00),
        SELFADVERSE("Self-Serve Adverse Action Kit",9,0.00),
        FEDCRIM("Federal Criminal Search",18,26.25),
        FEDCIVIL("Federal Civil Search",17,26.25),
        COUNTYCIVIL("County Civil Search",16,40.50), 
        GLOBEX("Globex",19,18.00);
      

  	
		public double DefPrice;
		public int ProductID;
		public String ProductName;
		VV_Products(String ProductName ,int ProductID,double DefPrice){
			this.DefPrice = DefPrice;
			this.ProductName = ProductName;
			this.ProductID = ProductID;
			
		}
		
	}
	
	public static OrderStatus fetchOrderStatusEnum(int statusID){
		
		OrderStatus requiredStatus = OrderStatus.NA;
		for(OrderStatus status:OrderStatus.values()){
			if(status.StatusID == statusID){
				requiredStatus =  status;
				break;
			}
		}
		
		return requiredStatus;
	}
	
	public static OrderScore fetchOrderScoreEnum(int scoreID){
		
		OrderScore requiredScore = OrderScore.NA;
		for(OrderScore score:OrderScore.values()){
			if(score.ScoreID == scoreID){
				requiredScore =  score;
				break;
			}
		}
		
		return requiredScore;
	}
	
	public static ProductCode fetchProductCodeEnum(int productID){
		
		ProductCode reqProduct = ProductCode.NA;
		for(ProductCode product:ProductCode.values()){
			if(product.productID == productID){
				reqProduct =  product;
				break;
			}
		}
		
		return reqProduct;
	}
	
	
	public enum State {
		
	    ALABAMA("Alabama", "AL"), 
	    ALASKA("Alaska", "AK"), 
	    AMERICAN_SAMOA("American Samoa", "AS"), 
	    ARIZONA("Arizona", "AZ"), 
	    ARKANSAS("Arkansas", "AR"), 
	    CALIFORNIA("California", "CA"), 
	    COLORADO("Colorado", "CO"), 
	    CONNECTICUT("Connecticut", "CT"), 
	    DELAWARE("Delaware", "DE"), 
	    DISTRICT_OF_COLUMBIA("District of Columbia", "DC"), 
	    FEDERATED_STATES_OF_MICRONESIA("Federated States of Micronesia", "FM"), 
	    FLORIDA("Florida", "FL"), 
	    GEORGIA("Georgia", "GA"), 
	    GUAM("Guam", "GU"), 
	    HAWAII("Hawaii", "HI"), 
	    IDAHO("Idaho", "ID"), 
	    ILLINOIS("Illinois", "IL"), 
	    INDIANA("Indiana", "IN"), 
	    IOWA("Iowa", "IA"), 
	    KANSAS("Kansas", "KS"), 
	    KENTUCKY("Kentucky", "KY"), 
	    LOUISIANA("Louisiana", "LA"), 
	    MAINE("Maine", "ME"), 
	    MARYLAND("Maryland", "MD"), 
	    MARSHALL_ISLANDS("Marshall Islands", "MH"), 
	    MASSACHUSETTS("Massachusetts", "MA"), 
	    MICHIGAN("Michigan", "MI"), 
	    MINNESOTA("Minnesota", "MN"), 
	    MISSISSIPPI("Mississippi", "MS"), 
	    MISSOURI("Missouri", "MO"), 
	    MONTANA("Montana", "MT"), 
	    NEBRASKA("Nebraska", "NE"), 
	    NEVADA("Nevada","NV"), 
	    NEW_HAMPSHIRE("New Hampshire", "NH"), 
	    NEW_JERSEY("New Jersey", "NJ"), 
	    NEW_MEXICO("New Mexico", "NM"),
	    NEW_YORK("New York", "NY"), 
	    NORTH_CAROLINA("North Carolina", "NC"), 
	    NORTH_DAKOTA("North Dakota", "ND"), 
	    NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", "MP"), 
	    OHIO("Ohio", "OH"), 
	    OKLAHOMA("Oklahoma", "OK"), 
	    OREGON("Oregon", "OR"), 
	    PALAU("Palau","PW"), 
	    PENNSYLVANIA("Pennsylvania", "PA"), 
	    PUERTO_RICO("Puerto Rico", "PR"), 
	    RHODE_ISLAND("Rhode Island", "RI"), 
	    SOUTH_CAROLINA("South Carolina", "SC"), 
	    SOUTH_DAKOTA("South Dakota", "SD"), 
	    TENNESSEE("Tennessee", "TN"), 
	    TEXAS("Texas", "TX"), 
	    UTAH("Utah", "UT"), 
	    VERMONT("Vermont", "VT"), 
	    VIRGIN_ISLANDS("Virgin Islands", "VI"), 
	    VIRGINIA("Virginia", "VA"),
	    WASHINGTON("Washington", "WA"),
	    WEST_VIRGINIA("West Virginia", "WV"), 
	    WISCONSIN("Wisconsin", "WI"),
	    WYOMING("Wyoming", "WY"), 
	    UNKNOWN("Unknown", "");
	
	    /**
	     * The state's name.
	     */
	    public String name;
	
	    /**
	     * The state's abbreviation.
	     */
	    public String abbreviation;
	
	    State(String name, String abbreviation) {
	        this.name = name;
	        this.abbreviation = abbreviation;
	    }
	    
	    /**
	     * Returns the state's abbreviation.
	     *
	     * @return the state's abbreviation.
	     */
	    public String getAbbreviation() {
	        return abbreviation;
	    }
	    
	    public String getName(){
		  return name;
	    }
	
	}
	
	
	public enum CMAStatus 
	{
		NA(-1,"NA"),
		REVIEW(5,"Review"),
		LEVEL3(4,"LEVEL3"),
		LEVEL2(3,"LEVEL2"),
		LEVEL1(2,"LEVEL1"),
		PASS(1,"PASS"),
		PENDING(0,"PENDING");

  	
		public int statusID;
		public String statusName;
		CMAStatus(int statusID,String statusName){
			this.statusID = statusID;
			this.statusName = statusName;
		}
		
	}
}
