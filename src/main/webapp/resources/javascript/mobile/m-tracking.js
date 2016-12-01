// Google Analytics tracking
var _gaq = _gaq || [], ipGroup;
$.ajax({
    url: model['base-path'] + "/apps/ipGroupFetch",
    dataType: "text",
    success: function(data) {
        if(data) {
            ipGroup = data;
            if(document.location.host.match("lane.stanford.edu")){
                _gaq.push(['_setAccount', 'UA-3202241-10']);
            }
            else {
                _gaq.push(['_setAccount', 'UA-3203486-11']);
            }
            //_gaq.push(['_setLocalServerMode']);
            _gaq.push(['_setDomainName', '.stanford.edu']);
            _gaq.push(['_setVar', ipGroup]);
            _gaq.push(['_setCustomVar', 1, 'ipGroup', ipGroup, 2]);
            _gaq.push(['_trackPageview']);
        }
    }
});

(function() {
    var s, ga = document.createElement('script');
    ga.type = 'text/javascript';
    ga.async = true;
    ga.src = ('https:' === document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(ga, s);
})();

if (typeof ($.LANE.tracking) === "undefined") {
    $.LANE.tracking = {};
}

$.LANE.tracking.isExternal = function(node) {
    if(node.nodeName !== 'A'){
        return false;
    }
    else if (node.pathname.indexOf('secure/apps/proxy/credential') > -1 || node.host && (node.host.match('^(?:login\\.)?laneproxy.stanford.edu$') || node.host !== document.location.host)) {
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

$.LANE.tracking.track = function(e) {

    var handle, node, getNode = function(e) {
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
                    _gaq.push(['_trackEvent', "searchResultClick", $("input[name=qSearch]").val(), $(node).text(), parseInt($(node).parent().attr('rank'),10)]);
                }
                if (node.nodeName === 'A'||node.nodeName === 'IMG') {
                    _gaq.push(['_trackPageview', basePath + encodeURIComponent($.LANE.tracking.getTrackingTitle(node))]);
                } else if (node.nodeName === 'H4' && node.parentNode.parentNode.id === 'hours') {
                    label = (node.parentNode.parentNode.className === 'expanded') ? "open" : "close";
                    _gaq.push(['_trackPageview', basePath + "hours/" + label]);
                }
            },
            vclick: function(node) {
                if (node.parentNode && node.parentNode.className === 'searchTabs' && node.nodeName === 'LI') {
                    _gaq.push(['_trackEvent', "searchTabClick", $(node).text()]);
                }
            },
            submit: function(node) {
                if (node.nodeName === 'FORM') {
                    _gaq.push(['_trackPageview', "/search?source="+$(node).attr('action')+"&"+$(node).serialize()]);
                }
            },
            autocompleteselect: function(node) {
                _gaq.push(['_trackEvent', "suggestSelect", node.id, decodeURIComponent(node.textContent)]);
            }
    };
    node = getNode(e);
    // ignore clicks on autocomplete Anchors
    if (!$(node).parent().parent().hasClass('ui-autocomplete')) {
        handle = eventHandlers[e.type];
        if (handle) {
            handle(node);
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
    _gaq.push(['_trackEvent', e.type, form.attr('action'), "term->"+searchTerm+"::suggestion->"+suggestion]);
});
