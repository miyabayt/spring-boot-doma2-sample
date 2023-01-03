SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  deleted_at IS NULL
/*%if criteria.roleCode != null */
  AND role_code = /* criteria.roleCode */'user.editUser'
/*%end*/
LIMIT
  1
