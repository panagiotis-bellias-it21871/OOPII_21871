package database;

import java.sql.*;
import java.util.*;
import traveller.guide.City;

/** The Construction of a class that handles the Oracle database.
* @since 31-05-2020
* @version 1.4
* @author it21871, it21846 */
public class OracleDBConnection {
    
    /* A connection (session) with a specific database. SQL statements are 
    executed and results are returned within the context of a connection. A Connection object's database is able to
    provide information describing its tables, its supported SQL grammar, its stored procedures, the capabilities
    of this connection, and so on. This information is obtained with the getMetaData method. */
    
    static Connection db_con_obj = null;
    
    /* An object that represents a precompiled SQL statement.
    A SQL statement is precompiled and stored in a PreparedStatement object. This object can then be used to
    efficiently execute this statement multiple times. */
    
    static PreparedStatement db_prep_obj = null;
   
    //====================================================makeJDBCConnection()==================================================
    /** The method makes connection to the Oracle Data base using jdbc driver.
     * @return true for successful connection, else false.
     */
    //==========================================================================================================================
    public static boolean makeJDBCConnection() {
		 
	try {   //We check that the DB Driver is available in our project.		
            Class.forName("oracle.jdbc.driver.OracleDriver"); 
            //This code line is to check that JDBC driver is available. Or else it will throw an exception.
            System.out.println("Congrats - Seems your Oracle JDBC Driver Registered!");
	} catch (ClassNotFoundException e) {
            System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency"
                    + "Correctly");
            return false;
	}
 
	try {
            // DriverManager: The basic service for managing a set of JDBC drivers. We connect to a DBMS.
            db_con_obj = DriverManager.getConnection("jdbc:oracle:thin:@oracle12c.hua.gr:1521:orcl","it21871",
                    "mySecretCodeForNow");// Returns a connection to the URL.
            //Attempts to establish a connection to the given database URL. The DriverManager attempts to select an
            //appropriate driver from the set of registered JDBC drivers.
            if (db_con_obj != null) { 
            	System.out.println("Connection Successful! Enjoy. Now it's time to CRUD data. ");
                return true;
            } else {
		System.out.println("Failed to make connection!");
                return false;
            }
	} catch (SQLException e) {
            System.out.println("Oracle Connection Failed!");
            return false;
	}
        
    }
    //================================================End of makeJDBCConnection()===============================================
    
    //=======================================================retrieveData()=====================================================
    /** The method retrieves the cities from database where the traveller id is equa=l to the given id.
     * @param table the name of the table which exists in database.
     * @param id the traveller id.
     * @return all the cities that exist in database.
     * @throws java.sql.SQLException
     */
    //==========================================================================================================================
    public static ArrayList<City> retrieveData(String table, int id) throws SQLException {
	
        ArrayList<City> cities = new ArrayList<>();
        
        try{
            db_prep_obj = db_con_obj.prepareStatement("SELECT * FROM " + table + " WHERE Traveller_id = " + id);
            ResultSet rs = db_prep_obj.executeQuery();

            while (rs.next()){

                String cityName = rs.getString("Name");
                String cityCountry = rs.getString("Country");
                String cityData = rs.getString("Data");
                String weather = rs.getString("Weather");
                double lat = rs.getDouble("Lat");
                double lon = rs.getDouble("Lon");

                City city = new City(cityName, cityCountry, cityData, weather, lat, lon);
                cities.add(city);
                   
                System.out.println("Name: "+ cityName + " Country: " + cityCountry + " Data: " + cityData 
                        + " Weather: " + weather + " Lat: " + lat + " Lon: " + lon);

            }
            
            db_con_obj.close();
            
        } catch (SQLException e) {
            System.out.println(e);
	}
        
        return cities;
    }
    //===================================================End of retrieveData()==================================================
    
    //=========================================================addData()========================================================
    /** The method retrieves the cities from database where the traveller id is equa=l to the given id.
     * @param cities the cities that will be added to the database.
     */
    //==========================================================================================================================
    public static void addData(ArrayList<City> cities) {
	
        try {
            
            Iterator<City> city_iterator = cities.iterator();
            while(city_iterator.hasNext()) {
                
                City city = city_iterator.next();
                String insertQueryStatement = "INSERT INTO cities VALUES (?,?,?,?,?,?,?)";

                db_prep_obj = db_con_obj.prepareStatement(insertQueryStatement);
                db_prep_obj.setString(1, city.getCityName());
                db_prep_obj.setString(2, city.getCityCountry());
                db_prep_obj.setString(3, city.getCityData());
                db_prep_obj.setString(4, city.getWeather());
                db_prep_obj.setDouble(5, city.getLat());
                db_prep_obj.setDouble(6, city.getLon());

                // execute insert SQL statement Executes the SQL statement in this PreparedStatement object, which
                // must be an SQL Data Manipulation Language (DML) statement
                int numRowChanged = db_prep_obj.executeUpdate(); //either (1) the row count for SQL Data Manipulation
                // Language (DML) statements or (2) 0 for SQL statements that return nothing
                System.out.println("Rows " + numRowChanged + " changed.");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    //=====================================================End of addData()====================================================
    
}//======================================================End of Class OracleDBConnection ======================================================
