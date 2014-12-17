(function() {

    var model = Y.lane.Model, redirectUrl,
    persistentStatusCookie = Y.Cookie.get('lane-login-expiration-date'),
    basePath = model.get(model.BASE_PATH)|| "",
    location = Y.lane.Location,
    now = new Date(),
    // isStanfordActive == true only if user is from stanford and is active in the LDAP
    // See UserDataBinder.java
    isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),
    basePath = model.get(model.BASE_PATH)|| "",
    drMode = model.get(model.DISASTER_MODE);

    // if someone click on a proxied link and he is from stanford so he will
    // have the possibility to extend his persistent login
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*=/redirect/cme]");
    Y.on("click", function(event) {
        extensionPersistentLoginPopup(event);
    }, "a[href*=laneproxy.stanford.edu/login]");

    var extensionPersistentLoginPopup = function(event){
        var link = event.target, href = link.get('href');
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
    var popupWindow = function(id, o) {
        var lightbox = Y.lane.Lightbox, shibbolethAnchors, href, node;
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
        messageDiv = Y.one('#persistent .message'),
        isPersistent = Y.Node.create("<div class='is-persistent'><input type='checkbox' id='is-persistent-login' /> <label>Log me in for 2 weeks (DON’T check if on a shared computer)</label></div>");
        div.insert(isPersistent, 'after');

        Y.on('change',     function(event) {
            var checkbox = event.target;
            if(checkbox.get('checked')) {
                Y.Cookie.set("isPersistent", "yes");
            } else {
                Y.Cookie.remove("isPersistent");
            }
        }
        , '#is-persistent-login');
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
