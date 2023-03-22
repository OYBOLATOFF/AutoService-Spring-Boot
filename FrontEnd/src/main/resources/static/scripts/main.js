var mastersTable = undefined;
var servicesTable = undefined;
var basket = undefined;

var usingMasterParams = false; // Флаг говорит, нужно ли учитывать параметры при выводе людей

var usingServiceParams = false; // Флаг говорит, нужно ли учитывать параметры при выводе людей
function showMastersTable() {
    $('.specialists').html('');
    $.each(mastersTable, function (indexInArray, valueOfElement) {
        var fullNameOK = $('#master_selection_fullname').val() === ''? true: (valueOfElement.firstName+' '+valueOfElement.lastName).includes($('#master_selection_fullname').val());
        var workExperienceOK = $('#master_selection_work_experience').val() === ''? true: (
            ( (valueOfElement.workExperience == $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '=' ) ) ||
            ( (valueOfElement.workExperience > $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '>' ) ) ||
            ( (valueOfElement.workExperience < $('#master_selection_work_experience').val()) && ( $('#master_selection_work_experience_select').val() == '<' ) )
        );
        var specializationOK = $('#master_selection_specialization').val() === 'all'? true: $('#master_selection_specialization').val() === valueOfElement.specialization;
        if (!usingMasterParams || (usingMasterParams && fullNameOK && workExperienceOK && specializationOK)) {
            $('.specialists').append(`
                <div class="specialist">
                    <div class="specialist-image">
                        <img src="${valueOfElement.portrait}" alt="Фотография мастера">
                    </div>
                    <div class="specialist-info">
                        <h3 class="specialist-name">${valueOfElement.firstName} ${valueOfElement.lastName}</h3>
                        <p class="specialist-specialty">${valueOfElement.specialization}</p>
                        <p class="specialist-experience">Опыт работы: ${valueOfElement.workExperience} лет</p>
                    </div>
                </div>
             `);
        }
    });
}

function showServicesTable() {
    $('.services_catalog > ._container').html('');
    $.each(servicesTable, function (indexInArray, valueOfElement) {
        var titleOK = $('#service_selection_title').val() === ''? true: (valueOfElement.title).includes( $('#service_selection_title').val() );
        var priceOK = $('#service_selection_price').val() === ''? true: (
            ( (valueOfElement.priceWithoutDiscount == $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '=' ) ) ||
            ( (valueOfElement.priceWithoutDiscount > $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '>' ) ) ||
            ( (valueOfElement.priceWithoutDiscount < $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '<' ) ) ||
            ( (valueOfElement.priceWithDiscount == $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '=' ) ) ||
            ( (valueOfElement.priceWithDiscount > $('#service_selection_price').val()) && ( $('#service_selection_price_select').val() == '>' ) ) ||
            ( ((valueOfElement.priceWithDiscount < $('#service_selection_price').val()) && (valueOfElement.priceWithDiscount !== null)) && ( $('#service_selection_price_select').val() == '<' ) )
        );
        if (!usingServiceParams || (usingServiceParams && titleOK && priceOK)) {
            var priceHTML = (valueOfElement.priceWithDiscount == null) ? `
            <span class="service-card-discount-price">$${valueOfElement.priceWithoutDiscount}</span>
            ` : `
            <span class="service-card-original-price">$${valueOfElement.priceWithoutDiscount}</span>
            <span class="service-card-discount-price">$${valueOfElement.priceWithDiscount}</span>
            `;


            $('.services_catalog > ._container').append(`
                <div class="service-card">
                    <div class="service-card-img">
                        <img src="${valueOfElement.image}" alt="Service Image">
                    </div>
                        <h3 class="service-card-title">${valueOfElement.title}</h3>
                        <p class="service-card-desc">${valueOfElement.description}</p>
                        <div class="service-card-price">
                            ${priceHTML}
                        </div>
                        <button class="service-card-btn">Добавить в корзину</button>
                    </div>
                </div>
            `);
        }
    });

    // Нажатие на добавление товара в корзину
    $('.service-card-btn').click(function (e) {
        var serviceTitle = $(this).parent('.service-card').children('.service-card-title').text().trim();
        $.ajax({
            type: "POST",
            url: "http://localhost:8081/api/basket",
            data: jQuery.param({username: $('info').attr('value'), serviceTitle: serviceTitle}),
            complete: function (response) {
                fillBasket();

                // Если пользователь авторизован
                if (response.status == 200) {
                    new Toast({
                        title: 'Корзина обновлена',
                        text: `Услуга '${serviceTitle}' добавлена в корзину`,
                        theme: 'success',
                        autohide: true,
                        interval: 5000
                      });

                //    Если не авторизован
                } else if (response.status == 400) {
                    $(`#login_popup`).addClass('active');
                    $('body').addClass('blocked');
                    new Toast({
                        title: 'Не получилось добавить товар',
                        text: 'Вы должны быть авторизованы',
                        theme: 'danger',
                        autohide: true,
                        interval: 5000
                      });
                }
            }
        });
    });
}

function generateMastersTable() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/api/specialists",
        complete: function (response) {
            mastersTable = JSON.parse(response.responseText);
            showMastersTable();
        }
    });
}

function generateServicesTable() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/api/services",
        data: "data",
        dataType: "dataType",
        complete: function (response) {
            servicesTable = JSON.parse(response.responseText);
            showServicesTable();
        }
    });
}

// Нажатие на кнопку "Обновить таблицу мастеров"
$('.refresh_masters_table_icon').click(function (e) {
    generateMastersTable();
})

// Нажатие на кнопку "Обновить таблицу услуг"
$('.refresh_services_table_icon').click(function (e) {
    generateServicesTable();
})

// Автоматическое изменение таблицы  мастера при изменении параметров
$('#master_selection_fullname, #master_selection_work_experience, #master_selection_specialization, #master_selection_work_experience_select').on('input', function() {
    usingMasterParams = true;
    showMastersTable();
})

// Автоматическое изменение таблицы услуг при изменении параметров
$('#service_selection_title, #service_selection_price, #service_selection_price_select').on('input', function() {
    usingServiceParams = true;
    showServicesTable();
})



$(document).ready(function () {
    generateMastersTable();
    generateServicesTable();
});