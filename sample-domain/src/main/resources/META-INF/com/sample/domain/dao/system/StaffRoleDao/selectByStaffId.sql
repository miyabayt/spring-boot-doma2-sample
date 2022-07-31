SELECT
  staff_role_id
  , sr.staff_id
  , r.role_code
  , r.role_name
  , p.permission_code
  , p.permission_name
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_code = r.role_code
    AND r.deleted_at IS NULL
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
    AND rp.deleted_at IS NULL
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
    AND p.deleted_at IS NULL
WHERE
  sr.staff_id = /* id */1
  AND sr.deleted_at IS NULL
