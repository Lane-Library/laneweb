YUI().use('event', 'node', 'yui2-connection', 'yui2-json', function(Y) {
    Y.Global.on('lane:searchready', function() {
    if (LANE.search.getEncodedSearchString()) {
        //wait until id=findIt available
        var getFindit = function() {
            Y.YUI2.util.Connect.asyncRequest('GET', '/././apps/sfx/json?q=' + LANE.search.getEncodedSearchString(), {
                success: function(o) {
                    var findIt = Y.YUI2.lang.JSON.parse(o.responseText), findItLink, findItContainer;
                    if (findIt.result) {
                        findItContainer = Y.one('#findIt');
                        findItLink = findItContainer.one('a');
                        findItLink.set('href', findIt.openurl);
                        findItLink.set('innerHTML', findIt.result);
                        //findItContainer.style.display = 'inline';
                        LANE.search.popin.fire(findItContainer);
                    }
                }
            });
        };
        Y.on('available', getFindit, 'findIt');
    }
});
});
