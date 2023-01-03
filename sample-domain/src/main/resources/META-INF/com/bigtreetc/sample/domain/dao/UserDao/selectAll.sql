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
/*%if criteria.firstName != null */
AND first_name LIKE /* @infix(criteria.firstName) */'john'
/*%end*/
/*%if criteria.lastName != null */
AND last_name LIKE /* @infix(criteria.lastName) */'doe'
/*%end*/
/*%if criteria.tel != null */
AND tel LIKE /* @prefix(criteria.tel) */'0901234'
/*%end*/
/*%if criteria.zip != null */
AND zip LIKE /* @prefix(criteria.zip) */'10600'
/*%end*/
/*%if criteria.address != null */
AND address LIKE /* @infix(criteria.address) */'東京都港区'
/*%end*/
/*%if criteria.onlyNullAddress */
AND address IS NULL
/*%end*/
ORDER BY user_id ASC
