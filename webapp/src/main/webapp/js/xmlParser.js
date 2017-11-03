$(document).ready(function () {

});
function exportToXml() {
    var accountId = $('input[name="id"]').val();
    var xml = $('<account></account>');
    var data = [];
    data.push("\<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    data.push("<account>\n");
    data.push("\t<id>" + accountId + "</id>\n");
    data.push("\t<name>" + $('#name').val() + "</name>\n");
    data.push("\t<middleName>" + $('#middleName').val() + "</middleName>\n");
    data.push("\t<surName>" + $('#surName').val() + "</surName>\n");
    data.push("\t<gender>" + $('input[name="gender"]').val() + "</gender>\n");
    data.push("\t<birthDate>" + $('#birthDate').val() + "</birthDate>\n");
    data.push("\t<phones>\n");
    $('.phone').each(function () {
        data.push("\t\t<phone>\n");
        data.push("\t\t\t<type>" + $(this).find('select[name$="type"]').val() + "</type>\n");
        data.push("\t\t\t<number>" + $(this).find('input[name$="number"]').val() + "</number>\n");
        data.push("\t\t</phone>\n");
    });
    data.push("\t<phones>\n");
    data.push("\t<homeAddress>" + $('#homeAddress').val() + "</homeAddress>\n");
    data.push("\t<workAddress>" + $('#workAddress').val() + "</workAddress>\n");
    data.push("\t<icq>" + $('#icq').val() + "</icq>\n");
    data.push("\t<skype>" + $('#skype').val() + "</skype>\n");
    data.push("\t<additionalInfo>" + $('#additionalInfo').val() + "</additionalInfo>\n");
    data.push("\t<email>" + $('#email').val() + "</email>\n");
    data.push("\t<password>" + $('#password').val() + "</password>\n");
    data.push("</account>\n");

    var file = new File(data, "AccountId" + accountId + ".xml", {type: 'plain/xml'});
    var url = URL.createObjectURL(file);
    var toXmlDownloadFile = document.getElementsByClassName("to-xml-download-file")[0];
    toXmlDownloadFile.href = url;
    toXmlDownloadFile.download = "AccountId" + accountId + ".xml";
    toXmlDownloadFile.click();
}

function importFromXml() {
    var fileInput = $('#xmlInput');
    fileInput.val("");
    fileInput.click();

    fileInput.change(function () {
        var file = this.files[0];
        var fr = new FileReader();
        fr.readAsText(file);
        fr.onloadend = function () {
            var xmlData = fr.result;
            var parser = new DOMParser().parseFromString(xmlData, "text/xml");
            $('#name').val(getNodeValue(parser, "name"));
            $('#middleName').val(getNodeValue(parser, "middleName"));
            $('#surName').val(getNodeValue(parser, "surName"));
            $('input[name="gender"]').val(getNodeValue(parser, "gender"));
            $('#birthDate').val(getNodeValue(parser, "birthDate"));

            var xmlPhonesList = parser.getElementsByTagName("phone");
            var htmlPhonesList = $('.phone');
            var xmlPhonesCount = xmlPhonesList.length;
            var htmlPhonesCount = htmlPhonesList.length;
            if (xmlPhonesCount > htmlPhonesCount) {
                for (var k = 0; k < xmlPhonesCount - htmlPhonesCount; k++) {
                    $('.phone:last').find('button[name="add_button"]').click();
                }
            } else if (xmlPhonesCount < htmlPhonesCount) {
                for (var j = 0; j < htmlPhonesCount - xmlPhonesCount; j++) {
                    $('.phone:last').find('button[name="remove_button"]').click();
                }
            }

            htmlPhonesList = $('.phone');
            var i = 0;
            htmlPhonesList.each(function () {
                var xmlType = xmlPhonesList[i].getElementsByTagName("type")[0].childNodes[0].nodeValue;
                $(this).find('select[name$="type"]').val(xmlType);
                var xmlNumber = xmlPhonesList[i].getElementsByTagName("number")[0].childNodes[0].nodeValue;
                $(this).find('input[name$="number"]').val(xmlNumber);
                i++;
            });

            $('#homeAddress').val(getNodeValue(parser, "homeAddress"));
            $('#workAddress').val(getNodeValue(parser, "workAddress"));
            $('#icq').val(getNodeValue(parser, "icq"));
            $('#skype').val(getNodeValue(parser, "skype"));
            $('#additionalInfo').val(getNodeValue(parser, "additionalInfo"));
            $('#email').val(getNodeValue(parser, "email"));
            $('#password').val(getNodeValue(parser, "password"));
        };

        $('input[name="confirm-changes"]').click();
    });
}

function getNodeValue(parser, name) {
    return parser.getElementsByTagName(name)[0].childNodes[0].nodeValue;
}