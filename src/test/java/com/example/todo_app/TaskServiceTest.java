package com.example.todo_app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    @Test
    void タスクの期限を変更できる(){

        Task created = taskService.createTask("期限変更", LocalDateTime.of(2027, 1, 1, 0, 0));

        Task result = taskService.updateDeadline(created.getId(), LocalDateTime.of(2028, 2, 2, 0, 0));

        assertThat(result.getDeadlineAt()).isEqualTo(LocalDateTime.of(2028, 2, 2, 0, 0));
    }

    @Test
    void 存在しないIDで完了しようとすると例外が出る() {
        // Arrange
        Long Id = 9999L;

        // Act & Assert
        assertThatThrownBy(() -> taskService.completeTask(Id))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @Transactional
    void N1問題を観察する() {

        Task task1 = taskService.createTask("タスク1", LocalDateTime.of(2027, 1, 1, 0, 0));
        Task task2 = taskService.createTask("タスク2", LocalDateTime.of(2027, 1, 1, 0, 0));

        createComment("タスク1へのコメントA", task1);
        createComment("タスク1へのコメントB", task1);
        createComment("タスク2へのコメントA", task2);
        createComment("タスク2へのコメントB", task2);

        entityManager.flush();
        entityManager.clear();

        System.out.println("===== ここから全コメント取得 =====");

        // Act：全コメントを取得
        List<Comment> comments = commentRepository.findAll();

        System.out.println("===== コメント取得完了。これから各コメントの親タスクを触る =====");

        // 各コメントの親タスクのタイトルを触る（ここでN+1が起きる）
        for (Comment c : comments) {
            System.out.println("コメント: " + c.getContent() + " / 親タスク: " + c.getTask().getTitle());
        }

        System.out.println("===== ループ終了 =====");
    }

    private void createComment(String content, Task task) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTask(task);
        commentRepository.save(comment);
    }
}