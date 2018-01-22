(function() {

    "use strict";

    var SEARCH_INDICATOR = "search-indicator",
        SEARCH_INDICATOR_ACTIVE = SEARCH_INDICATOR + "-active",
        indicatorNode = document.querySelector("." + SEARCH_INDICATOR),

    /**
     * Provides static show and hide methods to (surprise!) show
     * and hide the search indicator animated gif.
     */
    searchIndicator = {

        /**
         * activates the .search-indicator
         * @method show
         * @static
         */
        show: function() {
            indicatorNode.classList.add(SEARCH_INDICATOR_ACTIVE);
        },

        /**
         * deactivates the .search-indicator
         * @method hide
         * @static
         */
        hide: function() {
            indicatorNode.classList.remove(SEARCH_INDICATOR_ACTIVE);
        }
    };

    L.on("search:search", searchIndicator.show);

    L.searchIndicator = searchIndicator;

})();
