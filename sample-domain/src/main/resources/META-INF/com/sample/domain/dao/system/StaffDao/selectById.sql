SELECT
  /*%expand*/*
FROM
  staffs
WHERE
  staff_id = /* id */1
  AND deleted_at IS NULL
