SELECT
    send_mail_queue_id
    ,from_address
    ,to_address
    ,cc_address
    ,bcc_address
    ,subject
    ,body
    ,sent_at
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,deleted_by
    ,deleted_at
    ,version
FROM
    send_mail_queue
WHERE
    deleted_at IS NULL
AND sent_at IS NULL
/*%if criteria.id != null */
  AND send_mail_queue_id = /* criteria.id */1
/*%end*/
ORDER BY send_mail_queue_id ASC
