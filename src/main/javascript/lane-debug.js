(function () {

    /*
     * This attaches the Y object with all dependencies to the window
     * so we can use it object globally.  It also creates the L object.
     */


    "use strict";


    window.L = {};

    let i, laneJavascript = [
        "util.js",
        "menu.js",
        "model.js",
        "slideshow.js",
        "guides.js",
        "link-info.js",
        "lane-ie.js",
        "lane-persistent-login.js",
        "search-indicator.js",
        "lane-bassett.js",
        "search.js",
        "search-reset.js",
        "search-dropdown.js",
        "search-placeholder.js",
        "search-help.js",
        "suggest.js",
        "search-pico.js",
        "search-suggest.js",
        "search-pico-fields.js",
        "search-pico-toggle.js",
        "bookmark.js",
        "bookmarks.js",
        "bookmark-animation.js",
        "bookmarks-widget.js",
        "bookmark-editor.js",
        "bookmarks-editor.js",
        "bookmark-link.js",
        "bookmark-animation.js",
        "bookmark-login.js",
        "lane-tracking.js",
        "lane-google-GA4.js",
        "lane-popup.js",
        "lane-tooltips.js",
        "lane-spellcheck.js",
        "shc-portal.js",
        "description-toggle.js",
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
        "table-hide-empty-columns.js",
        "zotero.js",
        "altmetric.js"

    ];

    //Model doesn't exist yet, get basePath by hand:
    let basePath = "", errorHandler;

    if (window.model) {
        basePath = window.model["base-path"] || basePath;
    }

    errorHandler = function (err) {
        if (err) {
            console.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
    };


    function loadScriptsSequentially(scripts) {
        if (scripts.length === 0) {
            return;
        }

        const script = document.createElement('script');
        script.src = basePath + "/resources/javascript/" + scripts[0];

        script.onload = () => {
            loadScriptsSequentially(scripts.slice(1));
        };
        document.head.appendChild(script);
    }

    loadScriptsSequentially(laneJavascript);

})();
