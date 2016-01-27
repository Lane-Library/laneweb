/**
 * set input placeholder attribute in capable browsers
 * for browsers without placeholder functionality (IE <=9) set, clear and style input value
 */
(function() {

    "use strict"

    Y.lane.TextInput = function(input, hintText) {
        var hintStyle = "inputHint",
            _hintText = hintText || '',
            placeholderCapable = 'placeholder' in Y.Node.getDOMNode(input),
            _destroy =function() {
                input.set('placeholder','');
            },
            _getValue = function() {
                return input.get('value');
            },
            _reset = function(input) {
                input.set('value', '');
            },
            _setHintText = function(hintText) {
                input.set('placeholder',hintText);
            },
            _setValue = function(value) {
                input.set('value', value);
            };
            input.set('placeholder',_hintText);
            if (!placeholderCapable) {
                _destroy =function() {
                    input.detach(focusHandle);
                    input.detach(blurHandle);
                    input.removeClass('inputHint');
                    if (input.get('value') === _hintText) {
                        input.set('value', '');
                    }
                };
                _getValue = function() {
                    var value = input.get('value');
                    return value === _hintText ? '' : value;
                };
                _reset = function(input) {
                    input.addClass(hintStyle);
                    input.set('value', _hintText);
                };
                _setHintText = function(hintText) {
                    var oldHintText = _hintText;
                    _hintText = hintText;
                    if (input.get('value') === '' || input.get('value') === oldHintText) {
                        _reset(input);
                    }
                };
                _setValue = function(value) {
                    input.set('value', value);
                    input.removeClass("inputHint");
                };
                focusHandle = input.on('focus', function(event) {
                    if (event.target.get('value') === _hintText) {
                        event.target.set('value', '');
                        event.target.removeClass(hintStyle);
                    }
                });
                blurHandle = input.on('blur', function(event) {
                    if (event.target.get('value') === '') {
                        _reset(event.target);
                    }
                });
                if (input.get('value') === '' || input.get('value') === _hintText) {
                    _reset(input);
                }
            }
        return {
            destroy: function() {
                _destroy();
            },
            getValue: function() {
                return _getValue();
            },
            getInput: function() {
                return input;
            },
            //added reset function so BookmarkEditor can reset
            reset : function() {
                _reset(input);
            },
            setHintText: function(hintText) {
                _setHintText(hintText);
            },
            setValue: function(value) {
                _setValue(value);
            }
        };
    };

    var i, title, textInputs = new Y.all('input[type="text"]');

    for (i = 0; i < textInputs.size(); i++) {
        title = textInputs.item(i).get('title');
        if (title) {
            (new Y.lane.TextInput(textInputs.item(i), title));
        }
    }

})();
