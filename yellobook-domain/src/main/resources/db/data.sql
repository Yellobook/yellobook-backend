-- 테이블 초기화 (TRUNCATE)
TRUNCATE TABLE order_comments RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_mentions RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE inventories RESTART IDENTITY CASCADE;
TRUNCATE TABLE teams RESTART IDENTITY CASCADE;
TRUNCATE TABLE members RESTART IDENTITY CASCADE;

-- 애플리케이션 사용자
INSERT INTO members (nickname, email, profile_image, role, allowance, created_at, updated_at)
VALUES ('사용자1', 'test1@gmail.com', 'profile1.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       ('사용자2', 'test2@gmail.com', 'profile2.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       ('사용자3', 'test3@gmail.com', 'profile3.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 팀
INSERT INTO teams (name, phone_number, address, created_at, updated_at)
VALUES ('팀1', '123456789', '서울특별시', '2024-08-17 09:00:00', '2024-08-17 09:00:00'),
       ('팀2', '123456789', '서울특별시', '2024-08-17 09:00:00', '2024-08-17 09:00:00');

-- 팀 멤버
INSERT INTO participants (team_id, member_id, role, created_at, updated_at)
VALUES (1, 1, 'ADMIN', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (1, 2, 'ORDERER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (1, 3, 'VIEWER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (2, 1, 'ORDERER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (2, 2, 'VIEWER', '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 재고
INSERT INTO inventories (team_id, title, view, created_at, updated_at)
VALUES (1, '2024년 08월 17일 재고현황', 57, '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 상품
INSERT INTO products (inventory_id, name, sub_product, sku, purchase_price, sale_price, amount, created_at, updated_at)
VALUES (1, '상품1', '서브제품1', 1001, 100000, 150000, 50, '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (1, '상품2', '서브제품2', 1002, 80000, 139000, 100, '2024-08-17 10:15:00', '2024-08-17 10:15:00'),
       (1, '상품3', '서브제품3', 1010, 15000, 25000, 100, '2024-08-17 10:30:00', '2024-08-17 10:30:00'),
       (1, '상품4', '서브제품4', 1011, 40000, 60000, 100, '2024-08-17 10:45:00', '2024-08-17 10:45:00');

-- 주문
INSERT INTO orders (view, memo, date, order_status, order_amount, product_id, member_id, team_id, created_at, updated_at)
VALUES (10, '주문1', '2024-08-17', 'CONFIRMED', 50, 1, 2, 1, '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 관리자 언급
INSERT INTO order_mentions (member_id, order_id, created_at, updated_at)
VALUES (1, 1, '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 주문 댓글
INSERT INTO order_comments (content, member_id, order_id, created_at, updated_at)
VALUES
    ('주문 댓글1', 1, 1, '2024-08-17 10:15:00', '2024-08-17 10:15:00'),
    ('주문 댓글2', 2, 1, '2024-08-02 11:15:00', '2024-08-02 11:15:00');




