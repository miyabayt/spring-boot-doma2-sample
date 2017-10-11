# 例外のハンドリング

## 共通処理で例外をハンドリングする

サンプルの実装では、`@ControllerAdvice`アノテーションを指定した例外ハンドラーで、
共通的に例外をハンドリングしている。
新しく例外クラスを作成する場合は、共通的にハンドリングするべきものであれば、
本クラスにハンドラーメソッドを追加すること。

```java
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
public class HtmlExceptionHandler {
    // ...
}
```

```eval_rst
.. note::
   業務ロジックで発生する機能固有の例外は、業務ロジックの中でハンドリングして、
   適切なメッセージを画面に表示することが望ましい。
```
