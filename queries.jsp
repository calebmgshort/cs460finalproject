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
		<title>Queries</title>

		<style>
			button {
			    background-color: #8aa;
			    color: black;
			    padding: 14px 20px;
			    margin: 8px 0;
			    border: 1px solid black;
			    cursor: pointer;
			    width: 200px;
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

			<select name="modelNum" id="modelSelect" form="query1Form">
				<option selected disabled>Select model</option>
				<%
					try{
						DatabaseController controller = new DatabaseController();
						controller.Open();

						List<Pair<Integer, String>> models = controller.getModels();

						for (Pair<Integer, String> model : models){
							out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
						}

						controller.Close();
					}
					catch(Exception ex){
							out.write("<option value=1>"+ex.toString()+"</option>");
					}
				%>
			</select>
			<form action="query.jsp" method="post" onsubmit="return validateQueryOne()" id="query1Form">
				<input type="hidden" name="query" value="1">
				<input type="submit" value="Get Cost of Parts" />
			</form>

			<button>Get Incomplete Ships</button></br>
			<button>Get MVP Customer</button></br>

    </div>

  </center>
	</body>
</html>

<script>

function validateQueryOne(){

		if (document.getElementById("modelSelect").selectedIndex > 0){
			return true;
		}

	return false;
}

</script>
