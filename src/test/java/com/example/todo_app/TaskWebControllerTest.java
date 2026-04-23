package com.example.todo_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ★ポイント1：TaskWebController "だけ" をテストする特別なアノテーション
@WebMvcTest(TaskWebController.class)
public class TaskWebControllerTest {

    @Autowired
    private MockMvc mockMvc; // 全自動Postmanの正体！

    // ★ポイント2：Serviceの「影武者（モック）」を用意する（本物のDBには繋がない）
    @MockitoBean
    private TaskService taskService;

    @Test
    void タスク一覧画面が正常に表示されること() throws Exception {
        // 準備: 影武者に「findAllが呼ばれたら、空のリストを返しなさい」と指示
        when(taskService.findAll()).thenReturn(Collections.emptyList());

        // 実行 & 検証: 全自動でGETリクエストを送信！
        mockMvc.perform(get("/web/tasks"))
                .andExpect(status().isOk()) // 1. ステータスは 200 OK か？
                .andExpect(view().name("tasks")) // 2. "tasks.html" を返しているか？
                .andExpect(model().attributeExists("tasks")) // 3. Modelに "tasks" のデータが入っているか？
                .andExpect(model().attributeExists("taskCreateForm")); // 4. Modelにフォームが入っているか？
    }

    @Test
    void 正常な入力値ならタスクが登録されてリダイレクトすること() throws Exception {
        // 実行 & 検証: 正しいデータをPOSTで送信！
        mockMvc.perform(post("/web/tasks")
                        .param("title", "自動テストのタスク")
                        .param("deadlineAt", "2099-12-31T10:00:00"))
                .andExpect(status().is3xxRedirection()) // 1. 成功してリダイレクト（302など）されるか？
                .andExpect(redirectedUrl("/web/tasks")); // 2. 転送先URLは正しいか？
    }

    @Test
    void タイトルが空の場合はバリデーションエラーになり元の画面に戻ること() throws Exception {
        // 実行 & 検証: わざとタイトルを「空っぽ」にしてPOST送信！
        mockMvc.perform(post("/web/tasks")
                        .param("title", "") // ★ここが意地悪テスト！
                        .param("deadlineAt", "2099-12-31T10:00:00"))
                .andExpect(status().isOk()) // 1. リダイレクトされず 200 OK で留まるか？
                .andExpect(view().name("tasks")) // 2. "tasks.html" が再表示されるか？
                .andExpect(model().hasErrors()); // 3. Modelの中にエラー情報が含まれているか？
    }
}
