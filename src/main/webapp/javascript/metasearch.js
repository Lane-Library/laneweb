var searchTerms;
var searchIndicator;
var searchMode;
var searchStatus;
var searchTemplate;
var searchUrl;
var counter = 0;

YAHOO.util.Event.addListener(window,'load',initializeMetasearch);

function initializeMetasearch(e)
{
	window.searchTerms = (getMetaContent("LW.q")) ? escape(getMetaContent("LW.q")): escape(getMetaContent("LW.searchTerms"));
     window.searchMode = getMetaContent("LW.searchMode");
     window.searchTemplate = (getMetaContent("LW.searchTemplate")) ? getMetaContent("LW.searchTemplate"): location.pathname.replace('/./.','');
     if( window.searchTerms && window.searchTerms != 'undefined'  )
     {
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/filtered-json?q='+window.searchTerms+'&source='+window.searchTemplate, window.metasearchCallback);
     	YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q='+window.searchTerms, window.spellCheckCallBack);
	    if(YAHOO.util.Dom.inDocument('searchIndicator')){
	         window.searchIndicator = new SearchIndicator('searchIndicator','Search Starting ... ');
	     }
	     YAHOO.util.Event.addListener('searchIndicator', 'click', haltMetasearch);
	     YAHOO.util.Event.addListener('toggleZeros', 'click', toggleZeros);
	}
 }


var showMetasearchResults = function(o)
{
	    var searchResponse = eval("("+o.responseText+")");
        window.searchStatus = (window.searchStatus == 'successful') ? window.searchStatus : searchResponse.status;
    	var metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch');
    	if(metasearchElements.length && window.searchStatus != 'successful'){
    		window.counter++;
    		var sleepingTime = 2000; //2 seconds
    		if(window.counter > 15) //time sleepingtime (2 seconds) * 15 = 30 seconds
    			sleepingTime = 10000;// 10 seconds
    	    setTimeout("YAHOO.util.Connect.asyncRequest('GET', '"+'/././apps/search/filtered-json?q='+window.searchTerms+'&source='+window.searchTemplate+'&rd='+Math.random()+"', window.metasearchCallback);",sleepingTime);
    	}
    	for( var z = 0; z<metasearchElements.length; z++){
			if( metasearchElements[z].className != 'complete'  ) {
				var resourceId = metasearchElements[z].getAttribute('id');
				searchResource = searchResponse.resources[resourceId];
				if(searchResource && searchResource.status && searchResource.status != 'running' )  
					var metasearchResult = new MetasearchResult(metasearchElements[z],searchResource, resourceId);
			}
    	}
        if(window.searchIndicator){
        	window.searchIndicator.setProgress(window.searchStatus,YAHOO.util.Dom.getElementsByClassName('metasearch').length,YAHOO.util.Dom.getElementsByClassName('complete').length);
        }
}

function MetasearchResult(metasearchElement,searchResource, id)
{
    if (metasearchElement == null) {
        window.log('MetasearchResult():  metasearchElement should not be null');
    }
    if (searchResource == null) {
       window.log('MetasearchResult():  searchResource should not be null');
    }

    this.id = id;
    this.name = (metasearchElement.innerHTML) ? metasearchElement.innerHTML : '';
    this.status = (searchResource.status) ? searchResource.status : 0;
    this.hits = searchResource.hits;
    this.href = searchResource.url || metasearchElement.href;
    
    this.setContent(metasearchElement);

    if(getMetaContent("LW.debug")){
        this.debug();
    }
}

MetasearchResult.prototype.setContent = function(metasearchElement)
{
    if (metasearchElement == null) {
        window.log('MetasearchResult.setContent():  metasearchElement should not be null');
    }
        switch(window.searchMode){
            case "original":
                if( this.status && this.status != 'running' && this.href ) {
                	metasearchElement.setAttribute('href',this.href);
                    // fix for IE7 (@ in text of element will cause element text to be replaced by href value
                    // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                    if (YAHOO.env.ua.ie){
                    	metasearchElement.innerHTML = this.name;
                    }
                	metasearchElement.setAttribute('target','_blank');
                    metasearchElement.className = 'complete';
                    var resultSpan = document.createElement('span');
                    resultSpan.innerHTML = ': ' + this.hits;
                    metasearchElement.parentNode.appendChild(resultSpan);
    
                    if( parseInt(this.hits) > 0 || this.status != 'successful' ) { 
                        var parentHeading = YAHOO.util.Dom.getAncestorByTagName(this.id,'div').getElementsByTagName('h3')[0];
                        parentHeading.style.display = 'block';
                        var parent = YAHOO.util.Dom.getAncestorByTagName(this.id,'li');
                        parent.style.display = 'block';
                    }
                    else if( parseInt(this.hits) == 0 ) { 
                        var parent = YAHOO.util.Dom.getAncestorByTagName(this.id,'li');
                        parent.className = 'zero';
                    }
                }
            break;
            default:  // merged 
                if( this.status == 'successful' && this.href ) {
                	metasearchElement.setAttribute('href',this.href);
                    // fix for IE7 (@ in text of element will cause element text to be replaced by href value
                    // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                    if (YAHOO.env.ua.ie){
                    	metasearchElement.innerHTML = this.name;
                    }
                	metasearchElement.setAttribute('target','_blank');
            
                	var resultSpan = document.createElement('span');
                	resultSpan.innerHTML = ': ' + this.hits;
                	metasearchElement.parentNode.appendChild(resultSpan);
                    metasearchElement.className = 'complete';
                }
                else if( this.status == 'failed' || this.status == 'canceled' ){
                	metasearchElement.className = 'complete';
                }
            break;
       }
}

