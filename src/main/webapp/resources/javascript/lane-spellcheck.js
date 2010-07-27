(function() {
    var Y = LANE.Y,
        spellCheck = Y.one('#spellCheck'),
        searchTerms = LANE.SearchResult.getEncodedSearchTerms();
    if (spellCheck && searchTerms) {
        //get the suggestion
        Y.io('/././apps/spellcheck/json?q=' + searchTerms, {
            on: {
                success:function(id, o) {
                    var sc = Y.JSON.parse(o.responseText), a;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup 
                        //and add the suggestion to the href
                        a = spellCheck.one('a');
                        a.set('href', document.location.href.replace('q=' + searchTerms, 'q=' + encodeURIComponent(sc.suggestion)));
                        a.set('innerHTML', sc.suggestion);
                        Y.Global.fire('lane:popin', spellCheck);
                    }
                }
            }
        });
    }
})();

