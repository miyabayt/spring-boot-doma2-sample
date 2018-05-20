SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  deleted_at IS NULL
/*%if rolePermission.roleKey != null */
  AND role_key = /* rolePermission.roleKey */'user.editUser'
/*%end*/
LIMIT
  1
