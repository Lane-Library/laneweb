(function() {

    "use strict";

    var getTrackerId = function() {
        var host = location.host,
            trackerId;
        if (host.match("lane.stanford.edu")) {
            trackerId = "G-Y6KGXN1JXT";
        } else if (host.match("lane-beta.stanford.edu")) {
            trackerId = "G-RF4JWB6KG5";
        } else {
            trackerId = "G-CRPQYN7JFT";
        }
        return trackerId;
    },

        GA_MEASUREMENT_ID = getTrackerId(),

        // custom dimension indexes must be configured in the GA admin interface for each property
        // https://support.google.com/analytics/answer/2709829?hl=en&topic=2709827&ctx=topic
        LANEWEB_DIMENSION = 'laneweb_dimensions',
        IP_GROUP_DIMENSION = 'dimension1',
        AUTHENTICATED_SESSION_DIMENSION = 'dimension2',
        BOOKMARK_ENABLED_SESSION_DIMENSION = 'dimension3';

    // load analytics.js and add the ga object
    // https://developers.google.com/analytics/devguides/collection/analyticsjs/#the_javascript_measurement_snippet
    L.Get.script("https://www.googletagmanager.com/gtag/js?id=" + GA_MEASUREMENT_ID, {
        onSuccess: function() {
            var model = L.Model, bookmark,
                ipGroup = model.get(model.IPGROUP),
                auth =  model.get(model.AUTH),
                dimensions = new Map();

            window.dataLayer = window.dataLayer || [];
            window.gtag = window.gtag || function() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());

            if (ipGroup) {
                dimensions.set(IP_GROUP_DIMENSION, 'ipGroup');
            }
            if (auth) {
                dimensions.set(AUTHENTICATED_SESSION_DIMENSION, 'auth');
                if (L.BookmarksWidget && L.BookmarksWidget.get("bookmarks").size() > 0) {
                    dimensions.set(BOOKMARK_ENABLED_SESSION_DIMENSION, 'bookmark');
                    bookmark = auth;
                }
            }

            gtag('config', GA_MEASUREMENT_ID, {
			    'custom_map': dimensions
            });

            gtag('event', LANEWEB_DIMENSION, { 'ipGroup': ipGroup , 'auth': auth,  'bookmark': bookmark });


        }
    });

    L.on("tracker:trackableEvent", function(event) {
        window.gtag('event', event.category, {
            'event_action': event.action,
            'event_label': event.label,
            'event_value': event.value
        });
    });

    L.on("tracker:trackablePageview", function(event) {
        if (event.external) {
            if (event.query !== undefined && event.query !== '') {
                window.gtag('event', 'lane:offsite', {
                    'event_action': "/OFFSITE-CLICK-EVENT/" + encodeURIComponent(event.title),
                    'event_label': event.host + event.path + event.query
                });

            } else {
                window.gtag('event', 'lane:offsite', {
                    'event_action': "/OFFSITE-CLICK-EVENT/" + encodeURIComponent(event.title),
                    'event_label': event.host + event.path
                });
            }
        } else {
            window.gtag('event', 'page_view', {
                'page_location':'/ONSITE/' + encodeURIComponent(event.title) + '/' + event.path
            })
        }
    });
})();
