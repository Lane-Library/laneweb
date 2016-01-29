/**
 * set input placeholder attribute in capable browsers
 * for browsers without placeholder functionality (IE <=9) set, clear and style input value
 */
(function() {

    "use strict";

    var EMPTY = "",
        HINT_CLASS = "inputHint",
        PLACEHOLDER = "placeholder",
        VALUE = "value",

        hasNoPlaceholder = document.createElement("input").placeholder === undefined,

        PlaceholderTextInput = function(input, hintText) {
            this._input = input;
            input.set(PLACEHOLDER, hintText);
        },

        NoPlaceholderTextInput = function(input, hintText) {
            this._input = input;
            this._hintText = hintText || EMPTY;
            this._focusHandle = this._input.on('focus', this._focusHandler, this);
            this._blurHandle = this._input.on('blur', this._blurHandler, this);
            if (this._input.get(VALUE) === EMPTY || this._input.get(VALUE) === this._hintText) {
                this.reset();
            }
        },

        TextInput = function(input, hintText, noPlaceholder) {
            if (hasNoPlaceholder || noPlaceholder) {
                return new NoPlaceholderTextInput(input, hintText);
            } else {
                return new PlaceholderTextInput(input, hintText);
            }
        };

    NoPlaceholderTextInput.prototype = {

        _focusHandler: function(event) {
            if (event.target.get(VALUE) === this._hintText) {
                event.target.set(VALUE, EMPTY);
                event.target.removeClass(HINT_CLASS);
            }
        },

        _blurHandler: function(event) {
            if (event.target.get(VALUE) === EMPTY) {
                this.reset();
            }
        },

        destroy: function() {
            this._input.detach(this._focusHandle);
            this._input.detach(this._blurHandle);
            this._input.removeClass(HINT_CLASS);
            if (this._input.get(VALUE) === this._hintText) {
                this._input.set(VALUE, EMPTY);
            }
        },

        getValue: function() {
            var value = this._input.get(VALUE);
            return value === this._hintText ? EMPTY : value;
        },

        reset: function() {
            this._input.addClass(HINT_CLASS);
            this._input.set(VALUE, this._hintText);
        },

        setHintText: function(hintText) {
            var oldHintText = this._hintText;
            this._hintText = hintText;
            if (this._input._node !== document.activeElement && (this._input.get(VALUE) === EMPTY || this._input.get(VALUE) === oldHintText)) {
                this.reset();
            }
        },

        setValue: function(value) {
            if (!value && this._input._node !== document.activeElement) {
                this.reset();
            } else {
                this._input.set(VALUE, value);
                this._input.removeClass(HINT_CLASS);
            }
        }
    };

    PlaceholderTextInput.prototype = {

        destroy: function() {
            this._input.set(PLACEHOLDER,EMPTY);
        },

        getValue: function() {
            return this._input.get(VALUE);
        },

        reset: function() {
            this._input.set(VALUE, EMPTY);
        },

        setHintText: function(hintText) {
            this._input.set(PLACEHOLDER, hintText);
        },

        setValue: function(value) {
            this._input.set(VALUE, value);
        }
    };

    Y.all('input[type=text][title]').each(function(input) {
        (new TextInput(input, input.get("title")));
    });

    Y.lane.TextInput = TextInput;

})();
