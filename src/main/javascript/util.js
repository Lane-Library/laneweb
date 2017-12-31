(function() {

    "use strict";

    L.activate = function(node, clazz) {
        node.classList.add(clazz +"-active");
    };

    L.deactivate = function(node, clazz) {
        node.classList.remove(clazz + "-active");
    };

    L.getData = function(node, name) {
        return node.dataset[name];
    };

    L.ancestor = function(node, selector, self) {
        var result;
        if (self && node.matches(selector)) {
            result = node;
        } else {
            result = node.parentNode;
            while (result && result.nodeType === 1 && !result.matches(selector)) {
                result = result.parentNode;
            }
        }
        return (result && result.nodeType === 1) ? result : null;
    };

    /*
     * polyfill for NodeList.prototype.forEach() from
     * https://github.com/imagitama/nodelist-foreach-polyfill
     */
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = function (callback, thisArg) {
            thisArg = thisArg || window;
            for (var i = 0; i < this.length; i++) {
                callback.call(thisArg, this[i], i, this);
            }
        };
    }

    /*
     * stubbable method for setting location.href
     */
    L.setLocationHref = function(href) {
        location.href = href;
    };

    /*
     * polyfill for Element.prototype.matches() from
     * https://developer.mozilla.org/en-US/docs/Web/API/Element/matches
     */
    if (!Element.prototype.matches) {
        Element.prototype.matches =
            Element.prototype.matchesSelector ||
            Element.prototype.mozMatchesSelector ||
            Element.prototype.msMatchesSelector ||
            Element.prototype.oMatchesSelector ||
            Element.prototype.webkitMatchesSelector ||
            function(s) {
                var matches = (this.document || this.ownerDocument).querySelectorAll(s),
                    i = matches.length;
                while (--i >= 0 && matches.item(i) !== this) {}
                return i > -1;
            };
    }

})();
