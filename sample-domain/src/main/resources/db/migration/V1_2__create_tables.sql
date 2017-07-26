CREATE TABLE IF NOT EXISTS persistent_logins(
  username VARCHAR(64) NOT NULL COMMENT 'ログインID'
  , series VARCHAR(64) COMMENT '直列トークン'
  , token VARCHAR(64) NOT NULL COMMENT 'トークン'
  , last_used DATETIME NOT NULL COMMENT '最終利用日'
  , PRIMARY KEY (series)
) COMMENT='ログイン記録';

CREATE TABLE IF NOT EXISTS code_category(
  code_category_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'コード分類ID'
  , category_key VARCHAR(50) NOT NULL COMMENT 'コード分類キー'
  , category_name VARCHAR(50) NOT NULL COMMENT 'コード分類名'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (code_category_id)
  , KEY idx_code_category (category_key, deleted_at)
) COMMENT='コード分類';

CREATE TABLE IF NOT EXISTS code(
  code_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'コードID'
  , code_category_id INT(11) unsigned NOT NULL COMMENT 'コード分類ID'
  , code_key VARCHAR(50) NOT NULL COMMENT 'コードキー'
  , code_value VARCHAR(100) NOT NULL COMMENT 'コード値'
  , code_alias VARCHAR(100) DEFAULT NULL COMMENT 'コードエイリアス'
  , attribute1 VARCHAR(2) DEFAULT NULL COMMENT '属性1'
  , attribute2 VARCHAR(2) DEFAULT NULL COMMENT '属性2'
  , attribute3 VARCHAR(2) DEFAULT NULL COMMENT '属性3'
  , attribute4 VARCHAR(2) DEFAULT NULL COMMENT '属性4'
  , attribute5 VARCHAR(2) DEFAULT NULL COMMENT '属性5'
  , attribute6 VARCHAR(2) DEFAULT NULL COMMENT '属性6'
  , display_order INT(11) DEFAULT 0 COMMENT '表示順'
  , is_invalid tinyint(1) NOT NULL DEFAULT 0 COMMENT '無効フラグ'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (code_id)
  , KEY idx_code (code_key, deleted_at)
) COMMENT='コード';

CREATE TABLE permissions(
  permission_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '権限ID'
  , category_key VARCHAR(50) NOT NULL COMMENT '権限カテゴリキー'
  , permission_key VARCHAR(50) NOT NULL COMMENT '権限キー'
  , permission_name VARCHAR(50) NOT NULL COMMENT '権限名'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (permission_id)
  , KEY idx_permission (permission_key, deleted_at)
) COMMENT='権限';

CREATE TABLE roles(
  role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '役割ID'
  , role_key VARCHAR(100) NOT NULL COMMENT '役割キー'
  , role_name VARCHAR(100) NOT NULL COMMENT '役割名'
  , permission_id INT(11) NOT NULL COMMENT '権限ID'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (role_id)
  , KEY idx_role (role_key, deleted_at)
) COMMENT='役割';

CREATE TABLE staff_roles(
  staff_role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '担当者役割ID'
  , staff_id INT(11) unsigned NOT NULL COMMENT '担当者ID'
  , role_id INT(11) unsigned NOT NULL COMMENT '役割ID'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (staff_role_id)
  , KEY idx_staff_role (staff_id, deleted_at)
) COMMENT='担当者役割';

CREATE TABLE user_roles(
  user_role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ユーザー役割ID'
  , user_id INT(11) unsigned NOT NULL COMMENT 'ユーザーID'
  , role_id INT(11) unsigned NOT NULL COMMENT '役割ID'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (user_role_id)
  , KEY idx_user_role (user_id, deleted_at)
) COMMENT='ユーザー役割';

CREATE TABLE IF NOT EXISTS staffs(
  staff_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '担当者ID'
  , first_name VARCHAR(40) NOT NULL COMMENT '名前'
  , last_name VARCHAR(40) NOT NULL COMMENT '苗字'
  , email VARCHAR(100) DEFAULT NULL COMMENT 'メールアドレス'
  , password VARCHAR(100) DEFAULT NULL COMMENT 'パスワード'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '電話番号'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (staff_id)
  , KEY idx_staffs (email, deleted_at)
) COMMENT='担当者';

CREATE TABLE IF NOT EXISTS users(
  user_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ユーザID'
  , first_name VARCHAR(40) NOT NULL COMMENT '名前'
  , last_name VARCHAR(40) NOT NULL COMMENT '苗字'
  , email VARCHAR(100) DEFAULT NULL COMMENT 'メールアドレス'
  , password VARCHAR(100) DEFAULT NULL COMMENT 'パスワード'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '電話番号'
  , zip VARCHAR(20) DEFAULT NULL COMMENT '郵便番号'
  , address VARCHAR(100) DEFAULT NULL COMMENT '住所'
  , upload_file_id INT(11) unsigned DEFAULT NULL COMMENT '添付ファイル'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (user_id)
  , KEY idx_users (email, deleted_at)
) COMMENT='ユーザー';

CREATE TABLE IF NOT EXISTS upload_files(
  upload_file_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ファイルID'
  , file_name VARCHAR(100) NOT NULL COMMENT 'ファイル名'
  , original_file_name VARCHAR(200) NOT NULL COMMENT 'オリジナルファイル名'
  , content_type VARCHAR(50) NOT NULL COMMENT 'コンテンツタイプ'
  , content LONGBLOB NOT NULL COMMENT 'コンテンツ'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (upload_file_id)
  , KEY idx_upload_files (file_name, deleted_at)
) COMMENT='アップロードファイル';