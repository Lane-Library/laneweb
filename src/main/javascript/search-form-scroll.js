// scrolls search result pages so that the form is the top element
// unless returning via back button (case 131724)
(function () {

    "use strict";

    let userAgent = L.getUserAgent();

    if (/.*\/search\.html/.test(location.pathname)) {
        let i = 0,
            // adjust scroll based on device width
            scrollAmount = (window.innerWidth < 812) ? 30 : 130,
            offset = document.forms[0].offsetTop - scrollAmount;
        if (window.scrollY < offset) {
            window.scroll(0, offset);
        }
    }

})();
