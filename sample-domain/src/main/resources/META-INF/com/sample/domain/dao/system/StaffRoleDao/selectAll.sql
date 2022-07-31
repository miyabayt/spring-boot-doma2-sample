SELECT
  staff_role_id
  , sr.staff_id
  , sr.role_id
  , r.role_code
  , r.role_name
  , p.category_code
  , p.permission_code
  , p.permission_name
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_code = r.role_code
    AND r.deleted_at IS NULL
  LEFT JOIN permissions p
    ON r.permission_code = p.permission_code
    AND p.deleted_at IS NULL
WHERE
  sr.deleted_at IS NULL
/*%if staffCriteria.id != null */
  AND sr.staff_id = /* staffCriteria.id */1
/*%end*/
/*%if permissionCriteria.permissionCode != null */
  AND p.permission_code = /* permissionCriteria.permissionCode */'01'
/*%end*/
