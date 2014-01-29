

$.LANE.setCookie = function(name, value, days) {
    var date, expires, ONE_DAY = 24 * 60 * 60 * 1000;
    if (days) {
        date = new Date();
        date.setTime(date.getTime() + (days * ONE_DAY));
        expires = "; expires=" + date.toGMTString();
    } else {
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
};

$.LANE.getCookie = function(name) {
    var nameEQ = name + "=", ca = document.cookie.split(';');
    for ( var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length, c.length);
        }
    }
    return null;
};

$.LANE.deleteCookie = function(name) {
    setCookie(name, "", -1);
};
