
(function() {

	var redirectUrl = null,  
	PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference', 
	persistentStatusCookie = Y.Cookie.get(PERSISTENT_PREFERENCE_COOKIE_NAME), 
	now = new Date(),
	needPopup= true,
	location = Y.lane.Location,
	model = Y.lane.Model,
	basePath = model.get(model.BASE_PATH) || "",
	auth = model.get(model.AUTH),
	ipgroup = model.get(model.IPGROUP),
	drMode = model.get(model.DISASTER_MODE),
	isActive = model.get(model.IS_ACTIVE_SUNETID),
	fromHospital = "SHC" == ipgroup || "LPCH" == ipgroup,
	
	getPopup = function(urlPage) {
		Y.io(urlPage, {
			on : {
				success : popupWindow
				}
		});
	};
	
	if(drMode || fromHospital ||  'denied' == persistentStatusCookie || (persistentStatusCookie  && now.getTime() < persistentStatusCookie )){
		needPopup = false;
	}

	
	//if someone click on MyLane Login 
	 Y.on("click",function(event) {
	     var locationHref = location.get("href"),
	         encodedHref = encodeURIComponent(locationHref);
		 if (persistentStatusCookie	&& 'denied' === persistentStatusCookie) {
			//will be redirected to same page after the webauth
		     location.set("href", basePath + '/secure/persistentLogin.html?pl=false&url='+ encodedHref);
		} else {
			var link = event.target;
			link.set('rel', 'persistentLogin');
			redirectUrl = encodedHref;
			getPopup(basePath + '/plain/persistent-login-popup.html');
		}
		event.preventDefault();
	}, 'a[href=' + basePath + '/secure/login.html]');
	
	

	//if not from hospital and user click on a link that going to be proxy
	 //If there are click subscriptions at multiple points in the DOM heirarchy, they will be executed in order from
	 //most specific (the button) to least specific (document) unless e.stopPropagation() is called along the line. 
	 //This will prevent subscriptions from elements higher up the parent axis from executing.
	 Y.on("click",
		function(event) {
		 if (needPopup) {
			 	var link = event.target, clickedUrl;
			    while (link && link.get('nodeName') != 'A') {
                    link = link.get('parentNode');
                }
			    if(link){
			    	clickedUrl = link.get('href');
			    }
				if (clickedUrl && (clickedUrl.indexOf("secure/apps/proxy/credential") > 0 || clickedUrl.indexOf("laneproxy") > 0) && clickedUrl.indexOf("javascript") != 0) {
					redirectUrl = encodeURIComponent(link.get('href'));
					event.preventDefault();
					// don\'t want a redirect with the tracking see tracking.js code if !rel  documment.location is not set
					link.set('rel', 'persistentLogin');
					// if preference cookie is present but date get into grace period
					if (persistentStatusCookie && now.getTime() > persistentStatusCookie && isActive) {
						getPopup(basePath + '/plain/persistent-extension-popup.html');
					} // no preference cookie at all
					else if (!persistentStatusCookie) {
						getPopup(basePath + '/plain/persistent-popup.html');
					}
					else{// if the user not active and on "grace period" the cookies will be deleted and the page will be reload 
                        Y.io(basePath + '/logout', {
                            sync : true
                        });
		            location.reload() ;
		            }
				}
			}
		},document.body);
	
    
	// for the static page persistentlogin.hrml Click on YES this way the user 
	// will not have to go through webauth.
	if(Y.one('#persistent-login')){
		Y.on('click',function(event) {
			event.preventDefault();
			if (auth) {
			    location.set("href", basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
			} else {
			    location.set("href", basePath + '/secure/persistentLogin.html?pl=true');
			}
		}, '#persistent-login');
	}
	
	
	

	var setLink = function(event) {
		var node = event.currentTarget, url = basePath + '/';
		if ( !auth ||  node.get('search').indexOf("pl=true") >0 ) {	
			url = url + 'secure/';
		}
		if(!redirectUrl)
			redirectUrl = "/index.html";
		url = url + 'persistentLogin.html' + node.get('search') + '&url='+ redirectUrl;
		node.set('href', url);
	};
	
	// The popup window
	var popupWindow = function(id, o, args) {
		var lightbox = Y.lane.Lightbox, date = new Date(), content, yesButton, noButton, dontAskCheckBox;
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
		content = lightbox.get("contentBox");
		yesButton = content.one('#yes-persistent-login');
		noButton = content.one('#no-persistent-login');
		dontAskCheckBox = content.one('#dont-ask-again');

		// Click on YES --
		yesButton.once('click',function(event) {
		    //  createGoHttpsIE();
		    event.stopPropagation(); // don't allow event to bubble up to main click handler (line ~41) 
			setLink(event); // cookie set in the PerssitentLoginController class
		});
		
		// Click on NO
		if(noButton){
			noButton.once('click',function(event) {
				event.stopPropagation(); // don't allow event to bubble up to main click handler (line ~41)
				setLink(event);
				//if the checkbox "don't ask me again" is enable the cookie is set to denied for 10 years
				//otherwise it is set for the session only
				if(dontAskCheckBox && dontAskCheckBox.get('checked')) {
						date.setFullYear(date.getFullYear() + 10);
						Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME,	'denied', {
								path : "/",
								expires : date
							});
				} else
					Y.Cookie.set(PERSISTENT_PREFERENCE_COOKIE_NAME,'denied', {	path : "/"	});
			});
		}
	
		//if someone click on the don't ask me again" the yes button class should look disable
		if(dontAskCheckBox){
			dontAskCheckBox.on('click',function(event) {
				if (dontAskCheckBox.get('checked')) {
					yesButton.replaceClass('red-btn', 'disabled-btn');
					yesButton.detach('click');
					yesButton.on('click',function(event) {
						event.preventDefault();
					});
				} else {
					yesButton.replaceClass('disabled-btn', 'red-btn');
					yesButton.detach('click');
					yesButton.once('click',function(event) {
						setLink(event); 
					});
				}
			});
		};
	};
	//END POPUP
	
//	var createGoHttpsIE = function(){
//		if (Y.UA.ie && (Y.UA.ie == 8 || Y.UA.ie == 9)) {
//		now.setFullYear(now.getFullYear() + 10);
//		Y.Cookie.set("GO_HTTPS_IE",	'please', {
//				path : "/",
//				expires : now
//			});
//		}
//	};
	
})();

