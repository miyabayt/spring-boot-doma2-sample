SELECT
  /*%expand*/*
FROM
  mail_templates
WHERE
  deleted_at IS NULL
/*%if criteria.id != null */
  AND mail_template_id = /* criteria.id */1
/*%end*/
/*%if criteria.templateCode != null */
  AND template_code = /* criteria.templateCode */'thanks'
/*%end*/
ORDER BY
  mail_template_id ASC
