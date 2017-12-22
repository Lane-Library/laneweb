(function() {

    "use strict";

    var lane = Y.lane;

    lane.publish('lane:popin', {emitFacade: false});

    lane.on("lane:popin", function(id) {
        //FIXME: are elements returned in the right order? probably not.
        var i, element,
            activeElementIndex = 99,
            elements = document.querySelectorAll('.popin > div');
        for (i = 0; i < elements.length; i++) {
            if (elements.item(i).style.display === 'inline') {
                activeElementIndex = i;
            }
        }
        for (i = 0; i < elements.length; i++) {
            element = elements.item(i);
            if (id === element.id && i <= activeElementIndex) {
                activeElementIndex = i;
                lane.activate(element, "popin");
            } else if (i > activeElementIndex) {
                lane.deactivate(element, "popin");
            }
        }
    });

})();
