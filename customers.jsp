<!--
Author: Michael Uebele

User page for managing customers.
Supports adding new customers.
-->

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
		<title>Customers</title>

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
				width: 100%;
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

		<h1>Infinite Improbability Intergalactic Inc</h1>
		<h4>Let all your space dreams come true</h4>

		<button><a href="index.html">Home</a></button>
		<hr/>

    <div>

			<h2><u>Add Customer</u></h2>
			<form action="crud.jsp" method="post" onsubmit="return validateInsert()" id="register">
				<input type="hidden" name="operation" value="insert">
				<input type="hidden" name="table" value="customer">

				Username: <input type="text" name="username" id="username"><br/>
				First Name: <input type="text" name="first" id="first"><br/>
				Last Name: <input type="text" name="last" id="last"><br/>

			  <input type="submit" value="submit" />
			</form>

  </center>
	</body>
</html>

<script>


function validateInsert(){
	if (document.getElementById("username").value != ""){
		if (document.getElementById("first").value != ""){
			if (document.getElementById("last").value != ""){
					return true;
			}
		}
	}

	return false;
}



</script>
