SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
  deleted_at IS NULL
/*%if criteria.roleCode != null */
  AND role_code = /* criteria.roleCode */'admin'
/*%end*/
/*%if criteria.roleCodes != null */
  AND role_code IN /* criteria.roleCodes */('admin', 'user')
/*%end*/
/*%if criteria.isEnabled != null */
  AND is_enabled = /* criteria.isEnabled */1
/*%end*/
ORDER BY
  role_code ASC, permission_code ASC
