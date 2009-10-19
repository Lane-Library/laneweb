(function() {
    LANE.namespace('search.metasearch');
    var startTime = new Date().getTime(),
        url,
        mergedMode = true,
        // filter out multiple ovid, mdc and cro requests b/c of IE6's 2048 character limit on requests
        // this depends on search app returning all resources for engine when one resource requested
        filterSearchUrl = function(el){
            var uberEngines = ['cro_','mdc_','ovid-'], i, add = true;
            for ( i = 0; i < uberEngines.length; i++){
                if( el.id.match(uberEngines[i]) && url.match(uberEngines[i]) ){
                    add = false;
                }
            }
            if ( add ){
                url+='&r='+el.id;
            }
        };

    YAHOO.util.Event.onDOMReady(function(){
        // check for presence of search term and metasearch classNames
        if( LANE.search.getEncodedSearchString() && YAHOO.util.Dom.getElementsByClassName('metasearch','a').length > 0 ){
            
            LANE.search.metasearch.getResultCounts = function() {
                url = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString();
                var metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch','a',document,filterSearchUrl);
                url += '&rd=' + Math.random();
                
                // determine if this is a merged page ... matters for result processing later on
                if( LANE.search.getSearchSource().match(/^(clinical|peds|research|pharmacy|history|test|textbooks)$/) !== null ){
                    mergedMode = false;
                }
                
                YAHOO.util.Connect.asyncRequest('GET',url, {
                    success: function(o){
                        var results = YAHOO.lang.JSON.parse(o.responseText),
                            i, needMore = false, result, resultSpan, sleepingTime, remainingTime;
    
                        for (i = 0; i < metasearchElements.length; i++) {
                            result = results.resources[metasearchElements[i].id];
                            if( result !== undefined ){
                                if (!result.status) {
                                    needMore = true;
                                }
                                if ( result.url && 
                                        (mergedMode && result.status == 'successful') ||
                                        (!mergedMode && result.status && result.status != 'running') ) {
                                    result.name = (metasearchElements[i].innerHTML) ? metasearchElements[i].innerHTML : '';
                                    metasearchElements[i].setAttribute('href', result.url);
                                    // fix for IE7 (@ in text of element will cause element text to be replaced by href value
                                    // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                                    if (YAHOO.env.ua.ie) {
                                        metasearchElements[i].innerHTML = result.name;
                                    }
                                    
                                    metasearchElements[i].setAttribute('target', '_blank');
                                    YAHOO.util.Dom.removeClass(metasearchElements[i],'metasearch');
                                    resultSpan = document.createElement('span');
                                    // force UpToDate count to 50+ when 150 returned (FB#25141)
                                    if(metasearchElements[i].id == 'uptodate' && result.hits == 150){
                                        result.hits = '50+';
                                    }
                                    resultSpan.appendChild(document.createTextNode(': ' + result.hits + ' '));
                                    metasearchElements[i].parentNode.appendChild(resultSpan);
        
                                    if ( mergedMode ){
                                        if (result.status == 'failed' || result.status == 'canceled') {
                                            YAHOO.util.Dom.removeClass(metasearchElements[i],'metasearch');
                                        }
                                    }                            
                                    else { // original mode (clinical, bioresearch, etc.)
                                        if (parseInt(result.hits, 10) > 0 || result.status != 'successful') {
                                            //TODO: check for these elements first?
                                            YAHOO.util.Dom.getAncestorByClassName(metasearchElements[i].id, 'searchCategory').getElementsByTagName('h3')[0].style.display = 'block';
                                            YAHOO.util.Dom.getAncestorByTagName(metasearchElements[i].id, 'li').style.display = 'block';
                                        } else if (parseInt(result.hits, 10) === 0) {
                                            // add zeroHit class to metasearchElement for toggling
                                            YAHOO.util.Dom.addClass(YAHOO.util.Dom.getAncestorByTagName(metasearchElements[i].id, 'li'),'zeroHit');
                                        }
                                    }
                                }
                            }
                        }
                        
                        sleepingTime = 2000;
                        remainingTime = (new Date().getTime()) - startTime;
                        if ( needMore && (remainingTime <= 60 * 1000)) { // at more than 20 seconds the sleeping time becomes 10 seconds
                            if (remainingTime > 20 * 1000) {
                                sleepingTime = 10000;
                            }
                            setTimeout(LANE.search.metasearch.getResultCounts,sleepingTime);
                        }
                        else{
                            LANE.search.stopSearch();
                        }
                    }// end request success definition
                });// end async request
                
            };// end getResultCounts
            
            // add handler for zeroHit toggle if id=toggleZeros present
            YAHOO.util.Event.onAvailable('toggleZeros',function() {
                YAHOO.util.Event.addListener('toggleZeros', 'click', function(event) {
                    var toggleEl = document.getElementById('toggleZeros'), 
                        zeroResources = YAHOO.util.Dom.getElementsByClassName('zeroHit'), 
                        searchCats = YAHOO.util.Dom.getElementsByClassName('searchCategory'),
                        display, i, y;
                    if ( toggleEl.innerHTML.match(/Show/) ){
                        display = "block";
                        toggleEl.innerHTML = toggleEl.innerHTML.replace("Show","Hide");
                    }
                    else {
                        display = "none";
                        toggleEl.innerHTML = toggleEl.innerHTML.replace("Hide","Show");
                    }
                    for (i = 0 ; i < zeroResources.length; i++) {
                        YAHOO.util.Dom.setStyle(zeroResources[i], 'display', display);
                    }
                    // toggle h3 header as well if all children in searchCat are zeroHit
                    for (y = 0; y < searchCats.length; y++) {
                        if (YAHOO.util.Dom.getElementsByClassName('zeroHit', '', searchCats[y]).length == searchCats[y].getElementsByTagName('li').length) {
                            YAHOO.util.Dom.setStyle(YAHOO.util.Dom.getFirstChild(searchCats[y]), 'display', display);
                        }
                    }
                    YAHOO.util.Event.preventDefault(event);
                });
            });
            
            // kick off initial metasearch request
            LANE.search.metasearch.getResultCounts();
            //LANE.search.startSearch(); // not available at onDOMReady
            document.getElementById('searchIndicator').style.visibility = 'visible';
        }
            
    });
})();