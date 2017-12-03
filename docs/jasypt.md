# パスワード暗号化

## jasypt-spring-boot を利用する

- JavaConfig設定

    > JavaConfigで暗号化を有効にし、対象ファイルを列挙する

```java
@Configuration
@EnableCaching // JCacheを有効可する
@EnableEncryptableProperties //Passwordを暗号化する
@EncryptablePropertySources({@EncryptablePropertySource("classpath:application-development.yml")})
public class ApplicationConfig extends BaseApplicationConfig {

    @Override
    protected List<String> getCorsAllowedOrigins() {
        return Arrays.asList("*");
    }
}
```

- 暗号化文字列生成

    > [jasypt.jar](https://sourceforge.net/projects/jasypt/files/latest/download?source=files "")をダウンロードし、jarを実行して、暗号化文字列生成
    
```bash
PC537:lib hirosue$ java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=passw0rd password=masterkey algorithm=PBEWithMD5AndDES

----ENVIRONMENT-----------------

Runtime: Oracle Corporation Java HotSpot(TM) 64-Bit Server VM 25.144-b01



----ARGUMENTS-------------------

algorithm: PBEWithMD5AndDES
input: passw0rd
password: masterkey



----OUTPUT----------------------

VLYQAVEU+9huoNowwGeuLz7oGEVVPe65
```

- application.yml修正

```
spring:
    profiles: development
    messages:
        cache-seconds: -1
    datasource:
        platform: mysql
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/sample?characterEncoding=UTF-8
        username: root
        password: ENC(RoJB/D+AnvFr9kKGpzYgoiMzW83OiI4V) #passw0rd    <----- 変更!!!!!!
    session:
        jdbc:
            # spring-session-jdbcに必要なテーブルを作成する
            schema: classpath:org/springframework/session/jdbc/schema-mysql.sql
    resources:
        # キャッシュの無効化
        cache-period: 0
#↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 追加　ここから
jasypt:
    encryptor:
        password: masterkey
#↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 追加　ここまで
```