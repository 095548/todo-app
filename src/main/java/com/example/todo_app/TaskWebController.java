package com.example.todo_app;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller // ★ここが @RestController ではなく @Controller になります！
@RequiredArgsConstructor
public class TaskWebController {

    private final TaskService taskService;

    // ブラウザで "http://localhost:8080/web/tasks" にアクセスした時の処理
    @GetMapping("/web/tasks")
    public String showTaskList(Model model) {
        // 1. Serviceに頼んで、DBからタスク一覧をもらってくる
        List<Task> tasks = taskService.findAll();

        // 2. 画面（HTML）に渡すために、"tasks" という名前の箱に入れる
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskCreateForm", new TaskCreateForm());

        // 3. "tasks.html" という画面を表示しなさい、という指示
        // （Strutsの return mapping.findForward("tasks") と同じです！）
        return "tasks";
    }

    @PostMapping("/web/tasks")
    public String createTask(
            // @Validated をつけると、TaskCreateForm に書いたチェック（@NotBlankなど）が発動します！
            @org.springframework.validation.annotation.Validated TaskCreateForm form,
            // BindingResult には、チェックした「結果（エラーがあったかどうか）」が入ります
            org.springframework.validation.BindingResult bindingResult,
            Model model
    ) {
        // 1. もし入力エラー（タイトルが空など）があったら？
        if (bindingResult.hasErrors()) {
            // エラーがある場合は登録せず、もう一度一覧データを取得して、元の画面に戻す
            List<Task> tasks = taskService.findAll();
            model.addAttribute("tasks", tasks);
            return "tasks"; // tasks.html を再表示
        }
        // 1. Serviceを呼んでDBに保存する
        taskService.createTask(form.getTitle(), form.getDeadlineAt());

        // 2. 保存が終わったら、一覧画面(GET)に「リダイレクト（転送）」する
        return "redirect:/web/tasks";
    }

    @GetMapping("/web/tasks/bomb")
    public String triggerError() {
        // わざと「予期せぬエラー（RuntimeException）」を投げる！
        throw new RuntimeException("データベースが爆発しました！！！");
    }
}