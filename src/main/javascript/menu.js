(function() {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2').forEach(function(menu) {
        menu.addEventListener('click', function(event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });


    document.querySelectorAll('.menu-container ul li a[href^="#"]').forEach(function(link) {
        link.addEventListener('click', function(event) {
           var link = event.target;
           link.closest('ul').querySelectorAll('li a').forEach(function(anchor) {
                anchor.classList.remove('menuitem-active');
            });
            
            link.classList.add('menuitem-active');    
        })
    });


})();
