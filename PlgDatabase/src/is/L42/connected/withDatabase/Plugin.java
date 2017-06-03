package is.L42.connected.withDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;

import static auxiliaryGrammar.EncodingHelper.*;

public class Plugin implements PluginType {

	// Connect to database only using a url
	@ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public Resources.Void Mconnect£xurl(Object _url){
		String s=ensureExtractStringU(_url);

		try {
			// Attempt connecting
			DatabaseResource data = DatabaseResource.connectToServer(s);
			System.out.println("Connected to datase and returning " + data.getURL());
			//return data.getURL();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Not Connected");

		// Couldn't connect
		return Resources.Void.instance;
    }

	// Connect to database with username and password
	@Deprecated /**Not yet implemented**/
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object Mconnect£xn1£xn2£xn3£xn4(Object server, Object database, Object username, Object password){
		String s=ensureExtractStringU(server);
		String t=ensureExtractStringU(database);
		String u=ensureExtractStringU(username);
		String p=ensureExtractStringU(password);


		try {
			// Attempt connecting
			DatabaseResource data = DatabaseResource.connectToServer(s, t, u, p);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Couldn't connect
		return null;
    }

	// Select rows from the database
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object Mquery£xurl£xquery(Object _url, Object _query){
	  String s=ensureExtractStringU(_query);
	  String u=ensureExtractStringU(_url);
	  DatabaseResource data = DatabaseResource.getDataResource(u);
	  assert data != null;

	  // Get the ResultSet from the query
	  // Then turn wrap it with a cursor so we can use it in the
	  int cursorID = Cursor.getCursor(data.query(s)).id;
	  return cursorID;
    }

	// Select rows from the database and display them as rows.
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MqueryToString£xurl£xquery(Object _url, Object _query){
	  String url=ensureExtractStringU(_url);
	  String query=ensureExtractStringU(_query);
	  DatabaseResource data = DatabaseResource.getDataResource(url);
	  assert data != null;


	  String result = data.queryToString(query);
	  return result;
    }

	// insert a new row into the database
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object Minsert£xurl£xquery(Object _url, Object _query){
	  String s=ensureExtractStringU(_url);
	  String q=ensureExtractStringU(_query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){ return 0; }

	  int rowseffected = data.insert(q);
	  return rowseffected;
    }

	// Add a new table to the database
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object McreateNewTable£xurl£xquery(Object _url, Object _query){
	  String s = ensureExtractStringU(_url);
	  String q = ensureExtractStringU(_query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){ return 0; }

	  boolean created = data.createTable(q);
	  return created ? 1 : 0;
    }

	// Delete rows from the database
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object Mdelete£xurl£xquery(Object _url, Object _query){
	  String s = ensureExtractStringU(_url);
	  String q = ensureExtractStringU(_query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  assert data != null;
	  
	  int rowseffected = data.delete(q);
	  return rowseffected;
    }

	// Connect to database only using a url
	@ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public Resources.Void Mclose£xurl(Object _url){
		String s = ensureExtractStringU(_url);
		DatabaseResource data = DatabaseResource.getDataResource(s);
		assert data != null;
		data.close();
		return Resources.Void.instance;
    }
	
	// Shuts down the server so we are no longer connected
	@ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public Resources.Void Mshutdown£xurl(Object _url){
		String s = ensureExtractStringU(_url);

		DatabaseResource data = DatabaseResource.getDataResource(s);
		assert data != null;

		data.shutdownConnection();
		return Resources.Void.instance;
    }

	// Checks if we are connected to the given url
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MisConnected£xurl(Object _url){
		String s = ensureExtractStringU(_url);
		DatabaseResource data = DatabaseResource.getDataResource(s);
		assert data != null;
		return data.isConnected() ? 1 : 0;
    }

	//==
	@Deprecated /**Not yet implemented**/
	@ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
	public  Resources.Void MifDatabaseResourceEqualDo£xo1£xo2(Object _o1,Object _o2){
	  DatabaseResource i1=ensureExtract(DatabaseResource.class,_o1);
	  DatabaseResource i2=ensureExtract(DatabaseResource.class,_o2);
	  if(!i1.equals(i2)){return Resources.Void.instance;}
	  throw Resources.notAct;
	}

	//
	// CURSOR METHODS
	//

	// Connect to database only using a url
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
    public Object McursorNext£xid(Object _id){
		int id=ensureExtractInt32(_id);

		Cursor c = Cursor.getCursor(id);
		assert c != null; // This cursor does exist!

		return c.next() ? 1 : 0;
    }

	// Connect to database only using a url
	@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object McursorGetString£xid£xcolumnName(Object _id, Object _columnName){
		int id=ensureExtractInt32(_id);
		String columnName=ensureExtractStringU(_columnName);

		Cursor c = Cursor.getCursor(id);
		assert c != null; // This cursor does exist!

		return c.getString(columnName);
    }

	// Connect to database only using a url
	@ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public Resources.Void McursorClose£xid(Object _id){
		int id=ensureExtractInt32(_id);

		Cursor c = Cursor.getCursor(id);
		assert c != null; // This cursor does exist!

		c.close();
		return Resources.Void.instance;
    }
}

















