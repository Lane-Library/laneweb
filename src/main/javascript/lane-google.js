(function() {

    "use strict";

    var getTrackerId = function () {
        var host = location.host,
            trackerId;
        if (host.match("lane.stanford.edu")) {
            trackerId = "UA-3202241-2";
        } else if (host.match("lane-beta.stanford.edu")) {
            trackerId = "UA-3203486-9";
        } else {
            trackerId = "UA-3203486-2";
        }
        return trackerId;
    },
    GA_MEASUREMENT_ID = getTrackerId(),
    // custom dimension indexes must be configured in the GA admin interface for each property
    // https://support.google.com/analytics/answer/2709829?hl=en&topic=2709827&ctx=topic
    IP_GROUP_DIMENSION = 'dimension1',
    AUTHENTICATED_SESSION_DIMENSION = 'dimension2',
    BOOKMARK_ENABLED_SESSION_DIMENSION = 'dimension3';

    // load analytics.js and add the ga object
    // https://developers.google.com/analytics/devguides/collection/analyticsjs/#the_javascript_measurement_snippet
    L.Get.script("https://www.google-analytics.com/analytics.js", {
        onSuccess: function() {
            var model = L.Model,
                ipGroup = model.get(model.IPGROUP),
                auth = model.get(model.AUTH),
                ga = window.ga;

            window.ga = window.ga || function() {
                ga.q = ga.q || [];
                ga.q.push(arguments);
            };
            ga.l = (new Date()).getTime();

            ga('create', GA_MEASUREMENT_ID, 'auto');

            if (ipGroup) {
                ga('set', IP_GROUP_DIMENSION, ipGroup);
            }
            if (auth) {
                ga('set', AUTHENTICATED_SESSION_DIMENSION, auth);
                if (L.BookmarksWidget && L.BookmarksWidget.get("bookmarks").size() > 0) {
                    ga('set', BOOKMARK_ENABLED_SESSION_DIMENSION, auth);
                }
            }
            ga('send', 'pageview');
        }
    });

    L.on("tracker:trackableEvent",  function(event) {
        window.ga('send', 'event', event.category, event.action, event.label, event.value);
    });

    L.on("tracker:trackablePageview",  function(event) {
        var ga = window.ga;
        if (event.external) {
            if(event.query !== undefined && event.query !== '' ){
                ga('send', 'event', 'lane:offsite', "/OFFSITE-CLICK-EVENT/" + encodeURIComponent(event.title), event.host + event.path + event.query);
            }else{
                ga('send', 'event', 'lane:offsite', "/OFFSITE-CLICK-EVENT/" + encodeURIComponent(event.title), event.host + event.path);
            }
        } else {
            ga('send', 'pageview', '/ONSITE/' + encodeURIComponent(event.title) + '/' + event.path);
        }
    });
})();
