<?php

class Movie {
    public function read($id) {
        $sql = "SELECT * FROM data.movie WHERE id=:id";

        $stmt = $this->getDb()->prepare($sql);  
        $stmt->bindParam("id", $id);
        $stmt->execute();
        $row = $stmt->fetchObject();  

        return $row;
    }

    function getDb()
    {
        $postgresqlConfig = include("conf.php");
        
        $username = $postgresqlConfig["username"];
        $password = $postgresqlConfig["password"];
        $hostname = $postgresqlConfig["hostname"];
        $port = $postgresqlConfig["port"];
        $name = $postgresqlConfig["name"];

        $db = new PDO("pgsql:host={$hostname};port=${port};dbname={$name};user={$username};password={$password}");

        return $db;
    }
}
