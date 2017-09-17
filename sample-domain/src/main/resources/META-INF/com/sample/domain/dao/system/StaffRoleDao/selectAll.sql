SELECT
  staff_role_id
  , sr.staff_id
  , sr.role_id
  , r.role_key
  , r.role_name
  , p.category_key
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
  sr.deleted_at IS NULL
/*%if staff.id != null */
  AND sr.staff_id = /* staff.id */1
/*%end*/
/*%if permission.permissionKey != null */
  AND p.permission_key = /* permission.permissionKey */'01'
/*%end*/
