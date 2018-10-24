SELECT
  /*%expand*/*
FROM
  code
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND code_id = /* criteria.id */1
/*%end*/
/*%if criteria.codeKey != null */
  AND code_key = /* criteria.codeKey */'01'
/*%end*/
LIMIT
  1
