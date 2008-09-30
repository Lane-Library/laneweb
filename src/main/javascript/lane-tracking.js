LANE.track = function() {
    var trackers = [];
    YAHOO.util.Event.addListener(this, 'click', function(e) {
            var t = e.srcElement || e.target;
            while (t) {
                if (LANE.track.isTrackable(t)) {
                    LANE.track.track(t);
                    return;
                }
                t = t.parentNode;
            }
    });
    return {
        addTracker:function(tracker) {
            if (!tracker || tracker.track === undefined) {
                throw 'tracker does not implement track()';
            }
            trackers.push(tracker);
        },
        isTrackable:function(node) {
            var h = document.location.host;
            //find self or ancestor with href
            while(node && node.nodeName != 'A') {
                node = node.parentNode;
            }
            if (node) {
                if (node.host == h) {
                    //track proxy logins
                    if ((/secure\/login.html/).test(node.pathname)) {
                        return true;
                    }
                    //otherwise rely on normal tracking for .html
                    if ((/\.html$/).test(node.pathname)) {
                        return false;
                    }
                    //all others fall through to trackable
                    return true;
                }
                //external reference is trackable
                return true;   
            }
            //no href, not trackable
            return false;
        },
        track:function(node) {
            var td = this.getTrackingData(node);
            for(var i = 0; i < trackers.length; i++) {
                trackers[i].track(td);
            }
        },
        getTrackingData:function(node) {
            var getTrackedTitle = function() {
                var title  = node.title, img, i = 0;
                if (!title) {
                    title = node.alt;
                }
                if (!title) {    
	                img = node.getElementsByTagName("IMG");
                    if (img) {
                        for (i = 0; i < img.length; i++) {
                            if (img[i].alt) {
                                title = img[i].alt;
                                break;
                            }
                        }
                    }
                }
                if (!title) {
                    title = node.innerHTML;//textContent doesn't work with safari
                    if (title && title.indexOf('<') > -1 ) {
                        title = title.substring(0,title.indexOf('<'));
                    }
                }        
                if (!title) {
                    title = 'unknown';
                }
                return title;
            },
            l = node;
            while(l.href === undefined) {
                l = l.parentNode;
                if (l === null) {
                    throw 'not trackable';
                }
            }
            if (!this.isTrackable(node)) {
                throw 'not trackable';
            }
            return {
                host:l.host,
                path:l.pathname,
                query:l.search,
                title:getTrackedTitle(node),
                searchTerms:LANE.core.getMetaContent('LW.searchTerms'),
                searchSource:LANE.core.getMetaContent('LW.searchSource'),
                external:l.host != document.location.host
            };
        }
    };
}();
