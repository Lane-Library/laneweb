(function () {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(function (menu) {
        menu.addEventListener('click', function (event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });

    // to select menu  from the header 
    document.querySelectorAll("nav ul li.nav-menu span").forEach(function (span) {
        span.parentElement.querySelectorAll("a").forEach(function (link) {
            if (link.pathname == window.location.pathname && link.hash == window.location.hash) {
                span.classList.add("red");
                span.classList.add("btm-brdr-red");
            }
        });
    });
})();
