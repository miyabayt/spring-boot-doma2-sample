SELECT
  /*%expand*/*
FROM
  code_categories
WHERE
  deleted_at IS NULL
  AND code_category_id = /* id */1
