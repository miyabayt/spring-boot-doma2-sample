SELECT
    code_id
    ,code_category_id
    ,code_key
    ,code_value
    ,code_alias
    ,attribute1
    ,attribute2
    ,attribute3
    ,attribute4
    ,attribute5
    ,attribute6
    ,display_order
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,deleted_by
    ,deleted_at
    ,version
FROM
    code
WHERE
    deleted_at IS NULL
AND is_invalid = 0
ORDER BY display_order ASC
