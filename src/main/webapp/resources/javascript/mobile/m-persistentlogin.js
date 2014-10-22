


var redirectUrl,
    PERSISTENT_PREFERENCE_COOKIE_NAME = 'lane-login-expiration-date';


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
    now = new Date(), cookieValue = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (!model['disaster-mode'] && ! cookieValue){
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
    var link = event.currentTarget, threeDays = 3600 *3 , 
    now = new Date(), cookieValue = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (!model['disaster-mode']  && model["isActiveSunetID"] && (cookieValue - threeDays) < now.getTime()){
        redirectUrl = encodeURIComponent(link.href);
        $.LANE.popupWindow(model['base-path'] + '/m/plain/shibboleth-persistentlogin-extension.html');
        event.preventDefault();
    }
});




$(document).on("click", '#shibboleth-links a', function(e) {
	var node = event.target, url, 
	persistentUrl = model['base-path']+ '/secure/persistentLogin.html?pl=', 
	isPersistent;
	if (node.nodeName === 'SPAN') {
        node = node.parentNode;
    }
	if (!redirectUrl) {
		redirectUrl = "/index.html";
	}
	event.preventDefault();
	if($('#is-persistent-login').length >0){
		isPersistent = $('#is-persistent-login').prop( "checked" );
		document.location =  node.href + encodeURIComponent( persistentUrl + isPersistent + '&url='+ redirectUrl);
	}else{
		document.location =   persistentUrl + 'renew&url='+ encodeURIComponent(redirectUrl);
	}
});




$.LANE.toggleLogin = function(){
    if( model['auth']){
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


