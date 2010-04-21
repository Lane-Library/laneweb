YUI().use('yui2-event','yui2-dom',function(Y) {
    LANE.namespace('search.popin');
    
    // custom onPopin event
    LANE.search.popin = new Y.YUI2.util.CustomEvent("onPopin");
    
    //list of all possible popin elements, in display precedence order
    var popinElms = ['spellCheck', 'queryMapping', 'findIt'],
    // TODO: could make markup order dictate precedence 
    //       (find elm and then fetch parent of elm to see if other popin children
    //       popInContent id NOT always present when popin required)
        onPopinHandler = function(type, el) {
            var i, activeEl = 99, elms = Y.YUI2.util.Dom.get(popinElms);
            for (i = 0; i < elms.length; i++) {
                if (elms[i] !== null && elms[i].style.display == 'inline') {
                    activeEl = i;
                }
            }
            for (i = 0; i < elms.length; i++) {
                if (elms[i] !== null) {
                    if (el[0].id === elms[i].id && i <= activeEl) {
                        activeEl = i;
                        elms[i].style.display = 'inline';
                    } else if (i > activeEl) {
                        elms[i].style.display = 'none';
                    }
                }
            }
        };
    LANE.search.popin.subscribe(onPopinHandler);
});
