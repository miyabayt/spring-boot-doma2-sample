SELECT
    c.code_id
    ,cc.category_code
    ,cc.category_name
    ,c.code_name
    ,c.code_value
    ,c.code_alias
    ,c.display_order
    ,c.created_by
    ,c.created_at
    ,c.updated_by
    ,c.updated_at
    ,c.deleted_by
    ,c.deleted_at
    ,c.version
FROM
    codes c
INNER JOIN code_categories cc
ON c.category_code = cc.category_code
AND cc.deleted_at IS NULL
WHERE
  c.deleted_at IS NULL
  AND c.code_id = /* id */1
