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
            out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
            out.write("<p>"+ex.toString()+"</p>");
            out.write("<p>"+"</p>");
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
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
          out.write("<p>"+"</p>");
          //out.write("<p>"+ex.StackTrace)
        }
      }
      else if (table.equals("Ship")){

        String ship = request.getParameter("id");
        String model = request.getParameter("modelNum");
        String cust = request.getParameter("custNum");


        try{
          boolean isDelete = false;
          if(operation.equals("insert")){
            controller.insertShip(Integer.parseInt(ship), Integer.parseInt(model), Integer.parseInt(cust));
            out.write("<p>Inserted new ship:</p>");
          }
          else if(operation.equals("update")){

            out.write("<p>Updated values:</p>");
          }
          else if(operation.equals("delete")){

            out.write("<p>Deleted ship "+ship+"</p>");
            isDelete = true;
          }
          if(!isDelete){
            out.write("<p>Ship: "+ship+"</p>");
            out.write("<p>Model: "+model+"</p>");
            out.write("<p>Customer: "+cust+"</p>");
          }
        }
        catch(Exception ex){
          out.write("<p>"+"Ahh! SOrry this no work. =/"+"</p>");
          out.write("<p>"+ex.toString()+"</p>");
          out.write("<p>"+"</p>");
          //out.write("<p>"+ex.StackTrace)
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
