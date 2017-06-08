(function() {

    "use strict";

    // a regular expression that matches everything that gets a dash
    var DASHABLE = /^(1|\d\d\d|1–\d\d\d|1–\d\d\d–\d\d\d|\d\d\d–\d\d\d)$/,

    /**
     * A Class that wraps inputs and dynamically adds dashes in the appropriate places for phone numbers.
     *
     * @class TelInput
     * @constructor
     * @param input {Node} should be type="tel"
     */
    TelInput = function(input) {
        this._input = input;
        input.after("keyup", this._handleKeyup, this);
    };

    TelInput.prototype = {

        /**
         * Handles keyup events in the input.  Checks the value against the regular expression and
         * adds a dash to the end of the value if appropriate.
         * @method _handleKeyup
         * @private
         */
        _handleKeyup : function() {
            var value = this._input.get("value");
            if(DASHABLE.test(value)) {
                this._input.set("value", value + "–");
            }
        }
    };

    // create a TelInput for each input of type tel.
    Y.all("input[type='tel']").each(function(input) {
        (new TelInput(input));
    });

    // make the constructor globally available
    Y.lane.TelInput = TelInput;

})();