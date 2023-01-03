SELECT
  /*%expand*/*
FROM
  mail_templates
WHERE
  mail_template_id = /* id */1
  AND deleted_at IS NULL
