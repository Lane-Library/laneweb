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
    var s, ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
 
(function() {
    if (typeof ($.LANE.tracking) === "undefined") {
        $.LANE.tracking = {};
    }
    
    $.LANE.tracking.decode = function(value) {
        if (decodeURIComponent) {
            return decodeURIComponent(value);
        }
        return unescape(value);
    };
    
    $.LANE.tracking.encode = function(value) {
        if (encodeURIComponent) {
            return encodeURIComponent(value);
        }
        return escape(value);
    };
    
    $.LANE.tracking.track = function(e) {
        var node = e.srcElement || e.target, basePath, label;
        // find parent A for IMG and STRONG nodes if possible
        if(node.nodeName == 'IMG'||node.nodeName == 'STRONG'){
            while (node && node.nodeName != 'A') {
                node = node.parentNode;
                if (node === null) {
                    node = e.srcElement || e.target;
                    break;
                }
            }
        }
        basePath = $.LANE.tracking.isExternal(node) ? '/OFFSITE/' : '/ONSITE/';
        if ($(e.target).parent().parent().hasClass('ui-autocomplete')) {
            // ignore clicks on autocomplete Anchors
            return;
        }
        else if (node.nodeName == 'H4' && node.parentNode.parentNode.id == 'hours') {
            label = (node.parentNode.parentNode.className == 'expanded') ? "open" : "close";
            _gaq.push(['_trackPageview', basePath + "hours/" + label]);
        }
        else if (e.type == 'click' && (node.nodeName == 'A'||node.nodeName == 'IMG')) {
            if(node.nodeName == 'A' && $(node).parent().attr('rank')){
                _gaq.push(['_trackEvent', "searchResultClick", $("input[name=qSearch]").val(), node.textContent, parseInt($(node).parent().attr('rank'),10)]);
            }
            _gaq.push(['_trackPageview', basePath + $.LANE.tracking.encode($.LANE.tracking.getTrackingTitle(node))]);
        }
        else if (e.type == 'submit' && node.nodeName == 'FORM') {
            _gaq.push(['_trackPageview', "/search?source="+$(e.target).attr('action')+"&"+$(e.target).serialize()]);
        }
        // track suggestSelect
        else if (e.type == 'autocompleteselect') {
            _gaq.push(['_trackEvent', "suggestSelect", e.target.id, $.LANE.tracking.decode(node.textContent)]);
        }
    };
    
    $.LANE.tracking.isExternal = function(node) {
        if(node.nodeName != 'A'){
            return false;
        }
        else if (node.pathname.indexOf('secure/apps/proxy/credential') > -1 || node.host.indexOf('laneproxy') === 0 || node.host != document.location.host) {
            return true;
        }
        return false;
    };
    
    $.LANE.tracking.getTrackingTitle = function(node) {
        // if there is a title attribute, use that.
        var title = node.title, img, i = 0;
        // next try alt attribute.
        if (!title) {
            title = node.alt;
        }
        // next look for alt attributes in any child img.
        if (!title) {
            img = node.getElementsByTagName("IMG");
            if (img) {
                for (i = 0; i < img.length; i++) {
                    if (img[i].alt) {
                        title = img[i].alt;
                        break;
                    }
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
})();

$("form").live( "autocompleteselect", function(e, ui) {
    $.LANE.tracking.track(e);
});
$("form").live( "submit", function(e) {
    $.LANE.tracking.track(e);
});
$(document).bind("click", function(e) {
    $.LANE.tracking.track(e);
});
$(document).bind('spellSuggestion spellSuggestionClick', function(e, form, searchTerm, suggestion) {
    _gaq.push(['_trackEvent', e.type, form.attr('action'), "term->"+searchTerm+"::suggestion->"+suggestion]);
});
