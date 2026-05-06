-- ====================================================
-- V1: tasks テーブルの作成
-- ====================================================
CREATE TABLE tasks (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    deadline_at TIMESTAMP,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    CONSTRAINT chk_tasks_status
        CHECK (status IN ('NOT_STARTED', 'DOING', 'COMPLETED', 'EXPIRED'))
);

CREATE INDEX idx_tasks_deadline_at ON tasks (deadline_at);
CREATE INDEX idx_tasks_status ON tasks (status);