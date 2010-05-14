YUI().use('lane', 'event-custom','node',function(Y) {
    LANE.namespace('search.popin');
    
    // custom popin event
    Y.publish('lane:popin',{broadcast:2});
    
    // TODO: could make markup order dictate precedence 
    //       (find elm and then fetch parent of elm to see if other popin children
    //       popInContent id NOT always present when popin required)
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
                    } else if (i > activeEl) {
						elms.item(i).setStyle('display','none');
                    }
                }
            }
        };
    Y.Global.on('lane:popin', onPopinHandler);
});
