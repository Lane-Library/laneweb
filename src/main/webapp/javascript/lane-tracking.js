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
                host, path, query, external,
                getTrackedTitle = function(){
                    var title = l.title, img, i = 0;
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
            if (l.pathname.indexOf('/secure/login.html') > -1) {
                host = (l.search.substring(l.search.indexOf('//') + 2));
                if (host.indexOf('/') > -1) {
                    path = host.substring(host.indexOf('/'));
                    if (path.indexOf('?') > -1) {
                        path = path.substring(0, path.indexOf('?'));
                    }
                    host = host.substring(0, host.indexOf('/'));
                }
                query = '';
                external = true;
            } else {
                host = l.host;
                path = l.pathname;
                external = l.host != document.location.host;
                query = external ? '' : l.search;
            }
            return {
                host: host,
                path: path,
                query: query,
                title: getTrackedTitle(),
                searchTerms: LANE.search.getSearchString(),
                searchSource: LANE.search.getSearchSource(),
                external: external
            };
        };
        
    YAHOO.util.Event.addListener(document, 'click', function(e){
//                            YAHOO.util.Event.preventDefault(e);
        if (isTrackableClick(e)) {
            var td = getTrackingData(e), node, parent, href,
                f = function(href) {
                    window.location = href;
                };
                //alert('host: '+td.host+'\npath: '+td.path+'\nquery: '+td.query+'\ntitle: '+td.title+'\nsearchTerms: '+td.searchTerms+'\nsearchSource: '+td.searchSource+'\nexternal: '+td.external);
            for (var i = 0; i < trackers.length; i++) {
                trackers[i].track(td);
            }
            //put in a delay for safari to make the tracking request:
            if (YAHOO.env.ua.webkit) {
                    node = e.target;
                    parent = node;
                    while (parent) {
                        if (parent.clicked !== undefined) {
                            return;
                        }
                        parent = parent.parentNode;
                    }
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
        },
        track: function(td) {
                alert('host: '+td.host+'\npath: '+td.path+'\nquery: '+td.query+'\ntitle: '+td.title+'\nsearchTerms: '+td.searchTerms+'\nsearchSource: '+td.searchSource+'\nexternal: '+td.external);
        }
    };
}();
