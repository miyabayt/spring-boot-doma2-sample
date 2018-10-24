SELECT
  user_role_id
  , ur.user_id
  , ur.role_id
  , r.role_key
  , r.role_name
  , p.category_key
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
  ur.deleted_at IS NULL
/*%if userCriteria.id != null */
  AND ur.user_id = /* userCriteria.id */1
/*%end*/
/*%if permissionCriteria.permissionKey != null */
  AND p.permission_key = /* permissionCriteria.permissionKey */'01'
/*%end*/
