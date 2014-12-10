/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({debug:true,filter:"debug",combine:true,fetchCSS:false,gallery: 'gallery-2010.05.21-18-16'}).use(
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "array-extras",
        "async-queue",
        "attribute-base",
        "attribute-complex",
        "attribute-core",
        "attribute-extras",
        "attribute-observable",
        "autocomplete-base",
        "autocomplete-sources",
        "autocomplete-list",
        "autocomplete-list",
        "autocomplete-list-keys",
        "autocomplete-plugin",
        "base-base",
        "base-build",
        "base-core",
        "base-observable",
        "base-pluginhost",
        "classnamemanager",
        "color-base",
        "console",
        "console",
        "cookie",
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
        "event-simulate",
        "event-synthetic",
        "event-tap",
        "event-touch",
        "event-valuechange",
        "gallery-formvalidator",
        "gallery-node-accordion",
        "gesture-simulate",
        "history-base",
        "history-hash",
        "intl",
        "io-base",
        "json-parse",
        "json-stringify",
        "node-base",
        "node-core",
        "node-event-delegate",
        "node-event-simulate",
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
        "test",
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
        "golfclub-headings.js",
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
        "lane-search-images.js",
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
        "same-height.js",
        "shibboleth-sfx.js"
    ];

    //Model doesn't exist yet, get basePath by hand:
    var basePath = "", errorHandler;

    if (window.model) {
        basePath = window.model["base-path"] || basePath;
    }

    errorHandler = function(err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
    };

    //load each javascript file separately
    for (i = 0; i < laneJavascript.length; i++) {
        Y.Get.js(basePath + "/resources/javascript/" + laneJavascript[i], errorHandler);
    }

});
