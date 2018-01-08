(function() {

    "use strict";

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
