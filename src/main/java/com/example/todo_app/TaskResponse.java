package com.example.todo_app;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        LocalDateTime deadlineAt,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

      public static TaskResponse from(Task task){
          return new TaskResponse(
                  task.getId(),
                  task.getTitle(),
                  task.getDeadlineAt(),
                  task.getStatus(),
                  task.getCreatedAt(),
                  task.getUpdatedAt()
          );
      }
}
