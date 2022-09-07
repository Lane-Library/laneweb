(function() {

    "use strict";

    var initializeHoldingsToggles = function() {
        var triggers = document.querySelectorAll(".hldgsTrigger"),
            searchResultCount = searchResults.querySelectorAll('li').length;
        triggers.forEach(function(node) {
            // show holdings table when:
            //  - only one search result OR
            //  - result has only one row present in the holdings table
            // otherwise, show "view all" trigger
            var ancestor = node.closest("div"),
                holdingsRows = ancestor.querySelectorAll(".table-row");
            if (holdingsRows.length == 2 || searchResultCount == 1) {
                ancestor.querySelector('.table-main').style.display = 'table';
            }
        })
    },

    searchResults = document.querySelector("#searchResults");

    //add trigger markup and delegate click events
    if (searchResults) {

        var handleClick = function(node, event) {
            var eresource = node.closest("li"),
                ancestor = node.closest(".hldgsContainer"),
                active = ancestor.classList.contains("active"),
                actionLabel;
            event.preventDefault();
            ancestor.classList.toggle("active");
            if (active) {
                actionLabel = ancestor.querySelector('.hldgsHeader').textContent.trim() + " " + node.textContent + "-- close";
            } else {
                actionLabel = ancestor.querySelector('.hldgsHeader').textContent.trim() + " " + node.textContent + "-- open";
            }
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

        initializeHoldingsToggles();
    }

})();
