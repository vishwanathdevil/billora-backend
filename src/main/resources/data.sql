-- ================================================
-- 🌱 BILLORA SEED DATA
-- Runs automatically on every startup.
-- Uses ON CONFLICT DO NOTHING → safe to run multiple times.
-- Add your stores, users, and products here.
-- ================================================


-- ================================================
-- 🏪 STORES
-- ================================================
INSERT INTO store (id, name)
VALUES
  (1, 'Billora Store 1'),
  (2, 'Billora Store 2')
ON CONFLICT (id) DO NOTHING;


-- ================================================
-- 👤 USERS (ADMIN + CASHIER)
-- Add all your admins and cashiers here.
-- Customers register themselves — no need to add them.
-- ================================================
INSERT INTO users (username, password, role, store_id)
VALUES
  ('admin1',   'your_admin_password',   'ADMIN',   1),
  ('cashier1', 'your_cashier_password', 'CASHIER', 1),
  ('admin2',   'your_admin_password',   'ADMIN',   2),
  ('cashier2', 'your_cashier_password', 'CASHIER', 2)
ON CONFLICT (username) DO NOTHING;


-- ================================================
-- 📦 PRODUCTS (Store 1)
-- Add all your products here.
-- Format: (name, code, price, stock, store_id)
-- ================================================
INSERT INTO product (name, code, price, stock, store_id)
VALUES
  ('Product A', 'PROD001', 50,  100, 1),
  ('Product B', 'PROD002', 120, 50,  1),
  ('Product C', 'PROD003', 200, 75,  1)
ON CONFLICT DO NOTHING;


-- ================================================
-- 📦 PRODUCTS (Store 2)
-- ================================================
INSERT INTO product (name, code, price, stock, store_id)
VALUES
  ('Item X', 'ITEM001', 80,  60, 2),
  ('Item Y', 'ITEM002', 150, 40, 2)
ON CONFLICT DO NOTHING;
