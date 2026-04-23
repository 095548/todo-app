package com.example.todo_app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController // 1. ここは「窓口」ですよ、という宣言
@RequestMapping("/tasks") // 2. "http://localhost:8080/tasks" で受け付けます
@RequiredArgsConstructor

public class TaskController {

    private final TaskService taskService;
    /**
     * タスク一覧を取得する
     * [GET] /tasks
     */
    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    /**
     * タスクを登録する
     * [POST] /tasks
     * Body: { "title": "...", "deadlineAt": "..." }
     */
    @PostMapping
    public Task create(@RequestBody TaskForm form) {
        // 申込書(Form)からデータを取り出して、Serviceに依頼する
        return taskService.createTask(form.title(), form.deadlineAt());
    }

    /**
     * 期限を変更する
     * [PATCH] /tasks/{id}/deadline
     * Body: { "deadlineAt": "..." }
     */
    @PatchMapping("/{id}/deadline")
    public Task updateDeadline(@PathVariable Long id, @RequestBody TaskForm form) {
        return taskService.updateDeadline(id, form.deadlineAt());
    }

    /**
     * タスクを完了にする
     * [PUT] /tasks/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public Task complete(@PathVariable Long id) {
        return taskService.completeTask(id);
    }

    // --- 【重要】申込書の定義 (DTO) ---
    // ユーザーから送られてくるデータの形を定義します
    // Java 16以降なら「record」を使うと簡潔に書けます
    public record TaskForm(String title, LocalDateTime deadlineAt) {}
}
