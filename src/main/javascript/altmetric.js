(function(){

    "use strict";
    let Model = L.Model;

    // show altmetric widgets for authenticated users
    if(Model.get(Model.AUTH) && document.querySelector(".lwSearchResults")){
        L.Get.script("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
        L.Get.script("https://badge.dimensions.ai/badge.js");
    }

})();
