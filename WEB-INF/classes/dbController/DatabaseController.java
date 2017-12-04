package dbController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import javafx.util.Pair;
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
    connect_string_ = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
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

  public String Open() {
	    try {
	        Class.forName("oracle.jdbc.OracleDriver");
	        connection_ = DriverManager.getConnection(connect_string_, username, password);
          	connection_.setAutoCommit(false);
	        statement_ = connection_.createStatement();
	        return "success";
	    } catch (SQLException sqlex) {
	        return sqlex.toString();
	    } catch (ClassNotFoundException e) {
	        return e.toString();
	        //System.exit(1); //programemer/dbsm error
	    } catch (Exception ex) {
	       return ex.toString();
	       //System.exit(2);
	    }
  }


  public String insertModel(int modelNum, String deptName, String modelName, float cost, int[] luxuryParts) throws SQLException{
      String updateStatement = "INSERT INTO hdcovello.DepartmentModel (modelNum,modelname,modelcost,deptname) "
    		  + "values (" + modelNum + ",'" + modelName + "'," + cost + ",'" + deptName + "')";
      statement_.executeUpdate(updateStatement);
	  for(int i = 0; i < luxuryParts.length; i++){
          updateStatement = "INSERT INTO hdcovello.LuxuryPartOfModel (modelNum,partNum, qty) "
          + "values (" + modelNum + "," + luxuryParts[i] + ",1)";
          statement_.executeUpdate(updateStatement);
      }
      Commit();
      return "success";
  }

  public List<Pair<Integer, String>> getModels() throws SQLException{
      String queryStatement = "SELECT (modelnum, modelname) "
    		  + "FROM hdcovello.DepartmentModel";
      ResultSet answer = statement_.executeQuery(queryStatement);
      List<Pair<Integer, String>> result = new ArrayList<Pair<Integer, String>>();
      while(answer.next()){
    	  int num = answer.getInt("modelnum");
    	  String name = answer.getString("modelname");
    	  result.add(new Pair<Integer,String>(new Integer(num), name));
      }
      return result;
  }



}
