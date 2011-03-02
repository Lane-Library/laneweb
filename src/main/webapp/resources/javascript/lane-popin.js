(function() {
    
    LANE.namespace('search.popin');
    
    // custom popin event
    var Y = LANE.Y, onPopinHandler;
    Y.publish('lane:popin',{broadcast:2});
    
        onPopinHandler = function(el) {
            //FIXME: are elms returned in the right order? probably not.
            var i, activeEl = 99, elms = Y.all('#spellCheck, #queryMapping, #findIt'), 
                searchResults = Y.one('#searchResults'), 
                searchFacets = Y.one('#searchFacets'), 
                rightSearchTips = Y.one('.rightSearchTips');
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null && elms.item(i).getStyle('display') == 'inline') {
                    activeEl = i;
                }
            }
            for (i = 0; i < elms.size(); i++) {
                if (elms.item(i) !== null) {
                    if (el.get('id') === elms.item(i).get('id') && i <= activeEl) {
                        activeEl = i;
                        elms.item(i).get('parentNode').setStyle('display','block');
                        elms.item(i).setStyle('display','inline');
                        if(searchResults){
                            if (Y.UA.ie <= 7){
                                searchResults.setStyle('marginTop','0');
                            }
                            else{
                                searchResults.setStyle('marginTop','-3px');
                            }
                        }
                        if(rightSearchTips){
                            rightSearchTips.setStyle('marginTop','7px');
                        }
                        if (searchFacets && !(Y.UA.ie && Y.UA.ie < 7)){ // ie 6 hides facets if they move ... let it bounce
                            searchFacets.setStyle('marginTop','-28px');
                        }
                    } else if (i > activeEl) {
                        elms.item(i).setStyle('display','none');
                    }
                }
            }
        };
    Y.Global.on('lane:popin', onPopinHandler);
})();
