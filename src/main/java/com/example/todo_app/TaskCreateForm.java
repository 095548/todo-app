package com.example.todo_app;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data // Lombokの魔法：ゲッターやセッターを自動生成
public class TaskCreateForm {

    // @NotBlank: 「空っぽ」や「スペースだけ」を許さない！
    @NotBlank(message = "タイトルは必須です")
    private String title;

    // @NotNull: nullを許さない
    // @Future: 「現在より未来の日付」しか許さない！
    @NotNull(message = "期限は必須です")
    @Future(message = "期限は未来の日付を設定してください")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadlineAt;
}
