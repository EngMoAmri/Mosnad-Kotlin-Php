<?php
session_start();
include "database.php";
$_POST = json_decode(file_get_contents('php://input'), true);
$email = $_POST["email"];
$name = $_POST["name"];
$phone = $_POST["phone"];
$password = md5($_POST["password"]);
// TODO if there is enough time add validation to confirm password and phone number
$role = 'client';
$sql = "SELECT * FROM users WHERE email = '$email'";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($result);
$output = array();

if (mysqli_num_rows($result) === 1) {
    $output['response'] = "false";
    $output['message'] = "user already exists with this email";
    echo json_encode($output);
}else{
    $sql = "SELECT * FROM users WHERE phone = '$phone'";
    $result = mysqli_query($conn, $sql);
    $row = mysqli_fetch_assoc($result);
    $output = array();
    
    if (mysqli_num_rows($result) === 1) {
        $output['response'] = "false";
        $output['message'] = "user already exists with this phone";
        echo json_encode($output);
    }else{
        $sql = "INSERT INTO users (email, name, phone, password, role) 
        VALUES ('$email', '$name', '$phone', '$password', '$role')";
        if (mysqli_query($conn, $sql) === true) {
            $_SESSION["user-email"] = $email;
            $_SESSION["user-name"] = $name;
            $_SESSION["user-phone"] = $phone;
            $_SESSION["user-role"] = $role;
            $output['response'] = "true";
            $output['user-email'] = $email;
            $output['user-name'] = $name;
            $output['user-phone'] = $phone;
            $output['user-role'] = $role;
        } else {
            $output['response'] = "false";
            $output['message'] = "Error creating account: " . mysqli_error($conn);
        }
        echo json_encode($output);
    }
}
?>
