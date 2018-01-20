DELETE FROM code_category WHERE created_by = 'none';
INSERT INTO code_category(code_category_id, category_key, category_name, created_by, created_at) VALUES (1, 'GNR0001', '性別', 'none', sysdate);
INSERT INTO code_category(code_category_id, category_key, category_name, created_by, created_at) VALUES (2, 'GNR0003', '有無区分', 'none', sysdate);

DELETE FROM code WHERE created_by = 'none';
INSERT INTO code(code_id, code_category_id, code_key, code_value, code_alias, display_order, is_invalid, created_by, created_at) VALUES (1, (SELECT code_category_id FROM code_category WHERE category_key = 'GNR0001' AND deleted_at IS NULL), '01', '男', 'male', 1, 0, 'none', sysdate);
INSERT INTO code(code_id, code_category_id, code_key, code_value, code_alias, display_order, is_invalid, created_by, created_at) VALUES (2, (SELECT code_category_id FROM code_category WHERE category_key = 'GNR0001' AND deleted_at IS NULL), '02', '女', 'female', 2, 0, 'none', sysdate);
INSERT INTO code(code_id, code_category_id, code_key, code_value, code_alias, display_order, is_invalid, created_by, created_at) VALUES (3, (SELECT code_category_id FROM code_category WHERE category_key = 'GNR0003' AND deleted_at IS NULL), '01', '無', NULL, 1, 0, 'none', sysdate);
INSERT INTO code(code_id, code_category_id, code_key, code_value, code_alias, display_order, is_invalid, created_by, created_at) VALUES (4, (SELECT code_category_id FROM code_category WHERE category_key = 'GNR0003' AND deleted_at IS NULL), '02', '有', NULL, 2, 0, 'none', sysdate);
