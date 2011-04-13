(function() {
    var gaPageTracker,
        gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    Y.Get.script(gaJsHost + "google-analytics.com/ga.js", {
        onSuccess: function() {
            var host = document.location.host,
                meta;
            if (_gat !== undefined) {
                if (host.match("lane.stanford.edu")) {
                    gaPageTracker = _gat._createTracker("UA-3202241-2","gaPageTracker");
                } else {
                    gaPageTracker = _gat._createTracker("UA-3203486-2","gaPageTracker");
                }
                //uncomment this for testing/debugging:
                //gaPageTracker._setLocalServerMode();
                gaPageTracker._setDomainName(".stanford.edu");
                meta = Y.one('html head meta[name="WT.seg_1"]');
                if (meta) {
                    gaPageTracker._setVar(meta.get('content'));
                    gaPageTracker._setCustomVar(1,'ipGroup',meta.get('content'),2);
                }
                gaPageTracker._trackPageview();
                Y.on("trackable", function(link, event) {
                    var trackingData;
                    if (link.get("trackable")) {
                        trackingData = link.get("trackingData");
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
    Y.on("click", function(event) {
        if (gaPageTracker !== undefined) {
            var searchTerms = Y.lane.SearchResult.getSearchTerms(),
                link = event.target.get("nodeName") == "A" ? event.target : event.target.ancestor("a"),
                resultTitle, resultPosition;
            if (link && link.ancestor(".lwSearchResults")) {
                resultTitle = link.get("textContent");
                resultPosition = parseInt(link.ancestor('ul').get('className').replace(/r-/, ''), 10);
                if (searchTerms) {
                    gaPageTracker._trackEvent("lane:searchResultClick", searchTerms, resultTitle, resultPosition);
                } else {
                    gaPageTracker._trackEvent("lane:browseResultClick", document.location.pathname, resultTitle, resultPosition);
                }
            }
        }
    }, document);
        
    Y.on("lane:suggestSelect",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.type, event.parentForm.source.value, event.suggestion);
        }
    });
    Y.on("lane:quickLinkClick",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent("lane:quickLinkClick", event.linkName);
        }
    });
    Y.on("lane:searchFormReset",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.type, document.location.pathname);
        }
    });
})();
