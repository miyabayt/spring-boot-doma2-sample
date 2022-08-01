SELECT
    /*%expand*/*
FROM
    code_categories
WHERE
    deleted_at IS NULL
/*%if criteria.id != null */
AND code_category_id = /* criteria.id */1
/*%end*/
/*%if criteria.categoryCode != null */
AND category_code = /* criteria.categoryCode */'GNR0001'
/*%end*/
