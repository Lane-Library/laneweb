(function() {

    "use strict";

    var Lane = Y.lane,
        location = Lane.Location,
        model = Lane.Model,
        searchTerms = model.get(model.URL_ENCODED_QUERY),
        searchSource = model.get(model.URL_ENCODED_SOURCE),
        Tracker = function() {
            model.on(model.URL_ENCODED_QUERY + "Change", function(event) {
                searchTerms = event.newVal;
            });
            //TODO more thorough documentation
            var getSearchResultsTrackingData = function(link) {
                var trackingData = {}, list = link.ancestor(".lwSearchResults"),
                    pageStart = Y.one("#pageStart");
                    // pageStart is the value in the pageStart span or 1 if its not there.
                pageStart = pageStart ? parseInt(pageStart.get("text"), 10) : 1;
                trackingData.value = list.all("li").indexOf(link.ancestor("li")) + pageStart;
                trackingData.label = link.get('text');
                if (searchTerms) {
                    trackingData.category = "lane:searchResultClick";
                    trackingData.action = decodeURIComponent(searchTerms);
                    trackingData.label = link.ancestor("li").one(".primaryType").get('text') + " -> " + trackingData.label;
                } else {
                    trackingData.category = "lane:browseResultClick";
                    trackingData.action = location.get("pathname");
                }
                return trackingData;
            },
            getEventTrackingDataByAncestor = function(link) {
                var i, trackingData = {},
                handlers = [
                            {selector:"#bookmarks", category:"lane:bookmarkClick"},
                            {selector:".yui3-bookmark-editor-content", category:"lane:bookmarkClick"},
                            {selector:".lane-nav", category:"lane:laneNav-top"},
                            {selector:".banner-content", category:"lane:bannerClick"},
                            {selector:"#laneFooter", category:"lane:laneNav-footer"}
                            ];
                for (i = 0; i < handlers.length; i++) {
                    if (link.ancestor(handlers[i].selector)) {
                        trackingData.category = handlers[i].category;
                        if (trackingData.category === "lane:bookmarkClick") {
                            trackingData.action = model.get(model.AUTH);
                            trackingData.label = Tracker.getTrackedTitle(link);
                        } else {
                            trackingData.action = link.get('href');
                            trackingData.label = link.get('text');
                        }
                        break;
                    }
                }
                return trackingData;
            },
            getEventTrackingData = function(event) {
                var link = event.target, trackingData = {};
                while (link && link.get('nodeName') !== 'A') {
                    link = link.get('parentNode');
                }
                if (link.ancestor(".lwSearchResults")) {
                    trackingData = getSearchResultsTrackingData(link);
                } else if (link.get('href').match('^javascript:.*bookmarklet.*')) {
                    if ("dragend" === event.type) {
                        trackingData.category = "lane:bookmarkletDrag";
                    }
                    else if ("contextmenu" === event.type) {
                        trackingData.category = "lane:bookmarkletRightClick";
                    }
                    trackingData.action = link.get('href');
                    trackingData.label = link.get('title');
                } else if (link.ancestor(".seeAll")) {
                    trackingData.category = "lane:searchSeeAllClick";
                    trackingData.action = link.get('search');
                    trackingData.label = link.ancestor('li').get('text').replace(/\s+/g,' ').trim();
                } else {
                    trackingData = getEventTrackingDataByAncestor(link);
                }
                return trackingData;
            },
            isProxyHost = function(node) {
                return node.get('hostname').match('^(?:login\\.)?laneproxy.stanford.edu$');
            },
            isProxyOrCMELogin = function(link) {
                var search = link.get("search"),
                pathname = link.get("pathname"),
                isProxyOrCMELogin = false;
                if (search && search.indexOf("url=") > -1 && /(secure\/apps\/proxy\/credential|redirect\/cme)/.test(pathname)) {
                    isProxyOrCMELogin = true;
                }
                return isProxyOrCMELogin;
            },
            isSecureVideo = function(link) {
                var pathname = link.get("pathname");
                return (pathname && (/\/secure\/edtech\//).test(pathname));
            },
            isLocalPopup = function(node) {
                var rel = node.getAttribute("rel");
                return rel && rel.indexOf("popup local") === 0;
            },
            getTrackedHost = function(node) {
                var host, pathname = node.get("pathname");
                if (pathname.indexOf("cookiesFetch") > -1) {
                    host = decodeURIComponent(node.get('search'));
                    host = host.substring(host.indexOf("path=") + 6);
                    if (host.indexOf("&") > -1) {
                        host = host.substring(0, host.indexOf("&"));
                    }
                    host = host.substring(host.indexOf("//") + 2);
                    host = host.substring(0, host.indexOf("/"));
                } else if (isProxyOrCMELogin(node) || isProxyHost(node)) {
                    host = (node.get('search').substring(node.get('search').indexOf('//') + 2));
                    if (host.indexOf('/') > -1) {
                        host = host.substring(0, host.indexOf('/'));
                    }
                } else if (isLocalPopup(node)) {
                    host = location.get("host");
                } else {
                    host = node.get('hostname');
                }
                return host;
            },
            getTrackedPath = function(node) {
                var path, host, pathname = node.get("pathname");
                if (isLocalPopup(node)) {
                    path = location.get("pathname");
                } else if (pathname.indexOf('cookiesFetch') > -1) {
                    host = decodeURIComponent(node.get('search'));
                    host = host.substring(host.indexOf("path=") + 6);
                    if (host.indexOf("&") > -1) {
                        host = host.substring(0, host.indexOf("&"));
                    }
                    host = host.substring(host.indexOf("//") + 2);
                    path = host.substring(host.indexOf("/"));
                } else if (isProxyOrCMELogin(node) || isProxyHost(node)) {
                    host = (node.get('search').substring(node.get('search').indexOf('//') + 2));
                    if (host.indexOf('/') > -1) {
                        path = host.substring(host.indexOf('/'));
                        if (path.indexOf('?') > -1) {
                            path = path.substring(0, path.indexOf('?'));
                        }
                    } else {
                        path = '/';
                    }
                } else {
                    path = pathname;
                }
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                return path;
            },
            getTrackedQuery = function(node) {
                var query, host, path;
                if (isProxyOrCMELogin(node) || isProxyHost(node)) {
                    host = (node.get('search').substring(node.get('search').indexOf('//') + 2));
                    if (host.indexOf('/') > -1) {
                        path = host.substring(host.indexOf('/'));
                        if (path.indexOf('?') > -1) {
                            query = path.substring(path.indexOf('?'), path.length);
                        }
                    } else {
                        query = '';
                    }
                } else if (isLocalPopup(node)) {
                    query = location.get("search");
                } else {
                    query = node.get('search');
                }
                return query;
            },
            getTrackedExternal = function(node) {
                var external;
                if (!node.get("hostname")) {
                    external = false;
                } else if (isProxyOrCMELogin(node) || isProxyHost(node) || isSecureVideo(node)) {
                    external = true;
                } else {
                    external = node.get("hostname") !== location.get("host");
                }
                return external;
            },
            getPageviewTrackingData = function(event) {
                var node = event.target, trackingData = {};
                if (node.get('nodeName') !== 'A' && node.one('a')) {
                    node = node.one('a');
                }
                if (!node.getData().isTrackableAsPageView) {
                    while (node && node.get('nodeName') !== 'A') {
                        node = node.get('parentNode');
                        if (node === null) {
                            throw 'not trackable';
                        }
                    }
                }
                trackingData.host = getTrackedHost(node);
                trackingData.path = getTrackedPath(node);
                trackingData.query = getTrackedQuery(node);
                trackingData.title = Tracker.getTrackedTitle(node);
                trackingData.searchTerms = decodeURIComponent(searchTerms);
                trackingData.searchSource = decodeURIComponent(searchSource);
                trackingData.external = getTrackedExternal(node);
                return trackingData;
            },
            isTrackableLocalClick = function(link) {
                var isTrackable, pathname = link.get("pathname");
                // rely on page tracking for \.html$ and \/$pages except for cookiesFetch
                if (!(/cookiesFetch/).test(pathname) && (/\.html$/).test(pathname) || (/\/$/).test(pathname)) {
                    isTrackable =  false;
                    //all others fall through to trackable
                } else {
                    isTrackable =  true;
                }
                return isTrackable;
            },
            getTitleFromImg = function(node) {
                var i, title, img = node.all('img');
                if (img) {
                    for (i = 0; i < img.size(); i++) {
                        if (img.item(i).get('alt')) {
                            title = img.item(i).get('alt');
                        } else if (img.item(i).get('src')) {
                            title = img.item(i).get('src');
                        }
                        if (title) {
                            break;
                        }
                    }
                }
                return title;
            };
            return {
                //figures out the title string for a node
                getTrackedTitle: function(node) {
                    //if there is a title attribute, use that.
                    var title = node.get('title');
                    //next try alt attribute.
                    if (!title) {
                        title = node.get('alt');
                    }
                    //next look for alt or src attributes in any child img.
                    if (!title) {
                        title = getTitleFromImg(node);
                    }
                    //next get the text content before any nested markup
                    if (!title) {
                        title = node.get('text');
                    }
                    if (!title) {
                        title = node.get('innerText');
                    }
                    //finally:
                    if (!title) {
                        title = 'unknown';
                    }
                    if (node.ancestor(".lane-nav")) {
                        title = "laneNav: " + title;
                    }
                    //if there is rel="popup local" then add "pop-up" to the title
                    if (isLocalPopup(node)) {
                        title = 'YUI Pop-up [local]: ' + title;
                    }
                    title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
                    return title;
                },
                isTrackableAsEvent: function(event) {
                    var link = event.target, isTrackable = false;
                    while (link !== null && link.get('nodeName') !== 'A') {
                        link = link.get('parentNode');
                    }
                    if (link) {
                        // bookmarklet drag or right-click or child of .seeAll
                        if (link.ancestor('.seeAll') || link.get('href').match('^javascript:void.*bookmarklet.*') && ("dragend" === event.type || "contextmenu" === event.type) ) {
                            isTrackable = true;
                        } else {
                            isTrackable = link.getData().isTrackableAsEvent;
                        }
                    }
                    return isTrackable;
                },
                isTrackableAsPageview: function(link) {
                    var isTrackable = false;
                    if (link.getData().isTrackableAsPageView) {
                        isTrackable = true;
                    } else {
                        //find self ancestor that is <a>
                        if (link.get("nodeName") !== "A") {
                            link = link.ancestor("a");
                        }
                        if (link && link.get("href")) {
                            if (link.get('hostname') === location.get("hostname")) {
                                isTrackable =  isTrackableLocalClick(link);
                            } else {
                                //external reference is trackable
                                isTrackable =  true;
                            }
                        }
                    }
                    return isTrackable;
                },
                trackEvent: function(event) {
                    var trackingData;
                    if (event.type === "click" && this.isTrackableAsPageview(event.target)) {
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
            var t = e.target, setLocation = function() {
                Lane.Location.set("href", t.get('href'));
            };
            Tracker.trackEvent(e);
            //put in a delay for safari to make the tracking request:
            if (Y.UA.webkit && (Tracker.isTrackableAsPageview(e.target) || Tracker.isTrackableAsEvent(e))) {
                    while (t) {
                        // have safari follow link if it's not:
                        //  - popup or facet
                        //    (can't halt facet click propagation b/c they need to be tracked)
                        if (t.get('href') &&
                                (!t.get('rel') && !t.get('target')) &&
                                !t.get('parentNode').hasClass('searchFacet') ) {
                            e.preventDefault();
                            setTimeout(setLocation, 200);
                            break;
                        }
                        t = t.get('parentNode');
                    }
            }
        }, document);

        // limit dragend listener to bookmarklet links
        Y.all('a[href*="bookmarklet"]').on('dragend', function(e) {
            Tracker.trackEvent(e);
        });

        // limit right-click listener to bookmarklet links
        Y.all('a[href*="bookmarklet"]').on('contextmenu', function(e) {
            Tracker.trackEvent(e);
        });

        Y.augment(Tracker, Y.EventTarget, null, null, {
            prefix : "tracker",
            emitFacade : true
        });

        Tracker.addTarget(Lane);

        Y.all(".searchFacet a, *[rel^='popup local']").setData("isTrackableAsPageView", true);
        Y.all("a[href*='secure/edtech']").setData("isTrackableAsPageView", true);
        Y.all("#bookmarks a, .yui3-bookmark-editor-content a, .lwSearchResults a, .lane-nav a, #laneFooter a, .banner-content a").setData("isTrackableAsEvent", true);
})();
