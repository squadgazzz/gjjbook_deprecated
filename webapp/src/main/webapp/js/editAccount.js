$(document).ready(function () {
    checkAddButton();
    checkRemoveButton();
    maskPhoneNumber();
    modalController();
    $('form[name="main_form"]').validator();
    $('#birthDate').datepicker({
        dateFormat: "yy-mm-dd"
    });

});

function phoneFilter() {
    $('input[name$="number"]').keydown(function (e) {
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
    var phone = $('.phone').find('input[name$="number"]');
    phone.mask("+7 (999) 999 99 99");
    phone.keydown(function (e) {
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
    var $lastPhone = $('.phone:last');
    var $clone = $lastPhone.clone();

    $clone.find('input[name$="id"]').val("0");
    $clone.find('input[name$="number"]').val("+7");
    $clone.find('select[name$="type"]').val('MOBILE');
    $lastPhone.find('[name="add_button"]').css("display", "none");
    $lastPhone.after($clone);

    checkRemoveButton();
    checkAddButton();
    maskPhoneNumber();
    phoneFilter();
    updatePhonesInputNames();
    $('form[name="main_form"]').validator('update');
}

function updatePhonesInputNames() {
    $('input[name$="number"]').each(function (index) {
        $(this).attr('name', "phones[" + index + "].number");
        $(this).attr('id', "phones[" + index + "].number");
    });

    $('select[name$="type"]').each(function (index) {
        $(this).attr('name', "phones[" + index + "].type");
        $(this).attr('id', "phones[" + index + "].type");
    });
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