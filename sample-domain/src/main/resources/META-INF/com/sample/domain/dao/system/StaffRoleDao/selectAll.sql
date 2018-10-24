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
/*%if staffCriteria.id != null */
  AND sr.staff_id = /* staffCriteria.id */1
/*%end*/
/*%if permissionCriteria.permissionKey != null */
  AND p.permission_key = /* permissionCriteria.permissionKey */'01'
/*%end*/
