var keywords;
var searchId;
var searchIndicator;
var searchMode;
var searchStatus;
var sourceTemplate;

YAHOO.util.Event.addListener(window,'load',initializeMetasearch);

function initializeMetasearch(e)
{
    try {

        window.keywords = escape(getMetaContent("LW.keywords"));
        window.searchId = getMetaContent("LW.searchId");
        window.searchMode = getMetaContent("LW.searchMode");
        window.searchTemplate = getMetaContent("LW.searchTemplate");

        if( (window.searchId && window.searchId != 'undefined') && (window.searchTemplate && window.searchTemplate != 'undefined') )
        {
        	YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/filtered-json?id='+window.searchId+'&source='+window.searchTemplate, window.metasearchCallback);
    		YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q='+window.keywords, window.spellCheckCallBack);

            if(YAHOO.util.Dom.inDocument('searchIndicator')){
                window.searchIndicator = new SearchIndicator('searchIndicator','Search Starting ... ');
            }
            
            YAHOO.util.Event.addListener('searchIndicator', 'click', haltMetasearch);
	    }
   	} catch(e) { window.handleException(e) }
}

var showMetasearchResults = function(o)
{
var resourceId;
    try {
	    var searchResponse = eval("("+o.responseText+")");
        window.searchStatus = (window.searchStatus == 'successful') ? window.searchStatus : searchResponse.status;
    	window.searchId = searchResponse.id;
        
    	var metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch');
        if(window.searchIndicator){
        	window.searchIndicator.setProgress(window.searchStatus,metasearchElements.length,YAHOO.util.Dom.getElementsByClassName('complete').length);
        }
    	if(metasearchElements.length && window.searchStatus != 'successful'){
    	    setTimeout("YAHOO.util.Connect.asyncRequest('GET', '"+'/././apps/search/filtered-json?id='+window.searchId+'&source='+window.searchTemplate+'&rd='+Math.random()+"', window.metasearchCallback);",2000);
    	}
    	for( var z = 0; z<metasearchElements.length; z++){
			if( metasearchElements[z].className != 'complete'  ) {
				resourceId = metasearchElements[z].getAttribute('id');
				searchResource = searchResponse.resources[resourceId];
				if(searchResource)  
					var metasearchResult = new MetasearchResult(metasearchElements[z],searchResource, resourceId);
			}
    	}

   	} catch(e) {  window.handleException(exception) }
}

function MetasearchResult(metasearchElement,searchResource, id)
{
    if (metasearchElement == null) {
        throw('null metasearchElement');
    }
    if (searchResource == null) {
        throw('null searchResource');
    }

    this.id = id;
    this.name = (metasearchElement.innerHTML) ? metasearchElement.innerHTML : '';
    this.status = (searchResource.status) ? searchResource.status : 0;
    this.hits = searchResource.hits;
    this.href = searchResource.url;
    
    this.setContent(metasearchElement);

    if(getMetaContent("LW.debug") && YAHOO.util.Dom.inDocument('debug')){
        this.debug();
    }
}

