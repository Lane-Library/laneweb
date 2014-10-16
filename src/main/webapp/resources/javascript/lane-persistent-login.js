
(function() {

    var redirectUrl = null,
    PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference',
    persistentStatusCookie = Y.Cookie.get(PERSISTENT_PREFERENCE_COOKIE_NAME),
    now = new Date(),
    location = Y.lane.Location,
    model = Y.lane.Model,
    basePath = model.get(model.BASE_PATH) || "",
    auth = model.get(model.AUTH),
    ipgroup = model.get(model.IPGROUP),
    drMode = model.get(model.DISASTER_MODE),
    //isActive == true only if user is from stanford and is active in the LDAP
    // See UserDataBinder.java
    isActive = model.get(model.IS_ACTIVE_SUNETID),
    

    getPopup = function(urlPage) {
        Y.io(urlPage, {
            on : {
                success : popupWindow
                }
        });
    };


	
    // if someone click on MyLane Login
	Y.on("click", function(event) {
		var redirectUrl = encodeURIComponent(location.get("href")), 
		link = event.target;
		link.set('rel', 'persistentLogin');
		getPopup(basePath + '/plain/shibboleth-persistent-login.html');
		event.preventDefault();
		}, 'a[href=' + basePath + '/secure/login.html]');

	
	// if someone click on link to be proxy
	  Y.on("click", function(event) {
		 var link = event.target,
		 href = link.get('href');
		if (!drMode && !persistentStatusCookie && href.indexOf("javascript") !== 0) {
			event.preventDefault();
			link.set('rel', 'persistentLogin');
			redirectUrl = encodeURIComponent(event.target.get('href'));
			getPopup(basePath + '/plain/shibboleth-persistent-login.html');
		}
	  },"a[href*=/secure/apps/proxy/credential]", "a[href*=redirect/cme]");

	  
	  
	// if someone click on a proxied link and he is from stanford so he will have the possibility to extend his persistent login
	  Y.on("click", function(event) {
		 var link = event.target,
		  href = link.get('href');
		if ( isActive && !drMode && !persistentStatusCookie && now.getTime() > persistentStatusCookie ) {
			event.preventDefault();
			link.set('rel', 'persistentLogin');
			redirectUrl = encodeURIComponent(event.target.get('href'));
			getPopup(basePath + '/plain/persistent-extention-popup.html');
		}
	  },"a[href*=laneproxy.stanford.edu/login]");
     


    // The popup window
    var popupWindow = function(id, o) {
        var lightbox = Y.lane.Lightbox, shibbolethAnchors, href;
        lightbox.setContent(o.responseText);
        lightbox.show();
        shibbolethAnchors = lightbox.get("contentBox").all('#shibboleth-links a');
      
        // Click on one organization -- below the url we have to set here for stanford idp
        //  /Shibboleth.sso/Login?SAMLDS=1&entityID=https%3A%2F%2Fidp.stanford.edu%2F&target=%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse%26url%3Dhttp%253A%252F%252Flocalhost%253A8080%252Fsecure%252Fapps%252Fproxy%252Fcredential%253Furl%253Dhttp%253A%252F%252Fgoogle.com
        Y.once("click",function(event) {
        	var node = event.currentTarget, url,
        	persistentUrl =  basePath + '/secure/persistentLogin.html?pl=',
        	isPersistent = Y.one('#is-persistent-login').get('checked');
 	        if(!redirectUrl) {
                 redirectUrl = "/index.html";
             }
            persistentUrl = persistentUrl + isPersistent+'&url='+redirectUrl;
            url = node.get('href')+ encodeURIComponent(persistentUrl);
            node.set('href', url);
        	
	    }, shibbolethAnchors);
    
    };
    //END POPUP

  
})();

