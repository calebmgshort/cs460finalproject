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

			<h2><u>Ships</u></h2>

			<div class="row">
				<div class="column">
					<h3>Order Ship</h3>

						<form action="crud.jsp" method="post" onsubmit="return validateInsert()" id="insertForm">
							<div class="data">
							<input type="hidden" name="operation" value="insert">
							<input type="hidden" name="table" value="Ship">
							Ship ID: <input type="number" min="0" name="id" id="insertId"><br/>
						  Customer ID: <input type="number" min="0" name="custNum" id="insertCustNum"><br/>

							<select name="modelNum" id="insertModelNum" form="insertForm">
							  <option selected disabled>Select model to order</option>
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
							<br/>
							</div>
						  <input type="submit" value="Submit" />
						</form>

				</div>
				<div class="column">
					<h3>Construct Ship</h3>
					<select name="id" id="update" onchange="UpdateSelect()" form="updateForm">
					  <option selected disabled>Select ship to work on</option>
						<%
							try{
								DatabaseController controller = new DatabaseController();
					  		controller.Open();
/*
								List<Pair<Integer, String>> models = controller.getShips();

								for (Pair<Integer, String> model : models){
									out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
								}
*/
								controller.Close();
							}
							catch(Exception ex){

							}
						%>
					</select>
					<br/><br/>
					<form action="crud.jsp" method="post" onsubmit="return validateUpdate()" id="updateForm">
						<input type="hidden" name="operation" value="update">
						<input type="hidden" name="table" value="Ship">
						<div class="data">

							Price: <input type="text" name="price" id="updatePrice"><br/>
							Quantity: <input type="number" name="quantity" id="updateQuantity"><br/>

						</div>
						<br/><br/>
						<input type="submit" value="Submit" />
					</form>
				</div>

				<div class="column">
					<h3>Delete</h3>
					<select name="id" id="delete" onchange="deleteSelect()" form="deleteForm">
					  <option selected disabled>Select ship to scrap</option>
						<%
							try{
								DatabaseController controller = new DatabaseController();
					  		controller.Open();
/*
								List<Pair<Integer, String>> models = controller.getShip();

								for (Pair<Integer, String> model : models){
									out.write("<option value="+model.getKey()+">"+model.getValue()+"</option>");
								}
*/
								controller.Close();
							}
							catch(Exception ex){

							}
						%>
					</select>
					<br/><br/>
					<form action="crud.jsp" method="post" onsubmit="return validateUpdate()" id="deleteForm">
						<input type="hidden" name="operation" value="delete">
						<input type="hidden" name="table" value="Ship">
						<input type="submit" value="Submit" />
					</form>
				</div>
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
	if (document.getElementById("insertCustNum").value != ""){
		if (document.getElementById("insertModelNum").value != ""){
			if (document.getElementById("insertID").value > "0"){
				return true;
			}
		}
	}


	return false;
}

function validateUpdate(){
		if (document.getElementById("updateDept").value != ""){
			if (document.getElementById("updateModel").value != ""){
				if (document.getElementById("updateCost").value != ""){
					if (document.getElementById("update").selectedIndex > 0){
						return true;
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
