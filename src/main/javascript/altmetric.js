(function(){

    "use strict";
    let Model = L.Model;

    // LANEWEB-11247: show altmetric/dimensions widgets on search/browse pages
    // show dimensions badges on public pages
    // altmetric badges require authentication
    if(document.querySelector(".lwSearchResults")){
        L.Get.script("https://badge.dimensions.ai/badge.js");
        if(Model.get(Model.AUTH)){
            L.Get.script("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
        }
    }

})();
