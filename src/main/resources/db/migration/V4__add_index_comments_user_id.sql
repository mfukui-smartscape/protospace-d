-- ユーザー別コメント一覧の絞り込み用
CREATE INDEX idx_comments_user_id ON comments (user_id);