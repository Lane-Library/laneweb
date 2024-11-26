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
                console.log("fire event: " + event);
                this.eventListeners[event].forEach(callback => callback.call(this, args));
            }
            if (obj.eventPrefix && obj.eventPrefix !== "lane") {
                console.log("repost LANE event: " + obj.eventPrefix + ":" + event);
                L.fire(obj.eventPrefix + ":" + event, args);
            }
        };
    };

    L.addEventTarget(L, {
        prefix: "lane"
    });



    /*
     * polyfill for NodeList.prototype.forEach() from
     * https://github.com/imagitama/nodelist-foreach-polyfill
     */
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = function (callback, thisArg) {
            thisArg = thisArg || window;
            for (let i = 0; i < this.length; i++) {
                callback.call(thisArg, this[i], i, this);
            }
        };
    }

    /*
     * polyfill for Element.prototype.matches() from
     * https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
     */
    if (!Element.prototype.matches) {
        Element.prototype.matches =
            Element.prototype.msMatchesSelector ||
            Element.prototype.webkitMatchesSelector;
    }

    /*
     * polyfill for Element.prototype.closest() modified from
     * https://plainjs.com/javascript/traversing/get-closest-element-by-selector-39/
     */
    if (!Element.prototype.closest) {
        Element.prototype.closest = function (selector) {
            let el = this;
            while (el && el.matches && !el.matches(selector)) {
                el = el.parentNode;
            }
            return el && el.matches ? el : null;
        };
    }

    /*
     * polyfill for remove()
     * from:https://github.com/jserz/js_piece/blob/master/DOM/ChildNode/remove()/remove().md
     */
    (function (arr) {
        arr.forEach(function (item) {
            if (item.hasOwnProperty('remove')) {
                return;
            }
            Object.defineProperty(item, 'remove', {
                configurable: true,
                enumerable: true,
                writable: true,
                value: function () {
                    if (this.parentNode !== null) {
                        this.parentNode.removeChild(this);
                    }
                }
            });
        });
    })([Element.prototype, CharacterData.prototype, DocumentType.prototype]);

})();
