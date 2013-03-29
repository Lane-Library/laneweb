(function() {
	
	/**
	 * Provides static show and hide methods to (surprise!) show
	 * and hide the search indicator animated gif.
	 */
	var SearchIndicator = Y.namespace("lane.SearchIndicator"),
	    node = Y.one("#searchIndicator");
	
	/**
	 * Adds class "on" to #searchIndicator
	 * @method show
	 * @static
	 */
	SearchIndicator.show = function() {
		if (node) {
			node.addClass("show");
		}
	};
	/**
	 * Removes class "on" from #searchIndicator
	 * @method hide
	 * @static
	 */	
	SearchIndicator.hide = function() {
		if (node) {
			node.removeClass("show");
		}
	};
	
})();