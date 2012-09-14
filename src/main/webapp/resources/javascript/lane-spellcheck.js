(function() {
    var spellCheck = Y.one('#spellCheck'),
        model = Y.lane.Model,
        query = model.get("query"),
        encodedQuery = model.get("url-encoded-query"),
        basePath = model.get("base-path") || "";
    if (spellCheck && query) {
        //get the suggestion
        Y.io(basePath + '/apps/spellcheck/json?q=' + encodedQuery, {
            on: {
                success:function(id, o) {
                    var sc = Y.JSON.parse(o.responseText), a;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup 
                        //and add the suggestion to the href
                        a = spellCheck.one('a');
                        a.set('href', document.location.href.replace('q=' + encodedQuery, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + encodedQuery));
                        a.set('innerHTML', sc.suggestion);
                        Y.fire('lane:popin', spellCheck);
                        // track suggestion and original query
                        Y.fire("lane:trackableEvent", {
                            category: "lane:spellSuggest",
                            action: "query=" + query,
                            label: "suggestion=" + sc.suggestion
                        });
                    }
                }
            }
        });
    }
})();

