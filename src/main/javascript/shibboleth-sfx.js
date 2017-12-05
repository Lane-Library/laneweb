// SFX kludge: SHC and LPCH set X-Frame-Options to DENY so clicks on their organization links will fail when our discovery page is loaded in a frame
// detect this situation and make SHC/LPCH clicks take over the parent frame
(function() {

    "use strict";

    var shibbLinks = Y.one('#shibboleth-links'), inFrame = function() {
        try {
            return window.self !== window.top;
        } catch (e) {
            return true;
        }
    };
    if (shibbLinks && inFrame()) {
        shibbLinks.all('a').each(function(node) {
            if (node.get('href').match(/stanfordmed|sch-sts/)) {
                node.on("click", function() {
                    top.location.href = this.get('href');
                }, this);
            }
        });
    }
})();