MetasearchResult.prototype.debug = function()
{
        var logMessage = '';
        for (var d in this){
            if( d.match(/id|name|status|hits|href/) ){
                logMessage += '\n' + d + ' ==> ' + this[d];
            }
        }
        YAHOO.log(logMessage , 'info');
}

function haltMetasearch()
{
        window.searchStatus = 'successful';
}

var metasearchCallback = 
{
    success:showMetasearchResults,
    failure:window.handleFailure,
  	argument:{file:"metasearch.js", line:"metasearchCallBack"}
};

function toggleZeros(e)
{
    var toggleEl = document.getElementById('toggleZeros');
    var zeroResources = YAHOO.util.Dom.getElementsByClassName('zero');
    var display;
    
    switch(toggleEl.innerHTML){
        case "Show Details":
            display = 'block';
            toggleEl.innerHTML = 'Hide Details';
        break;
        case "Hide Details":
            display = 'none';
            toggleEl.innerHTML = 'Show Details';
        break;
    }
    
    for(var i = 0; i<zeroResources.length; i++){
        YAHOO.util.Dom.setStyle(zeroResources[i],'display',display);
    }

    // toggle h3 header as well if all children in searchCat are zeros
    var searchCats = YAHOO.util.Dom.getElementsByClassName('searchCategory');
    for(var y = 0; y<searchCats.length; y++){
        if(YAHOO.util.Dom.getElementsByClassName('zero','',searchCats[y]).length ==  searchCats[y].getElementsByTagName('li').length ){
            YAHOO.util.Dom.setStyle(YAHOO.util.Dom.getFirstChild(searchCats[y]),'display',display);
        }
    }
}


function SearchIndicator(elementId,message) 
{
    this.elementId = (YAHOO.util.Dom.inDocument(elementId)) ? elementId : null;
    if(null == this.elementId){
        window.log('SearchIndicator():  SearchIndicator missing elementId');
    }
    this.message = message;
    this.show();
}

SearchIndicator.prototype.hide = function()
{
 	YAHOO.util.Dom.setStyle(this.elementId,'visibility','hidden');
}

SearchIndicator.prototype.show = function()
{
   YAHOO.util.Dom.setStyle(this.elementId,'visibility','visible');
}

SearchIndicator.prototype.setProgress = function(status,pendingResources,completedResources)
{
    this.show();
    this.setMessage(completedResources + ' of ' + (pendingResources + completedResources) + ' sources searched');
    if(status == 'successful' || pendingResources == 0)
    {
        this.hide();
        YAHOO.util.Dom.setStyle('resultsMessage','display','inline');
        YAHOO.util.Dom.setStyle('metasearchControls','display','inline');
    }
}

SearchIndicator.prototype.setMessage = function(message)
{
    this.message = message;
    document.getElementById(this.elementId).title = this.message;
}



function showSpellCheck(o)
{
	    var spellCheckResponse = eval("("+o.responseText+")");
	    if (spellCheckResponse.suggestion) {
			var spellCheckContainer = document.getElementById("spellCheck");
			var spellCheckLink = document.getElementById("spellCheckLink");
			if(spellCheckContainer && spellCheckLink)
			{
    			spellCheckLink.href = location.href.replace(location.href.match(/\W(keywords|q)=([^&]*)/)[2],spellCheckResponse.suggestion);
    			spellCheckLink.innerHTML = spellCheckResponse.suggestion;
    			spellCheckContainer.style.display= 'inline';
    			spellCheckContainer.style.visibility= 'visible';
            }
		}
}

var spellCheckCallBack =
{
  success:showSpellCheck,
  failure:handleFailure,
  argument:{file:"metasearch.js", line:"spellCheckCallBack"} 
};


