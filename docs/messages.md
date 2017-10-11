# メッセージ

## メッセージ定義ファイルの指定

```properties
spring.messages.basename=messages,ValidationMessages,PropertyNames # カンマ区切りで複数ファイルを指定できる
spring.messages.cache-seconds=-1 # -1でキャッシュが無効になる。本番環境ではある程度キャッシュする
spring.messages.encoding=UTF-8
```

サンプルの実装では下記の役割りでファイルを分割している。
1. messages.properties
    - 静的文言などのメッセージを定義する。
1. ValidationMessages.properties
    - バリデーションエラーのメッセージを定義する。
1. PropertyNames.properties
    - バリデーションエラーが発生した際に項目名を表示するための項目名を定義する。

## ロケールによるメッセージ切替

下記のBeanを定義することで、i18n対応が可能になる。
ロケールが「ja」の場合は、`messages_ja.properties`や`ValidationMessages_ja.properties`に定義したメッセージが使われる。

```java
@Bean
public LocaleResolver localeResolver() {
    // Cookieに言語を保存する
    val resolver = new CookieLocaleResolver();
    resolver.setCookieName("lang"); // cookieのlang属性に設定された値を利用してロケールを切り替える
    return resolver;
}

@Bean
public LocaleChangeInterceptor localeChangeInterceptor() {
    // langパラメータでロケールを切り替える
    val interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang"); // ロケール切替に使うパラメータ名を指定する
    return interceptor;
}
```
