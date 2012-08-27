
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
	if (needPopup && Y.all('a[href*=secure/apps/proxy/credential]' , 'a[href*=laneproxy]')) {
		Y.all('a[href*=secure/apps/proxy/credential]' , 'a[href*=laneproxy]').on("click",function(event) {
			var link = event.target, isActive;
			redirectUrl = encodeURI(link.get('href'));
			event.preventDefault();
			// don't want a redirect with the tracking see tracking.js code if !rel documment.location is not set
			link.set('rel', 'persistentLogin');
			
			//if preference cookie  is present but date get into grace period
			if (persistentStatusCookie && now.getTime() >persistentStatusCookie ){
				isActive = Y.io('/././user/active', {sync : true});
				if (isActive.responseText === 'true') {
					LANE.persistentlogin.newWindow(event,'/././plain/persistent-extension-popup.html');
					}
				} //no preference cookie at all
				else if (!persistentStatusCookie){
					LANE.persistentlogin.newWindow(event,'/././plain/persistent-popup.html');						
				}
		}, document);
	}
	
	
	if(Y.one('a[href=/././secure/login.html]')){
		 Y.all('a[href=/././secure/login.html]').on("click",function(event) {
			 if (persistentStatusCookie	&& 'denied' === persistentStatusCookie) {
				//will be redirected to same page after the webauth
				document.location = '/././secure/persistentLogin.html?pl=false&url='+ encodeURI(document.location);
			} else {
				var link = event.target;
				link.set('rel', 'persistentLogin');
				redirectUrl = encodeURI(document.location);
				LANE.persistentlogin.newWindow(event,'/././plain/persistent-login-popup.html');
			}
			event.preventDefault();
		}, document);
	}
	

//		The popup window 
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
			
			//if the checkbox "don't ask me again" is enable so no action happen    
			if (Y.one('#dont-ask-again') && Y.one('#dont-ask-again').get('checked')) {
				event.preventDefault();
			} else {
				setLink(event); // cookie set in the PerssitentLoginFilter class
			}
		});

		// Click on NO
		if (Y.one('#no-persistent-login')) {
			Y.one('#no-persistent-login').on('click',function(event) {
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
			});
		}
		if (Y.one('#dont-ask-again')) {
			//if someone click on the don't ask me again" the yes button clas should look disable
			Y.one('#dont-ask-again').on('click',function(event) {
				if (Y.one('#dont-ask-again')&& Y.one('#dont-ask-again').get('checked')) {
					Y.one('#yes-persistent-login').replaceClass('red-btn', 'disabled-btn');
				} else {
					Y.one('#yes-persistent-login').replaceClass('disabled-btn', 'red-btn');
				}
			});
		}
	};

	
	// for the static page persistentlogin.hrml Click on YES this way the user 
	// will not have to go through webauth.
	if (Y.one('#yes-persistent-login')) {
		Y.one('#yes-persistent-login').on('click',function(event) {
		event.preventDefault();
		if (auth && "" != auth.get("content")) {
			document.location = '/././persistentLogin.html?pl=renew&url=/myaccounts.html';
		} else {
			document.location = '/././secure/persistentLogin.html?pl=true';
		}
		});
	}
	
	

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

