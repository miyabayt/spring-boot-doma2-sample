# コード生成プラグイン（おまけ）

## プラグインの設定

以下のような設定を`build.gradle`に記述する。
各プロパティは、生成するソースのテンプレートで使用されているので、
出力したい内容に合わせて変更すること。

```groovy
apply plugin: com.sample.CodeGenPlugin

codegen {
    domainProjectName = "sample-domain" // ドメインのプロジェクト名
    webProjectName = "sample-web-admin" // メインプロジェクト（SpringBootのメインメソッドを実装している）

    commonDtoPackageName = "com.sample.domain.dto.common"             // IDなどの共通的なエンティティのパッケージ名
    daoPackageName = "com.sample.domain.dao"                          // Daoを配置するパッケージ名
    dtoPackageName = "com.sample.domain.dto"                          // Dtoを配置するパッケージ名
    servicePackageName = "com.sample.domain.service"                  // サービスを配置するパッケージ名
    commonServicePackageName = "com.sample.domain.service"            // サービスの基底クラスを配置するパッケージ名
    exceptionPackageName = "com.sample.domain.exception"              // 例外クラスを配置するパッケージ名
    webBasePackageName = "com.sample.web.base"                        // コントローラー関連の基底クラスを配置するパッケージ名
    baseValidatorPackageName = com.sample.domain.validator            // バリデーターを配置するパッケージ名
    baseControllerPackageName = "com.sample.web.base.controller.html" // コントローラーの基底クラスを配置するパッケージ名
    controllerPackageName = "com.sample.web.admin.controller.html"    // 生成するコントローラのパッケージ名
}
```

## プラグインの実行

下記のコマンドを実行すると最低限のソースファイルが生成される。

```bash
$ cd /path/to/sample-web-admin
$ gradlew codegen -PsubSystem=system -Pfunc=employee -PfuncStr=従業員
```

## 生成ファイル一覧

以下の階層で、それぞれのソースファイルが作成される。

```
.
├── sample-domain
│   └── src
│       └── main
│            ├── java
│            │   └── com
│            │        └── sample
│            │             └── domain
│            │                  ├── dao
│            │                  │   └── system
│            │                  │        └── EmployeeDao.java
│            │                  ├── dto
│            │                  │   └── system
│            │                  │        └── Employee.java
│            │                  └── service
│            │                       └── system
│            │                            └── EmployeeDervice.java
│            └── resources
│                 └── META-INF
│                     └── com
│                          └── sample
│                               └── domain
│                                    └── dao
│                                         └── EmployeeDao
│                                              ├── select.sql
│                                              ├── selectAll.sql
│                                              └── selectById.sql
└── sample-web-admin
     └── src
         └── main
              ├── java
              │   └── com
              │        └── sample
              │             └── web
              │                  └── admin
              │                       └── controller
              │                            └── html
              │                                 └── system
              │                                      └── employees
              │                                           ├── EmployeeCsv.java
              │                                           ├── EmployeeForm.java
              │                                           ├── EmployeeFormValidator.java
              │                                           ├── EmployeeHtmlController.java
              │                                           └── SearchEmployeeForm.java
              └── resources
                   └── templates
                        └── modules
                             └── system
                                  └── employees
                                       ├── find.html
                                       ├── new.html
                                       └── show.html
```


```eval_rst
.. note::
   最小限のソースのみを生成するようになっているので、
   必要に応じて生成元のテンプレートを編集したり、プラグイン自体を改修しする。
   テンプレートは、buildSrc/src/resources/templatesに格納されている。
```
