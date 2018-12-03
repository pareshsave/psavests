package testExcecutionData;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.sterlingTS.utils.commonUtils.database.DataBaseManager;

public class DatabasequesryTest {
	
 static Logger log = Logger.getLogger(DatabasequesryTest.class);
	
	public static void main(String[] args) throws SQLException {
//		String dbURL = "jdbc:sqlserver://"+"QATLSQL29" + ";portNumber="+"55297"+";databaseName=MerQa2";
//		String userName = "MercuryWeb";
//		String pwd = "Mz\\6Iy`16";
		
		//String dbURL = "jdbc:sqlserver://"+"QATLMSQL01"+"\\"+"QATLMSQLI01" + ";portNumber="+"1433"+";databaseName=PRISMLocalSQLDB";
		String dbURL = "jdbc:sqlserver://"+"QATLMSQL01"+"\\"+"QATLMSQLI01" +";databaseName=PRISMLocalSQLDB";
		String userName = "prismuserqa";
		String pwd = "steR$1234";
		
		log.debug("asdfsfsf");
		
		DataBaseManager db = new DataBaseManager(dbURL,userName,pwd);
	//	Object[][] abc = db.executeSelectQuery("select * from NpnOrder where NpnOrderId=1234");
		Object[][] abc = db.executeSelectQuery("select top 1 Screening.RequestOrderNumber, fulfillment.Status, fulfillment.ServerReferenceId, fulfillment.Error_Code FROM Screening,fulfillment where Screening.screeningid = fulfillment.screeningid and RequestOrderNumber = 'TST_15952_6' order by fulfillment.Fulfillment_Data_ID desc");
		for(int i=0;i<abc.length;i++)
		{
			for(int j =0;j<abc[i].length;j++)
			{
				System.out.print(abc[i][j] +"\t");
				//log.info(abc[i][j]);
			}
		}
		
		db.closeConnection();
	}

}
