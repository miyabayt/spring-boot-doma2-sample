SELECT
  r.role_key
  , r.role_name
  , p.category_key
  , p.permission_key
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_id = r.role_id
    AND r.deleted_at IS NULL
  LEFT JOIN permissions p
    ON r.permission_id = p.permission_id
    AND p.deleted_at IS NULL
WHERE
  ur.deleted_at IS NULL
/*%if permission.id != null */
  AND p.permission_id = /* permission.id */1
/*%end*/
/*%if permission.permissionKey != null */
  AND permission_key = /* permission.permissionKey */'01'
/*%end*/
