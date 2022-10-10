(function() {

    "use strict";



    searchResults = document.querySelector("#searchResults");

    //add trigger markup and delegate click events
    if (searchResults) {

        var handleClick = function(node, event) {
            var eresource = node.closest("li"),
                ancestor = node.closest(".hldgsContainer");
            event.preventDefault();
            ancestor.classList.toggle("active");
            L.fire("tracker:trackableEvent", {
                category: "lane:hldgsTrigger",
                action: actionLabel,
                label: eresource.querySelector('.primaryLink').textContent
            });
        };

        searchResults.addEventListener("click", function(event) {
            var node = event.target.closest(".hldgsTrigger");
            if (node) {
                handleClick(node, event);
            }
        });

    }

})();
