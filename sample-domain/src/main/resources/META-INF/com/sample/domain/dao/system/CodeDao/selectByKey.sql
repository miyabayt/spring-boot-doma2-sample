SELECT
  /*%expand*/*
FROM
  code
WHERE
  deleted_at IS NULL
  AND code_key = /* codeKey */1
