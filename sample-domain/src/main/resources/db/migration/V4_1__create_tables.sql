CREATE TABLE role_permissions(
  role_permission_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '役割権限紐付けID'
  , role_key VARCHAR(100) NOT NULL COMMENT '役割キー'
  , permission_id INT(11) NOT NULL COMMENT '権限ID'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME DEFAULT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (role_permission_id)
) COMMENT='役割権限紐付け';
