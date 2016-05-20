package is.L42.connected.withDatabase;

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
	@ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public Object Mconnect£xn1(Object url){
		String s=ensureExtractStringU(url);
	  
		try {
			// Attempt connecting
			DatabaseResource data = DatabaseResource.connectToServer(s);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Couldn't connect
		return null;
    }

	// Connect to database with username and password
	@ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
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
	@ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public Object Mselect£xn1(Object query){
	  String s=ensureExtractStringU(query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){
		  throw new RuntimeException("Server with url " + s + " not registered/connected.");
	  }
	  
	  
	  ResultSet result = data.query(s);
	  return result;
    }
	
	// insert a new row into the database
	@ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object Minsert£xn1£xn2(Object o, Object query){
	  String s=ensureExtractStringU(o);
	  String q=ensureExtractStringU(query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){
		  throw new RuntimeException("Server with url " + s + " not registered/connected.");
	  }
	  
	  int rowseffected = data.insert(q);
	  return rowseffected;
    }
	
	// Add a new table to the database
	@ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object McreateNewTable£xn1£xn2(Object o, Object query){
	  String s = ensureExtractStringU(o);
	  String q = ensureExtractStringU(query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){
		  throw new RuntimeException("Server with url " + s + " not registered/connected.");
	  }
	  
	  boolean created = data.createTable(q);
	  return created;
    }
	
	// Delete rows from the database
	@ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object Mdelete£xn1£xn2(Object o, Object query){
	  String s = ensureExtractStringU(o);
	  String q = ensureExtractStringU(query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){
		  throw new RuntimeException("Server with url " + s + " not registered/connected.");
	  }
	  
	  int rowseffected = data.delete(q);
	  return rowseffected;
    }
	
	// Connect to database only using a url
	@ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public Object Mclose£xn1(Object query){
	  String s=ensureExtractStringU(query);
	  DatabaseResource data = DatabaseResource.getDataResource(s);
	  if( data == null ){
		  throw new RuntimeException("Server with url " + s + " not registered/connected.");
	  }
	  
	  data.close();
	  return Resources.Void.instance;
    }
	
	//==
	@ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
	public  Resources.Void MifDatabaseResourceEqualDo£xn1£xn2(Object cb1,Object cb2){
	  DatabaseResource i1=ensureExtract(DatabaseResource.class,cb1);
	  DatabaseResource i2=ensureExtract(DatabaseResource.class,cb2);
	  if(!i1.equals(i2)){return Resources.Void.instance;}
	  throw Resources.notAct;
	}
}

















