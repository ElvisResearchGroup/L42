package is.L42.connected.withDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Cursor {
	private static Map<Integer,Cursor> cursors = new HashMap<Integer,Cursor>();
	private static int CURSORS = 0;

	// Id representation of the Cursor. Will never change
	public final int id;

	private final ResultSet resultSet;

	private Cursor(ResultSet resultSet){
		this.id = ++CURSORS;
		this.resultSet = resultSet;
	}

	public ResultSet getResultSet(){
		return resultSet;
	}

	/**
	 * Creates a new Cursor for L42 using the ResultSet
	 * @param result
	 * @return new Cusor
	 */
	public static Cursor getCursor(ResultSet result){
		Cursor c = new Cursor(result);
		cursors.put(c.id,c);
		return c;
	}

	/**
	 * Creates a new Cursor for L42 using the ResultSet
	 * @param id ID of the Cursor we should already have
	 * @return Already made Cursor
	 */
	public static Cursor getCursor(int id){
		return cursors.get(id);
	}

	/**
	 * Checks if the ResultSet has another row for us to iterate over.
	 * If it does then this method returns true and the ResultSet will then look at the next row
	 * @param id ID of the Cursor
	 * @return True if we have another row to look at
	 */
	public boolean next(){

		try {
			ResultSet ts = getResultSet();
			return ts.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cursor can not check next in resultSet!");
		}
	}

	/**
	 * Gets the value of the column that's in our current row
	 * @param columnName Column Name that we want to get the value from
	 * @return
	 */
	public String getString(String columnName){

		try {
			ResultSet ts = getResultSet();
			return ts.getString(columnName);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cursor can not get String from resultSet!");
		}

	}

	/**
	 * Gets the value of the column that's in our current row
	 * @param columnName Column Name that we want to get the value from
	 * @return
	 */
	public void close(){

		try {
			// Close the resultset
			ResultSet ts = getResultSet();
			ts.close();

			// Delete it from our list of cursors
			cursors.remove(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
