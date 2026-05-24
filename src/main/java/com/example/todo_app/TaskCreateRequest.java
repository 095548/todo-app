package com.example.todo_app;

import java.time.LocalDateTime;

public record TaskCreateRequest(String title, LocalDateTime deadlineAt) {
}