MetasearchResult.prototype.setContent = function(metasearchElement)
{
    if (metasearchElement == null) {
        throw('null metasearchElement');
    }
 	try {
        switch(window.searchMode){
            case "original":
                if( this.status && this.status != 'running' ) {
                	metasearchElement.setAttribute('href',this.href);
                	metasearchElement.setAttribute('target','_blank');
                    metasearchElement.className = 'complete';
    
                    var resultSpan = document.createElement('span');
                    resultSpan.innerHTML = ': ' + this.hits;
                    metasearchElement.parentNode.appendChild(resultSpan);
    
                    if( this.hits > 0 || this.status != 'successful' ) { 
                        var parentHeading = YAHOO.util.Dom.getAncestorByTagName(this.id,'div').getElementsByTagName('h3')[0];
                        parentHeading.style.display = 'block';
                        var parent = YAHOO.util.Dom.getAncestorByTagName(this.id,'li');
                        parent.style.display = 'block';
                    }
                    else if( this.hits == 0 ) { 
                        var parent = YAHOO.util.Dom.getAncestorByTagName(this.id,'li');
                        parent.className = 'zero';
                    }
                }
            break;
            default:  // merged 
                if( this.status == 'successful') {
                	metasearchElement.setAttribute('href',this.href);
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
   	} catch(exception) { window.handleException(exception) }
}

MetasearchResult.prototype.debug = function()
{
 	try {
        var dd = document.createElement('div');
        for (var d in this){
            if( d.match(/id|name|status|hits/) ){
                dd.innerHTML += ' :: ' + d + '=' + this[d];
            }
            else if(d == 'href'){
                var a = document.createElement('a');
                a.href = this[d];
                a.innerHTML = d;
                dd.innerHTML += ' :: ';
                dd.appendChild(a);
            }
        }
        document.getElementById('debug').appendChild(dd);
        
   	} catch(exception) { window.handleException(exception) }
}
function haltMetasearch()
{
 	try {
        window.searchStatus = 'successful';
   	} catch(exception) { window.handleException(exception) }
}

var metasearchCallback = 
{
    success:showMetasearchResults,
    failure:window.handleFailure	
};

function toggleZeros(e)
{
    try {
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
   	} catch(e) { window.handleException(e) }
}


function SearchIndicator(elementId,message) 
{
    this.elementId = (YAHOO.util.Dom.inDocument(elementId)) ? elementId : null;
    if(null == this.elementId){
        throw('SearchIndicator missing elementId');
    }
    this.message = message;
    this.show();
}

SearchIndicator.prototype.hide = function()
{
 	try {
        YAHOO.util.Dom.setStyle(this.elementId,'visibility','hidden');
   	} catch(exception) { window.handleException(exception) }
}

SearchIndicator.prototype.show = function()
{
 	try {
 	    YAHOO.util.Dom.setStyle(this.elementId,'visibility','visible');
   	} catch(exception) { window.handleException(exception) }
}

SearchIndicator.prototype.setProgress = function(status,pendingResources,completedResources)
{
	try {
        this.show();
        if(status == 'successful' || pendingResources == 0)
        {
            this.hide();
            //YAHOO.util.Dom.setStyle('haltMetasearch','display','none');
        	this.setMessage('Results for ' + window.keywords);
            YAHOO.util.Dom.setStyle('resultsMessage','display','inline');
            YAHOO.util.Dom.setStyle('metasearchControls','display','inline');
            YAHOO.util.Event.addListener('toggleZeros', 'click', toggleZeros);
        }
        else{
       		this.setMessage(completedResources + ' of ' + (pendingResources + completedResources) + ' sources searched');
        }
   	} catch(exception) { window.handleException(exception) }
}

SearchIndicator.prototype.setMessage = function(message)
{
	try {
        this.message = message;
        document.getElementById(this.elementId).title = this.message;
   	} catch(exception) { window.handleException(exception) }
}



function showSpellCheck(o)
{
	try {
	    var spellCheckResponse = eval("("+o.responseText+")");
	    if (spellCheckResponse.suggestion) {
			var spellCheckContainer = document.getElementById("spellCheck");
			var spellCheckLink = document.getElementById("spellCheckLink");
			if(spellCheckContainer && spellCheckLink)
			{
    			spellCheckLink.href = location.href.replace("keywords=" + window.keywords,"keywords=" + spellCheckResponse.suggestion);
    			spellCheckLink.innerHTML = spellCheckResponse.suggestion;
    			spellCheckContainer.style.display= 'inline';
    			spellCheckContainer.style.visibility= 'visible';
            }
		}
   	} catch(exception) { window.handleException(exception) }
}

var spellCheckCallBack =
{
  success:showSpellCheck
};




//TODO should these move to laneweb.js?
function handleException(exception) {
    alert(exception.name + '\n' + exception.message + '\n' + exception.fileName + '\n' + exception.lineNumber + '\n' + exception.stack);
}

function handleFailure(o)
{
	alert("status: "+o.status+ '\n' +"statusText "+o.statusText  );	
}