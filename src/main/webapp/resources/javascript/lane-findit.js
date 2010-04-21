 //check if there is a query
 YUI().use('yui2-event','yui2-connection','yui2-json',function(Y) {
if (LANE.search.getEncodedSearchString()) {
    //wait until id=findIt available
    Y.YUI2.util.Event.onAvailable('findIt',function() {
        Y.YUI2.util.Connect.asyncRequest('GET', '/././apps/sfx/json?q=' + LANE.search.getEncodedSearchString(), {
            success:function(o) {
                var findIt = Y.YUI2.lang.JSON.parse(o.responseText), findItLink, findItContainer;
                if (findIt.result) {
                    findItContainer = document.getElementById('findIt');
                    findItLink = findItContainer.getElementsByTagName('a')[0];
                    findItLink.href = findIt.openurl;
                    findItLink.innerHTML = findIt.result;
                    //findItContainer.style.display = 'inline';
                    LANE.search.popin.fire(findItContainer);
                }
            }
        });
    });
}
});
    