CREATE TABLE persistent_logins(
  username VARCHAR(64) NOT NULL
  , ip_address VARCHAR(64) NOT NULL
  , user_agent VARCHAR(200) NOT NULL
  , series VARCHAR(64)
  , token VARCHAR(64) NOT NULL
  , last_used TIMESTAMP NOT NULL
  , constraint persistent_logins_pk PRIMARY KEY (series)
);

CREATE TABLE code_category(
  code_category_id NUMBER(11)
  , category_key VARCHAR(50) NOT NULL
  , category_name VARCHAR(50) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint code_category_pk PRIMARY KEY (code_category_id)
);

CREATE TABLE code(
  code_id NUMBER(11)
  , code_category_id NUMBER(11) NOT NULL
  , code_key VARCHAR(50) NOT NULL
  , code_value VARCHAR(100) NOT NULL
  , code_alias VARCHAR(100) DEFAULT NULL
  , attribute1 VARCHAR(2) DEFAULT NULL
  , attribute2 VARCHAR(2) DEFAULT NULL
  , attribute3 VARCHAR(2) DEFAULT NULL
  , attribute4 VARCHAR(2) DEFAULT NULL
  , attribute5 VARCHAR(2) DEFAULT NULL
  , attribute6 VARCHAR(2) DEFAULT NULL
  , display_order NUMBER(11) DEFAULT 0
  , is_invalid NUMBER(1) DEFAULT 0 NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint code_pk PRIMARY KEY (code_id)
);

CREATE TABLE permissions(
  permission_id NUMBER(11)
  , category_key VARCHAR(50) NOT NULL
  , permission_key VARCHAR(100) NOT NULL
  , permission_name VARCHAR(50) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint permissions_pk PRIMARY KEY (permission_id)
);

CREATE TABLE roles(
  role_id NUMBER(11)
  , role_key VARCHAR(100) NOT NULL
  , role_name VARCHAR(100) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint roles_pk PRIMARY KEY (role_id)
);

CREATE TABLE staff_roles(
  staff_role_id NUMBER(11)
  , staff_id NUMBER(11) NOT NULL
  , role_key VARCHAR(100) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint staff_roles_pk PRIMARY KEY (staff_role_id)
);

CREATE TABLE user_roles(
  user_role_id NUMBER(11)
  , user_id NUMBER(11)
  , role_key VARCHAR(100) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint user_roles_pk PRIMARY KEY (user_role_id)
);

CREATE TABLE staffs(
  staff_id NUMBER(11)
  , first_name VARCHAR(40) NOT NULL
  , last_name VARCHAR(40) NOT NULL
  , email VARCHAR(100) DEFAULT NULL
  , password VARCHAR(100) DEFAULT NULL
  , tel VARCHAR(20) DEFAULT NULL
  , password_reset_token VARCHAR(50) DEFAULT NULL
  , token_expires_at TIMESTAMP DEFAULT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint staffs_pk PRIMARY KEY (staff_id)
);

CREATE TABLE users(
  user_id NUMBER(11) NOT NULL
  , first_name VARCHAR(40) NOT NULL
  , last_name VARCHAR(40) NOT NULL
  , email VARCHAR(100) DEFAULT NULL
  , password VARCHAR(100) DEFAULT NULL
  , tel VARCHAR(20) DEFAULT NULL
  , zip VARCHAR(20) DEFAULT NULL
  , address VARCHAR(100) DEFAULT NULL
  , upload_file_id NUMBER(11) DEFAULT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint users_pk PRIMARY KEY (user_id)
);

CREATE TABLE upload_files(
  upload_file_id NUMBER(11)
  , file_name VARCHAR(100) NOT NULL
  , original_file_name VARCHAR(200) NOT NULL
  , content_type VARCHAR(50) NOT NULL
  , content NCLOB NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint upload_files_pk PRIMARY KEY (upload_file_id)
);

CREATE TABLE mail_templates(
  mail_template_id NUMBER(11)
  , category_key VARCHAR(50) DEFAULT NULL
  , template_key VARCHAR(100) NOT NULL
  , subject VARCHAR(100) NOT NULL
  , template_body CLOB NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint mail_templates_pk PRIMARY KEY (mail_template_id)
);

CREATE TABLE send_mail_queue(
  send_mail_queue_id NUMBER(20)
  , from_address varchar(255) NOT NULL
  , to_address varchar(255) DEFAULT NULL
  , cc_address varchar(255) DEFAULT NULL
  , bcc_address varchar(255) DEFAULT NULL
  , subject varchar(255) DEFAULT NULL
  , body CLOB
  , sent_at TIMESTAMP DEFAULT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint send_mail_queue_pk PRIMARY KEY (send_mail_queue_id, created_at)
);

CREATE TABLE role_permissions(
  role_permission_id NUMBER(11) NOT NULL
  , role_key VARCHAR(100) NOT NULL
  , permission_id NUMBER(11) NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint role_permissions_pk PRIMARY KEY (role_permission_id)
);

CREATE TABLE holidays(
  holiday_id NUMBER(11) NOT NULL
  , holiday_name VARCHAR(100) NOT NULL
  , holiday_date DATE NOT NULL
  , created_by VARCHAR(50) NOT NULL
  , created_at TIMESTAMP NOT NULL
  , updated_by VARCHAR(50) DEFAULT NULL
  , updated_at TIMESTAMP DEFAULT NULL
  , deleted_by VARCHAR(50) DEFAULT NULL
  , deleted_at TIMESTAMP DEFAULT NULL
  , version NUMBER(11) DEFAULT 1 NOT NULL
  , constraint holidays_pk PRIMARY KEY (holiday_id)
);

create unique index holidays_pk_inx1 on holidays (holiday_name, deleted_at);
