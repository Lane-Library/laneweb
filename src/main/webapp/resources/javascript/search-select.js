(function() {
	var SearchSelect = function(options, index) {
		this._options = options ? options : [];
		this._index = index ? index : 0;
		this.publish("selectedChange", {
			defaultFn : this._setIndex
		});
	};
	SearchSelect.prototype = {
		_setIndex : function(event) {
			this._index = event.newIndex;
		},
		getSelected : function() {
			return this._options[this._index];
		},
		setSelected : function(index) {
			var newIndex = typeof index === "string" ? this._options.indexOf(index) : index;
			if (newIndex !== this._index) {
				this.fire("selectedChange", {
                    newIndex : newIndex
                });
				// this.fire("selectedChange", {index: newIndex, prevVal :
				// this._options[this._index], newVal :
				// this._options[newIndex]});
			}
		}
	};
	// Add EventTarget attributes to the SearchSelect prototype
	Y.augment(SearchSelect, Y.EventTarget, null, null, {
		emitFacade : true,
		prefix : "searchSelect"
	});
	Y.lane.SearchSelect = SearchSelect;
})();
