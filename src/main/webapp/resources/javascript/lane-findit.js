(function() {
    var findItNode = Y.one('#findIt'),
        model = Y.lane.Model,
        query = model.get("query"),
        encodedQuery = model.get("url-encoded-query"),
        basePath = model.get("base-path") || "",
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
