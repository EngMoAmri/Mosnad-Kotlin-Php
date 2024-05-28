<?php
session_start();
include "database.php";
$_POST = json_decode(file_get_contents('php://input'), true);
$email = $_POST["email"];
$oldPassword = md5($_POST["password"]);
$newPassword = md5($_POST["newPassword"]);
$sql = "SELECT * FROM users WHERE email = '$email' and password = '$oldPassword'";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($result);
$output = array();

if (mysqli_num_rows($result) === 1) {
    $sql = "UPDATE users SET password = '$newPassword' WHERE email = '$email'";
    if (mysqli_query($conn, $sql) === true) {
        $output['response'] = "true";
        $output['message'] = "password changed successfully";
    } else {
        $output['response'] = "false";
        $output['message'] = "Error creating account: " . mysqli_error($conn);
    }
}else{
    
    $output['response'] = "false";
    $output['message'] = "old password not correct";
}
echo json_encode($output);
?>
