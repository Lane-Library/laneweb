// Google Analytics tracking

$.ajax({
    url: "https://www.google-analytics.com/analytics.js",
    dataType: "script",
    success: function() {
        var model = window.model,
        ipGroup = model['ipgroup'],
        auth = model['auth'],
        trackerId = location.host.match("lane.stanford.edu") ? "UA-3202241-10" : "UA-3203486-11",
        // custom dimension indexes must be configured in the GA admin interface for each property
        // https://support.google.com/analytics/answer/2709829?hl=en&topic=2709827&ctx=topic
        IP_GROUP_DIMENSION = 'dimension1',
        AUTHENTICATED_SESSION_DIMENSION = 'dimension2',
        ga = window.ga;

        window.ga = window.ga || function() {
            ga.q = ga.q || [];
            ga.q.push(arguments);
        };

        ga.l = (new Date()).getTime();

        ga('create', trackerId, 'auto');

        if (ipGroup) {
            ga('set', IP_GROUP_DIMENSION, ipGroup);
        }
        if (auth) {
            ga('set', AUTHENTICATED_SESSION_DIMENSION, auth);
        }
        ga('send', 'pageview');
    }
});

if (typeof ($.LANE.tracking) === "undefined") {
    $.LANE.tracking = {};
}

$.LANE.tracking.isExternal = function(node) {
    if(node.nodeName !== 'A'){
        return false;
    }
    else if (node.pathname.indexOf('secure/apps/proxy/credential') > -1
            || node.host && (node.host.match('^(?:login\\.)?laneproxy.stanford.edu$')
                    || node.host !== document.location.host)) {
        return true;
    }
    return false;
};

$.LANE.tracking.getTrackingTitle = function(node) {
    // if there is a title attribute, use that.
    var title = node.title, img, i;
    // next try alt attribute.
    if (!title) {
        title = node.alt;
    }
    // next look for alt attributes in any child img.
    if (!title) {
        img = node.getElementsByTagName("IMG");
        for (i = 0; i < img.length; i++) {
            if (img[i].alt) {
                title = img[i].alt;
                break;
            }
        }
    }
    if (!title) {
        title = node.textContent;
    }
    if (!title) {
        title = node.innerText;
    }
    if (title) {
        // trim and normalize:
        title = title.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
    }
    if (!title) {
        title = node.href;
    }
    // finally:
    if (!title) {
        title = 'unknown';
    }
    return title;
};

$.LANE.tracking.track = function(event) {

    var handle, anchorNode, getAnchorNode = function(e) {
        var node = e.srcElement || e.target;
        // find parent A for IMG and STRONG nodes if possible
        if(node.nodeName === 'IMG'||node.nodeName === 'STRONG'){
            while (node.nodeName !== 'A') {
                node = node.parentNode;
                if (node === null) {
                    node = e.srcElement || e.target;
                    break;
                }
            }
        }
        return node;
    },
    eventHandlers = {
            click: function(node) {
                var label, basePath = $.LANE.tracking.isExternal(node) ? '/OFFSITE/' : '/ONSITE/';
                if(node.nodeName === 'A' && $(node).parent().attr('rank')){
                    ga('send', 'event', "searchResultClick", $("input[name=qSearch]").val(), $(node).text(), parseInt($(node).parent().attr('rank'),10));
                }
                if (node.nodeName === 'A'||node.nodeName === 'IMG') {
                    ga('send', 'pageview', basePath + encodeURIComponent($.LANE.tracking.getTrackingTitle(node)));
                } else if (node.nodeName === 'H4' && node.parentNode.parentNode.id === 'hours') {
                    label = (node.parentNode.parentNode.className === 'expanded') ? "open" : "close";
                    ga('send', 'pageview', basePath + "hours/" + label);
                }
            },
            vclick: function(node) {
                if (node.parentNode && node.parentNode.className === 'searchTabs' && node.nodeName === 'LI') {
                    ga('send', 'event', "searchTabClick", $(node).text());
                }
            },
            submit: function(node) {
                if (node.nodeName === 'FORM') {
                    ga('send', 'pageview', "/search?source="+$(node).attr('action')+"&"+$(node).serialize());
                }
            },
            autocompleteselect: function(node) {
                ga('send', 'event', "suggestSelect", node.id, decodeURIComponent(node.value));
            }
    };
    anchorNode = getAnchorNode(event);
    // ignore clicks on autocomplete Anchors
    if (!$(anchorNode).parent().parent().hasClass('ui-autocomplete')) {
        handle = eventHandlers[event.type];
        if (handle) {
            handle(anchorNode);
        }
    }
};

$("form").one( "autocompleteselect", function(e) {
    $.LANE.tracking.track(e);
});
$("form").on( "submit", function(e) {
    $.LANE.tracking.track(e);
});
$(document).bind("click", function(e) {
    $.LANE.tracking.track(e);
});
$(document).bind("vclick", function(e) {
    $.LANE.tracking.track(e);
});
$(document).bind('spellSuggestion spellSuggestionClick', function(e, form, searchTerm, suggestion) {
    ga('send', 'event', e.type, form.attr('action'), "term->"+searchTerm+"::suggestion->"+suggestion);
});
