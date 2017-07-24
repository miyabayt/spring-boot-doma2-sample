SELECT
    staff_id
    ,first_name
    ,last_name
    ,email
    ,password
    ,tel
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,deleted_by
    ,deleted_at
    ,version
FROM
    staffs
WHERE
    deleted_at IS NULL
/*%if staff.id != null */
AND staff_id = /* staff.id */1
/*%end*/
/*%if staff.email != null */
AND email = /* staff.email */'aaaa@bbbb.com'
/*%end*/
LIMIT 1
