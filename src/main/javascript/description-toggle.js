(function() {

    "use strict";

    var initializeDescriptionToggles = function() {
        var triggers = document.querySelectorAll(".descriptionTrigger");
        triggers.forEach(function(node) {
            if (node.classList.contains("eresource")) {
                node.innerHTML = "<a href=\"#\"><i class=\"fa-regular fa-eye\"></i> View Description <i class=\"fa-regular fa-angles-down fa-xs\"></i></a>";
            } else if (node.classList.contains("searchContent")) {
                node.innerHTML = "<a href=\"#\"><i class=\"fa-regular fa-eye\"></i> Preview Abstract <i class=\"fa-regular fa-angles-down fa-xs\"></i></a>";
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
                node.innerHTML = "<a href=\"#\"><i class=\"fa-regular fa-eye\"></i> View Description <i class=\"fa-regular fa-angles-down fa-xs\"></i></a>";
            } else if (active && searchContent) {
                node.innerHTML = "<a href=\"#\"><i class=\"fa-regular fa-eye\"></i> Preview Abstract <i class=\"fa-regular fa-angles-down fa-xs\"></i></a>";
            } else if (!active) {
                node.innerHTML = "<a href=\"#\">close... <i class=\"fa-regular fa-angles-up fa-regular fa-xs\"></i></a>";
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
