SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  deleted_at IS NULL
/*%if criteria.roleKey != null */
  AND role_key = /* criteria.roleKey */'user.editUser'
/*%end*/
LIMIT
  1
