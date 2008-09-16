(function() {
    LANE.namespace('search.metasearch');

    YAHOO.util.Event.addListener(this,'load',function() {
		// check for presence of search term and metasearch classNames
		if( LANE.search.getEncodedSearchString() && YAHOO.util.Dom.getElementsByClassName('metasearch','a') ){
		    LANE.search.metasearch.getResultCounts = function() {
				var url = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString(),
					mergedMode = true,
					// fetch metasearchElements and put ids in url string
					metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch','a',document,function(){
						// filter out multiple ovid, mds and cro requests b/c of IE6's 2048 character limit on requests
						// this works b/c search returns all resources for engine when one resource requested
						var uberEngines = ['cro_','mdc_','ovid-'], i, add = true;
						for ( i = 0; i < uberEngines.length; i++){
							if( this.id.match(uberEngines[i]) && url.match(uberEngines[i]) ){
								add = false;
							}
						}
						if ( add ){
							url+='&r='+this.id;
						}
					});
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
					                resultSpan.innerHTML = ': ' + result.hits;
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
						                } else if (parseInt(this.hits, 10) === 0) {
					                    	// add zero class to metasearchElement
					                    	// TODO: need to add toggle?
					                        YAHOO.util.Dom.addClass(YAHOO.util.Dom.getAncestorByTagName(metasearchElements[i].id, 'li'),'zero');
						                }
									}
		                        }
	                        }
		                }
		                
		                if (needMore) {
		                	//TODO: need to calculate sleeping time
		                    setTimeout("LANE.search.metasearch.getResultCounts()",2000);
		                }
		                
		            }// end request success definition
		        });// end async request
		    };// end getResultCounts
		    
		    //TODO: make this do something
		    LANE.search.metasearch.toggleZerohits = function() {
		    	
		    }
		    
		    LANE.search.metasearch.getResultCounts();
		}
			
    });//end addListener
})();