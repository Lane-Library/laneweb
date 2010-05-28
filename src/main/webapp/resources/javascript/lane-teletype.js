// teletype functionality for PubMed/UTD through Lane pages
// typing in #searchTeletyper .teletypeInput sends keystrokes to #search #searchTerms
YUI().use('node', function(Y) {
    var targetElm = Y.one('#searchTerms'),
        inputElms = Y.all('.teletypeInput'),
        searchForm = Y.one('#search'),
        searchTeletyper = Y.one('#searchTeletyper'),
        i,
        teletype = function(inputElm){
            var qString;
            if (!targetElm){
                return false;
            }
            if (inputElm.get('value') != inputElm.get('title')) {
                qString = inputElm.get('value');
            }
            if (!qString){
                qString = targetElm.get('title');
            }
            targetElm.set('value', qString);
        };

        // send #searchTeletyper submit events to #search
        if (searchTeletyper) {
	        searchTeletyper.on("submit",function(e){
	        	e.halt();
	        	searchForm.submit();
	        });
        }
    
    for (i = 0; i < inputElms.size(); i++){
        inputElms.item(i).on( 'keyup', function(){
            teletype(this);
        });
        inputElms.item(i).on( 'blur', function(){
            teletype(this);
        });
    }
});
