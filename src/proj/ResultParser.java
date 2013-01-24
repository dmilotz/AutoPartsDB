package proj;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
//Parses a ResultSet 

public class ResultParser {	
	
	//Parses tuple with single column
	
	public String[] parseSingleColumn(ResultSet results) throws SQLException{
		ArrayList<String> list = new ArrayList<String>();
		while (results.next()) {
	        // Get the data from the row using the column index
	        list.add(results.getString(1));
	    }
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arr[i] = list.get(i);
		}
		
		return arr;
	}
	
	//Parses tuple with multiple columns
	
	public String[] parseMultipleColumns(ResultSet results) throws SQLException{
		ResultSetMetaData rsmd = results.getMetaData();
		int count = rsmd.getColumnCount();
		String[] resultsArr = new String[count];
		//ArrayList<String>  resultsArr = new ArrayList<String>();
		while(results.next()){
			for(int i = 1; i < count; i ++){
			resultsArr[i-1] = results.getString(i);
			}
			
		}
		return resultsArr;
		
	}
	
/*
 * Returns string formatted with labels dealing with parts
 */
	
	public String getPartDetails(ResultSet results) throws SQLException
	{
		String newLine = System.getProperty("line.separator");
		String[] columns = parseMultipleColumns(results);
		String partDetails = "P_NUMBER: " + columns[0] + newLine + "CORE: " + columns[1] + newLine + 
				"INHEAD: " + columns[2] + newLine  + "OUTHEAD: " + columns[3] + newLine + "INCON: " + columns[4] + newLine + 
				"TMOUNT: " + columns[5] + newLine + "OILCOOL: " + columns[6] + newLine + "PRICE: "  + columns[7] + newLine + 
				"AMOUNT: " + columns[8];

		return partDetails;
	}
}
