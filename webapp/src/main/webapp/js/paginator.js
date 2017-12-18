var activeTabName = null;

function tabClicker() {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        activeTabName = e.target;
        alert(activeTabName.innerHTML);
    });
}

function paginateResults() {
    var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    var pageSize = $('#pageSize').val();
    var searchResultCount = $('#searchResultCount').val();
    var query = $('#query').val();
    activeTabName = document.getElementsByClassName("nav nav-tabs")[0].getElementsByClassName("active")[0].textContent;

    tabClicker();

    if (searchResultCount > pageSize) {
        var url = contextPath;
        switch (activeTabName) {
            case "My friends":
                url+='/'
        }
        $('#compact-pagination').pagination({
            items: searchResultCount,
            itemsOnPage: pageSize,
            cssStyle: 'light-theme',
            onPageClick: function (pageNumber) {
                $.ajax({
                    url: url,
                    method: 'POST',
                    data: {
                        q: query,
                        currentPage: pageNumber,
                        pageSize: pageSize
                    },
                    success: function (data) {
                        var foundAccountsCount = data.length;
                        var pageAccounts = document.getElementsByName("foundAccount");
                        var pageAccountsCount = pageAccounts.length;

                        if (foundAccountsCount < pageAccountsCount) {
                            for (var i = 0; i < pageAccountsCount - foundAccountsCount; i++) {
                                $('a[name="foundAccount"]:last').remove();
                            }
                        } else if (foundAccountsCount > pageAccountsCount) {
                            for (var k = 0; k < foundAccountsCount - pageAccountsCount; k++) {
                                var lastAccount = $('a[name="foundAccount"]:last');
                                var clone = lastAccount.clone();
                                lastAccount.after(clone);
                            }
                        }

                        $.each(data, function (i, account) {
                            var avatar = "data:image/jpeg;base64," + account.stringAvatar;
                            var accountUrl = contextPath + "/account?id=" + account.id;
                            var accountName = account.name + " " + account.middleName + " " + account.surName;

                            document.getElementsByName("accountAvatar")[i].setAttribute("src", avatar);
                            document.getElementsByName("foundAccount")[i].setAttribute("href", accountUrl);
                            document.getElementsByName("accountName")[i].childNodes[0].textContent = accountName;
                        });
                    }
                })
            }
        });
    } else {
        $('object[name="paginator"]').hide();
    }
}

$(document).ready(function () {
    paginateResults();
});


