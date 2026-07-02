-- protospace-d ローカルDB セットアップスクリプト
-- 実行方法: sudo -u postgres psql -f setup_db.sql
--
-- ※ DB名・ユーザー・パスワードは
--    src/main/resources/application.properties と完全に一致させること

-- 開発用DB
CREATE DATABASE protospace_development;

-- テスト用DB
CREATE DATABASE protospace_test;

-- ユーザー作成
CREATE USER protospace WITH PASSWORD 'password';

-- DBへの権限付与
GRANT ALL PRIVILEGES ON DATABASE protospace_development TO protospace;

GRANT ALL PRIVILEGES ON DATABASE protospace_test TO protospace;

-- PostgreSQL 15+ で必要: public スキーマへの権限
-- （これが無いと Flyway のテーブル作成が権限不足で失敗する）
\c protospace_development
GRANT ALL ON SCHEMA public TO protospace;

\c protospace_test
GRANT ALL ON SCHEMA public TO protospace;