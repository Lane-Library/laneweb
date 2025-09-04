(function () {

    "use strict";

    const model = L.Model;
    const searchSource = model.get(model.URL_ENCODED_SOURCE);

    const Tracker = (() => {

        /**
         * Determines if a node's hostname is a Lane Proxy host.
         * @param {HTMLAnchorElement} node - The link element to check.
         * @returns {boolean} - True if the host is a proxy host.
         */
        const isProxyHost = (node) => node.hostname.match('^(?:login\\.)?laneproxy.stanford.edu$');

        /**
         * Checks if a link is a proxy login or a CME redirect.
         * @param {HTMLAnchorElement} link - The link element to check.
         * @returns {boolean} - True if the link is a proxy or CME login.
         */
        const isProxyOrCMELogin = (link) => {
            const { search, pathname } = link;
            return search?.includes("url=") && /(secure\/apps\/proxy\/credential|redirect\/cme)/.test(pathname);
        };

        /**
         * Checks if a link is for a local popup window.
         * @param {HTMLAnchorElement} node - The link element to check.
         * @returns {boolean} - True if the link is a local popup.
         */
        const isLocalPopup = (node) => node.getAttribute("rel")?.startsWith("popup local");

        /**
         * Checks if a link is proxied through laneproxy or a CME login.
         * @param {HTMLAnchorElement} node - The link element to check.
         * @returns {boolean}
         */
        const isProxied = (node) => isProxyOrCMELogin(node) || isProxyHost(node);

        /**
         * Extracts the title from child <img> elements, preferring alt text over the src.
         * @param {HTMLElement} node - The parent element.
         * @returns {string|undefined} - The extracted title or undefined.
         */
        const getTitleFromImg = (node) => {
            for (const img of node.querySelectorAll('img')) {
                const title = img.alt || img.src;
                if (title) {
                    return title;
                }
            }
            return undefined;
        };

        /**
         * Creates tracking data for a click on a search result link.
         * @param {HTMLAnchorElement} link - The clicked search result link.
         * @returns {object} - The tracking data object.
         */
        const getSearchResultsTrackingData = (link) => {
            const trackingData = {};
            const searchTerms = model.get(model.URL_ENCODED_QUERY);
            const listItem = link.closest("li");

            trackingData.value = listItem.dataset.index;
            trackingData.label = link.textContent;

            if (searchTerms) {
                const sid = listItem.dataset.sid;
                const primaryType = listItem.querySelector(".primaryType").textContent;
                trackingData.category = "lane:searchResultClick";
                trackingData.action = decodeURIComponent(searchTerms);
                trackingData.label = `${sid} -> ${primaryType} -> ${trackingData.label}`;
            }
            return trackingData;
        };

        /**
         * Creates tracking data for clicks based on an ancestor element's selector.
         * This handles generic clicks in areas like bookmarks or the footer.
         * @param {HTMLAnchorElement} link - The clicked link.
         * @returns {object} - The tracking data object.
         */
        const getEventTrackingDataByAncestor = (link) => {
            const trackingData = {};
            const handlers = [
                { selector: "#bookmarks", category: "lane:bookmarkClick" },
                { selector: ".bookmark-editor-content", category: "lane:bookmarkClick" },
                { selector: "footer", category: "lane:laneNav-footer" }
            ];

            const handler = handlers.find(h => link.closest(h.selector));

            if (handler) {
                trackingData.category = handler.category;
                if (trackingData.category === "lane:bookmarkClick") {
                    trackingData.action = model.get(model.AUTH);
                    trackingData.label = Tracker.getTrackedTitle(link);
                } else {
                    trackingData.action = link.href;
                    trackingData.label = link.textContent;
                }
            }
            return trackingData;
        };

        /**
         * Creates event tracking data for a variety of click or drag events.
         * @param {Event} event - The DOM event.
         * @returns {object} - The tracking data object.
         */
        const getEventTrackingData = (event) => {
            const link = event.target.closest("a");
            let trackingData = {};

            if (link.closest(".lwSearchResults")) {
                trackingData = getSearchResultsTrackingData(link);
            } else if (link.href.includes('bookmarklet')) {
                if (event.type === "dragend") {
                    trackingData.category = "lane:bookmarkletDrag";
                } else if (event.type === "contextmenu") {
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
        };

        /**
         * Gets the "real" host from a link, unwrapping it from a proxy URL if necessary.
         * @param {HTMLAnchorElement} node - The link element.
         * @returns {string} - The tracked host.
         */
        const getTrackedHost = (node) => {
            if (isProxied(node)) {
                let host = node.search.substring(node.search.indexOf('//') + 2);
                if (host.includes('/')) {
                    host = host.substring(0, host.indexOf('/'));
                }
                return host;
            }
            if (isLocalPopup(node)) {
                return location.host;
            }
            return node.hostname;
        };

        /**
         * Gets the "real" path from a link, unwrapping it from a proxy URL if necessary.
         * @param {HTMLAnchorElement} node - The link element.
         * @returns {string} - The tracked path.
         */
        const getTrackedPath = (node) => {
            let path;
            if (isLocalPopup(node)) {
                path = location.pathname;
            } else if (isProxied(node)) {
                const host = node.search.substring(node.search.indexOf('//') + 2);
                if (host.includes('/')) {
                    path = host.substring(host.indexOf('/'));
                    if (path.includes('?')) {
                        path = path.substring(0, path.indexOf('?'));
                    }
                } else {
                    path = '/';
                }
            } else {
                path = node.pathname;
            }
            return path.startsWith('/') ? path : `/${path}`;
        };

        /**
         * Gets the "real" query string from a link, unwrapping it from a proxy URL.
         * @param {HTMLAnchorElement} node - The link element.
         * @returns {string} - The tracked query string.
         */
        const getTrackedQuery = (node) => {
            if (isProxied(node)) {
                const host = node.search.substring(node.search.indexOf('//') + 2);
                if (host.includes('/')) {
                    const path = host.substring(host.indexOf('/'));
                    if (path.includes('?')) {
                        return path.substring(path.indexOf('?'));
                    }
                }
                return '';
            }
            if (isLocalPopup(node)) {
                return location.search;
            }
            return node.search;
        };

        /**
         * Determines if a link points to an external resource.
         * @param {HTMLAnchorElement} node - The link element.
         * @returns {boolean} - True if the link is external.
         */
        const getTrackedExternal = (node) => {
            if (!node.hostname) {
                return false;
            }
            return isProxied(node) || (node.hostname !== location.host);
        };

        /**
         * Assembles all data for a pageview tracking event.
         * @param {Event} event - The DOM event that triggered the pageview.
         * @returns {object} - The pageview tracking data.
         */
        const getPageviewTrackingData = (event) => {
            let node = event.target.closest("a");
            if (!node) {
                throw new Error('Not a trackable pageview link.');
            }

            const searchTerms = model.get(model.URL_ENCODED_QUERY);
            return {
                host: getTrackedHost(node),
                path: getTrackedPath(node),
                query: getTrackedQuery(node),
                title: Tracker.getTrackedTitle(node),
                searchTerms: decodeURIComponent(searchTerms),
                searchSource: decodeURIComponent(searchSource),
                external: getTrackedExternal(node)
            };
        };

        /**
        * Determines if a click on a local link should be tracked as a pageview.
        * We rely on actual page loads for .html and directory-level pages, so we don't track those as click events.
        * @param {HTMLAnchorElement} link - The link to check.
        * @returns {boolean}
        */
        const isTrackableLocalClick = (link) => {
            const { pathname } = link;
            // Trackable if it's NOT a .html page, a directory index, or a libguide page.
            return !(/\.html$/.test(pathname) || /libguides/.test(pathname) || /\/$/.test(pathname));
        };

        return {
            /**
             * Figures out the most appropriate title string for a trackable node.
             * It checks for title, alt, and image alt/src attributes before falling back to textContent.
             * @param {HTMLElement} node - The element to get a title from.
             * @returns {string} The determined title.
             */
            getTrackedTitle(node) {
                // Prioritize explicit title or alt attributes.
                let title = node.title || node.alt
                    // Then check for titles within child images.
                    || getTitleFromImg(node)
                    // Fallback to the node's text content.
                    || node.textContent
                    // Finally, use 'unknown' if no title can be found.
                    || 'unknown';

                // Add a pop-up indicator to the title if necessary.
                if (isLocalPopup(node)) {
                    title = `YUI Pop-up [local]: ${title}`;
                }
                return title.replace(/\s+/g, ' ').trim();
            },
            /**
             * Checks if an event is trackable as a custom event.
             * @param {Event} event - The DOM event.
             * @returns {boolean}
             */
            isTrackableAsEvent(event) {
                const link = event.target.closest("a");
                if (!link) {
                    return false;
                }
                // Bookmarklet drags/right-clicks and ".seeAll" links are always trackable.
                if (link.closest('.seeAll') || (link.href.includes('bookmarklet') && ["dragend", "contextmenu"].includes(event.type))) {
                    return true;
                }
                // Otherwise, check for the manually-set property.
                return link.isTrackableAsEvent;
            },
            /**
             * Checks if an event is trackable as a pageview.
             * @param {HTMLElement} target - The event target.
             * @returns {boolean}
             */
            isTrackableAsPageview(target) {
                if (target.isTrackableAsPageView) {
                    return true;
                }
                const link = target.closest("a");
                if (link?.href) {
                    // External links are always trackable as pageviews.
                    if (link.hostname !== location.hostname) {
                        return true;
                    }
                    // For local links, apply specific rules.
                    return isTrackableLocalClick(link);
                }
                return false;
            },
            /**
             * The main tracking handler. It determines if an event is a trackable pageview
             * or a custom event and fires the corresponding internal event.
             * NOTE: This must be a 'function' and not an arrow function to preserve `this`
             * context, which is set by `L.addEventTarget`.
             * @param {Event} event - The DOM event.
             */
            trackEvent(event) {
                if (event.type === "click" && this.isTrackableAsPageview(event.target)) {
                    const pageviewData = getPageviewTrackingData(event);
                    this.fire("trackablePageview", pageviewData);
                }

                if (this.isTrackableAsEvent(event)) {
                    const eventData = getEventTrackingData(event);
                    this.fire("trackableEvent", eventData);
                }
            }
        };
    })();

    /**
     * Listen for all clicks on the document.
     */
    document.addEventListener('click', (event) => {
        let target = event.target;
        const isLeftClick = event.button === 0;

        Tracker.trackEvent(event);

        // If the click is trackable, introduce a small delay before navigating.
        // This gives the tracking request time to be sent.
        if (isLeftClick && (Tracker.isTrackableAsPageview(target) || Tracker.isTrackableAsEvent(event))) {
            // Traverse up the DOM to find the link.
            while (target) {
                // Follow the link if it's not a popup or a facet link.
                if (target.href && !target.rel && !target.target && !target.parentNode.classList.contains('searchFacet')) {
                    event.preventDefault();
                    const navigate = () => L.setLocationHref(target.href);
                    setTimeout(navigate, 200);
                    break;
                }
                target = target.parentNode;
            }
        }
    });

    /**
     * Add specific listeners for right-clicking or dragging bookmarklet links.
     */
    document.querySelectorAll("a[href*='bookmarklet']").forEach(node => {
        node.addEventListener("contextmenu", Tracker.trackEvent.bind(Tracker));
        node.addEventListener("dragend", Tracker.trackEvent.bind(Tracker));
    });

    // Make the Tracker object an event emitter, allowing other parts of the application
    // to listen for 'trackablePageview' and 'trackableEvent'.
    L.addEventTarget(Tracker, {
        prefix: "tracker"
    });

    // Mark specific elements as trackable by adding a property to the DOM node.
    // This is a flag used by the tracking logic to identify what to track.
    document.querySelectorAll(".searchFacet a, *[rel^='popup local']").forEach(node => {
        node.isTrackableAsPageView = true;
    });

    document.querySelectorAll("#bookmarks a, .bookmark-editor-content a, .lwSearchResults a, footer a").forEach(node => {
        node.isTrackableAsEvent = true;
    });

})();
