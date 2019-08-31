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

        var handleClick = function(node, event) {
            var eresource = node.classList.contains("eresource"),
                searchContent = node.classList.contains("searchContent"),
                ancestor = node.closest("li"),
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
            L.fire("tracker:trackableEvent", {
                category: "lane:descriptionTrigger",
                action: event.target.textContent,
                label: ancestor.querySelector('.primaryLink').textContent
            });
        };

        document.querySelector("#searchResults").addEventListener("click", function(event) {
            var node = event.target.closest(".descriptionTrigger");
            if (node) {
                handleClick(node, event);
            }
        });

        initializeDescriptionToggles();

    }

    //reinitialize when content has changed
    L.on("lane:new-content", function() {
        initializeDescriptionToggles();
    });

})();
