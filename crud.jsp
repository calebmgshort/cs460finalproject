<!--
Author: Michael Uebele

This page receieves HTTP Post requests for most CRUD operations for the company.
Creating, updating, and deleting of models, parts, ships, and customers is all done here.
-->

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
              controller.insertModel(idNum, dept, model, costNum, partNums);
              out.write("<p>Inserted new model:</p>");
            }
            else if(operation.equals("update")){

              out.write("<p>Updated values:</p>");
            }
            else if(operation.equals("delete")){

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
          catch(Exception ex){
            out.write("<p>"+"ERROR: Unable to create model. Model ID "+idNum+" already exists. Please select a different Model ID number and try again."+"</p>");

            //out.write("<p>"+ex.StackTrace)
          }
      }
      else if (table.equals("Part")){
        String id = request.getParameter("id");
        int idNum = Integer.parseInt(id);

        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        String required = request.getParameter("required");

        int isRequired = 0;
        if (required != null){
          isRequired = 1;
        }

        try{
          boolean isDelete = false;
          if(operation.equals("insert")){
            controller.insertPart(idNum, name, price, isRequired);
            out.write("<p>Inserted new part:</p>");
            out.write("<p>ID Number: "+idNum+"</p>");
            out.write("<p>Part Name: "+name+"</p>");
            out.write("<p>Price: "+price+"</p>");
            //out.write("<p>Quantity in stock: "+quantity+"</p>");
            if (required != null){
              out.write("<p>Required: Yes</p>");
            }
            else{
              out.write("<p>Required: No</p>");
            }
          }
          else if(operation.equals("update")){
            controller.updatePart(idNum, price);
            out.write("<p>Updated part: "+idNum+"</p>");
            out.write("<p>New price: "+price+"</p>");
          }
          else if(operation.equals("delete")){

            out.write("<p>Deleted Part "+idNum+"</p>");
            isDelete = true;
          }
          if(!isDelete){

          }
        }
        catch(Exception ex){
          out.write("<p>"+"ERROR: Unable to create part. Part ID "+idNum+" already exists. Please select a different Part ID number and try again."+"</p>");

          //out.write("<p>"+ex.StackTrace)
        }
      }
      else if (table.equals("Ship")){

        String ship = request.getParameter("id");
        String model = request.getParameter("modelNum");
        String cust = request.getParameter("custNum");


        try{
          if(operation.equals("insert")){
            controller.insertShip(Integer.parseInt(ship), Integer.parseInt(model), Integer.parseInt(cust));
            out.write("<p>Created new ship order:</p>");
            out.write("<p>Ship: "+ship+"</p>");
            out.write("<p>Model: "+model+"</p>");
            out.write("<p>Customer: "+cust+"</p>");
          }
          else if(operation.equals("delete")){

            controller.deleteShip(Integer.parseInt(ship));
            out.write("<p>Scrapped ship "+ship+"</p>");
          }
        }
        catch(Exception ex){
          out.write("<p>"+"ERROR: Unable to create ship order. Ship ID "+ship+" already exists. Please select a different Ship ID number and try again."+"</p>");

          //out.write("<p>"+ex.StackTrace)
        }
      }
      else if (table.equals("customer")){
        String username = request.getParameter("username");
        String first = request.getParameter("first");
        String last = request.getParameter("last");

        try{
          if (operation.equals("insert")){
            controller.insertCustomer(username,first,last);
            out.write("<p>Created new customer:</p>");
            out.write("<p>Username: "+username+"</p>");
            out.write("<p>First name: "+first+"</p>");
            out.write("<p>Last name: "+last+"</p>");
          }
        }
        catch(Exception ex){
          out.write("<p>"+"ERROR: Unable to create customer. Username "+username+" already exists. Please select a different username and try again."+"</p>");
          out.write(ex.toString());
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
