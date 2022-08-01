DELETE FROM roles WHERE created_by = 'init';
INSERT INTO roles (role_code, role_name, created_by, created_at) VALUES
('system_admin', 'システム管理者', 'init', NOW()),
('admin', '管理者', 'init', NOW()),
('operator', 'オペレーター', 'init', NOW()),
('user', 'ユーザー', 'init', NOW());

DELETE FROM permissions WHERE created_by = 'init';
INSERT INTO permissions (permission_code, permission_name, created_by, created_at) VALUES
('codeCategory:read', '分類マスタ検索', 'init', NOW()),
('codeCategory:save', '分類マスタ登録・編集', 'init', NOW()),
('code:read', 'コードマスタ検索', 'init', NOW()),
('code:save', 'コードマスタ登録・編集', 'init', NOW()),
('holiday:read', '祝日マスタ検索', 'init', NOW()),
('holiday:save', '祝日マスタ登録・編集', 'init', NOW()),
('mailTemplate:read', 'メールテンプレート検索', 'init', NOW()),
('mailTemplate:save', 'メールテンプレート登録・編集', 'init', NOW()),
('role:read', 'ロール検索', 'init', NOW()),
('role:save', 'ロール登録・編集', 'init', NOW()),
('uploadFile', 'ファイルアップロード', 'init', NOW()),
('user:read', '顧客マスタ検索', 'init', NOW()),
('user:save', '顧客マスタ登録・編集', 'init', NOW()),
('staff:read', '担当者マスタ検索', 'init', NOW()),
('staff:save', '担当者マスタ登録・編集', 'init', NOW());

DELETE FROM role_permissions WHERE created_by = 'init';
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at)
    SELECT 'system_admin', permission_code, 1, 'init', NOW() FROM permissions;
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at)
    SELECT 'admin', permission_code, 0, 'init', NOW() FROM permissions;
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at)
    SELECT 'operator', permission_code, 0, 'init', NOW() FROM permissions;
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at)
    SELECT 'user', permission_code, 0, 'init', NOW() FROM permissions;

DELETE FROM staff_roles WHERE created_by = 'init';
INSERT INTO staff_roles (staff_id, role_code, created_by, created_at) VALUES
((SELECT staff_id FROM staffs WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'system_admin', 'init', NOW());

DELETE FROM user_roles WHERE created_by = 'init';
INSERT INTO user_roles (user_id, role_code, created_by, created_at) VALUES
((SELECT user_id FROM users WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'user', 'init', NOW());
