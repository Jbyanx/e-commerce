CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO inventory_item (id, product_id, available_quantity, price)
VALUES
    (gen_random_uuid(), 'P-1001', 20, 15000.00),
    (gen_random_uuid(), 'P-1002', 10, 35000.00),
    (gen_random_uuid(), 'P-1003', 5, 82000.00);
