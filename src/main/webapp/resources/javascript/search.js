(function() {

    "use strict";

    var Lane = Y.lane,
    Model = Y.lane.Model,
    basePath = Model.get(Model.BASE_PATH) || "",

    searchIndicator = Lane.searchIndicator,

    /**
     * A Class that encapsulates the search form behavior.
     *
     * @class Search
     * @uses EventTarget
     * @constructor
     * @param form {Node}
     */
    Search = function(form) {

        //sanity check, is there a form?
        if (form) {

            // bubble search events to the Y.lane object
            this.addTarget(Lane);

            this._form = form;
            form.on("submit", this.submit, this);
            this.publish("submit",{defaultFn : this._doSubmit});

            //create the SearchSelectWidget and set up the Select model
            this._select = new Lane.SearchSelectWidget({srcNode:form.one("#searchSource"),render:true}).get("model");
            this._select.after("selectedChange", this._selectedChange, this);
            this.publish("sourceChange", {defaultFn : this._sourceChange});

            //create the input and set up event handler
            this._input = form.one("#searchTerms");
            this._textInput = new Lane.TextInput(this._input, this._select.getSelectedTitle());
            this._input.on("valueChange", this._handleValueChange, this);

            //set up the search tips link
            this._tips = Y.one('#searchTips');
            this._setSearchTipsUrl();

            //set up the auto-complete suggestions
            this._suggest = new Lane.Suggest(this._input);
            this._suggest.setLimitForSource = function(source) {
                var limit = "";
                if (source.match(/^(all|articles|catalog)/)) {
                    limit = "er-mesh";
                } else if (source.match(/^(bioresearch|images)/)) {
                    limit = "mesh";
                } else if (source.match(/^bassett/)) {
                    limit = "bassett";
                }
                this.setLimit(limit);
            };
            this._suggest.setLimitForSource(this._select.getSelected());
            this._suggest.on("select", this._handleSuggestSelect, this);

            //set up search reset
            this._searchReset = Y.one(".searchReset");
            if (this._textInput.getValue()) {
                this._searchReset.addClass("active");
            }
            this._searchReset.on("click", this._handleResetClick, this);
            this.publish("reset", {defaultFn : this._reset});
        }
    };

    Search.prototype = {

        /**
         * The default submit handler.  Does nothing if there is no input.
         * @method _doSubmit
         * @private
         */
        _doSubmit : function() {
            this._searchReset.removeClass("active");
            if (this._textInput.getValue()) {
                searchIndicator.show();
                this._form.submit();
            }
        },

        /**
         * Handles clicks on the reset button, fires a reset event.
         * @method _handleResetClick
         * @private
         */
        _handleResetClick : function() {
            this.fire("reset");
        },

        /**
         * Handles suggestion selections, fires a tracking event.
         * @method _handleSuggestSelect
         * @private
         */
        _handleSuggestSelect : function(event) {
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:suggestSelect",
                action: this.getSource(),
                label: event.suggestion
            });
            this.submit();
        },

        /**
         * Handles search input value changes and toggles the active class
         * of the reset button appropriately.
         * @method _handleValueChange
         * @private
         */
        _handleValueChange : function() {
            if (this._textInput.getValue()) {
                this._searchReset.addClass("active");
            } else {
                this._searchReset.removeClass("active");
            }
        },

        /**
         * Clears the search input and results if present.
         * @method reset
         * @private
         */
        _reset : function() {
            this._textInput.reset();
            this._searchReset.removeClass("active");
            this._form.all('input[type=hidden]').each(function(){
                if(this.get("name").match(/sort|facets/)){
                    this.remove();
                }
            });
            this._input.focus();
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:searchFormReset",
                action: Y.lane.Location.get("pathname")
            });
        },

        /**
         * The event handler for changes in the Select object.  Fires a sourceChange event.
         * @method _selectedChange
         * @private
         * @param event {CustomEvent} the selectedChange event from the Select object.
         */
        _selectedChange : function(event) {
            this.fire("sourceChange", {newVal:event.newVal, prevVal:event.prevVal});
        },

        /**
         * Set the proper url for search tips.
         * @method _setSearchTipsUrl
         * @private
         */
        _setSearchTipsUrl : function() {
            var url = "lane", source = this._select.getSelected();
            if (source.match(/^(clinical|peds)/)) {
                url = "pico";
            } else if (source.match(/^bioresearch/)) {
                url = "bioresearch";
            } else if (source.match(/^bassett/)) {
                url = "bassett";
            } else if (source.match(/^images/)) {
                url = "bioimage";
            } else if (source.match(/^textbook/)) {
                url = "textbook";
            }
            this._tips.set("href", basePath + "/" + url + "search.html");
        },

        /**
         * Default handler for sourceChange. Changes the search tips url,
         * the limit parameter for the suggestions, and the input hint text.
         * @method _sourceChange
         * @private
         */
        _sourceChange : function(event) {
            this._textInput.setHintText(this._select.getSelectedTitle());
            this._setSearchTipsUrl();
            this._suggest.setLimitForSource(event.newVal);
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:searchDropdownSelection",
                action: event.newVal,
                label: "from " + event.prevVal + " to " + event.newVal
            });
        },

        /**
         * Accessor for the Select object's selected value.
         * @method getSource
         * @returns {String} the Select object's selected value
         */
        getSource : function() {
            return this._select.getSelected();
        },

        /**
         * Accessor for the search input's text.
         * @method getQuery
         * @returns {String} the search input's text.
         */
        getQuery : function() {
            return this._textInput.getValue();
        },

        /**
         * Reports whether or not the search input has a value.
         * @method searchTermsPresent
         * @returns {boolean} whether or not the search input has value
         */
        searchTermsPresent : function() {
            return this._textInput.getValue() ? true : false;
        },

        /**
         * Setter for the search input
         * @method setSearchTerms
         * @param terms {String} the text to put in the search input
         */
        setSearchTerms : function(terms) {
            this._textInput.setValue(terms);
        },

        /**
         * Submit the form by firing a submit event.
         * @method submit
         */
        submit : function(event) {
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
    Lane.search = new Search(Y.one("#search"));

})();
