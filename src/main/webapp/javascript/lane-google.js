YAHOO.util.Event.addListener(this, 'load', function() {
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    YAHOO.util.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function(){
            var host = document.location.host, 
            pageTracker,
            encode = function(value){
                if (encodeURIComponent) {
                    return encodeURIComponent(value);
                }
                return escape(value);
            };
            if (_gat !== undefined) {
                if ("lane.stanford.edu" == host) {
                    pageTracker = _gat._getTracker("UA-3202241-2");
                } else {
                    pageTracker = _gat._getTracker("UA-3203486-2");
                }
                //TODO: remove this for production
                //pageTracker._setLocalServerMode();
                pageTracker._setDomainName(".stanford.edu");
                pageTracker._setAllowHash(false);
                pageTracker._trackPageview();
                pageTracker._setVar(LANE.core.getMetaContent('WT.seg_1'));
                LANE.tracking.addTracker({
                    track: function(trackingData){
                        if (trackingData.external) {
                            pageTracker._trackPageview('/OFFSITE/' + encode(trackingData.title));
                        } else {
                            pageTracker._trackPageview('/ONSITE/' + encode(trackingData.title) + '/' + trackingData.path);
                        }
                    }
                });
            }
        }
    });
});
