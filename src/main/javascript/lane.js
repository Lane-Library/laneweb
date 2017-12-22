/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.
 */

YUI({fetchCSS:false}).use("*", function(Y) {

    "use strict";

    //keep a global reference of this YUI object
    window.Y = Y;

    Y.lane = {};

    Y.augment(Y.lane, Y.EventTarget, null, null, {
        prefix : "lane",
        emitFacade : true,
        broadcast : 1
    });

});
/*
 * uses these modules (no longer certain this is a complete list):
        "anim-base",
        "anim-easing",
        "anim-scroll",
        "array-extras",
        "async-queue",
        "attribute-base",
        "attribute-complex",
        "attribute-core",
        "attribute-extras",
        "attribute-observable",
        "autocomplete-base",
        "autocomplete-sources",
        "autocomplete-list",
        "autocomplete-list",
        "autocomplete-list-keys",
        "autocomplete-plugin",
        "base-base",
        "base-build",
        "base-core",
        "base-observable",
        "base-pluginhost",
        "classnamemanager",
        "color-base",
        "console",
        "console",
        "cookie",
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
        "event-simulate",
        "event-synthetic",
        "event-tap",
        "event-touch",
        "event-valuechange",
        "gesture-simulate",
        "history-base",
        "history-hash",
        "intl",
        "io-base",
        "json-parse",
        "json-stringify",
        "node-base",
        "node-core",
        "node-event-delegate",
        "node-event-simulate",
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
        "test",
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

included in these urls:
http://yui.yahooapis.com/combo?3.18.1/oop/oop-min.js&3.18.1/attribute-core/attribute-core-min.js&3.18.1/event-custom-base/event-custom-base-min.js&3.18.1/event-custom-complex/event-custom-complex-min.js&3.18.1/attribute-observable/attribute-observable-min.js&3.18.1/attribute-extras/attribute-extras-min.js&3.18.1/attribute-base/attribute-base-min.js&3.18.1/base-core/base-core-min.js&3.18.1/base-observable/base-observable-min.js&3.18.1/base-base/base-base-min.js&3.18.1/dom-core/dom-core-min.js&3.18.1/dom-base/dom-base-min.js&3.18.1/color-base/color-base-min.js&3.18.1/dom-style/dom-style-min.js&3.18.1/dom-style-ie/dom-style-ie-min.js&3.18.1/event-base/event-base-min.js&3.18.1/selector-native/selector-native-min.js&3.18.1/selector/selector-min.js&3.18.1/node-core/node-core-min.js&3.18.1/node-base/node-base-min.js&3.18.1/event-base-ie/event-base-ie-min.js&3.18.1/node-style/node-style-min.js&3.18.1/anim-base/anim-base-min.js&3.18.1/anim-easing/anim-easing-min.js&3.18.1/anim-scroll/anim-scroll-min.js
http://yui.yahooapis.com/combo?3.18.1/array-extras/array-extras-min.js&3.18.1/base-build/base-build-min.js&3.18.1/escape/escape-min.js&3.18.1/event-synthetic/event-synthetic-min.js&3.18.1/event-focus/event-focus-min.js&3.18.1/event-valuechange/event-valuechange-min.js&3.18.1/autocomplete-base/autocomplete-base-min.js&3.18.1/autocomplete-sources/autocomplete-sources-min.js&3.18.1/intl/intl-min.js&3.18.1/autocomplete-list/lang/autocomplete-list_en.js&3.18.1/event-resize/event-resize-min.js&3.18.1/dom-screen/dom-screen-min.js&3.18.1/node-screen/node-screen-min.js&3.18.1/selector-css2/selector-css2-min.js&3.18.1/selector-css3/selector-css3-min.js&3.18.1/pluginhost-base/pluginhost-base-min.js&3.18.1/pluginhost-config/pluginhost-config-min.js&3.18.1/node-pluginhost/node-pluginhost-min.js&3.18.1/shim-plugin/shim-plugin-min.js&3.18.1/attribute-complex/attribute-complex-min.js&3.18.1/base-pluginhost/base-pluginhost-min.js&3.18.1/classnamemanager/classnamemanager-min.js&3.18.1/widget-base/widget-base-min.js
http://yui.yahooapis.com/combo?3.18.1/widget-base-ie/widget-base-ie-min.js&3.18.1/widget-htmlparser/widget-htmlparser-min.js&3.18.1/widget-skin/widget-skin-min.js&3.18.1/event-delegate/event-delegate-min.js&3.18.1/node-event-delegate/node-event-delegate-min.js&3.18.1/widget-uievents/widget-uievents-min.js&3.18.1/widget-position/widget-position-min.js&3.18.1/widget-position-align/widget-position-align-min.js&3.18.1/autocomplete-list/autocomplete-list-min.js&3.18.1/autocomplete-list-keys/autocomplete-list-keys-min.js&3.18.1/autocomplete-plugin/autocomplete-plugin-min.js&3.18.1/cookie/cookie-min.js&3.18.1/datatype-number-format/datatype-number-format-min.js&3.18.1/yui-throttle/yui-throttle-min.js&3.18.1/dd-ddm-base/dd-ddm-base-min.js&3.18.1/dd-drag/dd-drag-min.js&3.18.1/dd-constrain/dd-constrain-min.js&3.18.1/dd-ddm/dd-ddm-min.js&3.18.1/dd-ddm-drop/dd-ddm-drop-min.js&3.18.1/dd-drop/dd-drop-min.js&3.18.1/dd-proxy/dd-proxy-min.js
http://yui.yahooapis.com/combo?3.18.1/history-base/history-base-min.js&3.18.1/history-hash/history-hash-min.js&3.18.1/querystring-stringify-simple/querystring-stringify-simple-min.js&3.18.1/io-base/io-base-min.js&3.18.1/json-parse/json-parse-min.js&3.18.1/json-stringify/json-stringify-min.js&3.18.1/widget-stdmod/widget-stdmod-min.js&3.18.1/widget-stack/widget-stack-min.js&3.18.1/widget-position-constrain/widget-position-constrain-min.js&3.18.1/overlay/overlay-min.js&3.18.1/plugin/plugin-min.js&3.18.1/querystring-parse-simple/querystring-parse-simple-min.js
http://yui.yahooapis.com/combo?3.18.1/event-mousewheel/event-mousewheel-min.js&3.18.1/event-mouseenter/event-mouseenter-min.js&3.18.1/event-key/event-key-min.js&3.18.1/event-hover/event-hover-min.js&3.18.1/event-outside/event-outside-min.js&3.18.1/event-touch/event-touch-min.js&3.18.1/event-move/event-move-min.js&3.18.1/event-flick/event-flick-min.js&3.18.1/event-tap/event-tap-min.js

 */
