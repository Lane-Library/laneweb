(function() {

    "use strict";

    L.getUserAgent = function() {
        return window.navigator.userAgent;
    };

    /*
     * stubbable method for setting location.href
     */
    L.setLocationHref = function(href) {
        location.href = href;
    };

    L.addEventTarget = function(obj, args) {
        Y.augment(obj, Y.EventTarget, false, null, args);
    };

    L.io = Y.io;
    L.Get = Y.Get;
    L.Cookie = Y.Cookie;

    L.addEventTarget(L, {
        prefix : "lane",
        emitFacade : true,
        broadcast : 1
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
        Element.prototype.closest = function(selector) {
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
          value: function() {
            if (this.parentNode !== null) {
                this.parentNode.removeChild(this);
            }
          }
        });
      });
    })([Element.prototype, CharacterData.prototype, DocumentType.prototype]);

})();
