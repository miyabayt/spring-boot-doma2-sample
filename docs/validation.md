# バリデーション

## Bean Validation（単一項目チェック）

入力フォームのフィールドにアノテーションを指定する。

```java
@Getter
@Setter
public class UserForm {

    @NotEmpty // ★空でないことをチェック
    @Size(min=2, max=30) // ★文字列長の下限上限チェック
    public String name;

    @Email // ★メールアドレス書式チェック
    public String email;
}
```

コントローラーの引数に@Validatedアノテーションを指定する。

```java
public class UserController {

    @PostMapping("/new")
    public String newUser(@Validated UserForm form, BindingResult result, RedirectAttributes attributes) { // ★@Validatedを指定する
        if (result.hasErrors()) { // ★バリデーションエラーがある場合はTrueが返る
            setFlashAttributeErrors(attributes, result);
            return "redirect:/user/users/new";
        }
    }
}
```

画面にバリデーションエラーの内容を表示する。

```html
<form th:object="${userForm}" method="post">
    <div th:with="valid=${!#fields.hasErrors('name')}">
        <input type="text" th:field="*{'name'}" />
        <span th:if="${!valid}" th:errors="*{'name'}">Error</span><!-- エラーがある場合はメッセージが表示される -->
    </div>
</form>
```

## Spring Validator（相関チェック）

サンプルの実装では、`org.springframework.validation.Validator`を実装した基底クラスを用意しているので、
下記のようなバリデーターを実装すれば相関チェックができる。

```java
@Component // ★Autowireするため@Componentを指定する
public class UserFormValidator extends AbstractValidator<UserForm> { // ★ジェネリクスにForm型を指定する

    @Override
    protected void doValidate(UserForm form, Errors errors) { // ★ジェネリクスの型で引数を受け取る

        // 確認用パスワードと突き合わせる
        if (!equals(form.getPassword(), form.getPasswordConfirm())) { // ★任意にチェックする
            errors.rejectValue("password", "users.unmatchPassword"); // ★エラーがあればErrorsオブジェクトに追加する
            errors.rejectValue("passwordConfirm", "users.unmatchPassword");
        }
    }
}
```

```eval_rst
.. note::
    エラーメッセージは、ValidationMessages.propertiesに定義する。
```

コントローラーにFormオブジェクトごとにバリデーターを設定する。
単一項目チェックと同様に、`@Validated`アノテーションを指定しているとバリデーターが動作する。

```java
@Controller
public class UserController {
    // ...
    @Autowired
    UserFormValidator userFormValidator; // ★Spring Validatorの実装

    @InitBinder("userForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(userFormValidator); // ★userFormにバインドする
    }
    // ...
}
```

## アノテーションの自作

共通化したい場合は下記のようなアノテーションクラスを作成して利用できる。

```java
@Documented
@Constraint(validatedBy = { ZipCodeValidator.class }) // ★バリデーターを指定する
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
public @interface ZipCode {

    String message() default "{validator.annotation.ZipCode.message}"; // ★バリデーションでエラーになった場合のメッセージキー

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ZipCode[] value();
    }
}
```

アノテーションと対になるバリデーターを作成する。

```java
public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> { // ★どのアノテーションを対象とするか指定する

    static final Pattern p = Pattern.compile("^[0-9]{7}$");

    @Override
    public void initialize(ZipCode ZipCode) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        if (StringUtils.isEmpty(value)) {
            isValid = true;
        } else {
            Matcher m = p.matcher(value);

            if (m.matches()) {
                isValid = true;
            }
        }

        return isValid;
    }
}
```
