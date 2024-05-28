<?php
ini_set('display_errors', '1');
$servername = "localhost";
$username = "root";
$password = "";
$conn = new mysqli($servername, $username, $password);
if ($conn->connect_error) {
    die("Could not connect to server: ".$conn->connect_error);
}
// Create database
$sql = "CREATE DATABASE IF NOT EXISTS mosnad";
if ($conn->query($sql) == FALSE) {
    die("Error creating database: " . $conn->error);
}
$database = "mosnad";
$conn = new mysqli($servername, $username, $password, $database);
if ($conn->connect_error) {
    die("Could not connect to database: ".$conn->connect_error);
}
// sql to create tables
$sql = "CREATE TABLE IF NOT EXISTS users (
    email VARCHAR(255) PRIMARY KEY,
    phone VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'client'
)";

if ($conn->query($sql) === FALSE) {
    die ("Error creating tables: " . $conn->error);
}

// add admin user
$sql = "INSERT IGNORE INTO users (email, phone, name, password, role) 
VALUES ('admin@gmail.com', '777777777', 'Admin', '".md5('admin')."', 'admin')";

if ($conn->query($sql) === FALSE) {
    die ("Error adding admin account: " . $conn->error);
}

?>