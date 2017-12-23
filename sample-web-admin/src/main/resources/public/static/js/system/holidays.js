$(function() {
    var options = {
        rules: {
            holidayName: {
                required: true,
                maxlength: 50
            }
        }
    };

    // 初期化
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
