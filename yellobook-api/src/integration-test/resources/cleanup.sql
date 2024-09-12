-- 공지 및 업무 멘션 데이터 삭제
DELETE FROM inform_mentions;
ALTER TABLE inform_mentions ALTER COLUMN id RESTART WITH 1;

-- 주문 댓글 데이터 삭제
DELETE FROM order_comments;
ALTER TABLE order_comments ALTER COLUMN  id RESTART WITH 1;

-- 공지 및 업무 댓글 데이터 삭제
DELETE FROM inform_comments;
ALTER TABLE inform_comments ALTER COLUMN  id RESTART WITH 1;

-- 주문 멘션 데이터 삭제
DELETE FROM order_mentions;
ALTER TABLE order_mentions ALTER COLUMN id RESTART WITH 1;

-- 공지 및 업무 데이터 삭제
DELETE FROM informs;
ALTER TABLE informs ALTER COLUMN id RESTART WITH 1;

-- 주문 데이터 삭제
DELETE FROM orders;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;

-- 제품 데이터 삭제
DELETE FROM products;
ALTER TABLE products ALTER COLUMN id RESTART WITH 1;

-- 팀 멤버 연관 데이터 삭제
DELETE FROM participants;
ALTER TABLE participants ALTER COLUMN id RESTART WITH 1;

-- 회원 데이터 삭제
DELETE FROM members;
ALTER TABLE members ALTER COLUMN id RESTART WITH 1;

-- 재고현황 데이터 삭제
DELETE FROM inventories;
ALTER TABLE inventories ALTER COLUMN id RESTART WITH 1;

-- 팀 데이터 삭제
DELETE FROM teams;
ALTER TABLE teams ALTER COLUMN id RESTART WITH 1;
