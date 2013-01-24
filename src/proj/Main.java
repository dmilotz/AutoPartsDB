package proj;

import java.sql.*;
import java.util.ArrayList;


/*
 * The main part of the whole backend of the project, and also where the project starts running.
 * Basically works by assuming that the class-scope fields that are going to be used at any given
 * time are useful. So when a car Model is selected, the carMaker is assumed to be stored that
 * corresponds to that Model. And if the implementation is correct, this assumption will always be
 * true. Because the queries build on each other sequentially as more information becomes available,
 * every piece that is assumed to be in place is guaranteed to be, otherwise we would not have been
 * able to get to that particular query. This is a terrible explanation. Just remember that because it's 
 * still essentially stepwise, we can't get ahead of ourselves. So one series of steps is:
 * choose to browse by car maker -> choose model -> choose year -> choose engine -> choose part ID, receive details
 * and the other is:
 * choose to browse by part maker -> choose part id, receive details
 */
public class Main
{

	static DBConnection connectionHelper = new DBConnection();
	static Connection con = connectionHelper.getDBConnection();
	static ResultParser parser = new ResultParser();
	static String partMakerForPartMakersScreen = null;
	static String partMakerForCarMakersScreen = null;
	static String carMaker = null;
	static String carModel = null;
	static String carYear = null;
	static String engineType = null;
	static String partIDForPartMakersScreen = null;
	static String partIDForCarMakersScreen = null;
	static DBGUI gui;
	
	public static void send(String userSelection, ScreenState selectionType)
	{
		try {
			if (selectionType.equals(ScreenState.BROWSE_CAR_MAKERS))
			{
				carMakersScreenSelected(userSelection);
			}
			else if (selectionType.equals(ScreenState.BROWSE_MODELS))
			{
				modelSelected(userSelection);
			}
			else if (selectionType.equals(ScreenState.BROWSE_YEARS))
			{
				yearSelected(userSelection);
			}
			else if (selectionType.equals(ScreenState.BROWSE_ENGINE_TYPES))
			{
				engineTypeSelected(userSelection);
			}
			else if (selectionType.equals(ScreenState.BROWSE_PARTS_FOR_ENGINE))
			{
				
				partIDSelectedForCarMakers(userSelection);
			}

			else if (selectionType.equals(ScreenState.BROWSE_PART_MAKERS))
			{
				partMakersScreenOptionSelected(userSelection);
			}
	
			else if (selectionType.equals(ScreenState.BROWSE_PARTS_FROM_MAKER))
			{
				
				partIDSelectedForPartMakers(userSelection);
			}
			
		} catch (Exception e)
		{
			// There is not really anything that makes sense to do with this catch
		}
	}
	
	public static void main (String[] args) throws Exception
	{
		//Initialize DBGGUI and do stuff
		gui = new DBGUI();
		createBrowseCarMakersScreen();
		createBrowsePartMakersScreen();
	}
	
	public static void createBrowsePartMakersScreen() throws SQLException
	{
		String[] makersOfParts = new String[partMakerMap.length];
		for (int i = 0; i < partMakerMap.length ; i++)
		{
			makersOfParts[i] = partMakerMap[i][0];
		}
		gui.populateByPartsBox(ScreenState.BROWSE_PART_MAKERS, makersOfParts);
	}
	
	public static void createBrowseCarMakersScreen() throws SQLException
	{
		String[] carMakers = new String[carMakerMap.length];
		for (int i = 0; i < carMakerMap.length; i++)
		{
			carMakers[i] = carMakerMap[i][0];
		}
		gui.populateByMakerBox(ScreenState.BROWSE_CAR_MAKERS, carMakers);
	}
	
	public static void partMakersScreenOptionSelected(String selection) throws SQLException
	{
		partMakerForPartMakersScreen = getThreeLetterDesignationForPartMaker(selection);
		createBrowsePartsFromMakerScreen();
	}
	
	public static void carMakersScreenSelected(String selection) throws SQLException
	{
		carMaker = selection;
		createBrowseModelsScreen();
	}
	
	public static void createBrowsePartsFromMakerScreen() throws SQLException
	{
		String queryString = "select distinct P_NUMBER from RDIM" + partMakerForPartMakersScreen;
		ResultSet result = executeQuery(queryString);
		String[] parts = parser.parseSingleColumn(result);
		gui.populateByPartsBox(ScreenState.BROWSE_PARTS_FROM_MAKER, parts);
	}
	
