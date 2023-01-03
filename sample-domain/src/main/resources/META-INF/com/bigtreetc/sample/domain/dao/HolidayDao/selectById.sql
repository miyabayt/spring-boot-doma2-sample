SELECT
  /*%expand*/*
FROM
  holidays
WHERE
  holiday_id = /* id */1
  AND deleted_at IS NULL
