package com.example.todo_app;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskUpdateDeadlineRequest(
        @NotNull
        @FutureOrPresent
        LocalDateTime deadlineAt) {
}
