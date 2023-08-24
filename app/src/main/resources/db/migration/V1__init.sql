create table if not exists users (
    created_at TIMESTAMP(6) not null,
    updated_at TIMESTAMP(6) not null,
    user_id bigint not null,
    user_type varchar(16) not null check (user_type in ('ROLE_USER', 'ROLE_ADMIN')),
    username varchar(25) not null,
    password varchar(255) not null,
    primary key (user_id)
);

create table if not exists product (
    created_at TIMESTAMP(6) not null,
    end_time TIMESTAMP(6) not null,
    product_id bigint not null,
    quantity bigint not null,
    start_time TIMESTAMP(6) not null,
    updated_at TIMESTAMP(6) not null,
    name varchar(255) not null,
    price varchar(255) not null,
    description longtext not null,
    primary key (product_id)
);

create table if not exists cart (
    cart_id bigint not null,
    created_at TIMESTAMP(6) not null,
    updated_at TIMESTAMP(6) not null,
    user_id bigint not null unique,
    primary key (cart_id)
);

create table if not exists cart_product (
    cart_id bigint not null,
    product_id bigint not null,
    quantity bigint not null,
    primary key (cart_id, product_id),
    foreign key (cart_id) references cart (cart_id),
    foreign key (product_id) references product (product_id)
);

create table if not exists orders (
    created_at TIMESTAMP(6) not null,
    order_id bigint not null,
    user_id bigint not null,
    order_type varchar(10) not null check (order_type in ('PENDING', 'SUCCESS', 'CANCELED', 'FAIL')),
    total_price varchar(255) not null,
    primary key (order_id)
);

create table if not exists order_product (
    created_at TIMESTAMP(6) not null,
    order_id bigint not null,
    product_id bigint not null,
    quantity bigint not null,
    name varchar(255) not null,
    price varchar(255) not null,
    primary key (order_id, product_id),
    foreign key (order_id) references orders (order_id)
);

create table if not exists payment (
    purchased_money numeric(38,0) not null,
    created_at TIMESTAMP(6) not null,
    order_id bigint not null,
    payment_id bigint not null,
    updated_at TIMESTAMP(6) not null,
    user_id bigint not null,
    pay_status varchar(16) not null check (pay_status in ('PENDING', 'SUCCESS', 'FAIL', 'CANCELED')),
    pay_type varchar(16) not null check (pay_type in ('CARD', 'DEPOSIT')),
    primary key (payment_id)
);

create index index_order_id on payment (order_id);
