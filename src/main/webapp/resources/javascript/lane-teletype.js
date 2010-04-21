// teletype functionality for PubMed/UTD through Lane pages
// typing in #searchTeletyper .teletypeInput sends keystrokes to #search #searchTerms
YUI().use('yui2-event','yui2-dom', function(Y) {
Y.YUI2.util.Event.onContentReady('searchTeletyper', function(){
    var targetElm = document.getElementById('searchTerms'),
    inputElms = Y.YUI2.util.Dom.getElementsByClassName('teletypeInput',null,this),
    searchForm = document.getElementById('search'),
    YE = Y.YUI2.util.Event,
    i,
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
    YE.addListener(this, 'submit', function(e){
        searchForm.submit();
        YE.preventDefault(e);
    });
    
    for (i = 0; i < inputElms.length; i++){
        // clear #teletypeInput input if it matches title (help text) value
        YE.addListener(inputElms[i], 'focus', function(){
            if (this.value == this.title){
                this.value = '';
            }
        });
        YE.addListener(inputElms[i], 'keyup', function(){
            teletype(this);
        });
        // if #teletypeInput input value is blank, set to title (help text)
        YE.addListener(inputElms[i], 'blur', function(){
            if (this.value === ''){
                this.value = this.title;
            }
            teletype(this);
        });
    }
});
});
