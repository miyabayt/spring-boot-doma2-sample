$(function() {
    var options = {
        rules: {
            subject: {
                required: true,
                maxlength: 100
            },
            templateBody: {
                required: true
            }
        }
    };

    // 初期化
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
