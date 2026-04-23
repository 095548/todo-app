package com.example.todo_app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j // ログ（エラーの記録）を出すためのLombokの魔法
@ControllerAdvice // ★重要：すべてのControllerを見張る「網」になります
public class GlobalExceptionHandler {

    // Exception.class（すべての予期せぬエラー）が発生したら、このメソッドが呼ばれる！
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        // 1. 開発者のために、裏側（コンソール）には詳細なエラーログを残す
        log.error("システムエラーが発生しました", ex);

        // 2. ユーザーの画面（HTML）に渡すための、優しいメッセージを準備する
        model.addAttribute("errorMessage", "予期せぬエラーが発生しました。しばらく経ってから再度お試しください。");

        // 3. "error.html" という専用のエラー画面を表示する
        return "error";
    }
}
