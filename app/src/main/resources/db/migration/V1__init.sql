create table if not exists users
(
    user_id    bigint       not null primary key,
    username   varchar(25)  not null,
    password   varchar(255) not null,
    user_type  varchar(16)  not null check (user_type in ('ROLE_USER', 'ROLE_ADMIN')),
    created_at TIMESTAMP(6) not null,
    updated_at TIMESTAMP(6) not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists product
(
    product_id  bigint       not null primary key,
    name        varchar(255) not null,
    description longtext     not null,
    price       varchar(255) not null,
    quantity    bigint       not null,
    start_time  TIMESTAMP(6) not null,
    end_time    TIMESTAMP(6) not null,
    created_at  TIMESTAMP(6) not null,
    updated_at  TIMESTAMP(6) not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists cart
(
    cart_id    bigint       not null primary key,
    user_id    bigint       not null unique,
    created_at TIMESTAMP(6) not null,
    updated_at TIMESTAMP(6) not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists cart_product
(
    cart_id    bigint not null references cart (cart_id),
    product_id bigint not null references product (product_id),
    quantity   bigint not null,
    primary key (cart_id, product_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists orders
(
    order_id    bigint       not null primary key,
    user_id     bigint       not null,
    total_price varchar(255) not null,
    order_type  varchar(10)  not null check (order_type in ('PENDING', 'SUCCESS', 'FAIL')),
    created_at  TIMESTAMP(6) not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists order_product
(
    order_id   bigint       not null references orders (order_id),
    product_id bigint       not null,
    name       varchar(255) not null,
    price      varchar(255) not null,
    quantity   bigint       not null,
    created_at TIMESTAMP(6) not null,
    primary key (order_id, product_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

create table if not exists payment
(
    payment_id      bigint         not null primary key,
    order_id        bigint         not null,
    user_id         bigint         not null,
    purchased_money numeric(38, 0) not null,
    pay_status      varchar(16)    not null check (pay_status in ('PENDING', 'SUCCESS', 'FAIL', 'CANCELED')),
    pay_type        varchar(16)    not null check (pay_type in ('CARD', 'DEPOSIT')),
    created_at      TIMESTAMP(6)   not null,
    updated_at      TIMESTAMP(6)   not null,
    index index_order_id (order_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
