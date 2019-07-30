package is.L42.connected.withDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseResource {
	
	// All open connections
	private static Map<String, DatabaseResource> connections = new HashMap<String,DatabaseResource>();

	
	// Connection to the database if there is one
	private Connection con;

	// Information that connects to the server
	private String url = "";
	private String username = "";
	private String password = "";


	/**
	 * Create a new instance of SQL Data and attempts to connect to a database with the given information
	 * @param server IP address
	 * @param database Database to connect to
	 * @param name Username of user connecting
	 * @param pass Password of their account
	 * @return True if connected
	 */
	private DatabaseResource(String server, String database, String name, String pass) throws SQLException{
		if( server.endsWith("/") && database.startsWith("/") ){
			server += "/";
		}
		connect(server, database, name, pass);
	}

	private DatabaseResource(String url) throws SQLException {
		this.url = url;
		connect();
	}
	
	private DatabaseResource(String url, String username, String password) throws SQLException {
		this.url = url;
		this.username = username;
		this.password = password;
		connect();
	}

	/**
	 * Assigns new connection info for our database to connect to
	 * Starts a new connection
	 * @param server Public domain on where we are connecting to
	 * @param database Database of which we will pull information from
	 * @param name Username of person connecting
	 * @param pass Password of account
	 * @return If we are connected or not
	 */
	public boolean connect(String server, String database, String name, String pass) throws SQLException{

		// Assign new server information
		this.url = server + database;
		this.username = name;
		this.password = pass;

		// Connect to database
		connect();

		// Return if we are connected or not.
		return isConnected();
	}

	/**
	 * Attempts connecting to the server
	 * Will throw a SQLException if there is an issue connecting to the server
	 * @throws SQLException
	 */
	private void connect() throws SQLException{

		// Register new driver
		DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		
		// Open a connection
		Connection conn1;
		if( !username.isEmpty() ){
			
			// Login with account details
			conn1 =  DriverManager.getConnection(url,username,password);
		}
		else{
			// Login without account details
			conn1 =  DriverManager.getConnection(url);
		}
		
		
		// Attempt logging in
        if (conn1 != null) {
            System.out.println("Connected to database #1");
        }
        else{
        	System.out.println("Failed to connect to database #1");
        }

        // Use this connection throughout the rest of the code
        con = conn1;
	}

	/**
	 * Check if the Database is currently connected
	 * @return True if we are connected. False if not
	 */
	public boolean isConnected() {

		try {
			// Check for valid connection
			return con != null && !con.isClosed();
		} catch (SQLException e) {}

		// Not connected
		return false;
	}

	/**
	 * Close the connection to the database
	 */
	public void close(){
		try {

			// Remove this connection from the list
			connections.remove(getURL());

			// Make sure we are connected
			if( con != null && !con.isClosed() ){
				
				// Stop the connection
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the given table is empty or not
	 * @param table Name of table to check
	 * @return True if rowcount is 0
	 */
	public boolean tableIsEmpty(String table) {
		return getTableRowCount(table) == 0;
	}

	/**
	 * Creates a new table in the database
	 * @param query Query to send to the server to create a table.
	 * @return True if created. False if not
	 */
	public boolean createTable(String query) {
		System.out.print("Create Table using: " + query);
		System.out.println();

		try {
			Statement st = con.createStatement();
			st.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Did not create table
		return false;

	}

	/**
	 * Deletes the table and all its contents from the server
	 * @param tableName Name of the table to delete
	 * @return True if dropped. False if not.
	 */
	public boolean dropTable(String tableName) {

		try {
			Statement st = con.createStatement();
			boolean result = st.execute("DROP TABLE " + tableName);
			return result;
		} catch (SQLException e) {
			if( e.getMessage().startsWith("'DROP TABLE' cannot be performed on '"+tableName.toUpperCase()+"' because it does not exist.")){
				return true;
			}
			else{
				e.printStackTrace();
			}
		}

		// Could not drop
		return false;
	}

	/**
	 * Insert a new row into the database
	 * @param insertQuert
	 * @return Number of rows effected by the insert
	 */
	public int insert(String insertQuert) {
		
		try {
			Statement st = con.createStatement();
			int result = st.executeUpdate(insertQuert);
			return result;
		} catch (SQLException e) {
			if( e.toString().contains("cannot accept a NULL value") ){
				String missingParameter = e.toString().substring(e.toString().indexOf("'")+1, e.toString().lastIndexOf("'"));
				System.err.println("CAUGHT ERROR. We are missing a parameter in the insert (" + missingParameter + ")");
			}
			e.printStackTrace();
		}

		// Failed to insert query
		return 0;
	}

	/**
	 * Selects data from the database and sends back a ResultSet which we can iterate over.
	 * @param query Select query to select data with
	 * @return ResultSet that holds a table of table from the selection
	 */
	public ResultSet query(String query) {
		
		try {
			Statement st = con.createStatement();
			ResultSet result = st.executeQuery(query);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Failed to execute
		return null;
	}
	
	/**
	 * Selection query from the database and then converts the results into a string 
	 * @param query Select query to select data with
	 * @return String representation with each row broken down with new lines
	 */
	public String queryToString(String query) {
		
		String output = "";
		try {
			Statement st = con.createStatement();
			ResultSet result = st.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			// Go through each row
			while(result.next()){
				String row = "";
				
				// Go through each column and save it into the row
				for(int i = 1; i <= columnCount; i++){
					String columnValue = result.getString(i);
					row += columnValue;
					if( i != columnCount ){
						row += " ";
					}
				}
				output += row += "\n";
			}
			
			// Remove tailing endline
			if( output.endsWith("\n") ){
				output = output.substring(0,output.length()-1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Failed to execute
		return output;
	}

	/**
	 * Checks how many rows are in the given table and returns an int
	 * @param tableName Name of table to get the rowcount from
	 * @return 0 if Empty otherwise 1 or greater.
	 */
	public int getTableRowCount(String tableName) {
		
		// Select everything and return the size of the table
		String query = "SELECT COUNT(*) as rowcount FROM " + tableName;
		try{
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet set = ps.executeQuery();
			if( set.next() ){
				int rowCount = set.getInt("rowcount");
				return rowCount;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return 0;
	}

	public int delete(String query) {
		try{
			PreparedStatement ps = con.prepareStatement(query);
			int set = ps.executeUpdate();
			return set;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * Checks if the given tableName is in the database or not
	 * Thank you Stackoverflow!
	 * @param tableName name of the table to check for
	 * @return true if the table exists in the database or not
	 */
	public boolean tableExists(String tableName) {
		
		if( !isConnected() ){
			throw new RuntimeException("Not connected");
		}
		
		try {
			DatabaseMetaData dbm = con.getMetaData();
			ResultSet tables = dbm.getTables(null, null, tableName, null);
			return tables.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static boolean databaseExists(String url) {
		
		try {
			DatabaseResource data = new DatabaseResource(url + ";create=false");
			return data.databaseExists();
		} catch (SQLException e) {
			String error = e.toString();
			if( error.startsWith("java.sql.SQLException: Database" ) && error.endsWith(" not found.")) {
				String dbName = error.substring(error.indexOf("'")+1, error.lastIndexOf("'"));
				System.out.println("Database " + dbName + " does not exist,");
				return false;
			}
			
			System.out.println("'"+error+"'");
			
			// Different error
			e.printStackTrace();
		}
		
		// Could not find out
		return false;
	}

	/**
	 * Checks if the given database exists
	 * @param url
	 * @param databaseName
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean databaseExists(String url, String username, String password) {
		try {
			DatabaseResource data = new DatabaseResource(url,username,password);
			data.close();
			return true;
		} catch (SQLException e) {
			String error = e.toString();
			if( error.startsWith("java.sql.SQLException: Database" ) && error.endsWith(" not found.")) {
				String dbName = error.substring(error.indexOf("'")+1, error.lastIndexOf("'"));
				System.out.println("FALSE: Database " + dbName + " does not exist,");
				return false;
			}
			
			//System.out.println("'"+error+"'");
			
			// Different error
			e.printStackTrace();
		}
		
		// Could not find out
		return false;
	}
	
	/**
	 * Checks if the current connection to the server contains the given database.
	 * Can not be called externally as this needs to be given server information and no database
	 * Gets called from the static method 'databaseExists'
	 * @param database Database to check for 
	 * @return True if the database exists or not
	 */
	private boolean databaseExists(){
		try {
			
			
			// Connection connection = <your java.sql.Connection>
			ResultSet resultSet = con.getMetaData().getCatalogs();

			//iterate each catalog in the ResultSet
			while (resultSet.next()) {
			  // Get the database name, which is at position 1
			  String databaseName = resultSet.getString(1);
			  System.out.println("Name " + databaseName);
			}
			resultSet.close();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	

	/**
	 * Gets the URL that the connection is connected to
	 * @return
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * Shuts the connection to derby down.
	 * @return True if shut down, false if not.
	 */
	public boolean shutdownConnection(){
		System.out.println("Shutting down");
		try {
			DriverManager.getConnection(
				    "jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			String error = e.toString();
			if( error.endsWith("Derby system shutdown.")){
				
				// This error is expected when shutting down a database
				return true;
			}
			else{
				e.printStackTrace();
			}
		}
		
		// Was not shut down
		return false;
	}
	
	public static DatabaseResource getDataResource(String stringRepresentation){
		DatabaseResource data = connections.get(stringRepresentation);
		return data;
	}
	
	/**
	 * Creates a new connection to the server. 
	 * If we already have a connection with the given URL, then that connection will be returned.
	 * @param server Derby server to connect to
	 * @param database The database we want to connect to
	 * @param name Username of the account to connect with
	 * @param pass Password of the account to connect with
	 * @return DatabaseResource if the connection is valid or null if not.
	 */
	public static DatabaseResource connectToServer(String server, String database, String name, String pass) throws SQLException{
		

		String url;
		if( !server.endsWith("/") && database.startsWith("/") ){
			url = server + "/" + database;
		}
		else{
			url = server+database;
		}
		
		// Return the current connection if we already have one
		DatabaseResource alreadyAssigned = connections.get(url);
		if( alreadyAssigned != null ){
			return alreadyAssigned;
		}
		
		
		// Create a new connection
		DatabaseResource resource = new DatabaseResource(server,database,name,pass);
		
		// Save the database if we connected successfully
		connections.put(resource.getURL(), resource);
		
		// Return the DataResource
		return null;
	}
	
	/**
	 * Creates a new connection to the server. 
	 * If we already have a connection with the given URL, then that connection will be returned.
	 * @param server Derby server to connect to
	 * @param database The database we want to connect to
	 * @param name Username of the account to connect with
	 * @param pass Password of the account to connect with
	 * @return DatabaseResource if the connection is valid or null if not.
	 */
	public static DatabaseResource connectToServer(String url, String name, String pass) throws SQLException{
		
		// Return the current connection if we already have one
		DatabaseResource alreadyAssigned = connections.get(url);
		if( alreadyAssigned != null ){
			return alreadyAssigned;
		}
		
		
		// Create a new connection
		try {
			// Attempt to connect to the database
			DatabaseResource resource = new DatabaseResource(url,name,pass);
			
			// Save the database if we connected successfully
			connections.put(resource.getURL(), resource);
			
			// Return the DataResource
			return resource;
		} catch (SQLException e) {/* Errors displayed in the methods */e.printStackTrace();}
		
		// Did not connect successfully
		return null;
	}
	
	/**
	 * Creates a new connection to the server. 
	 * If we already have a connection with the given URL, then that connection will be returned.
	 * @param server Derby server to connect to
	 * @param database The database we want to connect to
	 * @param name Username of the account to connect with
	 * @param pass Password of the account to connect with
	 * @return DatabaseResource if the connection is valid or null if not.
	 */
	public static DatabaseResource connectToServer(String url) throws SQLException{
		
		// Return the current connection if we already have one
		DatabaseResource alreadyAssigned = connections.get(url);
		if( alreadyAssigned != null ){
			return alreadyAssigned;
		}
		
		
		// Attempt to connect to the database
		DatabaseResource resource = new DatabaseResource(url);
		
		// Save the database if we connected successfully
		connections.put(resource.getURL(), resource);
		
		// Return the DataResource
		return resource;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DatabaseResource))
			return false;
		DatabaseResource other = (DatabaseResource) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Deprecated
	public static void main(String[] args){

		// Create DB
		System.out.println("Running Database...");
		DatabaseResource data = null;
		try{
			data = new DatabaseResource("mathparser.com", "stardrop_test", "idonotexist", "tonkatoy2014");
		}catch(SQLException e){
			System.out.println(e);
		}

		// Check we are connected
		System.out.println("Database is Connected: " + data.isConnected());


		System.out.println("Closing... ");
		data.close();
		System.out.println("Database  is Connected: " + data.isConnected());
	}
}



















