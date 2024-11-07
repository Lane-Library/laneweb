(function () {

    "use strict";

    let model = L.Model,
        searchSource = model.get(model.URL_ENCODED_SOURCE),
        Tracker = function () {
            //TODO more thorough documentation
            let getSearchResultsTrackingData = function (link) {
                let trackingData = {},

                    searchTerms = model.get(model.URL_ENCODED_QUERY);
                trackingData.value = link.closest("li").dataset['index'];
                trackingData.label = link.textContent;
                if (searchTerms) {
                    trackingData.category = "lane:searchResultClick";
                    trackingData.action = decodeURIComponent(searchTerms);
                    trackingData.label = link.closest("li").dataset['sid'] + " -> " + link.closest("li").querySelector(".primaryType").textContent + " -> " + trackingData.label;
                } else {
                    trackingData.category = "lane:browseResultClick";
                    trackingData.action = location.pathname;
                    trackingData.label = link.closest("li").dataset['sid'] + " -> " + link.closest("li").querySelector(".primaryType").textContent + " -> " + trackingData.label;
                }
                return trackingData;
            },
                getEventTrackingDataByAncestor = function (link) {
                    let i, trackingData = {},
                        handlers = [
                            { selector: "#bookmarks", category: "lane:bookmarkClick" },
                            { selector: ".yui3-bookmark-editor-content", category: "lane:bookmarkClick" },
                            { selector: "footer", category: "lane:laneNav-footer" }
                        ];
                    for (i = 0; i < handlers.length; i++) {
                        if (link.closest(handlers[i].selector)) {
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
                getEventTrackingData = function (event) {
                    let link = event.target.closest("a"),
                        trackingData = {};
                    if (link.closest(".lwSearchResults")) {
                        trackingData = getSearchResultsTrackingData(link);
                    } else if (link.href.match('^javascript:.*bookmarklet.*')) {
                        if ("dragend" === event.type) {
                            trackingData.category = "lane:bookmarkletDrag";
                        }
                        else if ("contextmenu" === event.type) {
                            trackingData.category = "lane:bookmarkletRightClick";
                        }
                        trackingData.action = link.href;
                        trackingData.label = link.title;
                    } else if (link.closest(".seeAll")) {
                        trackingData.category = "lane:searchSeeAllClick";
                        trackingData.action = link.search;
                        trackingData.label = link.closest('li').textContent.replace(/\s+/g, ' ').trim();
                    } else {
                        trackingData = getEventTrackingDataByAncestor(link);
                    }
                    return trackingData;
                },
                isProxyHost = function (node) {
                    return node.hostname.match('^(?:login\\.)?laneproxy.stanford.edu$');
                },
                isProxyOrCMELogin = function (link) {
                    let search = link.search,
                        pathname = link.pathname,
                        returnValue = false;
                    if (search && search.indexOf("url=") > -1 && /(secure\/apps\/proxy\/credential|redirect\/cme)/.test(pathname)) {
                        returnValue = true;
                    }
                    return returnValue;
                },
                isSecureVideo = function (link) {
                    let pathname = link.pathname;
                    return (pathname && (/\/secure\/edtech\//).test(pathname));
                },
                isLocalPopup = function (node) {
                    let rel = node.getAttribute("rel");
                    return rel && rel.indexOf("popup local") === 0;
                },
                getTrackedHost = function (node) {
                    let host, pathname = node.pathname;
                    if (isProxyOrCMELogin(node) || isProxyHost(node)) {
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
                getTrackedPath = function (node) {
                    let path, host, pathname = node.pathname;
                    if (isLocalPopup(node)) {
                        path = location.pathname;
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
                getTrackedQuery = function (node) {
                    let query, host, path;
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
                getTrackedExternal = function (node) {
                    let external;
                    if (!node.hostname) {
                        external = false;
                    } else if (isProxyOrCMELogin(node) || isProxyHost(node) || isSecureVideo(node)) {
                        external = true;
                    } else {
                        external = node.hostname !== location.host;
                    }
                    return external;
                },
                getPageviewTrackingData = function (event) {
                    let node = event.target,
                        trackingData = {},
                        searchTerms = model.get(model.URL_ENCODED_QUERY);
                    if (node.nodeName !== "A" && node.querySelector("a")) {
                        node = node.querySelector("a");
                    }
                    if (!node.isTrackableAsPageView) {
                        node = node.closest("a");
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
                isTrackableLocalClick = function (link) {
                    let isTrackable, pathname = link.pathname;
                    // rely on page tracking for \.html$ and \/$pages 
                    if ((/\.html$/).test(pathname) || (/libguides/).test(pathname) || (/\/$/).test(pathname)) {
                        isTrackable = false;
                        //all others fall through to trackable
                    } else {
                        isTrackable = true;
                    }
                    return isTrackable;
                },
                getTitleFromImg = function (node) {
                    let i, title, img = node.querySelectorAll('img');
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
                getTrackedTitle: function (node) {
                    //if there is a title attribute, use that.
                    let title = node.title;
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
                    //if there is rel="popup local" then add "pop-up" to the title
                    if (isLocalPopup(node)) {
                        title = 'YUI Pop-up [local]: ' + title;
                    }
                    title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
                    return title;
                },
                isTrackableAsEvent: function (event) {
                    let link = event.target.closest("a"),
                        isTrackable = false;
                    if (link) {
                        // bookmarklet drag or right-click or child of .seeAll
                        if (link.closest('.seeAll') || link.href.match('^javascript:void.*bookmarklet.*') && ("dragend" === event.type || "contextmenu" === event.type)) {
                            isTrackable = true;
                        } else {
                            isTrackable = link.isTrackableAsEvent;
                        }
                    }
                    return isTrackable;
                },
                isTrackableAsPageview: function (theLink) {
                    let isTrackable = false,
                        link = theLink;
                    if (link.isTrackableAsPageView) {
                        isTrackable = true;
                    } else {
                        //find self ancestor that is <a>
                        link = link.closest("a");
                        if (link && link.href) {
                            if (link.hostname === location.hostname) {
                                isTrackable = isTrackableLocalClick(link);
                            } else {
                                //external reference is trackable
                                isTrackable = true;
                            }
                        }
                    }
                    return isTrackable;
                },
                trackEvent: function (event) {
                    let trackingData;
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

    document.addEventListener('click', function (e) {
        let leftClick = e.button === 0,
            t = e.target,
            setLocation = function () {
                L.setLocationHref(t.href);
            };
        Tracker.trackEvent(e);
        //put in a delay to make the tracking request:
        if (leftClick && (Tracker.isTrackableAsPageview(e.target) || Tracker.isTrackableAsEvent(e))) {
            while (t) {
                // follow link if it's not:
                //  - popup or facet
                //    (can't halt facet click propagation b/c they need to be tracked)
                if (t.href &&
                    (!t.rel && !t.target) &&
                    !t.parentNode.classList.contains('searchFacet')) {
                    e.preventDefault();
                    setTimeout(setLocation, 200);
                    break;
                }
                t = t.parentNode;
            }
        }
    });

    // limit right-click and dragend listener to bookmarklet links
    document.querySelectorAll("a[href*='bookmarklet']").forEach(function (node) {
        node.addEventListener("contextmenu", function (event) {
            Tracker.trackEvent(event);
        });
        node.addEventListener("dragend", function (event) {
            Tracker.trackEvent(event);
        });
    });

    L.addEventTarget(Tracker, {
        prefix: "tracker"
    });

    Tracker.addTarget(L);

    document.querySelectorAll(".searchFacet a, *[rel^='popup local']").forEach(function (node) {
        node.isTrackableAsPageView = true;
    });
    document.querySelectorAll("a[href*='secure/edtech']").forEach(function (node) {
        node.isTrackableAsPageView = true;
    });
    document.querySelectorAll("#bookmarks a, .yui3-bookmark-editor-content a, .lwSearchResults a, footer a").forEach(function (node) {
        node.isTrackableAsEvent = true;
    });
})();
