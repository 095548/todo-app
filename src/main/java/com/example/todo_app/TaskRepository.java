package com.example.todo_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // これだけで、保存(save)、削除(delete)、ID検索(findById)などが使えます！
}