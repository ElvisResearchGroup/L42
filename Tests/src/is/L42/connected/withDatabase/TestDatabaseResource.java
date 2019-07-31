package is.L42.connected.withDatabase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//public class TestDatabaseResource {
//	private static final String TEST_DATABASE_NAME = "webdb1";
//	private static final String TEST_PROTOCOL =  "jdbc:derby:";
//	private static final String TEST_SERVER_NAME = "codejava";
//	private static final String TEST_URL = TEST_PROTOCOL+TEST_SERVER_NAME+TEST_DATABASE_NAME+";create=true";
//	private static final String TEST_USERNAME = "TEST_USER";
//	private static final String TEST_PASWORD = "TEST_PASS";
//	private static final String TEST_TABLE_NAME = "TEST_TABLE";
//
//	private static final String TEST_CREATE_TABLE = ""
//	+"CREATE TABLE " + TEST_TABLE_NAME + " "
//		+"("
//		+"PersonID int  NOT NULL PRIMARY KEY,"
//		+"LastName varchar(255) NOT NULL,"
//		+"FirstName varchar(255),"
//		+"Address varchar(255),"
//		+"City varchar(255)"
//	+")";
//
//	//private static final String[][] TEST_TABLE_PROPERTIES = new String[][]{{"PersonID","int"},{"FirstName","varchar(255)"}};
//	//private static final String[][] TEST_TABLE_DATA = new String[][]{{"PersonID","1"},{"FirstName","'James'"}};
//
//	private static final String TEST_TABLE_DELETE_MIKE_1Left = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID > 1 AND FirstName = 'Mike'";
//	private static final String TEST_TABLE_DELETE_ALLMIKES = "DELETE FROM " + TEST_TABLE_NAME + " WHERE FirstName = 'Mike'";
//
//	private static final String TEST_TABLE_DELETEDATA = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID = 1";
//	private static final String TEST_TABLE_DELETEWRONGDATA = "DELETE FROM " + TEST_TABLE_NAME + " WHERE PersonID = 75";
//	private static final String TEST_TABLE_INSERTDATA = "INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ( 1, 'James', 'Veugelaers' )";
//	private static final String TEST_TABLE_SELECTDATA = "SELECT * FROM " + TEST_TABLE_NAME + " WHERE PersonId = 1";
//
//	private static final String[] TEST_TABLE_BIGDATA_VALUES = new String[]{
//			"0, 'Mike',  'Tyson'",
//			"1, 'Marco', 'L42God'",
//			"2, 'Mike',  'Tyson'",
//			"3, 'Mike',  'Tyson'",
//			"4, 'Mike',  'Tyson'",
//			"5, 'James', 'Veugelaers'",
//			"6, 'Mike',  'Tyson'",
//			"7, 'Marco', 'L42God'"
//		};
//	private static final String[] TEST_TABLE_BIGDATA = new String[]{
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[0]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[1]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[2]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[3]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[4]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[5]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[6]+")",
//		"INSERT INTO " + TEST_TABLE_NAME + " ( PERSONID, FirstName, LastName ) VALUES ("+TEST_TABLE_BIGDATA_VALUES[7]+")"
//	};
//
//	private static final String TEST_TABLE_SELECT_ALLMIKES = "SELECT * FROM " + TEST_TABLE_NAME + " WHERE FirstName = 'Mike'";
//
//
//	private DatabaseResource sql;
//
//	@Before
//	public void setup(){
//		try {
//			sql = DatabaseResource.connectToServer(TEST_URL, TEST_USERNAME, TEST_PASWORD);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@After
//	public void tearDown(){
//
//		// Close connection
//		sql.close();
//
//		// Shut down the server
//		sql.shutdownConnection();
//
//		// Delete the files so we no longer have the database
//		String folderName = "./"+TEST_SERVER_NAME+TEST_DATABASE_NAME + "/";
//
//		// Delete the folder from our directory
//		File file = new File(folderName);
//		if( file.exists() ){
//			try {
//				delete(file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		// Delete the log file
//		File logFile = new File("derby.log");
//		if( logFile.exists() ){
//			try {
//				delete(logFile);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private void delete(File f) throws IOException {
//
//		if (f.isDirectory()) {
//			for (File c : f.listFiles())
//				delete(c);
//		}
//		if (!f.delete())
//		    throw new FileNotFoundException("Failed to delete file: " + f);
//	}
//
//	@Test
//	public void areConnected(){
//
//		// Make sure we have an option
//		assertTrue(sql != null);
//
//		// Check we are connected
//		assertTrue(sql.isConnected());
//
//		// Close the connection
//		sql.close();
//
//		// Should not be connected after closing
//		assertTrue(!sql.isConnected());
//	}
//
//	@Test
//	public void createTable(){
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//	}
//
//	@Test
//	public void failToCreateTable(){
//
//		// Should be able to create a table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Should get an exception stating that we can not create this table!
//		assertTrue(!sql.createTable(TEST_CREATE_TABLE));
//	}
//
//	@Test
//	public void insertQuery(){
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Add the row
//		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
//	}
//
//	@Test
//	public void databaseExists(){
//
//		// Check for our usual database
//		String connectionString = TEST_PROTOCOL+TEST_SERVER_NAME+TEST_DATABASE_NAME;
//		System.out.println("connectionString " + connectionString);
//		assertTrue(DatabaseResource.databaseExists(connectionString,TEST_USERNAME,TEST_PASWORD));
//
//
//		// This database does not exist
//		assertTrue(!DatabaseResource.databaseExists("jdbc:derby:sample",TEST_USERNAME,TEST_PASWORD));
//
//
//		// Close the current connection
//		tearDown();
//
//		assertTrue(!DatabaseResource.databaseExists(connectionString,TEST_USERNAME,TEST_PASWORD));
//	}
//
//	@Test
//	public void tableExists(){
//
//		// Haven't created a table so this table should not exist
//		assertTrue(!sql.tableExists(TEST_TABLE_NAME));
//
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Table should exist now
//		assertTrue(sql.tableExists(TEST_TABLE_NAME));
//	}
//
//	@Test
//	public void emptyTable(){
//
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Check it's empty
//		assertTrue(sql.tableIsEmpty(TEST_TABLE_NAME));
//
//		// Add something to it
//		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
//
//		// Check it's NOT empty
//		assertTrue(!sql.tableIsEmpty(TEST_TABLE_NAME));
//	}
//
//	@Test
//	public void removeQuery(){
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Add the row
//		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
//
//		// Make sure we have at least 1 row
//		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME)==1);
//
//		// Delete a non existent row
//		assertTrue(sql.delete(TEST_TABLE_DELETEWRONGDATA)==0);
//
//		// Delete the row
//		assertTrue(sql.delete(TEST_TABLE_DELETEDATA)==1);
//
//		// We should not have any rows now
//		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME)==0);
//
//		// Delete the row that no longer exists
//		assertTrue(sql.delete(TEST_TABLE_DELETEDATA)==0);
//	}
//
//	@Test
//	public void selectQuery(){
//		try {
//			// Create the table
//			assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//			// Add the row
//			assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
//
//			// Select
//			ResultSet result = sql.query(TEST_TABLE_SELECTDATA);
//			assertTrue(result != null);
//
//
//			// Check we got the right data back
//			System.out.println("ResultSet:");
//			int rows = 0;
//			while(result.next()){
//
//				// Get the ID from the current row
//				int id = result.getInt("PERSONID");
//
//				// The ID should be 1 as it's the first in the list
//				int insertedId = 1;
//
//				// Fail if they are not equal
//				assertCompareIntegers(id,insertedId);
//				rows++;
//			}
//			result.close();
//
//
//			// Should only have one row
//			assertTrue(rows == 1);
//		} catch (SQLException e) {
//			fail(e.toString());
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void selectAsStringTest(){
//
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Insert our Data
//		assertTrue(sql.insert(TEST_TABLE_INSERTDATA)==1);
//
//		// Select everything as a string
//		String selection = sql.queryToString("SELECT * FROM " + TEST_TABLE_NAME);
//		assertCompareStrings(selection, "1 Veugelaers James null null");
//	}
//
//	@Test
//	public void selectAsStringBigDataTest(){
//
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Insert our Data
//		for(String query : TEST_TABLE_BIGDATA){
//			assertTrue(sql.insert(query)==1);
//		}
//
//		// Select everything as a string
//		String selection = sql.queryToString("SELECT * FROM " + TEST_TABLE_NAME);
//		selection = selection.replaceAll("null", ""); // Remove all nulls
//
//		// Turn our current values that we added into a set
//		// We need a collection because order of columns can differ to how we input them
//		List<String> insertedRows = new ArrayList<String>();
//		for(String row : TEST_TABLE_BIGDATA_VALUES){
//			insertedRows.add(row.replaceAll("[,']", ""));
//		}
//
//
//		// Compare each row in the queriedSelection with what we added to the List
//
//		int index = 0;
//		Scanner selectionScanner = new Scanner(selection);
//		while( selectionScanner.hasNext() ){
//			String selectedRow = selectionScanner.nextLine().trim();
//			String insertedRow = insertedRows.get(index).trim();
//
//			// Check if each token in the selectedRow is in the insertedRow
//			Scanner tokenScanner = new Scanner(selectedRow);
//			while(tokenScanner.hasNext()){
//				String token = tokenScanner.next();
//				if( !insertedRow.contains(token) ){
//
//					// Rows are not equal
//					fail("Rows at index " + index + " are not equal!\n\t" + selectedRow + "\n\t" + selectedRow);
//				}
//			}
//
//			// Start next row
//			tokenScanner.close();
//			index++;
//		}
//		selectionScanner.close();
//	}
//
//	@Test
//	public void testRowCount(){
//		// Create the table
//		assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//		// Size should be 0
//		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == 0);
//
//		// Add each row from the big data and check the amount has increased
//		for(int i = 0; i < TEST_TABLE_BIGDATA.length; i++){
//			int rowcount = sql.getTableRowCount(TEST_TABLE_NAME);
//			assertTrue(rowcount == i);
//
//
//			String query = TEST_TABLE_BIGDATA[i];
//			assertTrue(sql.insert(query)==1); // Check we did insert 1 row
//		}
//
//		// Added all the rows so the size should be equal to the data we gave it
//		assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
//	}
//
//	@Test
//	public void selectQueryBigData5Mikes(){
//		try {
//			// Create the table
//			assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//
//			// Add all the rows
//			for(String query : TEST_TABLE_BIGDATA){
//				assertTrue(sql.insert(query)==1);
//			}
//
//			// Make sure we have the correct amount
//			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
//
//			// Select All 5 mikes
//			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);
//
//			// Check we got the right data back
//			System.out.println("ResultSet:");
//			int rows = 0;
//			while(result.next()){
//				int v = result.getInt("PersonID"); // Get something so JDBC is happy
//				System.out.println(v);
//
//				rows++;
//			}
//			result.close();
//
//			// Should have 5 mikes
//			assertCompareIntegers(rows, 5);
//
//		} catch (SQLException e) {
//			fail(e.toString());
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void selectQueryBigData1Mikes(){
//		try {
//			// Create the table
//			assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//
//			// Add all the rows
//			for(String query : TEST_TABLE_BIGDATA){
//				assertTrue(sql.insert(query)==1);
//			}
//
//			// Make sure we have the correct amount
//			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
//
//			// Remove 4 mikes
//			assertCompareIntegers(sql.delete(TEST_TABLE_DELETE_MIKE_1Left),4);
//
//			// Select All mikes
//			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);
//
//			// Check we got the right data back
//			System.out.println("ResultSet:");
//			int rows = 0;
//			while(result.next()){
//				int v = result.getInt("PersonID"); // Get something so JDBC is happy
//				System.out.println(v);
//
//				rows++;
//			}
//			result.close();
//
//			// Should have 1 mike
//			assertCompareIntegers(rows, 1);
//
//			// Should have 3 people left
//			assertCompareIntegers(sql.getTableRowCount(TEST_TABLE_NAME), 4);
//
//		} catch (SQLException e) {
//			fail(e.toString());
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void selectQueryBigData0Mikes(){
//		try {
//			// Create the table
//			assertTrue(sql.createTable(TEST_CREATE_TABLE));
//
//
//			// Add all the rows
//			for(String query : TEST_TABLE_BIGDATA){
//				assertTrue(sql.insert(query)==1);
//			}
//
//			// Make sure we have the correct amount
//			assertTrue(sql.getTableRowCount(TEST_TABLE_NAME) == TEST_TABLE_BIGDATA.length);
//
//			// Remove ALL mikes
//			assertCompareIntegers(sql.delete(TEST_TABLE_DELETE_ALLMIKES),5);
//
//			// Select All mikes
//			ResultSet result = sql.query(TEST_TABLE_SELECT_ALLMIKES);
//
//			// Check we got the right data back
//			System.out.println("ResultSet:");
//			int rows = 0;
//			while(result.next()){
//				int v = result.getInt("PersonID"); // Get something so JDBC is happy
//				System.out.println(v);
//
//				rows++;
//			}
//			result.close();
//
//			// Should have 0 mikes
//			assertCompareIntegers(rows, 0);
//
//			// Should have 3 people left
//			assertCompareIntegers(sql.getTableRowCount(TEST_TABLE_NAME), 3);
//
//		} catch (SQLException e) {
//			fail(e.toString());
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * Removes quotes from a string that might be needed if we are creating a table.
//	 * @param a
//	 * @return new string of the original without quotes
//	 */
//	public String removeQuotes(String a){
//		return a.replaceAll("'", "");
//	}
//
//	/**
//	 * Compares the two given strings and produces a failed assert if they are not equal
//	 * @param a
//	 * @param b
//	 */
//	public void assertCompareStrings(String a, String b){
//		if( !a.equals(b) ){
//			fail("Strings '" + a + "' and '" + b + "' are not equal!");
//		}
//	}
//
//	/**
//	 * Compares the two given ints and produces a failed assert if they are not equal
//	 * @param a
//	 * @param b
//	 */
//	public void assertCompareIntegers(int a, int b){
//		if( a != b ){
//			fail("Integers " + a + " and " + b + " are not equal!");
//		}
//	}
//}
