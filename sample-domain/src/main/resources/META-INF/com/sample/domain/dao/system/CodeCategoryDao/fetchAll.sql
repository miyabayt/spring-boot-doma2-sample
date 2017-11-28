SELECT
    /*%expand*/*
FROM
    code_category
WHERE
    deleted_at IS NULL
ORDER BY
    1
