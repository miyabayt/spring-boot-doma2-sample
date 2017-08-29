INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES ('system_admin', 'システム管理者', 'none', NOW(), 1);
INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES ('admin', '管理者', 'none', NOW(), 1);
INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES ('operator', 'オペレーター', 'none', NOW(), 1);
INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES ('user', 'ユーザー', 'none', NOW(), 1);

INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('*', '*', '全操作', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('code', 'Code.findCode', 'コード検索', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('code', 'Code.newCode', 'コード登録', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('code', 'Code.editCode', 'コード編集', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('code', 'Code.showCode', 'コード詳細', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('code', 'Code.downloadCsv', 'コードCSVダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('home', 'Home.index', 'ホーム索引', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('role', 'Role.findRole', '役割検索', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('role', 'Role.newRole', '役割登録', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('role', 'Role.editRole', '役割編集', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('role', 'Role.showRole', '役割詳細', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('role', 'Role.downloadCsv', '役割CSVダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('upload', 'UploadFiles.listFiles', 'ファイルアップロード 一覧表示', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('upload', 'UploadFiles.serveFile', 'ファイルアップロード ', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('upload', 'UploadFiles.downloadFile', 'ファイルアップロード ダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('upload', 'UploadFiles.uploadFile', 'ファイルアップロード アップロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.findUser', 'ユーザー検索', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.newUser', 'ユーザー登録', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.editUser', 'ユーザー編集', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.showUser', 'ユーザー詳細', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.downloadCsv', 'ユーザーCSVダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.downloadExcel', 'ユーザーExcelダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('user', 'User.downloadPdf', 'ユーザーPDFダウンロード', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('staff', 'Staff.findStaff', '担当者検索', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('staff', 'Staff.newStaff', '担当者登録', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('staff', 'Staff.editStaff', '担当者編集', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('staff', 'Staff.showStaff', '担当者詳細', 'none', NOW(), 1);
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES ('staff', 'Staff.downloadCsv', '担当者CSVダウンロード', 'none', NOW(), 1);

INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES ('system_admin', 1, 'none', NOW(), 1);
INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES ('user', 1, 'none', NOW(), 1);

INSERT INTO staff_roles (staff_id, role_key, created_by, created_at, version) VALUES (1, 'system_admin', 'none', NOW(), 1);
INSERT INTO user_roles (user_id, role_key, created_by, created_at, version) VALUES (1, 'user', 'none', NOW(), 1);
