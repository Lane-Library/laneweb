/**
 * candy for input[type="text"]
 *  assumes uses title and value attributes used for help text
 * 	applies hintStyle only to default help text
 *  clears default help text on focus and adds back on blur
 */
YUI().use('yui2-event','node',function(Y) {
    var Event = Y.YUI2.util.Event; //shorthand for Event
    Event.onDOMReady(function() {
    	var textInputs, i,
    	hintStyle = 'inputHint';
        textInputs = new Y.all('input[type="text"]');
	    for (i = 0; i < textInputs.size(); i++){
	    	// default style
    	    if (textInputs.item(i).get('value') == textInputs.item(i).get('title')){
    	    	textInputs.item(i).addClass(hintStyle);
    	    }
	    	// clear input if it matches title (help text) value
			Event.addListener(Y.Node.getDOMNode(textInputs.item(i)), 'focus', function(){
	    	    if (this.value == this.title){
	    	        this.value = '';
	    	        Y.one(this).removeClass(hintStyle);
	    	    }
	    	});
	    	// if input value is blank, set to title (help text)
	    	Event.addListener(Y.Node.getDOMNode(textInputs.item(i)), 'blur', function(){
	    	    if (this.value === ''){
	    	        this.value = this.title;
	    	        Y.one(this).addClass(hintStyle);
	    	    }
	    	});
	    }
    });
});
