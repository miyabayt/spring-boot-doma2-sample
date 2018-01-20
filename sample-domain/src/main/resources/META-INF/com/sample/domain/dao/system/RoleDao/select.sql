SELECT
  /*%expand*/*
FROM
  roles
WHERE
  deleted_at IS NULL
/*%if role.id != null */
  AND role_id = /* role.id */1
/*%end*/
/*%if role.roleKey != null */
  AND role_key = /* role.roleKey */'user.editUser'
/*%end*/
  AND rownum <= 1