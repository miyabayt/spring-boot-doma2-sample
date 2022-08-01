SELECT
  user_role_id
  , ur.user_id
  , r.role_code
  , r.role_name
  , p.permission_code
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_code = r.role_code
    AND r.deleted_at IS NULL
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
    AND rp.deleted_at IS NULL
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
    AND p.deleted_at IS NULL
WHERE
  ur.user_id = /* id */1
  AND ur.deleted_at IS NULL
