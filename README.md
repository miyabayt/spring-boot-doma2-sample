# Spring Boot Sample Application

## ローカル環境

ソースのダウンロード
```
git clone https://github.com/miyabayt/spring-boot-doma2-sample.git
```

### Docker APIの有効化

Windows10の場合
* Settings > General > `Expose daemon on tcp://...`をONにする。

MacOSXの場合
* デフォルトで`unix:///var/run/docker.sock`に接続できる。
* TCPでAPIを利用したい場合は、下記を実施する。

```
brew install socat
socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock &
```

### Dockerの起動

MySQLなどのサーバーを立ち上げる。

```
cd /path/to/spring-boot-doma2-sample
./gradlew startDockerContainer
```

### FakeSMTPの起動

メール送信のテストのためFakeSMTPを立ち上げる。

```
cd /path/to/spring-boot-doma2-sample
./gradlew startFakeSmtpServer
```
   
### アプリケーションの起動

デモ用アカウント

* test@sample.com / passw0rd

```
# admin application
cd /path/to/spring-boot-doma2-sample/sample-web-admin
./gradlew bootRun
    
# front application
cd /path/to/spring-boot-doma2-sample/sample-web-front
./gradlew bootRun
```

## 開発環境（IntelliJ）

### 必要なプラグイン・設定

* Lombok pluginをインストールする。
* Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`をONにする。
* Eclipse Code Formatterをインストールする。
* Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`をONにする。

### Tips

* Windowsの場合は、コンソール出力が文字化けするため、`C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`の中にある`idea64.exe.vmoptions`ファイルに`-Dfile.encoding=UTF-8`を追記する。

## 参考

|プロジェクト|概要|
|[Springframework](https://projects.spring.io/spring-framework/)|Spring Framework|
|[Spring Mobile](http://projects.spring.io/spring-mobile/)|a framework that provides capabilities to detect the type of device|
|[Apache Shiro](https://shiro.apache.org/)|Security, Session Management|
|[Doma2](https://doma.readthedocs.io/ja/stable/)|ORM|
|[spring-boot-doma2](https://github.com/domaframework/doma-spring-boot)||
|[Thymeleaf](http://www.thymeleaf.org/)|Template Engine|
|[WebJars](https://www.webjars.org/)|client-side web libraries (e.g. jQuery & Bootstrap) packaged into JAR|
|[ModelMapper](http://modelmapper.org/)|Object Mapping|
|[Apache Tika](https://tika.apache.org/)|detect and extract metadata|
|[Ehcache](http://www.ehcache.org/)|Java-based cache|
|[Spock](http://spockframework.org/)|unit test framework|
|[Mockito](http://site.mockito.org/)|mocking framework|

