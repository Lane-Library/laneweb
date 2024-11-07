(function () {

    "use strict";

    // the check box for persistent login on the discovery login page
    let persistentLoginCheckbox = document.getElementById('is-persistent-login');

    // handle checking or unchecking the check box on the discovery login page
    if (persistentLoginCheckbox) {
        persistentLoginCheckbox.addEventListener("change", function (event) {
            if (event.target.checked) {
                document.cookie = "isPersistent=yes; path=/";
            } else {
                document.cookie = "isPersistent=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            }
        });
    }

})();
