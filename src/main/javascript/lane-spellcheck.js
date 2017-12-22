(function() {

    "use strict";

    var spellCheck = Y.one('#spellCheck'),
        model = Y.lane.Model,
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "";
    if (spellCheck && encodedQuery) {
        //get the suggestion
        Y.io(basePath + '/apps/spellcheck/json?q=' + encodedQuery, {
            on: {
                success:function(id, o) {
                    var sc = JSON.parse(o.responseText), a, correctedUrl;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup
                        //and add the suggestion to the href
                        correctedUrl = Y.lane.Location.get("href").replace('q=' + encodedQuery, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + encodedQuery);
                        //strip #facet stuff from URL
                        correctedUrl = correctedUrl.replace(/#.*/,'');
                        // if the suggestion is long, set parent container height;
                        // this creates UI bounce, so only do it as needed
                        if (sc.suggestion.length > 110) {
                            spellCheck.ancestor(".popin").setStyle("height","100%");
                        }
                        a = spellCheck.one('a');
                        a.set('href', correctedUrl);
                        a.set('innerHTML', sc.suggestion);
                        Y.lane.fire('lane:popin', "spellCheck");
                        // track suggestion and original query
                        Y.lane.fire("tracker:trackableEvent", {
                            category: "lane:spellSuggest",
                            action: "query=" + decodeURIComponent(encodedQuery),
                            label: "suggestion=" + sc.suggestion
                        });
                    }
                }
            }
        });
    }
})();

