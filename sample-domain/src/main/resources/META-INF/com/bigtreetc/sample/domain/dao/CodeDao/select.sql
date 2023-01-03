SELECT
  /*%expand*/*
FROM
  codes c
INNER JOIN code_categories cc
ON c.category_code = cc.category_code
AND cc.deleted_at IS NULL
WHERE
  c.deleted_at IS NULL
/*%if criteria.id != null */
  AND c.code_id = /* criteria.id */1
/*%end*/
/*%if criteria.categoryCode != null */
  AND c.category_code = /* criteria.categoryCode */'GNR0001'
/*%end*/
LIMIT 1
