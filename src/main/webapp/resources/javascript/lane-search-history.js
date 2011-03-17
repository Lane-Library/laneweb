(function() {

    if (Y.lane.Search) {
    var history,
        searchFacets = Y.one('#searchFacets');
    Y.lane.Search.History = function(){
        if(searchFacets){
            history = new Y.HistoryHash();        
            if(history.get('facet')){
                Y.lane.search.facets.setActiveFacet(history.get('facet'));
            }
            history.on("facetChange",function(e) {
                Y.lane.search.facets.setActiveFacet(e.newVal);
            });
            history.on("facetRemove",function(e) {
                Y.lane.search.facets.setActiveFacet(Y.lane.Search.getSearchSource());
            });
        }
        return history;
        
    }();
    
    }
        
})();
