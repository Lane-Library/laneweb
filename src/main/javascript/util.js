(function () {

    "use strict";



    L.getUserAgent = function () {
        return window.navigator.userAgent;
    };

    /*
     * stubbable method for setting location.href
     */
    L.setLocationHref = function (href) {
        location.href = href;
    };

    L.addEventTarget = function (obj, args) {
        if (args && args.prefix) {
            obj.eventPrefix = args.prefix;
        }

        obj.on = function (event, callback) {
            if (!this.eventListeners) {
                this.eventListeners = {};
            }
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].unshift(callback);
        }


        obj.after = function (event, callback) {
            if (!this.eventListeners) {
                this.eventListeners = {};
            }
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].push(callback);
        };

        obj.removeEventListener = function (event, callback) {
            if (this.eventListeners && this.eventListeners[event]) {
                const index = this.eventListeners[event].indexOf(callback);
                if (index > -1) {
                    this.eventListeners[event].splice(index, 1);
                }
            }
        };

        obj.fire = function (event, args) {
            if (this.eventListeners && this.eventListeners[event]) {
                this.eventListeners[event].forEach(callback => callback.call(this, args));
            }
            if (obj.eventPrefix && obj.eventPrefix !== "lane") {
                L.fire(obj.eventPrefix + ":" + event, args);
            }
        };
    };

    L.addEventTarget(L, {
        prefix: "lane"
    });

})();