	public static void createBrowseModelsScreen() throws SQLException
	{
		String queryString = "select distinct MODEL from APL" + getThreeLetterDesignationForCarMaker(carMaker);
		ResultSet result = executeQuery(queryString);
		String[] models = parser.parseSingleColumn(result);
		gui.populateByMakerBox(ScreenState.BROWSE_MODELS, models);
	}
	
	//This can be called from either way of looking at parts (by maker or car type)
	public static void partIDSelectedForCarMakers(String selection) throws SQLException
	{
		int spaceIndex = selection.indexOf(" ");
		partMakerForCarMakersScreen = selection.substring(0, spaceIndex);
		partIDForCarMakersScreen = selection.substring(spaceIndex+1);
		createPartDetailScreenForCarMakers();
	}
	
	
	public static void partIDSelectedForPartMakers(String selection) throws SQLException
	{
		partIDForPartMakersScreen = selection;
		createPartDetailScreenForPartMakers();
	}
	
	
	public static void createPartDetailScreenForCarMakers() throws SQLException
	{
		String queryString = "select * from RDIM" + partMakerForCarMakersScreen + " where " 
				+ "P_NUMBER = '" + partIDForCarMakersScreen + "'";
		ResultSet result = executeQuery(queryString);
		String engineDetails = parser.getPartDetails(result);
		gui.setByMakerDisplay(engineDetails);
	}
	
	
	public static void createPartDetailScreenForPartMakers() throws SQLException
	{
		
		String queryString = "select * from RDIM" + partMakerForPartMakersScreen + " where " 
				+ "P_NUMBER = '" + partIDForPartMakersScreen + "'";
		ResultSet result = executeQuery(queryString);
		String engineDetails = parser.getPartDetails(result);
		gui.setByPartsDisplay(engineDetails);
	}
	
	
	public static void modelSelected(String selection) throws SQLException
	{
		carModel = selection;
		createBrowseYearsScreen();
	}
	
	public static void createBrowseYearsScreen() throws SQLException
	{
		String queryString = "select distinct YEAR from APL" + getThreeLetterDesignationForCarMaker(carMaker)
				+ " where MODEL = '" + carModel + "'";
		ResultSet result = executeQuery(queryString);
		String[] years = parser.parseSingleColumn(result);
		gui.populateByMakerBox(ScreenState.BROWSE_YEARS, years);
	}
	
	public static void yearSelected(String selection) throws SQLException
	{
		carYear = selection;
		createBrowseEngineTypes();
	}
	
	public static void createBrowseEngineTypes() throws SQLException
	{
		String queryString = "select distinct ENGINE_TYPE from APL" + getThreeLetterDesignationForCarMaker(carMaker)
				+ " where MODEL = '" + carModel + "' and YEAR = '" + carYear +"'";
		ResultSet result = executeQuery(queryString);
		String[] engineTypes = parser.parseSingleColumn(result);
		gui.populateByMakerBox(ScreenState.BROWSE_ENGINE_TYPES, engineTypes);
	}
	
	public static void engineTypeSelected(String selection) throws SQLException
	{
		engineType = selection;
		createBrowsePartsForEngineScreen();
	}
	
