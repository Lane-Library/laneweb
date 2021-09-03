(function(){

    "use strict";

    var viewport = function() {
            var scrolled = false,
                updateScrolled = function() {
                    scrolled = true;
                },
                checkScrolled = function() {
                    if (scrolled) {
                        viewport.fire("scrolled", {viewport:viewport});
                        scrolled = false;
                    }
                };
            window.addEventListener("scroll", updateScrolled);
            window.setInterval(checkScrolled, 500);

            return {
                // viewportMultiplier: how many times the viewport's current height
                // to still be considered in or near the viewport
                nearView: function(node, viewportMultiplier) {
                    var rect = node.getBoundingClientRect();
                    return rect.bottom >= 0 && rect.top <= viewportMultiplier * (window.innerHeight || document.documentElement.clientHeight);
                },
                inView: function(node) {
                    return this.nearView(node,1);
                }
            };

        }();

    L.addEventTarget(viewport, {
        emitFacade : true,
        prefix : "viewport"
    });

    viewport.addTarget(L);

    viewport.fire("init", {viewport:viewport});
})();
