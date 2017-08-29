SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  deleted_at IS NULL
/*%if rolePermission.roleKey != null */
  AND role_key = /* rolePermission.roleKey */'user.editUser'
/*%end*/
ORDER BY
  role_key ASC, permission_id ASC
