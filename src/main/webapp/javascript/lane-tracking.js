LANE.tracking = function(){
    //TODO more descriptive variable names
    //TODO put conditionals into sub-functions
    //TODO more thorough documentation
    //TODO use 'track' less
    var trackers = [],
        getTrackingData = function(e){
            var node = e.srcElement || e.target,
                //TODO: not sure I need this l variable
                l = node,
                host, path, query, external, title, searchTerms, searchSource, children,
                getTrackedTitle = function(){
                    var title = l.title, img, i = 0;
                    if (l.rel && l.rel.indexOf('popup') === 0) {
                        title = 'YUI Pop-up [' + l.rel.substring(6) + ']';
                    } else if (l.nodeName == 'FORM') {
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
                    } else if (l.rel) {
                        host = document.location.host;
                        path = document.location.pathname;
                        query = document.location.search;
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
            var target = e.srcElement || e.target, link, documentHost, linkHost, relTokens;
            documentHost = document.location.host;
            if (e.type == 'click') {
                if (target.className == 'eLibraryTab') {
                    return true;
                }
                //find self ancestor that is <a>
                link = target;
                while (link && link.nodeName != 'A') {
                    link = link.parentNode;
                }
                if (link) {
                    //for popups:
                    if (link.rel && link.rel.indexOf('popup ') === 0) {
                        relTokens = link.rel.split(' ');
                        if (relTokens[1] == 'local' || relTokens[1] == 'faq') {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    linkHost = link.host;
                    if (linkHost.indexOf(':') > -1) {
                        linkHost = linkHost.substring(0, linkHost.indexOf(':'));
                    }
                    if (linkHost == documentHost) {
                        //track proxy logins
                        if ((/secure\/login.html/).test(link.pathname) && link.search !== '') {
                            return true;
                        }
                        //otherwise rely on normal tracking for .html and / unless
                        //a parent has a clicked function
                        if ((/\.html$/).test(link.pathname) || (/\/$/).test(link.pathname)) {
                            while (link !== null) {
                                if (link.clicked && !link.rel) {
                                    return true;
                                }
                                link = link.parentNode;
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
                while (target) {
                    if (target.trackable) {
                        return true;
                    }
                    target = target.parentNode;
                }
                return false;
            } else if (e.type == 'submit') {
                if (target.action.indexOf(document.location.host) == -1) {
                    if (target.isValid !== undefined && !target.isValid) {
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

//for temporary backwards compatibility:
LANE.track = LANE.tracking;
