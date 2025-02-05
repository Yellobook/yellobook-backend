CREATE TABLE IF NOT EXISTS members
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname      VARCHAR(20)  NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    role          VARCHAR(10)  NOT NULL
        CHECK (role IN ('ADMIN', 'USER')),
    allowance     BOOLEAN      NOT NULL,
    profile_image VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP
);


CREATE TABLE IF NOT EXISTS teams
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(40)  NOT NULL UNIQUE,
    phone_number VARCHAR(20)  NOT NULL,
    address      VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS informs
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT  NOT NULL,
    team_id    BIGINT  NOT NULL,
    title      VARCHAR(255),
    content    VARCHAR(200),
    date       DATE    NOT NULL,
    view       INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_informs_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_informs_team FOREIGN KEY (team_id) REFERENCES teams (id)
);


CREATE TABLE IF NOT EXISTS inform_comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    inform_id  BIGINT       NOT NULL,
    member_id  BIGINT       NOT NULL,
    content    VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inform_comments_inform FOREIGN KEY (inform_id) REFERENCES informs (id),
    CONSTRAINT fk_inform_comments_member FOREIGN KEY (member_id) REFERENCES members (id)
);


CREATE TABLE IF NOT EXISTS inform_mentions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    inform_id  BIGINT NOT NULL,
    member_id  BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inform_mentions_inform FOREIGN KEY (inform_id) REFERENCES informs (id),
    CONSTRAINT fk_inform_mentions_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT uq_inform_mentions_member_inform UNIQUE (member_id, inform_id)
);


CREATE TABLE IF NOT EXISTS inventories
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id    BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    view       INTEGER      NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventories_team FOREIGN KEY (team_id) REFERENCES teams (id)
);


CREATE TABLE IF NOT EXISTS participants
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    team_id    BIGINT       NOT NULL,
    role       VARCHAR(255) NOT NULL
        CHECK (role IN ('ADMIN', 'ORDERER', 'VIEWER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_participants_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_participants_team FOREIGN KEY (team_id) REFERENCES teams (id),
    CONSTRAINT uq_participant_team_member UNIQUE (team_id, member_id)
);


CREATE TABLE IF NOT EXISTS products
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventory_id   BIGINT      NOT NULL,
    name           VARCHAR(30) NOT NULL,
    sub_product    VARCHAR(30),
    sku            INTEGER     NOT NULL,
    amount         INTEGER     NOT NULL,
    purchase_price INTEGER     NOT NULL,
    sale_price     INTEGER     NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_inventory FOREIGN KEY (inventory_id) REFERENCES inventories (id),
    CONSTRAINT uq_inventory_sku UNIQUE (inventory_id, sku)
);


CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT  NOT NULL,
    product_id   BIGINT,
    team_id      BIGINT,
    date         DATE    NOT NULL,
    order_amount INTEGER NOT NULL,
    view         INTEGER NOT NULL,
    memo         VARCHAR(200),
    order_status VARCHAR(255)
        CHECK (order_status IN ('PENDING_CONFIRM', 'PENDING_MODIFY', 'CONFIRMED')),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_orders_team FOREIGN KEY (team_id) REFERENCES teams (id)
);


CREATE TABLE IF NOT EXISTS order_comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    order_id   BIGINT       NOT NULL,
    content    VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_comments_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_order_comments_order FOREIGN KEY (order_id) REFERENCES orders (id)
);


CREATE TABLE IF NOT EXISTS order_mentions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_mentions_member FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_order_mentions_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT uq_order_mentions_member_order UNIQUE (member_id, order_id)
);