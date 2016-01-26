(function() {

    "use strict";

    var indicator = document.querySelector(".search-indicator");

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
            Y.lane.activate(indicator);
        },

        /**
         * deactivates the .search-indicator, activate the .search-button
         * @method hide
         * @static
         */
        hide: function() {
            Y.lane.deactivate(indicator);
        }
    };

})();