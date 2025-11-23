(() => {

    "use strict";

    const DEFAULT_TRACKER_ID = 'G-CRPQYN7JFT';
    const TRACKER_CONFIG = new Map([
        ['lane.stanford.edu', 'G-Y6KGXN1JXT'],
        ['lane-beta.stanford.edu', 'G-RF4JWB6KG5'],
    ]);
    const GA_MEASUREMENT_ID = TRACKER_CONFIG.get(location.host) || DEFAULT_TRACKER_ID;

    // Custom dimension indexes must be configured in the GA admin interface for each property
    // https://support.google.com/analytics/answer/2709829?hl=en&topic=2709827&ctx=topic
    const LANEWEB_DIMENSION = "laneweb_dimensions";
    const IP_GROUP_DIMENSION = "dimension1";
    const AUTHENTICATED_SESSION_DIMENSION = "dimension2";
    const BOOKMARK_ENABLED_SESSION_DIMENSION = "dimension3";

    // load analytics.js and add the ga object
    // https://developers.google.com/analytics/devguides/collection/analyticsjs/#the_javascript_measurement_snippet
    const script = document.createElement('script');
    script.src = `https://www.googletagmanager.com/gtag/js?id=${GA_MEASUREMENT_ID}`;
    script.onload = () => {
        const { get, IPGROUP, AUTH } = L.Model;
        const ipGroup   = get(IPGROUP);
        const auth      = get(AUTH);
        let bookmark;

        const dimensions = new Map();

        if (ipGroup) {
            dimensions.set(IP_GROUP_DIMENSION, 'ipGroup');
        }
        if (auth) {
            dimensions.set(AUTHENTICATED_SESSION_DIMENSION, 'auth');
            if (L.BookmarksWidget?.bookmarks?.size() > 0) {
                dimensions.set(BOOKMARK_ENABLED_SESSION_DIMENSION, 'bookmark');
                bookmark = auth;
            }
        }

        window.dataLayer = window.dataLayer || [];
        // do not use arrow function here, it breaks gtag which expects an "array-like" arguments object
        window.gtag = window.gtag || function () {
            dataLayer.push(arguments);
        }

        // Initialize gtag
        gtag('js', new Date());

        gtag('config', GA_MEASUREMENT_ID, {
            // gtag expects a plain object, not a Map, so convert it
            'custom_map': Object.fromEntries(dimensions)
        });

        gtag('event', LANEWEB_DIMENSION, { ipGroup, auth, bookmark });
    };
    document.head.appendChild(script);

    L.on("tracker:trackableEvent", ({ category, action, label, value }) => {
        if (window.gtag) {
            window.gtag('event', category, {
                'event_action': action,
                'event_label': label,
                'event_value': value
            });
        }
    });

    L.on("tracker:trackablePageview", (event) => {
        const { external, query, title, host, path } = event;
        if (window.gtag && external) {
            window.gtag('event', 'lane:offsite', {
                'event_action': `/OFFSITE-CLICK-EVENT/${encodeURIComponent(title)}`,
                'event_label': `${host}${path}${query || ''}`
            });
        } else if (window.gtag) {
            window.gtag('event', 'page_view', {
                'page_location': `/ONSITE/${encodeURIComponent(title)}/${path}`
            })
        }
    });

})();
