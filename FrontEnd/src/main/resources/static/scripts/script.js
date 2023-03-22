// Если в каком-то теге <a> указан атрибут popup_id с id попапа, то этот попап при нажатии откроется
$('a').click(function (e) {
    var popup_id = $(this).attr('popup-id')
    $('body').addClass('blocked');
    if (popup_id !== undefined) {
        $(`#${popup_id}`).addClass('active');
    }
});

$('.basket_popup_link').click(function (e) {
    fillBasket();
})

// Функция обновляет содержимое корзины и выводит на экран
function fillBasket() {
    $.ajax({
        type: "GET",
        url: `http://localhost:8081/api/basket/${ $('info').attr('value') }`,
        dataType: "application/json",
        complete: function (response) {
            basket = JSON.parse(response.responseText);
            showBasket();
        },
    });
}

// Функция генерирует корзину в попапе
function showBasket() {
    $('.basket__items').html('');
    $.each(basket.basket, function (indexInArray, valueOfElement) {
        var priceHTML = (valueOfElement.priceWithDiscount != null)? `
                        <span class="service-card-original-price">$${valueOfElement.priceWithoutDiscount}</span>
                        <span class="service-card-discount-price">$${valueOfElement.priceWithDiscount}</span>
                        `: `
                        <span class="service-card-discount-price">$${valueOfElement.priceWithoutDiscount}</span>
                        `;

        $('.basket__items').append(`
            <div class="basket__item">
                    <div class="basket__item_title">
                        <h2>${valueOfElement.title}</h2>
                    </div>
                    <div class="basket__item_price">
                        <span style="font-size: 24px;">Цена: </span>
                        ${priceHTML}
                    </div>
                    <div class="basket__item_amount">
                        <span style="font-size: 18px;">Количество</span>
                        <input class="basket__item_amount_input" type="number" min="0" max="99" value="${valueOfElement.amount}">
                    </div>
                    <div class="basket__item_total_cost">
                        <span style="font-size: 18px;">Итого: ${valueOfElement.totalCost}$</span>
                    </div>
                    <a class="basket__item_delete_service">Удалить</a>
                </div><hr>
         `);
    });

    // Перерисовываем общую сумму корзины
    $('.basket__total_cost').text(`Общая сумма: ${basket.totalCost}$`);

    // Отправка запроса при изменении кол-ва товара
    $('.basket__item_amount_input').on('input', function () {
        var serviceTitle = $(this).parent().parent().children('.basket__item_title').text().trim();
        var amount = $(this).val();
        $.ajax({
            type: "PUT",
            url: "http://localhost:8081/api/basket",
            data: jQuery.param({username: $('info').attr('value'), serviceTitle: serviceTitle, amount: amount}),
            complete: function (response) {
                fillBasket();
            }
        });
    });

    $('.basket__item_delete_service').click(function (e) {
        var serviceTitle = $(this).parent().children('.basket__item_title').text().trim();
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8081/api/basket",
            data: jQuery.param({username: $('info').attr('value'), serviceTitle: serviceTitle}),
            complete: function (response) {
                fillBasket();
            }
        });
    });
}

// Закрытие попапа
$('.popup_bg, .close_popup').click(function (e) {
    $('.popup.active').removeClass('active');
    $('body').removeClass('blocked');
});

// Нет аккаунта? Зарегистрироваться!
$('#open_registration_popup_link, #open_login_popup_link').click(function (e) {
    $('#login_popup, #registration_popup').toggleClass('active');
});

// Нажатие на регистрацию
$('#registration_btn').click(function (e) {
    if (everythingIsOKWithRegistrationForm()) {

        // Отправляем запрос на регистрацию
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/api/users',

            // Все данные из формы передаём как JSON
            data: JSON.stringify({
                'username': $('#reg_login_input').val(),
                'password': $('#reg_password_input').val(),
                'confirmedPassword': $('#reg_confirmed_password_input').val(),
                'birthDay': $('#reg_birthday_input').val()
            }),

            complete: function(data) {
                $('#registration_success').html( (data['status'] != 400)? data['responseText']: '' );
                $('#registration_error').html( (data['status'] == 400)? data['responseText']: '' );

                // Если регистрация успешна, то перебрасываем на окно входа
                if (data['status'] != 400) {
                    $('#login_popup, #registration_popup').toggleClass('active');
                    $('#login_error').remove();
                }
            },

            contentType: "application/json",
            dataType: 'json',
            cache: false
        });

    } else {
        $('#registration_error').html( 'Введите нормальные данные!' );
    }
});

$(document).ready(function () {
    // Проверка на наличие ошибки для повторного вывода окна логина
    if ( $('#login_error').length ) {
        $('#login_popup').addClass('active');
    }
});



// Валидация форм
function everythingIsOKWithRegistrationForm() {
    return ( !$('#username_warn').html().length && !$('#password_warn').html().length )
}

$('#reg_login_input, #reg_password_input, #reg_confirmed_password_input').on('input', function () {
    var currentUsername = $('#reg_login_input').val();
    var currentPassword = $('#reg_password_input').val();
    var currentConfirmedPassword = $('#reg_confirmed_password_input').val();

    $('#username_warn').html( (currentUsername.length < 3)? 'Слишком короткое имя': '' );
    $('#password_warn').html( (currentPassword !== currentConfirmedPassword)? 'Пароли должны быть одинаковые': '' );
});