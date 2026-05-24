package com.example.todo_app;

import java.time.LocalDateTime;

public record TaskUpdateDeadlineRequest(LocalDateTime deadlineAt) {
}
