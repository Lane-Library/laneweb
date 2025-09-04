(() => {

    "use strict";

    // LANEWEB-11247: show altmetric/dimensions widgets on search pages
    if (document.querySelector(".lwSearchResults")) {
        const Model = L.Model;
        // authentication status: authenticated user or on-site IP range
        const isAuthenticated = Model.get(Model.AUTH) || /^(SU|SHC|LPCH)$/.test(Model.get(Model.IPGROUP));

        /**
         * Creates and appends a script tag to the document head.
         * @param {string} src The URL for the script's src attribute.
         */
        const loadScript = (src) => {
            const script = document.createElement('script');
            script.src = src;
            document.head.appendChild(script);
        };

        // always load Dimensions badges on search results
        loadScript("https://badge.dimensions.ai/badge.js");

        // Altmetric badge JS requires authentication
        if (isAuthenticated) {
            loadScript("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
        } else {
            // otherwise, delete altmetric badge nodes (helps with display)
            document.querySelectorAll('.altmetric-embed').forEach(e => e.remove());
        }
    }
})();
