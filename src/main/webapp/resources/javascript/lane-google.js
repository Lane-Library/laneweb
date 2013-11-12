(function() {
    var gaPageTracker,
        lane = Y.lane,
        tracker = lane.Tracker,
        location = lane.Location,
        gaJsHost = (("https:" == location.get("protocol")) ? "https://ssl." : "http://www.");
    Y.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function() {
            var host = location.get("host"),
                model = lane.Model,
                ipgroup = model.get(model.IPGROUP),
                auth = model.get(model.AUTH);
            if (_gat !== undefined) {
                if (host.match("lane.stanford.edu")) {
                    gaPageTracker = _gat._createTracker("UA-3202241-2","gaPageTracker");
                } else {
                    gaPageTracker = _gat._createTracker("UA-3203486-2","gaPageTracker");
                }
                //uncomment this for testing/debugging:
                //gaPageTracker._setLocalServerMode();
                gaPageTracker._setDomainName(".stanford.edu");
                if (ipgroup) {
                    gaPageTracker._setVar(ipgroup);
                    gaPageTracker._setCustomVar(1,'ipGroup',ipgroup ,2);
                }
                if (auth) {
                    gaPageTracker._setCustomVar(2,'authenticatedSession',auth,2);
                    if (lane.BookmarksWidget && lane.BookmarksWidget.get("bookmarks").size() > 0) {
                        gaPageTracker._setCustomVar(3,'bookmarkEnabledSession',auth,2);
                    }
                }
                gaPageTracker._trackPageview();
            }
        }
    });
    tracker.on("trackableEvent",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.category, event.action, event.label, event.value);
        }
    });
    tracker.on("trackablePageview",  function(event) {
        if (gaPageTracker !== undefined) {
            if (event.external) {
                if(event.query !== undefined && event.query !== '' ){
                    gaPageTracker._trackEvent('lane:offsite', "/OFFSITE-CLICK-EVENT/"+encodeURIComponent(event.title) ,event.host+event.path+event.query);
                }else{
                    gaPageTracker._trackEvent('lane:offsite', "/OFFSITE-CLICK-EVENT/"+encodeURIComponent(event.title) ,event.host+event.path);
                }
                gaPageTracker._trackPageview('/OFFSITE/' + encodeURIComponent(event.title));
            } else {
                gaPageTracker._trackPageview('/ONSITE/' + encodeURIComponent(event.title) + '/' + event.path);
            }
        }
    });
})();
