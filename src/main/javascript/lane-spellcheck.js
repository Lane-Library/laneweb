//check if there is a query
if (LANE.search.getEncodedSearchString()) {
    //wait until id=spellcheck available
    YAHOO.util.Event.onAvailable('spellCheck',function() {
        //get the suggestion
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q=' + LANE.search.getEncodedSearchString(), {
            success: function(o){
                var sc = YAHOO.lang.JSON.parse(o.responseText), s, a;
                if (sc.suggestion) {
                    //if there is a suggestion show the spellcheck markup 
                    //and add the suggestion to the href
                    s = document.getElementById('spellCheck');
                    s.style.display = 'inline';
                    a = s.getElementsByTagName('a')[0];
                    a.href += '?source=' + LANE.search.getSearchSource() + '&q=' + encodeURIComponent(sc.suggestion);
                    //TODO safari doesn't do textContent?
                    a.textContent = sc.suggestion;
                }
            //failure:don't do anything
            }
        });
    });
}


