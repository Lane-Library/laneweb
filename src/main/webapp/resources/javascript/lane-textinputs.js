/**
 * candy for input[type="text"]
 *  assumes uses title and value attributes used for help text
 * 	applies hintStyle only to default help text
 *  clears default help text on focus and adds back on blur
 */
YUI().use('event-base','node-base',function(Y) {
    Y.on("domready", function() {
    	var textInputs, i,
    	hintStyle = 'inputHint';
    	
        textInputs = new Y.all('input[type="text"]');
        
        // default to hintStyle when value and title are same
        for (i = 0; i < textInputs.size(); i++){
        	if (textInputs.item(i).get('value') == textInputs.item(i).get('title')){
        		textInputs.item(i).addClass(hintStyle);
        	}
        }
        // if input value is blank, set to title (help text)
        textInputs.on('blur', function(e){
    	    if (e.currentTarget.get('value') === ''){
    	    	e.currentTarget.set('value',e.currentTarget.get('title'));
    	    	e.currentTarget.addClass(hintStyle);
    	    }
    	});
    	// clear input if it matches title (help text) value
        textInputs.on('focus', function(e){
	    	if (e.currentTarget.get('value') == e.currentTarget.get('title')){
	    		e.currentTarget.set('value','');
	    		e.currentTarget.removeClass(hintStyle);
	    	}
    	});
    	
    });
});
