package dbController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.util.*;
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


  public void insertModel(int modelNum, String deptName, String modelName,
        float cost, int[] luxuryParts) throws SQLException{
    // Add a new model
    String updateStatement = "INSERT INTO hdcovello.DepartmentModel (modelNum,modelname,modelcost,deptname) "
    		  + "VALUES (" + modelNum + ",'" + modelName + "'," + cost + ",'" + deptName + "')";
    statement_.executeUpdate(updateStatement);

    // Add the luxury parts associated with this model
	  for(int i = 0; i < luxuryParts.length; i++){
      updateStatement = "INSERT INTO hdcovello.LuxuryPartOfModel (modelNum,partNum, qty) "
            + "VALUES (" + modelNum + "," + luxuryParts[i] + ",1)";
      statement_.executeUpdate(updateStatement);
    }
    Commit();
  }

  public void insertShip(int shipNum, int modelNum, int custNum) throws SQLException{
    // Get the cost associated with the given modelnum
    //String queryStatement = "SELECT modelCost FROM hdcovello.DepartmentModel "
    //    + "WHERE modelnum=" + modelNum;
    //ResultSet answer = statement_.executeQuery(queryStatement);
    //answer.next();
    //int cost = answer.getInt(1);

    // Insert the actual ship
    String updateStatement = "INSERT INTO hdcovello.ShipContract (shipnum,modelnum,custnum,aftermarkupcost) "
        + "VALUES (" + shipNum + "," + modelNum + "," + custNum + ",0)";
    statement_.executeUpdate(updateStatement);

    // Now insert the parts for that ship into PartToComplete
    queryStatement = "(SELECT partnum,qty,price "
        + "FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.Part USING (partnum)) "
			  + "UNION "
			  + "(SELECT partnum,1 as \"qty\",price FROM hdcovello.Part WHERE isrequired=0)";
	  answer = statement_.executeQuery(queryStatement);
	  while(answer.next()){
		  int partNum = answer.getInt(1);
		  int qty = answer.getInt(2);
		  int price = answer.getInt(3);
		  updateStatement = "INSERT INTO hdcovello.PartToComplete (shipnum,partnum,qtyleft,contractedprice) "
				  + "VALUES (" + shipNum + "," + partNum + "," + qty + "," + price + ")";
		  statement_.executeUpdate(updateStatement);
	  }
	  Commit();
  }

  public void deleteShip(int shipNum) throws SQLException{
    String updateStatement = "DELETE FROM hdcovello.ShipContract "
    		  + "WHERE shipnum=" + shipNum;
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  public void updateShip(int shipNum, int partNum) throws SQLException{

    String queryStatement = "SELECT price FROM hdcovello.Part "
        + "WHERE partnum=" + partNum;
    ResultSet answer = statement_.executeQuery(queryStatement);
    answer.next();
    int price = answer.getInt(1);

    String updateStatement = "UPDATE hdcovello.ShipContract "
          + "SET afterMarkupCost = afterMarkupCost + " + price
    		  + " WHERE shipnum=" + shipNum;
    statement_.executeUpdate(updateStatement);

    // TODO: the Update Ship also needs to decrement the quantity left,
    // delete from PartToComplete if necessary, and change the status of the
    // Ship if all parts are completed as a result.
    Commit();

  }

  public void insertPart(int partNum, String partName, int price, int isRequired) throws SQLException{
    String updateStatement = "INSERT INTO hdcovello.Part (partnum,partname,price,isrequired) "
    		  + "VALUES (" + partNum + ",'" + partName + "'," + price + "," + isRequired + ")";
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  public void updatePart(int partNum, int newPrice) throws SQLException{
    String updateStatement = "UPDATE hdcovello.Part "
    		  + "SET price = " + newPrice
          + " WHERE partnum = " + partNum;
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  public void insertCustomer(int custNum, String firstName, String lastName) throws SQLException{
    String updateStatement = "INSERT INTO hdcovello.Customer (custnum,firstname,lastname) "
        + "VALUES (" + custNum + ",'" + firstName + "','" + lastName + "')";
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  public List<Pair<Integer, String>> getModels() throws SQLException{
    String queryStatement = "SELECT modelnum, modelname "
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

  public List<Pair<Integer, String>> getParts() throws SQLException{
    String queryStatement = "SELECT partnum, partname "
        + "FROM hdcovello.Part";
    ResultSet answer = statement_.executeQuery(queryStatement);
    List<Pair<Integer, String>> result = new ArrayList<Pair<Integer, String>>();
    while(answer.next()){
      int num = answer.getInt("partnum");
      String name = answer.getString("partname");
      result.add(new Pair<Integer,String>(new Integer(num), name));
    }
    return result;
  }

  public List<Integer> getShips() throws SQLException{
    String queryStatement = "SELECT shipNum "
        + "FROM hdcovello.ShipContract";
    ResultSet answer = statement_.executeQuery(queryStatement);
    List<Integer> result = new ArrayList<Integer>();
    while(answer.next()){
      int shipNum = answer.getInt(1);
      result.add(new Integer(shipNum));
    }
    return result;
  }

  public List<Pair<Integer,Pair<String,Integer>>> getRemainingShipParts(int shipNum) throws SQLException{
      String queryStatement = "SELECT partnum,partname,qtyleft "
          + "FROM hdcovello.PartToComplete JOIN hdcovello.Part using (partnum) "
          + "WHERE shipNum=" + shipNum;
      ResultSet answer = statement_.executeQuery(queryStatement);
      List<Pair<Integer,Pair<String,Integer>>> result = new ArrayList<Pair<Integer,Pair<String,Integer>>>();
      while(answer.next()){
        int partNum = answer.getInt(1);
        String partName = answer.getString(2);
        int qtyLeft = answer.getInt(3);
        Pair<String,Integer> singleAnswerPart = new Pair<String,Integer>(partName,new Integer(qtyLeft));
        result.add(new Pair<Integer,Pair<String,Integer>>(new Integer(partNum), singleAnswerPart));
      }
      return result;
  }

  public int query1(int modelNum) throws SQLException{
	  String query = "SELECT SUM(price * qty) "  +
			    "FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.Part using (partNum) " +
			    "WHERE modelNum = " + modelNum;
	  ResultSet answer = statement_.executeQuery(query);
    if(!answer.next())
      return -1;
    int result1 = answer.getInt(1);
	  query = "SELECT SUM(price) FROM hdcovello.RequiredPart";
	  answer = statement_.executeQuery(query);
    if(!answer.next())
      return -1;
    int result2 = answer.getInt(1);
    return result1 + result2;
  }

  public List<Integer> query2() throws SQLException{
	  String query = "SELECT DISTINCT shipNum FROM hdcovello.ShipContract " +
			  "JOIN hdcovello.PartToComplete p USING (shipNum) WHERE EXISTS " +
			  "(((SELECT partNum, qty FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.ShipContract USING (modelNum)) " +
			  "UNION " +
			  "(SELECT partNum, 1 as 'qty' FROM hdcovello.Part WHERE isRequired = 1)) " +
			  "MINUS " +
			  "(SELECT partNum, qtyLeft FROM hdcovello.PartToComplete JOIN hdcovello.ShipContract USING (shipNum))) " +
			  "AND EXISTS (SELECT shipNum FROM hdcovello.PartToComplete WHERE p.qtyLeft > 0)";
	  ResultSet answer = statement_.executeQuery(query);
	  List<Integer> result = new ArrayList<Integer>();
    while(answer.next()){
      int num = answer.getInt(1);
      result.add(new Integer(num));
    }
    return result;
  }

  public Pair<Integer, Integer> query3() throws SQLException{
	  String query = "select custNum, totalSpent from ( " +
				"select custNum, sum(afterMarkupCost) as totalSpent from hdcovello.Customer " +
				"join hdcovello.ShipContract using (custNum) " +
				"group by custNum " +
				"order by totalSpent desc " +
			  ") where rownum = 1";
	  ResultSet answer = statement_.executeQuery(query);
	  answer.next();
	  int custNum = answer.getInt("custNum");
	  int totalSpent = answer.getInt("totalSpent");
	  Pair<Integer, Integer> ans = new Pair<Integer, Integer>(new Integer(custNum), new Integer(totalSpent));
	  return ans;
  }

  public List<Integer> query4(String username, String status) throws SQLException{
    // TODO: implement query 4
    String query = "Select distinct sc.shipNum as 'ORDERNUM', modelName, afterMarkupCost as 'COST' "
    + "from ShipContract sc "
    + "join PartToComplete p on sc.shipNum = p.shipNum "
    + "join DepartmentModel using (modelNum) "
    + "join Customer using (custNum) "
    + "where username = '" + username + "' and not exists ("
	  + "select * from PartToComplete "
	  + "join ShipContract using (shipNum) "
	  + "where p.qtyLeft != 0 and sc.shipNum = shipNum)";
    return null;
  }

}
