YUI().use('yui2-event','node',function(Y) {
    LANE.namespace('search.popin');
    
    // custom onPopin event
    LANE.search.popin = new Y.YUI2.util.CustomEvent("onPopin");
    
    //list of all possible popin elements, in display precedence order
    var popinElms = ['spellCheck', 'queryMapping', 'findIt'],
    // TODO: could make markup order dictate precedence 
    //       (find elm and then fetch parent of elm to see if other popin children
    //       popInContent id NOT always present when popin required)
        onPopinHandler = function(type, el) {
            var i, activeEl = 99, elms = Y.all('#spellCheck, #queryMapping, #findIt');
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null && elms.item(i).getStyle('display') == 'inline') {
                    activeEl = i;
                }
            }
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null) {
                    if (el[0].id === elms.item(i).getAttribute('id') && i <= activeEl) {
                        activeEl = i;
						elms.item(i).setStyle('display','inline');
                    } else if (i > activeEl) {
						elms.item(i).setStyle('display','none');
                    }
                }
            }
        };
    LANE.search.popin.subscribe(onPopinHandler);
});
