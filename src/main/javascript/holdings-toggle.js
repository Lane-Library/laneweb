(function() {

    "use strict";


    document.querySelectorAll('.hldgsTrigger').forEach(function(node) {
        node.addEventListener("click", function(event) {
            event.preventDefault();
            var eresource = node.closest("li"),
                ancestor = node.closest(".hldgsContainer"),
                active = ancestor.classList.contains("active"),
                actionLabel;
            ancestor.classList.toggle("active");
            if (active) {
                actionLabel = ancestor.querySelector('.hldgsHeader').textContent.trim() + " -- close";
            } else {
                actionLabel = ancestor.querySelector('.hldgsHeader').textContent.trim() + " -- open";
            }
            L.fire("tracker:trackableEvent", {
                category: "lane:hldgsTrigger",
                action: actionLabel,
                label: eresource.querySelector('.primaryLink').textContent
            });
        });
    })


})();
