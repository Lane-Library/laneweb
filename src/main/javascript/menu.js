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


    //For menu item with the hash in the url like in the guides.html and mobile-applications.html
    let links = document.querySelectorAll(".menu-container.hoverline ul li a[href^='#']");
    if (links) {
        let hash = window.location.hash;
        links.forEach(function (link) {
            link.addEventListener('click', function (event) {
                hash = event.target.hash;
                links.forEach(function (l) { l.classList.remove('menuitem-active') });
                L.fire("menu-changed", { hash: hash });
                link.classList.add('menuitem-active');
            })
        });
        selectMenuByHash(hash);
    }

    function selectMenuByHash(hash) {
        let selectedLink = document.querySelector('.menu-container.hoverline ul li a[href="' + hash + '"]');
        if (selectedLink) {
            selectedLink.classList.add('menuitem-active');
        }
    }

    L.on("content-changed", function (event) {
        selectMenuByHash(event.hash);
    });

})();
