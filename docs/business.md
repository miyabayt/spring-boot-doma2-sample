# 業務ロジック

## 宣言的トランザクション管理

サンプルの実装では、`BaseTransactionalService`を継承することで、
宣言的トランザクション管理が適用される。トランザクション管理が不要な場合は、
`BaseService`を継承する。


```java
@Service
public class UserService extends BaseTransactionalService { // ★親クラスで@Transactionalを宣言済み

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    // ...

    // ★AOPでこのメソッドを囲うようにDBトランザクションの開始・終了が行われる
    // ★例外が発生した場合はロールバックされる
    public User create(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");

        // 1件登録
        userDao.insert(inputUser); // ★1つ目のテーブル

        // ロール権限紐付けを登録する
        val userRole = new UserRole();
        userRole.setUserId(inputUser.getId());
        userRole.setRoleCode("user");
        userRoleDao.insert(userRole); // ★2つ目のテーブル

        return inputUser;
    }
}

```

`BaseTransactionalService`を継承することで、漏れなく`@Transactional`の指定が行われる。
ただし、読み取り専用のメソッドには、`@Transactional(readOnly = true)`を指定する必要がある。

### ページング処理

```java
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<User> findAll(User where, Pageable pageable) { // ★何件ずつ取得するか、何ページ目を取得するかをPageableに設定して引数に渡す
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count(); // ★Pageableを元にDoma2のSelectOptionsを作成する
        val users = userDao.selectAll(where, options, toList());

        // ★SelectOptionsのcountメソッドを呼び出すと、件数取得とレコード取得が一つのSQLで行える
        return PageFactory.create(users, pageable, options.getCount()); // ファクトリメソッドにリストを渡してPageオブジェクトで包んで返す
    }
```

```eval_rst
.. note::
   画面側のプロジェクトがDoma2ライブラリを依存関係に持たなくてよくするため、Doma2のSelectOptionsを直接に受け取らないようにする。
   Pageインターフェースの実装を後から変更したくなった場合に一括修正しなくても済むようにファクトリメソットを利用する。
```
