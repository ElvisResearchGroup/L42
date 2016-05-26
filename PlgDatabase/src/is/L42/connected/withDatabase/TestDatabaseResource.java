package is.L42.connected.withDatabase;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDatabaseResource {
	private static final String TEST_DATABASE_NAME = "webdb1";
	private static final String TEST_SERVER_NAME =  "jdbc:derby:codejava";
	private static final String TEST_URL = TEST_SERVER_NAME+TEST_DATABASE_NAME+";create=true";
	private static final String TEST_USERNAME = "TEST_USER";
	private static final String TEST_PASWORD = "TEST_PASS";
	private static final String TEST_TABLE_NAME = "TEST_TABLE";

	private static final String TEST_CREATE_TABLE = ""
	+"CREATE TABLE " + TEST_TABLE_NAME + " "
		+"("
		+"PersonID int  NOT NULL PRIMARY KEY,"
		+"LastName varchar(255) NOT NULL,"
		+"FirstName varchar(255),"
		+"Address varchar(255),"
		+"City varchar(255)"
	+")";
	
	//private static final String[][] TEST_TABLE_PROPERTIES = new String[][]{{"PersonID","int"},{"FirstName","varchar(255)"}};
	//private static final String[][] TEST_TABLE_DATA = new String[][]{{"PersonID","1"},{"FirstName","'James'"}};

	private static final String TEST_TABLE_DELETE_MIKE_1Left = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID > 1 AND FirstName = 'Mike'";
	private static final String TEST_TABLE_DELETE_ALLMIKES = "DELETE FROM " + TEST_TABLE_NAME + " WHERE FirstName = 'Mike'";

	private static final String TEST_TABLE_DELETEDATA = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID = 1";
	private static final String TEST_TABLE_DELETEWRONGDATA = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID = 75";
	private static final String TEST_TABLE_INSERTDATA = "INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 1, 'James', 'Veugelaers' )";
	private static final String TEST_TABLE_SELECTDATA = "SELECT * FROM " + TEST_TABLE_NAME + " WHERE PersonId = 1";

	private static final String[] TEST_TABLE_BIGDATA = new String[]{
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 0, 'Mike',  'Tyson' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 1, 'Marco', 'L42God' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 2, 'Mike',  'Tyson' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 3, 'Mike',  'Tyson' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 4, 'Mike',  'Tyson' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 5, 'James', 'Veugelaers' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 6, 'Mike',  'Tyson' )",
		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 7, 'Marco', 'L42God' )"
	};

	private static final String TEST_TABLE_SELECT_ALLMIKES = "SELECT * FROM " + TEST_TABLE_NAME + " WHERE FirstName = 'Mike'";


	private DatabaseResource sql;

	@Before
	public void setup(){
		try {
			sql = DatabaseResource.connectToServer(TEST_URL, TEST_USERNAME, TEST_PASWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown(){
		sql.dropTable(TEST_TABLE_NAME);
		sql.close();
	}

	@Test
	public void areConnected(){

		// Make sure we have an option
		assertTrue(sql != null);

		// Check we are connected
		assertTrue(sql.isConnected());
	}

	@Test
	public void createTable(){
		assertTrue(sql.createTable(TEST_CREATE_TABLE));
	}

	@Test
	public void failToCreateTable(){
		assertTrue(sql.createTable(TEST_CREATE_TABLE));
		assertTrue(!sql.createTable(TEST_CREATE_TABLE));
	}

	@Test
	public void insertQuery(){
		// Create the table
		assertTrue(sql.createTable(TEST_CREATE_TABLE));

		// Add the row
		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
	}
	
	@Test
	public void databaseExists(){
		
		// This database does not exist
		assertTrue(!DatabaseResource.databaseExists("jdbc:derby:sample","test",TEST_USERNAME,TEST_PASWORD));
		
		assertTrue(!sql.tableExists(TEST_TABLE_NAME));
		
		// Does not yet work
		// Does not yet work
		// Does not yet work
		
		// Check for our usual database
		assertTrue(DatabaseResource.databaseExists(TEST_SERVER_NAME,TEST_DATABASE_NAME,TEST_USERNAME,TEST_PASWORD));
	}
	
	@Test
	public void tableExists(){
		
		// Haven't created a table so this table should not exist
		assertTrue(!sql.tableExists(TEST_TABLE_NAME));
		
		// Create the table
		assertTrue(sql.createTable(TEST_CREATE_TABLE));
		
		// Table should exist now
		assertTrue(sql.tableExists(TEST_TABLE_NAME));
	}
	
	@Test
	public void emptyTable(){
		
		// Create the table
		assertTrue(sql.createTable(TEST_CREATE_TABLE));
		
		// Check it's empty
		assertTrue(sql.tableIsEmpty(TEST_TABLE_NAME));
		
		// Add something to it
		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
		
		// Check it's NOT empty
		assertTrue(!sql.tableIsEmpty(TEST_TABLE_NAME));
	}
	
	@Test
	public void removeQuery(){
		// Create the table
		assertTrue(sql.createTable(TEST_CREATE_TABLE));

		// Add the row
		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
		
		// Make sure we have at least 1 row
		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME)==1);

		// Delete a non existent row
		assertTrue(sql.delete(TEST_TABLE_DELETEWRONGDATA)==0);
		
		// Delete the row
		assertTrue(sql.delete(TEST_TABLE_DELETEDATA)==1);
		
		// We should not have any rows now
		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME)==0);
		
		// Delete the row that no longer exists
		assertTrue(sql.delete(TEST_TABLE_DELETEDATA)==0);
	}
	
	@Test
	public void selectQuery(){
		try {
			// Create the table
			assertTrue(sql.createTable(TEST_CREATE_TABLE));
	
			// Add the row
			assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
			
			// Select
			ResultSet result = sql.query(TEST_TABLE_SELECTDATA);
			assertTrue(result != null);
		
		
			// Check we got the right data back
			System.out.println("ResultSet:");
			int rows = 0;
			while(result.next()){
				
				// Get the ID from the current row
				int id = result.getInt("PERSONID");
				
				// The ID should be 1 as it's the first in the list
				int insertedId = 1;
				
				// Fail if they are not equal
				assertCompareIntegers(id,insertedId);
				rows++;
			}
			result.close();
			
			
			// Should only have one row
			assertTrue(rows == 1);
		} catch (SQLException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRowCount(){
		// Create the table
		assertTrue(sql.createTable(TEST_CREATE_TABLE));

		// Size should be 0
		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == 0);
		
		// Add each row from the big data and check the amount has increased
		for(int i = 0; i < TEST_TABLE_BIGDATA.length; i++){
			int rowcount = sql.getTableRowCount(TEST_TABLE_NAME);
			assertTrue(rowcount == i);
			
			
			String query = TEST_TABLE_BIGDATA[i];
			assertTrue(sql.insert(query)==1); // Check we did insert 1 row
		}
		
		// Added all the rows so the size should be equal to the data we gave it
		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
	}
	
	@Test
	public void selectQueryBigData5Mikes(){
		try {
			// Create the table
			assertTrue(sql.createTable(TEST_CREATE_TABLE));
	
			
			// Add all the rows
			for(String query : TEST_TABLE_BIGDATA){
				assertTrue(sql.insert(query)==1);
			}
			
			// Make sure we have the correct amount
			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
			
			// Select All 5 mikes
			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);

			// Check we got the right data back
			System.out.println("ResultSet:");
			int rows = 0;
			while(result.next()){
				int v = result.getInt("PersonID"); // Get something so JDBC is happy
				System.out.println(v);
				
				rows++;
			}
			result.close();

			// Should have 5 mikes
			assertCompareIntegers(rows, 5);

		} catch (SQLException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectQueryBigData1Mikes(){
		try {
			// Create the table
			assertTrue(sql.createTable(TEST_CREATE_TABLE));
	
			
			// Add all the rows
			for(String query : TEST_TABLE_BIGDATA){
				assertTrue(sql.insert(query)==1);
			}
			
			// Make sure we have the correct amount
			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
			
			// Remove 4 mikes
			assertCompareIntegers(sql.delete(TEST_TABLE_DELETE_MIKE_1Left),4);
			
			// Select All mikes
			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);

			// Check we got the right data back
			System.out.println("ResultSet:");
			int rows = 0;
			while(result.next()){
				int v = result.getInt("PersonID"); // Get something so JDBC is happy
				System.out.println(v);
				
				rows++;
			}
			result.close();
			
			// Should have 1 mike
			assertCompareIntegers(rows, 1);
			
			// Should have 3 people left
			assertCompareIntegers(sql.getTableRowCount(TEST_TABLE_NAME), 4);

		} catch (SQLException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectQueryBigData0Mikes(){
		try {
			// Create the table
			assertTrue(sql.createTable(TEST_CREATE_TABLE));
	
			
			// Add all the rows
			for(String query : TEST_TABLE_BIGDATA){
				assertTrue(sql.insert(query)==1);
			}
			
			// Make sure we have the correct amount
			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
			
			// Remove ALL mikes
			assertCompareIntegers(sql.delete(TEST_TABLE_DELETE_ALLMIKES),5);
			
			// Select All mikes
			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);

			// Check we got the right data back
			System.out.println("ResultSet:");
			int rows = 0;
			while(result.next()){
				int v = result.getInt("PersonID"); // Get something so JDBC is happy
				System.out.println(v);
				
				rows++;
			}
			result.close();
			
			// Should have 0 mikes
			assertCompareIntegers(rows, 0);
			
			// Should have 3 people left
			assertCompareIntegers(sql.getTableRowCount(TEST_TABLE_NAME), 3);

		} catch (SQLException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Removes quotes from a string that might be needed if we are creating a table.
	 * @param a
	 * @return new string of the original without quotes
	 */
	public String removeQuotes(String a){
		return a.replaceAll("'", "");
	}
	
	/**
	 * Compares the two given strings and produces a failed assert if they are not equal
	 * @param a 
	 * @param b
	 */
	public void assertCompareStrings(String a, String b){
		if( !a.equals(b) ){
			fail("Strings '" + a + "' and '" + b + "' are not equal!");
		}
	}
	
	/**
	 * Compares the two given ints and produces a failed assert if they are not equal
	 * @param a 
	 * @param b
	 */
	public void assertCompareIntegers(int a, int b){
		if( a != b ){
			fail("Integers " + a + " and " + b + " are not equal!");
		}
	}
	
	
}








