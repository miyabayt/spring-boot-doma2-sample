SELECT
  /*%expand*/*
FROM
  staffs
WHERE
  deleted_at IS NULL
/*%if staff.id != null */
  AND staff_id = /* staff.id */1
/*%end*/
/*%if staff.email != null */
  AND email = /* staff.email */'aaaa@bbbb.com'
/*%end*/
/*%if staff.passwordResetToken != null */
  AND password_reset_token = /* staff.passwordResetToken */'xxxx'
  AND token_expires_at > NOW()
/*%end*/
  AND rownum <= 1
