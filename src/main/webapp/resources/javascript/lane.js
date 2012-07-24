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
//http://yui.yahooapis.com/combo?3.5.1/build/yui/yui-min.js&3.5.1/build/anim-base/anim-base-min.js&3.5.1/build/anim-easing/anim-easing-min.js&3.5.1/build/anim-scroll/anim-scroll-min.js&3.5.1/build/base-build/base-build-min.js&3.5.1/build/pluginhost-base/pluginhost-base-min.js&3.5.1/build/pluginhost-config/pluginhost-config-min.js&3.5.1/build/base-pluginhost/base-pluginhost-min.js&3.5.1/build/classnamemanager/classnamemanager-min.js&3.5.1/build/cookie/cookie-min.js&3.5.1/build/dataschema-base/dataschema-base-min.js&3.5.1/build/json-parse/json-parse-min.js&3.5.1/build/json-stringify/json-stringify-min.js&3.5.1/build/dataschema-json/dataschema-json-min.js&3.5.1/build/datasource-local/datasource-local-min.js&3.5.1/build/querystring-stringify-simple/querystring-stringify-simple-min.js&3.5.1/build/io-base/io-base-min.js&3.5.1/build/datasource-io/datasource-io-min.js&3.5.1/build/plugin/plugin-min.js&3.5.1/build/datasource-jsonschema/datasource-jsonschema-min.js&3.5.1/build/datatype-number-format/datatype-number-format-min.js&3.5.1/build/dom-screen/dom-screen-min.js&3.5.1/build/event-delegate/event-delegate-min.js&3.5.1/build/event-synthetic/event-synthetic-min.js&3.5.1/build/event-focus/event-focus-min.js&3.5.1/build/event-mouseenter/event-mouseenter-min.js&3.5.1/build/history-base/history-base-min.js&3.5.1/build/history-hash/history-hash-min.js&3.5.1/build/history-hash-ie/history-hash-ie-min.js&3.5.1/build/node-event-delegate/node-event-delegate-min.js&3.5.1/build/node-pluginhost/node-pluginhost-min.js&3.5.1/build/node-screen/node-screen-min.js&3.5.1/build/widget-base/widget-base-min.js&3.5.1/build/widget-base-ie/widget-base-ie-min.js&3.5.1/build/widget-htmlparser/widget-htmlparser-min.js&3.5.1/build/widget-skin/widget-skin-min.js&3.5.1/build/widget-uievents/widget-uievents-min.js&3.5.1/build/widget-position/widget-position-min.js&3.5.1/build/widget-position-align/widget-position-align-min.js&3.5.1/build/widget-position-constrain/widget-position-constrain-min.js&3.5.1/build/attribute-core/attribute-core-min.js&3.5.1/build/oop/oop-min.js&3.5.1/build/event-custom-base/event-custom-base-min.js&3.5.1/build/event-custom-complex/event-custom-complex-min.js&3.5.1/build/attribute-events/attribute-events-min.js&3.5.1/build/attribute-extras/attribute-extras-min.js&3.5.1/build/attribute-base/attribute-base-min.js&3.5.1/build/attribute-complex/attribute-complex-min.js&3.5.1/build/base-core/base-core-min.js&3.5.1/build/base-base/base-base-min.js&3.5.1/build/dom-core/dom-core-min.js&3.5.1/build/dom-base/dom-base-min.js&3.5.1/build/dom-style/dom-style-min.js&3.5.1/build/dom-style-ie/dom-style-ie-min.js&3.5.1/build/selector-native/selector-native-min.js&3.5.1/build/selector/selector-min.js&3.5.1/build/selector-css2/selector-css2-min.js&3.5.1/build/node-core/node-core-min.js&3.5.1/build/node-base/node-base-min.js&3.5.1/build/event-base/event-base-min.js&3.5.1/build/event-base-ie/event-base-ie-min.js&3.5.1/build/node-style/node-style-min.js&3.5.1/build/widget-stack/widget-stack-min.js&3.5.1/build/widget-stdmod/widget-stdmod-min.js&3.5.1/build/overlay/overlay-min.js&3.5.1/build/querystring-parse-simple/querystring-parse-simple-min.js&gallery-2010.05.21-18-16/build/gallery-node-accordion/gallery-node-accordion-min.js&gallery-2010.05.21-18-16/build/gallery-value-change/gallery-value-change-min.js
/*
 * uses these modules:
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "attribute-base",
        "attribute-complex",
        "attribute-core",
        "attribute-events",
        "attribute-extras",
        "base-base",
        "base-build",
        "base-core",
        "base-pluginhost",
        "classnamemanager",
        "cookie",
        "dataschema-base",
        "dataschema-json",
        "datasource-io",
        "datasource-jsonschema",
        "datasource-local",
        "datatype-number-format",
        "dom-base",
        "dom-core",
        "dom-screen",
        "dom-style-ie",
        "dom-style",
        "event-base-ie",
        "event-base",
        "event-custom-base",
        "event-custom-complex",
        "event-delegate",
        "event-focus",
        "event-mouseenter",
        "event-synthetic",
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
        "selector-native",
        "selector-css2",
        "selector",
        "widget-base-ie",
        "widget-base",
        "widget-htmlparser",
        "widget-position-align",
        "widget-position-constrain",
        "widget-position",
        "widget-skin",
        "widget-stack",
        "widget-stdmod",
        "widget-uievents",
        "gallery-node-accordion",
        "gallery-value-change",
 * 
 */