var reviewsTable = undefined;
var isAdmin = false;

function deleteReview(reviewId) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8081/api/reviews",
        data: jQuery.param({reviewId: reviewId}),
        complete: function (response) {
            if (response.status == 200) {
                fillReviews();
                new Toast({title: 'Одним отзывом на сайте меньше', text: `Вы успешно удалили отзыв!`, theme: 'success', autohide: true, interval: 5000});
            }
        }
    });
}

function showReviews() {
    $('.reviews_content').html('');

    $.each(reviewsTable, function (indexInArray, valueOfElement) { 
        $('.reviews_content').append(`
        <div class="reviews__item">
            <div class="reviews__item_date">${valueOfElement.date}</div>
            <div class="reviews__item_author">
                Оставил: ${valueOfElement.author}
            </div>

            <div class="reviews__item_rate">
                ${ "<i class='review_star'></i>".repeat(valueOfElement.rate) }
            </div>

            <div class="reviews__item_content style-1">
                ${valueOfElement.content}
            </div>
            ${isAdmin? `<div class="deleteReviewButton" value="${valueOfElement.id}"></div>`: ``}
        </div>
        `);
    });

    $('.deleteReviewButton').click(function (e) { 
        var reviewId = Number($(this).attr('value'));
        deleteReview(reviewId);
    });
}

function fillReviews() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/api/reviews",
        complete: function (response) {
            reviewsTable = JSON.parse(response.responseText);
            showReviews();
        }
    });
}

$('#feedback_btn').on('click', function () {
    var author = $('#feedback_author').val();
    var content = $('#feedback_content').val();
    var rate = $('#feedback_rate').val();

    if (author == '' || content == '') {
        new Toast({title: 'Не удалось оставить отзыв', text: `Необходимов заполнить все поля!`, theme: 'danger', autohide: true, interval: 5000});
    } else {
        $.ajax({
            type: "POST",
            url: "http://localhost:8081/api/reviews",
            data: jQuery.param({author: author, content: content, rate: rate}),
            complete: function (response) {
                fillReviews();
                new Toast({title: 'Спасибо за обратную связь', text: `Вы успешно оставили отзыв!`, theme: 'success', autohide: true, interval: 5000});
            }
        });
    }
});

$(document).ready(function () {
    fillReviews();
    
    // Проверка на администратора
    if ($('info').attr('value') != undefined) {
        $.ajax({
            type: "GET",
            url: `http://localhost:8081/api/users/isAdmin/${$('info').attr('value')}`,
            complete: function (response) {
                isAdmin = response.responseText == 'true';
            }
        });
    }
});