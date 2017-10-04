DELETE FROM roles WHERE created_by = 'none';
INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES
('system_admin', 'システム管理者', 'none', NOW(), 1),
('admin', '管理者', 'none', NOW(), 1),
('operator', 'オペレーター', 'none', NOW(), 1),
('user', 'ユーザー', 'none', NOW(), 1);

DELETE FROM permissions WHERE created_by = 'none';
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES
('*', '.*', '全操作', 'none', NOW(), 1),
('code', '^Code\\.(find|show|download)Code$', 'コード検索', 'none', NOW(), 1),
('code', '^Code\\.(new|edit)Code$', 'コード登録・編集', 'none', NOW(), 1),
('home', '^Home\\.index$', 'ホーム索引', 'none', NOW(), 1),
('role', '^Role\\.(find|show|download)Role$', '役割検索', 'none', NOW(), 1),
('role', '^Role\\.(new|edit)Role$', '役割登録・編集', 'none', NOW(), 1),
('upload', '^UploadFiles\\..*', 'ファイルアップロード', 'none', NOW(), 1),
('user', '^User\\.(find|show|downloadCsv|downloadExcel|downloadPdf)User$', 'ユーザー検索', 'none', NOW(), 1),
('user', '^User\\.(new|edit)User$', 'ユーザー登録・編集', 'none', NOW(), 1),
('staff', '^Staff\\.(find|show|download)Staff$', '担当者検索', 'none', NOW(), 1),
('staff', '^Staff\\.(new|edit)Staff$', '担当者登録・編集', 'none', NOW(), 1);

DELETE FROM role_permissions WHERE created_by = 'none';
INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES
('system_admin', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', NOW(), 1);
INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES
('user', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', NOW(), 1);

DELETE FROM staff_roles WHERE created_by = 'none';
INSERT INTO staff_roles (staff_id, role_key, created_by, created_at, version) VALUES
((SELECT staff_id FROM staffs WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'system_admin', 'none', NOW(), 1);

DELETE FROM user_roles WHERE created_by = 'none';
INSERT INTO user_roles (user_id, role_key, created_by, created_at, version) VALUES
((SELECT user_id FROM users WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'user', 'none', NOW(), 1);
