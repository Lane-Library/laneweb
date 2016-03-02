(function() {

    "use strict";

    var model = Y.lane.Model, redirectUrl,
    persistentStatusCookie = Y.Cookie.get('lane-login-expiration-date'),
    basePath = model.get(model.BASE_PATH)|| "",
    location = Y.lane.Location,
    now = new Date(),
    // isStanfordActive == true only if user is from stanford and is active in the LDAP
    // See UserDataBinder.java
    isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),
    drMode = model.get(model.DISASTER_MODE),
    extensionPersistentLoginPopup,
    popupWindow,
    getPopup;

    // if someone click on a proxied link and he is from stanford so he will
    // have the possibility to extend his persistent login
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*=/redirect/cme]");
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*=laneproxy.stanford.edu/login]");

    extensionPersistentLoginPopup = function(event){
        var link = event.target;
        if (isStanfordActive && !drMode && persistentStatusCookie && now.getTime() > persistentStatusCookie) {
            event.preventDefault();
            link.set('rel', 'persistentLogin');
            redirectUrl = encodeURIComponent(event.target.get('href'));
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
        var lightbox = Y.lane.Lightbox, shibbolethAnchors;
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

    //To display the persistent checkbox in the discoverypage
    if(Y.one('#persistent')){
        var div = Y.one('#persistent div.header'),
        host = location.get("host"),
        isPersistent = Y.Node.create("<div class='is-persistent'><input type='checkbox' id='is-persistent-login' /> <label>Log me in for 2 weeks (DON’T check if on a shared computer)</label></div>");
        div.insert(isPersistent, 'after');

        Y.on('change',     function(event) {
            var checkbox = event.target;
            if(checkbox.get('checked')) {
                Y.Cookie.set("isPersistent", "yes");
            } else {
                Y.Cookie.remove("isPersistent");
            }
        }, '#is-persistent-login');
        // LPCH/SCH has different IdP/ADFS instances for lane (prod) and lane-beta and lane-prototype (dev):
        // prod: https://sch-sts.lpch.org/adfs/services/trust
        // dev : http://sch-sts-test.stanfordchildrens.org/adfs/services/trust -->
        if (!host.match("lane.stanford.edu")) {
            Y.all("#shibboleth-links a").each(function(node) {
                var href = node.get("href");
                if (href.indexOf("entityID=https%3A%2F%2Fsch-sts.lpch.org")) {
                    href = href.replace("https%3A%2F%2Fsch-sts.lpch.org","http%3A%2F%2Fsch-sts-test.stanfordchildrens.org")
                    node.set("href",href);
                }
            });
        }
    }

    // for the static page myaccounts.html Click on YES this way the user
    // will not have to go through webauth.
    if(Y.one('#persistent-login')){
        Y.on('click',function(event) {
            event.preventDefault();
            if (isStanfordActive) {
                location.set("href", basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
            } else {
                location.set("href", basePath + '/secure/persistentLogin.html?pl=true');
            }
        }, '#persistent-login');
    }

})();
