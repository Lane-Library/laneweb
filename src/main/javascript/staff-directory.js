(function() {

    "use strict";

    if (document.querySelectorAll(".staff-list .slide") !== undefined && document.querySelectorAll(".staff-list .slide").length > 0) {

        document.querySelectorAll(".staff-list .slide").forEach(function(slide) {
            slide.addEventListener("click", function(event) {
                var nodeName = event.target.tagName;
                if ("A" != nodeName && slide.querySelector(".overlay") !== undefined) {
                    slide.querySelector(".overlay").classList.toggle("on");
                    slide.querySelector(".staff-info").classList.toggle("on");
                    event.stopPropagation();
                    event.preventDefault();
                }
            });
        })
    }


})();