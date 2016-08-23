// scrolls search result pages so that the form is the top element
(function() {

    "use strict";

    if (/\/search\.html/.test(document.referrer) && /\/search\.html/.test(location.pathname)) {
        location.hash = "search";
        document.addEventListener("DOMContentLoaded", function() {
            scroll(0, document.forms[0].offsetTop + 50);
        }, false);
    }

})();