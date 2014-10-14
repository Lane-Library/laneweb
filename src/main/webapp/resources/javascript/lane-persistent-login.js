
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
    fromStanford = "Stanford" === ipgroup,

    getPopup = function(urlPage) {
        Y.io(urlPage, {
            on : {
                success : popupWindow
                }
        });
    };

    if(drMode ||  (persistentStatusCookie  && now.getTime() < persistentStatusCookie )){
        needPopup = false;
    }

	
    // if someone click on MyLane Login
	Y.on("click", function(event) {
		var redirectUrl = encodeURIComponent(location.get("href")), 
		link = event.target;
		link.set('rel', 'persistentLogin');
		getPopup(basePath + '/plain/shibboleth-persistent-popup.html');
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
                while (link && link.get('nodeName') !== 'A') {
                    link = link.get('parentNode');
                }
                if(link){
                    clickedUrl = link.get('href');
                }
                if (clickedUrl && (clickedUrl.indexOf("secure/apps/proxy/credential") > 0 || clickedUrl.indexOf("redirect/cme") > 0 || clickedUrl.indexOf("laneproxy") > 0) && clickedUrl.indexOf("javascript") !== 0) {
                    redirectUrl = encodeURIComponent(link.get('href'));
                    event.preventDefault();
                    // don\'t want a redirect with the tracking see tracking.js code if !rel  documment.location is not set
                    link.set('rel', 'persistentLogin');
                    // if preference cookie is present but date get into grace period
//                    if (persistentStatusCookie && now.getTime() > persistentStatusCookie && fromStanford && isActive) {
//                        getPopup(basePath + '/plain/persistent-extension-popup.html');
//                    } // no preference cookie at all
//                    else 
                    	if (!persistentStatusCookie) {
                        getPopup(basePath + '/plain/shibboleth-persistent-popup.html');
                    }
//                    else{// if the user not active and on "grace period" the cookies will be deleted and the page will be reload
//                        Y.io(basePath + '/logout', {
//                            sync : true
//                        });
//                    location.reload() ;
//                    }
                }
            }
        },document.body);


    // for the static page persistentlogin.hrml Click on YES this way the user
    // will not have to go through webauth.
//    if(Y.one('#persistent-login')){
//        Y.on('click',function(event) {
//            event.preventDefault();
//            if (auth) {
//                location.set("href", basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
//            } else {
//                location.set("href", basePath + '/secure/persistentLogin.html?pl=true');
//            }
//        }, '#persistent-login');
//    }





    // The popup window
    var popupWindow = function(id, o) {
        var lightbox = Y.lane.Lightbox, shibbolethAnchors, href;

        lightbox.setContent(o.responseText);
        lightbox.show();
        shibbolethAnchors = lightbox.get("contentBox").all('#shibboleth-links a');
      
        // Click on YES --
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

