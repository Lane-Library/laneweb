

var cookieMock;
var redirectUrl,
	LANE_USER_COOKIE_NAME = 'user',
	PERSISTENT_PREFERENCE_COOKIE_NAME = 'persistent-preference',
	EXPIRATION_DATE_COOKIE_NAME = 'persistent-expiration-date'; 
	

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
	} else {
		$.LANE.popupWindow("/././m/plain/persistent-login.html");
		redirectUrl = escape(document.location);
	}
});


//when a click is coming from a external resource
$('a[href*="secure/apps/proxy/credential"],a[href*="laneproxy"]').live("click", function(event) {
	var link = event.target, 
	now = new Date();
	var expirationDate = $.LANE.getCookie(EXPIRATION_DATE_COOKIE_NAME);
	if (!$.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME) || (!'denied' === $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME)  && expirationDate < now.getTime())){
		redirectUrl = escape(link.href);
		var isValidSundetId = "false"; 
		$.ajax({
			  url: '/././user/active',
			  async : false,
			  success:  function(data){
			isValidSundetId = data;
			}
		});
		if(isValidSundetId === "true"){ 
				$.LANE.popupWindow('/././m/plain/persistentlogin-extention.html');
				event.preventDefault();
		}
		else{
			$.LANE.popupWindow('/././m/plain/persistentlogin-proxylink.html');
			event.preventDefault();
		}
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
	if(($.LANE.getCookie(LANE_USER_COOKIE_NAME) != null ) || $.LANE.getCookie('webauth_at') != null){
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
	if(cookieMock && cookieMock.name)
		$('#sunetId').text(cookieMock.name);
};



//
////toggle login button at every pageinit
$(this).bind("pageinit", function() {
	$.LANE.toggleLogin();
});

var setLink = function(event) {
	var node = event.target, url = '/././'; 
	if (node.nodeName == 'SPAN') {
		node = node.parentNode;
	}
	 if (!cookieMock.valid ) {
	 url = url + 'secure/';
	 }
	event.preventDefault();
	document.location = url + 'persistentLogin.html' + node.search + '&url=' + redirectUrl;
};

