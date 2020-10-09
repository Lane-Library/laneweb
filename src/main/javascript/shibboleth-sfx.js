// SFX kludge: SHC and LPCH set X-Frame-Options to DENY so clicks on their organization links will fail when our discovery page is loaded in a frame
// detect this situation and make SHC/LPCH clicks take over the parent frame
(function() {

    "use strict";

    var shibbLinks = document.querySelector('#shibboleth-links'), inFrame = function() {
        try {
            return window.self !== window.top;
        } catch (e) {
            return true;
        }
    };
    if (shibbLinks && inFrame()) {
        shibbLinks.querySelectorAll('a').forEach(function(node) {
            if (node.href.match(/idp\.stanford|adfs\.stanfordmed|fs\.stanfordchildrens|sch-sts/)) {
                node.addEventListener("click", function() {
                    top.location.href = this.href;
                });
            }
        });
    }
})();
