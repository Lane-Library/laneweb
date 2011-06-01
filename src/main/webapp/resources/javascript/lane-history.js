/**
 * A tracker implementation that sends the data back to the web
 * application for history tracking.
 */
(function() {
    
    var HistoryTracker = function() {
        HistoryTracker.superclass.constructor.apply(this, arguments);
    };
    
    HistoryTracker.ATTRS = {
        io : {
            value : Y.io
        }
    };
    

    Y.extend(HistoryTracker, Y.Base, {
        track : function(trackingData) {
            this.get("io")("/././history/track", {
                method : "post",
                data : Y.JSON.stringify(trackingData),
                headers: {
                    "Content-Type" : "application/json"
                }
            });
        }
    });
    
    Y.lane.HistoryTracker = new HistoryTracker();
    
    Y.on("domready", function() {
        //only track history if <meta name="emrid"/>
        var emridMeta = Y.one("meta[name='emrid']"),
            searchTerms, emrid, title, url, index;
        if (emridMeta) {
            //shorten title:
        	title = Y.one("title").getContent();
        	index = title.indexOf(" - Lane Medical Library");
            if (index > 0) {
                title = title.substring(0, index).replace("&amp;", "&");
            } else {
                title = "Lane Medical Library";
            }
            searchTerms = Y.lane.SearchResult.getSearchTerms();
            if (searchTerms) {
            	title = "Search for: " + searchTerms;
            }
            url = window.location.pathname;
            if (window.location.search) {
            	url += "?" + window.location.search;
            }
            Y.lane.HistoryTracker.track({
                label : title,
                url : url
            });
            
            Y.on("trackable", function(link, event) {
                var trackingData = link.get("trackingData");
                if (trackingData.external) {
                    Y.lane.HistoryTracker.track({
                        label : trackingData.title,
                        url : link.get("url")
                    });
                }
            });
        }
    });
    
})();