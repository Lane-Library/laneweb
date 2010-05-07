/**
 * candy for input[type="text"]
 *  assumes uses title and value attributes used for help text
 * 	applies hintStyle only to default help text
 *  clears default help text on focus and adds back on blur
 */
YUI().add('lane-textinputs', function(Y) {
    
    var hintStyle = 'inputHint';
    
    LANE.TextInput = LANE.TextInput || function(input) {
        if (input.get('value') == input.get('title')) {
            input.setStyle(hintStyle);
        }
        input.on('focus', function() {
            if (this.get('value') == this.get('title')) {
                this.set('value', '');
                this.removeClass(hintStyle);
            }
        });
        input.on('blur', function() {
            if (this.get('value') === '') {
                this.addClass(hintStyle);
                this.set('value', this.get('title'));
            }
        });
    }
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
}, '1.11.0-SNAPSHOT', {requires:['lane', 'event-base','node-base']});

YUI().use('lane-textinputs', function(Y) {
    
    var i, textInputs = new Y.all('input[type="text"]');
    
    for (i = 0; i < textInputs.size(); i++) {
        new LANE.TextInput(textInputs.item(i));
    }
    
});
