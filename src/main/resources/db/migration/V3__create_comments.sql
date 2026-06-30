-- comments テーブル
-- Comment エンティティに対応。
-- user_id（投稿者）と prototype_id（対象プロトタイプ）の2つの外部キーを持つ。
CREATE TABLE comments (
    id           BIGSERIAL PRIMARY KEY,
    content      TEXT      NOT NULL,              -- コメント内容
    user_id      BIGINT    NOT NULL,
    prototype_id BIGINT    NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_prototype
        FOREIGN KEY (prototype_id) REFERENCES prototypes (id) ON DELETE CASCADE
);

-- プロトタイプ詳細でのコメント絞り込み用
CREATE INDEX idx_comments_prototype_id ON comments (prototype_id);
