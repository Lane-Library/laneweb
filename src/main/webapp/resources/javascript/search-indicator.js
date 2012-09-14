(function() {
	
	var SearchIndicator = Y.namespace("lane.SearchIndicator"),
	    node = Y.one("#searchIndicator");
	
	/**
	 * Adds class "on" to #searchIndicator
	 * @method show
	 * @static
	 */
	SearchIndicator.show = function() {
		if (node) {
			node.addClass("on");
		}
	};
	/**
	 * Removes class "on" from #searchIndicator
	 * @method hide
	 * @static
	 */	
	SearchIndicator.hide = function() {
		if (node) {
			node.removeClass("on");
		}
	};
	
})();