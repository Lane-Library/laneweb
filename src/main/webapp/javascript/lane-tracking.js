LANE.track = function(){
    var trackers = [],
        getTrackingData = function(e){
            var node = e.srcElement || e.target,
                //TODO: not sure I need this l variable
                l = node,
                host, path, query, external, title, searchTerms, searchSource, children,
                getTrackedTitle = function(){
                    var title = l.title, img, i = 0;
                    if (l.nodeName == 'FORM') {
                        title = l.name || l.id;
                    }
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
                    return title.replace(/\s+/g,' ').replace(/^\s|\s$/g, '');
                };
                if (e.type == 'click') {
                    if (l.nodeName != 'A') {
                        children = l.getElementsByTagName('a');
                        if (children.length > 0) {
                            l = children[0];
                        }
                    }
                    while (l && l.nodeName != 'A') {
                        l = l.parentNode;
                            if (l === null) {
                                throw 'not trackable';
                            }
                    }
                    if (l.pathname.indexOf('secure/login.html') > -1 || l.host.indexOf('laneproxy') === 0) {
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
                        if (host.indexOf(':') > -1) {
                            host = host.substring(0, host.indexOf(':'));
                        }
                        path = l.pathname;
                        external = host != document.location.host;
                        query = external ? '' : l.search;
                    }
                }
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                title = getTrackedTitle();
                searchTerms = LANE.search.getSearchString();
                searchSource = LANE.search.getSearchSource();
            return {
                host: host,
                path: path,
                query: query,
                title: title,
                searchTerms: searchTerms,
                searchSource: searchSource,
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
                //TODO: remove this after fixing bugs 22495 and 22496
                            if (td.path.indexOf('secure/login') > -1) {
                                var msg = 'useragent:' + navigator.userAgent;
                                msg += ';ref:' + document.location.toString();
                                msg += ';title:' + trackingData.title;
                                msg += ';path:' + trackingData.path;
                                msg += ';external:' + trackingData.external;
                                LANE.core.log(msg);
                            }
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
            var node = e.srcElement || e.target, dh, nh, rel;
            dh = document.location.host;
            //find self ancestor that is <a>
            if (e.type == 'click') {
                if (node.className == 'eLibraryTab') {
                    return true;
                }
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
                    nh = node.host;
                    if (nh.indexOf(':') > -1) {
                        nh = nh.substring(0, nh.indexOf(':'));
                    }
                    if (nh == dh) {
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
                if (node.action.indexOf(document.location.host) == -1) {
                    if (node.isValid !== undefined && !node.isValid) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    };
}();
