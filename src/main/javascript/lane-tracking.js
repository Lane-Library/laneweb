

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
                    if ((/\/secure\/login.html/).test(node.pathname)) {
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
        track:function(trackable) {
            for(var i = 0; i < trackers.length; i++) {
                trackers[i].track(trackable);
            }
        },
        Trackable: function(node) {
            //TODO: make this real
            var n = ['A','AREA'],
            getTrackedTitle = function() {
                return 'The Title';
            };
            return {
                host:'www.google.com',
                uri:'/foo/bar.html',
                query:'',
                title:getTrackedTitle(node),
                searchTerms:'',
                searchSource:'',
                external:false
            };
        }
    };
}();
