SELECT
  /*%expand*/*
FROM
  roles
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND role_id = /* criteria.id */1
/*%end*/
/*%if criteria.roleKey != null */
  AND role_key = /* criteria.roleKey */'user.editUser'
/*%end*/
LIMIT
  1
