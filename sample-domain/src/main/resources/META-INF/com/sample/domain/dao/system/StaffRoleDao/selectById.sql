SELECT
  staff_role_id
  , sr.staff_id
  , r.role_key
  , r.role_name
  , p.permission_key
  , p.permission_name
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_key = r.role_key
    AND r.deleted_at IS NULL
  LEFT JOIN permissions p
    ON r.permission_id = p.permission_id
    AND p.deleted_at IS NULL
WHERE
  p.permission_id = /* id */1
  AND sr.deleted_at IS NULL
