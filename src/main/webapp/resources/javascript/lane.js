/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({fetchCSS:false}).use("*", function(Y) {
    
    //keep a global reference of this YUI object
    window.Y = Y;
    
    //create the lane namespace
    var lane = Y.namespace("lane");
    
    Y.augment(lane, Y.EventTarget, null, null, {
    	prefix : "lane",
    	emitFacade : true,
    	broadcast : 1
    });

});
/*
 * uses these modules: (intl first because autocomplete chokes if it hasn't been added already)
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
        
included in this url:
http://yui.yahooapis.com/combo?3.13.0/yui-base/yui-base-min.js&3.13.0/oop/oop-min.js&3.13.0/event-custom-base/event-custom-base-min.js&3.13.0/event-custom-complex/event-custom-complex-min.js&3.13.0/intl-base/intl-base-min.js&3.13.0/intl/intl-min.js&3.13.0/attribute-core/attribute-core-min.js&3.13.0/attribute-observable/attribute-observable-min.js&3.13.0/attribute-extras/attribute-extras-min.js&3.13.0/attribute-base/attribute-base-min.js&3.13.0/base-core/base-core-min.js&3.13.0/base-observable/base-observable-min.js&3.13.0/base-base/base-base-min.js&3.13.0/features/features-min.js&3.13.0/dom-core/dom-core-min.js&3.13.0/dom-base/dom-base-min.js&3.13.0/color-base/color-base-min.js&3.13.0/dom-style/dom-style-min.js&3.13.0/selector-native/selector-native-min.js&3.13.0/selector/selector-min.js&3.13.0/node-core/node-core-min.js&3.13.0/node-base/node-base-min.js&3.13.0/event-base/event-base-min.js&3.13.0/node-style/node-style-min.js&3.13.0/anim-base/anim-base-min.js&3.13.0/anim-easing/anim-easing-min.js&3.13.0/anim-scroll/anim-scroll-min.js&3.13.0/array-extras/array-extras-min.js&3.13.0/attribute-complex/attribute-complex-min.js&3.13.0/base-build/base-build-min.js&3.13.0/escape/escape-min.js&3.13.0/event-synthetic/event-synthetic-min.js&3.13.0/event-focus/event-focus-min.js&3.13.0/event-valuechange/event-valuechange-min.js&3.13.0/autocomplete-base/autocomplete-base-min.js&3.13.0/autocomplete-sources/autocomplete-sources-min.js&3.13.0/pluginhost-base/pluginhost-base-min.js&3.13.0/pluginhost-config/pluginhost-config-min.js&3.13.0/base-pluginhost/base-pluginhost-min.js&3.13.0/classnamemanager/classnamemanager-min.js&3.13.0/dom-screen/dom-screen-min.js&3.13.0/event-delegate/event-delegate-min.js&3.13.0/event-resize/event-resize-min.js&3.13.0/node-event-delegate/node-event-delegate-min.js&3.13.0/node-pluginhost/node-pluginhost-min.js&3.13.0/node-screen/node-screen-min.js&3.13.0/selector-css2/selector-css2-min.js&3.13.0/selector-css3/selector-css3-min.js&3.13.0/shim-plugin/shim-plugin-min.js&3.13.0/widget-base/widget-base-min.js&3.13.0/widget-htmlparser/widget-htmlparser-min.js&3.13.0/widget-skin/widget-skin-min.js&3.13.0/widget-uievents/widget-uievents-min.js&3.13.0/widget-position/widget-position-min.js&3.13.0/widget-position-align/widget-position-align-min.js&3.13.0/autocomplete-list/lang/autocomplete-list.js&3.13.0/autocomplete-list/lang/autocomplete-list_en.js&3.13.0/autocomplete-list/autocomplete-list-min.js&3.13.0/autocomplete-list-keys/autocomplete-list-keys-min.js&3.13.0/autocomplete-plugin/autocomplete-plugin-min.js&3.13.0/cookie/cookie-min.js&3.13.0/datasource-local/datasource-local-min.js&3.13.0/querystring-stringify-simple/querystring-stringify-simple-min.js&3.13.0/io-base/io-base-min.js&3.13.0/datasource-io/datasource-io-min.js&3.13.0/datatype-number-format/datatype-number-format-min.js&3.13.0/yui-throttle/yui-throttle-min.js&3.13.0/dd-ddm-base/dd-ddm-base-min.js&3.13.0/dd-drag/dd-drag-min.js&3.13.0/dd-constrain/dd-constrain-min.js&3.13.0/dd-ddm/dd-ddm-min.js&3.13.0/dd-ddm-drop/dd-ddm-drop-min.js&3.13.0/dd-drop/dd-drop-min.js&3.13.0/dd-proxy/dd-proxy-min.js&3.13.0/dom-style-ie/dom-style-ie-min.js&3.13.0/event-base-ie/event-base-ie-min.js&3.13.0/event-key/event-key-min.js&3.13.0/event-mouseenter/event-mouseenter-min.js&3.13.0/history-base/history-base-min.js&3.13.0/yui-later/yui-later-min.js&3.13.0/history-hash/history-hash-min.js&3.13.0/history-hash-ie/history-hash-ie-min.js&3.13.0/json-parse/json-parse-min.js&3.13.0/json-stringify/json-stringify-min.js&3.13.0/widget-position-constrain/widget-position-constrain-min.js&3.13.0/widget-stack/widget-stack-min.js&3.13.0/widget-stdmod/widget-stdmod-min.js&3.13.0/overlay/overlay-min.js&3.13.0/plugin/plugin-min.js&3.13.0/querystring-parse-simple/querystring-parse-simple-min.js&3.13.0/widget-base-ie/widget-base-ie-min.js&gallery-2010.06.09-20-45/build/gallery-formvalidator/gallery-formvalidator-min.js&gallery-2010.05.21-18-16/build/gallery-node-accordion/gallery-node-accordion-min.js&3.13.0/event-flick/event-flick-min.js&3.13.0/event-hover/event-hover-min.js&3.13.0/event-mousewheel/event-mousewheel-min.js&3.13.0/event-move/event-move-min.js&3.13.0/event-outside/event-outside-min.js&3.13.0/event-tap/event-tap-min.js&3.13.0/event-touch/event-touch-min.js&3.13.0/json-parse-shim/json-parse-shim-min.js&3.13.0/json-stringify-shim/json-stringify-shim-min.js
 * 
 */