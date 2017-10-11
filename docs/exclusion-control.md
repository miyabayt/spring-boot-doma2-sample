# 排他制御

## 楽観的排他制御

サンプルの実装では、楽観的排他制御は下記の流れで動作するようになっている。

1. 画面表示処理で、改定番号を含めてSELECT文を発行する。
2. SessionAttributeに指定されたFormオブジェクトを使って、改定番号をセッションに保存する。
3. 保存ボタンが押下された後、保存処理でセッションに保存された改定番号をWHERE句に指定してUPDATE文を発行する。
4. 更新件数が0件の場合は、排他エラーとする。

共通のエンティティに、排他制御用のフィールドを定義している。
複数のテーブルに対しての同時の排他制御は、ここでは取り扱わない。

楽観的排他制御は、Doma2の機能を利用しているので下記のドキュメントを参照されたい。
[Doma 2.0 SQLの自動生成による更新](http://doma.readthedocs.io/ja/stable/query/update/#sql)

```java
public abstract class DomaDtoImpl implements DomaDto, Serializable {
    // ..

    // 楽観的排他制御で使用する改定番号
    @Version // ★楽観的排他制御に使う項目であることを示す
    @Column(name = "version") // ★DBのカラム名
    @JsonIgnore // レスポンスするJSONに含めない項目
    Integer version;
}
```

## 悲観的排他制御

悲観的排他制御についても、Doma2の機能を利用しているので、下記のドキュメントを参照されたい。
[Doma 2.0 検索 - 悲観的排他制御](http://doma.readthedocs.io/ja/stable/query/select/#id13)

ページング処理と同様に、SelectOptionsを作成し、`forUpdate`メソッドを呼び出してからdaoの引数に渡すことで、
SELECT ...FOR UPDATE文が発行される。

```java
// 悲観的排他制御
val options = createSearchOptions(pageable).forUpdate(); // ★Pageableを元にDoma2のSelectOptionsを作成する
val users = userDao.selectAll(where, options, toList()); // ★SELECT ...FOR UPDATE
```
