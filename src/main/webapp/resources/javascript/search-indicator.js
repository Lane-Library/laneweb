(function() {

    "use strict";

    var SEARCH_INDICATOR = "search-indicator",
        indicator = document.querySelector("." + SEARCH_INDICATOR);

    /**
     * Provides static show and hide methods to (surprise!) show
     * and hide the search indicator animated gif.
     */
    Y.lane.searchIndicator = {

        /**
         * activates the .search-indicator, deactivates the .search-button
         * @method show
         * @static
         */
        show: function() {
            Y.lane.activate(indicator, SEARCH_INDICATOR);
        },

        /**
         * deactivates the .search-indicator, activate the .search-button
         * @method hide
         * @static
         */
        hide: function() {
            Y.lane.deactivate(indicator, SEARCH_INDICATOR);
        }
    };

})();