/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({debug:true,filter:"debug",combine:false,fetchCSS:false}).use(
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "attribute-base",
        "autocomplete-sources",
        "autocomplete-plugin",
        "base-base",
        "base-build",
        "cookie",
        "dd-constrain",
        "dd-drag",
        "dd-drop",
        "dd-proxy",
        "event-custom-base",
        "io-base",
        "node-core",
        "oop",
        "overlay",
        "plugin",
        "querystring-parse-simple",
        "widget-base",
        "widget-position",
        "widget-position-align",
        "widget-position-constrain",
        "widget-stack",
        "widget-stdmod",
        function(Y) {

    "use strict";

    //keep a global reference of this YUI object
    window.Y = Y;

    Y.lane = {};

    Y.augment(Y.lane, Y.EventTarget, null, null, {
        prefix : "lane",
        emitFacade : true,
        broadcast : 1
    });

    var i, laneJavascript = [
        "util.js",
        "menu.js",
        "location.js",
        "golfclub-headings.js",
        "model.js",
        "link-plugin.js",
        "lane-ie.js",
        "lane-mobile-ad.js",
        "lane-persistent-login.js",
        "search-indicator.js",
        "lane-bassett.js",
        "suggest.js",
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
        "lane-google.js",
        "lane-popup.js",
        "lane-metasearch.js",
        "lane-search-images.js",
        "lane-tooltips.js",
        "lane-spellcheck.js",
        "lane-findit.js",
        "lane-querymap.js",
        "lane-teletype.js",
        "lane-popin.js",
        "description-toggle.js",
        "telinput.js",
        "lane-feedback.js",
        "purchase-suggestions.js",
        "same-height.js",
        "shibboleth-sfx.js",
        "youtube.js",
        "back-to-top.js",
        "solr-facets.js",
        "solr-pagination.js",
        "bookcovers.js",
        "viewport.js",
        "authors-toggle.js",
        "clinical-toggle.js",
        "search-form-scroll.js"
    ];

    //Model doesn't exist yet, get basePath by hand:
    var basePath = "", errorHandler;

    if (window.model) {
        basePath = window.model["base-path"] || basePath;
    }

    errorHandler = function(err) {
        if (err) {
            console.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
    };

    //load each javascript file separately
    for (i = 0; i < laneJavascript.length; i++) {
        Y.Get.js(basePath + "/resources/javascript/" + laneJavascript[i], errorHandler);
    }

});
