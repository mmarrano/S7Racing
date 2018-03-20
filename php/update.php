<?php
	// Make connection to database
	$connection = mysqli_connect('mysql.cs.iastate.edu','dbu309gpb4','cS!Ve2xE','db309gpb4')
 	or die('Error connecting to MySQL server.');

	// Parameters
	$username = $_POST['username']; // string
	$win = $_POST['result']; // string

	// Initialize our response
	$response = array();
	$response['success'] = false;
	$response['wins'] = 0;
	$response['username'] = $username;

	// Prepare, bind, execute, and store mysql
	$stmt = mysqli_prepare($connection, "SELECT wins 
										 FROM user
										 WHERE username = ?");
	mysqli_stmt_bind_param($stmt, 's', $username);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_store_result($stmt);
	//$response['stmt'] = mysqli_stmt_num_rows($stmt);
	mysqli_stmt_bind_result($stmt, $dbwins);

	// Fetching information from the database
	while(mysqli_stmt_fetch($stmt)){
		$dbwins++;
		$response['wins'] = $dbwins;

		//mysqli_stmt_close($stmt);

		$stmt = mysqli_prepare($connection, "UPDATE user
		 									 SET wins = ?
		 									 WHERE username = ?");
		mysqli_stmt_bind_param($stmt, 'is', $dbwins, $username);
		mysqli_stmt_execute($stmt);

		$response['success'] = true;
	}

	// Sending information back to main app
	echo json_encode($response);

	// Close the connection
	mysqli_close($connection);
?>