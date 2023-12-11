(function(){

    "use strict";

    // LANEWEB-11247: show altmetric/dimensions widgets on search/browse pages
    // show dimensions badges on public pages
    // altmetric badges require authentication (enforced in search--browse-common.xsl)
    if(document.querySelector(".lwSearchResults")){
        // citation badge JS
        L.Get.script("https://badge.dimensions.ai/badge.js");
        // altmetric badge JS
        L.Get.script("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
    }

})();
