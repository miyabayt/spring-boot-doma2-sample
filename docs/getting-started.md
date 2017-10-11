# Getting started

## JDKのインストール

- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)をダウンロードしてインストールする。
- 環境変数`JAVA_HOME`を設定する。

## IDEのインストール

- Eclipseの場合
    1. Eclipseをダウンロードする。（[STS](https://spring.io/tools/sts)、[Pleiades](http://mergedoc.osdn.jp/)など）
    2. Gradleプラグインが入っていない場合はインストールする。
    3. [Lombok](https://projectlombok.org/download)をダウンロードする。
        * ダウンロードした`lombok.jar`をダブルクリックして起動する。
        * Eclipseが検出されていることを確認して「Install」ボタンを押下する。
    4. `eclipse.ini`ファイルに、「-javaagent:lombok.jar」が追記されるので、Eclipseを再起動する。

- IntelliJの場合
    1. Lombok pluginをインストールする。
        * Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`をONにする。
    2. Eclipse Code Formatterをインストールする。
        * Settings > Other Settings > Eclipse Code Formatter > `Use the Eclipse code formatter`をONにする。
        * `Eclipse Java Formatter config file`に`eclipse-formatter.xml`を指定する。
    3. bootRunを実行している場合でもビルドされるようにする。
        * Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`をONにする。
    4. Windowsの場合は、コンソール出力が文字化けするため、`C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`の中にある`idea64.exe.vmoptions`ファイルに`-Dfile.encoding=UTF-8`を追記する。

- 共通
    1. ブラウザにLiveReload機能拡張をインストールする。
        * `http://livereload.com/extensions/`から各ブラウザの機能拡張をダウンロードする。
