package com.example.todo_app;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data // Getter, Setter, toStringなどを自動生成します
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // bigintはJavaではLongに対応します

    @Column(nullable = false)
    private String title;

    private LocalDateTime deadlineAt; // timestampはLocalDateTimeに対応します

    @Enumerated(EnumType.STRING) // DBには文字列（"DOING"など）で保存します
    @Column(nullable = false)
    private TaskStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
