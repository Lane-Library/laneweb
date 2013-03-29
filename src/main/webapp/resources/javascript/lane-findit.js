(function() {
    var findItNode = Y.one('#findIt'),
        model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        url;
    if (findItNode && query) {
        url = basePath + '/apps/sfx/json?q=' + encodedQuery;
        Y.io(url, {
            on: {
                success: function(id, o) {
                    var findIt = Y.JSON.parse(o.responseText),
                        findItLink;
                    if (findIt.result) {
                        findItLink = findItNode.one('a');
                        findItLink.set('href', findIt.openurl);
                        findItLink.set('innerHTML', findIt.result);
                        Y.fire('lane:popin', findItNode);
                        // tracking
                        Y.fire("lane:trackableEvent", {
                            category: "lane:findit",
                            action: "query=" + query,
                            label: "result=" + findIt.result
                        });
                    }
                }
            }
        });
    }
})();
