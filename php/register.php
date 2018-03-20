<?php
	// For password encryption
	require_once('password.php');

	// Make connection to database
	$connection = mysqli_connect('mysql.cs.iastate.edu','dbu309gpb4','cS!Ve2xE','db309gpb4')
	or die('Error connecting to MySQL server.');

	// Parameters
	$username = $_POST['username'];
	$password = $_POST['password'];

	$zero = 0;

	// Initialize our response
	$response = array();
	$response['success'] = false;
	$response['username_err'] = false;
	$response['password_err'] = false;
	$response['username_empty_err'] = false;
	$response['password_empty_err'] = false;
	$response['duplicate_username_err'] = false;

	// Check to see if the username or password is empty
	$empty_check = false;
	if(strlen($username) == 0){
		$response['username_empty_err'] = true;
		$empty_check = true;
	}
	if(strlen($password) == 0){
		$response['password_empty_err'] = true;
		$empty_check = true;
	}

	// Check to see if the username or password are too long
	$length_check = false;
	if(strlen($username) > 15){
		$response['username_err'] = true;
		$length_check = true;
	}
	if(strlen($password) > 15){
		$response['password_err'] = true;
		$length_check = true;
	}

	// Send false success response if username or password is too long
	if($length_check || $empty_check){
		echo json_encode($response);
		return;
	}

	// Do we need to check if the username created is a duplicate? Or are we not going to worry about that
	$stmt = mysqli_prepare($connection, "SELECT username
										 FROM user
										 WHERE username = ?");
	mysqli_stmt_bind_param($stmt, 's', $username);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_store_result($stmt);

	$num_rows = mysqli_stmt_num_rows($stmt);
	//mysqli_stmt_close($stmt);

	if($num_rows == 0){
		// Password encryption
		$password_hash = password_hash($password, PASSWORD_BCRYPT);

		// Prepare, bind, and execute mysql
		$stmt = mysqli_prepare($connection, "INSERT INTO user(username, password, wins) 
											 VALUES (?,?,?)");
		mysqli_stmt_bind_param($stmt, 'ssi', $username, $password_hash, $zero);
		mysqli_stmt_execute($stmt);

		// Send back a success
		$response['success'] = true;	
	} else{
		$response['duplicate_username_err'] = true;
	}

	// Sending a success message back to the main app
	echo json_encode($response);

	// Close the connection
	mysqli_close($connection);
?>