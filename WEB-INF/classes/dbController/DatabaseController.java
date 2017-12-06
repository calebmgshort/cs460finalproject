/*+----------------------------------------------------------------------
 ||
 ||  Class DatabaseController
 ||
 ||         Author:  Caleb short (adapted from the starter file)
 ||
 ||        Purpose:  This class exists to interface between the front-end and
 ||                  the database
 ||
 ||  Inherits From:  None
 ||
 ||     Interfaces:  None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  pubic DatabaseController(): No arguments
 ||
 ||  Class Methods:  [List the names, arguments, and return types of all
 ||                   public class methods.]
 ||
 ||  Inst. Methods:  TODO[List the names, arguments, and return types of all
 ||                   public instance methods.]
 ||
 ++-----------------------------------------------------------------------*/

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

  /*---------------------------------------------------------------------
   |  Method DatabaseController
   |
   |  Purpose:  The class constructor, which just initializes class variables
   |
   |  Pre-condition:  None
   |
   |  Post-condition: An instance of this class exists
   |
   |  Parameters: None
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
  public DatabaseController() {
    // your cs login name
    username = "calebmgshort";
    // your Oracle password, NNNN is the last four digits of your CSID
    password = "Cmg21514007!";
    connect_string_ = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
  }

  /*---------------------------------------------------------------------
   |  Method Open
   |
   |  Purpose:  This method opens a connection to the database
   |
   |  Pre-condition:  There is no connection to the database
   |
   |  Post-condition: There is a connection to the database and a statement
   |                  associated with that connection
   |
   |  Parameters: None
   |
   |  Returns:  A string indicating whether or not the connection was made successfuly
   *-------------------------------------------------------------------*/
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

   /*---------------------------------------------------------------------
 |  Method Close
 |
 |  Purpose:  Closes the DBMS connection that was opened by the open call.
 |
 |  Pre-condition:  The connection to the database and statement_ have been set up
 |
 |  Post-condition: The connection to the database and statement_ are closed
 |
 |  Parameters: None
 |
 |  Returns:  None
 *-------------------------------------------------------------------*/
  public void Close() {
    try {
      statement_.close();
      connection_.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    connection_ = null;
  }

   /*---------------------------------------------------------------------
 |  Method Commit
 |
 |  Purpose:  Commits all update operations made to the dbms.
 |            If auto-commit is on, which is by default, it is not necessary
 |            to call this method.
 |
 |  Pre-condition:  None
 |
 |  Post-condition: The current changes in the database are commited
 |
 |  Parameters: None
 |
 |  Returns:  None
 *-------------------------------------------------------------------*/
  public void Commit() {
    try {
      if (connection_ != null && !connection_.isClosed())
        connection_.commit();
    } catch (SQLException e) {
      System.err.println("Commit failed");
      e.printStackTrace();
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
    String queryStatement = "SELECT modelCost FROM hdcovello.DepartmentModel "
        + "WHERE modelnum=" + modelNum;
    ResultSet answer = statement_.executeQuery(queryStatement);
    answer.next();
    int cost = answer.getInt(1);

    // Insert the actual ship
    String updateStatement = "INSERT INTO hdcovello.ShipContract (shipnum,modelnum,custnum,aftermarkupcost) "
        + "VALUES (" + shipNum + "," + modelNum + "," + custNum + "," + cost + ")";
    statement_.executeUpdate(updateStatement);

    // Now insert the parts for that ship into PartToComplete
    updateStatement = "INSERT INTO hdcovello.PartToComplete (shipnum,partnum,qtyleft,contractedprice) "
          + "((SELECT " + shipNum + ",partnum,qty,price "
          + "FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.Part USING (partnum)) "
      	  + "UNION "
      		+ "(SELECT " + shipNum + ",partnum,1,price FROM hdcovello.Part WHERE isrequired=1))";
    statement_.executeUpdate(updateStatement);
    /*
    queryStatement = "(SELECT partnum,qty,price "
        + "FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.Part USING (partnum)) "
			  + "UNION "
			  + "(SELECT partnum,1 as \"qty\",price FROM hdcovello.Part WHERE isrequired=1)";
	  answer = statement_.executeQuery(queryStatement);
	  while(answer.next()){
		  int partNum = answer.getInt(1);
		  int qty = answer.getInt(2);
		  int price = answer.getInt(3);
		  updateStatement = "INSERT INTO hdcovello.PartToComplete (shipnum,partnum,qtyleft,contractedprice) "
				  + "VALUES (" + shipNum + "," + partNum + "," + qty + "," + price + ")";
		  statement_.executeUpdate(updateStatement);
	  }
    */
	  Commit();
  }

  public void deleteShip(int shipNum) throws SQLException{
    String updateStatement = "DELETE FROM hdcovello.PartToComplete "
    		  + "WHERE shipnum=" + shipNum;
          statement_.executeUpdate(updateStatement);
    updateStatement = "DELETE FROM hdcovello.ShipContract "
    		  + "WHERE shipnum=" + shipNum;
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  public void updateShip(int shipNum, int partNum) throws SQLException{

    // Decrement the quantity of the given part for that ship remaining
    String updateStatement = "UPDATE hdcovello.PartToComplete "
          + "SET qtyleft = qtyleft - 1"
    		  + " WHERE shipnum=" + shipNum + " AND partnum=" + partNum;
    statement_.executeUpdate(updateStatement);

    // Remove the given part for the given ship if it is less than or equal to 0
    updateStatement = "DELETE FROM hdcovello.PartToComplete "
          + " WHERE shipnum=" + shipNum + " AND partnum=" + partNum + " AND qtyleft<=0";
    statement_.executeUpdate(updateStatement);

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

  public List<Pair<Integer, String>> getLuxuryParts() throws SQLException{
    String queryStatement = "SELECT partnum, partname "
        + "FROM hdcovello.Part WHERE isrequired=0";
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
			  "(SELECT partNum, 1 as \"qty\" FROM hdcovello.Part WHERE isRequired = 1)) " +
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
	  Pair<Integer, Integer> result = new Pair<Integer, Integer>(new Integer(custNum), new Integer(totalSpent));
	  return result;
  }

  public List<Integer> query4(String username, String status) throws SQLException{
    String query;
    if(status.equals("all")){
      query = "select distinct shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" from hdcovello.ShipContract "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "'";
    }
    else if(status.equals("in progress")){
      query = "Select distinct shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\"  from hdcovello.ShipContract "
            + "join hdcovello.PartToComplete p using (shipNum) "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "' and exists "
            + "(((select partNum, qty from hdcovello.LuxuryPartOfModel join hdcovello.ShipContract using (modelNum)) "
            + "union "
            + "(select partNum, 1 as \"qty\" from hdcovello.Part where isRequired = 1)) "
            + "minus "
            + "(select partNum, qtyLeft from hdcovello.PartToComplete join hdcovello.ShipContract using (shipNum))) "
            + "and exists (select shipNum from hdcovello.PartToComplete where p.qtyLeft > 0)";
    }
    else if(status.equals("pending")){
      query = "Select distinct shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" from hdcovello.ShipContract "
            + "join hdcovello.PartToComplete p using (shipNum) "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "' and not exists "
            + "(((select partNum, qty from hdcovello.LuxuryPartOfModel join hdcovello.ShipContract using (modelNum)) "
            + "union "
            + "(select partNum, 1 as \"qty\" from hdcovello.Part where isRequired = 1)) "
            + "minus "
            + "(select partNum, qtyLeft from hdcovello.PartToComplete join hdcovello.ShipContract using (shipNum)))";

    }
    else if(status.equals("complete")){
      query = "Select distinct sc.shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" "
            + "from hdcovello.ShipContract sc "
            + "join hdcovello.PartToComplete p on sc.shipNum = p.shipNum "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "' and not exists ("
  	        + "select * from hdcovello.PartToComplete "
  	        + "join hdcovello.ShipContract using (shipNum) "
  	        + "where p.qtyLeft != 0 and sc.shipNum = shipNum)";
    }
    else{
      return null;
    }
    ResultSet answer = statement_.executeQuery(query);
    List<Integer> result = new ArrayList<Integer>();
    while(answer.next()){
      int shipNum = answer.getInt(1);
      result.add(new Integer(shipNum));
    }

    return result;
  }

  public Query5ReturnResult query5() throws SQLException{
    String query = "SELECT sub.modelNum, sub.modelName, totalSold, MIN(afterMarkupCost) as \"min\", "
        + "AVG(afterMarkupCost) as \"avg\", MAX(afterMarkupCost) as \"max\" "
        + "FROM (SELECT modelNum, modelName, COUNT(shipNum) AS totalSold "
        + "FROM ShipContract "
        + "JOIN DepartmentModel using (modelNum) "
        + "WHERE rownum <= 3 "
        + "GROUP BY modelNum, modelName "
        + "ORDER BY totalSold DESC) sub"
        + "JOIN ShipContract sc on (sc.modelNum = sub.modelNum) "
        + "GROUP BY sub.totalSold, sub.modelNum, sub.modelName";

    ResultSet answer = statement_.executeQuery(query);
    int modelNum = answer.getInt("modelNum");
    String modelName = answer.getString("modelName");
    int totalSold = answer.getInt("totalSold");
    float lowestPrice = answer.getFloat("min");
    float averagePrice = answer.getFloat("avg");
    float highestPrice = answer.getFloat("max");
    return new Query5ReturnResult(modelNum, modelName, totalSold, lowestPrice,
            averagePrice, highestPrice);
  }

  public class Query5ReturnResult{
    public int modelNum;
    public String modelName;
    public int totalSold;
    public float lowestPrice;
    public float averagePrice;
    public float highestPrice;
    public Query5ReturnResult(int modelNum, String modelNme, int totalSold,
              float lowestPrice, float averagePrice, float highestPrice){
      this.modelNum = modelNum;
      this.modelName = modelName;
      this.totalSold = totalSold;
      this.lowestPrice = lowestPrice;
      this.averagePrice = averagePrice;
      this.highestPrice = highestPrice;
    }
  }

}
