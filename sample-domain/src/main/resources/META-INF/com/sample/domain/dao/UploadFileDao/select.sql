SELECT
    upload_file_id
    ,file_name
    ,original_file_name
    ,content_type
    ,content
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,deleted_by
    ,deleted_at
    ,version
FROM
    upload_files
WHERE
    deleted_at IS NULL
/*%if uploadFile.id != null */
AND upload_file_id = /* uploadFile.id */1
/*%end*/
