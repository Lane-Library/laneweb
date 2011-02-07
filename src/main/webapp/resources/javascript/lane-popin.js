(function() {
    
    LANE.namespace('search.popin');
    
    // custom popin event
    var Y = LANE.Y, onPopinHandler;
    Y.publish('lane:popin',{broadcast:2});
    
        onPopinHandler = function(el) {
            //FIXME: are elms returned in the right order? probably not.
            var i, activeEl = 99, elms = Y.all('#spellCheck, #queryMapping, #findIt');
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null && elms.item(i).getStyle('display') == 'inline') {
                    activeEl = i;
                }
            }
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null) {
                    if (el.get('id') === elms.item(i).get('id') && i <= activeEl) {
                        activeEl = i;
                        elms.item(i).get('parentNode').setStyle('display','inline');
                        elms.item(i).setStyle('display','inline');
                        Y.one('#searchResults').setStyle('marginTop','5px');
                    } else if (i > activeEl) {
                        elms.item(i).setStyle('display','none');
                    }
                }
            }
        };
    Y.Global.on('lane:popin', onPopinHandler);
})();
