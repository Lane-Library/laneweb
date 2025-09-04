(() => {

    "use strict";

    /**
     * Viewport utility to detect if elements are in view
     * Fires a throttled event on scroll
     */
    const viewport = (() => {

        let throttleTimeout = null;
        const throttleDuration = 500;

        const throttledScrollHandler = () => {
            // do nothing if a timeout is already set
            if (throttleTimeout) {
                return;
            }

            throttleTimeout = setTimeout(() => {
                viewport.fire("scrolled", { viewport });
                // clear timeout so it can be set on the next scroll
                throttleTimeout = null;
            }, throttleDuration);
        };
        
        // passive option for better scroll performance, so browser does not need to wait for preventDefault
        window.addEventListener("scroll", throttledScrollHandler, { passive: true });

        // public viewport API
        return {
            /**
             * Checks if a node is within a multiple of the viewport's height.
             * @param {Node} node - The DOM node to check.
             * @param {number} [viewportMultiplier=1] - Multiplier for the viewport height. 
             * e.g., 1.5 means checking if the element is within 1.5x the viewport height.
             * @returns {boolean}
             */
            nearView(node, viewportMultiplier = 1) {
                if (!node) return false;
                const rect = node.getBoundingClientRect();
                const vh = window.innerHeight || document.documentElement.clientHeight;
                return rect.bottom >= 0 && rect.top <= viewportMultiplier * vh;
            },

            /**
             * Checks if a node is currently within the visible viewport.
             * @param {Node} node - The DOM node to check.
             * @returns {boolean}
             */
            inView(node) {
                return this.nearView(node, 1);
            }
        };

    })();

    L.addEventTarget(viewport, {
        prefix: "viewport"
    });

    // fire 'init' event once the viewport is ready
    viewport.fire("init", { viewport });

})();
