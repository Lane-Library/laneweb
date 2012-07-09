(function() {
    var spellCheck = Y.one('#spellCheck'),
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
                        a.set('href', document.location.href.replace('q=' + searchTerms, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + searchTerms));
                        a.set('innerHTML', sc.suggestion);
                        Y.fire('lane:popin', spellCheck);
                        // track suggestion and original query
                        Y.fire("lane:trackableEvent", {
                            category: "lane:spellSuggest",
                            action: "query=" + LANE.SearchResult.getSearchTerms(),
                            label: "suggestion=" + sc.suggestion
                        });
                    }
                }
            }
        });
    }
})();

