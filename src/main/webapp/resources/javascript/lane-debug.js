/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.  The
 * LANE object is retained for backwards compatiblity
 * and the LANE.search object is created taking place of
 * the previous LANE.namespace function.
 */

YUI({debug:true,filter:"debug",combine:true,fetchCSS:false,gallery: 'gallery-2010.05.21-18-16'}).use(
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
        "dd-ddm-base",
        "dd-drag",
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
    Y.Get.js('/././resources/javascript/lane-ie.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-mobile-ad.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-persistent-login.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    Y.Get.js('/././resources/javascript/lane-search-result.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-indicator.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-bassett.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-textinputs.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/suggest.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-pico.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/link-plugin.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    Y.Get.js('/././resources/javascript/bookmarks.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    Y.Get.js('/././resources/javascript/lane-tracking.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/bookmarks-marketing.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    Y.Get.js('/././resources/javascript/bookmark-instructions.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    Y.Get.js('/././resources/javascript/lane-expandies.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-google.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-lightbox.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-popup.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-metasearch.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-facets.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-facet-counts.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-history.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-tooltips.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-spellcheck.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-findit.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-querymap.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-teletype.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-selections.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-popin.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-quicklinks.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-forms.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-reset.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-hoverText.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-feedback.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/banner.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/purchase-suggestions.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/menu-delay.js', function (err) {
    if (err) {
        Y.log('Error loading JS: ' + err[0].error, 'error');
        return;
    }
    });
    Y.Get.js('/././resources/javascript/lane-search-printonly.js', function (err) {
        if (err) {
            Y.log('Error loading JS: ' + err[0].error, 'error');
            return;
        }
        });
    
    Y.log('lane-all.js loaded successfully!');
    
});
