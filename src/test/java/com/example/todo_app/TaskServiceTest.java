package com.example.todo_app;

import jakarta.persistence.SqlResultSetMapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class) // 1. Mockitoを使ってテストするよ、という宣言
class TaskServiceTest {

    @Mock // 2. Repositoryの「偽物」を作る
    private TaskRepository taskRepository;

    @InjectMocks // 3. 偽物のRepositoryを、本物のServiceに注入する
    private TaskService taskService;

    @Test
    void タスクを登録できること() {
        // --- 準備 (Given) ---
        Task task = new Task();
        task.setTitle("テスト用タスク");
        task.setDeadlineAt(LocalDateTime.now().plusDays(1)); // 明日が期限
        task.setStatus(TaskStatus.NOT_STARTED);

        // 「Repositoryのsaveメソッドが呼ばれたら、このtaskを返してね」と偽物に指示しておく
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // --- 実行 (When) ---
        // まだ中身が空っぽの create メソッドを呼ぶ
        Task result = taskService.createTask("テスト用タスク", LocalDateTime.now().plusDays(1));


        // --- 検証 (Then) ---
        // 1. 戻り値がちゃんとあるか？
        assertThat(result).isNotNull();
        // 2. 戻ってきたタスクのタイトルは正しいか？
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDeadlineAt()).isEqualTo(LocalDateTime.now().plusDays(1));
        // 3. (重要) ServiceはちゃんとRepositoryのsaveを呼んだか？（サボっていないか？）
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void タスクの期限を変更できること(){
        // --- 準備 (Given) ---
        Long id = 1L;
        Task existingTask = new Task();
        existingTask.setId(id);
        existingTask.setTitle("既存のタスク");
        existingTask.setDeadlineAt(LocalDateTime.now()); // 古い期限

        // 1. 「IDで検索されたら、このタスクを返してね」と演技指導
        // Optional.of(...) は「値が入った箱」を作るメソッドです
        when(taskRepository.findById(id)).thenReturn(java.util.Optional.of(existingTask));

        // 2. 「保存されたら、そのタスクを返してね」と演技指導
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // --- 実行 (When) ---
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(5);
        Task updatedTask = taskService.updateDeadline(id, newDeadline);

        // --- 検証 (Then) ---
        // 期限が新しくなっているか？
        assertThat(updatedTask.getDeadlineAt()).isEqualTo(newDeadline);

        // タイトルが消えていないか？（これ大事！）
        assertThat(updatedTask.getTitle()).isEqualTo("既存のタスク");

        // ちゃんと検索と保存が1回ずつ呼ばれたか？
        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void タスクのステータスを完了にできること(){

        Long id = 2L;
        Task completetask = new Task();
        completetask.setId(id);
        completetask.setTitle("更新確認");
        completetask.setStatus(TaskStatus.NOT_STARTED);

        when(taskRepository.findById(id)).thenReturn(java.util.Optional.of(completetask));

        when(taskRepository.save(any(Task.class))).thenReturn(completetask);

        Task tmpcompletetask = taskService.completeTask(id);

        assertThat(tmpcompletetask.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(tmpcompletetask.getTitle()).isEqualTo("更新確認");

        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void タスクを全件取得できること() {
        // --- 準備 (Given) ---
        Task task1 = new Task();
        Task task2 = new Task();

        // 2つのタスクが入ったリストを作る
        List<Task> taskList = List.of(task1, task2);

        // 「findAll」と呼ばれたら、このリストを返してね
        when(taskRepository.findAll()).thenReturn(taskList);

        // --- 実行 (When) ---
        List<Task> result = taskService.findAll();

        // --- 検証 (Then) ---
        // 1. リストのサイズは2つか？
        assertThat(result).hasSize(2);

        // 2. RepositoryのfindAllをちゃんと呼んだか？
        verify(taskRepository, times(1)).findAll();
    }
}