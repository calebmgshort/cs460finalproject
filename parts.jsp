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
		<title>Parts</title>

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

			<h2><u>Parts</u></h2>

			<div class="row">
				<div class="column">
					<h3>Insert</h3>


						<form action="crud.jsp" method="post" onsubmit="return validateInsert()" id="insertForm">
							<div class="data">
							<input type="hidden" name="operation" value="insert">
							<input type="hidden" name="table" value="Part">
							ID Number: <input type="number" min="0" name="id" id="insertId"><br/>
						  Part Name: <input type="text" name="name" id="insertName"><br/>
							Price: <input type="number" name="price" id="insertPrice"><br/>
							Required: <input type="checkbox" name="required" id="insertRequired"><br/>
							Quantity: <input type="number" name="quantity" id="insertQuantity"><br/>

							</div>
						  <input type="submit" value="Submit" />
						</form>

				</div>
				<div class="column">
					<h3>Update</h3>
					<select name="id" id="update" onchange="UpdateSelect()" form="updateForm">
					  <option selected disabled>Select part to update</option>
						<%
							try{
								DatabaseController controller = new DatabaseController();
					  		controller.Open();

								List<Pair<Integer, String>> models = controller.getParts();

								for (Pair<Integer, String> model : models){
									out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
								}

								controller.Close();
							}
							catch(Exception ex){

							}
						%>
					</select>
					<br/><br/>
					<form action="crud.jsp" method="post" onsubmit="return validateUpdate()" id="updateForm">
						<input type="hidden" name="operation" value="update">
						<input type="hidden" name="table" value="Part">
						<div class="data">

							Price: <input type="number" name="price" id="updatePrice"><br/>

						</div>
						<br/><br/>
						<input type="submit" value="Submit" />
					</form>
				</div>

				<div class="column">
					<h3>Delete</h3>
					<select name="id" id="delete" onchange="deleteSelect()" form="deleteForm">
					  <option selected disabled>Select model to delete</option>
						<%
							try{
								DatabaseController controller = new DatabaseController();
					  		controller.Open();

								List<Pair<Integer, String>> models = controller.getParts();

								for (Pair<Integer, String> model : models){
									out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
								}

								controller.Close();
							}
							catch(Exception ex){

							}
						%>
					</select>
					<br/><br/>
					<form action="crud.jsp" method="post" onsubmit="return validateUpdate()" id="deleteForm">
						<input type="hidden" name="operation" value="delete">
						<input type="hidden" name="table" value="Part">
						<input type="submit" value="Submit" />
					</form>
				</div>
			</div>

    </div>

  </center>
	</body>
</html>

<script>



function validateInsert(){
	if (document.getElementById("insertName").value != ""){
		if (document.getElementById("insertPrice").value != ""){
			if (document.getElementById("insertQuantity").value != ""){
				if (document.getElementById("insertID").value > "0"){
					return true;
				}
			}
		}
	}


	return false;
}

function validateUpdate(){
	if (document.getElementById("updatePrice").value != ""){
		if (document.getElementById("update").selectedIndex > 0){
			return true;
		}
	}

	return false;
}

function validateDelete(){

		if (document.getElementById("delete").selectedIndex > 0){
			return true;
		}

	return false;
}

</script>
