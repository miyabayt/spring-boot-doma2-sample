SELECT
  /*%expand*/*
FROM
  roles
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND role_id = /* criteria.id */1
/*%end*/
/*%if criteria.roleCode != null */
  AND role_code = /* criteria.roleCode */'user.editUser'
/*%end*/
/*%if criteria.roleName != null */
  AND role_name LIKE /* @infix(criteria.roleName) */'ロール名' ESCAPE '$'
/*%end*/
ORDER BY
  role_id ASC
