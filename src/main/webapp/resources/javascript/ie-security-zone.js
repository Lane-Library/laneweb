(function() {
    /*
     * PersistentLoginController appends pca=true|false|renew when cookies are set via an SSL request. 
     * When this parameter is present, the user should be authenticated or the user cookie should be present. 
     * Report when Internet Explorer + pca is present +  user is either unauthenticated or user cookie is not missing.
     * Detect and report for now ... may alert and instruct the user.
     */
    if(Y.UA.ie){
        var userId = LANE.tracking.getUserId(), queryString = document.location.search, errorCategory = null, f;
        if(queryString.match(/pca=/) && null == userId){
            errorCategory = "no-auth";
        }
        else if(queryString.match(/pca=(true|renew)/) && !Y.Cookie.get("user")){
            errorCategory = "no-user-cookie";
        }
        if(errorCategory) {
            f = function(){
                Y.fire("lane:trackableEvent", {
                    category: "ie-security-zone-error:" + errorCategory,
                    action: document.location.href,
                    label: Y.UA.userAgent
                });
            };
            setTimeout(f, 500);
        }
    }
})();

