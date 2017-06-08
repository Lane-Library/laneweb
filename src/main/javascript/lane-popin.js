(function() {

    "use strict";

    // custom popin event
    var onPopinHandler;
    Y.publish('lane:popin',{broadcast:1});

        onPopinHandler = function(el) {
            //FIXME: are elms returned in the right order? probably not.
            var i, activeEl = 99, elms = Y.all('#spellCheck, #queryMapping, #findIt');
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null && elms.item(i).getStyle('display') === 'inline') {
                    activeEl = i;
                }
            }
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null) {
                    if (el.get('id') === elms.item(i).get('id') && i <= activeEl) {
                        activeEl = i;
                        elms.item(i).addClass('active');
                    } else if (i > activeEl) {
                        elms.item(i).removeClass('active');
                    }
                }
            }
        };
    Y.on('lane:popin', onPopinHandler);
})();
