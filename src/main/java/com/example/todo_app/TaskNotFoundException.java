package com.example.todo_app;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException (String messege) {
        super(messege);
    }
}