	public static void createBrowsePartsForEngineScreen() throws SQLException
	{
		String queryString = "select ARS1, ARS2, ARS3, ARS4, MOD1, MOD2, MOD3, MOD4, BEH1, BEH2, BEH3, " 
				+ "BEH4, DAN1, DAN2, DAN3, DAN4 from RADCRX where RLINK in " 
				+ "(select RLINK from APL" + getThreeLetterDesignationForCarMaker(carMaker)
				+ " where MODEL = '" + carModel + "' and "
				+ "YEAR = '" + carYear + "' and ENGINE_TYPE = '" + engineType + "')";
		ResultSet result = executeQuery(queryString);
		String query2 = "SELECT * FROM APL" + getThreeLetterDesignationForCarMaker(carMaker) + " where Model = '" + carModel +  "' and "
				+ "year = '" + carYear + "'";
		ResultSet rs2 = executeQuery(query2);
		String[] parts = parser.parseMultipleColumns(result);
		String[] carInfo = parser.parseMultipleColumns(rs2);
		gui.printCarDetails(carInfo);
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i] != null)
			{
				if (i < 4)
					parts[i] = "ARS " + parts[i];
				else if (i >= 4 && i < 8)
					parts[i] = "MOD " + parts[i];
				else if (i >= 8 && i < 12)
					parts[i] = "BEH " + parts[i];
				else if (i >= 12 && i < 16)
					parts[i] = "DAN " + parts[i];
			}
		} 
		gui.populateByMakerBox(ScreenState.BROWSE_PARTS_FOR_ENGINE, parts);
	}
	
	
	public static void insertPart(String[] str){
		String query = "INSERT INTO RDIM" + getThreeLetterDesignationForPartMaker(str[0]) + " VALUES ('" + str[1] + "','" + str[2] +"','" 
				 +str[3] +"','" +str[4] + "','" +	str[5] + "','" + str[6] + "','"  + str[7] +"','" 
						 +str[8] +"','" +str[9] + "','" +	str[10] + "')"; 
		ResultSet rs = executeQuery(query);
		if(rs != null)
			System.out.println("Insert Succesful");
		else
			System.out.println("Insert Failed");
	}
	
	public static void insertCar(String[] str){
		String query = "INSERT INTO APL" + getThreeLetterDesignationForCarMaker(str[0]) + " VALUES ('" + str[1] + "','" + str[2] +"','" 
	 +str[3] +"','" +str[4] + "','" +	str[5] + "','" + str[6] + "', 999)";
					ResultSet rs = executeQuery(query);
					if(rs != null)
						System.out.println("Insert Succesful");
					else
						System.out.println("Insert Failed");
	}
	
	
	public static void updateCar(String[] newCar, String[] oldCar) throws SQLException{
		String query = "UPDATE APL" + getThreeLetterDesignationForCarMaker(oldCar[0]) + " SET  YEAR = '" + newCar[1] + 
				"', ENGINE_TYPE = '" + newCar[2]  + "', description =  '" + newCar[4] + "', litres = '" + newCar[5] +
				"', cubic_inches = '" + newCar[6] + "' WHERE MODEL = '" + oldCar[1] + "' and YEAR = '"
				+ oldCar[2] + "' AND ENGINE_TYPE = '" + oldCar[3] + "'" ;
		ResultSet rs = executeQuery(query);
		if(rs != null)
			System.out.println("Update Succesful");
		else
			System.out.println("Update Failed");
		String query2 = "SELECT * FROM APL" + getThreeLetterDesignationForCarMaker(oldCar[0]) + " where Model = '" + oldCar[1] +  "' and "
				+ "year = '" + newCar[1] + "'";
		ResultSet rs2 = executeQuery(query2);
		String[] cars = parser.parseMultipleColumns(rs2);
		for(int i = 0; i < cars.length -1; i ++)
		System.out.println(cars[i]);
		gui.printCarDetails(cars);
	}
	
	public static void updatePart(String[] oldPart, String[] newPart){
		String query = "UPDATE RDIM" + getThreeLetterDesignationForPartMaker(oldPart[0]) + " SET  core = '" + newPart[1] +"',  inhead = '" + newPart[2] +"', outhead= '" + newPart[3] +"'," +
						" incon = '" + newPart[4] +"', oucon = '" + newPart[5] +"', tmount = '" + newPart[6] +"'," +
								" oilcool = '" + newPart[7] +"', price = " + newPart[8] +", amount = " + newPart[9] + 
			" WHERE P_NUMBER = '" + oldPart[1] + "'";
		ResultSet rs = executeQuery(query);

	}
	
	
	public static void addPartToCar(String partMaker, String partNumber, String[] selCar){
		String query = "Update radcrx set " + partMaker +"4 = '" + partNumber + "' where rlink in (select distinct rlink from apl" + getThreeLetterDesignationForCarMaker(selCar[0]) +
				" where model = '" + selCar[1] + "' and year = '" +selCar[2] +"' and engine_type = '" + selCar[3] + "' )";
		ResultSet rs = executeQuery(query);
		if(rs != null)
			gui.setByPartsDisplay("Update Succesful");
		else
			gui.setByPartsDisplay("Update failed");
	}
	
