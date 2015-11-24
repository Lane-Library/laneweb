
(function() {

    var Lane = Y.lane,
        model = Lane.Model,
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
            source: SOURCE_BASE + (limit || DEFAULT_LIMIT)
        });

        /**
         * @event select
         * @description fired when a suggestion is selected
         */
        this.publish(SELECT,{
            broadcast : 1,
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

        //pico inputs don't have width when the Suggest is constructed
        if (!this._ac.get("width")) {
            this._visibleHandle = this._ac.on("visibleChange", this._handleVisibleChange, this);
        }

        // disable suggestion list after lane search submitted
        Lane.on("search:submit", function(){
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
         * Set the autocomplete width if necessary. Pico inputs don't have width
         * when the Suggest is constructed.
         * @method _handleVisibleChange
         * @param event {CustomEvent} the visibleChange event
         * @private
         */
        _handleVisibleChange : function(event) {
            if (event.newVal) {
                this._ac.set("width", this._ac.get("inputNode").get("offsetWidth"));
                this._visibleHandle.detach();
                this._visibleHandle = null;
            }
        },

        /**
         * Set the limit parameter for the request.  If it is history disable ac by setting
         * minQueryLength to -1, otherwise setting it to the default.
         * @method setLimit
         * @param limit {String} the limit parameter
         */
        setLimit : function(limit) {
            this._ac.set("source", SOURCE_BASE + (limit || DEFAULT_LIMIT));
            this._ac.set("minQueryLength", limit === "history" ? -1 : 3);
        }
    };

    //Add EventTarget attributes to the Suggest prototype
    Y.augment(Suggest, Y.EventTarget, null, null, {
        emitFacade : true,
        prefix     : 'suggest'
    });

    //make the Suggest constructor globally accessible
    Lane.Suggest = Suggest;
})();

(function() {

    // hybrid search page inputs
    var laneSuggest, hybridInput = Y.one('.laneSuggest');
    if (hybridInput) {
        laneSuggest = new Y.lane.Suggest(hybridInput);
        laneSuggest.on("select",function(){
            Y.lane.SearchIndicator.show();
            hybridInput.ancestor("form").submit();
        });
    }
})();