# Spring Boot Sample Application

## ローカル環境

ソースのダウンロード
```bash
$ git clone https://github.com/miyabayt/spring-boot-doma2-sample.git
```

### 開発環境（IntelliJ）

#### 必要なプラグイン・設定

- Lombok pluginをインストールする。
  - Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`をONにする。
- Eclipse Code Formatterをインストールする。
  - Settings > Other Settings > Eclipse Code Formatter > `Use the Eclipse code formatter`をONにする。
    - `Eclipse Java Formatter config file`に`eclipse-formatter.xml`を指定する。
- bootRunを実行している場合でもビルドされるようにする。
  - Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`をONにする。
- Windowsの場合は、コンソール出力が文字化けするため、`C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`の中にある`idea64.exe.vmoptions`ファイルに`-Dfile.encoding=UTF-8`を追記する。
- ブラウザにLiveReload機能拡張をインストールする。
  - `http://livereload.com/extensions/`から各ブラウザの機能拡張をダウンロードする。

### Docker APIの有効化

#### Windows10の場合
* Settings > General > `Expose daemon on tcp://...`をONにする。

#### MacOSXの場合
* デフォルトで`unix:///var/run/docker.sock`に接続できる。
* TCPでAPIを利用したい場合は、下記を実施する。

```bash
$ brew install socat
$ socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock &
```

#### Docker Toolboxの場合
* 後述の`Dockerの起動`の手順を実施する。


### Dockerの起動
MySQLなどのサーバーを立ち上げる。

#### Windows10、MacOSXの場合
```bash
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew startDockerContainer
```

#### Docker Toolboxの場合
* `Docker CLI`でbuildとrunを実行する。
```bash
$ cd /path/to/spring-boot-doma2-sample/docker
$ docker build --no-cache --rm -t sample:latest .
```

* `application-development.yml`を編集する。
  * `spring.datasource.url`の`127.0.0.1:3306`を`192.168.99.100:3306`に変更する。
```bash
$ # 初回の場合
$ docker run -it -p 22:22 -p 3306:3306 --name sample sample:latest /usr/bin/supervisord
# dockerから抜ける
ctrl + pしてからctrl押しっぱなしでqを押す

$ # コンテナが存在する場合
$ docker start sample
```

### FakeSMTPの起動
メール送信のテストのためFakeSMTPを立ち上げる。

```bash
$ cd /path/to/spring-boot-doma2-sample
$ ./gradlew startFakeSmtpServer
```

### アプリケーションの起動

#### 管理側
```bash
$ # admin application
$ cd /path/to/spring-boot-doma2-sample/sample-web-admin
$ ../gradlew bootRun
```

#### フロント側
```
$ # front application
$ cd /path/to/spring-boot-doma2-sample/sample-web-front
$ ../gradlew bootRun
```

#### バッチ
```
$ # 担当者情報取り込みバッチを起動する
$ cd /path/to/spring-boot-doma2-sample/sample-batch
$ ../gradlew bootRun -Pargs="--job=importStaffJob"
```

### 接続先情報
#### テストユーザー test@sample.com / passw0rd

| 接続先    | URL                                      |
| :----- | :--------------------------------------- |
| 管理側画面  | http://localhost:18081/admin             |
| 管理側API | http://localhost:18081/admin/api/v1/users.json |
| フロント側  | http://localhost:18080/                  |

#### データベース接続先

```bash
# Windows10、MacOSXの場合
mysql -h 127.0.0.1 -P 3306 -uroot -ppassw0rd sample

# Docker Toolboxの場合
mysql -h 192.168.99.100 -P 3306 -uroot -ppassw0rd sample
```



## 参考

| プロジェクト                                   | 概要                               |
| :--------------------------------------- | :------------------------------- |
| [Lombok Project](https://projectlombok.org/) | 定型的なコードを書かなくてもよくする               |
| [Springframework](https://projects.spring.io/spring-framework/) | Spring Framework                 |
| [Spring Security](https://projects.spring.io/spring-security/) | セキュリティ対策、認証・認可のフレームワーク           |
| [Spring Mobile](http://projects.spring.io/spring-mobile/) | モバイルデバイス検知、解像度検知を行うフレームワーク       |
| [Doma2](https://doma.readthedocs.io/ja/stable/) | O/Rマッパー                          |
| [spring-boot-doma2](https://github.com/domaframework/doma-spring-boot) | Doma2とSpring Bootを連携する           |
| [Flyway](https://flywaydb.org/)          | DBマイグレーションツール                    |
| [Thymeleaf](http://www.thymeleaf.org/)   | テンプレートエンジン                       |
| [Thymeleaf Layout Dialect](https://ultraq.github.io/thymeleaf-layout-dialect/) | テンプレートをレイアウト化する                  |
| [WebJars](https://www.webjars.org/)      | jQueryなどのクライアント側ライブラリをJARとして組み込む |
| [ModelMapper](http://modelmapper.org/)   | Beanマッピングライブラリ                   |
| [Ehcache](http://www.ehcache.org/)       | キャッシュライブラリ                       |
| [Spock](http://spockframework.org/)      | テストフレームワーク                       |
| [Mockito](http://site.mockito.org/)      | モッキングフレームワーク                     |

