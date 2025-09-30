{

    "use strict";

    // Ensure the global L namespace exists
    window.L = window.L || {};

    /** @type {string[]} */
    const laneJavascript = [
        "lane.js",
        "util.js",
        "menu.js",
        "model.js",
        "lightbox.js",
        "slideshow.js",
        "guides.js",
        "link-info.js",
        "persistent-login.js",
        "search-indicator.js",
        "bassett.js",
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
        "tracking.js",
        "google-GA4.js",
        "popup.js",
        "tooltips.js",
        "spellcheck.js",
        "shc-portal.js",
        "description-toggle.js",
        "shibboleth-sfx.js",
        "back-to-top.js",
        "solr-date-facet.js",
        "solr-pagination.js",
        "solr-facet-search.js",
        "solr-facet-suggest.js",
        "bookcovers.js",
        "browzine.js",
        "viewport.js",
        "authors-toggle.js",
        "permalink-toggle.js",
        "clinical-toggle.js",
        "search-form-scroll.js",
        "message.js",
        "validation.js",
        "holdings-toggle.js",
        "table-hide-empty-columns.js",
        "tables-search.js",
        "zotero.js",
        "altmetric.js",
        "mobile-navigation.js",
        "sfp-form.js"
    ];

    // Model doesn't exist yet, get basePath by hand
    const basePath = window.model?.["base-path"] ?? "";

    /**
     * Load a single script and resolve when it’s ready.
     *
     * @param {string} src - Full URL of the script to load
     * @returns {Promise<void>}
     */
    const loadScript = (src) => {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = src;
            script.onload = () => resolve();
            script.onerror = () => reject(new Error(`Failed to load script: ${src}`));
            document.head.appendChild(script);
        });
    };

    /**
     * Sequentially load an array of script filenames.
     *
     * @param {string[]} files - Filenames relative to the JS resources dir
     * @returns {Promise<void>}
     */
    const loadAllScriptsSequentially = async () => {
        // Build the base URL once, taking Cypress test path into account.
        let url = `${basePath}/resources/javascript/`;
        if (window.location.pathname.startsWith('/cypress-test')) {
            url += 'instrumented/';
        }

        for (const file of laneJavascript) {
            const scriptSrc = `${url}${file}`;
            await loadScript(scriptSrc);
        }
    };

    loadAllScriptsSequentially().catch((err) => {
        console.error(err);
    });
}
