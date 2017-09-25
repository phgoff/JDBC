
// SimpleJDBC.java
// Andrew Davison, August 2016, ad@fivedots.coe.psu.ac.th

/* Query Books.accdb (or Books.mdb), and print all the names
   in the Authors table. 

   The database is assumed to be in this directory.

   Uses the UCanAccess Java library
   (http://ucanaccess.sourceforge.net/site.html)

  Usage:
    > compile SimpleJDBC.java

    > run SimpleJDBC

*/

import java.sql.*;
import net.ucanaccess.jdbc.*;


public class SimpleJDBC 
{

  public static void main(String[] args)
  {
    try {
      // load the UCanAccess driver
      Class.forName( "net.ucanaccess.jdbc.UcanaccessDriver" );

      // connect to the database using the DriverManager
      String url = UcanaccessDriver.URL_PREFIX + "Books.accdb";
      // String url = UcanaccessDriver.URL_PREFIX + "Books.mdb"  +
      //                                       ";newDatabaseVersion=V2003";
      Connection conn =  DriverManager.getConnection(url);

      Statement st = conn.createStatement();
       
      ResultSet rs = st.executeQuery("SELECT lastName, firstName FROM Authors" );
      while( rs.next() )
        System.out.println( rs.getString("lastName") + ", " +
                            rs.getString("firstName") );

      st.close();
      conn.close();
    } 
    catch (ClassNotFoundException e) {
      System.out.println("Could not load UCanAccess library: " + e);
    }
    catch (SQLException e) {
      System.out.println("SQL Exception: " + e);
    }
  } // end of main()

} // end of SimpleJDBC class

