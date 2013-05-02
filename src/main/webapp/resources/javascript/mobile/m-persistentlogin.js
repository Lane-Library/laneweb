


var redirectUrl,
	PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference',
	IS_USER_VALID;
	

$.LANE.popupWindow = function(url){
	$.mobile.changePage(url, {
		transition : "pop",
		reverse : false,
		changeHash : false
	});
};



// click on login link
$(".webauthLogin:contains('Logout')").live("click",function(e) {
	e.preventDefault();
    if(true == confirm("Do you really want to logout?")){
        document.location.href = e.target.href;
     }
});


$(".webauthLogin:contains('Login')").live("click",function(e) {
	e.preventDefault();
	var persistentStatusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
	if (persistentStatusCookie	&& 'denied' === persistentStatusCookie) {
		document.location = '/././secure/persistentLogin.html?pl=false&url='+ document.location;
	} else if($.LANE.model['disaster-mode'] == true){
        document.location = '/././login-disabled.html';
    }
	else {
		$.LANE.popupWindow("/././m/plain/persistent-login.html");
		redirectUrl = encodeURIComponent(document.location);
	}
});


//when a click is coming from a external resource
$('a[href*="secure/apps/proxy/credential"],a[href*="laneproxy"]').live("click", function(event) {
	var link = event.currentTarget, 
	now = new Date(), statusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
	if ($.LANE.model['disaster-mode'] != true && 'denied' !== statusCookie && (IS_USER_VALID === 'false' || statusCookie < now.getTime())){
		redirectUrl = encodeURIComponent(link.href);
		if(IS_USER_VALID === 'true'){ 
			$.LANE.popupWindow('/././m/plain/persistentlogin-extention.html');
		}
		else{
			$.LANE.popupWindow('/././m/plain/persistentlogin-proxylink.html');
		}
		event.preventDefault();
	}
});



$('#yes-persistent-login').live('click', function(e) {
	if ($('#dont-ask-again:checked').val() == 'on') {
		e.preventDefault();
	} else {
		setLink(event); // cookie set in the PerssitentLoginFilter class
	}
});



// Click on NO
$('#no-persistent-login').live('click', function(event) {
	if ($('#dont-ask-again') && $('#dont-ask-again:checked').val() == 'on') {
		$.LANE.setCookie(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', 3650);
	} else
		$.LANE.setCookie(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', null);
	setLink(event);
});

$('#dont-ask-again').live('click', function(e) {
	if ($('#dont-ask-again:checked').val() == 'on') {
		$('#yes-persistent-login').removeClass('red-btn').addClass('disabled-btn');
	} else {
		$('#yes-persistent-login').removeClass('disabled-btn').addClass('red-btn');
	}
});


$.LANE.toggleLogin = function(){
	if( IS_USER_VALID === 'true' || $.LANE.getCookie('webauth_at') != null){
        $('.webauthLogin').each(function(){
            $(this).text('Logout');
            $(this).attr('href','/././logout');
            $(this).attr('data-ajax','false');
        });
    }
    else{
        $('.webauthLogin').each(function(){
            $(this).text('Login');
            $(this).attr('href','/././secure/mobile-login.html');
            $(this).attr('data-ajax','false');
        });
    }	
};

$('.persistent-header').live('click', function(e) {
	document.location = '/././';
});

//
////toggle login button at every pageinit
$(this).bind("pageinit", function() {
	$.ajax({
		  url: '/././user/active',
		  async : false,
		  success:  function(data){
		 IS_USER_VALID = data;
		}
	});
	$.LANE.toggleLogin();
});

var setLink = function(event) {
	var node = event.target, url = '/././'; 
	if (node.nodeName == 'SPAN') {
		node = node.parentNode;
	}
	if ('false' == IS_USER_VALID || node.search.indexOf("pl=true") >0 ) {
		 url = url + 'secure/';
	 }
	event.preventDefault();
	document.location = url + 'persistentLogin.html' + node.search + '&url=' + redirectUrl;
};

