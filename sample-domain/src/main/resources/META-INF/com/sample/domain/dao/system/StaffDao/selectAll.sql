SELECT
  /*%expand*/*
FROM
  staffs
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND staff_id = /* criteria.id */1
/*%end*/
/*%if criteria.lastName != null */
  AND last_name LIKE /* @infix(criteria.lastName) */'john' ESCAPE '$'
/*%end*/
/*%if criteria.firstName != null */
  AND first_name LIKE /* @infix(criteria.firstName) */'john' ESCAPE '$'
/*%end*/
/*%if criteria.email != null */
  AND email = /* criteria.email */'aaaa@bbbb.com'
/*%end*/
ORDER BY
  staff_id ASC
