(function() {

    "use strict";

    var initializeAuthorToggles = function() {
        Y.delegate("click", function(event) {
            var node = event.currentTarget,
            parent = node.get('parentNode');

            event.preventDefault();
            node.set('text','');
            parent.set('text',parent.get('text'));
        }, "#searchResults", ".authorsTrigger");
    };

    //add trigger markup and delegate click events on class "authorsTrigger"
    if (Y.one("#searchResults")) {
        initializeAuthorToggles();
    }

    //reinitialize when content has changed
    Y.lane.on("lane:new-content", function() {
        initializeAuthorToggles();
    });

})();