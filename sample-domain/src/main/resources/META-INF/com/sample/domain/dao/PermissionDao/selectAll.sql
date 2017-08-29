SELECT
  /*%expand*/*
FROM
  permissions
WHERE
  deleted_at IS NULL
/*%if permission.id != null */
  AND permission_id = /* permission.id */1
/*%end*/
/*%if permission.categoryKey != null */
  AND p.category_key = /* permission.categoryKey */'01'
/*%end*/
