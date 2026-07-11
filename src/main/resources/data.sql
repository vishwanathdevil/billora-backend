-- ================================================
-- 🌱 BILLORA SEED DATA
-- Correct table names (matching JPA entity @Table annotations):
--   Store entity  → table: "stores"
--   User entity   → table: "users"
--   Product entity → table: "products"
--
-- HOW TO USE:
-- Enable in application.properties by adding:
--   spring.sql.init.mode=always
--   spring.jpa.defer-datasource-initialization=true
--
-- ⚠️ Currently DISABLED (use /api/stores/bulk and /api/users/bulk instead)
-- ================================================

-- ================================================
-- 🏪 STORES (table name = "stores")
-- ================================================
-- INSERT INTO stores (name)
-- VALUES
--   ('D-Mart'),
--   ('Reliance Fresh'),
--   ('More Supermarket'),
--   ('BigBazaar'),
--   ('Spencers')
-- ON CONFLICT DO NOTHING;


-- ================================================
-- 👤 USERS (table name = "users")
-- ================================================
-- INSERT INTO users (username, password, role, store_id)
-- VALUES
--   ('dmart_admin',       'dmart@admin123',    'ADMIN',   1),
--   ('dmart_cashier',     'dmart@cash123',     'CASHIER', 1),
--   ('reliance_admin',    'reliance@admin123', 'ADMIN',   2),
--   ('reliance_cashier',  'reliance@cash123',  'CASHIER', 2)
-- ON CONFLICT (username) DO NOTHING;


-- ================================================
-- 📦 PRODUCTS (table name = "products")
-- ================================================
-- INSERT INTO products (name, code, price, stock, store_id)
-- VALUES
--   ('Product A', 'PROD001', 50, 100, 1),
--   ('Product B', 'PROD002', 120, 50, 1)
-- ON CONFLICT DO NOTHING;
