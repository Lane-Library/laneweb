(function() {

    "use strict";

    var model = L.Model, redirectUrl,
    loginExpirationDate = L.Cookie.get('lane-login-expiration-date'),
    basePath = model.get(model.BASE_PATH)|| "",
    // isStanfordActive == true only if user is from stanford and is active in the LDAP
    // See UserDataBinder.java
    isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),
    // the check box for persistent login on the discovery login page
    persistentLoginCheckbox = document.getElementById('is-persistent-login'),
    myAccountsLink = document.getElementById("persistent-login"),

    // The popup window for expension
    popupWindow = function(id, o) {
        var lightbox = L.Lightbox, okLink;
        lightbox.setContent(o.responseText);
        redirectUrl = redirectUrl || "/index.html";
        okLink = document.querySelector(".yui3-lightbox a");
        okLink.href = basePath + "/persistentLogin.html?pl=renew&url=" + encodeURIComponent(redirectUrl);
        lightbox.show();
    };
    // END POPUP

    // if someone click on a proxied link and he is from stanford so he will
    // have the possibility to extend his persistent login
    if (isStanfordActive && loginExpirationDate) {
        document.addEventListener("click", function(event) {
            var node = event.target.closest("a[href*='laneproxy.stanford.edu/login'], a[href*='/redirect/cme']");
            if (node && new Date().getTime() > loginExpirationDate) {
                event.preventDefault();
                redirectUrl = node.href;
                // setting rel keeps the tracking redirect thing from happening
                node.rel = "persistentLogin";
                L.io(basePath + '/plain/shibboleth-persistent-extension.html', {
                    on : {
                        success : popupWindow
                    }
                });
            }
        });
    };

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
    if (myAccountsLink) {
        myAccountsLink.addEventListener('click',function(event) {
            event.preventDefault();
            if (isStanfordActive) {
                L.setLocationHref(basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
            } else {
                L.setLocationHref(basePath + '/secure/persistentLogin.html?pl=true');
            }
        });
    }

})();
