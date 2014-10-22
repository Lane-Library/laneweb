(function() {

	var redirectUrl = null, 
	persistentStatusCookie = Y.Cookie.get('lane-login-expiration-date'), 
	location = Y.lane.Location, 
	model = Y.lane.Model, 
	basePath = model.get(model.BASE_PATH)|| "", 
	drMode = model.get(model.DISASTER_MODE),
	// istanfordActive == true only if user is from stanford and is active in the LDAP
	// See UserDataBinder.java
	isStanfordActive = model.get(model.IS_ACTIVE_SUNETID),

	getPopup = function(urlPage) {
		Y.io(urlPage, {
			on : {
				success : popupWindow
			}
		});
	};

	Y.on("click", function(event) {popupShibboltehWindow(event);}, 'a[href=' + basePath + '/secure/login.html]');
	Y.on("click", function(event) {popupShibboltehWindow(event);}, "a[href*=/secure/apps/proxy/credential]");
	Y.on("click", function(event) {popupShibboltehWindow(event);}, "a[href*=/redirect/cme]");

	popupShibboltehWindow = function(event) {
		var link = event.currentTarget,
		href = link.get('href');
		if (!drMode && !persistentStatusCookie && ( !href ||  href.indexOf("javascript") !== 0)){
			if(!href || href.indexOf("/secure/login.html")>-1){
				redirectUrl = encodeURIComponent(location.get("href"))
			}else{
				redirectUrl = encodeURIComponent(href);
			}
			event.preventDefault();
			link.set('rel', 'persistentLogin');
			getPopup(basePath + '/plain/shibboleth-persistent-login.html');
		}
	};

	// if someone click on a proxied link and he is from stanford so he will
	// have the possibility to extend his persistent login
	Y.on("click", function(event) {extensionPersistentLoginPopup(event);}, "a[href*=/redirect/cme]");
	Y.on("click", function(event) {extensionPersistentLoginPopup(event);}, "a[href*=laneproxy.stanford.edu/login]");
	
	var extensionPersistentLoginPopup = function(event){
		var threeDays = 3600 * 3, 
		now = new Date(), 
		link = event.target; 
		
		if (isStanfordActive && !drMode && persistentStatusCookie && now.getTime() > (persistentStatusCookie - threeDays)) {
			event.preventDefault();
			link.set('rel', 'persistentLogin');
			redirectUrl = encodeURIComponent(link.get('href'));
			getPopup(basePath + '/plain/shibboleth-persistent-extension.html');
		}
	};

	
	
	// The popup window
	var popupWindow = function(id, o) {
		var lightbox = Y.lane.Lightbox, shibbolethAnchors;
		lightbox.setContent(o.responseText);
		lightbox.show();
		shibbolethAnchors = lightbox.get("contentBox").all('#shibboleth-links a');

		// Click on one organization -- below the url we have to set here for
		// stanford idp
		// /Shibboleth.sso/Login?SAMLDS=1&entityID=https%3A%2F%2Fidp.stanford.edu%2F&target=%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse%26url%3Dhttp%253A%252F%252Flocalhost%253A8080%252Fsecure%252Fapps%252Fproxy%252Fcredential%253Furl%253Dhttp%253A%252F%252Fgoogle.com
		Y.once("click", function(event) {
			var node = event.currentTarget, url, 
			persistentUrl = basePath+ '/secure/persistentLogin.html?pl=', 
			isPersistent;
			if (!redirectUrl) {
				redirectUrl = "/index.html";
			}
			
			if(Y.one('#is-persistent-login')){
				isPersistent = Y.one('#is-persistent-login').get('checked');
				url = node.get('href') + encodeURIComponent( persistentUrl + isPersistent + '&url='+ redirectUrl);
			}else{
				url =  persistentUrl + 'renew&url='+ encodeURIComponent(redirectUrl);
			}
			node.set('href', url);
		}, shibbolethAnchors);
	};
	// END POPUP

	
	
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
