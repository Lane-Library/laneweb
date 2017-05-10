// scrolls search result pages so that the form is the top element
// unless returning via back button (case 131724)
(function() {

    "use strict";

    if (/\/search\.html/.test(location.pathname)) {
        window.addEventListener("DOMContentLoaded", function() {
            var i = 0,
                offset = document.forms[0].offsetTop + 50,
                doScroll = function() {
                    scroll(0, offset);
                    console.log("scroll from search-form-scroll.js");
                },
                // Edge has scrollY 0 initially regardless of previous position
                // poll value every 100ms for 1s
                edgeDelay = function() {
                    if (window.scrollY === 0 && i < 10) {
                        i++;
                        window.setTimeout(edgeDelay, 100);
                    } else if (window.scrollY < offset) {
                        window.scroll(0, offset);
                    }
                }
            if (/Edge/.test(window.navigator.userAgent)) {
                edgeDelay();
            } else if (window.scrollY < offset) {
                doScroll();
            }
        });
    }

})();