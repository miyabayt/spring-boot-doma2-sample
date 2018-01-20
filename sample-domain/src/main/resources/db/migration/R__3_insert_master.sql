DELETE FROM staffs WHERE email = 'test@sample.com';
INSERT INTO staffs(staff_id, first_name, last_name, email, password, tel, created_by, created_at) VALUES
(1, 'john', 'doe', 'test@sample.com', '$2a$06$hY5MzfruCds1t5uFLzrlBuw3HcrEGeysr9xJE4Cml5xEOVf425pmK', '09011112222', 'none', sysdate);

DELETE FROM users WHERE email = 'test@sample.com';
INSERT INTO users(user_id, first_name, last_name, email, password, tel, address, created_by, created_at) VALUES
(1, 'john', 'doe', 'test@sample.com', '$2a$06$hY5MzfruCds1t5uFLzrlBuw3HcrEGeysr9xJE4Cml5xEOVf425pmK', '09011112222', 'tokyo, chuo-ku 1-2-3', 'none', sysdate);

DELETE FROM mail_templates WHERE created_by = 'none';
INSERT INTO mail_templates (mail_template_id, category_key, template_key, subject, template_body, created_by, created_at) VALUES
(1, NULL, 'passwordReset', 'パスワードリセット完了のお願い', '[[$' || '{staff.firstName}]]さん\r\n\r\n下記のリンクを開いてパスワードをリセットしてください。\r\n[[$' || '{url}]]', 'none', sysdate);