//Delete car ///////////////////////////////////
	
	public static void deleteCar(String[] str){
		//String whereString = parseCarString(str);
		String query = "DELETE FROM APL" + getThreeLetterDesignationForCarMaker(str[0]) + " WHERE model = '" + str[1] + "' and" +
				" year = '" + str[2] + "' and engine_type = '" + str[3] + "'";
		executeQuery(query);		
	}
	
	
	
	public static void deletePart(String[] str){
		
		
		  String query = "DELETE FROM RDIM" + getThreeLetterDesignationForPartMaker(str[0]) + 
				" WHERE p_number = '" + str[1] + "'";
				
		String query1 = "UPDATE RADCRX Set " +getThreeLetterDesignationForPartMaker(str[0]) + "1 = '' WHERE "  + 
				getThreeLetterDesignationForPartMaker(str[0]) + "1 = '" + str[1] + "'"; 
		String query2 = "UPDATE RADCRX Set " +getThreeLetterDesignationForPartMaker(str[0]) + "2 = '' WHERE "  + 
				getThreeLetterDesignationForPartMaker(str[0]) + "2 = '" + str[1] + "'"; 
		String query3 = "UPDATE RADCRX Set " +getThreeLetterDesignationForPartMaker(str[0]) + "3 = '' WHERE "  + 
				getThreeLetterDesignationForPartMaker(str[0]) + "3 = '" + str[1] + "'"; 
		String query4 = "UPDATE RADCRX Set " +getThreeLetterDesignationForPartMaker(str[0]) + "4 = '' WHERE "  + 
				getThreeLetterDesignationForPartMaker(str[0]) + "4 = '" + str[1] + "'"; 
		executeQuery(query);
		executeQuery(query1);
				executeQuery(query2);
				executeQuery(query3);
				executeQuery(query4);
				
	}
	public static String parseCarString(String[] str){
		String result = "";
		for(int i = 1; i < str.length -1; i++){
			if(str[i] != ""){
				if(i == 1){
					result = " model = " + str[1];
				}
				else if(i ==2){
					result = result + " and year = " + str[2];
				}
				else if(i ==3){
					result = result + " and engine_type = " + str[3]; 
				}
			}
		}
		return result;
	}
	
	
	public static ResultSet executeQuery(String queryString)
	{
		try {
			Statement stmnt = con.createStatement();
	    	 return stmnt.executeQuery(queryString);
		 }
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static String getThreeLetterDesignationForCarMaker(String fullName) 
	{
		for (int i = 0; i < carMakerMap.length; i++)
		{
			if (carMakerMap[i][0].equals(fullName))
			{
				return carMakerMap[i][1];
			}
		}
		return null;
	}
	
	public static String getFullNameForCarMaker(String threeLetterDesignation)
	{
		for (int i = 0; i < carMakerMap.length; i++)
		{
			if (carMakerMap[i][1].equals(threeLetterDesignation))
			{
				return carMakerMap[i][0];
			}
		}
		return null;
	}
	
	public static String[][] carMakerMap = {
		{"BUICK", "BUK"},
		{"CADILLAC", "CAD"},
		{"CHEVROLET",	"CHE"},
		{"CHRYSLER", 	"CRY"},
		{"FORD LIGHT TRUCK AND VAN",	"FDT"},
		{"FORD",		"FOR"},
		{"GMC TRUCK AND VAN",	"GMC"},
		{"INTERNATIONAL TRUCK (I.H.C.)", 	"INT"},
		{"ISUZU", 	"ISU"},
		{"LINCOLN",	"LIN"},
		{"MAZDA",	"MZD"},
		{"OLDSMOBILE",	"OLD"},
		{"PORSCHE",	"POR"},
		{"RENAULT",	"REN"},
		{"SAAB",	"SAB"},
		{"SUBARU",	"SUB"},
		{"TOYOTA", 	"TOY"},
		{"UPS",	"UPS"},
		{"VOLKSWAGEN",	"VOL"}
	};
	
	static String[][] partMakerMap = {
		{"A.R.S.", "ARS"}, 
		{"BEHR", "BEH"},
		{"DANIEL", "DAN"},
		{"MODINE", "MOD"}
	};

	public static String getThreeLetterDesignationForPartMaker(String fullName) 
	{
		for (int i = 0; i < partMakerMap.length; i++)
		{
			if (partMakerMap[i][0].equals(fullName))
			{
				return partMakerMap[i][1];
			}
		}
		return null;
	}
	
	public static String getFullNameForPartMaker(String threeLetterDesignation)
	{
		for (int i = 0; i < partMakerMap.length; i++)
		{
			if (partMakerMap[i][1].equals(threeLetterDesignation))
			{
				return partMakerMap[i][0];
			}
		}
		return null;
	}

}
