document.addEventListener("DOMContentLoaded", function () {
    const scrollContainer = document.querySelector('.destinations-scroll');
    const leftBtn = document.querySelector('.left-btn');
    const rightBtn = document.querySelector('.right-btn');

    leftBtn.addEventListener('click', () => {
        scrollContainer.scrollBy({
            left: -300, // Scroll left by 300 pixels
            behavior: 'smooth'
        });
    });

    rightBtn.addEventListener('click', () => {
        scrollContainer.scrollBy({
            left: 300, // Scroll right by 300 pixels
            behavior: 'smooth'
        });
    });
});



document.addEventListener('DOMContentLoaded', function() {
    const slider = document.querySelector('.recommended-grid');
    let index = 0;
    const totalCards = document.querySelectorAll('.recommended-card').length;
    const cardWidth = document.querySelector('.recommended-card').offsetWidth;

    function showNextSlide() {
        index++;
        if (index >= totalCards) index = 0;
        slider.style.transform = `translateX(${-index * cardWidth}px)`;
    }

    setInterval(showNextSlide, 3000); // Change slide every 3 seconds
});