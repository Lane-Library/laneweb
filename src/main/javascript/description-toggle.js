(function() {

    "use strict";

    var initializeDescriptionToggles = function() {
        var triggers = document.querySelectorAll(".descriptionTrigger");
        triggers.forEach(function(node) {
            if (node.classList.contains("eresource")) {
                node.innerHTML = "<a href=\"#\"><i class=\"fa fa-eye\"></i> View Description <i class=\"fa fa-angle-double-down\"></i></a>";
            } else if (node.classList.contains("searchContent")) {
                node.innerHTML = "<a href=\"#\"><i class=\"fa fa-eye\"></i> Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>";
            }
        });
    };

    //add trigger markup and delegate click events on class "descriptionTrigger"
    if (document.querySelector("#searchResults")) {
        initializeDescriptionToggles();

        document.querySelector(".content").addEventListener("click", function(event) {
            var node = event.target,
                ancestor, active, eresource, searchContent;
            // walk up the tree to find a .descriptionTrigger
            while (node && !node.classList.contains("descriptionTrigger")) {
                node = node.parentNode;
                // document doesn't have classList
                if (node === document) {
                    node = null;
                }
            }
            if (node) {
                eresource = node.classList.contains("eresource");
                searchContent = node.classList.contains("searchContent");
                ancestor = node;
                // walk up the tree to find a li
                while (ancestor && ancestor.nodeName != "LI") {
                    ancestor = ancestor.parentNode;
                }
                active = ancestor.classList.contains("active");
                event.preventDefault();
                ancestor.classList.toggle("active");
                if (active && eresource) {
                    node.innerHTML = "<a href=\"#\"><i class=\"fa fa-eye\"></i> View Description <i class=\"fa fa-angle-double-down\"></i></a>";
                } else if (active && searchContent) {
                    node.innerHTML = "<a href=\"#\"><i class=\"fa fa-eye\"></i> Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>";
                } else if (!active) {
                    node.innerHTML = "<a href=\"#\">close... <i class=\"fa fa-angle-double-up\"></i></a>";
                }
                Y.lane.fire("tracker:trackableEvent", {
                    category: "lane:descriptionTrigger",
                    action: event.target.textContent,
                    label: ancestor.querySelector('.primaryLink').textContent
                });
            }
        });

    }

    //reinitialize when content has changed
    Y.lane.on("lane:new-content", function() {
        initializeDescriptionToggles();
    });

})();
