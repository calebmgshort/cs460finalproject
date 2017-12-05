<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="
java.util.*,
java.util.AbstractMap.SimpleEntry,
java.util.Map,
javafx.util.Pair,
dbController.DatabaseController
"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Ships</title>

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

      a {
        color: black;
      }

      body{
        background-image: url("grey.jpg");
      }

			table {
				font-size: 18px;
				font-family:'Verdana';
			}

			.column{
				width: 33%;
				float: left;
				border: 1px solid black;
				height: 300px;
			}

			.data{
				text-align: center;
			}
		</style>
	</head>

	<body>


  <center>

    <div>
      <h1>Infinite Improbability Intergalactic Inc</h1>
      <h4>Let all your space dreams come true</h4>

      <button><a href="index.html">Home</a></button>
      <hr/>

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

      String id = request.getParameter("id");
      List<Pair<Integer,Pair<String,Integer>>> rows = getRemainingShipParts(Integer.parse(id));

      out.write("<h3>Ship "+id+"</h3>");



  		if (rows == null) {
         out.write("Ship is complete!");
      }
      else if (rows.size() == 0){
         out.write("Ship is complete!");
      }
      else{
        out.write("<table>");

        out.write("<tr><th><u>Part</u>&nbsp;&nbsp;&nbsp;&nbsp;</th>" +
    		"<th><u>Quantity Remaining</u></th> " +
    		"<th>&nbsp;&nbsp;&nbsp;&nbsp;</th></tr>");

      		if (vecResult != null && vecResult.size() > 0) {
        		for (int i = 0; i < vecResult.size(); i++) {
          			String row = vecResult.get(i);
         		 	String[] detail = row.split("##");
          			if (detail.length != 4) {
            		//break;
          			}

         	 		content.append(
              			"<tr id=\"tablerow_" + i + "\">");
          			content.append(
              			"<td class=\"postlist\">" +
              			detail[0] + "</td>");
          			content.append(
              			"<td>" + detail[1] + "</td>");
          			content.append("<td>" + detail[2] + "</td>" +
                         "<td> &nbsp;&nbsp;" + detail[3] + "</td>");
          			content.append("</tr>");
        		}
      		}
      		out.write(content.toString());
    		out.write("</table><hr/>");

      }



      %>

    </div>

  </center>
	</body>
</html>

<script>



</script>
