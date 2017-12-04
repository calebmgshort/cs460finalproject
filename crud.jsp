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
      String operation = request.getParameter("operation");

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

          try{
            boolean isDelete = false;
            if(operation.equals("insert")){
              result = controller.insertModel(idNum, dept, model, costNum, partNums);
              out.write("<p>Insertion result: "+result+"</p>");
              out.write("<p>Inserted new model:</p>");
            }
            else if(operation.equals("update")){

              out.write("<p>Update result: "+result+"</p>");
              out.write("<p>Updated values:</p>");
            }
            else if(operation.equals("delete")){

              out.write("<p>Delete result: "+result+"</p>");
              out.write("<p>Deleted DepartmentModel "+idNum+"</p>");
              isDelete = true;
            }
            if(!isDelete){
              out.write("<p>ID Number: "+idNum+"</p>");
              out.write("<p>Department Name: "+dept+"</p>");
              out.write("<p>Model Name: "+model+"</p>");
              out.write("<p>Cost: "+cost+"</p>");
              out.write("<p>Part Numbers: ");
                for (int num : partNums){
                  out.write(num + "; ");
                }
              out.write("</p>");
            }
          }
          catch(SQLException ex){
            out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>")
            out.write("<p>"+ex.toString()+"</p>");
            out.write("<p>"+"</p>")
            //out.write("<p>"+ex.StackTrace)
          }
      }


  		out.write("<hr/>");
      out.write("<button><a href="Index.html">Home</a></button>");

  		// close the dbcontroller and relase all resources occupied by it.
  	  controller.Close();
	%>
	</div>


	</body>
</html>
