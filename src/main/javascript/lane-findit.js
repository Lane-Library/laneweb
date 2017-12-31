(function() {

    "use strict";

    var findItNode = document.querySelector('#findIt'),
        model = L.Model,
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        url;
    // SFX responds very slowly to numeric requests (PMIDs)
    if (findItNode && encodedQuery && isNaN(encodedQuery)) {
        url = basePath + '/apps/sfx/json?q=' + encodedQuery;
        Y.io(url, {
            on: {
                success: function(id, o) {
                    var findIt = JSON.parse(o.responseText),
                        findItLink;
                    if (findIt.result) {
                        findItLink = findItNode.querySelector('a');
                        findItLink.href = findIt.openurl;
                        findItLink.innerHTML = findIt.result;
                        L.fire('lane:popin', "findIt");
                        // tracking
                        L.fire("tracker:trackableEvent", {
                            category: "lane:findit",
                            action: "query=" + decodeURIComponent(encodedQuery),
                            label: "result=" + findIt.result
                        });
                    }
                }
            }
        });
    }
})();
