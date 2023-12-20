(function(){

    "use strict";

    let viewport = function() {
            let scrolled = false,
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
            window.setInterval(checkScrolled, 1000);

            return {
                // viewportMultiplier: how many times the viewport's current height
                // to still be considered in or near the viewport
                nearView: function(node, viewportMultiplier) {
                    let rect = node.getBoundingClientRect();
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
