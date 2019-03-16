# 静的コンテンツ

## 静的コンテンツの置き場所

静的コンテンツの置き場所を設定ファイルに指定する。
デフォルト値は下記が設定されているので、`src/main/resources/static`、`src/main/resources/public`などに配置されたファイルは静的コンテンツとして公開される。

```properties
spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
```

## キャッシュコントロール

キャッシュする時間を設定する。24時間キャッシュする場合は、下記を設定する。

```properties
spring.resources.cache-period=86400
```

静的コンテンツファイルをバージョニングする。ファイル名を変更せずにファイル内容を変えた場合に、キャッシュが効かないようにする。
静的コンテンツの内容からMD5ハッシュ値を計算してファイル名にする。

下記の設定をすると自動的にファイル名にハッシュ値が含まれるようになる。

```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```

- 出力例

    ```html
    <!-- テンプレートは普通にファイルを指定する -->
    <link rel="shortcut th:href="@{/static/images/favicon.png}" />

    <!-- 下記のようにハッシュ値が自動で出力される -->
    <link rel="shortcut href="/admin/static/images/favicon-ca31b78daf0dd9a106bbf3c6d87d4ec7.png" />
    ```

## WebJars

WebJarsを使ってJavascript、CSSライブラリを管理する。
build.gradleにライブラリを追加する。

```groovy
compile "org.webjars:webjars-locator"
compile "org.webjars:jquery:2.2.4"
```

webjars-locatorを使えるようにする。

```java
public class WebAppConfig extends WebMvcConfigurerAdapter {
    // ...
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // webjarsをResourceHandlerに登録する
        registry.addResourceHandler("/webjars/**")
                // JARの中身をリソースロケーションにする
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                // webjars-locatorを使うためにリソースチェイン内のキャッシュを無効化する
                .resourceChain(false);
    }
}
```

webjars-locatorを使うとバージョン指定が不要になる。

```html
<!-- テンプレートにはバージョンを書かない -->
<link th:src="@{/webjars/jquery/jquery.min.js}" />

<!-- 下記に変換される（2.2.4を依存関係に指定しているため） -->
<link src="/admin/webjars/jquery/2.2.4/jquery.min.js" />
```

Jarファイルに内包されたGZip済みリソースを使うようにする。

```properties
spring.resources.chain.compressed=true
```

## セキュリティ

Spring Securityの設定で認証をかけないようにする。

```java
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // ...
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静的ファイルへのアクセスは認証をかけない
        web.ignoring()//
                .antMatchers("/webjars/**", "/static/**");
    }
}
```
