(function () {

    "use strict";

    var objects = [];


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
        objects.push(obj);

        obj.on = function (event, callback) {
            if (!this.eventListeners) {
                this.eventListeners = {};
            }
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].push(callback);
        };

        obj.first = function (event, listener) {
            if (!this.eventListeners) {
                this.eventListeners = {};
            }
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].unshift(listener);
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
        };

        obj.addTarget = function (target) {
            obj.targets = obj.targets || [];
            obj.targets.push(target);
            obj.prefix = args.prefix;
            // if (args && args.prefix) {
            //     let events = Object.keys(target.eventListeners);
            //     events.forEach(function (event) {
            //         if (event.startsWith(args.prefix + ":")) {
            //             let callbacks = target.eventListeners[event];
            //             callbacks.forEach(callback => {
            //                 obj.on(event, callback);
            //             });
            //         }
            //     });
            // }

        }
    };

    document.addEventListener('readystatechange', event => {
        if (event.target.readyState === "complete") {
            objects.forEach(obj => {
                if (obj.targets) {
                    obj.targets.forEach(target => {
                        let events = Object.keys(target.eventListeners);
                        events.forEach(event => {
                            if (event.startsWith(obj.prefix + ":")) {
                                let callbacks = target.eventListeners[event];
                                callbacks.forEach(callback => {
                                    let eventName = event.split(":")[1];
                                    obj.on(eventName, callback);
                                });
                            }
                        });
                    });
                }
            });
        }
    });

    L.addEventTarget(L, {
        prefix: "lane",
        emitFacade: true,
        broadcast: 1
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
