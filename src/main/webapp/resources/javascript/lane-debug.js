/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({debug:true,filter:"debug",combine:false,fetchCSS:false,gallery: 'gallery-2010.05.21-18-16'}).use(
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "attribute-base",
        "autocomplete-sources",
        "autocomplete-plugin",
        "base-base",
        "base-build",
        "cookie",
        "datatype-number-format",
        "dd-constrain",
        "dd-drag",
        "dd-drop",
        "dd-proxy",
        "event-custom-base",
        "gallery-formvalidator",
        "gallery-node-accordion",
        "history-base",
        "history-hash",
        "io-base",
        "json-parse",
        "json-stringify",
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

    //keep a global reference of this YUI object
    window.Y = Y;

    //create the lane object
    Y.lane = {

        activate: function(node, clazz) {
            node.classList.add("active");
        },

        deactivate: function(node, clazz) {
            node.classList.remove("active");
        },

        getData: function(node, name) {
            return node.dataset[name];
        }
    };

    if (Y.UA.ie) {

        // rewrite lane.getData to use getAttribute()
        if (!document.body.dataset) {
            Y.lane.getData = function(node, name) {
                return node.getAttribute("data-" + name);
            }
        };

        // rewrite lane.activate and lane.deactivate to use className
        if (!document.body.classList) {
            Y.lane.activate = function(node) {
                node.className += " active";
            }
            Y.lane.deactivate = function(node) {
                node.className = node.className.replace(/ active/, "");
            }
        };
    };

    Y.augment(Y.lane, Y.EventTarget, null, null, {
        prefix : "lane",
        emitFacade : true,
        broadcast : 1
    });

    var i, laneJavascript = [
        "lane-ie.js",
        "location.js",
        "golfclub-headings.js",
        "model.js",
        "link-plugin.js",
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
        "description-toggle.js",
        "telinput.js",
        "lane-feedback.js",
        "banner.js",
        "purchase-suggestions.js",
        "menu-delay.js",
        "same-height.js",
        "shibboleth-sfx.js",
        "youtube.js",
        "back-to-top.js",
        "solr-facets.js",
        "solr-pagination.js"
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
