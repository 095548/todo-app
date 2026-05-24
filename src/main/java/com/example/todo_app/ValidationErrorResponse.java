package com.example.todo_app;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(LocalDateTime timestamp, int status, String message, String path, List<FieldError> errors ) {
}
