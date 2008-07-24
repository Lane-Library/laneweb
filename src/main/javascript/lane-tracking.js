

LANE.track = function() {
    var trackers = [];
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
            //TODO: make this real
            var getTrackedTitle = function() {
                return 'The Title';
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
                external:l.host == document.location.host
            };
        }
    };
}();
