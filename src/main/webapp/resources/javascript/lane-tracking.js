(function() {
    
    var model = Y.lane.Model;
    
        LANE.tracking = function() {
            //TODO more descriptive variable names
            //TODO put conditionals into sub-functions
            //TODO more thorough documentation
            //TODO use 'track' less
            var enabled = true,
            getEventTrackingData = function(event) {
                var link = event.target, category = null, action = null, label = null, value = null;
                while (link && link.get('nodeName') != 'A') {
                    link = link.get('parentNode');
                }
                if (link.ancestor("#favorites") || link.ancestor("#bookmarks") || link.ancestor(".yui3-bookmark-editor-content")) {
                    category = "lane:bookmarkClick";
                    action = model.get("auth");
                    label = LANE.tracking.getTrackedTitle(link);
                }
                if (link.ancestor(".lwSearchResults")) {
                    if (LANE.SearchResult.getSearchTerms()) {
                        category = "lane:searchResultClick";
                        action = LANE.SearchResult.getSearchTerms();
                        label = link.get('textContent');
                        value = parseInt(link.ancestor('ul').get('className').replace(/r-/, ''), 10);
                    } else {
                        category = "lane:browseResultClick";
                        action = document.location.pathname;
                        label = link.get('textContent');
                        value = parseInt(link.ancestor('ul').get('className').replace(/r-/, ''), 10);
                    }
                }
                return {
                    category: category,
                    action: action,
                    label: label,
                    value: value
                };
            },
            getPageviewTrackingData = function(event) {
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
                                	query = path.substring(path.indexOf('?'), path.length);
                                    path = path.substring(0, path.indexOf('?'));
                                }
                                host = host.substring(0, host.indexOf('/'));
                            } else {
                            	query = '';
                                path = '/';
                            }
                            external = true;
                        } else if (node.get('rel') && (node.get('rel').indexOf('popup local') === 0)) {
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
//                            query = external ? '' : node.get('search');
                            query = node.get('search');
                        }
                    }
                }
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                title = LANE.tracking.getTrackedTitle(node);
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
            return {
                disableTracking : function() {
                    enabled = false;
                },
                enableTracking : function () {
                    enabled = true;
                },
                //figures out the title string for a node
                getTrackedTitle: function(node) {
                    //if there is a title attribute, use that.
                    var title = node.get('title'), img, i, rel, relTokens;
                    //if there is rel="popup .." then create a title from it.
                    rel = node.get('rel');
                    if (rel && rel.indexOf('popup') === 0) {
                        relTokens = rel.split(' ');
                        if (relTokens[1] == 'local') {
                            title = 'YUI Pop-up [local]';
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
                isTrackableAsEvent: function(event) {
                    if (!enabled) {
                        return false;
                    }
                    var link = event.target;
                    while (link !== null && link.get('nodeName') != 'A') {
                        link = link.get('parentNode');
                    }
                    if (link) {
                        if (link.ancestor("#favorites") || link.ancestor("#bookmarks") || link.ancestor(".yui3-bookmark-editor-content")) {
                            return true;
                        }
                        if (link.ancestor(".lwSearchResults")) {
                            return true;
                        }
                    }
                    return false;
                },
                isTrackableAsPageview: function(event) {
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
                                if (relTokens[1] == 'local') {
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
                },
                trackClick: function(event) {
                    var trackingData;
                    if (this.isTrackableAsPageview(event)) {
                        trackingData = getPageviewTrackingData(event);
                        Y.fire("lane:trackablePageview", {
                            host: trackingData.host,
                            path: trackingData.path,
                            query: trackingData.query,
                            title: trackingData.title,
                            searchTerms: trackingData.searchTerms,
                            searchSource: trackingData.searchSource,
                            external: trackingData.external
                        });
                    }
                    if (this.isTrackableAsEvent(event)) {
                        trackingData = getEventTrackingData(event);
                        Y.fire("lane:trackableEvent", {
                            category: trackingData.category,
                            action: trackingData.action,
                            label: trackingData.label,
                            value: trackingData.value
                        });
                    }
                }
            };
        }();
        Y.publish("lane:trackableEvent",{
            category:null,
            action:null,
            label:null,
            value: null
          });
        Y.publish("lane:trackablePageview",{
            host: null,
            path: null,
            query: null,
            title: null,
            searchTerms: null,
            searchSource: null,
            external: null
        });
        Y.on('click', function(e) {
            LANE.tracking.trackClick(e);
            //put in a delay for safari to make the tracking request:
            if (Y.UA.webkit && (LANE.tracking.isTrackableAsPageview(e) || LANE.tracking.isTrackableAsEvent(e))) {
                    var t = e.target, f;
                    while (t) {
                        // have safari follow link if it's not:
                        //  - popup or facet
                        //    (can't halt facet click propagation b/c they need to be tracked)
                        if (t.get('href') &&
                                (!t.get('rel') && !t.get('target')) &&
                                !t.get('parentNode').hasClass('searchFacet') ) {
                            f = function() {
                                window.location = t.get('href');
                            };
                            e.preventDefault();
                            setTimeout(f, 200);
                            break;
                        }
                        t = t.get('parentNode');
                    }
            }
        }, document);
        
        //TODO: Tracking bookmarks:addSync here. I'm not sure if this is the best place for it.
        if (Y.lane.BookmarksWidget) {
            Y.lane.BookmarksWidget.get("bookmarks").after("addSync", function(event) {
                Y.fire("lane:trackableEvent", {
                    category: "lane:bookmarkAdd",
                    action: model.get("auth"),
                    label: event.bookmark.getLabel()
                });
            });
        }
        Y.on("suggest:select",  function(event) {
            var action = "";
            //determine whether or not to include search source value.
            if (event.input.get("id") == "searchTerms") {
                action = LANE.Search.getSearchSource();
            }
            Y.fire("lane:trackableEvent", {
                //keep category same as previous event.type:
                category: "lane:suggestSelect",
                action: action,
                label: event.suggestion
            });
        });
        Y.on("lane:quickLinkClick",  function(event) {
            Y.fire("lane:trackableEvent", {
                category: event.type,
                action: event.linkName
            });
        });
        Y.on("lane:searchFormReset",  function(event) {
            Y.fire("lane:trackableEvent", {
                category: event.type,
                action: document.location.pathname
            });
        });
})();
