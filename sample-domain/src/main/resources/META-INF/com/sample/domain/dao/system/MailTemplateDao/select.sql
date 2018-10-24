SELECT
  /*%expand*/*
FROM
  mail_templates
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND mail_template_id = /* criteria.id */1
/*%end*/
/*%if criteria.templateKey != null */
  AND template_key = /* criteria.templateKey */'thanks'
/*%end*/
LIMIT 1
