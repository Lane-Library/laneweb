(function () {

    "use strict";
    let Model = L.Model;

    // LANEWEB-11247: show altmetric/dimensions widgets on search/browse pages
    // show dimensions badges on public pages
    // altmetric badges require authentication
    if (document.querySelector(".lwSearchResults")) {
        // citation badge JS
        let script = document.createElement('script');
        script.src = "https://badge.dimensions.ai/badge.js";
        document.head.appendChild(script);
        // altmetric badge JS requires selective loading
        // load for Stanford-authenticated (logged-in or IP authenticated) users

        if (Model.get(Model.AUTH) || Model.get(Model.IPGROUP).match('^(SU|SHC|LPCH)$')) {
            let altmetricScript = document.createElement('script');
            altmetricScript.src = "https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js";
            document.head.appendChild(altmetricScript);
        } else {
            document.querySelectorAll('.altmetric-embed').forEach(e => e.remove());
        }
    }

})();
