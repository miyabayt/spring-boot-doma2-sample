SELECT
  /*%expand*/*
FROM
  holidays
WHERE
  deleted_at IS NULL
/*%if holiday.id != null */
  AND holiday_id = /* holiday.id */1
/*%end*/
LIMIT 1
