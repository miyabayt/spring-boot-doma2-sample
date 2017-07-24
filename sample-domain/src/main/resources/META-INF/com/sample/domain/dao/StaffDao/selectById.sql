SELECT
    staff_id
    ,first_name
    ,last_name
    ,email
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
    staff_id = /* id */1
AND
    deleted_at IS NULL
