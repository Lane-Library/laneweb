YUI().use('lane-tracking','lane-suggest','node', function(Y) {
    var gaPageTracker, gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    Y.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function() {
            var host = document.location.host,
                i, l, meta;
            if (_gat !== undefined) {
                if (host.match("lane.stanford.edu")) {
                	gaPageTracker = _gat._createTracker("UA-3202241-2","gaPageTracker");
                } else if (host.match("lane-beta.stanford.edu")) {
                	gaPageTracker = _gat._createTracker("UA-3203486-9","gaPageTracker");
                } else {
                	gaPageTracker = _gat._createTracker("UA-3203486-2","gaPageTracker");
                }
                //uncomment this for testing/debugging:
                //gaPageTracker._setLocalServerMode();
                gaPageTracker._setDomainName(".stanford.edu");
                gaPageTracker._trackPageview();
                meta = Y.one('html head meta[name="WT.seg_1"]');
                if (meta) {
                	gaPageTracker._setVar(meta.get('content'));
                }
                LANE.tracking.addTracker({
                    track: function(trackingData) {
                        if (trackingData.external) {
                        	gaPageTracker._trackPageview('/OFFSITE/' + encodeURIComponent(trackingData.title));
                        } else {
                        	gaPageTracker._trackPageview('/ONSITE/' + encodeURIComponent(trackingData.title) + '/' + trackingData.path);
                        }
                    }
                });
            }
        }
    });
    Y.Global.on("lane:suggestSelect",  function(event) {
        if (gaPageTracker !== undefined) {
        	gaPageTracker._trackEvent(event.type, event.parentForm.source.value, event.suggestion);
        }
    });
    Y.Global.on("lane:searchOptions",  function(event) {
    	if (gaPageTracker !== undefined) {
    		gaPageTracker._trackEvent(event.type, "changeOptions", event.action);
    	}
    });
    Y.Global.on("lane:searchFacets",  function(event) {
    	if (gaPageTracker !== undefined) {
    		gaPageTracker._trackEvent(event.type, "facetClick", event.facetName);
    	}
    });

});
