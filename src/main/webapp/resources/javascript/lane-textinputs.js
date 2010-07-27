/**
 * candy for input[type="text"]
 *  assumes uses title and value attributes used for help text
 *     applies hintStyle only to default help text
 *  clears default help text on focus and adds back on blur
 */
(function() {
    
    LANE.TextInput = function(input, hintText) {
        var hintStyle = "inputHint",
            _hintText = hintText || '',
            _reset = function(input) {
                input.addClass(hintStyle);
                input.set('value', _hintText);
            },
            focusHandle = input.on('focus', function() {
                if (this.get('value') == _hintText) {
                    this.set('value', '');
                    this.removeClass(hintStyle);
                }
            }),
            blurHandle = input.on('blur', function() {
                if (this.get('value') === '') {
                    _reset(this);
                }
            });
        if (input.get('value') === '' || input.get('value') == _hintText) {
            _reset(input);
        }
        return {
            getValue: function() {
                var value = input.get('value');
                return value == _hintText ? '' : value; 
            },
            setValue: function(value) {
                input.set('value', value);
                input.removeClass("inputHint");
            },
            getInput: function() {
                return input;
            },
            setHintText: function(hintText) {
                var oldHintText = _hintText;
                _hintText = hintText;
                if (input.get('value') === '' || input.get('value') == oldHintText) {
                    _reset(input);
                }
            },
            destroy: function() {
                input.detach(focusHandle);
                input.detach(blurHandle);
                input.removeClass('inputHint');
                if (input.get('value') == _hintText) {
                    input.set('value', '');
                }
            }
        };
    };

    var Y = LANE.Y, i, title, textInputs = new Y.all('input[type="text"]');
    
    for (i = 0; i < textInputs.size(); i++) {
        title = textInputs.item(i).get('title');
        if (title) {
            new LANE.TextInput(textInputs.item(i), title);
        }
    }
    
})();
