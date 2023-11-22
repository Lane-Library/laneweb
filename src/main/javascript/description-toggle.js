(function() {

    "use strict";

    var initializeDescriptionToggles = function() {
        var triggers = document.querySelectorAll(".descriptionTrigger");
        triggers.forEach(function(node) {
            if (node.classList.contains("eresource")) {
                node.innerHTML = "<a href=\"#\">Read Full Description <svg><use xlink:href=\"/resources/svg/regular.svg#angle-down\"></use></svg></a>";
            } else if (node.classList.contains("searchContent")) {
                node.innerHTML = "<a href=\"#\">Abstract <svg><use xlink:href=\"/resources/svg/regular.svg#angle-down\"></use></svg></a>";
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
                node.innerHTML = "<a href=\"#\"> Read Full Description <svg><use xlink:href=\"/resources/svg/regular.svg#angle-down\"></use></svg></a>";
            } else if (active && searchContent) {
                node.innerHTML = "<a href=\"#\">Abstract <svg><use xlink:href=\"/resources/svg/regular.svg#angle-down\"></use></svg></a>";
            } else if (!active && eresource) {
                node.innerHTML = "<a href=\"#\"> Read Full Description <svg><use xlink:href=\"/resources/svg/regular.svg#angle-up\"></use></svg></a>";
            } else if (!active && searchContent) {
                node.innerHTML = "<a href=\"#\">Abstract <svg><use xlink:href=\"/resources/svg/regular.svg#angle-up\"></use></svg></a>";
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
