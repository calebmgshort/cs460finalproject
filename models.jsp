<!--
Author: Michael Uebele

User page for managing models.
Supports adding new models.
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
		<title>Models</title>

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

    <div>
      <h1>Infinite Improbability Intergalactic Inc</h1>
      <h4>Let all your space dreams come true</h4>

      <button><a href="index.html">Home</a></button>
      <hr/>

			<h2><u>Department Models</u></h2>

			<div class="row">
				<div class="column">
					<h3>Create New Model</h3>


						<form action="crud.jsp" method="post" onsubmit="return validateInsert()" id="insertForm">
							<div class="data">
							<input type="hidden" name="operation" value="insert">
							<input type="hidden" name="table" value="DepartmentModel">
							ID Number: <input type="number" min="0" name="id" id="insertId"><br/>
						  Department Name: <input type="text" name="dept" id="insertDept"><br/>
							Model Name: <input type="text" name="model" id="insertModel"><br/>
							Cost: <input type="number" name="cost" id="insertCost"><br/>

							<h3>
							<select name="parts" multiple id="insertParts" form="insertForm">
								<%
									try{
										DatabaseController controller = new DatabaseController();
							  		controller.Open();

										List<Pair<Integer, String>> models = controller.getLuxuryParts();

										for (Pair<Integer, String> model : models){
											out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
										}

										controller.Close();
									}
									catch(Exception ex){

									}
								%>
							</select>

							</div>
						  <input type="submit" value="Submit" />
						</form>
<!--
				</div>
				<div class="column">
					<h3>Update</h3>
					<select name="id" id="update" onchange="UpdateSelect()" form="updateForm">
					  <option selected disabled>Select model to update</option>
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

							}
						%>
					</select>
					<br/><br/>
					<form action="crud.jsp" method="post" onsubmit="return validateUpdate()" id="updateForm">
						<input type="hidden" name="operation" value="update">
						<input type="hidden" name="table" value="DepartmentModel">
						<div class="data">
						Department Name: <input type="text" name="dept" id="updateDept"><br/>
						Model Name: <input type="text" name="model" id="updateModel"><br/>
						Cost: <input type="number" name="cost" id="updateCost"><br/>
						</div>
						<br/>

						<select multiple name="parts" multiple id="updateParts" form="updateForm">
							<option value="9">fuzzy rearview mirror dice</option>
							<option value="10">infinite improbability drive</option>
							<option value="11">cosmic catalytic converter</option>
							<option value="12">wine bar</option>
							<option value="13">eternal Christmas decor</option>
							<option value="14">faux fireplace with mantle</option>
							<option value="15">teleporter</option>
							<option value="16">snow machine</option>
							<option value="17">washing machine and dryer</option>
							<option value="18">grand piano</option>
							<option value="19">petting zoo</option>
							<option value="20">helicopter blades</option>
							<option value="21">greenhouse garden</option>
							<option value="22">laser cannon</option>
						</select>

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

								List<Pair<Integer, String>> models = controller.getModels();

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
						<input type="hidden" name="table" value="DepartmentModel">
						<input type="submit" value="Submit" />
					</form>
				</div>
			-->
			</div>

    </div>

  </center>
	</body>
</html>

<script>

function CheckLuxuryCount(select){

	var count = 0;
	for (var i = 0; i < select.length; i++){
		if (select[i].selected) count++;
	}

	if (count >= 3 && count <= 10){
		return true;
	}

	return false;
}

function validateInsert(){
	if (CheckLuxuryCount(document.getElementById("insertParts"))){
		if (document.getElementById("insertDept").value != ""){
			if (document.getElementById("insertModel").value != ""){
				if (document.getElementById("insertCost").value != ""){
					if (document.getElementById("insertID").value > "0"){
						return true;
					}
				}
			}
		}
	}

	return false;
}

function validateUpdate(){
	if (CheckLuxuryCount(document.getElementById("updateParts"))){
		if (document.getElementById("updateDept").value != ""){
			if (document.getElementById("updateModel").value != ""){
				if (document.getElementById("updateCost").value != ""){
					if (document.getElementById("update").selectedIndex > 0){
						return true;
					}
				}
			}
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
