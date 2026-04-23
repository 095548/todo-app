package com.example.todo_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // DB操作に関連する部品だけを起動する便利な設定
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void タスクが保存できること() {
        // 1. 準備 (Entityを作る)
        Task task = new Task();
        task.setTitle("テスト用のタスク");
        task.setStatus(TaskStatus.NOT_STARTED);

        // 2. 実行 (Repositoryで保存)
        Task savedTask = taskRepository.save(task);

        // 3. 検証 (正しく保存されたか確認)
        assertThat(savedTask.getId()).isNotNull(); // IDが自動採番されているか
        assertThat(savedTask.getTitle()).isEqualTo("テスト用のタスク");
        assertThat(savedTask.getCreatedAt()).isNotNull(); // 自動付与されるはず
    }
}