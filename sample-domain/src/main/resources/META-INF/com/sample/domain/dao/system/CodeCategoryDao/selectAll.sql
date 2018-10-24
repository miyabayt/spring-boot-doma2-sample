SELECT
    /*%expand*/*
FROM
    code_category
WHERE
    deleted_at IS NULL
/*%if criteria.id != null */
AND code_category_id = /* criteria.id */1
/*%end*/
/*%if criteria.categoryKey != null */
AND category_key = /* criteria.categoryKey */'GNR0001'
/*%end*/
