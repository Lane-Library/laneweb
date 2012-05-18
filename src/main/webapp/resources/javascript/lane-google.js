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
                meta = Y.one('html head meta[name="ipGroup"]');
                if (meta) {
                    gaPageTracker._setVar(meta.get('content'));
                    gaPageTracker._setCustomVar(1,'ipGroup',meta.get('content'),2);
                }
                if (Y.one('html head meta[name="auth"][content="true"]')) {
                    gaPageTracker._setCustomVar(2,'authenticatedSession','true',2);
                }
                gaPageTracker._trackPageview();
                LANE.tracking.addTracker({
                    track: function(trackingData) {
                        if (trackingData.external) {
                        	if(trackingData.query !== undefined && trackingData.query !== '' ){
                        		gaPageTracker._trackEvent('lane:offsite', "/OFFSITE-CLICK-EVENT/"+encodeURIComponent(trackingData.title) ,trackingData.host+trackingData.path+trackingData.query);
                        	}else{
                        		gaPageTracker._trackEvent('lane:offsite', "/OFFSITE-CLICK-EVENT/"+encodeURIComponent(trackingData.title) ,trackingData.host+trackingData.path);
                        	}
                        	gaPageTracker._trackPageview('/OFFSITE/' + encodeURIComponent(trackingData.title));
                        } else {
                            gaPageTracker._trackPageview('/ONSITE/' + encodeURIComponent(trackingData.title) + '/' + trackingData.path);
                        }
                    }
                });
            }
        }
    });
        
        // for search result event tracking
    Y.publish("lane:searchResultClick",{
        broadcast:1,
        emitFacade: true,
        searchTerms:null,
        resultTitle:null,
        resultPosition:null
    });
    Y.publish("lane:browseResultClick",{
        broadcast:1,
        emitFacade: true,
        resultTitle:null,
        resultPosition:null
    });
    Y.on("click", function(event) {
        
        var link = event.target;
        while (link && link.get('nodeName') != 'A') {
            link = link.get('parentNode');
        }
        if (link) {
            if (link.ancestor(".lwSearchResults")) {
                if (LANE.SearchResult.getSearchTerms()) {
                    Y.fire("lane:searchResultClick", {
                        searchTerms: LANE.SearchResult.getSearchTerms(),
                        resultTitle: link.get('textContent'),
                        resultPosition: parseInt(link.ancestor('ul').get('className').replace(/r-/, ''), 10)
                    });
                } else {
                    Y.fire("lane:browseResultClick", {
                        resultTitle: link.get('textContent'),
                        resultPosition: parseInt(link.ancestor('ul').get('className').replace(/r-/, ''), 10)
                    });
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
            gaPageTracker._trackEvent(event.type, event.linkName);
        }
    });
    Y.on("lane:searchResultClick",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.type, event.searchTerms, event.resultTitle, event.resultPosition);
        }
    });
    Y.on("lane:browseResultClick",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.type, document.location.pathname, event.resultTitle, event.resultPosition);
        }
    });
    Y.on("lane:searchFormReset",  function(event) {
        if (gaPageTracker !== undefined) {
            gaPageTracker._trackEvent(event.type, document.location.pathname);
        }
    });
})();
