(function() {
    
    //TODO: move to lane-is.js or lane.js
    if(!Array.indexOf){
        Array.prototype.indexOf = function(obj){
            for(var i=0; i<this.length; i++){
                if(this[i]==obj){
                    return i;
                }
            }
            return -1;
        }
    }
    
    LANE.namespace('search.metasearch');
    LANE.search.metasearch = function(){
        var searchElms,   // the elements in need of hit counts
        searchables = [], // all engines to search
        searchUrl,          // url to poll search app
        searchRequests = [], // search timerIds so we can abort sleeping getResultCounts
        uberEngines = ['cro_','mdc_','ovid-'], // engines with multiple resources
        i, y,
        startTime;
        return {
            initialize: function(){
                searchElms = YAHOO.util.Dom.getElementsBy(
                    function(el){
                            return el.className.match("metasearch");
                        }
                );
                for(i = 0; i < searchElms.length; i++){
                    LANE.search.metasearch.addSearchable(searchElms[i])
                }
                LANE.search.metasearch.abortPendingRequests();
                startTime = new Date().getTime();
            },
            getStartTime : function(){
                return startTime;
            },
            getSearchElms : function(){
                return searchElms;
            },
            getSearchables : function(){
                return searchables;
            },
            addSearchable : function(el){
                if(searchables.indexOf(el.id) == -1){
                    searchables.push(el.id);
                }
            },
            getSearchRequests : function(){
                return searchRequests;
            },
            addSearchRequest : function(id){
                searchRequests.push(id);
            },
            abortPendingRequests : function(){
                searchRequests = LANE.search.metasearch.getSearchRequests();
                for (var z = 0; z < searchRequests.length; z++){
                    clearTimeout(searchRequests[z]);
                    searchRequests.splice(z,1);
                }
            },
            getSearchUrl : function(){
                searchUrl = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString();
                for ( y = 0; y < searchables.length; y++){
                    var add = true;
                    for (i = 0; i < uberEngines.length; i++){
                        // don't add if: 
                        // - engine is uber and uber already on url
                        // - engine already on url
                        if( searchables[y].match(uberEngines[i]) && searchUrl.match(uberEngines[i]) ) {
                            add = false;
                        }
                        else if (searchUrl.match("r="+searchables[y]+"(&|$)")){
                            add = false;
                        }
                    }
                    if (add){
                        searchUrl+='&r='+searchables[y];
                    }
                }
                searchUrl += '&rd=' + Math.random();
                return searchUrl;
            }
        };
    }();
    
    YAHOO.util.Event.onDOMReady(function(){
        LANE.search.metasearch.initialize();
        searchElms = LANE.search.metasearch.getSearchElms();
        
        // check for presence of search term and metasearch classNames
        if( LANE.search.getEncodedSearchString() && searchElms.length > 0 ){
                        
            LANE.search.metasearch.getResultCounts = function() {
                searchables = LANE.search.metasearch.getSearchables();
                
                YAHOO.util.Connect.asyncRequest('GET',LANE.search.metasearch.getSearchUrl(), {
                    success: function(o){
                        var response = YAHOO.lang.JSON.parse(o.responseText), 
                            results = response.resources,
                            i, needMore = false, result, updateable, resultSpan, sleepingTime, remainingTime;
                        
                        for (i = 0; i < searchables.length; i++) {
                            updateable = document.getElementById(searchables[i]);
                            result = results[searchables[i]];
                            if(result === undefined || !result.status){
                                needMore = true;
                                continue;
                            }
                            else if(updateable && result.status == 'successful'){
                                // process display of each updateable node
                                // once all processed, remove id from searchables
                                resultSpan = YAHOO.util.Dom.getElementsByClassName('searchCount','span',updateable.parentNode)[0];
                                resultSpan.innerHTML = '&#160;' + YAHOO.util.Number.format(result.hits,{thousandsSeparator:","});
                                if (!updateable.href){
                                    updateable.setAttribute('href', result.url);
                                    updateable.setAttribute('target', '_blank');
                                }
                                YAHOO.util.Dom.removeClass(updateable,'metasearch');
                                searchables.splice(i, 1);
                            }
                            else if (updateable && (result.status == 'failed' || result.status == 'canceled')) {
                                resultSpan = YAHOO.util.Dom.getElementsByClassName('searchCount','span',updateable.parentNode)[0];
                                resultSpan.innerHTML = ' ? ';
                                YAHOO.util.Dom.removeClass(updateable,'metasearch');
                                searchables.splice(i, 1);
                            }
                        }
                        sleepingTime = 2000;
                        remainingTime = (new Date().getTime()) - LANE.search.metasearch.getStartTime();
                        if ( response.status != 'successful' 
                                && needMore 
                                && searchables.length > 0 
                                && (remainingTime <= 60 * 1000) ) { // at more than 20 seconds the sleeping time becomes 10 seconds
                            if (remainingTime > 20 * 1000) {
                                sleepingTime = 10000;
                            }
                            LANE.search.metasearch.addSearchRequest(setTimeout(LANE.search.metasearch.getResultCounts,sleepingTime));
                        }
                        else{
                            LANE.search.stopSearch();
                        }
                    }// end request success definition
                });// end async request
                
            };// end getResultCounts
            // kick off initial metasearch request
            LANE.search.metasearch.getResultCounts();
            LANE.search.startSearch();
        }
            
    });//end addListener
})();
