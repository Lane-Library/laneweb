// teletype functionality for PubMed/UTD through Lane pages
// typing in #searchTeletyper #teletypeInput sends keystrokes to #search #searchTerms
YAHOO.util.Event.onContentReady('searchTeletyper', function(){
	var targetElm = document.getElementById('searchTerms'),
	inputElm = document.getElementById('teletypeInput'),
	searchForm = document.getElementById('search'),
	teletype = function(inputElm){
        var qString;
        if (!targetElm){
        	return false;
        }
	    if (inputElm.value != inputElm.title) {
	        qString = inputElm.value;
	    }
	    if (!qString){
	    	qString = targetElm.title;
	    }
	    targetElm.value = qString;
    }
	// send #searchTeletyper submit events to #search
	YAHOO.util.Event.addListener(this, 'submit', function(e){
		searchForm.submit();
		YAHOO.util.Event.preventDefault(e);
	});
	// clear #teletypeInput input if it matches title (help text) value
	YAHOO.util.Event.addListener(inputElm, 'focus', function(){
        if (this.value == this.title){
            this.value = '';
        }
	});
	YAHOO.util.Event.addListener(inputElm, 'keyup', function(){
        teletype(this);
    });
	// if #teletypeInput input value is blank, set to title (help text)
	YAHOO.util.Event.addListener(inputElm, 'blur', function(){
        if (this.value === ''){
            this.value = this.title;
        }
    	teletype(this);
    });
	
});
