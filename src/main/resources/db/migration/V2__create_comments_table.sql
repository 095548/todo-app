-- ====================================================
-- V2: comments テーブルの作成
-- ====================================================
CREATE TABLE comments (
    id          BIGSERIAL    PRIMARY KEY,
    content     VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP,
    task_id     BIGINT       NOT NULL,
    CONSTRAINT fk_comments_task
        FOREIGN KEY (task_id) REFERENCES tasks (id)
);

CREATE INDEX idx_comments_task_id ON comments (task_id);