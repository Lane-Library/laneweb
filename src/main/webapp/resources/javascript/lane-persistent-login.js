(function() {

	var redirectUrl,
	PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference';
	
	
	LANE.persistentlogin = function() {
		return {
			newWindow : function(event, urlPage) {
				Y.io(urlPage, {
					on : {
						success : show
					}
				});
			}
		};
	}();

	
	
	Y.on('click', function(event) {
		var link = event.target, 
		userCookie = Y.Cookie.get('user'),
		now = new Date(),
		isActive,
		persistentStatusCookie = Y.Cookie.get(PERSISTENT_PREFERENCE_COOKIE_NAME);
		
		if (link && link.get('nodeName') == 'A' && (link.get('pathname').indexOf('secure/apps/proxy/credential') > -1 || link.get('host').indexOf('laneproxy') === 0)) {
			if (!persistentStatusCookie ||( 'denied' !== persistentStatusCookie &&  persistentStatusCookie < now.getTime() )) {
				// don't want a redirect with the tracking see tracking.js code if !rel documment.location is not set
				link.set('rel', 'persistentLogin');
				redirectUrl = escape(link.get('href'));
				if(userCookie){
					isActive = Y.io('/././user/active', { sync: true});
					if(isActive.responseText === 'true' ){
						LANE.persistentlogin.newWindow(event, '/././plain/persistent-extension-popup.html');
						event.preventDefault();
					}
				}
				else{
					LANE.persistentlogin.newWindow(event, '/././plain/persistent-popup.html');
					event.preventDefault();
				}
					
			}
			
		} else if (link && link.get('nodeName') == 'A'	&& (link.get('pathname').indexOf('secure/login.html') > -1)) {
			if (persistentStatusCookie && 'denied' === persistentStatusCookie) {
				document.location =  '/././secure/persistentLogin.html?&url='+escape(document.location);
			} else{
				link.set('rel', 'persistentLogin');
				redirectUrl = escape(document.location);
				LANE.persistentlogin.newWindow(event,'/././plain/persistent-login-popup.html');
			}
			event.preventDefault();
		}
	}, document);
		
	
	var show = function(id, o, args) {
		var lightbox = Y.lane.Lightbox,
		date = new Date();
		lightbox.setContent(o.responseText);
		lightbox.show();
		// To hide tclose the window
		Y.get('#lightboxClose').setStyle('visibility', 'hidden');
		
		// Click on YES --
		Y.get('#yes-persistent-login').on('click', function(event) {
			if(Y.get('#dont-ask-again') && Y.get('#dont-ask-again').get('checked')){
				event.preventDefault();
			}
			else{
				setLink(event); //cookie set in the PerssitentLoginFilter class
			}
		});
		
		// Click on NO
		if(Y.get('#no-persistent-login')){
			Y.get('#no-persistent-login').on('click', function(event) {
				setLink(event);
				if(Y.get('#dont-ask-again') && Y.get('#dont-ask-again').get('checked')){
					date.setFullYear(date.getFullYear()+10);
					Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', {path: "/",expires: date});
				} else
					Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', {path: "/"});
			});
		}
		if(Y.get('#dont-ask-again')){
			Y.get('#dont-ask-again').on('click', function(event) {
				if(Y.get('#dont-ask-again') && Y.get('#dont-ask-again').get('checked')){
					Y.get('#yes-persistent-login').set('src', '/../../graphics/buttons/yes-off.gif');
					Y.get('#yes-persistent-login').setStyles({cursor :'default'});
				} else{
					Y.get('#yes-persistent-login').set('src', '/../../graphics/buttons/yes.gif');
					Y.get('#yes-persistent-login').setStyles({cursor :'auto'});
				}
			});
		}
	};
		


	// /to Display persistent login expiration day left
	if (Y.get('#persistent-login-expiration-day')) {
		var ONE_DAY = 1000 * 60 * 60 * 24,
		 expDay = 0,
		expDate = Y.Cookie.get('persistent-expiration-date');  
		if (expDate) {
			expDay = Math.round((expDate - new Date()) / ONE_DAY);
		}
		Y.get('#persistent-login-expiration-day').set("innerHTML", expDay);
	}
	
	//for the static page persistentlogin.hrml
	// Click on YES --
	if (Y.get('#yes-persistent-login')) {
		Y.get('#yes-persistent-login').on('click', function(event) {
			var url = '/././', 
			userCookie = Y.Cookie.get('user');
			if (!userCookie ) {
				url = url + 'secure/';
			}
			document.location =  url + 'persistentLogin.html?pl=true';
		});
	}
	
	
	var setLink = function(event) {
		var node = event.target, 
		url = '/././', 
		userCookie = Y.Cookie.get('user');
		node = node.get('parentNode');
		if (!userCookie ) {
			url = url + 'secure/';
		}
		url = url + 'persistentLogin.html' + node.get('search') + '&url='+ redirectUrl;
		node.set('href', url);
	};

	
		
	
})();
