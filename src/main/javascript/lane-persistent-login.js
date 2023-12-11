(function() {

    "use strict";

    // the check box for persistent login on the discovery login page
    var    persistentLoginCheckbox = document.getElementById('is-persistent-login');
    
    // handle checking or unchecking the check box on the discovery login page
    if (persistentLoginCheckbox) {
        persistentLoginCheckbox.addEventListener("change", function(event) {
            if (event.target.checked) {
                L.Cookie.set("isPersistent", "yes");
            } else {
                L.Cookie.remove("isPersistent");
            }
        });
    }

})();
