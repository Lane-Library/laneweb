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

        inputs = {},

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
            var textInput = inputs[input._yuid];
            if (!textInput) {
                if (hasNoPlaceholder || noPlaceholder) {
                    textInput = new NoPlaceholderTextInput(input, hintText);
                } else {
                    textInput = new PlaceholderTextInput(input, hintText);
                }
                inputs[input._yuid] = textInput;
            } else {
                textInput.setHintText(hintText);
            }
            return textInput;
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
            delete inputs[this._input._yuid];
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
            delete inputs[this._input._yuid];
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
