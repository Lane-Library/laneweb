(function() {

    "use strict";

    L.publish('lane:popin', {emitFacade: false});

    L.on("lane:popin", function(id) {
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
                element.classList.add("popin-active");
            } else if (i > activeElementIndex) {
                element.classList.remove("popin-active");
            }
        }
    });

})();
