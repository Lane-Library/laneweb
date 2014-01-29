(function() {
    var spellCheck = Y.one('#spellCheck'),
        model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "";
    if (spellCheck && query) {
        //get the suggestion
        Y.io(basePath + '/apps/spellcheck/json?q=' + encodedQuery, {
            on: {
                success:function(id, o) {
                    var sc = Y.JSON.parse(o.responseText), a, correctedUrl;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup
                        //and add the suggestion to the href
                        correctedUrl = Y.lane.Location.get("href").replace('q=' + encodedQuery, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + encodedQuery);
                        //strip #facet stuff from URL (#facet=catalog-lois added when no hits)
                        correctedUrl = correctedUrl.replace(/#.*/,'');
                        a = spellCheck.one('a');
                        a.set('href', correctedUrl);
                        a.set('innerHTML', sc.suggestion);
                        Y.fire('lane:popin', spellCheck);
                        // track suggestion and original query
                        Y.lane.fire("tracker:trackableEvent", {
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

