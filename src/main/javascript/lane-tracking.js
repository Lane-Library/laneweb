

LANE.track = function() {
    var trackers = [];
    return {
        addTracker:function(tracker) {
            if (!tracker || tracker.track === undefined) {
                throw 'tracker does not implement track()';
            }
            trackers.push(tracker);
        },
        getTrackedTitle: function(node) {
            //TODO implement this
            return 'title';
        },
        isTrackable:function(node) {
            var h = document.location.host;
            //find self or ancestor with href
            while(node && node.nodeName != 'A') {
                node = node.parentNode;
            }
            if (node) {
                if (node.host == h) {
                    //TODO:remove this in favor of a test
                    if (!node.pathname) {
                        alert('no node.pathname');
                    }
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
        track:function(node) {
            //TODO: instead of a node pass an object with various
            //required properties.  I need to determine where best
            //to build that object, here, in the tracker, or in 
            //whatever calls this.  Probably that last one, but defined
            //here.
            var i;
            if (!this.isTrackable(node)) {
                throw 'node is not trackable';
            }
            for(i = 0; i < trackers.length; i++) {
                trackers[i].track(node);
            }
        }
    };
}();
