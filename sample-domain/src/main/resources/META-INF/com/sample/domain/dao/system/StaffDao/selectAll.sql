SELECT
  /*%expand*/*
FROM
  staffs
WHERE
  deleted_at IS NULL
/*%if staff.id != null */
  AND staff_id = /* staff.id */1
/*%end*/
/*%if staff.lastName != null && staff.firstName != null */
  AND (
    last_name LIKE /* @infix(staff.lastName) */'john' ESCAPE '$'
    OR first_name LIKE /* @infix(staff.firstName) */'john' ESCAPE '$'
  )
/*%end*/
/*%if staff.email != null */
  AND email = /* staff.email */'aaaa@bbbb.com'
/*%end*/
ORDER BY
  staff_id ASC
