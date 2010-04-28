/**
 * @author ceyates
 */
YUI().add('lane', function(Y) {
    if (typeof LANE == "undefined" || !LANE) {
        /**
         * The LANE global namespace object.  If LANE is already defined, the
         * existing LANE object will not be overwritten so that defined
         * namespaces are preserved.
         * @class LANE
         * @static
         */
        LANE = {};
    }
    LANE.namespace = function() {
        var a = arguments, o = null, i, j, d;
        for (i = 0; i < a.length; i = i + 1) {
            d = a[i].split(".");
            o = LANE;
            // LANE is implied, so it is ignored if it is included
            for (j = (d[0] == "LANE") ? 1 : 0; j < d.length; j = j + 1) {
                o[d[j]] = o[d[j]] || {};
                o = o[d[j]];
            }
        }
        return o;
    };
    LANE.core = LANE.core || function() {
        var m = {}, //the meta element name/values
            meta, //the meta elements
            i;
        //gather the meta elements:
        meta = Y.all('meta');
        for (i = 0; i < meta.size(); i++) {
            m[meta.item(i).get('name')] = meta.item(i).get('content');
        }
        //create the change event:
        Y.publish('lane:change', {broadcast: 2});
        Y.on('click', function(e) {
            if (LANE.tracking) {
                LANE.tracking.trackEvent(e);
                //put in a delay for safari to make the tracking request:
                //TODO: revisit this and make sure it actually is useful
                //            if (Y.UA.webkit && LANE.tracking.isTrackable(e)) {
                //                    t = e.target;
                //                    parent = t;
                //                    while (parent) {
                //                        if (parent.clicked !== undefined) {
                //                            return;
                //                        }
                //                        parent = parent.parentNode;
                //                    }
                //                    while (t) {
                //                        if (t.href && (!t.rel && !t.target)) {
                //                            f = function() {
                //                                window.location = t.href;
                //                            };
                //                            Y.YUI2.util.Event.preventDefault(e);
                //                            setTimeout(f, 200);
                //                            break;
                //                        }
                //                        t = t.parentNode;
                //                    }
                //            }
            }
        }, document);
        return {
            getMetaContent: function(name) {
                return m[name] === undefined ? undefined : m[name];
            }
        };
    }();

}, '1.11.0-SNAPSHOT',{requires:['node']});
