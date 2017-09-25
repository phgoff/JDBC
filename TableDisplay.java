
// TableDisplay.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, August 2016

/* Show all the Authors details from Books.accdb inside a 
   GUI table.
*/

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.*;
import net.ucanaccess.jdbc.*;




public class TableDisplay extends JFrame 
{
  private Connection conn;
    
    
  public TableDisplay() 
  {   
    String url = UcanaccessDriver.URL_PREFIX + "Books.accdb";

    // Load the driver to allow conn to the database
    try {
      Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
      conn = DriverManager.getConnection(url);
      executeQuery(conn, "SELECT FirstName from Authors where AuthorID IN"+"(SELECT AuthorID from AuthorISBN where ISBN IN"
                          +"(SELECT ISBN from Titles where Title like 'C*'))");
    
    } 
    catch (ClassNotFoundException e) {
      System.out.println("Could not load UCanAccess library: " + e);
    }
    catch (SQLException e) {
      System.out.println("SQL Exception: " + e);
    }

    // GUI stuff
    addWindowListener(new WindowAdapter() 
    { public void windowClosing(WindowEvent e) 
      {  try {
           conn.close();
         }
         catch (SQLException ex) {}
         System.exit(0);
      }
    });
    setSize(450, 150);
    setVisible(true);
  }  // end of TableDisplay()
   


  private void executeQuery(Connection conn, String query) throws SQLException
  {
    System.out.println("Executing query: \"" + query + "\"...");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);
    displayResultSet(rs, query);
    st.close();
  }  // end of executeQuery()

   

   private void displayResultSet(ResultSet rs, String query) throws SQLException
   // display the result set in a GUI table
   {
    // if there are no records, display a message
    if (!rs.next()) {
      JOptionPane.showMessageDialog(this, "ResultSet contains no records");
      return;
    }

    setTitle(query);

    ResultSetMetaData rsmd = rs.getMetaData();
    int numCols = rsmd.getColumnCount();

    // get column headers
    String[] columnHeads = new String[numCols];
    for (int i = 1; i <= numCols; i++) 
      columnHeads[i-1] = rsmd.getColumnName(i);

    // create table with column heads
    DefaultTableModel tableModel = new DefaultTableModel(columnHeads, 0);
    JTable table = new JTable(tableModel);

    // fill table with ResultSet contents, one row at a time
    do {
      tableModel.addRow( getRow(rs, rsmd) ); 
    } while (rs.next());

    // make scrollable table
    JScrollPane scroller = new JScrollPane(table);
    getContentPane().add(scroller, BorderLayout.CENTER);
    validate();
  }  // end of displayResultSet()
  


   private Object[] getRow(ResultSet rs, ResultSetMetaData rsmd) 
                                                      throws SQLException
   // return current row of resultset as an array
   { 
     int numCols = rsmd.getColumnCount();
     Object[] row = new Object[numCols];

     for (int i = 1; i <= numCols; i++) {
       switch( rsmd.getColumnType(i)) {
         case Types.VARCHAR:
           row[i-1] = rs.getString(i);
           break;
         case Types.INTEGER:
           row[i-1] = rs.getLong(i);
           break;
         default: 
           System.out.println("Type was: " + rsmd.getColumnTypeName(i));
         }
      }
      return row;
   }  // end of getRow()



// ------------------------------------------------------

   public static void main( String args[] ) 
   {  new TableDisplay();  }  
   
}  // end of TableDisplay class

