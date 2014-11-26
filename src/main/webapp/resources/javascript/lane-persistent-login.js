(function() {

	var redirectUrl = null, 
	persistentStatusCookie = Y.Cookie.get('lane-login-expiration-date'), 
	location = Y.lane.Location, 
	model = Y.lane.Model, 
	basePath = model.get(model.BASE_PATH)|| "", 
	drMode = model.get(model.DISASTER_MODE),
	// isStanfordActive == true only if user is from stanford and is active in the LDAP
	// See UserDataBinder.java
	isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),
	ipgroup = model.get(model.IPGROUP),
    fromHospital = "SHC" === ipgroup || "LPCH" === ipgroup,
    popupShibbolethWindowNotHospital,
    popupShibbolethWindow,

    // The popup window
    popupWindow = function(id, o) {
        var lightbox = Y.lane.Lightbox, shibbolethAnchors, href, node;
        lightbox.setContent(o.responseText);
        lightbox.show();
        shibbolethAnchors = lightbox.get("contentBox").all('#shibboleth-links a');
        
        // Click on one organization -- below the url we have to set here for
        // stanford idp
        // /Shibboleth.sso/Login?SAMLDS=1&entityID=https%3A%2F%2Fidp.stanford.edu%2F&target=%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse%26url%3Dhttp%253A%252F%252Flocalhost%253A8080%252Fsecure%252Fapps%252Fproxy%252Fcredential%253Furl%253Dhttp%253A%252F%252Fgoogle.com
        Y.once("click", function(event) {
            var node = event.currentTarget, href,
            persistentUrl = basePath+ '/secure/persistentLogin.html?pl='; 
            if (!redirectUrl) {
                redirectUrl = "/index.html";
            }
            if(Y.one('#is-persistent-login')){
               href = node.get('href') + encodeURIComponent( persistentUrl + Y.one('#is-persistent-login').get('checked') + '&url='+ redirectUrl);
            }else{
                href = persistentUrl + 'renew&url='+ encodeURIComponent(redirectUrl);
            }
            node.set('href', href);
        }, shibbolethAnchors);
    },
    // END POPUP
	
	getPopup = function(urlPage) {
		Y.io(urlPage, {
			on : {
				success : popupWindow
			}
		});
	};

	Y.on("click", function(event) {popupShibbolethWindow(event);}, 'a[href=' + basePath + '/secure/login.html]');
	Y.on("click", function(event) {popupShibbolethWindowNotHospital(event);}, "a[href*=/secure/apps/proxy/credential]");
	Y.on("click", function(event) {popupShibbolethWindowNotHospital(event);}, "a[href*=/redirect/cme]");

	popupShibbolethWindowNotHospital = function(event){
		if(!fromHospital){
			popupShibbolethWindow(event);
		}
	};
	
	popupShibbolethWindow = function(event) {
		var link = event.currentTarget,
		href = link.get('href');
		if (!drMode && !persistentStatusCookie && ( !href ||  href.indexOf("javascript") !== 0)){
			if(!href || href.indexOf("/secure/login.html")>-1){
				redirectUrl = encodeURIComponent(location.get("href"));
			}else{
				redirectUrl = encodeURIComponent(href);
			}
			event.preventDefault();
			link.set('rel', 'persistentLogin');
			getPopup(basePath + '/plain/shibboleth-persistent-login.html');
		}
	};
    
    var extensionPersistentLoginPopup = function(event){
        var link = event.target, href = link.get('href');
        if (isStanfordActive && !drMode && persistentStatusCookie && now.getTime() > persistentStatusCookie) {
            event.preventDefault();
            link.set('rel', 'persistentLogin');
            redirectUrl = encodeURIComponent(event.target.get('href'));
            getPopup(basePath + '/plain/persistent-extension-popup.html');
        }
    };
    
	// if someone click on a proxied link and he is from stanford so he will
	// have the possibility to extend his persistent login
	Y.on("click", function(event) {extensionPersistentLoginPopup(event);}, "a[href*=/redirect/cme]");
	Y.on("click", function(event) {extensionPersistentLoginPopup(event);}, "a[href*=laneproxy.stanford.edu/login]");


	
	

	

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
