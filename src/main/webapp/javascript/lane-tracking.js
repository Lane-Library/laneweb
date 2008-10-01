LANE.track = function(){
    var trackers = [],
        isTrackableClick = function(e){
            var node = e.srcElement || e.target, h, rel;
            h = document.location.host;
            //find self or ancestor with href
            while (node && node.nodeName != 'A') {
                node = node.parentNode;
            }
            if (node) {
                //for popups:
                if (node.rel) {
                    rel = node.rel.split(' ');
                    if (rel[0] == 'popup' && (rel[1] == 'local' || rel[1] == 'faq')) {
                        //TODO: change to true when ready to track popups
                        return false;
                    }
                }
                if (node.host == h) {
                    //track proxy logins
                    if ((/secure\/login.html/).test(node.pathname)) {
                        return true;
                    }
                    //otherwise rely on normal tracking for .html unless
                    //a parent has a clicked function
                    if ((/\.html$/).test(node.pathname)) {
                        while (node !== null) {
                            if (node.clicked && !node.rel) {
                                return true;
                            }
                            node = node.parentNode;
                        }
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
        isTrackableMouseover = function(e){
            var node = e.srcElement || e.target;
            while (node) {
                if (node.trackable) {
                    return true;
                }
                node = node.parentNode;
            }
            return false;
        },
        isTrackableSubmit = function(e){
            var node = e.srcElement || e.target;
            return node.action.indexOf(document.location.host) == -1;
        },
        getTrackingData = function(e){
            var node = e.srcElement || e.target,
                l = node,
                getTrackedTitle = function(){
                    var title = node.title, img, i = 0;
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
                        title = node.innerHTML;
                        if (title && title.indexOf('<') > -1) {
                            title = title.substring(0, title.indexOf('<'));
                        }
                    }
                    if (!title) {
                        title = 'unknown';
                    }
                    return title;
                };
            while (l.href === undefined) {
                l = l.parentNode;
                if (l === null) {
                    throw 'not trackable';
                }
            }
            return {
                host: l.host,
                path: l.pathname || '',
                query: l.search,
                title: getTrackedTitle(node),
                searchTerms: LANE.core.getMetaContent('LW.searchTerms'),
                searchSource: LANE.core.getMetaContent('LW.searchSource'),
                external: l.host != document.location.host
            };
        };
        
    YAHOO.util.Event.addListener(document, 'click', function(e){
        if (isTrackableClick(e)) {
            var td = getTrackingData(e),
                f = function(href) {
                    window.location = href;
                };
            for (var i = 0; i < trackers.length; i++) {
                trackers[i].track(td);
            }
            //put in a delay for safari to make the tracking request:
            if (YAHOO.env.ua.webkit) {
                    var node = e.target, href;
                    while (node) {
                        if (node.href && (!node.rel && !node.target)) {
                            href = node.href;
                            YAHOO.util.Event.preventDefault(e);
                            setTimeout(f(href), 200);
                            break;
                        }
                        node = node.parentNode;
                    }
            }
        }
    });
    //    YAHOO.util.Event.addListener(this, 'mouseover', function(e){
    //        if (isTrackableMouseover(e)) {
    //            var td = getTrackingData(e);
    //            for (var i = 0; i < trackers.length; i++) {
    //                trackers[i].track(td);
    //            }
    //        }
    //    });
    //    YAHOO.util.Event.addListener(this, 'submit', function(e){
    //        if (isTrackableSubmit(e)) {
    //            var td = getTrackingData(e);
    //            for (var i = 0; i < trackers.length; i++) {
    //                trackers[i].track(td);
    //            }
    //        }
    //    });
    return {
        addTracker: function(tracker){
            if (!tracker || tracker.track === undefined) {
                throw 'tracker does not implement track()';
            }
            trackers.push(tracker);
        }
    };
}();
