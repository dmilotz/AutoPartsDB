package proj;

import java.sql.*;

/**
 * This class is in charge to connect to the desired database and returns
 * the Connection object.
 * @author Ahmad Yazdankhah
 * @version 1.0  Fall 2011   CS157A
 * Prof.: Dr. T. Y. Lin
 */
public class DBConnection
{
// ........................ D A T A   F I E L D S ............................//
// ................. G L O B A L   P R I V A T E   V A R S ...................//

    /** hold a pointer to the database connection. */
    private Connection con;

// ............. G L O B A L   P R I V A T E   C O N S T A N T S .............//

    private final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final String DB_URL    = "jdbc:oracle:thin:@localhost:1521:classdatabas";
    private final String USER_ID   = "scott";
    private final String PASSWORD  = "tiger";


// ........................ C O N S T R U C T O R S ..........................//

    public DBConnection()
    {
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER_ID, PASSWORD);

            System.out.println("Connected to DB!");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }

    } //constructor

//.............................. G E T T E R S ...............................//

    /** get the database connection object. */
    public Connection getDBConnection()
    {  return this.con; } //method


// ...................... P R I V A T E   M E T H O D S ......................//
// ...................... P U B L I C   M E T H O D S ........................//

    /** disconnect from the database. */
    public void disconnectFromDB() {
        try {
            con.close();
            System.out.println("Disconnected from DB!");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    } //method

} // class

