# メール送信

## JavaMailSenderを利用する

サンプルの実装では、`JavaMailSender`を使ってメールを送信するヘルパークラスを実装している。
メール本文のテンプレートは、データベースに持たせることを想定しているので、
まず本文をテンプレートエンジン（Thymeleaf）に掛けてから、
メール送信メソッドの引数に渡す流れで処理する。

```java
@Component
@Slf4j
public class SendMailHelper {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * メールを送信します。
     *
     * @param fromAddress
     * @param toAddress
     * @param subject
     * @param body // テンプレートエンジンでプレースホルダーを埋めた文字列
     */
    public void sendMail(String fromAddress, String[] toAddress, String subject, String body) {
        // ...
    }

    /**
     * 指定したテンプレートのメール本文を返します。
     *
     * @param template // ThymeleafのTEXT型の文法で書かれたテンプレート文
     * @param objects // テンプレートのプレースホルダーを埋めるための変数
     * @return
     */
    public String getMailBody(String template, Map<String, Object> objects) {
        // ...
    }
}
```
