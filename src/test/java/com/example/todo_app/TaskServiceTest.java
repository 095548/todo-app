package com.example.todo_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    void タスクを作成できる() {
        // Arrange（準備）
        String title = "テストタスク";
        LocalDateTime deadline = LocalDateTime.of(2027, 1, 1, 0, 0);

        // Act（実行）
        Task result = taskService.createTask(title, deadline);

        // Assert（検証）
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("テストタスク");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.NOT_STARTED);
    }

    @Test
    void タスクを完了状態にできる(){

        Task created = taskService.createTask("完了テスト", LocalDateTime.of(2027, 1, 1, 0, 0));

        Task result = taskService.completeTask(created.getId());

        assertThat(result.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }
}