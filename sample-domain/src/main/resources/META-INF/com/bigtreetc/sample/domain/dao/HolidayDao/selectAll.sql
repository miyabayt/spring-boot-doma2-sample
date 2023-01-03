SELECT
  /*%expand*/*
FROM
  holidays
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND holiday_id = /* criteria.id */1
/*%end*/
/*%if criteria.holidayName != null */
  AND holiday_name LIKE /* @infix(criteria.holidayName) */'名称' ESCAPE '$'
/*%end*/
ORDER BY
  holiday_id ASC
