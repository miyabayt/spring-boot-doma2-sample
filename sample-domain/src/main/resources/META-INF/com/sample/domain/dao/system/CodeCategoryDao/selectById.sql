SELECT
  /*%expand*/*
FROM
  code_category
WHERE
  deleted_at IS NULL
  AND code_category_id = /* id */1
