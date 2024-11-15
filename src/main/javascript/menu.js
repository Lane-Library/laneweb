(function () {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(function (menu) {
        menu.addEventListener('click', function (event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });

})();
