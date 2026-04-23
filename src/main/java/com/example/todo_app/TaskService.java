package com.example.todo_app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    // データベースと会話する係（Repository）を呼び出せるようにする
    private final TaskRepository taskRepository;

    /**
     * タスクの登録をする
     * @param title タイトル
     * @param deadline 期限
     * @return 保存されたタスク
     */
    @Transactional // データの変更があるメソッドに付ける
    public Task createTask(String title, LocalDateTime deadline) {
        Task task = new Task();
        task.setTitle(title);
        task.setDeadlineAt(deadline);
        task.setStatus(TaskStatus.NOT_STARTED);

        return taskRepository.save(task);
    }

    /**
     * 期限変更
     * @param id 変更したいタスクのID
     * @param newDeadline 新しい期限
     * @return 更新後のタスク
     */
    @Transactional
    public Task updateDeadline(Long id, LocalDateTime newDeadline) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定されたIDのタスクが見つかりません: " + id));
        task.setDeadlineAt(newDeadline);

        return taskRepository.save(task);
    }

    /**
     * 完了処理
     * @param id 完了にしたいタスクのID
     */
    @Transactional
    public Task completeTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定されたIDのタスクが見つかりません: " + id));
        task.setStatus(TaskStatus.COMPLETED);

        return taskRepository.save(task);
    }

    /**
     * 全件取得（一覧表示用）
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    // 論理削除は少し応用編なので、まずは上記の基本機能を作ってから実装しましょう！
}