SELECT
  /*%expand*/*
FROM
  permissions
WHERE
   deleted_at IS NULL
/*%if criteria.id != null */
  AND permission_id = /* criteria.id */1
/*%end*/
/*%if criteria.permissionCode != null */
  AND permission_code = /* criteria.permissionCode */'01'
/*%end*/
LIMIT 1
