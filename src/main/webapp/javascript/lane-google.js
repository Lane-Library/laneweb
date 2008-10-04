YAHOO.util.Event.addListener(this, 'load', function() {
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    YAHOO.util.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function(){
            var host = document.location.host, pageTracker;
            if (_gat !== undefined) {
                if ("lane.stanford.edu" == host) {
                    pageTracker = _gat._getTracker("UA-3202241-2");
                } else {
                    pageTracker = _gat._getTracker("UA-3203486-2");
                }
                //TODO: remove this for production
                //pageTracker._setLocalServerMode();
                //TODO:
                pageTracker._initData();
                pageTracker._trackPageview();
                pageTracker._setVar(LANE.core.getMetaContent('WT.seg_1'));
                LANE.track.addTracker({
                    track: function(trackable){
                        var t = trackable;
                        if (t.external) {
                            pageTracker._trackPageview('/OFFSITE/' + t.title);
                        } else {
                            pageTracker._trackPageview('/ONSITE/' + t.title + '/' + t.path);
                        }
                    }
                });
            }
        }
    });
});
