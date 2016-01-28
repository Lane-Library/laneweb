/**
 * set input placeholder attribute in capable browsers
 * for browsers without placeholder functionality (IE <=9) set, clear and style input value
 */
(function() {

    "use strict";

    var HINT_CLASS = "inputHint",

        hasNoPlaceholder = document.createElement("input").placeholder === undefined,

        PlaceholderTextInput = function(input, hintText) {
            this._input = input;
            input.set('placeholder', hintText);
        },

        NoPlaceholderTextInput = function(input, hintText) {
            this._input = input;
            this._hintText = hintText || "";
            this._focusHandle = this._input.on('focus', function(event) {
                if (event.target.get('value') === this._hintText) {
                    event.target.set('value', '');
                    event.target.removeClass(HINT_CLASS);
                }
            }, this);
            this._blurHandle = this._input.on('blur', function(event) {
                if (event.target.get('value') === '') {
                    this.reset(event.target);
                }
            }, this);
            if (this._input.get('value') === '' || this._input.get('value') === this._hintText) {
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

        destroy: function() {
            this._input.detach(this._focusHandle);
            this._input.detach(this._blurHandle);
            this._input.removeClass(HINT_CLASS);
            if (this._input.get('value') === this._hintText) {
                this._input.set('value', '');
            }
        },

        getValue: function() {
            var value = this._input.get('value');
            return value === this._hintText ? '' : value;
        },

        reset: function() {
            this._input.addClass(HINT_CLASS);
            this._input.set('value', this._hintText);
        },

        setHintText: function(hintText) {
            var oldHintText = this._hintText;
            this._hintText = hintText;
            if (this._input.get('value') === '' || this._input.get('value') === oldHintText) {
                this.reset(this._input);
            }
        },

        setValue: function(value) {
            this._input.set('value', value);
            this._input.removeClass(HINT_CLASS);
        }
    };

    PlaceholderTextInput.prototype = {

        destroy: function() {
            this._input.set('placeholder','');
        },

        getValue: function() {
            return this._input.get('value');
        },

        reset: function() {
            this._input.set("value", "");
        },

        setHintText: function(hintText) {
            this._input.set('placeholder', hintText);
        },

        setValue: function(value) {
            this._input.set('value', value);
        }
    };

    Y.all('input[type=text][title]').each(function(input) {
        (new TextInput(input, input.get("title")));
    });

    Y.lane.TextInput = TextInput;

})();
