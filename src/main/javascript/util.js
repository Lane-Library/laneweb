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
            if (event instanceof Array) {
                event.forEach(e => {
                    this.pushEvent(e, callback);
                })
            } else {
                this.pushEvent(event, callback);
            }
        };

        obj.pushEvent = function (event, callback) {
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].push(callback);
        }

        obj.first = function (event, callback) {
            if (!this.eventListeners) {
                this.eventListeners = {};
            }
            if (event instanceof Array) {
                event.forEach(e => {
                    this.unshiftEvent(e, callback);
                })
            } else {
                this.unshiftEvent(event, callback);
            }
        };

        obj.unshiftEvent = function (event, callback) {
            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }
            this.eventListeners[event].unshift(callback);
        }

        obj.removeEventsListener = function (event, callback) {
            if (this.eventListeners && this.eventListeners[event]) {
                if (event instanceof Array) {
                    event.forEach(e => {
                        this.spliceEvent(e, callback)
                    })
                } else {
                    this.spliceEvent(event, callback)
                }
            }
        }

        obj.spliceEvent = function (event, callback) {
            let index = this.eventListeners[event].indexOf(callback);
            if (index > -1) {
                this.eventListeners[event].splice(index, 1);
            }
        }


        obj.fire = function (event, args) {
            if (this.eventListeners && this.eventListeners[event]) {
                args = args || {};
                this.eventListeners[event].forEach(callback => callback.call(this, args));
            }
        };

        obj.addTarget = function (target) {
            obj.targets = obj.targets || [];
            obj.targets.push(target);
            obj.prefix = args.prefix;
        }
    };


    document.addEventListener('readystatechange', event => {
        if (event.target.readyState === "complete") {
            mergeEvents();
        }
    });

    function mergeEvents() {
        objects.forEach(obj => {
            if (obj.targets) {
                obj.targets.forEach(target => {
                    addEventsListenerToObject(target, obj);
                });
            }
        });
    }

    function addEventsListenerToObject(target, obj) {
        let events = Object.keys(target.eventListeners);
        events.forEach(targetEventName => {
            targetEventName.split(",").forEach(e => {
                addEventListenerToObject(e, targetEventName, target, obj)
            });
        });
    }

    function addEventListenerToObject(event, targetEventName, target, obj) {
        if (event.startsWith(obj.prefix + ":")) {
            let callbacks = target.eventListeners[targetEventName];
            callbacks.forEach(callback => {
                let eventName = event.split(":")[1];
                obj.on(eventName, callback);
            });
        }
    }

    //For testing
    L.mergeEvents = mergeEvents;

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
