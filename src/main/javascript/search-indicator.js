(function() {

    "use strict";

    var SEARCH_INDICATOR = "search-indicator",
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
            L.activate(indicatorNode, SEARCH_INDICATOR);
        },

        /**
         * deactivates the .search-indicator
         * @method hide
         * @static
         */
        hide: function() {
            L.deactivate(indicatorNode, SEARCH_INDICATOR);
        }
    };

    L.on("search:search", searchIndicator.show);

    L.searchIndicator = searchIndicator;

})();
