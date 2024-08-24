-- 테이블 초기화 (TRUNCATE)
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

-- 애플리케이션 사용자
INSERT INTO members (nickname, email, profile_image, role, allowance, created_at, updated_at)
VALUES ('사용자1', 'test1@gmail.com', 'profile1.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       ('사용자2', 'test2@gmail.com', 'profile2.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       ('사용자3', 'test3@gmail.com', 'profile3.jpg', 'USER', true, '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 팀 2개
INSERT INTO teams (name, phone_number, address, created_at, updated_at)
VALUES ('팀1', '123456789', '서울특별시', '2024-08-17 09:00:00', '2024-08-17 09:00:00'),
       ('팀2', '123456789', '서울특별시', '2024-08-17 09:00:00', '2024-08-17 09:00:00');

-- 팀 멤버
INSERT INTO participants (team_id, member_id, role, created_at, updated_at)
VALUES (1, 1, 'ADMIN', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (1, 2, 'ORDERER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (1, 3, 'VIEWER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       -- 팀2 에는 주문자와 뷰어만 존재
       (2, 1, 'ORDERER', '2024-08-17 10:00:00', '2024-08-17 10:00:00'),
       (2, 2, 'VIEWER', '2024-08-17 10:00:00', '2024-08-17 10:00:00');

-- 재고현황 1개
INSERT INTO inventories (team_id, title, view, created_at, updated_at)
VALUES (1, '2024년 08월 24일 재고현황', 57, '2024-08-24 10:00:00', '2024-08-24 10:00:00');

-- 상품 10개
DO
$$
    DECLARE
        i              INT;
        product_sku       INT := 1000;
        product_amount INT := 10000;
    BEGIN
        FOR i IN 1..10
            LOOP
                product_sku := product_sku + 1;
                INSERT INTO products (inventory_id, name, sub_product, sku, purchase_price, sale_price, amount,
                                      created_at, updated_at)
                VALUES (1, '상품' || i, '서브제품' || i, product_sku, 100000, 150000, product_amount, '2024-08-17 10:00:00',
                        '2024-08-17 10:00:00');
            END LOOP;
    END
$$;


DO
$$
    DECLARE
        i                  INT;
        j                  INT;
        first_day          DATE := date_trunc('month', current_date)::DATE;
        last_day           DATE := (date_trunc('month', current_date) + INTERVAL '1 month - 1 day')::DATE;
        -- 주문/공지 날짜 (년:월:일)
        schedule_day       DATE := first_day;
        -- 주문 시간 (기본값)
        base_time          TIME := TIME '10:00:00';
        -- 데이터베이스 삽입 시간
        created_at         TIMESTAMP;
        -- 주문 1건당 주문 상품 개수 (기본값)
        order_amount       INT  := 10;
        -- 누적 주문 개수
        order_cnt          INT  := 0;
        -- 누적 주문 댓글 개수
        order_comment_cnt  INT  := 0;
        -- 누적 공지 개수
        inform_cnt         INT  := 0;
        -- 누적 공지 댓글 개수
        inform_comment_cnt INT  := 0;
    BEGIN
        -- 주문과 공지 데이터 추가
        WHILE schedule_day <= last_day
            LOOP
                -- 주문 데이터 추가
                FOR i IN 1..10
                    LOOP
                        order_cnt := order_cnt + 1;
                        created_at := schedule_day + (base_time + INTERVAL '1 minute' * (order_cnt - 1));
                        INSERT INTO orders (view, memo, date, order_status, order_amount, product_id, member_id,
                                            team_id, created_at,
                                            updated_at)
                        VALUES (0, '주문' || order_cnt, schedule_day, 'CONFIRMED', order_amount, 1, 2, 1, created_at,
                                NULL);

                        -- 주문에 대한 관리자 언급 추가
                        INSERT INTO order_mentions (member_id, order_id, created_at, updated_at)
                        VALUES (1, order_cnt, schedule_day + TIME '10:00:00', schedule_day + TIME '10:00:00');

                        -- 주문 건당 관리자 댓글 5개, 주문자 댓글 5개
                        FOR j in 1..10
                            LOOP
                                order_comment_cnt := order_comment_cnt + 1;
                                INSERT INTO order_comments (content, member_id, order_id, created_at, updated_at)
                                VALUES ('주문자 댓글' || order_comment_cnt, 1, order_cnt,
                                        schedule_day + TIME '10:15:00', NULL);
                                order_comment_cnt := order_comment_cnt + 1;
                                INSERT INTO order_comments (content, member_id, order_id, created_at, updated_at)
                                VALUES ('관리자 댓글' || order_comment_cnt, 1, order_cnt,
                                        schedule_day + TIME '10:15:01', NULL);
                            END LOOP;
                    END LOOP;

                -- 공지 및 업무 데이터 추가
                FOR i IN 1..10
                    LOOP
                        inform_cnt := inform_cnt + 1;
                        created_at := schedule_day + (base_time + INTERVAL '1 minute' * (inform_cnt - 1));
                        INSERT INTO informs (title, content, view, date, member_id, team_id, created_at, updated_at)
                        VALUES ('공지 및 업무' || inform_cnt, '테스트 내용' || inform_cnt, inform_cnt * 100, schedule_day, 1, 1,
                                created_at, NULL);

                        -- 공지에 대한 주문자, 뷰어 언급
                        INSERT INTO inform_mentions (member_id, inform_id, created_at, updated_at)
                        VALUES (2, inform_cnt, schedule_day + TIME '11:00:00', NULL),
                               (3, inform_cnt, schedule_day + TIME '11:00:00', NULL);

                        -- 공지 건당 댓글 10개, 작성자 댓글 10개
                        FOR j IN 1..10
                            LOOP
                                inform_comment_cnt := inform_comment_cnt + 1;
                                INSERT INTO inform_comments (content, member_id, inform_id, created_at, updated_at)
                                VALUES ('작성자 댓글' || inform_comment_cnt, 1, inform_cnt,
                                        schedule_day + TIME '11:15:00', NULL);

                                inform_comment_cnt := inform_comment_cnt + 1;
                                INSERT INTO inform_comments (content, member_id, inform_id, created_at, updated_at)
                                VALUES ('관리자 댓글' || inform_comment_cnt, 2, inform_cnt,
                                        schedule_day + TIME '11:15:01', NULL);

                                inform_comment_cnt := inform_comment_cnt + 1;
                                INSERT INTO inform_comments (content, member_id, inform_id, created_at, updated_at)
                                VALUES ('뷰어 댓글' || inform_comment_cnt, 3, inform_cnt,
                                        schedule_day + TIME '11:15:02', NULL);
                            END LOOP;
                    END LOOP;
                schedule_day := schedule_day + INTERVAL '1 day';
            END LOOP;
    END
$$;