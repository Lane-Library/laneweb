(function() {

    "use strict";

    window.model = window.model || {};

    L.Model = {
        get: function(prop) {
            return window.model[prop];
        },
        set: function(prop, value) {
            window.model[prop] = value;
        },
        //keep these in sync with edu.stanford.irt.laneweb.model.Model
        AUTH : "auth",
        BASE_PATH : "base-path",
        DISASTER_MODE : "disaster-mode",
        IPGROUP : "ipgroup",
        IS_ACTIVE_SUNETID : "isActiveSunetID",
        PROXY_LINKS : "proxy-links",
        URL_ENCODED_SOURCE : "url-encoded-source",
        URL_ENCODED_QUERY : "url-encoded-query"
    };

})();
