
$.LANE.popupWindow = function(url){
    $.mobile.changePage(url, {
        transition : "pop",
        reverse : false,
        changeHash : false
    });
};

$(document).on("click", "#close", function() {
    document.location = window.model['base-path'] + '/';
});

// click on login link
$(document).on("click", ".webauthLogin:contains('Logout')", function(e) {
    e.preventDefault();
    if (confirm("Do you really want to logout?")) {
        document.location.href = e.target.href;
    }
});

$.LANE.toggleLogin = function() {
    if (window.model.auth) {
        $('.webauthLogin').each(function() {
            $(this).text('Logout');
            $(this).attr('href', window.model['base-path'] + '/logout');
            $(this).attr('data-ajax', 'false');
        });
    } else {
        $('.webauthLogin').each(
                function() {
                    $(this).text('Login');
                    $(this).attr('href',
                            window.model['base-path'] + '/secure/login.html?url='+encodeURIComponent(document.location.href));
                    $(this).attr('data-ajax', 'false');
                });
    }
};

//
// //toggle login button at every pageinit
$(this).bind("pageinit", function() {
    $.LANE.toggleLogin();
});
