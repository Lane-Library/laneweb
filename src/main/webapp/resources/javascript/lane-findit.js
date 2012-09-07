(function() {
    var findItNode = Y.one('#findIt'),
        searchString = LANE.SearchResult.getEncodedSearchTerms(),
        basePath = Y.lane.Model.get("base-path") || "",
        url;
    if (findItNode && searchString) {
        url = basePath + '/apps/sfx/json?q=' + searchString;
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
                            action: "query=" + LANE.SearchResult.getSearchTerms(),
                            label: "result=" + findIt.result
                        });
                    }
                }
            }
        });
    }
})();
