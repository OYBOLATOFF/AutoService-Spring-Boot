// ID текущих мастеров и услуг
var currentMasterID = null;
var currentServiceID = null;

// Подгрузка данных для таблиц
var mastersTable = undefined;
var servicesTable = undefined;

var usingMasterParams = false; // Флаг говорит, нужно ли учитывать параметры при выводе людей
var usingServiceParams = false; // Флаг говорит, нужно ли учитывать параметры при выводе людей

// ----------------------------------------------------------------------------------------------------- //

// Удаление строчки с мастером
function removeSpecialistFromTableByID(currentMasterID) {
    $(getSpecialistRowByID(currentMasterID)).remove();
}

// Получение строки из таблицы мастеров по ID мастера
function getSpecialistRowByID(currentMasterID) {
    var result = undefined;
    $('.specialist_row').each(function(index, tr) {
        var masterID = $(this).children('td').eq(4).text();
        if (masterID == currentMasterID) {
            result = tr;
        }
    })

    return result;
}

// Вывод уведомления при изменении данных мастера
async function showSpecialistUpdateMessage(message, successfull) {
    var tempClassForNotification = successfull? 'notification_success': 'notification_error';
    $('#master_info_error').text(message); $('#master_info_error').addClass(tempClassForNotification);
    setTimeout(() => $('#master_info_error').removeClass(tempClassForNotification), 4000);
}

// Удаление мастера
$('#master_delete_btn').click(function (e) {
    $.ajax({
        type: 'DELETE',
        url: `http://localhost:8081/api/specialists/${currentMasterID}`,

        success: function(data) {
            $('#master_popup').removeClass('active');
            $('body').removeClass('blocked'); // Снимаем блокировку скролла страницы
            removeSpecialistFromTableByID(currentMasterID); // Удаляем строчку в таблице
        },

        error: function(data) {
            alert('Что-то пошло не так при удалении!');
        },

        cache: false
    });
});

function findIdOfService(val) {
    var id = null
    $('.service_row').each(function(index, tr) {
        // alert( $(this).children('td').eq(4).text()+' | '+val );
        if ($(this).children('td').eq(0).text() == val) {
            id = $(this).children('td').eq(4).text()
        }
    })

    return id;
}

// Изменение данных мастера
$('#master_change_info_btn').click(function(e) {
    $.ajax({
        type: 'PUT',
        url: `http://localhost:8081/api/specialists`,

        data: JSON.stringify({
            'id': currentMasterID,
            'firstName': $('#master_first_name_input').val(),
            'lastName': $('#master_last_name_input').val(),
            'specializationId': findIdOfService($('#master_specialization_input').val()),
            'workExperience': $('#master_experience_input').val(),
        }),

        complete: function(data) {
            if (data.status == 200) {
                // Изменяем имя в таблице
                generateMastersTable();
            } else {
                showSpecialistUpdateMessage('Какое-то поле введено некорректно', false);
            }
        },

        contentType: "application/json",
        dataType: 'json',
        cache: false
    });
})

// Изменение фотографии мастера
var changeMasterAvatarButton = document.getElementById('changeMasterAvatarButton');
$('#selectMasterImage').on('input', function () {
    changeMasterAvatarButton.click();
});

// Изменение данных услуги
$('#service_change_info_btn').click(function(e) {
    $.ajax({
        type: 'PUT',
        url: `http://localhost:8081/api/services`,

        data: JSON.stringify({
            'id': currentServiceID,
            'title': $('#service_title_input').val(),
            'description': $('#service_description_input').val(),
            'priceWithoutDiscount': $('#service_price_without_discount_input').val(),
            'priceWithDiscount': $('#service_price_with_discount_input').val(),
        }),

        complete: function(data) {
            if (data.status == 200) {
                // Изменяем имя в таблице
                generateServicesTable();
            } else {
                showServiceUpdateMessage('Какое-то поле введено некорректно', false);
            }
        },

        contentType: "application/json",
        dataType: 'json',
        cache: false
    });
})

$('#service_delete_btn').click(function (e) {
    $.ajax(
        {
            type: 'DELETE',
            url: 'http://localhost:8081/api/services',
            data: jQuery.param({id: currentServiceID}),
            complete: function(params) {
                if (params.status == 200) {
                    generateServicesTable()
                }
            }
        }
    )
})

