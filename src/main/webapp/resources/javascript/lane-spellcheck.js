(function() {
    var spellCheck = Y.one('#spellCheck'),
        searchTerms = Y.lane.SearchResult.getSearchTerms(location.search),
        basePath = Y.lane.Model.get("base-path") || "";
    if (spellCheck && searchTerms) {
        //get the suggestion
        Y.io(basePath + '/apps/spellcheck/json?q=' + encodeURIComponent(searchTerms), {
            on: {
                success:function(id, o) {
                    var sc = Y.JSON.parse(o.responseText), a;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup 
                        //and add the suggestion to the href
                        a = spellCheck.one('a');
                        a.set('href', document.location.href.replace('q=' + encodeURIComponent(searchTerms), 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + searchTerms));
                        a.set('innerHTML', sc.suggestion);
                        Y.fire('lane:popin', spellCheck);
                        // track suggestion and original query
                        Y.fire("lane:trackableEvent", {
                            category: "lane:spellSuggest",
                            action: "query=" + searchTerms,
                            label: "suggestion=" + sc.suggestion
                        });
                    }
                }
            }
        });
    }
})();

