$(document).ready(function () {
    var $contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    $("#quickSearch").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: $contextPath + '/quickSearch',
                method: 'POST',
                data: {
                    q: request.term
                },
                success: function (data) {
                    response($.map(data, function (account, i) {
                        return {
                            data: account.id,
                            value: account.name + ' ' + account.surName
                            // label: account.name + ' ' + account.surName // done: по клику на результат имя не должно подставляться в строку поиска
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            event.preventDefault();
            window.location.href = $contextPath + '/account?id=' + ui.item.data;
        },
        focus: function (event, ui) {
            event.preventDefault();
        },
        open: function () {
            $('.ui-menu').width(385)
        }
    });
});
// autoFocus: true,
// position: {my: "left top-14", at: "left bottom"},
// $.post('<%=request.getContextPath()%>' + '<c:url value="/quickSearch?q="/>', {q: request.term}), function (data) {
//     response($.map(data, function (account, i) {
//         return {value: account.id, label: account.name + ' ' + account.surName};
//     }));
// };
//
// $.post('<%=request.getContextPath()%>' + '<c:url value="/quickSearch?q="/>', {q: request.term}), function (data) {
//     response($.map(data, function (account, i) {
//         return {value: account.id, label: account.name + ' ' + account.surName};
//     }));
// };
