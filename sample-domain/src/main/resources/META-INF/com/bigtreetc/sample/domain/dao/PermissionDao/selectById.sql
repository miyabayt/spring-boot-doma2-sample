SELECT
  /*%expand*/*
FROM
  permissions
WHERE
  deleted_at IS NULL
  AND permission_id = /* id */1
