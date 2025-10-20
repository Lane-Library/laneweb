(() => {

    "use strict";

    window.L = window.L || {};

    L.Cookie = {

        /**
         * Gets a cookie value by name.
         * @param {string} name - The name of the cookie.
         * @returns {string|undefined} The cookie value or undefined if not found.
         */
        get(name) {
            const cookie = document.cookie
                .split(';')
                .find(c => c.trim().startsWith(`${name}=`));

            if (!cookie) {
                return undefined;
            }
            return cookie.split('=')[1];
        },

        /**
         * Sets a cookie.
         * @param {string} name - The name of the cookie.
         * @param {string} value - The value of the cookie.
         * @param {number} [days] - The number of days until the cookie expires.
         */
        set(name, value, days) {
            let expires = "";
            if (days) {
                const date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = `; expires=${date.toUTCString()}`;
            }
            document.cookie = `${name}=${value || ""}${expires}; path=/`;
        },

        /**
         * Removes a cookie by name.
         * @param {string} name - The name of the cookie to remove.
         */
        remove(name) {
            this.set(name, '', -1);
        }
    };

})();