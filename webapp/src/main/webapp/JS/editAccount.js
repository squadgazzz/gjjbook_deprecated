$(document).ready(function () {
    checkAddButton();
    checkRemoveButton();
    maskPhoneNumber();
    modalController();
    $('form[name="main_form"]').validator();
    $("#birthDate").datepicker({
        dateFormat: "yy-mm-dd"
    });

});

function phoneFilter() {
    $('input[name="number"]').keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
            // Allow: Ctrl+A, Command+A
            (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
            // Allow: home, end, left, right, down, up
            (e.keyCode >= 35 && e.keyCode <= 40)) {
            // let it happen, don't do anything
            return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
}

function validateForm() {
    var hasErrors = $('form[name="main_form"]').validator('validate').has('.has-error').length;
    if (!hasErrors) {
        $('#confirm_modal').modal('show');
    }
}

function modalController() {
    $('#confirm_modal').find('.btn-success').click(function () {
        $('form[name="main_form"]').submit();
    });
}

function maskPhoneNumber() {
    $('.phone').find('input[name="number"]').mask("+7 (999) 999 99 99");
    $('.phone').find('input[name="number"]').keydown(function (e) {
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
    var $clone = $('.phone:last').clone();
    $clone.find('input[name="number"]').val("+7");
    $clone.find('#type').val('MOBILE');

    $('.phone:last').find('[name="add_button"]').css("display", "none");
    $('.phone:last').after($clone);

    checkRemoveButton();
    checkAddButton();
    maskPhoneNumber();
    phoneFilter();
    $('form[name="main_form"]').validator('update');
}

function removeButtonClick(element) {
    $(element).parents('.phone').remove();
    checkAddButton();
    checkRemoveButton();
}

function checkAddButton() {
    var maxPhones = 5;
    var rowCount = $('.phone').length;
    if (rowCount < maxPhones) {
        $('.phone:last').find('[name="add_button"]').css("display", "inline");
    } else {
        $('.phone:last').find('[name="add_button"]').css("display", "none");
    }
}

function checkRemoveButton() {
    var rowCount = $('.phone').length;
    if (rowCount === 1) {
        $('.phone').find('[name="remove_button"]:first').css("display", "none");
    } else {
        $('.phone').find('[name="remove_button"]').css("display", "inline");
    }
}