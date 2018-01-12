(function() {

    "use strict";

    var model = L.Model, redirectUrl,
    persistentStatusCookie = L.Cookie.get('lane-login-expiration-date'),
    basePath = model.get(model.BASE_PATH) || "",
    now = new Date(),
    // isStanfordActive == true only if user is from stanford and is active in the LDAP
    // See UserDataBinder.java
    isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),
    extensionPersistentLoginPopup,
    popupWindow,
    getPopup,
    // the check box for persistent login on the discovery login page
    persistentLoginCheckbox = document.querySelector('#is-persistent-login');

    // if someone click on a proxied link and he is from stanford so he will
    // have the possibility to extend his persistent login
    document.addEventListener("click", function(event) {
        if (event.target.closest("a[href*='laneproxy.stanford.edu/login'], a[href*='/redirect/cme']")) {
            extensionPersistentLoginPopup(event);
        }
    });

    extensionPersistentLoginPopup = function(event){
        if (isStanfordActive && persistentStatusCookie && now.getTime() > persistentStatusCookie) {
            event.preventDefault();
            redirectUrl = event.target.href;
            getPopup(basePath + '/plain/shibboleth-persistent-extension.html');
        }
    };

    getPopup = function(urlPage) {
        L.io(urlPage, {
            on : {
                success : popupWindow
            }
        });
    };

    // The popup window for expension
    popupWindow = function(id, o) {
        var lightbox = L.Lightbox,
            listener = function() {
                if (!redirectUrl) {
                    redirectUrl = "/index.html";
                }
                this.href = basePath+ '/persistentLogin.html?pl=renew&url='+ encodeURIComponent(redirectUrl);
                this.removeEventListener("click", listener);
            };
        lightbox.setContent(o.responseText);
        lightbox.show();
        document.querySelectorAll('#shibboleth-links a').forEach(function(node) {
            node.addEventListener("click", listener);
        });
    };
    // END POPUP

    //handle checking or unchecking the check box on the discovery login page
    if (persistentLoginCheckbox) {
        persistentLoginCheckbox.addEventListener("change", function(event) {
            if (event.target.checked) {
                L.Cookie.set("isPersistent", "yes");
            } else {
                L.Cookie.remove("isPersistent");
            }
        });
    }

    // for the static page myaccounts.html Click on YES this way the user
    // will not have to go through webauth.
    if (document.querySelector('#persistent-login')) {
        document.querySelector("#persistent-login").addEventListener('click',function(event) {
            event.preventDefault();
            if (isStanfordActive) {
                L.setLocationHref(basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
            } else {
                L.setLocationHref(basePath + '/secure/persistentLogin.html?pl=true');
            }
        });
    }

})();
