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
            var title = node.title, img, i = 0;
            //if there is rel="popup .." then create a title from it.
            if (node.rel && node.rel.indexOf('popup') === 0) {
                title = 'YUI Pop-up [' + node.rel.substring(6) + ']';
            }
            //next try alt attribute.
            if (!title) {
                title = node.alt;
            }
            //next look for alt attributes in any child img.
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
            //next get the text content before any nested markup
            //TODO:textContent?
            if (!title) {
                title = node.innerHTML;
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
            return title;
        },
        getTrackingData = function(event){
            var node = event.srcElement || event.target,
                host, path, query, external, title, searchTerms, searchSource, children;
                if (event.type == 'click') {
                    if (node.nodeName != 'A') {
                        children = node.getElementsByTagName('a');
                        if (children.length > 0) {
                            node = children[0];
                        }
                    }
                    while (node && node.nodeName != 'A') {
                        node = node.parentNode;
                            if (node === null) {
                                throw 'not trackable';
                            }
                    }
                    if (node.pathname.indexOf('secure/login.html') > -1 || node.host.indexOf('laneproxy') === 0) {
                        host = (node.search.substring(node.search.indexOf('//') + 2));
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
                    } else if (node.rel && node.rel.indexOf('popup') === 0) {
                        host = document.location.host;
                        path = document.location.pathname;
                        query = document.location.search;
                    } else {
                        host = node.host;
                        if (host.indexOf(':') > -1) {
                            host = host.substring(0, host.indexOf(':'));
                        }
                        path = node.pathname;
                        external = host != document.location.host;
                        query = external ? '' : node.search;
                    }
                }
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                title = getTrackedTitle(node);
				if (LANE.search) {
					searchTerms = LANE.search.getSearchString();
					searchSource = LANE.search.getSearchSource();
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
//                alert('host: '+td.host+'\npath: '+td.path+'\nquery: '+td.query+'\ntitle: '+td.title+'\nsearchTerms: '+td.searchTerms+'\nsearchSource: '+td.searchSource+'\nexternal: '+td.external);
                 for (var i = 0; i < trackers.length; i++) {
                     trackers[i].track(trackingData);
                 }
        },
        isTrackable: function(event){
            var target = event.srcElement || event.target, link, documentHost, linkHost, relTokens;
            documentHost = document.location.host;
			if (documentHost.indexOf(':') > -1) {
				documentHost = documentHost.substring(0, documentHost.indexOf(':'));
			}
            if (event.type == 'click') {
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

//for temporary backwards compatibility:
LANE.track = LANE.tracking;
