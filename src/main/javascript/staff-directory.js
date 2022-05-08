(function() {

    "use strict";

    if (document.querySelectorAll(".staff-list .slide") !== undefined && document.querySelectorAll(".staff-list .slide").length > 0) {

        document.querySelectorAll('div[class ="staff-list"] div[class ="slide"]').forEach(function(slide) {
            slide.addEventListener("click", function(event) {
                var nodeName = event.target.tagName,
                    currentNode = event.currentTarget;
                if ("A" != nodeName && currentNode.querySelector(".overlay") !== undefined) {
                    currentNode.querySelector(".overlay").classList.toggle("on");
                    currentNode.querySelector(".staff-info").classList.toggle("on");
                    event.stopPropagation();
                    event.preventDefault();
                }
            });
        })
    }


})();