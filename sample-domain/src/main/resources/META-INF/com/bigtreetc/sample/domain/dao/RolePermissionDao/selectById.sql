SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  role_permissions_id = /* id */1
  AND deleted_at IS NULL
