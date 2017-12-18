$(document).ready(function () {
    var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    var loggedId = $('input[name="loggedId"]').val();
    var friendId = $('input[name="id"]').val();
    var friendButtons = document.getElementsByName("friend-button");
    var friendButtonOne = friendButtons[0];
    var friendButtonTwo = friendButtons[1];

    if (loggedId !== friendId) {
        $.ajax({
            url: contextPath + '/getFriendByPk',
            method: 'POST',
            data: {
                accountTwo: friendId
            },
            success: function (data) {
                if (data) {
                    var status = data.status;
                    var actionId = data.actionAccount.id;
                    loggedId = Number(loggedId);
                    switch (status) {
                        case 'PENDING':
                            if (actionId === loggedId) {
                                friendButtonOne.textContent = "Cancel request";
                            } else {
                                friendButtonOne.textContent = "Confirm friendship";
                                friendButtonTwo.textContent = "Decline friendship";
                                friendButtonTwo.style.display = "block";
                                // var clone = friendButton.cloneNode(true);
                                // clone.textContent = "Decline friendship";
                                // friendButton.parentElement.appendChild(clone);
                            }
                            break;
                        case 'ACCEPTED':
                            friendButtonOne.textContent = "Remove friend";
                            break;
                        case 'DECLINED':
                            if (actionId === loggedId) {
                                friendButtonOne.textContent = "Cancel request";
                            } else {
                                friendButtonOne.textContent = "Add friend(declined)";
                            }
                            friendButtonOne.textContent = "Declined";
                            break;
                        case 'BLOCKED':
                            friendButtonOne.textContent = "Blocked";
                            break;
                        default:
                            friendButtonOne.textContent = "Add friend";
                            break;
                    }
                } else {
                    friendButtonOne.textContent = "Add friend";
                }
            }
        })
    } else {
        friendButtonOne.textContent = "Edit profile";
        // friendButton.setAttribute("href", contextPath + "/editaccount?id=" + loggedId);
    }

    friendButtonOne.addEventListener("click", function () {
        friendButtonTrigger(friendButtonOne, contextPath, loggedId, friendId);
    }, false);

    friendButtonTwo.addEventListener("click", function () {
        friendButtonTrigger(friendButtonTwo, contextPath, loggedId, friendId);
    }, false);
});

function friendButtonTrigger(friendButton, contextPath, loggedId, friendId) {
    switch (friendButton.textContent) {
        case "Edit profile":
            window.location.href = contextPath + "/editaccount?id=" + loggedId;
            break;
        case "Add friend":
            addFriend(friendButton, contextPath, friendId);
            break;
        case "Confirm friendship":
            confirmFriend(friendButton, contextPath, friendId);
            break;
        case "Decline friendship":
            declineFriendShip(friendButton, contextPath, friendId);
            break;
        case "Cancel request":
            removeFriend(friendButton, contextPath, friendId);
            break;
        case "Remove friend":
            removeFriend(friendButton, contextPath, friendId);
            break;
        case "Add friend(declined)":
            confirmFriend(friendButton, contextPath, friendId);
            break;
    }
}

function declineFriendShip(friendButton, contextPath, friendId) {
    $.ajax({
        url: contextPath + "/declineFriend",
        method: 'POST',
        data: {
            accountTwo: friendId
        },
        success: function () {
            friendButton.style.display = "none";
        },
        error: function () {
            alert("fail1");
        }
    });
}

function removeFriend(friendButton, contextPath, friendId) {
    $.ajax({
        url: contextPath + "/removeFriend",
        method: 'POST',
        data: {
            accountTwo: friendId
        },
        success: function () {
            friendButton.textContent = "Add friend";
        },
        error: function () {
            alert("fail1");
        }
    });
}

function confirmFriend(friendButton, contextPath, friendId) {
    $.ajax({
        url: contextPath + "/confirmFriend",
        method: 'POST',
        data: {
            accountTwo: friendId
        },
        success: function () {
            friendButton.textContent = "Remove friend";
        },
        error: function () {
            alert("fail2");
        }
    });
}

function addFriend(friendButton, contextPath, friendId) {
    $.ajax({
        url: contextPath + "/addFriend",
        method: 'POST',
        data: {
            accountTwo: friendId
        },
        success: function () {
            friendButton.textContent = "Cancel request";
            // document.getElementsByName("friend-button")[0].textContent = "Add friend"
        },
        error: function () {
            alert("fail3");
        }
    });
}
