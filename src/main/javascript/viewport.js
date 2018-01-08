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

    L.addEventTarget(viewport, {
        emitFacade : true,
        prefix : "viewport"
    });

    viewport.addTarget(L);

    viewport.fire("init", {viewport:viewport});
})();
