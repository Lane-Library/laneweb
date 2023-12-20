// SFX kludge: authentication systems set X-Frame-Options to DENY
// clicks on organization IdP links will fail when our discovery page is loaded in a frame (SFX)
// detect this situation and open IdP links in a new page
(function() {

    "use strict";

    let shibbLinks = document.querySelector('#shibboleth-links'), inFrame = function() {
        try {
            return window.self !== window.top;
        } catch (e) {
            return true;
        }
    };
    if (shibbLinks && inFrame()) {
        shibbLinks.querySelectorAll('a').forEach(function(node) {
            if (node.href.match(/idp\.stanford|adfs\.stanfordmed|fs\.stanfordchildrens|sch-sts/)) {
                node.target = '_blank';
            }
        });
    }
})();
