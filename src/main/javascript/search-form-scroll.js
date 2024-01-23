// scrolls search result pages so that the form is the top element
// unless returning via back button (case 131724)
(function() {

    "use strict";

    if (/.*\/search\.html/.test(location.pathname)) {
        let i = 0,
            // adjust scroll based on device width
            scrollAmount = (window.innerWidth < 812) ? 30 : 130,
            offset = document.forms[0].offsetTop - scrollAmount ,
        // Edge has scrollY initially set to 0 regardless of previous position
        // this polls value every 100ms for 1s
        edgeDelay = function() {
            if (window.pageYOffset === 0 && i < 10) {
                i++;
                window.setTimeout(edgeDelay, 100);
            } else if (window.pageYOffset < offset) {
                window.scroll(0, offset);
            }
        }
        if (/Edge/.test(window.navigator.userAgent)) {
            edgeDelay();
        } else if (window.pageYOffset < offset) {
            window.scroll(0, offset);
        }
    }

})();
