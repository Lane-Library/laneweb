(function() {
    /*
     * PersistentLoginController appends scs=1 (secure cookie set) when cookies are set via an SSL request. 
     * When this parameter is present, the user should be authenticated. Check and report when Internet Explorer
     * is the browser, scs is present in the request, and the user is NOT authenticated.
     * Detect and report for now ... may alert and instruct the user.
     */
    if(Y.UA.ie){
        var userId = LANE.tracking.getUserId(),f;
        if(document.location.search.match(/scs=1/) && null == userId){
            f = function(){
                Y.fire("lane:trackableEvent", {
                    category: "ie-security-zone-error",
                    action: document.location.href,
                    label: Y.UA.userAgent
                });
            };
            setTimeout(f, 500);
        }
    }
})();

