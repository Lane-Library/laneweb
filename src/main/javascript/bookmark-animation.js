(() => {

    "use strict";

    // Animates favorites icon when a bookmark is added 

    const favoritesIcon = Array.from(document.querySelectorAll('.favorites.dropdown .fa-bookmark'))
        .find(el => el.offsetParent !== null);

    // exit if favorites icon is not present and visible
    if (!favoritesIcon) {
        return;
    }

    const ANIMATION_DURATION = 2000;

    const handleBookmarkAdded = () => {
        favoritesIcon.classList.add('shake');

        setTimeout(() => {
            favoritesIcon.classList.remove('shake');
        }, ANIMATION_DURATION);
    };

    L.on("bookmarks:added", handleBookmarkAdded);

})();
