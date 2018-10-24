SELECT
  /*%expand*/*
FROM
  holidays
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND holiday_id = /* criteria.id */1
/*%end*/
ORDER BY
  holiday_id ASC
