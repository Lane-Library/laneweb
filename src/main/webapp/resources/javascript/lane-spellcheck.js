//check if there is a query
YUI().use('yui2-event','yui2-connection','yui2-json', function(Y) {
if (LANE.search.getEncodedSearchString()) {
    //wait until id=spellcheck available
    Y.YUI2.util.Event.onAvailable('spellCheck',function() {
        //get the suggestion
        Y.YUI2.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q=' + LANE.search.getEncodedSearchString(), {
            success: function(o){
                var sc = Y.YUI2.lang.JSON.parse(o.responseText), s, a;
                if (sc.suggestion) {
                    //if there is a suggestion show the spellcheck markup 
                    //and add the suggestion to the href
                    s = document.getElementById('spellCheck');
                    //s.style.display = 'inline';
                    LANE.search.popin.fire(s);
                    a = s.getElementsByTagName('a')[0];
                    a.href = document.location.href.replace('q='+LANE.search.getEncodedSearchString(),'q='+encodeURIComponent(sc.suggestion));
                    a.innerHTML = sc.suggestion;
                }
            //failure:don't do anything
            }
        });
    });
}
});


