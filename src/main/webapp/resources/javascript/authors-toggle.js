(function() {

    "use strict";

    var initializeAuthorToggles = function() {
        Y.all('.authorsTrigger').each(function(node) {
            if (!node.getData().authorsTriggerSubscribed) {
                node.setData('authorsTriggerSubscribed',true);
                node.on('click', function(event) {
                    var node = event.currentTarget,
                    parent = node.get('parentNode'),
                    container = parent.ancestor('div');
                    
                    event.preventDefault();
                    node.toggleClass('active');
                    if (!node.hasClass('active')) {
                        parent.one('.authors-hide').setStyles({display : 'block'});
                        node.set('text',' - show less ')
                    } else {
                        node.set('text',' ... show more ')
                        parent.one('.authors-hide').setStyles({display : 'none'});
                    }
                });
            }
        });
    };

    //add trigger markup and delegate click events on class "authorsTrigger"
    if (Y.one('#searchResults')) {
        initializeAuthorToggles();
    }

    //reinitialize when content has changed
    Y.lane.on('lane:new-content', function() {
        initializeAuthorToggles();
    });

})();