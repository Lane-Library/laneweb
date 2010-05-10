/**
 * candy for input[type="text"]
 *  assumes uses title and value attributes used for help text
 * 	applies hintStyle only to default help text
 *  clears default help text on focus and adds back on blur
 */
YUI().add('lane-textinputs', function(Y) {
    
    Y.namespace('lane');

    var hintStyle = 'inputHint';
    
    Y.lane.TextInput = function(input, hintText) {
        var _hintText = hintText || '',
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
    //    Y.on("domready", function() {
    //    	var textInputs, i,
    //    	hintStyle = 'inputHint';
    //    	
    //        textInputs = new Y.all('input[type="text"]');
    //        
    //        // default to hintStyle when value and title are same
    //        for (i = 0; i < textInputs.size(); i++){
    //        	if (textInputs.item(i).get('value') == textInputs.item(i).get('title')){
    //        		textInputs.item(i).addClass(hintStyle);
    //        	}
    //        }
    //        // if input value is blank, set to title (help text)
    //        textInputs.on('blur', function(e){
    //    	    if (e.currentTarget.get('value') === ''){
    //    	    	e.currentTarget.set('value',e.currentTarget.get('title'));
    //    	    	e.currentTarget.addClass(hintStyle);
    //    	    }
    //    	});
    //    	// clear input if it matches title (help text) value
    //        textInputs.on('focus', function(e){
    //	    	if (e.currentTarget.get('value') == e.currentTarget.get('title')){
    //	    		e.currentTarget.set('value','');
    //	    		e.currentTarget.removeClass(hintStyle);
    //	    	}
    //    	});
    //    	
    //    });
}, '1.11.0-SNAPSHOT', {
    requires: ['lane', 'event-base', 'node-base']
});

YUI().use('lane-textinputs', function(Y) {

    var i, title, textInputs = new Y.all('input[type="text"]');
    
    for (i = 0; i < textInputs.size(); i++) {
        title = textInputs.item(i).get('title');
        if (title) {
            new Y.lane.TextInput(textInputs.item(i), title);
        }
    }
    
});
