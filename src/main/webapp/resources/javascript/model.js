(function() {

    "use strict";

    Y.namespace("lane");

    var Lane = Y.lane,

    Model = function() {
        Model.superclass.constructor.apply(this, arguments);
    };

    Y.extend(Model, Y.Base, {
        //keep this in sync with edu.stanford.irt.laneweb.model.Model
        AUTH : "auth",
        BASE_PATH : "base-path",
        DISASTER_MODE : "disaster-mode",
        IPGROUP : "ipgroup",
        IS_ACTIVE_SUNETID : "isActiveSunetID",
        PROXY_LINKS : "proxy-links",
        URL_ENCODED_SOURCE : "url-encoded-source",
        URL_ENCODED_QUERY : "url-encoded-query"
    }, {
        NAME : "model"
    });

    Lane.Model = new Model();

    Lane.Model.setAttrs(window.model || {});

})();