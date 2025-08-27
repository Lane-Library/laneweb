{
  'use strict';

  const SEARCH_INDICATOR = 'search-indicator';
  const SEARCH_INDICATOR_ACTIVE = `${SEARCH_INDICATOR}-active`;
  const indicatorNode = document.querySelector(`.${SEARCH_INDICATOR}`);

  /**
   * Provides static show and hide methods to (surprise!) show
   * and hide the search indicator animated gif.
   */
  const searchIndicator = {
    /**
     * activates the .search-indicator
     * @method show
     * @static
     */
    show() {
      indicatorNode?.classList.add(SEARCH_INDICATOR_ACTIVE);
    },

    /**
     * deactivates the .search-indicator
     * @method hide
     * @static
     */
    hide() {
      indicatorNode?.classList.remove(SEARCH_INDICATOR_ACTIVE);
    },
  };

  L.on('search:search', searchIndicator.show);

  // LANEWEB-10724: spinner appears on back button
  window.addEventListener('pagehide', searchIndicator.hide);

  // Expose the object to the global L namespace
  L.searchIndicator = searchIndicator;
}
