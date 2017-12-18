$(document).ready(function () {
    var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    $("#quickSearch").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: contextPath + '/quickSearch',
                method: 'POST',
                data: {
                    q: request.term
                },
                success: function (data) {
                    response($.map(data, function (account, i) {
                        var result = account.name;
                        var middleName = account.middleName;
                        if (middleName !== null) {
                            result += ' ' + middleName;
                        }
                        result += ' ' + account.surName;

                        return {
                            data: account.id,
                            value: result
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            event.preventDefault();
            window.location.href = contextPath + '/account?id=' + ui.item.data;
        },
        focus: function (event, ui) {
            event.preventDefault();
        },
        open: function () {
            $('.ui-menu').width(385)
        }
    });
});
