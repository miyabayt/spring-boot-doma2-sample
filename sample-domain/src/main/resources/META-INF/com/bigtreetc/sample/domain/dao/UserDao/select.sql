SELECT
    user_id
    ,first_name
    ,last_name
    ,email
    ,password
    ,tel
    ,zip
    ,address
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,deleted_by
    ,deleted_at
    ,version
FROM
    users
WHERE
    deleted_at IS NULL
/*%if criteria.id != null */
AND user_id = /* criteria.id */1
/*%end*/
/*%if criteria.email != null */
AND email = /* criteria.email */'aaaa@bbbb.com'
/*%end*/
/*%if criteria.onlyNullAddress */
AND address IS NULL
/*%end*/
