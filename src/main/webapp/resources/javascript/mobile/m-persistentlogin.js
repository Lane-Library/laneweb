
$.LANE.popupWindow = function(url){
    $.mobile.changePage(url, {
        transition : "pop",
        reverse : false,
        changeHash : false
    });
};

(function() {

    var redirectUrl;

    $(document).on("click", 'a[href*="laneproxy"]', function(event) {
        var link = event.currentTarget, threeDays = 3600 *3 ,
        now = new Date(), cookieValue = $.LANE.getCookie('lane-login-expiration-date');
        if (!model['disaster-mode'] &&  model.isActiveSunetID && cookieValue &&  (cookieValue - threeDays) < now.getTime()){
            redirectUrl = encodeURIComponent(link.href);
            $.LANE.popupWindow(model['base-path'] + '/m/plain/shibboleth-persistentlogin-extension.html');
            event.preventDefault();
        }
    });

    $(document).on("click", '#shibboleth-links a', function(event) {
        var persistentUrl = model['base-path']+ '/persistentLogin.html?pl=';
        if (!redirectUrl) {
            redirectUrl = "/index.html";
        }
        event.preventDefault();
        document.location =   persistentUrl + 'renew&url='+ encodeURIComponent(redirectUrl);
    });

})();


$(document).on("click", "#close", function() {
    document.location = model['base-path'] + '/';
});

// click on login link
$(document).on("click", ".webauthLogin:contains('Logout')", function(e) {
    e.preventDefault();
    if (confirm("Do you really want to logout?")) {
        document.location.href = e.target.href;
    }
});

$.LANE.toggleLogin = function() {
    if (model.auth) {
        $('.webauthLogin').each(function() {
            $(this).text('Logout');
            $(this).attr('href', model['base-path'] + '/logout');
            $(this).attr('data-ajax', 'false');
        });
    } else {
        $('.webauthLogin').each(
                function() {
                    $(this).text('Login');
                    $(this).attr('href',
                            model['base-path'] + '/secure/login.html?url='+encodeURIComponent(document.location.href));
                    $(this).attr('data-ajax', 'false');
                });
    }
};

//
// //toggle login button at every pageinit
$(this).bind("pageinit", function() {
    $.LANE.toggleLogin();
});
