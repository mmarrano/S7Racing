<?php
	// For password encryption
	require_once('password.php');

	// Make connection to database
	$connection = mysqli_connect('mysql.cs.iastate.edu','dbu309gpb4','cS!Ve2xE','db309gpb4')
 	or die('Error connecting to MySQL server.');

	// Parameters
	$username = $_POST['username'];
	$password = $_POST['password'];

	// Initialize our response
	$response = array();
	$response['success'] = false;
	$response['username'] = "";

	// Prepare, bind, execute, and store mysql
	$stmt = mysqli_prepare($connection, "SELECT password 
										 FROM user 
										 WHERE username = ?");
	mysqli_stmt_bind_param($stmt, 's', $username);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_store_result($stmt);
	mysqli_stmt_bind_result($stmt, $dbpassword_hash); // Don't forget to add highscore and color here if we decide to do those

	// Fetching information from the database
	while(mysqli_stmt_fetch($stmt)){
		// Check to see if the given password matches the encrypted one
		if(password_verify($password, $dbpassword_hash)){
			$response['username'] = $username;
			$response['success'] = true;
		}
	}

	// Sending information back to main app
	echo json_encode($response);

	// Close the connection
	mysqli_close($connection);
?>