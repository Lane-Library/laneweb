/*
 * Mobile Login
 * 
 */

$.LANE.cookieValid = undefined;
$.LANE.cookieValidTomorrow = undefined;
$.LANE.cookieWarningIssued = undefined;

/**
 * cookie reader
 * @param cookieName
 */
$.LANE.hasCookie = function(cookieName) {
    var nameEQ = cookieName + "=", ca = document.cookie.split(';'), i, c;
    for( i = 0; i < ca.length; i++) {
        c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1,c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            //return c.substring(nameEQ.length,c.length);
            return true;
        }
    }
    return false;
};

/**
 * controller for appropriate UI buttons and warnings
 * sets login globals as well
 * 
 * @param cookieValid
 * @param validTomorrow
 */
$.LANE.toggleLogin = function(cookieValid,validTomorrow){
    var loginWarningAccepted = false;
    if($.LANE.cookieValid == undefined){
        $.LANE.cookieValid = cookieValid;
    }
    if($.LANE.cookieValidTomorrow == undefined){
        $.LANE.cookieValidTomorrow = validTomorrow;
    }
    if($.LANE.cookieValid && !$.LANE.cookieValidTomorrow && !$.LANE.cookieWarningIssued){
        $.LANE.cookieWarningIssued = true;
        loginWarningAccepted = confirm("Your persistent login is about to expire.\n Click \"OK\" to extend.");
        if(loginWarningAccepted == true){
            document.location.href = '/././m/persistentlogin.html?pl=true';
        }
        // should cancel route to logout so we don't loop?
    }
    if($.LANE.cookieValid || $.LANE.hasCookie('webauth_at')){
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

/**
 * validate login on the server
 * callback toggles login/logout button
 * 
 */
$.LANE.validateCookie = function(){
    $.ajax({
        url : "/././apps/loginCookieValidator",
        dataType : "jsonp",
        jsonpCallback : "$.LANE.toggleLogin"
    });
};

// validate cookie at page load
$(document).ready(function() {
    $.LANE.validateCookie();
});
    
// toggle login button at every pageinit
$(this).bind("pageinit", function() {
    $.LANE.toggleLogin();
});

//login form on persistent login page
$(".loginRedirect").bind("submit", function(e) {
    e.preventDefault();
    $.ajax({
        url : $(e.target).attr('action') + '?' + $(e.target).serialize(),
        dataType : "html",
        success : function(data) {
            document.location.href = '/././m/';
        }
    });
    return false;
});

// logout warning
$(".webauthLogin").live("click", function(e) {
    if(this.text == "Logout"){
        e.preventDefault();
        if(true == confirm("Do you really want to logout?")){
            document.location.href = e.target.href;
        }
    }
});

