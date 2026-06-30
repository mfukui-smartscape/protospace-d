-- users テーブル
-- User エンティティに対応。
-- email は一意制約あり（要件: メールアドレスは一意性である）
CREATE TABLE users (
    id          BIGSERIAL    PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,            -- BCryptハッシュを保存
    name        VARCHAR(255) NOT NULL,
    profile     TEXT         NOT NULL,
    affiliation VARCHAR(255) NOT NULL,
    position    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
