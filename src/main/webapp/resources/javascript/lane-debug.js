/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.  The
 * LANE object is retained for backwards compatiblity
 * and the LANE.search object is created taking place of
 * the previous LANE.namespace function.
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
        "escape",
        "event-base",
        "event-base-ie",
        "event-custom-base",
        "event-custom-complex",
        "event-delegate",
        "event-focus",
        "event-key",
        "event-mouseenter",
        "event-resize",
        "event-synthetic",
        "event-valuechange",
        "gallery-node-accordion",
        "history-base",
        "history-hash",
        "history-hash-ie",
        "io-base",
        "json-parse",
        "json-stringify",
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
    Y.namespace("lane");
    
    //create the LANE.search object that gets used elsewhere
    LANE = {
        search : {}
    };
    
    var i, laneJavascript = [
        "lane-ie.js",
        "lane-mobile-add.js",
        "lane-persistent-login.js",
        "lane-search-result.js",
        "lane-search-indicator.js",
        "lane-bassett.js",
        "lane-textinputs.js",
        "suggest.js",
        "lane-search-pico.js",
        "lane-search.js",
        "link-plugin.js",
        "bookmarks.js",
        "lane-tracking.js",
        "bookmarks-marketing.js",
        "bookmark-instructions.js",
        "lane-expandies.js",
        "lane-google.js",
        "lane-lightbox.js",
        "lane-popup.js",
        "lane-metasearch.js",
        "lane-sarch-facets.js",
        "lane-search-facet-counts.js",
        "lane-search-history.js",
        "lane-tooltips.js",
        "lane-spellcheck.js",
        "lane-findit.js",
        "lane-querymap.js",
        "lane-teletype.js",
        "lane-selections.js",
        "lane-popin.js",
        "lane-quicklinks.js",
        "lane-forms.js",
        "lane-search-reset.js",
        "lane-search-hoverText.js",
        "lane-feedback.js",
        "banner.js",
        "purchase-suggestions.js",
        "menu-delay.js",
        "lane-search-printonly.js"
    ];
    
    //load each javascript file separately
    for (i = 0; i < laneJavascript.length; i++) {
        Y.Get.js("/././resources/javascript/" + laneJavascript[i], function (err) {
            if (err) {
                Y.log('Error loading JS: ' + err[0].error, 'error');
                return;
            }
        });
    }
    
});
