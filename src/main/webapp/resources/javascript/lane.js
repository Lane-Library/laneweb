/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.  The
 * LANE object is retained for backwards compatiblity
 * and the LANE.search object is created taking place of
 * the previous LANE.namespace function.
 */

YUI({fetchCSS:false}).use("*", function(Y) {
    
    //keep a global reference of this YUI object
    window.Y = Y;
    
    //create the lane namespace
    Y.namespace("lane");
    
    //create the LANE.search object that gets used elsewhere
    LANE = {
        search : {}
    };
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
        "gallery-formvalidator",
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
        
included in this url:
http://yui.yahooapis.com/combo?3.6.0/build/yui/yui-min.js&3.6.0/build/anim-base/anim-base-min.js&3.6.0/build/intl/intl-min.js&3.6.0/build/anim-easing/anim-easing-min.js&3.6.0/build/anim-scroll/anim-scroll-min.js&3.6.0/build/array-extras/array-extras-min.js&3.6.0/build/attribute-base/attribute-base-min.js&3.6.0/build/attribute-complex/attribute-complex-min.js&3.6.0/build/attribute-core/attribute-core-min.js&3.6.0/build/attribute-events/attribute-events-min.js&3.6.0/build/attribute-extras/attribute-extras-min.js&3.6.0/build/autocomplete-base/autocomplete-base-min.js&3.6.0/build/autocomplete-list/autocomplete-list-min.js&3.6.0/build/autocomplete-list/lang/autocomplete-list_en.js&3.6.0/build/autocomplete-list-keys/autocomplete-list-keys-min.js&3.6.0/build/autocomplete-plugin/autocomplete-plugin-min.js&3.6.0/build/autocomplete-sources/autocomplete-sources-min.js&3.6.0/build/base-base/base-base-min.js&3.6.0/build/base-build/base-build-min.js&3.6.0/build/base-core/base-core-min.js&3.6.0/build/base-pluginhost/base-pluginhost-min.js&3.6.0/build/classnamemanager/classnamemanager-min.js&3.6.0/build/cookie/cookie-min.js&3.6.0/build/datasource-io/datasource-io-min.js&3.6.0/build/datasource-local/datasource-local-min.js&3.6.0/build/datatype-number-format/datatype-number-format-min.js&3.6.0/build/dd-constrain/dd-constrain-min.js&3.6.0/build/dd-ddm/dd-ddm-min.js&3.6.0/build/dd-ddm-base/dd-ddm-base-min.js&3.6.0/build/dd-ddm-drop/dd-ddm-drop-min.js&3.6.0/build/dd-drag/dd-drag-min.js&3.6.0/build/dd-drop/dd-drop-min.js&3.6.0/build/dd-proxy/dd-proxy-min.js&3.6.0/build/dom-base/dom-base-min.js&3.6.0/build/dom-core/dom-core-min.js&3.6.0/build/dom-screen/dom-screen-min.js&3.6.0/build/dom-style/dom-style-min.js&3.6.0/build/dom-style-ie/dom-style-ie-min.js&3.6.0/build/escape/escape-min.js&3.6.0/build/event-base/event-base-min.js&3.6.0/build/event-base-ie/event-base-ie-min.js&3.6.0/build/event-custom-base/event-custom-base-min.js&3.6.0/build/event-custom-complex/event-custom-complex-min.js&3.6.0/build/event-delegate/event-delegate-min.js&3.6.0/build/event-focus/event-focus-min.js&3.6.0/build/event-key/event-key-min.js&3.6.0/build/event-mouseenter/event-mouseenter-min.js&3.6.0/build/event-resize/event-resize-min.js&3.6.0/build/event-synthetic/event-synthetic-min.js&3.6.0/build/event-valuechange/event-valuechange-min.js&gallery-2010.06.09-20-45/build/gallery-formvalidator/gallery-formvalidator-min.js&gallery-2010.05.21-18-16/build/gallery-node-accordion/gallery-node-accordion-min.js&3.6.0/build/history-base/history-base-min.js&3.6.0/build/history-hash/history-hash-min.js&3.6.0/build/history-hash-ie/history-hash-ie-min.js&3.6.0/build/io-base/io-base-min.js&3.6.0/build/json-parse/json-parse-min.js&3.6.0/build/json-stringify/json-stringify-min.js&3.6.0/build/node-base/node-base-min.js&3.6.0/build/node-core/node-core-min.js&3.6.0/build/node-event-delegate/node-event-delegate-min.js&3.6.0/build/node-pluginhost/node-pluginhost-min.js&3.6.0/build/node-screen/node-screen-min.js&3.6.0/build/node-style/node-style-min.js&3.6.0/build/oop/oop-min.js&3.6.0/build/overlay/overlay-min.js&3.6.0/build/plugin/plugin-min.js&3.6.0/build/pluginhost-base/pluginhost-base-min.js&3.6.0/build/pluginhost-config/pluginhost-config-min.js&3.6.0/build/querystring-parse-simple/querystring-parse-simple-min.js&3.6.0/build/querystring-stringify-simple/querystring-stringify-simple-min.js&3.6.0/build/selector/selector-min.js&3.6.0/build/selector-css2/selector-css2-min.js&3.6.0/build/selector-css3/selector-css3-min.js&3.6.0/build/selector-native/selector-native-min.js&3.6.0/build/shim-plugin/shim-plugin-min.js&3.6.0/build/widget-base/widget-base-min.js&3.6.0/build/widget-base-ie/widget-base-ie-min.js&3.6.0/build/widget-htmlparser/widget-htmlparser-min.js&3.6.0/build/widget-position/widget-position-min.js&3.6.0/build/widget-position-align/widget-position-align-min.js&3.6.0/build/widget-position-constrain/widget-position-constrain-min.js&3.6.0/build/widget-skin/widget-skin-min.js&3.6.0/build/widget-stack/widget-stack-min.js&3.6.0/build/widget-stdmod/widget-stdmod-min.js&3.6.0/build/widget-uievents/widget-uievents-min.js&3.6.0/build/yui-throttle/yui-throttle-min.js
 * 
 */