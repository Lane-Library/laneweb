(function() {

    "use strict";

    let SEARCH_INDICATOR = "search-indicator",
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
			if(indicatorNode){
            	indicatorNode.classList.remove(SEARCH_INDICATOR_ACTIVE);
            }
        }
    };

    L.on("search:search", searchIndicator.show);

    // LANEWEB-10724: spinner appears on back button
    addEventListener("pagehide", searchIndicator.hide);

    L.searchIndicator = searchIndicator;

})();
