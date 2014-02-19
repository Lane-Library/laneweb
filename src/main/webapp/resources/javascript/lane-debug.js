/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({debug:true,filter:"debug",combine:false,fetchCSS:false,gallery: 'gallery-2010.05.21-18-16'}).use(
        "intl",
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "array-extras",
        "attribute-base",
        "attribute-complex",
        "attribute-core",
        "attribute-events",
        "attribute-extras",
        "autocomplete-base",
        "autocomplete-list",
        "lang/autocomplete-list_en",
        "autocomplete-list-keys",
        "autocomplete-plugin",
        "autocomplete-sources",
        "base-base",
        "base-build",
        "base-core",
        "base-pluginhost",
        "classnamemanager",
        "cookie",
        "datasource-io",
        "datasource-local",
        "datatype-number-format",
        "dd-constrain",
        "dd-ddm",
        "dd-ddm-base",
        "dd-ddm-drop",
        "dd-drag",
        "dd-drop",
        "dd-proxy",
        "dom-base",
        "dom-core",
        "dom-screen",
        "dom-style",
        "dom-style-ie",
        "dump",
        "escape",
        "event-base",
        "event-base-ie",
        "event-custom-base",
        "event-custom-complex",
        "event-delegate",
        "event-flick",
        "event-focus",
        "event-hover",
        "event-key",
        "event-mouseenter",
        "event-mousewheel",
        "event-move",
        "event-outside",
        "event-resize",
        "event-synthetic",
        "event-tap",
        "event-touch",
        "event-valuechange",
        "gallery-formvalidator",
        "gallery-node-accordion",
        "history-base",
        "history-hash",
        "history-hash-ie",
        "io-base",
        "json-parse",
        "json-parse-shim",
        "json-stringify",
        "json-stringify-shim",
        "node-base",
        "node-core",
        "node-event-delegate",
        "node-pluginhost",
        "node-screen",
        "node-style",
        "oop",
        "overlay",
        "plugin",
        "pluginhost-base",
        "pluginhost-config",
        "querystring-parse-simple",
        "querystring-stringify-simple",
        "selector",
        "selector-css2",
        "selector-css3",
        "selector-native",
        "shim-plugin",
        "widget-base",
        "widget-base-ie",
        "widget-htmlparser",
        "widget-position",
        "widget-position-align",
        "widget-position-constrain",
        "widget-skin",
        "widget-stack",
        "widget-stdmod",
        "widget-uievents",
        "yui-throttle",
        function(Y) {

    //keep a global reference of this YUI object
    window.Y = Y;

    //create the lane namespace
    var lane = Y.namespace("lane");

    Y.augment(lane, Y.EventTarget, null, null, {
        prefix : "lane",
        emitFacade : true,
        broadcast : 1
    });

    var i, laneJavascript = [
        "location.js",
        "model.js",
        "link-plugin.js",
        "lane-ie.js",
        "lane-mobile-ad.js",
        "lane-persistent-login.js",
        "search-indicator.js",
        "lane-bassett.js",
        "lane-textinputs.js",
        "suggest.js",
        "search-select.js",
        "lane-search-pico.js",
        "search.js",
        "bookmark.js",
        "bookmarks.js",
        "bookmarks-widget.js",
        "bookmark-editor.js",
        "bookmarks-editor.js",
        "bookmark-link.js",
        "lane-lightbox.js",
        "bookmark-login.js",
        "lane-tracking.js",
        "bookmark-instructions.js",
        "lane-expandies.js",
        "lane-google.js",
        "lane-popup.js",
        "lane-metasearch.js",
        "lane-search-facets.js",
        "lane-search-facet-counts.js",
        "lane-tooltips.js",
        "lane-spellcheck.js",
        "lane-findit.js",
        "lane-querymap.js",
        "lane-teletype.js",
        "lane-selections.js",
        "lane-popin.js",
        "form-validator.js",
        "hover-controller.js",
        "telinput.js",
        "lane-feedback.js",
        "banner.js",
        "purchase-suggestions.js",
        "menu-delay.js",
        "resource-list-pagination.js"
    ];

    //Model doesn't exist yet, get basePath by hand:
    var basePath = "";

    if (window.model) {
        basePath = window.model["base-path"] || basePath;
    }

    //load each javascript file separately
    for (i = 0; i < laneJavascript.length; i++) {
        Y.Get.js(basePath + "/resources/javascript/" + laneJavascript[i], function (err) {
            if (err) {
                Y.log('Error loading JS: ' + err[0].error, 'error');
                return;
            }
        });
    }

});
