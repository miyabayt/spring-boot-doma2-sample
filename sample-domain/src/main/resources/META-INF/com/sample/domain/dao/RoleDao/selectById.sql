SELECT
  /*%expand*/*
FROM
  roles
WHERE
  role_id = /* id */1
  AND deleted_at IS NULL
