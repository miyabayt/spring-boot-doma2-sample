ALTER TABLE staff_roles
DROP COLUMN role_id;

ALTER TABLE user_roles
DROP COLUMN role_id;

ALTER TABLE roles
DROP COLUMN permission_id;

ALTER TABLE permissions
DROP INDEX idx_permission;

ALTER TABLE roles
DROP INDEX idx_role;

ALTER TABLE staff_roles
DROP INDEX idx_staff_role;

ALTER TABLE user_roles
DROP INDEX idx_user_role;
