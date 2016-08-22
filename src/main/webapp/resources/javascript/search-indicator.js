(function() {

    "use strict";

    var SEARCH_INDICATOR = "search-indicator",
        lane = Y.lane,
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
            lane.activate(indicatorNode, SEARCH_INDICATOR);
        },

        /**
         * deactivates the .search-indicator
         * @method hide
         * @static
         */
        hide: function() {
            lane.deactivate(indicatorNode, SEARCH_INDICATOR);
        }
    };

    lane.on("search:search", searchIndicator.show);

    lane.searchIndicator = searchIndicator;

})();