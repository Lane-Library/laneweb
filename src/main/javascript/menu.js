(function() {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(function(menu) {
        menu.addEventListener('click', function(event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });


    document.querySelectorAll('.menu-container ul li a[href^="#"]').forEach(function(link) {
        link.addEventListener('click', function(event) {
           let clickTarget = event.target;
           clickTarget.closest('ul').querySelectorAll('li a').forEach(function(anchor) {
                anchor.classList.remove('menuitem-active');
            });
            clickTarget.classList.add('menuitem-active');    
        })
    });

    
    window.addEventListener("load", function() {
    let hash = window.location.hash;
    document.querySelectorAll('.menu-container ul li a[href^="#"]').forEach(function(link) {
        if(hash === link.hash){
            link.classList.add('menuitem-active');
        }       
    });
    });

})();
