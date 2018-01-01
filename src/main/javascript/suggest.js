
(function() {

    "use strict";

    var model = L.Model,
        basePath = model.get(model.BASE_PATH) || "",
    SOURCE_BASE = basePath + "/apps/suggest/getSuggestionList?q={query}&l=",
    DEFAULT_LIMIT = "mesh-di",
    SELECT = "select",

    /**
     * A class that provides an autocomplete widget for inputs.
     *
     * @class Suggest
     * @uses EventTarget
     * @constructor
     * @param input {Node} the input node.
     * @param limit {String} the limit parameter for the request.
     */
    Suggest = function(input, limit) {
        input.plug(Y.Plugin.AutoComplete, {
            minQueryLength: 3,
            source: SOURCE_BASE + (limit || DEFAULT_LIMIT),
            width: "100%"
        });

        /**
         * @event select
         * @description fired when a suggestion is selected
         */
        this.publish(SELECT,{
            emitFacade : true
        });

        //save the input
        this._input = input;

        //save the autocomplete object
        this._ac = input.ac;

        //hoveredItemChange is fired on mouseover events in the suggestion list
        this._ac.after("hoveredItemChange", this._handleHoveredItemChange, this);

        //select is fired on clicks or return pressed in suggestion list
        this._ac.after(SELECT, this._handleSelect, this);

        // disable suggestion list after lane search submitted
        L.on("search:search", function(){
            input.ac.destroy();
        });
    };

    Suggest.prototype = {

        /**
         * Sets the active item to the mouseovered one.
         * @method _handleHoveredItemChanage
         * @param event {CustomEvent} the hoveredItemChange event
         * @private
         */
        _handleHoveredItemChange : function(event) {
            if (event.newVal) {
                this._ac.set("activeItem", event.newVal);
            }
        },

        /**
         * Fires the select event when an autocomplete item is selected
         * @method _handleSelect
         * @param event {CustomEvent} the autocomplete select event
         * @private
         */
        _handleSelect : function(event) {
            this.fire(SELECT, {
                suggestion : event.result.text,
                input : this._input
            });
        },

        /**
         * Set the limit parameter for the request, setting it to the default if the value is empty.
         * @method setLimit
         * @param limit {String} the limit parameter
         */
        setLimit : function(limit) {
            this._ac.set("source", SOURCE_BASE + (limit || DEFAULT_LIMIT));
        }
    };

    //Add EventTarget attributes to the Suggest prototype
    L.addEventTarget(Suggest, {
        emitFacade : true,
        prefix     : 'suggest'
    });

    //make the Suggest constructor globally accessible
    L.Suggest = Suggest;
})();

(function() {

    "use strict";

    // hybrid search page inputs
    var laneSuggest,
        hybridInput = document.querySelector('.laneSuggest');
    if (hybridInput) {
        laneSuggest = new L.Suggest(new Y.Node(hybridInput));
        laneSuggest.on("select",function(){
            L.searchIndicator.show();
            L.ancestor(hybridInput, "form").submit();
        });
    }
})();
