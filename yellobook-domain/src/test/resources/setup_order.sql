-- 팀 데이터 삽입
INSERT INTO teams (id, name, phone_number, address, created_at, updated_at)
VALUES
    (1, '팀 A', '010-1234-5678', '서울특별시', '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (2, '팀 B', '010-1234-5679', '서울특별시', '2024-07-25 10:00:00', '2024-07-25 10:00:00');

-- 회원 데이터 삽입
INSERT INTO members (id, nickname, email, profile_image, role, allowance, created_at, updated_at)
VALUES
    (1, '사용자1', 'user1@example.com', 'profile1.png', 'USER', true, '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (2, '사용자2', 'user2@example.com', 'profile2.png', 'USER', true, '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (3, '사용자3', 'user3@example.com', 'profile3.png', 'USER', true, '2024-07-25 10:00:00', '2024-07-25 10:00:00');

-- 팀별 권한 데이터 삽입
INSERT INTO participants (id, team_id, member_id, role, created_at, updated_at)
VALUES
    (1, 1, 1, 'ADMIN', '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (2, 1, 2, 'ORDERER', '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (3, 1, 3, 'VIEWER', '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (4, 2, 1, 'ORDERER', '2024-07-25 10:00:00', '2024-07-25 10:00:00');

INSERT INTO inventories (id, team_id, title, view, created_at, updated_at)
VALUES
    (1, 1, '2024년 07월 25일 재고현황', 10, '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (2, 1, '2024년 07월 25일 재고현황', 5, '2024-07-25 10:00:00', '2024-07-25 10:00:00');

-- 제품 데이터 삽입
INSERT INTO products (id, name, sub_product, sku, purchase_price, sale_price, amount, inventory_id, created_at, updated_at)
VALUES
    (1, '제품 A', '서브제품1', 1001, 500, 700, 50, 1, '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
    (2, '제품 B', '서브제품2', 1002, 400, 600, 30, 1, '2024-07-25 10:00:00', '2024-07-25 10:00:00');

-- 주문 데이터 삽입
INSERT INTO orders (id, view, memo, date, order_status, order_amount, product_id, member_id, team_id, created_at, updated_at)
VALUES
    (1, 10, '메모1', '2024-07-26', 'CONFIRMED', 10, 1, 2, 1, '2024-07-25 10:00:01', '2024-07-25 10:00:01'),
    (2, 20, '메모2', '2024-07-27', 'CONFIRMED', 5, 1, 2, 1, '2024-07-25 10:01:02', '2024-07-25 10:01:02'),
    (3, 30, '메모3', '2024-07-28', 'PENDING_CONFIRM', 20, 2, 2, 1, '2024-07-25 10:02:03', '2024-07-25 10:02:03');

INSERT INTO order_comments(id, content, member_id, order_id, created_at, updated_at)
VALUES
    (1, '댓글1', 1, 1, '2024-07-25 10:00:01', '2024-07-25 10:00:01'),
    (2, '댓글2', 1, 1, '2024-07-25 10:01:02', '2024-07-25 10:01:02'),
    (3, '댓글3', 2, 1, '2024-07-25 10:02:03', '2024-07-25 10:02:03');

INSERT INTO order_mentions(id, member_id, order_id, created_at, updated_at)
VALUES
    (1, 1, 1, '2024-07-25 10:00:01', '2024-07-25 10:00:01');
