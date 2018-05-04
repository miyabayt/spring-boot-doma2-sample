SELECT
  user_role_id
  , ur.user_id
  , r.role_key
  , r.role_name
  , p.permission_key
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_key = r.role_key
    AND r.deleted_at IS NULL
  LEFT JOIN permissions p
    ON r.permission_id = p.permission_id
    AND p.deleted_at IS NULL
WHERE
  p.permission_id = /* id */1
  AND ur.deleted_at IS NULL
