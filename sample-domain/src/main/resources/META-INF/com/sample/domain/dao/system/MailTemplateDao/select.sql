SELECT
  /*%expand*/*
FROM
  mail_templates
WHERE
  deleted_at IS NULL
/*%if mailTemplate.id != null */
  AND mail_template_id = /* mailTemplate.id */1
/*%end*/
/*%if mailTemplate.templateKey != null */
  AND template_key = /* mailTemplate.templateKey */'thanks'
/*%end*/
  AND rownum <= 1
