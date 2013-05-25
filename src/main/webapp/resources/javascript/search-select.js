(function() {
	var Select = function(items, index) {
		this._items = items ? items : [];
		this._index = index ? index : 0;
		this.publish("selectedChange", {
			defaultFn : this._setIndex
		});
	};
	Select.prototype = {
		_setIndex : function(event) {
			var index = event.newIndex;
			if (index < 0 || index > this._items.length) {
				throw "array index out of range";
			}
			this._index = event.newIndex;
		},
		getSelected : function() {
			return this._items[this._index];
		},
		setSelected : function(index) {
			var newIndex = typeof index === "string" ? Y.Array.indexOf(this._items, index) : index;
			if (newIndex !== this._index) {
				this.fire("selectedChange", {
                    newIndex : newIndex,
                    newVal : this._items[newIndex]
                });
				// this.fire("selectedChange", {index: newIndex, prevVal :
				// this._items[this._index], newVal :
				// this._items[newIndex]});
			}
		}
	};
	// Add EventTarget attributes to the Select prototype
	Y.augment(Select, Y.EventTarget, null, null, {
		emitFacade : true,
		prefix : "select"
	});
	Y.lane.Select = Select;
	
	//TODO: use a constructor to create a wrapping div then use Y.extend instead of Y.Base.create
	var SearchSelectWidget = Y.Base.create("searchSelect", Y.Widget, [], {
		renderUI : function() {
			this.get("boundingBox").insertBefore("<span class='searchselect-selected-content'>" + this.get("model").getSelected() + "</span>", this.get("srcNode"));
		},
		bindUI : function() {
			this.get("model").after("selectedChange", this._handleModelChange, this);
            this.get("srcNode").after("change", this._handleViewChange, this);
		},
		_handleViewChange : function(event) {
			this.get("model").setSelected(this.get("srcNode").get("selectedIndex"));
		},
		_handleModelChange : function(event) {
			var srcNode = this.get("srcNode"),
			    selected = event.newVal;
			srcNode.set("value", selected);
			this.get("boundingBox").one(".searchselect-selected-content").set("innerHTML", selected);
		}
	}, {
		ATTRS : {
			model : {}
		},
		HTML_PARSER : {
			model : function(srcNode) {
				var i, index = srcNode.get("selectedIndex"),
				    items = [], options = srcNode.all("option");
				for (i = 0; i < options.size(); i++) {
					items.push(options.item(i).get("value"));
					
				};
				return new Select(items, index);
			}
		}
	});
	
	Y.lane.SearchSelectWidget = SearchSelectWidget;

})();
