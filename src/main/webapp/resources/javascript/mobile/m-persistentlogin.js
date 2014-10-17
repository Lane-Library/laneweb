


var redirectUrl,
    PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference';


$.LANE.popupWindow = function(url){
    $.mobile.changePage(url, {
        transition : "pop",
        reverse : false,
        changeHash : false
    });
};



// click on login link
$(document).on("click", ".webauthLogin:contains('Logout')", function(e) {
    e.preventDefault();
    if(confirm("Do you really want to logout?")){
        document.location.href = e.target.href;
     }
});


//when a click is coming from a external resource
$(document).on("click", 'a[href*="secure/apps/proxy/credential"],a[href*="laneproxy"], .webauthLogin:contains("Login")', function(event) {
    var link = event.currentTarget,
    now = new Date(), statusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (!model['disaster-mode'] && !statusCookie ){
    	if(link.href && link.href.indexOf('/mobile-login.html') >-1){
    		redirectUrl = encodeURIComponent(document.location);
    	}else{
    		redirectUrl = encodeURIComponent(link.href);
    	}
        $.LANE.popupWindow(model['base-path'] + '/m/plain/shibboleth-persistentlogin.html');
        event.preventDefault();
    }
});

$(document).on("click", 'a[href*="laneproxy"]', function(event) {
    var link = event.currentTarget,
    now = new Date(), statusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (!model['disaster-mode'] && !statusCookie && model["isActiveSunetID"] && statusCookie < now.getTime()){
        redirectUrl = encodeURIComponent(link.href);
        $.LANE.popupWindow(model['base-path'] + '/m/plain/persistentlogin-extention.html');
        event.preventDefault();
    }
});




$(document).on("click", '#shibboleth-links a', function(e) {
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
});




$.LANE.toggleLogin = function(){
    if( model["isActiveSunetID"] || $.LANE.getCookie('webauth_at') != null){
        $('.webauthLogin').each(function(){
            $(this).text('Logout');
            $(this).attr('href',model['base-path'] + '/logout');
            $(this).attr('data-ajax','false');
        });
    }
    else{
        $('.webauthLogin').each(function(){
            $(this).text('Login');
            $(this).attr('href',model['base-path'] + '/secure/mobile-login.html');
            $(this).attr('data-ajax','false');
        });
    }
};

$(document).on("click", ".persistent-header", function() {
    document.location = model['base-path'] + '/';
});

//
////toggle login button at every pageinit
$(this).bind("pageinit", function() {
    $.LANE.toggleLogin();
});


