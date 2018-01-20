SELECT
  /*%expand*/*
FROM
  code_category
WHERE
  deleted_at IS NULL
/*%if codeCategory.id != null */
  AND code_category_id = /* codeCategory.id */1
/*%end*/
/*%if codeCategory.categoryKey != null */
  AND category_key = /* codeCategory.categoryKey */'01'
/*%end*/
  AND rownum <= 1
