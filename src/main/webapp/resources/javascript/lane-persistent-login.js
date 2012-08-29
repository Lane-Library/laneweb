
(function() {

	var redirectUrl = null,  
	PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference', 
	persistentStatusCookie = Y.Cookie.get(PERSISTENT_PREFERENCE_COOKIE_NAME), 
	now = new Date(),
	needPopup= true,
	auth = Y.one('html head meta[name="auth"]'),
	metaGroup = Y.one('html head meta[name="ipGroup"]'),	
	fromHospital = false;
	
	if (!metaGroup && ("SHC" == metaGroup.get("content") || "LPCH" == metaGroup.get("content"))){
		fromHospital = true;
	}
	if(fromHospital ||  'denied' == persistentStatusCookie || (persistentStatusCookie  && now.getTime() < persistentStatusCookie )){
		needPopup = false;
	}

	
	
	//if not from hospital and user click on a link that going to be proxy
    if (needPopup ){
        Y.on("click", _proxiedResourceClickedOn, 'a[href*=secure/apps/proxy/credential]');        
        Y.on("click", _proxiedResourceClickedOn, 'a[href*=laneproxy]');
    }

    
	
	//if someone click on MyLane Login 
	 Y.on("click",function(event) {
		 if (persistentStatusCookie	&& 'denied' === persistentStatusCookie) {
			//will be redirected to same page after the webauth
			document.location = '/././secure/persistentLogin.html?pl=false&url='+ encodeURIComponent(document.location);
		} else {
			var link = event.target;
			link.set('rel', 'persistentLogin');
			redirectUrl = encodeURIComponent(document.location);
			LANE.persistentlogin.newWindow(event,'/././plain/persistent-login-popup.html');
		}
		event.preventDefault();
	}, 'a[href=/././secure/login.html]');

	
	
    function _proxiedResourceClickedOn(event) {
		var link = event.target, isActive;
		redirectUrl = encodeURIComponent(link.get('href'));
		event.preventDefault();
		// don\'t want a redirect with the tracking see tracking.js code if !rel
		// documment.location is not set
		link.set('rel', 'persistentLogin');

		// if preference cookie is present but date get into grace period
		if (persistentStatusCookie && now.getTime() > persistentStatusCookie) {
			isActive = Y.io('/././user/active', {
				sync : true
			});
			if (isActive.responseText === 'true') {
				LANE.persistentlogin.newWindow(event,'/././plain/persistent-extension-popup.html');
			}
		} // no preference cookie at all
		else if (!persistentStatusCookie) {
			LANE.persistentlogin.newWindow(event,'/././plain/persistent-popup.html');
		}
	}
	
// The popup window
	var popupWindow = function(id, o, args) {
		var lightbox = Y.lane.Lightbox, date = new Date();
		if (Y.UA.ie && Y.UA.ie < 8) {
	        lightbox.on("visibleChange", function(event) {
	            if (event.newVal) {
	                var boundingBox = this.get("boundingBox");
//	              //this forces the markup to be rendered, not sure why it is needed.
	              boundingBox.setStyle("visibility", "hidden");
	              boundingBox.setStyle("visibility", "visible");
	            }
	        }, lightbox);
		}
		lightbox.setContent(o.responseText);
		lightbox.show();
		
		// Click on YES --
		Y.one('#yes-persistent-login').once('click',function(event) {
				setLink(event); // cookie set in the PerssitentLoginFilter class
		});
		

		// Click on NO
		Y.on('click',function(event) {
			setLink(event);
			//if the checkbox "don't ask me again" is enable the cookie is set to denied for 10 years
			//otherwise it is set for the session only
			if (Y.one('#dont-ask-again') && Y.one('#dont-ask-again').get('checked')) {
					date.setFullYear(date.getFullYear() + 10);
					Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME,	'denied', {
							path : "/",
							expires : date
						});
			} else
				Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME,'denied', {	path : "/"	});
		}, '#no-persistent-login');
	
	
		//if someone click on the don't ask me again" the yes button clas should look disable
		Y.on('click',function(event) {
			var yesButton = Y.one('#yes-persistent-login');
			if (Y.one('#dont-ask-again').get('checked')) {
				yesButton.replaceClass('red-btn', 'disabled-btn');
				yesButton.detach('click');
				yesButton.on('click',function(event) {
					event.preventDefault();
				});
			} else {
				yesButton.replaceClass('disabled-btn', 'red-btn');
				yesButton.detach('click');
				yesButton.once('click',function(event) {
					setLink(event); // cookie set in the PerssitentLoginFilter class
			});
			}
		},'#dont-ask-again');
	};

	
	// for the static page persistentlogin.hrml Click on YES this way the user 
	// will not have to go through webauth.
		Y.on('click',function(event) {
			event.preventDefault();
			if (auth && "" != auth.get("content")) {
				document.location = '/././persistentLogin.html?pl=renew&url=/myaccounts.html';
			} else {
				document.location = '/././secure/persistentLogin.html?pl=true';
			}
		}, '#persistent-login');

	
	
	

	var setLink = function(event) {
		var node = event.target, url = '/././';
		if (node.get('tagName') === 'SPAN') {
			node = node.get('parentNode');
		}
		if ( auth && "" === auth.get("content") ||  node.get('search').indexOf("pl=true") >0 ) {	
			url = url + 'secure/';
		}
		url = url + 'persistentLogin.html' + node.get('search') + '&url='+ redirectUrl;
		node.set('href', url);
	}

	
	LANE.persistentlogin = function() {
		return {
			newWindow : function(event, urlPage) {
				Y.io(urlPage, {
					on : {
						success : popupWindow
						}
				});
			}
		};
	}();
	
})();

