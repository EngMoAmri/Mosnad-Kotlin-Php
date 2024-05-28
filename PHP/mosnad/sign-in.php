<?php 
session_start();
include "database.php";
$_POST = json_decode(file_get_contents('php://input'), true);
$email = $_POST["email"];
$password = md5($_POST["password"]);
$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($result);
$output = array();

if (mysqli_num_rows($result) === 1) {
    $_SESSION["user-email"] = $row["email"];
    $_SESSION["user-name"] = $row["name"];
    $_SESSION["user-phone"] = $row["phone"];
    $_SESSION["user-role"] = $row["role"];
    
    $output['response'] = "true";
    $output['user-email'] = $row["email"];
    $output['user-name'] = $row["name"];
    $output['user-phone'] = $row["phone"];
    $output['user-role'] = $row["role"];
} else {
    $output['response'] = "false";
    $output['message'] = "no record found";
}

echo json_encode($output);
?>
