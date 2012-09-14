(function() {
    var findItNode = Y.one('#findIt'),
        searchTerms = Y.lane.SearchResult.getSearchTerms(),
        basePath = Y.lane.Model.get("base-path") || "",
        url;
    if (findItNode && searchTerms) {
        url = basePath + '/apps/sfx/json?q=' + encodeURIComponent(searchTerms);
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
                            action: "query=" + searchTerms,
                            label: "result=" + findIt.result
                        });
                    }
                }
            }
        });
    }
})();