// Изменение фотографии услуги
var changeServiceAvatarButton = document.getElementById('changeServiceAvatarButton');
$('#selectServiceImage').on('input', function () {
    changeServiceAvatarButton.click();
});

// Добавление услуги
$('#add_service_btn').click(function (e) {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8081/api/services',
        data: JSON.stringify({
            title: $('#add_service_title_input').val(),
            description: $('#add_service_description_input').val(),
            priceWithoutDiscount: $('#add_service_price_without_discount_input').val(),
            priceWithDiscount: $('#add_service_price_with_discount_input').val()
        }),
        contentType: 'application/json',
        complete: function (response) {
            if (response.status == 200) generateServicesTable();
            else if (response.status == 400) alert(response.responseText);
        }
    })
})

// Добавление мастера
$('#add_master_btn').click(function (e) {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8081/api/specialists',
        contentType: 'application/json',
        data: JSON.stringify({
            'firstName': $('#add_master_first_name_input').val(),
            'lastName': $('#add_master_last_name_input').val(),
            'specializationId': findIdOfService($('#add_master_specialization_input').val()),
            'workExperience': $('#add_master_experience_input').val(),
        }),
        complete: function (response) {
            if (response.status == 200) generateMastersTable();
            else if (response.status == 400) alert(response.responseText);
        }
    })
})

// Получить контент для таблицы мастеров
function generateMastersTable() {
    $.ajax({
        type: "get",
        url: `http://localhost:8081/api/specialists`,
        dataType: "dataType",
        complete: function (response) {
            if (response.status == 200) {
                mastersTable = JSON.parse(response.responseText);
                showMastersTable();
            }
        }
    });
}

// Отобразить таблицу с мастерами
function showMastersTable() {
    $('#specialists_tbody').html('');
    $.each(mastersTable, function(i, item) {
        var fullNameOK = $('#master_selection_fullname').val() === ''? true: (item.firstName+' '+item.lastName).includes($('#master_selection_fullname').val());
        var workExperienceOK = $('#master_selection_work_experience').val() === ''? true: (
            ( (item.workExperience == $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '=' ) ) ||
            ( (item.workExperience > $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '>' ) ) ||
            ( (item.workExperience < $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '<' ) )
        );
        var specializationOK = $('#master_selection_specialization').val() === 'all'? true: $('#master_selection_specialization').val() === item.specialization;
        if (!usingMasterParams || (usingMasterParams && fullNameOK && workExperienceOK && specializationOK)) {
            $('#specialists_tbody').append(
                `<tr class="specialist_row">
                <td>${item.firstName}</td>
                <td>${item.lastName}</td>
                <td>${item.specialization}</td>
                <td>${item.workExperience}</td>
                <td style="display: none">${item.id}</td>
                <td style="display: none">${item.portrait}</td>
            </tr>`
            );
        }
    })

    // К появившимся строкам привязываем попап при нажатии
    $('.specialist_row').click(function (e) {
        $('#master_popup').addClass('active');
        $('body').addClass('blocked');

        // Меняем значения карточки
        $('#master_first_name_input').val( $(this).children('td').eq(0).text() );
        $('#master_last_name_input').val( $(this).children('td').eq(1).text() );
        $('#master_specialization_input').val( $(this).children('td').eq(2).text() );
        $('#master_experience_input').val( $(this).children('td').eq(3).text() );
        currentMasterID = $(this).children('td').eq(4).text();
        $('#changeMasterAvatarForm').attr('action', `http://localhost:8081/api/changeMasterAvatar/${currentMasterID}`);

        var photoBase64 = $(this).children('td').eq(5).text();
        $('#master_photo').attr('src', photoBase64);
    });
}

// Автоматическое изменение таблицы  мастера при изменении параметров
$('#master_selection_fullname, #master_selection_work_experience, #master_selection_specialization, #master_selection_work_experience_select').on('input', function() {
    usingMasterParams = true;
    showMastersTable();
})

// Обновление таблицы мастера
$('.refresh_masters_table_icon').click(function(e) {
    generateMastersTable();
})

