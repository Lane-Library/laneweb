{

    "use strict";

    window.model = window.model || {};

    // --- define constants with key-value map ---
    const MODEL_CONSTANTS = {
        AUTH: "auth",
        BASE_PATH: "base-path",
        BASE_PROXY_URL: "base-proxy-url",
        BOOKMARKING: "bookmarking",
        BOOKMARKS: "bookmarks",
        IPGROUP: "ipgroup",
        IS_ACTIVE_SUNETID: "isActiveSunetID",
        PROXY_LINKS: "proxy-links",
        URL_ENCODED_SOURCE: "url-encoded-source",
        URL_ENCODED_QUERY: "url-encoded-query"
    };

    // --- define the final L.Model object ---
    L.Model = {
        get: (prop) => window.model[prop],
        set: (prop, value) => { window.model[prop] = value; },

        // spread properties from constants map into the final object
        ...MODEL_CONSTANTS
    };
}
