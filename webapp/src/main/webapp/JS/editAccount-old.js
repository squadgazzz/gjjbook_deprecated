$(document).ready(function () {
    checkAddButton();
    checkRemoveButton();
    maskPhoneNumber();
    modalController();
});

function modalController() {
    $('#confirm_modal').find('.btn-success').click(function () {
        $('[name="main_form"]').submit();
    });
}

function maskPhoneNumber() {
    $('#main_table').find('tr.phone_row').find('input[name="phone"]').mask("+7 (999) 999 99 99");
    $('#main_table').find('tr.phone_row').find('input[name="phone"]').keydown(function (e) {
        var oldvalue = $(this).val();
        var field = this;
        setTimeout(function () {
            if (field.value.indexOf('+7') !== 0) {
                $(field).val(oldvalue);
            }
        }, 1);
    });
}

function addButtonClick() {
    var $clone = $('#main_table').find('tr.phone_row:last').clone();
    $clone.find('input').val("+7");

    $('#main_table').find('tr.phone_row:last').find('[name="add_button"]').css("display", "none");
    $('#main_table').find('tr.phone_row:last').after($clone);

    checkRemoveButton();
    checkAddButton();
    maskPhoneNumber();
}

function removeButtonClick(element) {
    $(element).parents('tr').remove();
    checkAddButton();
    checkRemoveButton();
}

function checkAddButton() {
    var maxPhones = 5;
    var rowCount = $('#main_table').find('tr.phone_row').length;
    if (rowCount < maxPhones) {
        $('#main_table').find('tr.phone_row:last').find('td:last').find('[name="add_button"]').css("display", "inline");
    } else {
        $('#main_table').find('tr.phone_row:last').find('td:last').find('[name="add_button"]').css("display", "none");
    }
}

function checkRemoveButton() {
    var rowCount = $('#main_table').find('tr.phone_row').length;
    if (rowCount === 1) {
        $('#main_table').find('[name="remove_button"]:first').css("display", "none");
    } else {
        $('#main_table').find('[name="remove_button"]').css("display", "inline");
    }
}