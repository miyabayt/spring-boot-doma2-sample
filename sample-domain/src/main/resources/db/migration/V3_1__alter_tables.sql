ALTER TABLE persistent_logins
ADD COLUMN ip_address VARCHAR(64) NULL AFTER username,
ADD COLUMN user_agent VARCHAR(200) NULL AFTER ip_address;
