(function(){

    "use strict";
    let Model = L.Model;

    // LANEWEB-11247: show altmetric/dimensions widgets on search/browse pages
    // show dimensions badges on public pages
    // altmetric badges require authentication
    if(document.querySelector(".lwSearchResults")){
        // citation badge JS
        L.Get.script("https://badge.dimensions.ai/badge.js");
        // altmetric badge JS requires selective loading
        // load for Stanford-authenticated (logged-in or IP authenticated) users
        // otherwise, delete altmetric badge nodes (helps with display)
        if(Model.get(Model.AUTH) || Model.get(Model.IPGROUP).match('^(SU|SHC|LPCH)$')){
            L.Get.script("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
        } else {
            document.querySelectorAll('.altmetric-embed').forEach(e => e.remove());
        }
    }

})();
