(() => {

    "use strict";

    /**
     * A stubbable method for getting the user agent
     * @returns {string} The navigator.userAgent string
     */
    L.getUserAgent = () => navigator.userAgent;

    /**
     * A stubbable method for setting location.href, useful for testing
     * @param {string} href - The URL to navigate to
     */
    L.setLocationHref = href => {
        location.href = href;
    };

    /**
     * Mixes event handling capabilities into an object
     * @param {object} obj - The object to make an event target
     * @param {object} [options={}] - Configuration options
     * @param {string} [options.prefix] - An event prefix for global event firing
     */
    L.addEventTarget = (obj, { prefix } = {}) => {
        // Initialize the eventListeners object
        obj.eventListeners = obj.eventListeners || {};

        if (prefix) {
            obj.eventPrefix = prefix;
        }

        // Helper to ensure an array exists for an event
        const ensureEventArray = (event) => {
            if (!obj.eventListeners[event]) {
                obj.eventListeners[event] = [];
            }
        };


        // Adds a listener to the beginning of the queue
        obj.on = (event, callback) => {
            ensureEventArray(event);
            obj.eventListeners[event].unshift(callback);
        };

        // Adds a listener to the end of the queue
        obj.after = (event, callback) => {
            ensureEventArray(event);
            obj.eventListeners[event].push(callback);
        };

        // Removes a specific listener
        obj.removeEventListener = (event, callback) => {
            const listeners = obj.eventListeners[event];
            if (listeners) {
                const index = listeners.indexOf(callback);
                if (index > -1) {
                    listeners.splice(index, 1);
                }
            }
        };

        // Fires an event, executing all its listeners
        obj.fire = (event, args) => {
            const listeners = obj.eventListeners[event];
            if (listeners) {
                // Use a copy of the array in case a listener modifies the original array
                [...listeners].forEach(callback => callback.call(obj, args));
            }
            // Fire the global event if a prefix is set
            if (obj.eventPrefix && obj.eventPrefix !== "lane") {
                L.fire(`${obj.eventPrefix}:${event}`, args);
            }
        };
    };

    L.addEventTarget(L, {
        prefix: "lane"
    });

})();
