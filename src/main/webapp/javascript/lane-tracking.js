LANE.track = function(){
    var trackers = [],
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
            if (l.pathname.indexOf('/secure/login.html') > -1 || l.host == 'laneproxy.stanford.edu' ) {
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
                if (external) {
                    query = '';
                } else {
                    if (l.search.length > 1) {
                        query = l.search.substring(1, l.search.length);
                    } else {
                        query = '';
                    }
                }
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
    return {
        addTracker: function(tracker){
            if (!tracker || tracker.track === undefined) {
                throw 'tracker does not implement track()';
            }
            trackers.push(tracker);
        },
        trackEvent: function(e){
            var td;
            if (this.isTrackable(e)) {
                td = getTrackingData(e);
                this.track(td);
            }
        },
        track: function(td) {
//                alert('host: '+td.host+'\npath: '+td.path+'\nquery: '+td.query+'\ntitle: '+td.title+'\nsearchTerms: '+td.searchTerms+'\nsearchSource: '+td.searchSource+'\nexternal: '+td.external);
                 for (var i = 0; i < trackers.length; i++) {
                     trackers[i].track(td);
                 }
        },
        isTrackable: function(e){
            var node = e.srcElement || e.target, h, rel;
            h = document.location.host;
            //find self or ancestor with href
            if (e.type == 'click') {
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
            } else if (e.type == 'mouseover'){
                while (node) {
                    if (node.trackable) {
                        return true;
                    }
                    node = node.parentNode;
                }
                return false;
            } else if (e.type == 'submit') {
                return node.action.indexOf(document.location.host) == -1;
            }
            return false;
        }
    };
}();
