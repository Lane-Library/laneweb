// scrolls search result pages so that the form is the top element
// unless returning via back button (case 131724)
(() => {

    "use strict";

    const searchForm = document.querySelector("form#search");

    if (location.pathname.endsWith("/search.html") && searchForm) {

        const MOBILE_BREAKPOINT_PX = 812;
        const SCROLL_OFFSET_DESKTOP = 130;
        const SCROLL_OFFSET_MOBILE = 30;

        const scrollAmount = window.innerWidth < MOBILE_BREAKPOINT_PX
            ? SCROLL_OFFSET_MOBILE
            : SCROLL_OFFSET_DESKTOP;

        const targetScrollY = searchForm.offsetTop - scrollAmount;

        if (window.scrollY < targetScrollY) {
            window.scrollTo({ top: targetScrollY, behavior: "smooth" });
        }
    }

})();
