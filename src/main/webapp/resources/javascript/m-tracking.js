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
_gaq.push(['_setCustomVar', 1, 'ipGroup', LANE.ipGroup, 2]);
_gaq.push(['_trackPageview']);

(function() {
    var s, ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
            
(function() {
            
    LANE.encode = function(value) {
        if (encodeURIComponent) {
            return encodeURIComponent(value);
        }
        return escape(value);
    };
    
    LANE.track = function(e) {
        var node = e.srcElement || e.target, basePath;
        // find parent A for IMG nodes if possible
        if(node.nodeName == 'IMG'){
            while (node && node.nodeName != 'A') {
                node = node.parentNode;
                if (node === null) {
                    node = e.srcElement || e.target;
                    break;
                }
            }
        }
        basePath = LANE.isExternal(node) ? '/OFFSITE/' : '/ONSITE/';
        if (e.type == 'click' && (node.nodeName == 'A'||node.nodeName == 'IMG')) {
            _gaq.push(['_trackPageview', basePath + LANE.encode(LANE.getTrackingTitle(node))]);
        }
        else if (e.type == 'submit' && node.nodeName == 'FORM') {
            _gaq.push(['_trackPageview', "/search?source="+node.id+"&q="+LANE.encode(node.elements['q'].value)]);
        }
        // inelegant way to track suggestSelect; if laneSearch, also tracks as a search event
        else if (e.inputElement && node.nodeName == 'LI') {
            _gaq.push(['_trackPageview', "/suggestSelect/"+e.inputElement.form.id+"/"+e.inputElement.id+"/"+LANE.encode(node.textContent)]);
            if("laneSearch" == e.inputElement.form.id){
                _gaq.push(['_trackPageview', "/search?source="+e.inputElement.form.id+"&q="+LANE.encode(node.textContent)]);
            }
        }
    };
    
    LANE.isExternal = function(node) {
        if(node.nodeName != 'A'){
            return false;
        }
        else if (node.pathname.indexOf('secure/apps/proxy/credential') > -1 || node.host.indexOf('laneproxy') === 0 || node.host != document.location.host) {
            return true;
        }
        return false;
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
