 //check if there is a query
if (LANE.search.getEncodedSearchString()) {
    //wait until id=findIt available
    YAHOO.util.Event.onAvailable('findIt',function() {
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/sfx/json?q=' + LANE.search.getEncodedSearchString(), {
            success:function(o) {
                var findIt = YAHOO.lang.JSON.parse(o.responseText), findItLink, findItContainer;
                if (findIt.result) {
                    findItContainer = document.getElementById('findIt');
                    findItLink = findItContainer.getElementsByTagName('a')[0];
                    findItLink.href = findIt.openurl;
                    findItLink.innerHTML = findIt.result;
                    findItContainer.style.display = 'inline';
                }
            }
        });
    });
}
    