$(function() {
    var options = {
        rules: {
            categoryCode: {
                required: true,
                maxlength: 100
            },
            categoryValue: {
                required: true,
                maxlength: 100
            }
        }
    };

    // 初期化
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
