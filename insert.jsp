<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="java.util.*,java.lang.StringBuffer,
    dbController.DatabaseController" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Search Results</title>

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

      String table = request.getParameter("table");

      if (table.equals("DepartmentModel")){
          String id = request.getParameter("id");
          int idNum = Integer.parseInt(id);

          String dept = request.getParameter("dept");
          String model = request.getParameter("model");
          String cost = request.getParameter("cost");
          float costNum = (float)Integer.parseInt(cost);

          String[] parts = request.getParameterValues("parts");
          int[] partNums = new int[parts.length];

          int i = 0;
          for (String num : parts){
            partNums[i] = Integer.parseInt(num);
            i++;
          }

          out.write(id);
          out.write(dept);
          out.write(model);
          out.write(cost);
          for (int x : partNums){
            out.write(""+x);
          }
          try{
            String result = controller.insertModel(idNum, dept, model, costNum, partNums);
            out.write(result);
          }
          catch(Exception ex){
            out.write(ex.toString());
            //out.write("<p>"+ex.StackTrace)
          }

          result = controller.insertModel(idNum, dept, model, costNum, partNums);
      }



  		// writing the content on output/response page
  		out.write("<h1 style=\"color: #4CAF50;\">All Products</h1>");
  		out.write("<hr/>");


  		// close the dbcontroller and relase all resources occupied by it.
  	  //controller.Close();
	%>
	</div>


	</body>
</html>
