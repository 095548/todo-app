package com.example.todo_app;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskCreateRequest(
        @NotBlank
        @Size(max = 255)
        String title,
        @NotNull
        @FutureOrPresent
        LocalDateTime deadlineAt) {
}
