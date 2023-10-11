
(function() {

    "use strict";

    var model = L.Model,
        basePath = model.get(model.BASE_PATH) || "",
    DEFAULT_SOURCE_BASE = basePath + "/apps/suggest/getSuggestionList?q={query}&l=",
    DEFAULT_LIMIT = "mesh",
    DEFAULT_QUERY_LENGTH = 3,
    SELECT = "select",

    /**
     * A class that provides an autocomplete widget for inputs.
     * @class Suggest
     * @type {Object}
     * @requires EventTarget
	 * @constructor
     * @param input {Node} the input node.
     * @param limit {String} the limit parameter for the request.
	 * @param sourceBase {String} .
     */
    Suggest = function(input,  minQueryLength, sourceBase) {
        var yuiinput = input._node ? input : new Y.Node(input);
        yuiinput.plug(Y.Plugin.AutoComplete, {
            minQueryLength: minQueryLength ? minQueryLength : DEFAULT_QUERY_LENGTH,
            source: sourceBase ? sourceBase :(  DEFAULT_SOURCE_BASE + DEFAULT_LIMIT),
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
        this._input = yuiinput;

        //save the autocomplete object
        this._ac = yuiinput.ac;

        //hoveredItemChange is fired on mouseover events in the suggestion list
        this._ac.after("hoveredItemChange", this._handleHoveredItemChange, this);

        //select is fired on clicks or return pressed in suggestion list
        this._ac.after(SELECT, this._handleSelect, this);

        // disable suggestion list after lane search submitted
        L.on("search:search", function(){
            yuiinput.ac.destroy();
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
            this._ac.set("source", DEFAULT_SOURCE_BASE + (limit || DEFAULT_LIMIT));
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
