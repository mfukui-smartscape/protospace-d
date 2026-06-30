-- prototypes テーブル
-- Prototype エンティティに対応。
-- user_id は users への外部キー。投稿者が削除されたら投稿も削除（CASCADE）。
CREATE TABLE prototypes (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,            -- プロトタイプ名称
    catch_copy  VARCHAR(255) NOT NULL,            -- キャッチコピー
    concept     TEXT         NOT NULL,            -- コンセプト
    image_name  VARCHAR(255) NOT NULL,            -- 画像ファイル名（実体は別管理）
    user_id     BIGINT       NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prototypes_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- 一覧表示・ユーザー詳細での絞り込み用
CREATE INDEX idx_prototypes_user_id ON prototypes (user_id);
