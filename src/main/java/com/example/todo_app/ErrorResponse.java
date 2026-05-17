package com.example.todo_app;

import java.time.LocalDateTime;

public record ErrorResponse (LocalDateTime timestamp, int status, String message, String path) {
}
