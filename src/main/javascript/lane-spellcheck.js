(function () {

    "use strict";

    let spellCheck = document.querySelector('.spellCheck'),
        model = L.Model,
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "";
    if (spellCheck && encodedQuery) {
        //get the suggestion
        fetch(basePath + '/apps/spellcheck/json?q=' + encodedQuery)
            .then(response => response.json())
            .then(json => {
                let sc = json, a, correctedUrl;
                if (sc.suggestion) {
                    //if there is a suggestion show the spellcheck markup
                    //and add the suggestion to the href
                    correctedUrl = location.href.replace('q=' + encodedQuery, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + encodedQuery);
                    //strip #facet stuff from URL
                    correctedUrl = correctedUrl.replace(/#.*/, '');
                    a = spellCheck.querySelector('a');
                    a.href = correctedUrl;
                    a.innerHTML = sc.suggestion;
                    spellCheck.style.visibility = "visible";
                    // track suggestion and original query
                    L.fire("tracker:trackableEvent", {
                        category: "lane:spellSuggest",
                        action: "query=" + decodeURIComponent(encodedQuery),
                        label: "suggestion=" + sc.suggestion
                    });
                }

            })
            .catch(error => {
                console.error('Error:', error);
            });
    }


})();

