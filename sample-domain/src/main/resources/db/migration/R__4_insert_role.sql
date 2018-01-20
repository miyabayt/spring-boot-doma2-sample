DELETE FROM roles WHERE created_by = 'none';
INSERT INTO roles (role_id, role_key, role_name, created_by, created_at, version) VALUES (1, 'system_admin', 'システム管理者', 'none', sysdate, 1);
INSERT INTO roles (role_id, role_key, role_name, created_by, created_at, version) VALUES (2, 'admin', '管理者', 'none', sysdate, 1);
INSERT INTO roles (role_id, role_key, role_name, created_by, created_at, version) VALUES (3, 'operator', 'オペレーター', 'none', sysdate, 1);
INSERT INTO roles (role_id, role_key, role_name, created_by, created_at, version) VALUES (4, 'user', 'ユーザー', 'none', sysdate, 1);

DELETE FROM permissions WHERE created_by = 'none';
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (1, '*', '.*', '全操作', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (2, 'code', '^Code\\.(find|show|download)Code$', 'コード検索', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (3, 'code', '^Code\\.(new|edit)Code$', 'コード登録・編集', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (4, 'home', '^Home\\.index$', 'ホーム索引', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (5, 'role', '^Role\\.(find|show|download)Role$', '役割検索', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (6, 'role', '^Role\\.(new|edit)Role$', '役割登録・編集', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (7, 'upload', '^UploadFiles\\..*', 'ファイルアップロード', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (8, 'user', '^User\\.(find|show|downloadCsv|downloadExcel|downloadPdf)User$', 'ユーザー検索', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (9, 'user', '^User\\.(new|edit)User$', 'ユーザー登録・編集', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (10, 'staff', '^Staff\\.(find|show|download)Staff$', '担当者検索', 'none', sysdate, 1);
INSERT INTO permissions (permission_id, category_key, permission_key, permission_name, created_by, created_at, version) VALUES (11, 'staff', '^Staff\\.(new|edit)Staff$', '担当者登録・編集', 'none', sysdate, 1);

DELETE FROM role_permissions WHERE created_by = 'none';
INSERT INTO role_permissions (role_permission_id, role_key, permission_id, created_by, created_at, version) VALUES (1, 'system_admin', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', sysdate, 1);
INSERT INTO role_permissions (role_permission_id, role_key, permission_id, created_by, created_at, version) VALUES (2, 'user', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', sysdate, 1);

DELETE FROM staff_roles WHERE created_by = 'none';
INSERT INTO staff_roles (staff_role_id, staff_id, role_key, created_by, created_at, version) VALUES (1, (SELECT staff_id FROM staffs WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'system_admin', 'none', sysdate, 1);

DELETE FROM user_roles WHERE created_by = 'none';
INSERT INTO user_roles (user_role_id, user_id, role_key, created_by, created_at, version) VALUES (1, (SELECT user_id FROM users WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'user', 'none', sysdate, 1);
