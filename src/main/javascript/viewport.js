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
            window.setInterval(checkScrolled, 1000);

            return {
                inView: function(node) {
                    var rect = node.getBoundingClientRect();
                    return rect.bottom >= 0 && rect.top <= (window.innerHeight || document.documentElement.clientHeight);
                }
            };

        }();

    Y.augment(viewport, Y.EventTarget, null, null, {
        emitFacade : true,
        prefix : "viewport"
    });

    viewport.addTarget(Y.lane);

    viewport.fire("init", {viewport:viewport});
})();