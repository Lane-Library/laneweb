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
                    track: function(trackingData){
                        if (trackingData.external) {
                            pageTracker._trackPageview('/OFFSITE/' + trackingData.title);
                        } else {
                            if (trackingData.path.indexOf('/secure/login') === 0) {
                                var msg = 'useragent:' + navigator.userAgent;
                                msg += ';host:' + document.location.toString();
                                msg += ';title:' + trackingData.title;
                                msg += ';path:' + trackingData.path;
                                LANE.core.log(msg);
                            }
                            pageTracker._trackPageview('/ONSITE/' + trackingData.title + '/' + trackingData.path);
                        }
                    }
                });
            }
        }
    });
});
