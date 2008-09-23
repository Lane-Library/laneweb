(function() {
    LANE.namespace('search.metasearch');
    var startTime = startTime = new Date().getTime(),
    	url,
    	mergedMode = true,
		// filter out multiple ovid, mdc and cro requests b/c of IE6's 2048 character limit on requests
		// this depends on search app returning all resources for engine when one resource requested
		filterSearchUrl = function(id){
			var uberEngines = ['cro_','mdc_','ovid-'], i, add = true;
			for ( i = 0; i < uberEngines.length; i++){
				if( this.id.match(uberEngines[i]) && url.match(uberEngines[i]) ){
					add = false;
				}
			}
			if ( add ){
				url+='&r='+this.id;
			}
		};

    YAHOO.util.Event.addListener(this,'load',function() {
		// check for presence of search term and metasearch classNames
		if( LANE.search.getEncodedSearchString() && YAHOO.util.Dom.getElementsByClassName('metasearch','a').length > 0 ){
			
		    LANE.search.metasearch.getResultCounts = function() {
				url = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString();
				var metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch','a',document,filterSearchUrl);
				url += '&rd=' + Math.random();
				
				// determine if this is a merged page ... matters for result processing later on
				if( LANE.search.getSearchSource().match(/^(clinical|peds|research|pharmacy|history|test)$/) !== null ){
					mergedMode = false;
				}
				
		        YAHOO.util.Connect.asyncRequest('GET',url, {
		            success: function(o){
		                var results = YAHOO.lang.JSON.parse(o.responseText),
		                    i, needMore = false, result, resultSpan;
	
		                for (i = 0; i < metasearchElements.length; i++) {
	                        result = results.resources[metasearchElements[i].id];
	                        if( result != undefined ){
		                        if (!result.status) {
		                            needMore = true;
		                        }
					            if ( result.url && 
					            		(mergedMode && result.status == 'successful') ||
					            		(!mergedMode && result.status && result.status != 'running') ) {
		                        	result.name = (metasearchElements[i].innerHTML) ? metasearchElements[i].innerHTML : ''
					                metasearchElements[i].setAttribute('href', result.url);
					                // fix for IE7 (@ in text of element will cause element text to be replaced by href value
					                // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
					                if (YAHOO.env.ua.ie) {
					                    metasearchElements[i].innerHTML = result.name;
					                }
					                
					                metasearchElements[i].setAttribute('target', '_blank');
					                YAHOO.util.Dom.removeClass(metasearchElements[i],'metasearch');
					                resultSpan = document.createElement('span');
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
		                    setTimeout("LANE.search.metasearch.getResultCounts()",sleepingTime);
		                }
		                else{
			            	LANE.search.indicator.hide();
		                }
		            }// end request success definition
		        });// end async request
		        
		    }// end getResultCounts
		    
			// add handler for zeroHit toggle if id=toggleZeros present
	    	YAHOO.util.Event.onAvailable('toggleZeros',function() {
	    		YAHOO.util.Event.addListener('toggleZeros', 'click', function() {
				    var toggleEl = document.getElementById('toggleZeros'), 
				    	zeroResources = YAHOO.util.Dom.getElementsByClassName('zeroHit'), 
				    	searchCats = YAHOO.util.Dom.getElementsByClassName('searchCategory'),
				    	display;
					if ( toggleEl.innerHTML.match(/Show/) ){
						display = "block";
						toggleEl.innerHTML = toggleEl.innerHTML.replace("Show","Hide");
					}
					else {
						display = "none";
						toggleEl.innerHTML = toggleEl.innerHTML.replace("Hide","Show");
					}
				    for (i in zeroResources) {
				        YAHOO.util.Dom.setStyle(zeroResources[i], 'display', display);
				    }
				    // toggle h3 header as well if all children in searchCat are zeroHit
				    for (y in searchCats) {
				        if (YAHOO.util.Dom.getElementsByClassName('zeroHit', '', searchCats[y]).length == searchCats[y].getElementsByTagName('li').length) {
				            YAHOO.util.Dom.setStyle(YAHOO.util.Dom.getFirstChild(searchCats[y]), 'display', display);
				        }
				    }
	    		});
	    	});
	    	
		    // kick off initial metasearch request
		    LANE.search.metasearch.getResultCounts();
        	LANE.search.indicator.show();
		}
			
    });//end addListener
})();