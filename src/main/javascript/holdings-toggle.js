(function() {

    "use strict";

    var HTML = "<a href=\"#\">View All <i class=\"fa-regular fa-angles-down fa-xs\"></i></a>";

    var initializeHoldingsToggles = function() {
        var triggers = document.querySelectorAll(".hldgsTrigger");
        triggers.forEach(function(node) {
            // show holdings table when only one row present
            // otherwise, show "view all" trigger
            var ancestor = node.closest("div"), 
                holdingsRows = ancestor.querySelectorAll("tbody tr");
            if (holdingsRows.length == 1) {
                ancestor.querySelector('table').style.display = 'block';
                node.style.display = 'none';
            }
            else {
                node.innerHTML = HTML;
            }
        });
    }, searchResults = document.querySelector("#searchResults");

    //add trigger markup and delegate click events
    if (searchResults) {

        var handleClick = function(node, event) {
            var eresource = node.closest("li"),
                ancestor = node.closest("div"),
                active = ancestor.classList.contains("active"),
                direction,
                actionLabel;

            event.preventDefault();
            ancestor.classList.toggle("active");
            if (active) {
                node.innerHTML = HTML;
                ancestor.querySelector('table').style.display = 'none';
                direction = "-- close";
            } else if (!active) {
                node.innerHTML = HTML.replace('-down', '-up');
                ancestor.querySelector('table').style.display = 'block';
                direction = "-- open";
            }
            actionLabel = ancestor.querySelector('.hldgsHeader').textContent.trim() + " " + node.textContent + direction;
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

    //reinitialize when content has changed
    L.on("lane:new-content", function() {
        initializeHoldingsToggles();
    });

})();
