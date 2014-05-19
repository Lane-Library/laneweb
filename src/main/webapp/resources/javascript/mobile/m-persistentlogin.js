


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


$(document).on("click", ".webauthLogin:contains('Login')", function(e) {
    e.preventDefault();
    var persistentStatusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (persistentStatusCookie    && 'denied' === persistentStatusCookie) {
        document.location = model['base-path'] + '/secure/persistentLogin.html?pl=false&url='+ document.location;
    } else if(model['disaster-mode']){
        document.location = model['base-path'] + '/login-disabled.html';
    }
    else {
        $.LANE.popupWindow(model['base-path'] + "/m/plain/persistent-login.html");
        redirectUrl = encodeURIComponent(document.location);
    }
});


//when a click is coming from a external resource
$(document).on("click", 'a[href*="secure/apps/proxy/credential"],a[href*="laneproxy"],a[href*="redirect/cme"]', function(event) {
    var link = event.currentTarget,
    now = new Date(), statusCookie = $.LANE.getCookie(PERSISTENT_PREFERENCE_COOKIE_NAME);
    if (!model['disaster-mode'] && 'denied' !== statusCookie && (!model["isActiveSunetID"] || statusCookie < now.getTime())){
        redirectUrl = encodeURIComponent(link.href);
        if(model["isActiveSunetID"]){
            $.LANE.popupWindow(model['base-path'] + '/m/plain/persistentlogin-extention.html');
        }
        else{
            $.LANE.popupWindow(model['base-path'] + '/m/plain/persistentlogin-proxylink.html');
        }
        event.preventDefault();
    }
});



$(document).on("click", "#yes-persistent-login", function(e) {
    if ($('#dont-ask-again:checked').val() === 'on') {
        e.preventDefault();
    } else {
        setLink(event); // cookie set in the PerssitentLoginFilter class
    }
});



// Click on NO
$(document).on("click", "#no-persistent-login", function(event) {
    if ($('#dont-ask-again') && $('#dont-ask-again:checked').val() === 'on') {
        $.LANE.setCookie(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', 3650);
    } else {
        $.LANE.setCookie(PERSISTENT_PREFERENCE_COOKIE_NAME, 'denied', null);
    }
    setLink(event);
});

$(document).on("click", "#dont-ask-again", function() {
    if ($('#dont-ask-again:checked').val() === 'on') {
        $('#yes-persistent-login').removeClass('red-btn').addClass('disabled-btn');
    } else {
        $('#yes-persistent-login').removeClass('disabled-btn').addClass('red-btn');
    }
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

var setLink = function(event) {
    var node = event.target, url = model['base-path'] + '/';
    if (node.nodeName === 'SPAN') {
        node = node.parentNode;
    }
    if (!model["isActiveSunetID"] || node.search.indexOf("pl=true") >0 ) {
         url = url + 'secure/';
     }
    event.preventDefault();
    document.location = url + 'persistentLogin.html' + node.search + '&url=' + redirectUrl;
};

