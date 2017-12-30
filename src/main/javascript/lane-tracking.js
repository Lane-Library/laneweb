(function() {

    "use strict";

    var Lane = Y.lane,
        model = Lane.Model,
        searchSource = model.get(model.URL_ENCODED_SOURCE),
        Tracker = function() {
            //TODO more thorough documentation
            var getSearchResultsTrackingData = function(link) {
                var trackingData = {}, list = link.ancestor(".lwSearchResults"),
                    pageStart = Y.one("#pageStart"),
                    searchTerms = model.get(model.URL_ENCODED_QUERY);
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
                    trackingData.action = location.pathname;
                }
                return trackingData;
            },
            getEventTrackingDataByAncestor = function(link) {
                var i, trackingData = {},
                handlers = [
                            {selector:"#bookmarks", category:"lane:bookmarkClick"},
                            {selector:".yui3-bookmark-editor-content", category:"lane:bookmarkClick"},
                            {selector:".lane-nav", category:"lane:laneNav-top"},
                            {selector:"#laneFooter", category:"lane:laneNav-footer"}
                            ];
                for (i = 0; i < handlers.length; i++) {
                    if (Y.lane.ancestor(link, handlers[i].selector)) {
                        trackingData.category = handlers[i].category;
                        if (trackingData.category === "lane:bookmarkClick") {
                            trackingData.action = model.get(model.AUTH);
                            trackingData.label = Tracker.getTrackedTitle(link);
                        } else {
                            trackingData.action = link.href;
                            trackingData.label = link.textContent;
                        }
                        break;
                    }
                }
                return trackingData;
            },
            getEventTrackingData = function(event) {
                var link = Y.lane.ancestor(event.target, "a", true),
                    trackingData = {};
                if (Y.lane.ancestor(link, ".lwSearchResults")) {
                    trackingData = getSearchResultsTrackingData(new Y.Node(link));
                } else if (link.href.match('^javascript:.*bookmarklet.*')) {
                    if ("dragend" === event.type) {
                        trackingData.category = "lane:bookmarkletDrag";
                    }
                    else if ("contextmenu" === event.type) {
                        trackingData.category = "lane:bookmarkletRightClick";
                    }
                    trackingData.action = link.href;
                    trackingData.label = link.title;
                } else if (Y.lane.ancestor(link, ".seeAll")) {
                    trackingData.category = "lane:searchSeeAllClick";
                    trackingData.action = link.search;
                    trackingData.label = Y.lane.ancestor(link, 'li').textContent.replace(/\s+/g,' ').trim();
                } else {
                    trackingData = getEventTrackingDataByAncestor(link);
                }
                return trackingData;
            },
            isProxyHost = function(node) {
                return node.hostname.match('^(?:login\\.)?laneproxy.stanford.edu$');
            },
            isProxyOrCMELogin = function(link) {
                var search = link.search,
                    pathname = link.pathname,
                    returnValue = false;
                if (search && search.indexOf("url=") > -1 && /(secure\/apps\/proxy\/credential|redirect\/cme)/.test(pathname)) {
                    returnValue = true;
                }
                return returnValue;
            },
            isSecureVideo = function(link) {
                var pathname = link.pathname;
                return (pathname && (/\/secure\/edtech\//).test(pathname));
            },
            isLocalPopup = function(node) {
                var rel = node.getAttribute("rel");
                return rel && rel.indexOf("popup local") === 0;
            },
            getTrackedHost = function(node) {
                var host, pathname = node.pathname;
                if (pathname.indexOf("cookiesFetch") > -1) {
                    host = decodeURIComponent(node.search);
                    host = host.substring(host.indexOf("path=") + 6);
                    if (host.indexOf("&") > -1) {
                        host = host.substring(0, host.indexOf("&"));
                    }
                    host = host.substring(host.indexOf("//") + 2);
                    host = host.substring(0, host.indexOf("/"));
                } else if (isProxyOrCMELogin(node) || isProxyHost(node)) {
                    host = (node.search.substring(node.search.indexOf('//') + 2));
                    if (host.indexOf('/') > -1) {
                        host = host.substring(0, host.indexOf('/'));
                    }
                } else if (isLocalPopup(node)) {
                    host = location.host;
                } else {
                    host = node.hostname;
                }
                return host;
            },
            getTrackedPath = function(node) {
                var path, host, pathname = node.pathname;
                if (isLocalPopup(node)) {
                    path = location.pathname;
                } else if (pathname.indexOf('cookiesFetch') > -1) {
                    host = decodeURIComponent(node.search);
                    host = host.substring(host.indexOf("path=") + 6);
                    if (host.indexOf("&") > -1) {
                        host = host.substring(0, host.indexOf("&"));
                    }
                    host = host.substring(host.indexOf("//") + 2);
                    path = host.substring(host.indexOf("/"));
                } else if (isProxyOrCMELogin(node) || isProxyHost(node)) {
                    host = (node.search.substring(node.search.indexOf('//') + 2));
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
                    host = (node.search.substring(node.search.indexOf('//') + 2));
                    if (host.indexOf('/') > -1) {
                        path = host.substring(host.indexOf('/'));
                        if (path.indexOf('?') > -1) {
                            query = path.substring(path.indexOf('?'), path.length);
                        }
                    } else {
                        query = '';
                    }
                } else if (isLocalPopup(node)) {
                    query = location.search;
                } else {
                    query = node.search;
                }
                return query;
            },
            getTrackedExternal = function(node) {
                var external;
                if (!node.hostname) {
                    external = false;
                } else if (isProxyOrCMELogin(node) || isProxyHost(node) || isSecureVideo(node)) {
                    external = true;
                } else {
                    external = node.hostname !== location.host;
                }
                return external;
            },
            getPageviewTrackingData = function(event) {
                var node = event.target,
                    trackingData = {},
                    searchTerms = model.get(model.URL_ENCODED_QUERY);
                if (node.nodeName !== "A" && node.querySelector("a")) {
                    node = node.querySelector("a");
                }
                if (!node.dataset.isTrackableAsPageView) {
                    node = Y.lane.ancestor(node, "a", true);
                    if (!node) {
                        throw 'not trackable';
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
                var isTrackable, pathname = link.pathname;
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
                var i, title, img = node.querySelectorAll('img');
                for (i = 0; i < img.length; i++) {
                    if (img[i].alt) {
                        title = img[i].alt;
                    } else if (img[i].src) {
                        title = img[i].src;
                    }
                    if (title) {
                        break;
                    }
                }
                return title;
            };
            return {
                //figures out the title string for a node
                getTrackedTitle: function(node) {
                    //if there is a title attribute, use that.
                    var title = node.title;
                    //next try alt attribute.
                    if (!title) {
                        title = node.alt;
                    }
                    //next look for alt or src attributes in any child img.
                    if (!title) {
                        title = getTitleFromImg(node);
                    }
                    //next get the text content before any nested markup
                    if (!title) {
                        title = node.textContent;
                    }
                    //finally:
                    if (!title) {
                        title = 'unknown';
                    }
                    if (Y.lane.ancestor(node, ".lane-nav")) {
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
                    var link = Y.lane.ancestor(event.target, "a", true),
                        isTrackable = false;
                    if (link) {
                        // bookmarklet drag or right-click or child of .seeAll
                        if (Y.lane.ancestor(link, '.seeAll') || link.href.match('^javascript:void.*bookmarklet.*') && ("dragend" === event.type || "contextmenu" === event.type) ) {
                            isTrackable = true;
                        } else {
                            isTrackable = link.dataset.isTrackableAsEvent;
                        }
                    }
                    return isTrackable;
                },
                isTrackableAsPageview: function(theLink) {
                    var isTrackable = false,
                        link = theLink;
                    if (link.dataset.isTrackableAsPageView) {
                        isTrackable = true;
                    } else {
                        //find self ancestor that is <a>
                        link = Y.lane.ancestor(link, "a", true);
                        if (link && link.href) {
                            if (link.hostname === location.hostname) {
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

        document.addEventListener('click', function(e) {
            var t = e.target, setLocation = function() {
                Lane.setHref(t.href);
            };
            Tracker.trackEvent(e);
            //put in a delay for safari to make the tracking request:
            if (Y.UA.webkit && (Tracker.isTrackableAsPageview(e.target) || Tracker.isTrackableAsEvent(e))) {
                    while (t) {
                        // have safari follow link if it's not:
                        //  - popup or facet
                        //    (can't halt facet click propagation b/c they need to be tracked)
                        if (t.href &&
                                (!t.rel && !t.target) &&
                                !t.parentNode.classList.contains('searchFacet') ) {
                            e.preventDefault();
                            setTimeout(setLocation, 200);
                            break;
                        }
                        t = t.parentNode;
                    }
            }
        });

        // limit right-click and dragend listener to bookmarklet links
        document.querySelectorAll("a[href*='bookmarklet']").forEach(function(node) {
            node.addEventListener("contextmenu", function(event) {
                Tracker.trackEvent(event);
            });
            node.addEventListener("dragend", function(event) {
                Tracker.trackEvent(event);
            });
        });

        Y.augment(Tracker, Y.EventTarget, null, null, {
            prefix : "tracker",
            emitFacade : true
        });

        Tracker.addTarget(Lane);

        document.querySelectorAll(".searchFacet a, *[rel^='popup local']").forEach(function(node) {
            node.dataset.isTrackableAsPageView = true;
        });
        document.querySelectorAll("a[href*='secure/edtech']").forEach(function(node) {
            node.dataset.isTrackableAsPageView = true;
        });
        document.querySelectorAll("#bookmarks a, .yui3-bookmark-editor-content a, .lwSearchResults a, .lane-nav a, #laneFooter a").forEach(function(node) {
            node.dataset.isTrackableAsEvent = true;
        });
})();
