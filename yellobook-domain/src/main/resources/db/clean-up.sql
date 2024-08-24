-- 데이터 정리 (개발 환경에서만 사용)
DELETE FROM flyway_schema_history WHERE version = 2;

TRUNCATE TABLE inform_comments RESTART IDENTITY CASCADE;
TRUNCATE TABLE inform_mentions RESTART IDENTITY CASCADE;
TRUNCATE TABLE informs RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_comments RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_mentions RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE inventories RESTART IDENTITY CASCADE;
TRUNCATE TABLE teams RESTART IDENTITY CASCADE;
TRUNCATE TABLE members RESTART IDENTITY CASCADE;