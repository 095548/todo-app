package com.example.todo_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskService taskService;

    @Test
    void タスク一覧取得APIが200を返す() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void タスク作成APIが201を返す() throws Exception {
        String requestBody = """
            {
                "title": "テストタスク",
                "deadlineAt": "2027-01-01T00:00:00"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void タスク削除APIが204を返す() throws Exception {

        Task task = taskService.createTask("削除されるタスク", LocalDateTime.of(2027, 1, 1, 0, 0));

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isNoContent());
    }
}