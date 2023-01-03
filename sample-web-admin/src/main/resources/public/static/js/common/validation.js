$(function() {
    // メッセージを上書きする
    $.extend($.validator.messages, {
        minlength: $.validator.format("{0}文字以上の文字を入力してください"),
        maxlength: $.validator.format("{0}文字以下の文字を入力してください"),
        rangelength: $.validator.format("{0}文字から{1}文字の値を入力してください。"),
        required: "必須項目です。入力してください。",
        equalTo: "正しい値を入力してください。",
        email: "正しいメールアドレスを入力してください。",
        digits: "数値を入力してください。",
        date: "日付はyyyy/MM/dd形式で入力してください。",
        extension: "正しい形式のファイルを選択してください。"
    });

    $.validator.setDefaults({
        // サーバー側バリデーションと同じ見た目にする
        errorElement: "span",
        errorClass: "help-block",
        // エラーメッセージ出力先
        errorPlacement: function(error, element) {
            $(element).next(".help-block").remove();
            $(element).addClass("is-invalid");
            $(element).closest(".form-group").addClass("has-error");
            error.insertAfter(element);
        },
        success: function(error, element) {
            $(element).closest(".form-group").removeClass("has-error");
            $(element).removeClass("is-invalid");
            $(element).next(".help-block").remove();
            $(error).remove();
        },
        onkeyup: function(element, event ) {
            // Avoid revalidate the field when pressing one of the following keys
			// Shift       => 16
			// Ctrl        => 17
			// Alt         => 18
			// Caps lock   => 20
			// End         => 35
			// Home        => 36
			// Left arrow  => 37
			// Up arrow    => 38
			// Right arrow => 39
			// Down arrow  => 40
			// Insert      => 45
			// Num lock    => 144
			// AltGr key   => 225
			var excludedKeys = [
				16, 17, 18, 20, 35, 36, 37,
				38, 39, 40, 45, 144, 225
			];

			if (event.which === 9 && this.elementValue(element) === "" || $.inArray(event.keyCode, excludedKeys) !== -1) {
				return;
			} else if (element.name in this.submitted || element.name in this.invalid) {
				this.element(element);
			}

            $(element).valid();
        },
        onfocusout: function(element) {
            if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                this.element(element);
            }

            $(element).valid();
        },
        invalidHandler: function(form, validator) {
            $("#modal-validation").modal();
        },
        submitHandler: function(form){
            form.submit();
        }
    });

    $.validator.methods.email = function(value, element) {
        return isOptionalInput(this, value, element) || this.optional(element) || (!value.includes(".@") && /^([\w-\.]+@([\w-]+\.)+[\w-]{2,20})?$/.test(value));
    };

    $.validator.addMethod("decimal", function(value, element) {
        return isOptionalInput(this, value, element) || this.optional(element) || /^(0|[1-9]\d*)\.\d+$/.test(value);
    }, "小数を入力してください。");

    $.validator.addMethod("dateFormat", function(value, element) {
        return isOptionalInput(this, value, element) || this.optional(element) || /^\d{4}\/\d{2}\/\d{2}$/.test(value);
    }, "日付はyyyy/MM/dd形式で入力してください。");

    function isOptionalInput(_this, value, element) {
        var rules = $(element).rules();
        var required = (_this.objectLength(rules)) ? rules.required : false;

        if (!required && _this.elementValue(element) === "") {
            // 必須入力ではない場合
            return true;
        }

        return false;
    }
});
