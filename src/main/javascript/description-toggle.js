(function() {

    "use strict";

    var initializeDescriptionToggles = function() {
        var triggers = Y.all(".descriptionTrigger");
        triggers.each(function(node) {
            if (node.hasClass("eresource")) {
                node.set("innerHTML", "<a href=\"#\"><i class=\"fa fa-eye\"></i> View Description <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (node.hasClass("searchContent")) {
                node.set("innerHTML", "<a href=\"#\"><i class=\"fa fa-eye\"></i> Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>");
            }
        });

        Y.delegate("click", function(event) {
            var node = event.currentTarget,
            ancestor = node.ancestor("li"),
            active = ancestor.hasClass("active"),
            eresource = node.hasClass("eresource"),
            searchContent = node.hasClass("searchContent");

            event.preventDefault();
            ancestor.toggleClass("active");
            if (active && eresource) {
                node.set("innerHTML", "<a href=\"#\"><i class=\"fa fa-eye\"></i> View Description <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (active && searchContent) {
                node.set("innerHTML", "<a href=\"#\"><i class=\"fa fa-eye\"></i> Preview Abstract <i class=\"fa fa-angle-double-down\"></i></a>");
            } else if (!active) {
                node.set("innerHTML", "<a href=\"#\">close... <i class=\"fa fa-angle-double-up\"></i></a>");
            }
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:descriptionTrigger",
                action: event.target.get('textContent'),
                label: ancestor.one('.primaryLink').get('textContent')
            });
        }, ".content", ".descriptionTrigger");
    };

    //add trigger markup and delegate click events on class "descriptionTrigger"
    if (Y.one("#searchResults")) {
        initializeDescriptionToggles();
    }

    //reinitialize when content has changed
    Y.lane.on("lane:new-content", function() {
        initializeDescriptionToggles();
    });

})();
