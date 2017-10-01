$(document).ready(function () {
    checkAddButton();
    checkRemoveButton();
    maskPhoneNumber();
    modalController();
    $('form[name="main_form"]').validator();
    $("#birth_date").datepicker({
        dateFormat: "yy-mm-dd"
    });
});

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
    $('.phone').find('input[name="phone"]').mask("+7 (999) 999 99 99");
    $('.phone').find('input[name="phone"]').keydown(function (e) {
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
    $clone.find('input[name="phone"]').val("+7");
    $clone.find('#phone_type').val('MOBILE');

    $('.phone:last').find('[name="add_button"]').css("display", "none");
    $('.phone:last').after($clone);

    checkRemoveButton();
    checkAddButton();
    maskPhoneNumber();
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