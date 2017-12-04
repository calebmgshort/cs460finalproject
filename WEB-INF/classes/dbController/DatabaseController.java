package dbController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.util.*;

/**
 * Servlet implementation class for Servlet: DatabaseController
 *
 */
public class DatabaseController {
  static final long serialVersionUID = 1L;
  /**
   * A handle to the connection to the DBMS.
   */
  protected Connection connection_;
  /**
   * A handle to the statement.
   */
  protected Statement statement_;
  /**
   * The connect string to specify the location of DBMS
   */
  protected String connect_string_ = null;
  /**
   * The password that is used to connect to the DBMS.
   */
  protected String password = "Cmg21514007!";
  /**
   * The username that is used to connect to the DBMS.
   */
  protected String username = "calebmgshort";


  public DatabaseController() {
    // your cs login name
    username = "calebmgshort";
    // your Oracle password, NNNN is the last four digits of your CSID
    password = "Cmg21514007!";
    connect_string_ = "jdbc:oracle:thin:@aloe.cs.arizona.edu:5243:oracle";
  }


  /**
   * Closes the DBMS connection that was opened by the open call.
   */
  public void Close() {
    try {
      statement_.close();
      connection_.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    connection_ = null;
  }


  /**
   * Commits all update operations made to the dbms.
   * If auto-commit is on, which is by default, it is not necessary to call
   * this method.
   */
  public void Commit() {
    try {
      if (connection_ != null && !connection_.isClosed())
        connection_.commit();
    } catch (SQLException e) {
      System.err.println("Commit failed");
      e.printStackTrace();
    }
  }

  public void Open() {
	    try {
	        Class.forName("oracle.jdbc.OracleDriver");
	        connection_ = DriverManager.getConnection(connect_string_, username, password);
          connection_.setAutoCommit(false);
	        statement_ = connection_.createStatement();
	        return;
	    } catch (SQLException sqlex) {
	        sqlex.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        System.exit(1); //programemer/dbsm error
	    } catch (Exception ex) {
	       ex.printStackTrace();
	       System.exit(2);
	    }
  }


  public String insertModel(int modelNum, String deptName, String modelName, float cost, int[] luxuryParts) {

      // Make sure that the provided number of luxury parts is between 3 and 10
      if(luxuryParts.length < 3 || luxuryParts.length > 10){
          return "The number of luxury parts for a given ship must be between 3 and 10"
      }
      //String queryStatement =

      try{
          String updateStatement = "INSERT INTO hdcovello.DepartmentModel "
          + "values (modelNum,modelname,modelcost,deptname) "
          + "(" + modelName + "," + cost + "," + deptName + ")";
          statement_.executeUpdate(updateStatement);
          for(int i = 0; i < luxuryParts.length; i++){
              updateStatement = "INSERT INTO hdcovello.LuxuryPartOfModel "
              + "values (modelNum,partNum) "
              + "(" + modelNum + "," + modelName + ")";
              statement_.executeUpdate(updateStatement);
          }
      }  catch (SQLException e){
          return e.toString();
          // TODO: handle if there is an error
      }
      Commit();
      return "success";
  }


  public Vector<String> FindAllProducts() {
    String sql_query = "SELECT * FROM yawenchen.products order by barcode";
    try {
      ResultSet rs  = statement_.executeQuery(sql_query);
      Vector<String> result_employees = new Vector<String>();
      while (rs.next()) {
         String temp_record = rs.getString("BARCODE") + "##" + rs.getString("NAME") +
             "##" + rs.getDouble("PRICE") + "##" + rs.getInt("QUANTITY");
        result_employees.add(temp_record);
      }
      return result_employees;
    } catch (SQLException sqlex) {
      sqlex.printStackTrace();
    }
    return null;
  }
}
