YUI().add('lane-tracking',function(Y) {
	LANE.tracking = function(){
    //TODO more descriptive variable names
    //TODO put conditionals into sub-functions
    //TODO more thorough documentation
    //TODO use 'track' less
    //trackers is the array of tracker objects
        var trackers = [],
            //figures out the title string for a node
            getTrackedTitle = function(node) {
                //if there is a title attribute, use that.
                var title = node.get('title'), img, i, rel, relTokens;
                //if there is rel="popup .." then create a title from it.
                rel = node.get('rel');
                if (rel && rel.indexOf('popup') === 0) {
                    relTokens = rel.split(' ');
                    if (relTokens[1] == 'local' || relTokens[1] == 'faq') {
                        title = 'YUI Pop-up [' + relTokens[1] + ']';
                    }
                }
                //next try alt attribute.
                if (!title) {
                    title = node.get('alt');
                }
                //next look for alt attributes in any child img.
                if (!title) {
                    img = node.all('img');
                    if (img) {
                        for (i = 0; i < img.size(); i++) {
                            if (img.item(i).get('alt')) {
                                title = img.item(i).get('alt');
                                break;
                            }
                        }
                    }
                }
                //next get the text content before any nested markup
                //TODO:textContent?
                if (!title) {
                    title = node.get('innerHTML');
                    if (title && title.indexOf('<') > -1) {
                        title = title.substring(0, title.indexOf('<'));
                    }
                }
                if (title) {
                    //trim and normalize:
                    title = title.replace(/\s+/g,' ').replace(/^\s|\s$/g, '');
                }
                //finally:
                if (!title) {
                    title = 'unknown';
                }
                if (node.hasClass('yui-accordion-toggle')) {
                    title = 'Expandy:' + title;
                }
                return title;
            },
            getTrackingData = function(event){
                var node = event.target,
                    host, path, query, external, title, searchTerms, searchSource, children;
                    if (event.type == 'click') {
                        if (node.hasClass('yui-accordion-toggle')) {
                            host = document.location.host;
                            path = document.location.pathname;
                            query = document.location.search;
                            external = false;
                        } else {
                            if (node.get('nodeName') != 'A') {
                                node = node.one('a');
                            }
                            while (node && node.get('nodeName') != 'A') {
                                node = node.get('parentNode');
                                if (node === null) {
                                    throw 'not trackable';
                                }
                            }
                            if (node.get('pathname').indexOf('secure/login.html') > -1 || node.get('host').indexOf('laneproxy') === 0) {
                                host = (node.get('search').substring(node.get('search').indexOf('//') + 2));
                                if (host.indexOf('/') > -1) {
                                    path = host.substring(host.indexOf('/'));
                                    if (path.indexOf('?') > -1) {
                                        path = path.substring(0, path.indexOf('?'));
                                    }
                                    host = host.substring(0, host.indexOf('/'));
                                } else {
                                    path = '/';
                                }
                                query = '';
                                external = true;
                            } else if (node.get('rel') && (node.get('rel').indexOf('popup local') === 0 || node.get('rel').indexOf('popup faq') === 0)) {
                                host = document.location.host;
                                path = document.location.pathname;
                                query = document.location.search;
                            } else {
                                host = node.get('host');
                                if (host.indexOf(':') > -1) {
                                    host = host.substring(0, host.indexOf(':'));
                                }
                                path = node.get('pathname');
                                external = host != document.location.host;
                                query = external ? '' : node.get('search');
                            }
                        }
                    }
                    if (path.indexOf('/') !== 0) {
                        path = '/' + path;
                    }
                    title = getTrackedTitle(node);
                    if (LANE.search && LANE.search.Result.getSearchTerms()) {
                        searchTerms = LANE.search.Result.getSearchTerms();
                        searchSource = LANE.search.Result.getSearchSource();
                    }
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
            trackEvent: function(event){
                var trackingData;
                if (this.isTrackable(event)) {
                    trackingData = getTrackingData(event);
                    this.track(trackingData);
                }
            },
            track: function(trackingData) {
    //            var td = trackingData;
    //                alert('host: '+td.host+'\npath: '+td.path+'\nquery: '+td.query+'\ntitle: '+td.title+'\nsearchTerms: '+td.searchTerms+'\nsearchSource: '+td.searchSource+'\nexternal: '+td.external);
                     for (var i = 0; i < trackers.length; i++) {
                         trackers[i].track(trackingData);
                     }
            },
            isTrackable: function(event){
                var link, documentHost, linkHost, relTokens;
                documentHost = document.location.host;
                if (documentHost.indexOf(':') > -1) {
                    documentHost = documentHost.substring(0, documentHost.indexOf(':'));
                }
                if (event.type == 'click') {
                    if (event.target.hasClass('searchFacet')) {
                        return true;
                    }
                    if (event.target.hasClass('yui-accordion-toggle')) {
                        return true;
                    }
                    //find self ancestor that is <a>
                    link = Y.Node.getDOMNode(event.target);
                    while (link && link.nodeName != 'A') {
                        link = link.parentNode;
                    }
                    if (link) {
                        //for popups:
                        if (link.rel && link.rel.indexOf('popup ') === 0) {
                            relTokens = link.rel.split(' ');
                            if (relTokens[1] == 'local' || relTokens[1] == 'faq') {
                                return true;
                            }
                        }
                        linkHost = link.host;
                        if (linkHost.indexOf(':') > -1) {
                            linkHost = linkHost.substring(0, linkHost.indexOf(':'));
                        }
                        if (linkHost == documentHost) {
                            //track proxy logins
                            if ((/secure\/login.html/).test(link.pathname) && link.search && link.search.indexOf('url=') > -1) {
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
                }
                return false;
            }
        };
    }();
        Y.on('click', function(e) {
                LANE.tracking.trackEvent(e);
                //put in a delay for safari to make the tracking request:
                //TODO: revisit this and make sure it actually is useful
                //            if (Y.UA.webkit && LANE.tracking.isTrackable(e)) {
                //                    t = e.target;
                //                    parent = t;
                //                    while (parent) {
                //                        if (parent.clicked !== undefined) {
                //                            return;
                //                        }
                //                        parent = parent.parentNode;
                //                    }
                //                    while (t) {
                //                        if (t.href && (!t.rel && !t.target)) {
                //                            f = function() {
                //                                window.location = t.href;
                //                            };
                //                            Y.YUI2.util.Event.preventDefault(e);
                //                            setTimeout(f, 200);
                //                            break;
                //                        }
                //                        t = t.parentNode;
                //                    }
                //            }
        }, document);
}, '1.11.0-SNAPSHOT', {requires:['lane', 'node','event-custom']});