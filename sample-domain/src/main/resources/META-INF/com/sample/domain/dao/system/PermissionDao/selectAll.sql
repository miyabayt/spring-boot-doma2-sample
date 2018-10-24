SELECT
  /*%expand*/*
FROM
  permissions
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND permission_id = /* criteria.id */1
/*%end*/
/*%if criteria.categoryKey != null */
  AND category_key = /* criteria.categoryKey */'01'
/*%end*/
