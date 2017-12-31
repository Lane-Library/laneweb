(function() {

    "use strict";

    var model = L.Model, redirectUrl,
    persistentStatusCookie = Y.Cookie.get('lane-login-expiration-date'),
    basePath = model.get(model.BASE_PATH)|| "",
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
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*='/redirect/cme']");
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*='laneproxy.stanford.edu/login']");

    extensionPersistentLoginPopup = function(event){
        var link = event.target;
        if (isStanfordActive && persistentStatusCookie && now.getTime() > persistentStatusCookie) {
            event.preventDefault();
            link.set('rel', 'persistentLogin');
            redirectUrl = event.target.get('href');
            getPopup(basePath + '/plain/shibboleth-persistent-extension.html');
        }
    };

    getPopup = function(urlPage) {
        Y.io(urlPage, {
            on : {
                success : popupWindow
            }
        });
    };

    // The popup window for expension
    popupWindow = function(id, o) {
        var lightbox = L.Lightbox, shibbolethAnchors;
        lightbox.setContent(o.responseText);
        lightbox.show();
        shibbolethAnchors = lightbox.get("contentBox").all('#shibboleth-links a');
        Y.once("click", function(event) {
            var node = event.currentTarget, href;
            if (!redirectUrl) {
                redirectUrl = "/index.html";
            }
            href =  basePath+ '/persistentLogin.html?pl=renew&url='+ encodeURIComponent(redirectUrl);
            node.set('href', href);
        }, shibbolethAnchors);
    };
    // END POPUP

    //handle checking or unchecking the check box on the discovery login page
    if (persistentLoginCheckbox) {
        persistentLoginCheckbox.addEventListener("change", function(event) {
            if (event.target.checked) {
                Y.Cookie.set("isPersistent", "yes");
            } else {
                Y.Cookie.remove("isPersistent");
            }
        });
    }

    // for the static page myaccounts.html Click on YES this way the user
    // will not have to go through webauth.
    if (document.querySelector('#persistent-login')) {
        document.querySelector("#persistent-login").addEventListener('click',function(event) {
            event.preventDefault();
            if (isStanfordActive) {
                L.setHref(basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
            } else {
                L.setHref(basePath + '/secure/persistentLogin.html?pl=true');
            }
        });
    }

})();
