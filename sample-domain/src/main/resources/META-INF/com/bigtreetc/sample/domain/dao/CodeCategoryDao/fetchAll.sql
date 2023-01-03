SELECT
    /*%expand*/*
FROM
    code_categories
WHERE
    deleted_at IS NULL
ORDER BY
    1
