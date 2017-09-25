
// MakeDB.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, September 2015

/* Create a new database consisting of 4 tables, and execute
   a query. Save the database in the old-style Access 2003
   file. 

   Based on Example.java by Marco Amadei.
   Uses the UCanAccess Java library
   (http://ucanaccess.sourceforge.net/site.html)

  Usage:
    run MakeDB test1.mdb
       // test1.mdb must not already exist
*/


import java.io.*;
import java.sql.*;

import net.ucanaccess.jdbc.*;



public class MakeDB
{


  public MakeDB(String dbName)
  {
    try {
      Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

      String url = UcanaccessDriver.URL_PREFIX + dbName +
                                             ";newDatabaseVersion=V2003";
      Connection conn =  DriverManager.getConnection(url, "sa", "");

      createTables(conn);
      insertData(conn);
      executeQuery(conn, "SELECT * from business");
      conn.close();
    }
    catch (ClassNotFoundException e) {
      System.out.println("Could not load UCanAccess library: " + e);
    }
    catch (SQLException e) {
      System.out.println("SQL Exception: " + e);
    }
  }  // end of MakeDB()





  private void createTables(Connection conn) throws SQLException
  {
    createTable(conn, "business", 
       "id COUNTER PRIMARY KEY, company text(400), number numeric(12,3), date0 datetime");

    createTable(conn, "people", "id COUNTER PRIMARY KEY, name text(400)");

    createTable(conn, "countries", "id LONG PRIMARY KEY, country text(400)");

    createTable(conn, "cities", "id LONG PRIMARY KEY, city text(400)");
  }  // end of createTables()



  private void createTable(Connection conn, String tableName,
                                       String schema) throws SQLException
  {
    System.out.println("Creating table \"" + tableName + "\"");
    Statement st = conn.createStatement();
    st.execute("CREATE TABLE " + tableName + " (" + schema + ")");
    st.close();
  }  // end of createTable()




  private void insertData(Connection conn) throws SQLException
  {
    System.out.println("Inserting data into tables...");

    Statement st = conn.createStatement();
    st.execute(
        "INSERT INTO business (company, number, date0)  VALUES( 'Apple', -1110.55446, #9/22/2015 10:42:58 PM#)");
    st.execute(
        "INSERT INTO business (company, number, date0)  VALUES( \"MS\", -113.55446, #9/22/2015 10:42:58 PM#)");

    st.execute("INSERT INTO people (name)  VALUES('John')");   // no need for counter
    st.execute("INSERT INTO people (name)  VALUES('Supatra')");
    st.execute("INSERT INTO people (name)  VALUES('Andrew')");
    st.execute("INSERT INTO people (name)  VALUES('Paul')");
    st.execute("INSERT INTO people (name)  VALUES('Sheila')");
    st.execute("INSERT INTO people (name)  VALUES('Pat')");
    st.execute("INSERT INTO people (name)  VALUES('Richard')");
    st.execute("INSERT INTO people (name)  VALUES('Mary')");
    st.execute("INSERT INTO people (name)  VALUES('Stan')");

    st.execute("INSERT INTO countries (ID, country)  VALUES(1, 'France')");
    st.execute("INSERT INTO countries (ID, country)  VALUES(2, 'England')");

    st.execute("INSERT INTO cities (ID, city)  VALUES(2, 'Paris')");
    st.execute("INSERT INTO cities (ID, city)  VALUES(3, 'London')");

    st.close();
  }  // end of insertData()



  private void executeQuery(Connection conn, String query) throws SQLException
  {
    System.out.println("Executing query: \"" + query + "\"...");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);
    showResultSet(rs, query);
    st.close();
  }  // end of executeQuery()




  private void showResultSet(ResultSet rs, String query) throws SQLException
  {
    System.out.println("\n-------------------------------------------------");
    System.out.println("ResultSet for \"" + query + "\"\n");
    while (rs.next()) {
      System.out.print("| ");
      int numCols = rs.getMetaData().getColumnCount();
      for (int i = 1; i <= numCols; i++)
        System.out.print(rs.getObject(i) + " | ");
      System.out.println("\n");
    }
  }  // end of showResultSet()


  // -------------------------------------------------


  public static void main(String[] args)
  {
    if (args.length == 0)
      System.out.println("You must specify a MDB database name");
    else
      new MakeDB(args[0]);
  }

}  // end of MakeDB class