// Функция обновляет выпадающий список с сервисами
function updateSpecializationInput() {
    $('#master_specialization_input, #add_master_specialization_input').html('');
    $('#master_selection_specialization').html('<option value="all">Все услуги</option>');
    $.each(servicesTable, function(i, item) {
        $('#master_specialization_input, #master_selection_specialization, #add_master_specialization_input').append(`<option value="${item.title}">${item.title}</option>`)
    })
}

// Получить контент для таблицы услуг
function generateServicesTable() {
    $.ajax({
        type: "get",
        url: `http://localhost:8081/api/services`,
        dataType: "dataType",
        complete: function (response) {
            if (response.status == 200) {
                servicesTable = JSON.parse(response.responseText);
                showServicesTable();
                updateSpecializationInput();
            }
        }
    });
}

// Отобразить таблицу с услугами
function showServicesTable() {
    $('#services_tbody').html('');
    $.each(servicesTable, function(i, item) {
        var titleOK = $('#service_selection_title').val() === ''? true: (item.title).includes( $('#service_selection_title').val() );
        var priceOK = $('#service_selection_price').val() === ''? true: (
            ( (item.priceWithoutDiscount == $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '=' ) ) ||
            ( (item.priceWithoutDiscount > $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '>' ) ) ||
            ( (item.priceWithoutDiscount < $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '<' ) ) ||
            ( (item.priceWithDiscount == $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '=' ) ) ||
            ( (item.priceWithDiscount > $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '>' ) ) ||
            ( ((item.priceWithDiscount < $('#service_selection_price').val()) && (item.priceWithDiscount !== null)) && ( $('#service_selection_price_select').val() == '<' ) )
        );
        if (!usingServiceParams || (usingServiceParams && titleOK && priceOK)) {
            $('#services_tbody').append(
                `<tr class="service_row">
                <td>${item.title}</td>
                <td>${item.shortDescription}</td>
                <td>${item.priceWithoutDiscount === null? '': `${item.priceWithoutDiscount}$`}</td>
                <td>${item.priceWithDiscount === null? '': `${item.priceWithDiscount}$`}</td>
                <td style="display: none">${item.id}</td>
                <td style="display: none">${item.image}</td>
                <td style="display: none">${item.description}</td>
            </tr>`
            );
        }
    })

    $('.service_row').click(function(e) {
        $('#service_popup').addClass('active');
        $('body').addClass('blocked');

        currentServiceID = $(this).children('td').eq(4).text();
        $('#service_title_input').val( $(this).children('td').eq(0).text() );
        $('#service_description_input').val( $(this).children('td').eq(6).text() );
        $('#service_price_without_discount_input').val( $(this).children('td').eq(2).text().slice(0, -1) );
        $('#service_price_with_discount_input').val( $(this).children('td').eq(3).text().slice(0, -1) );
        $('#changeServiceAvatarForm').attr('action', `http://localhost:8081/api/changeServiceAvatar/${currentServiceID}`);

        var photoBase64 = $(this).children('td').eq(5).text();
        $('#service_photo').attr('src', photoBase64);
    })
}

// Вывод уведомления при изменении данных услуги
async function showServiceUpdateMessage(message, successfull) {
    var tempClassForNotification = successfull? 'notification_success': 'notification_error';
    $('#service_info_error').text(message); $('#service_info_error').addClass(tempClassForNotification);
    setTimeout(() => $('#service_info_error').removeClass(tempClassForNotification), 4000);
}

// Автоматическое изменение таблицы услуг при изменении параметров
$('#service_selection_title, #service_selection_price, #service_selection_price_select').on('input', function() {
    usingServiceParams = true;
    showServicesTable();
})

// Обновление таблицы услуг
$('.refresh_services_table_icon').click(function(e) {
    generateServicesTable();
})

$('#add_service_button').click(function (e) {
    $('#add_service_popup').addClass('active');
    $('body').addClass('blocked');
})

$('#add_master_button').click(function (e) {
    $('#add_master_popup').addClass('active');
    $('body').addClass('blocked');
})

// ----------------------------------------------------------------------------------------------------- //

// Действия после загрузки страницы
$(function () {
    generateMastersTable(); // Получаем всех мастеров и выводим в таблицу
    generateServicesTable(); // Получаем все услуги и выводим в таблицу
});