<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="java.util.*,java.lang.StringBuffer,
    dbController.DatabaseController" %>
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
  		String result = controller.Open();

      if (!result.equals("success")){
        out.write(result);
      }

      String query = request.getParameter("query");

      if (query.equals("1")){
          String id = request.getParameter("modelNum");
          int idNum = Integer.parseInt(id);

          try{

            int totalCost = controller.query1(idNum);

            out.write("<p>Total cost of parts for model : "+totalCost+"</p>");
            }
          catch(Exception ex){
            out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
            out.write("<p>"+ex.toString()+"</p>");
            out.write("<p>"+idNum+"</p>");
            //out.write("<p>"+ex.StackTrace)
          }
      }


  		out.write("<hr/>");
      out.write("<button><a href=\"Index.html\">Home</a></button>");

  		// close the dbcontroller and relase all resources occupied by it.
  	  controller.Close();
	%>
	</div>


	</body>
</html>
