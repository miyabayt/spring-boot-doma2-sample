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

    $("#holidayDate").daterangepicker({
        singleDatePicker: true,
        locale: {
            applyLabel: '選択',
            cancelLabel: 'キャンセル',
            fromLabel: '開始日',
            toLabel: '終了日',
            weekLabel: 'W',
            daysOfWeek: moment.weekdaysMin(),
            monthNames: moment.monthsShort(),
            firstDay: moment.localeData()._week.dow
        }
    });
});
