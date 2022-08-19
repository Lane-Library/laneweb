/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates the L object.
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

    window.L = {};

    var i, laneJavascript = [
        "util.js",
        "model.js",
		"slideshow.js",
		"guides.js",
		"menu.js",
        "link-info.js",
        "lane-ie.js",
        "lane-persistent-login.js",
        "search-indicator.js",
        "lane-bassett.js",
        "suggest.js",
        "search.js",
        "search-reset.js",
        "search-dropdown.js",
        "search-placeholder.js",
        "search-help.js",
        "search-suggest.js",
        "search-pico-toggle.js",
        "search-pico-fields.js",
        "search-pico.js",
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
        "lane-google-GA4.js",
        "lane-popup.js",
        "lane-search-images.js",
        "lane-tooltips.js",
        "lane-spellcheck.js",
        "description-toggle.js",
        "seminars.js",
        "shibboleth-sfx.js",
        "youtube.js",
        "back-to-top.js",
        "solr-facets.js",
        "solr-pagination.js",
        "solr-facet-suggest.js",
        "bookcovers.js",
        "browzine.js",
        "viewport.js",
        "authors-toggle.js",
        "permalink-toggle.js",
        "clinical-toggle.js",
        "search-form-scroll.js",
        "message.js",
        "navigation.js",
        "course-reserves.js",
        "validation.js",
        "holdings-toggle.js",
        "table-hide-empty-columns.js"

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
