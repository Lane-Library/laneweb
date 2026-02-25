(() => {

    "use strict";

    /**
     * SFX kludge: Authentication systems can set X-Frame-Options to DENY,
     * which prevents IdP login pages from loading inside an iframe (e.g., in SFX).
     * This script detects when the page is in a frame and forces specific IdP
     * links to open in a new tab/window.
     */

    /**
     * Determine if the current window is inside an iframe.
     * @returns {boolean} True if the page is in a frame, false otherwise.
     */
    function isInIFrame(win) {
        try {
            return !!win && win.self !== win.top;
        } catch (e) {
            // if an error, it likely means we can't access window.top and are in a cross-origin iframe
            return true;
        }
    }

    function applyShibbolethSfx(doc, win) {
        const d = doc || (typeof document !== 'undefined' ? document : null);
        const w = win || (typeof window !== 'undefined' ? window : null);
        const idpHostPattern = /idp\.stanford|adfs\.stanfordmed|fs\.stanfordchildrens|sch-sts/;
        if (!d || !w) return;

        if (!isInIFrame(w)) return;

        const container = d.querySelector('#shibboleth-links');
        if (!container) return;

        container.querySelectorAll('a').forEach(link => {
            if (idpHostPattern.test(link.href)) {
                link.target = '_blank';
            }
        });

    }

    applyShibbolethSfx(document, window);

    // Expose for Jest testing (Node/CommonJS only)
    if (typeof module !== 'undefined' && module.exports) {
        module.exports = { applyShibbolethSfx, isInIFrame };
    }

})();