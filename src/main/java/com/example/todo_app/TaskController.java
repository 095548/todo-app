package com.example.todo_app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public List<TaskResponse> findAll() {
        return taskService.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    /**
     * タスクを登録する
     * [POST] /tasks
     * Body: { "title": "...", "deadlineAt": "..." }
     */
    @PostMapping
    public TaskResponse create(@RequestBody TaskCreateRequest request) {
        Task task = taskService.createTask(request.title(), request.deadlineAt());
        return TaskResponse.from(task);
    }

    /**
     * 期限を変更する
     * [PATCH] /tasks/{id}/deadline
     * Body: { "deadlineAt": "..." }
     */
    @PatchMapping("/{id}/deadline")
    public TaskResponse updateDeadline(@PathVariable Long id, @RequestBody TaskUpdateDeadlineRequest request) {
        Task task = taskService.updateDeadline(id, request.deadlineAt());
        return TaskResponse.from(task);
    }

    /**
     * タスクを完了にする
     * [PUT] /tasks/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public TaskResponse complete(@PathVariable Long id) {

        Task task = taskService.completeTask(id);
        return TaskResponse.from(task);
    }

    /**
     * タスクを削除する
     * [DELETE] /tasks/{id}/delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
