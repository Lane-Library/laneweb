(() => {

    "use strict";

    /**
     * SFX kludge: Authentication systems can set X-Frame-Options to DENY,
     * which prevents IdP login pages from loading inside an iframe (e.g., in SFX).
     * This script detects when the page is in a frame and forces specific IdP
     * links to open in a new tab/window.
     */
    const shibbolethLinksContainer = document.querySelector('#shibboleth-links');

    /**
     * Determine if the current window is inside an iframe.
     * @returns {boolean} True if the page is in a frame, false otherwise.
     */
    const isInsideFrame = () => {
        try {
            return window.self !== window.top;
        } catch (e) {
            // if an error, it likely means we can't access window.top and are in a cross-origin iframe
            return true;
        }
    };

    // guardian clase to exit early if the link container doesn't exist
    // or if it does exist but we're not inside a frame
    if (!shibbolethLinksContainer || !isInsideFrame()) {
        return;
    }

    const idpHostPattern = /idp\.stanford|adfs\.stanfordmed|fs\.stanfordchildrens|sch-sts/;

    shibbolethLinksContainer.querySelectorAll('a').forEach(link => {
        if (idpHostPattern.test(link.href)) {
            link.target = '_blank';
        }
    });

})();
