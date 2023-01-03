# DBマイグレーション

## flywayのバージョンを変更する

`Spring Boot 1.5.6`では、`Flyway v3.2.1`が依存関係に入っているが、
下記の設定を`build.gradle`に指定することでバージョンを引き上げられる。
（`Flyway v4`では、リピータブルマイグレーションが利用できる）

```groovy
ext["flyway.version"] = "4.2.0"
```

## マイグレーションファイル

下記のような内容のマイグレーションファイル`R__1_create_tables.sql`を`src/main/resources/db/migration`に配置する。
`R`で始まるファイルは、リピータブルマイグレーションが実施される。

```sql
CREATE TABLE IF NOT EXISTS users(
  user_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ユーザID'
  , first_name VARCHAR(40) NOT NULL COMMENT '名前'
  , last_name VARCHAR(40) NOT NULL COMMENT '苗字'
  , email VARCHAR(100) DEFAULT NULL COMMENT 'メールアドレス'
  , password VARCHAR(100) DEFAULT NULL COMMENT 'パスワード'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '電話番号'
  , zip VARCHAR(20) DEFAULT NULL COMMENT '郵便番号'
  , address VARCHAR(100) DEFAULT NULL COMMENT '住所'
  , upload_file_id INT(11) unsigned DEFAULT NULL COMMENT '添付ファイル'
  , password_reset_token VARCHAR(50) DEFAULT NULL COMMENT 'パスワードリセットトークン'
  , token_expires_at DATETIME DEFAULT NULL COMMENT 'トークン失効日'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME NOT NULL COMMENT '登録日時'
  , updated_by VARCHAR(255) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(255) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (user_id)
  , KEY idx_users (email, deleted_at)
) COMMENT='ユーザー';
```

## flywayの設定

サンプルでは開発環境のみでの利用を想定する。
下記の設定を指定するとアプリケーションの起動時にFlywayのマイグレーションが実施される。

```properties
spring.flyway.enable=true
```
