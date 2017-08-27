SELECT
  r.role_key
  , r.role_name
  , p.category_key
  , p.permission_key
  , p.permission_name
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_id = r.role_id
    AND r.deleted_at IS NULL
  LEFT JOIN permissions p
    ON r.permission_id = p.permission_id
    AND p.deleted_at IS NULL
WHERE
  sr.staff_id = /* id */1
  AND sr.deleted_at IS NULL
