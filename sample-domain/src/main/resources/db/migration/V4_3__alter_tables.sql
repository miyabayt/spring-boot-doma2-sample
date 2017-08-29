ALTER TABLE persistent_logins
ADD INDEX idx_persistent_logins(username, ip_address, user_agent);

ALTER TABLE persistent_logins
ADD INDEX idx_persistent_logins_01(last_used);

ALTER TABLE staff_roles
ADD COLUMN role_key VARCHAR(100) NOT NULL COMMENT '役割キー' AFTER staff_id;

ALTER TABLE user_roles
ADD COLUMN role_key VARCHAR(100) NOT NULL COMMENT '役割キー' AFTER user_id;

ALTER TABLE permissions
ADD INDEX idx_permissions(permission_key, deleted_at);

ALTER TABLE roles
ADD INDEX idx_roles(role_key, deleted_at);

ALTER TABLE staff_roles
ADD INDEX idx_staff_roles(staff_id, deleted_at);

ALTER TABLE user_roles
ADD INDEX idx_user_roles(user_id, deleted_at);
