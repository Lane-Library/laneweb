// Google Analytics tracking
var _gaq = _gaq || [];
if(document.location.host.match("lane.stanford.edu")){
    _gaq.push(['_setAccount', 'UA-3202241-10']);
}
else {
    _gaq.push(['_setAccount', 'UA-3203486-11']);
}
//_gaq.push(['_setLocalServerMode']);
_gaq.push(['_setDomainName', '.stanford.edu']);
_gaq.push(['_trackPageview']);
_gaq.push(['_setVar',LANE.ipGroup]);

(function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
            
(function() {
            
    LANE.encode = function(value) {
        if (encodeURIComponent) {
            return encodeURIComponent(value);
        }
        return escape(value);
    };
    
    LANE.track = function(e) {
        var node = e.srcElement || e.target;
        if (e.type == 'click' && (node.nodeName == 'A'||node.nodeName == 'IMG')) {
            _gaq.push(['_trackPageview', LANE.encode(LANE.getTrackingTitle(node))]);
        }
        else if (e.type == 'submit' && node.nodeName == 'FORM') {
            _gaq.push(['_trackPageview', LANE.encode("/search/"+node.id+"/"+node.elements['q'].value)]);
        }
    };
    
    LANE.getTrackingTitle = function(node) {
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
        // finally:
        if (!title) {
            title = 'unknown';
        }
        return title;
    };
})();
