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
        Y.on('submit', searchForm.submit, searchTeletyper);
    }
    
    for (i = 0; i < inputElms.size(); i++){
        // clear #teletypeInput input if it matches title (help text) value
        inputElms.item(i).on('focus', function(){
            if (this.get('value') == this.get('title')){
                this.set('value', '');
            }
        });
        inputElms.item(i).on( 'keyup', function(){
            teletype(this);
        });
        // if #teletypeInput input value is blank, set to title (help text)
        inputElms.item(i).on( 'blur', function(){
            if (this.get('value') === ''){
                this.set('value', this.get('title'));
            }
            teletype(this);
        });
    }
});
