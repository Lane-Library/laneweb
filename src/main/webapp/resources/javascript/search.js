(function() {
    
    var Lane = Y.lane,
    
    SearchIndicator = Lane.SearchIndicator,
    
    /**
     * A Class that encapsulates the search form behavior.
     * 
     * @class Search
     * @uses EventTarget
     * @constructor
     * @param form {Node}
     */
    Search = function(form) {
        var widget, tips, suggest;
        
        //sanity check, is there a form?
        if (form) {
            this._form = form;
            form.on("submit", this.submitSearch, this);
            this.publish("submit",{defaultFn : this._doSubmit});
            
            //create the SearchSelectWidget and set up the Select model
            widget = new Lane.SearchSelectWidget({srcNode:form.one("#searchSource"),render:true});
            this._select = widget.get("model");
            
            this.on("select:selectedChange", this._selectedChange);
            this.addTarget(Lane);
            this._select.addTarget(this);
            this.publish("sourceChange");
            
            //create the input and set up event handler
            this._input = new Lane.TextInput(form.one("#searchTerms"), this._select.getSelectedTitle());
            Y.augment(this._input, Y.EventTarget);
            this._select.addTarget(this._input);
            this._input.after("select:selectedChange", function(event) {
                this.setHintText(event.target.getSelectedTitle());
            });
            
            //set up the search tips link
            tips = Y.one('#searchTips a');
            tips.set("href", tips.get("href") + "#" + this._select.getSelected());
            this._select.addTarget(tips);
            tips.on("select:selectedChange", function(event) {
                this.set("href", this.get("href").replace(/#.*/, "#" + event.newVal));
            });
            
            //set up the auto-complete suggestions
            suggest = new Lane.Suggest(this._input.getInput());
            suggest.setLimitForSource = function(source) {
                var limit = "";
                if (source.match(/^(all|articles|catalog)/)) {
                    limit = "er-mesh";
                } else if (source.match(/^bioresearch/)) {
                    limit = "mesh";
                } else if (source.match(/^history/)) {
                    limit = "history";
                } else if (source.match(/^bassett/)) {
                    limit = "bassett";
                }
                this.setLimit(limit);
            };
            suggest.on("select:selectedChange", function(event) {
                this.setLimitForSource(event.newVal);
            });
            this._select.addTarget(suggest);
            suggest.on("select", this.submitSearch, this);
        }
    };
    
    Search.prototype = {
            
        /**
         * The default submit handler
         * @method _doSubmit
         * @private
         * @param event {CustomEvent} the submit event fired by submitSearch()
         */
        _doSubmit : function(event) {
            if (this._input.getValue()) {
                SearchIndicator.show();
                this._form.submit();
            }
        },
        
        /**
         * The event handler for changes in the Select object.  Fires a sourceChange event.
         * @method _selectedChange
         * @private
         * @param event {CustomEvent} the selectedChange event from the Select object.
         */
        _selectedChange : function(event) {
            this.fire("sourceChange", {newVal:event.newVal});
        },
        
        /**
         * Accessor for the Select object's selected value.
         * @method getSearchSource
         * @returns {String} the Select object's selected value
         */
        getSearchSource : function() {
            return this._select.getSelected();
        },
        
        /**
         * Accessor for the search input's text.
         * @method getSearchTerms
         * @returns {String} the search input's text.
         */
        getSearchTerms : function() {
            return this._input.getValue();
        },
        
        /**
         * Reports whether or not the search input has a value.
         * @method searchTermsPresent
         * @returns {boolean} whether or not the search input has value
         */
        searchTermsPresent : function() {
            return this._input.getValue() ? true : false;
        },
        
        /**
         * Setter for the search input
         * @method setSearchTerms
         * @param terms {String} the text to put in the search input
         */
        setSearchTerms : function(terms) {
            this._input.setValue(terms);
        },
        
        /**
         * Submit the form by firing a submit event.
         * @method submitSearch
         */
        submitSearch : function(event) {
        	if (event) {
        		event.preventDefault();
        	}
            this.fire("submit");
        }
    };
    
    // Add EventTarget attributes to the Search prototype
    Y.augment(Search, Y.EventTarget, null, null, {
        prefix : "search",
        emitFacade : true
    });
    
    // create an instance and make it globally accessible
    Lane.Search = new Search(Y.one("#search"));
    
})();
