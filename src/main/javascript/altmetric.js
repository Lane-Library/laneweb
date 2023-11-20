(function(){

    "use strict";
    let Model = L.Model;

    // show altmetric widgets for authenticated users
    if(Model.get(Model.AUTH) && document.querySelector(".lwSearchResults")){
        L.Get.script("https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js");
        L.Get.script("https://badge.dimensions.ai/badge.js");
        /* add help link/labl to donut
        document.querySelectorAll(".altmetric-embed").forEach(function(badgeNode) {
            badgeNode.addEventListener("altmetric:show", function() {
                badgeNode.insertAdjacentHTML("beforeend",
                    '<a title="What is Altmetric?" href="https://help.altmetric.com/support/solutions/articles/6000232837-what-is-altmetric-and-what-does-it-provide-">Altmetric</a>'
                )
            })
        });
        */
    }

})();
