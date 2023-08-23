alter table orders
drop check order_type,
add check (order_type in ('PENDING', 'SUCCESS', 'FAIL', 'CANCELED'));
