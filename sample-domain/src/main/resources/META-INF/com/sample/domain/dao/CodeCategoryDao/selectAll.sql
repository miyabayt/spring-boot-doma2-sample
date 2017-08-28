SELECT
    /*%expand*/*
FROM
    code_category
WHERE
    deleted_at IS NULL
/*%if codeCategory.id != null */
AND code_category_id = /* codeCategory.id */1
/*%end*/
/*%if codeCategory.categoryKey != null */
AND category_key = /* codeCategory.categoryKey */'GNR0001'
/*%end*/
