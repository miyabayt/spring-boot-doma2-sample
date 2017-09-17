SELECT
  /*%expand*/*
FROM
  code_category
WHERE
  deleted_at IS NULL
  AND category_key = /* categoryKey */1
