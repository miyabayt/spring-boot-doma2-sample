Dropzone.autoDiscover = false;

$(function() {
    var myDropzone = new Dropzone("#myDropzone", {
        url: "/admin/uploadFiles/upload",
        paramName: "file",
        dictDefaultMessage: "ここにファイルをドラッグ＆ドロップしてください",
        maxFilesize: 20,
        addRemoveLinks : true,
        dictRemoveFile: "削除",
        autoProcessQueue: false
    });

    myDropzone.on("sending", function(file, xhr, formData) {
        formData.append("_csrf", $('input[name="_csrf"]').val());
    });

    myDropzone.on("queuecomplete", function(file) {
        myDropzone.options.autoProcessQueue = false;
        location.reload();
    });

    $("#form1").submit(function(e) {
        e.preventDefault();
        e.stopPropagation();
        myDropzone.options.autoProcessQueue = true;
        myDropzone.processQueue();
    });
});
