(function() {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2').forEach(function(menu) {
        menu.addEventListener('click', function(event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });


    document.querySelectorAll('.menu ul').forEach(function(submenu) {
        if (submenu.querySelector('.menuitem-active')) {
            submenu.className = 'submenu submenu-active';
            submenu.parentNode.querySelector('a').innerHTML += ' <i class="fa fa-chevron-up"></i>';
        } else {
            submenu.className = 'submenu';
            submenu.parentNode.querySelector('a').innerHTML += ' <i class="fa fa-chevron-down"></i>';
        }
        submenu.parentNode.addEventListener('click', function(event) {
            if (event.target === this.querySelector('.fa')) {
                event.preventDefault();
                this.querySelector('ul').classList.toggle('submenu-active');
                event.target.classList.toggle('fa-chevron-down');
                event.target.classList.toggle('fa-chevron-up');
            }
        });
    });


})();
