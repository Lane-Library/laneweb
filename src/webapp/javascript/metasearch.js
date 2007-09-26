var searchId;
var searchIndicator;

YAHOO.util.Event.addListener(window,'load',initializeMetasearch);

function initializeMetasearch(e)
{
    try {
        if(getMetaContent("LW.keywords")){
        	YAHOO.util.Connect.asyncRequest('GET', '/././apps/search-proxy/json?q='+getMetaContent("LW.keywords"), window.metasearchCallback);
    		YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q='+getMetaContent("LW.keywords"), window.spellCheckCallBack);
    		window.searchIndicator = new SearchIndicator('searchIndicator','Search Starting');
    		window.searchIndicator.show();
	    }
   	} catch(e) { window.handleException(e) }
}

var showMetasearchResults = function(o)
{
    try {
	    var searchResponse = eval("("+o.responseText+")");
        var searchResources = searchResponse.resources;
    	var searchStatus = searchResponse.status;
    	window.searchId = searchResponse.id;
        
    	var metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch');
    	window.searchIndicator.setProgress(searchStatus,metasearchElements.length);

    	if(metasearchElements.length && searchStatus != 'successful'){
    	    setTimeout("YAHOO.util.Connect.asyncRequest('GET', '/././apps/search-proxy/json?id='+"+window.searchId+"+'&r='+"+Math.random()+", window.metasearchCallback);",1500);
    	}
    
    	for(var i = 0; i<searchResources.length; i++){
    			for( var z = 0; z<metasearchElements.length; z++){
    				if( metasearchElements[z].className != 'complete' && metasearchElements[z].getAttribute('id') == searchResources[i].id ) {
    			        var metasearchResult = new MetasearchResult(metasearchElements[z],searchResources[i]);
    				}
    			}
    	}


   	} catch(e) { window.handleException(e) }
}

function MetasearchResult(metasearchElement,searchResource)
{
    if (metasearchElement == null) {
        throw('null metasearchElement');
    }
    if (searchResource == null) {
        throw('null searchResource');
    }

    this.id = searchResource.id;
    this.name = (metasearchElement.innerHTML) ? metasearchElement.innerHTML : '';
    this.status = searchResource.status;
    this.hits = searchResource.hits;
    this.href = searchResource.url;

    // write debugging info
    if(getMetaContent("LW.debug") && YAHOO.util.Dom.inDocument('debug')){
        var dd = document.createElement('div');
        for (var d in this){
            if(d == 'href'){
                var a = document.createElement('a');
                a.href = this[d];
                a.innerHTML = d;
                dd.innerHTML += ' :: ';
                dd.appendChild(a);
            }
            else{
                dd.innerHTML += ' :: ' + d + '=' + this[d];
            }
        }
        document.getElementById('debug').appendChild(dd);
    }

    if( this.status == 'successful') {
    	metasearchElement.setAttribute('href',this.href);
    	metasearchElement.setAttribute('target','_blank');

    	this.resultSpan = document.createElement('span');
    	this.resultSpan.innerHTML = ': ' + this.hits;
    	metasearchElement.parentNode.appendChild(this.resultSpan);
        metasearchElement.className = 'complete';
    }
    else if( this.status == 'failed' || this.status == 'canceled' ){
    	metasearchElement.className = 'complete';
    }
}

var metasearchCallback = 
{
    success:showMetasearchResults,
    failure:window.handleFailure	
};


function SearchIndicator(elementId,message) 
{
    this.elementId = (YAHOO.util.Dom.inDocument(elementId)) ? elementId : null;
    if(null == elementId){
        throw('SearchIndicator missing elementId');
    }
    this.message = message;
}

SearchIndicator.prototype.hide = function()
{
    YAHOO.util.Dom.setStyle(this.elementId,'visibility','hidden');
}

SearchIndicator.prototype.show = function()
{
    YAHOO.util.Dom.setStyle(this.elementId,'visibility','visible');
}

SearchIndicator.prototype.setProgress = function(status,pendingResources)
{
    this.show();
    
    if(status == 'successful' || pendingResources == 0)
    {
        this.setMessage('Search complete');
        this.hide();
    }
    else if(pendingResources == 1)
    {
        this.setMessage(pendingResources + 'one pending resource');
    }
    else if(pendingResources > 1)
    {
        this.setMessage(pendingResources + ' pending resources');
    }
}

SearchIndicator.prototype.setMessage = function(message)
{
    this.message = message;
    document.getElementById(this.elementId).title = this.message;
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
    			spellCheckLink.href = location.href.replace("keywords=" + getMetaContent("LW.keywords"),"keywords=" + spellCheckResponse.suggestion);
    			spellCheckLink.innerHTML = spellCheckResponse.suggestion;
    			spellCheckContainer.style.display= 'inline';
    			spellCheckContainer.style.visibility= 'visible';
            }
		}
	
   	} catch(exception) { window.handleException(exception) }
}

var spellCheckCallBack =
{
  success:showSpellCheck,
  failure: null
};




//TODO should these move to laneweb.js?
function handleException(exception) {
    alert(exception.name + '\n' + exception.message + '\n' + exception.fileName + '\n' + exception.lineNumber + '\n' + exception.stack);
}

function handleFailure(o)
{
	alert("status: "+o.status+ '\n' +"statusText "+o.statusText  );	
}

