(() => {
    "use strict";

    // Find the check box for persistent login on the discovery login page
    // Handle setting or removing the cookie when the user toggles the checkbox
    // Attach a listener in one chain using optional chaining (?.) in case the element doesn't exist
    document.getElementById('is-persistent-login')
        ?.addEventListener("change", (event) => {
            event.target.checked
                ? L.Cookie.set("isPersistent", "yes")
                : L.Cookie.remove("isPersistent");
        });

})();
