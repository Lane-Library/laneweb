(function() {
    
    var Lane = Y.lane,
        location = Lane.Location,
        model = Lane.Model,
        searchTerms = model.get(model.QUERY),
        searchSource = model.get(model.SOURCE),
        Tracker = function() {
            model.on(model.QUERY + "Change", function(event) {
                searchTerms = event.newVal;
            });
            //TODO more descriptive variable names
            //TODO put conditionals into sub-functions
            //TODO more thorough documentation
            //TODO use 'track' less
            var getEventTrackingData = function(event) {
                var link = event.target, category = null, action = null, label = null, value = null;
                while (link && link.get('nodeName') != 'A') {
                    link = link.get('parentNode');
                }
                //TODO: this counts My Bookmarks clicks as well: check if href=/favorites.html and skip?
                if (link.ancestor("#favorites") || link.ancestor("#bookmarks") || link.ancestor(".yui3-bookmark-editor-content")) {
                    category = "lane:bookmarkClick";
                    action = model.get(model.AUTH);
                    label = Tracker.getTrackedTitle(link);
                }
                else if (link.ancestor("#laneNav")) {
                    category = "lane:laneNav-top";
                    action = link.get('href');
                    label = link.get('text');
                }
                else if (link.ancestor("#qlinks")) {
                    category = "lane:quickLinkClick";
                    action = link.get('href');
                    label = link.get('text');
                }
                else if (link.ancestor("#topResources")) {
                    category = "lane:topResources";
                    action = link.get('href');
                    label = link.get('text');
                }
                else if (link.ancestor(".banner-content")) {
                    category = "lane:bannerClick";
                    action = link.get('href');
                    label = link.get('title');
                }
                else if (link.ancestor(".sectionMenu")) {
                    category = "lane:laneNav-sectionMenu";
                    action = link.get('href');
                    label = link.get('text');
                }
                else if (link.ancestor("#laneFooter")) {
                    category = "lane:laneNav-footer";
                    action = link.get('href');
                    label = link.get('text');
                }
                else if (link.ancestor(".lwSearchResults")) {
                	var list = link.ancestor(".lwSearchResults"),
                	    pageStart = Y.one("#pageStart");
                	// pageStart is the value in the pageStart span or 1 if its not there.
                	pageStart = pageStart ? parseInt(pageStart.get("text"), 10) : 1;
                	value = list.all("li").indexOf(link.ancestor("li")) + pageStart;
                    label = link.get('text');
                    if (searchTerms) {
                        category = "lane:searchResultClick";
                        action = searchTerms;
                    } else {
                        category = "lane:browseResultClick";
                        action = location.get("pathname");
                    }
                }
                else if (link.get('href').match('^javascript:.*bookmarklet.*')) {
                    category = "lane:bookmarklet";
                    if ("dragend" == event.type) {
                        category += "Drag";
                    }
                    else if ("contextmenu" == event.type) {
                        category += "RightClick";
                    }
                    action = link.get('href');
                    label = link.get('title');
                }
                return {
                    category: category,
                    action: action,
                    label: label,
                    value: value
                };
            },
            getPageviewTrackingData = function(event) {
                var node = event.target, host, path, query, external, title;
                if (event.type == 'click') {
                    if (node.hasClass('yui3-accordion-item-trigger')) {
                        host = location.get("host");
                        path = location.get("pathname");
                        query = location.get("search");
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
                            host = location.get("host");
                            path = location.get("pathname");
                            query = location.get("search");
                        } else {
                            host = node.get('host');
                            if (host.indexOf(':') > -1) {
                                host = host.substring(0, host.indexOf(':'));
                            }
                            path = node.get('pathname');
                            external = host != location.get("host");
//                            query = external ? '' : node.get('search');
                            query = node.get('search');
                        }
                    }
                }
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                title = Tracker.getTrackedTitle(node);
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
                //figures out the title string for a node
                getTrackedTitle: function(node) {
                    //if there is a title attribute, use that.
                    var title = node.get('title'), img, i, rel, relTokens;
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
                        title = node.get('text');
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
                    //if there is rel="popup .." then add "pop-up" to the title
                    rel = node.get('rel');
                    if (rel && rel.indexOf('popup') === 0) {
                        relTokens = rel.split(' ');
                        if (relTokens[1] == 'local') {
                            title = 'YUI Pop-up [local]: ' + title;
                        }
                    }
                    return title;
                },
                isTrackableAsEvent: function(event) {
                    var link = event.target;
                    while (link !== null && link.get('nodeName') != 'A') {
                        link = link.get('parentNode');
                    }
                    if (link) {
                        // bookmarks
                        if (link.ancestor("#favorites") || link.ancestor("#bookmarks") || link.ancestor(".yui3-bookmark-editor-content")) {
                            return true;
                        }
                        // search results
                        if (link.ancestor(".lwSearchResults")) {
                            return true;
                        }
                        // navigation click events
                        if (link.ancestor("#laneNav") || link.ancestor(".sectionMenu") || link.ancestor("#laneFooter") || link.ancestor("#qlinks") || link.ancestor("#topResources") || link.ancestor(".banner-content")) {
                            return true;
                        }
                        // bookmarklet drag or right-click
                        if (link.get('href').match('^javascript:void.*bookmarklet.*') && ("dragend" == event.type || "contextmenu" == event.type) ) {
                            return true;
                        }
                    }
                    return false;
                },
                isTrackableAsPageview: function(event) {
                    var link, documentHost, linkHost, rel, relTokens;
                    documentHost = location.get("host");
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
                trackEvent: function(event) {
                    var trackingData;
                    if (this.isTrackableAsPageview(event)) {
                        trackingData = getPageviewTrackingData(event);
                        this.fire("trackablePageview", {
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
                        this.fire("trackableEvent", {
                            category: trackingData.category,
                            action: trackingData.action,
                            label: trackingData.label,
                            value: trackingData.value
                        });
                    }
                }
            };
        }();

        Y.on('click', function(e) {
            Tracker.trackEvent(e);
            //put in a delay for safari to make the tracking request:
            if (Y.UA.webkit && (Tracker.isTrackableAsPageview(e) || Tracker.isTrackableAsEvent(e))) {
                    var t = e.target, f;
                    while (t) {
                        // have safari follow link if it's not:
                        //  - popup or facet
                        //    (can't halt facet click propagation b/c they need to be tracked)
                        if (t.get('href') &&
                                (!t.get('rel') && !t.get('target')) &&
                                !t.get('parentNode').hasClass('searchFacet') ) {
                            f = function() {
                                Lane.Location.set("href", t.get('href'));
                            };
                            e.preventDefault();
                            setTimeout(f, 200);
                            break;
                        }
                        t = t.get('parentNode');
                    }
            }
        }, document);

        // limit dragend listener to bookmarklet links
        Y.on('dragend', function(e) {
            Tracker.trackEvent(e);
        }, 'a[href*=bookmarklet]');
        
        // limit right-click listener to bookmarklet links
        Y.on('contextmenu', function(e) {
            Tracker.trackEvent(e);
        }, 'a[href*=bookmarklet]');
        
        //TODO: Tracking bookmarks:addSync here. I'm not sure if this is the best place for it.
        if (Lane.BookmarksWidget) {
            Lane.BookmarksWidget.get("bookmarks").after("addSync", function(event) {
                Tracker.fire("trackableEvent", {
                    category: "lane:bookmarkAdd",
                    action: model.get(model.AUTH),
                    label: event.bookmark.getLabel()
                });
            });
        }
        Y.on("suggest:select",  function(event) {
            var action = "";
            //determine whether or not to include search source value.
            if (event.input.get("id") == "searchTerms") {
                action = Lane.Search.getSearchSource();
            }
            Tracker.fire("trackableEvent", {
                //keep category same as previous event.type:
                category: "lane:suggestSelect",
                action: action,
                label: event.suggestion
            });
        });
        Lane.on("search:reset",  function(event) {
            Tracker.fire("trackableEvent", {
                category: "lane:searchFormReset",
                action: location.get("pathname")
            });
        });
        
        Y.augment(Tracker, Y.EventTarget, null, null, {
            prefix : "tracker",
            emitFacade : true
        });
        
        Tracker.addTarget(Lane);
})();
