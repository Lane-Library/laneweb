(function() {
        LANE.tracking = function() {
            //TODO more descriptive variable names
            //TODO put conditionals into sub-functions
            //TODO more thorough documentation
            //TODO use 'track' less
            //trackers is the array of tracker objects
            var enabled = true,
            trackers = [],        //figures out the title string for a node
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
                            else if (img.item(i).get('src')) {
                                title = img.item(i).get('src');
                                break;
                            }
                        }
                    }
                }
                //next get the text content before any nested markup
                if (!title) {
                    title = node.get('textContent');
                }
                if (!title) {
                    title = node.get('innerText');
                }
                if (title) {
                    //trim and normalize:
                    title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
                }
                //finally:
                if (!title) {
                    title = 'unknown';
                }
                if (node.hasClass('yui3-accordion-item-trigger')) {
                    title = 'Expandy:' + title;
                } else if (node.ancestor("#laneNav")) {
                    title = "laneNav: " + title;
                }
                return title;
            },
            getTrackingData = function(event) {
                var node = event.target, host, path, query, external, title, searchTerms, searchSource;
                if (event.type == 'click') {
                    if (node.hasClass('yui3-accordion-item-trigger')) {
                        host = document.location.host;
                        path = document.location.pathname;
                        query = document.location.search;
                        external = false;
                    } else {
                        if (node.get('nodeName') != 'A' && node.one('a')) {
                            node = node.one('a');
                        }
                        while (node && node.get('nodeName') != 'A') {
                            node = node.get('parentNode');
                            if (node === null) {
                                throw 'not trackable';
                            }
                        }
                        if (node.get('pathname').indexOf('cookiesFetch') > -1) {
                            host = decodeURIComponent(node.get('search'));
                            host = host.substring(host.indexOf("path=") + 6);
                            if (host.indexOf("&") > -1) {
                                host = host.substring(0, host.indexOf("&"));
                            }
                            host = host.substring(host.indexOf("//") + 2);
                            path = host.substring(host.indexOf("/"));
                            host = host.substring(0, host.indexOf("/"));
                        } else if (node.get('pathname').indexOf('secure/apps/proxy/credential') > -1 || node.get('host').indexOf('laneproxy') === 0) {
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
                if (LANE.search && LANE.SearchResult.getSearchTerms()) {
                    searchTerms = LANE.SearchResult.getSearchTerms();
                    searchSource = LANE.SearchResult.getSearchSource();
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


            Y.on("startTracking", function() {
            	enabled = true;
            });
            Y.on("stopTracking", function() {
            	enabled = false;
            });
            return {
                addTracker: function(tracker) {
                    if (!tracker || tracker.track === undefined) {
                        throw 'tracker does not implement track()';
                    }
                    trackers.push(tracker);
                },
                trackEvent: function(event) {
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
                isTrackable: function(event) {
                	if (!enabled) {
                		return false;
                	}
                    var link, documentHost, linkHost, rel, relTokens;
                    documentHost = document.location.host;
                    if (documentHost.indexOf(':') > -1) {
                        documentHost = documentHost.substring(0, documentHost.indexOf(':'));
                    }
                    if (event.type == 'click') {
                        if (event.target.ancestor('.searchFacet')) {
                            return true;
                        }
                        if (event.target.hasClass('yui3-accordion-item-trigger')) {
                            return true;
                        }
                        //find self ancestor that is <a>
                        link = event.target;
                        while (link && link.get('nodeName') != 'A') {
                            link = link.get('parentNode');
                        }
                        if (link) {
                            //for popups:
                            rel = link.get('rel');
                            if (rel && rel.indexOf('popup ') === 0) {
                                relTokens = rel.split(' ');
                                if (relTokens[1] == 'local' || relTokens[1] == 'faq') {
                                    return true;
                                }
                            }
                            linkHost = link.get('host');
                            if (linkHost.indexOf(':') > -1) {
                                linkHost = linkHost.substring(0, linkHost.indexOf(':'));
                            }
                            if (linkHost == documentHost) {
                                //track proxy logins
                                if ((/secure\/apps\/proxy\/credential\?/).test(link.get('pathname')) && link.get('search') && link.get('search').indexOf('url=') > -1) {
                                    return true;
                                }
                                //track cookieFetch.html
                                if ((/cookiesFetch/).test(link.get('pathname'))) {
                                    return true;
                                }
                                //otherwise rely on normal tracking for .html
                                if ((/\.html$/).test(link.get('pathname')) || (/\/$/).test(link.get('pathname'))) {
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
        LANE.Y.on('click', function(e) {
            LANE.tracking.trackEvent(e);
            //put in a delay for safari to make the tracking request:
//            if (LANE.Y.UA.webkit && LANE.tracking.isTrackable(e)) {
                    var t = e.target, f;
                    while (t) {
                        // have safari follow link if it's not:
                        //  - popup or facet
                        //    (can't halt facet click propagation b/c they need to be tracked)
                        if (t.get('href') 
                                && (!t.get('rel') && !t.get('target'))
                                && !t.get('parentNode').hasClass('searchFacet') ){
                            f = function() {
                                window.location = t.get('href');
                            };
                            e.preventDefault();
                            setTimeout(f, 500);
                            break;
                        }
                        t = t.get('parentNode');
                    }
//            }
        }, document);
})();
