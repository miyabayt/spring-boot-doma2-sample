SELECT
  /*%expand*/*
FROM
  code
WHERE
  deleted_at IS NULL
/*%if code.id != null */
  AND code_id = /* code.id */1
/*%end*/
/*%if code.codeKey != null */
  AND code_key = /* code.codeKey */'01'
/*%end*/
LIMIT
  1
