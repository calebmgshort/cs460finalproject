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
 ||   Constructors:  public DatabaseController(): No arguments
 ||
 ||  Class Methods:  None
 ||
 ||  Inst. Methods:  public DatabaseController()
 ||                  public String Open()
 ||                  public void Close()
 ||                  public void Commit()
 ||                  public void insertModel(int modelNum, String deptName, String modelName, float cost, int[] luxuryParts)
 ||                  public void insertShip(int shipNum, int modelNum, int custNum)
 ||                  public void deleteShip(int shipNum)
 ||                  public void updateShip(int shipNum, int partNum)
 ||                  public void insertPart(int partNum, String partName, int price, int isRequired)
 ||                  public void updatePart(int partNum, int newPrice)
 ||                  public void insertCustomer(String username, String firstName, String lastName)
 ||                  public List<Pair<Integer, String>> getModels()
 ||                  public List<Pair<Integer, String>> getParts()
 ||                  public List<Pair<Integer, String>> getCustomers()
 ||                  public List<Pair<Integer, String>> getLuxuryParts()
 ||                  public List<Integer> getShips()
 ||                  public List<Pair<Integer,Pair<String,Integer>>> getRemainingShipParts(int shipNum)
 ||                  public int query1(int modelNum)
 ||                  public List<Integer> query2()
 ||                  public Pair<Integer, Integer> query3()
 ||                  public List<Integer> query4(String username, String status)
 ||                  public List<Query5ReturnResult> query5()
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

  /*---------------------------------------------------------------------
   |  Method insertModel
   |
   |  Purpose:  Create a new department and model
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: A tuple has been added to DepartmentModel, and 3-10 tuples have been
   |                  added to LuxuryPartOfModel
   |
   |  Parameters:
   |      int modelNum -- The model number of the model to add
   |      String deptName -- The name of the DepartmentModel
   |      String modelName -- The name of the model
   |      float cost -- The cost of this modelNme
   |      int[] luxuryParts -- The luxury parts that go with this model
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method insertShip
   |
   |  Purpose:  Create/order a new ship
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: A tuple has been added to ShipContract, and several tuples have been
   |                  added to PartToComplete
   |
   |  Parameters:
   |      int shipNum -- The ship number of the ship to add
   |      int modelNum -- The model number
   |      int custNum -- The customer number
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
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

    // Insert the parts associated with this ship
    queryStatement = "(SELECT partnum,qty,price "
        + "FROM hdcovello.LuxuryPartOfModel JOIN hdcovello.Part USING (partnum) "
        + "WHERE modelnum = " + modelNum + ") "
			  + "UNION "
			  + "(SELECT partnum,1 as \"qty\",price FROM hdcovello.Part WHERE isrequired=1)";
	  answer = statement_.executeQuery(queryStatement);
    List<Integer> partNums = new ArrayList<Integer>();
    List<Integer> qtys = new ArrayList<Integer>();
    List<Integer> prices = new ArrayList<Integer>();
    while(answer.next()){
		  int partNum = answer.getInt(1);
		  int qty = answer.getInt(2);
		  int price = answer.getInt(3);
      partNums.add(new Integer(partNum));
      qtys.add(new Integer(qty));
      prices.add(new Integer(price));
	  }
    for(int i = 0; i < partNums.size(); i++){
      int partNum = partNums.get(i).intValue();
      int qty = qtys.get(i).intValue();
      int price = prices.get(i).intValue();
      updateStatement = "INSERT INTO hdcovello.PartToComplete (shipnum,partnum,qtyleft,contractedprice) "
				  + "VALUES (" + shipNum + "," + partNum + "," + qty + "," + price + ")";
		  statement_.executeUpdate(updateStatement);
    }

	  Commit();
  }

  /*---------------------------------------------------------------------
   |  Method deleteShip
   |
   |  Purpose:  Delete a ship
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: A tuple has been removed from ShipContract, and 0-many tuples have been
   |                  removed from PartToComplete
   |
   |  Parameters:
   |      int shipNum -- The ship number of the ship to delete
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
  public void deleteShip(int shipNum) throws SQLException{
    String updateStatement = "DELETE FROM hdcovello.PartToComplete "
    		  + "WHERE shipnum=" + shipNum;
          statement_.executeUpdate(updateStatement);
    updateStatement = "DELETE FROM hdcovello.ShipContract "
    		  + "WHERE shipnum=" + shipNum;
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  /*---------------------------------------------------------------------
   |  Method updateShip
   |
   |  Purpose:  Update a ship, thus adding the given part to the given ship
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: PartToComplete qty in 1 PartToComplete tuple is -=1, and is
   |                  removed if that qty reaches 0
   |
   |  Parameters:
   |      int shipNum -- The ship number associated with the part do decrement
   |      int partNum -- The part number of the part do decrement
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method insertPart
   |
   |  Purpose:  Insert a part into the catalogue of parts
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: A tuple has been added to relation Part
   |
   |  Parameters:
   |      int partNum -- The part number of the part to create
   |      String partName -- The name of the part to create
   |      int price -- The price of the part to create
   |      int isRequired -- 1 if this part is required for all ships, 0 otherwise
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
  public void insertPart(int partNum, String partName, int price, int isRequired) throws SQLException{
    String updateStatement = "INSERT INTO hdcovello.Part (partnum,partname,price,isrequired) "
    		  + "VALUES (" + partNum + ",'" + partName + "'," + price + "," + isRequired + ")";
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  /*---------------------------------------------------------------------
   |  Method updatePart
   |
   |  Purpose:  Change the price of the given part to the new price
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: The price field in a tuple in Part has been changed
   |
   |  Parameters:
   |      int partNum -- The part number of the part to change
   |      int newPrice -- The new price of the given part
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
  public void updatePart(int partNum, int newPrice) throws SQLException{
    String updateStatement = "UPDATE hdcovello.Part "
    		  + "SET price = " + newPrice
          + " WHERE partnum = " + partNum;
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  /*---------------------------------------------------------------------
   |  Method insertCustomer
   |
   |  Purpose:  Create a new customer
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: A tuple has been added to relation Customer
   |
   |  Parameters:
   |      String username -- The username of the customer being created
   |      String firstName -- The first name of the customer being created
   |      String lastName -- The last name of the customer being created
   |
   |  Returns:  None
   *-------------------------------------------------------------------*/
  public void insertCustomer(String username, String firstName, String lastName) throws SQLException{
    // Get the customer number to used
    String queryStatement = "SELECT Max(custnum) FROM hdcovello.Customer";
    ResultSet answer = statement_.executeQuery(queryStatement);
    int custNum;
    if(answer.next())
      custNum = answer.getInt(1) + 1;
    else
      custNum = 1;
    // Insert a customer
    String updateStatement = "INSERT INTO hdcovello.Customer (custnum,username,firstname,lastname) "
        + "VALUES (" + custNum + ",'" + username + "','" + firstName + "','" + lastName + "')";
    statement_.executeUpdate(updateStatement);
    Commit();
  }

  /*---------------------------------------------------------------------
   |  Method getModels
   |
   |  Purpose:  Get the whole list of models in the database
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  A list of the modelNums and modelNames of each model in the db
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method getParts
   |
   |  Purpose:  Get the whole list of parts in the database
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  A list of the partNums and partNames of each part in the db
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method getCustomers
   |
   |  Purpose:  Get the whole list of customers in the database
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  A list of the custNums and usernames of each part in the db
   *-------------------------------------------------------------------*/
  public List<Pair<Integer, String>> getCustomers() throws SQLException{
    String queryStatement = "SELECT custnum, username "
        + "FROM hdcovello.Customer";
    ResultSet answer = statement_.executeQuery(queryStatement);
    List<Pair<Integer, String>> result = new ArrayList<Pair<Integer, String>>();
    while(answer.next()){
      int num = answer.getInt("custnum");
      String name = answer.getString("username");
      result.add(new Pair<Integer,String>(new Integer(num), name));
    }
    return result;
  }

  /*---------------------------------------------------------------------
   |  Method getLuxuryParts
   |
   |  Purpose:  Get the whole list of luxuryParts in the database
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  A list of the partNums and partNames of each luxury part in the db
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method getShips
   |
   |  Purpose:  Get the whole list of ships in the database
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  A list of the shipNums of each ship in the db
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method getRemainingShipParts
   |
   |  Purpose:  Get the whole list of parts that remain to be built for the given ship
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters:
   |        int shipNum -- The ship, for which the list of remaining parts will be returned
   |
   |  Returns:  A list of the partNums, partNames and qtyLefts of each part that has yet to be built
   |            for this ship
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method query1
   |
   |  Purpose:  Satisfy query1, which is "Choosing one from a list of ship types,
   |            report the cost of all the parts"
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters:
   |      int modelNum -- The modelNum of the model for which to report the cost
   |
   |  Returns:  The cost of all the parts of this model
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method query2
   |
   |  Purpose:  Satisfy query2, which is "List all ships that are partially
   |            built but incomplete"
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  The shipNums of the shps that are partially build but incomplete
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method query3
   |
   |  Purpose:  Satisfy query3, which is "Find the customer that has paid the most
   |            for ships from I4 , and how much they have spent"
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  The customer that has paid the most to I4, and how much they have spent
   *-------------------------------------------------------------------*/
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

  /*---------------------------------------------------------------------
   |  Method query4
   |
   |  Purpose:  Satisfy query4, which is "Using the given customer name and status,
   |            show the customer all of their ships with that status"
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters:
   |        String username - The username of the customer whose ships to display
   |        String status - The status of the ships to display
   |
   |  Returns:  The list of ships credited to the given customer and with the given status
   *-------------------------------------------------------------------*/
  public List<Integer> query4(String username, String status) throws SQLException{
    String query;
    if(status.equals("all")){
      query = "select distinct shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" from hdcovello.ShipContract "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "'";
    }
    else if(status.equals("in progress")){
      query = "Select distinct shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\"  from hdcovello.ShipContract sc "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "' and exists "
            + "(((select partNum, qty from hdcovello.LuxuryPartOfModel join hdcovello.ShipContract using (modelNum) where sc.shipNum = shipNum) "
            + "union "
            + "(select partNum, 1 as \"qty\" from hdcovello.Part where isRequired = 1)) "
            + "minus "
            + "(select partNum, qtyLeft from hdcovello.PartToComplete join hdcovello.ShipContract using (shipNum) where sc.shipNum = shipNum)) "
            + "and exists (select * from hdcovello.PartToComplete where qtyLeft > 0 and sc.shipNum = shipNum)";
    }
    else if(status.equals("pending")){
      query ="Select distinct sc.shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" from hdcovello.ShipContract sc "
    + "join hdcovello.DepartmentModel using (modelNum) "
    + "join hdcovello.Customer using (custNum) "
    + "where username = '" + username + "' and not exists "
    + "(((select partNum, qty from hdcovello.LuxuryPartOfModel join hdcovello.ShipContract using (modelnum) where sc.shipNum = shipNum) "
    + "union "
    + "(select partNum, 1 as \"qty\" from hdcovello.Part where isRequired = 1)) "
    + "minus "
    + "(select partNum, qtyLeft from hdcovello.PartToComplete join hdcovello.ShipContract using (shipnum) where sc.shipNum = shipNum))";
    }
    else if(status.equals("complete")){
      query = "Select distinct sc.shipNum as \"ORDERNUM\", modelName, afterMarkupCost as \"COST\" "
            + "from hdcovello.ShipContract sc "
            + "join hdcovello.DepartmentModel using (modelNum) "
            + "join hdcovello.Customer using (custNum) "
            + "where username = '" + username + "' and not exists ("
  	        + "select * from hdcovello.PartToComplete "
  	        + "join hdcovello.ShipContract using (shipNum) "
  	        + "where qtyLeft != 0 and sc.shipNum = shipNum)";
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

  /*---------------------------------------------------------------------
   |  Method query5
   |
   |  Purpose:  Satisfy query5, which is "What are the top three most popular ship
   |            models sold, how many have sold to date, and what have the min,
   |            average, and max prices been for each?"
   |
   |  Pre-condition:  The database has been initalized and the connection is valid
   |
   |  Post-condition: None
   |
   |  Parameters: None
   |
   |  Returns:  Data about the top three most popular ship models sold
   *-------------------------------------------------------------------*/
  public List<Query5ReturnResult> query5() throws SQLException{
    String query = "SELECT sub.modelNum, sub.modelName, totalSold, MIN(afterMarkupCost) as \"min\", "
        + "AVG(afterMarkupCost) as \"avg\", MAX(afterMarkupCost) as \"max\" "
        + "FROM (SELECT modelNum, modelName, COUNT(shipNum) AS totalSold "
        + "FROM hdcovello.ShipContract "
        + "JOIN hdcovello.DepartmentModel using (modelNum) "
        + "GROUP BY modelNum, modelName) sub "
        + "JOIN hdcovello.ShipContract sc on (sc.modelNum = sub.modelNum) "
        + "GROUP BY sub.totalSold, sub.modelNum, sub.modelName "
        + "ORDER BY sub.totalSold DESC";




    ResultSet answer = statement_.executeQuery(query);
    List<Query5ReturnResult> result = new ArrayList<Query5ReturnResult>();
    int i = 0;
    while(answer.next() && i < 3){
      i++;
      int modelNum = answer.getInt("modelNum");
      String modelName = answer.getString("modelName");
      int totalSold = answer.getInt("totalSold");
      float lowestPrice = answer.getFloat("min");
      float averagePrice = answer.getFloat("avg");
      float highestPrice = answer.getFloat("max");
      result.add(new Query5ReturnResult(modelNum, modelName, totalSold, lowestPrice,
            averagePrice, highestPrice));
    }
    return result;
  }

  /*+----------------------------------------------------------------------
   ||
   ||  Class Query5ReturnResult
   ||
   ||         Author:  Caleb Short
   ||
   ||        Purpose:  This is an object containing multiple fields needed to be returned by query5
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
   ||   Constructors:  public Query5ReturnResult(int modelNum, String modelNme, int totalSold,
             float lowestPrice, float averagePrice, float highestPrice)
   ||
   ||  Class Methods:  None
   ||
   ||  Inst. Methods:  None
   ||
   ++-----------------------------------------------------------------------*/
  public class Query5ReturnResult{
    public int modelNum;
    public String modelName;
    public int totalSold;
    public float lowestPrice;
    public float averagePrice;
    public float highestPrice;
    public Query5ReturnResult(int modelNum, String modelName, int totalSold,
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
