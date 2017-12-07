<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="
java.util.*,
java.util.AbstractMap.SimpleEntry,
java.util.Map,
javafx.util.Pair,
dbController.DatabaseController,
dbController.DatabaseController.Query5ReturnResult
"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Query Results</title>

		<style>
    button {
        background-color: #8aa;
        color: white;
        padding: 14px 20px;
        margin: 8px 0;
        border: 1px solid black;
        cursor: pointer;
        width: 100px;
    }

			table {
				font-size: 18px;
				font-family:'Verdana';
			}
		</style>
	</head>

	<body>

	<div id="searchresult" align="center" >
	<%
  		request.setCharacterEncoding("utf-8");
 	 	  response.setContentType("text/html;charset=utf-8");

  		DatabaseController controller = new DatabaseController();
  		// connect to backend database server via the databasecontroller, which
  		// is a wrapper class providing necessary methods for this particular
  		// application
  		String message = controller.Open();

      if (!message.equals("success")){
        out.write(message);
      }

      String query = request.getParameter("query");

      if (query.equals("1")){
        try{
          String id = request.getParameter("modelNum");
          int idNum = Integer.parseInt(id);

          int totalCost = controller.query1(idNum);

          out.write("<p>Total cost of parts for model : "+totalCost+"</p>");
          }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
        }
      }
      else if (query.equals("2")){
        try{
          out.write("<p>The following ships are currently under construction:</p>");

          List<Integer> ships = controller.query2();

          for (Integer ship : ships){
            out.write("<p>Ship "+ship+"</p>");
          }
        }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
        }
      }
      else if (query.equals("3")){
        try{
          Pair<Integer,Integer> MVP = controller.query3();

          out.write("<p>Customer "+MVP.getKey()+" is our most valuable customer, having spent $"+MVP.getValue()+"</p>");
          }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
        }
      }
      else if (query.equals("4")){
        try{
          String username = request.getParameter("username");
          String status = request.getParameter("status");

          out.write("<p>Ships for "+username+" with status "+status+":</p>");

          List<Integer> ships = controller.query4(username, status);

          for (Integer ship : ships){
            out.write("<p>Ship "+ship+"</p>");
          }
        }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
        }
      }
			else if (query.equals("5")){
				try{
          Query5ReturnResult result = controller.query5();

          out.write("<p>Number: "+result.modelNum+"</p>");
					out.write("<p>Name: "+result.modelName+"</p>");
					out.write("<p>Total Sold: "+result.totalSold+"</p>");
					out.write("<p>Lowest Price: "+result.lowestPrice+"</p>");
					out.write("<p>Average Price: "+result.averagePrice+"</p>");
					out.write("<p>Highest Price: "+result.highestPrice+"</p>");
        }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
        }
			}


  		out.write("<hr/>");
      out.write("<button><a href=\"index.html\">Home</a></button>");

  		// close the dbcontroller and relase all resources occupied by it.
  	  controller.Close();
	%>
	</div>


	</body>
</html>
