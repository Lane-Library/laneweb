(function() {

    "use strict";

    // animate the favorites icon when bookmarks are added 
    L.on("bookmarks:added", function() {
        var favoritesIcon = document.querySelector('.favorites.dropdown .fa-bookmark');
        favoritesIcon.classList.add('shake');
        setTimeout(function() {
            favoritesIcon.classList.remove('shake');
        }, 2000);
    });

})();
