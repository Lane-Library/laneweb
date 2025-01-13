
(function () {

    "use strict";

    window.L = {};

    L.Cookie = {
        get: function (name) {
            const value = `; ${document.cookie}`;
            const parts = value.split(`; ${name}=`);
            if (parts.length === 2) {
                return cookie = parts.pop().split(';').shift();
            }
        },
        set: function (name, value, days) {
            let expires = "";
            if (days) {
                const date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toUTCString();
            }
            document.cookie = name + "=" + (value || "") + expires + "; path=/";
        },
        remove: function (name) {
            this.set(name, '', -1);
        }
    };

})();