# 二重送信防止

## 二重送信防止とは

Webアプリケーションでは、以下の操作を行うと同じ処理が2回実行されて、
データが重複することがある。

1. 登録ボタンを連続して押下する。
2. 登録処理が終わった後、ブラウザの再読み込みボタンを押すことで、前回の処理が再度実行される。
3. 登録完了画面に遷移してから、ブラウザの戻るボタンを押すことで、登録処理が再実行される。

## 対策方法

下記の対策をすると二重送信がある程度防止できる。

* Javascriptを使ってボタンを連続して押せないようにする。
* Post-Redirect-Getパターンを利用して、フォームの再送信をできないようにする。
* トークンによる制御
    1. 画面表示のタイミングでトークンを払い出す。
    2. 登録ボタンを押下する。（画面に埋め込まれたトークンも送信）
    3. サーバーで払い出したトークンと、画面から渡ったトークンを比較して一致しなければ不正とする。
    4. 正常に処理した場合は、トークンを破棄する。

## サンプルの実装での例

サンプルの実装では、下記の流れで、
二重送信防止チェックを行っている。

* `DoubleSubmitCheckingRequestDataValueProcessor`がFormタグにトークンを埋め込む。

```java
public class DoubleSubmitCheckingRequestDataValueProcessor implements RequestDataValueProcessor {
    // ...

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        val map = PROCESSOR.getExtraHiddenFields(request);
        String token = DoubleSubmitCheckToken.getExpectedToken(request);
        if (token == null) {
            token = DoubleSubmitCheckToken.renewToken(request);
        }

        if (!map.isEmpty()) {
            // ★トークンを埋め込む
            map.put(DoubleSubmitCheckToken.DOUBLE_SUBMIT_CHECK_PARAMETER, token);
        }
        return map;
    }

    // ...
}
```

* `SetDoubleSubmitCheckTokenInterceptor`で、POSTメソッドのリクエストをインターセプトして、トークンの比較を行う。
    - 画面からトークンが渡ってきていない場合は、新しいトークンを生成して内部的に保持しておく。
    - トークンの比較で同一の場合は、そのまま処理を続ける。

```java
public class SetDoubleSubmitCheckTokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        val expected = DoubleSubmitCheckToken.getExpectedToken(request);
        val actual = DoubleSubmitCheckToken.getActualToken(request);
        DoubleSubmitCheckTokenHolder.set(expected, actual);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            // POSTされたときにトークンが一致していれば新たなトークンを発行する
            val expected = DoubleSubmitCheckToken.getExpectedToken(request);
            val actual = DoubleSubmitCheckToken.getActualToken(request);

            if (expected != null && actual != null && Objects.equals(expected, actual)) {
                DoubleSubmitCheckToken.renewToken(request);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
        DoubleSubmitCheckTokenHolder.clear();
    }
}
```

* Doma2のリスナー`DefaultEntityListener`で、トークンの比較を行う。
    - INSERT文を発行する前のタイミング（preInsert）でトークンの比較を行い、一致しない場合は、例外をスローする。

```java
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

    @Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
        // 二重送信防止チェック
        val expected = DoubleSubmitCheckTokenHolder.getExpectedToken();
        val actual = DoubleSubmitCheckTokenHolder.getActualToken();

        if (expected != null && actual != null && !Objects.equals(expected, actual)) {
            throw new DoubleSubmitErrorException(); // ★一致しない場合は、例外をスローする
        }

        // ...
    }

    // ...
}
```

* `HtmlExceptionHandler`で`DoubleSubmitErrorException`をハンドリングする。
    - 二重送信を検知した旨のメッセージをFlashMapに設定して、元の画面にリダイレクトする。

```java
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
public class HtmlExceptionHandler {
    // ...

    @ExceptionHandler({ DoubleSubmitErrorException.class })
    public RedirectView handleDoubleSubmitErrorException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {

        // 共通メッセージを取得する
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = DOUBLE_SUBMIT_ERROR; // ★二重送信エラーのメッセージ
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }
    // ...
}
```
