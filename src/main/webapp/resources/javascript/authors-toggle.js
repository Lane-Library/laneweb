(function() {

    "use strict";

    var initializeAuthorToggles = function() {
        Y.all('.authorsTrigger').each(function(node) {
            if (!node.getData().authorsTriggerSubscribed) {
                node.setData('authorsTriggerSubscribed',true);
                node.on('click', function(event) {
                    var node = event.currentTarget,
                    anchorNode = node.one('a'),
                    parent = node.get('parentNode'),
                    iconNode = node.one('i');

                    event.stopPropagation();
                    event.preventDefault();
                    node.toggleClass('active');
                    if (!node.hasClass('active')) {
                        node.previous().set('text', ' - ');
                        parent.one('.authors-hide').setStyles({display : 'block'});
                        anchorNode.set('text',' Show Less ');
                        iconNode.removeClass('fa-angle-double-down');
                        iconNode.addClass('fa-angle-double-up');
                    } else {
                        node.previous().set('text', ' ... ');
                        parent.one('.authors-hide').setStyles({display : 'none'});
                        anchorNode.set('text',' Show More ');
                        iconNode.removeClass('fa-angle-double-up');
                        iconNode.addClass('fa-angle-double-down');
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