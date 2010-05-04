YUI().use('lane-tracking', 'node', function(Y) {
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    Y.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function() {
            var host = document.location.host,
                i, l, meta, pageTracker;
            if (_gat !== undefined) {
                if (host.match("lane.stanford.edu")) {
                    pageTracker = _gat._getTracker("UA-3202241-2");
                } else if (host.match("lane-beta.stanford.edu")) {
                    pageTracker = _gat._getTracker("UA-3203486-9");
                } else {
                    pageTracker = _gat._getTracker("UA-3203486-2");
                }
                //uncomment this for testing/debugging:
                //pageTracker._setLocalServerMode();
                pageTracker._setDomainName(".stanford.edu");
                pageTracker._trackPageview();
                meta = Y.one('html head meta[name="WT.seg_1"]');
                if (meta) {
                    pageTracker._setVar(meta.get('content'));
                }
                LANE.tracking.addTracker({
                    track: function(trackingData) {
                        if (trackingData.external) {
                            pageTracker._trackPageview('/OFFSITE/' + encodeURIComponent(trackingData.title));
                        } else {
                            pageTracker._trackPageview('/ONSITE/' + encodeURIComponent(trackingData.title) + '/' + trackingData.path);
                        }
                    }
                });
            }
        }
    });
});
