<?php

class UserRepository {
    private $connection;

    public function __construct($connection) {
        $this->connection = $connection;
    }

    public function findAll() {
        $sql = "SELECT * FROM users"; // Noncompliant {{Possible raw SQL statement SELECT * FROM users detected.}} {{Possible raw SQL statement SELECT * FROM users detected.}}
        return $this->connection->query($sql);
    }

    public function create($id, $name) {
        return $this->connection->query("INSERT INTO users VALUES ($id, '$name')"); // Noncompliant {{Possible raw SQL statement INSERT INTO users VALUES ($id, '$name') detected.}}
    }

    public function update($id, $name) {
        $sql =  "UPDATE users SET name = '$name' WHERE id = $id"; // Noncompliant {{Possible raw SQL statement UPDATE users SET name = '$name' WHERE id = $id detected.}}
        return $this->connection->query($sql);
    }

    public function delete($id) {
        $sql = "DELETE FROM users WHERE id = $id"; // Noncompliant {{Possible raw SQL statement DELETE FROM users WHERE id = $id detected.}}
        return $this->connection->query($sql);
    }

    public function safeMethod() {
        $message = "This is just some text with the word SELECT in it"; // Compliant
        return $message;
    }

    public function anotherSafeMethod() {
        $text = "Just some text that happens to contain SELECT somewhere"; //Compliant
        return $text;
    }
}