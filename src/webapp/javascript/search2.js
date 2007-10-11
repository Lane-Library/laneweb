var keywords;
var startTime = new Date().getTime();
var activeResult;
var spellcheck;
var queryMapping;

YAHOO.util.Event.addListener(window,'load',initSearch);

function initSearch() {
    try {
        window.keywords = escape(getMetaContent("LW.keywords"));
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/html?q='+window.keywords, window.querymapCallBack);
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/sfx/json?q='+window.keywords, window.findItCallBack);
       	YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q='+window.keywords, window.spellCheckCallBack);
       	YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results?id='+getMetaContent("LW.searchId"), window.showHitsCallback);
        var tabs = document.getElementById('eLibraryTabs').getElementsByTagName('li');
        var popIn = document.getElementById('popInContent');
        for (var i = 0; i  < tabs.length; i++) {
            if (tabs[i].className == 'eLibraryTab' || tabs[i].className == 'eLibraryTabActive') {
                var tab = tabs[i];
                var id = tab.id;
                var type = id.substring(0,id.indexOf('Tab'));
                var container = document.getElementById('eLibrarySearchResults');
                var result = new Result(type, tab, container);
                var content = null;
                if (tab.className == 'eLibraryTabActive') {
                    content = new Array();
                    for (var j = 0; j < container.childNodes.length; j++) {
                        content[j] = container.childNodes[j];
                    }
                    result.setContent(content);
                    window.activeResult = result;
                }
                tab.result = result;
                tab.activate = function(event) {
                    if (this.className != 'eLibraryTabActive') {
                        this.style.textDecoration = 'underline';
                        this.style.cursor = 'pointer';
                    }
                }
                tab.deactivate = function(event) {
                    this.style.textDecoration = 'none';
                    this.style.cursor = 'default';
                }
                tab.clicked = function(event) {
                    this.result.show();
                    YAHOO.util.Event.stopEvent(event);
                }
            }
        }
       	 spellcheck = new Spellcheck(getMetaContent("LW.source"));
    } catch(e) {
        window.handleException(e);
    }
}



function Result(type, tab, container) {
    if (null == type) {
        throw('null type');
    }
    if (null == tab) {
        throw ('null tab');
    }
    if (null == container) {
        throw ('null container');
    }
    this._type = type;
    this._tab = tab;
    this.container = container;
    this._url = '/././plain/search2/'+this._type+'.html?source='+this._type+'&keywords=';
    this._callback = {
        success:this.callbackSuccess,
        failure:handleFailure,
        argument: {
            result:this
        }
    }
    this._state = 'initialized';
}

Result.prototype.setContent = function(content) {
    if (null == content) {
        throw ('null content');
    }
    if(this._content == null)
    	this._content = content;
    else
    	this._content = this._content.concat(content);
    this._count = 0;
    for (var i = 0; i < this._content.length; i++) {
       if (this._content[i].nodeName.toLowerCase() == 'dl') {
           this._count = this._content[i].getElementsByTagName('dt').length;
           break;
       }
    }
    this.setTabCount(this._count);
    this._state = 'searched';
}


Result.prototype.callbackSuccess = function(o) {
    try {
        var result = o.argument.result;
        
        var bodyNodes = o.responseXML.getElementsByTagName('body')[0].childNodes;
        var content = new Array();
        for (var i = 0; i < bodyNodes.length; i++) {
            content[i] = window.importNodes(bodyNodes[i], true);
        }
        result.setContent(content);
        result.show();
    } catch(exception) {
        window.handleException(exception);
    }
}


Result.prototype.getContent = function() {
    try {
        if (this._state == 'initialized') {
            this._state = 'searching';
            var request = YAHOO.util.Connect.asyncRequest('GET', this._url+window.keywords, this._callback);
        } else if (this._state == 'searched') {
            this.show();
        } else if (this._state == 'searching') {
            alert('search in progress');
        }
    } catch(exception) {
        window.handleException(exception);
    }
}

Result.prototype.show = function() {
    try {
        if (this._state == 'initialized') {
            this.getContent();
        } else if (this._state == 'searching') {
            alert('search in progress');
        } else {
            window.spellcheck.setSource(this._type);
            window.activeResult.hide();
            this._tab.className = 'eLibraryTabActive';
            if(window.queryMapping && window.queryMapping.getContent())
	            this.container.appendChild(window.queryMapping.getContent());
	      
           	for (var i = 0; i < this._content.length; i++) {
                this.container.appendChild(this._content[i]);
            }
            window.activeResult = this;
        }
     
    } catch(exception) {
        window.handleException(exception);
    }
}


Result.prototype.hide = function() {
    try {
        while(this.container.childNodes.length > 0) {
            this.container.removeChild(this.container.lastChild);
        }
        this._tab.className = 'eLibraryTab';
    } catch(exception) {
        window.handleException(exception);
    }
}



Result.prototype.setTabCount = function(count) {
    try {
        var hitCount = this._tab.getElementsByTagName('span')[0];
        hitCount.textContent = count;
        hitCount.style.visibility = 'visible';
    } catch(exception) {
        window.handleException(exception);
    }
}




var showHitsCallback =
{
  success:showHits,
  failure:handleFailure
};


function showHits(o) {
	try 
	{
		var response = eval("("+o.responseText+")");
		for (var j = 0; j < response.results.tabs.length; j++)
		 {
			 var tabName = response.results.tabs[j].resource;
			 var tab = document.getElementById(tabName+"Tab")
			 if ( tab != undefined) 
				{
					var hitSpan = tab.getElementsByTagName('span')[0];
					var hits = response.results.tabs[j].hits;
					if(hitSpan != null && hits != "")
						hitSpan.innerHTML = hits;   
					var linkValue = response.results.tabs[j].url;
					if(linkValue != null && tab.getElementsByTagName('a')[0] != undefined)
						tab.getElementsByTagName('a')[0].href = linkValue;
					hitSpan.style.visibility = 'visible';
				}
		}
		var sleepingTime = 2000;
		var remainingTime = (new Date().getTime())-startTime;
		var status = response.results.status;
		if(status != 'successful' && ( remainingTime < 60*1000))
		{	// if time superior at 20 seconds the sleeping time equals 10 seconds 
			if(remainingTime > 20 *1000)
				sleepingTime = 10000;
			setTimeout( "getTabResult()", sleepingTime);
		}
	} catch (e) { window.handleException(e) }
}

function getTabResult()
{

try {
	  YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results?id='+getMetaContent("LW.searchId"), window.showHitsCallback);

    } catch(exception) { window.handleException(exception) }
}


var findItCallBack =
{
  success:showFindIt,
  failure:handleFailure	
};


function showFindIt(o)
{
	try {
		var findIt = eval("("+o.responseText+")");
		if (findIt.result != '')
		{
				var findItLink = document.getElementById("findItLink");
				findItLink.href = findIt.openurl;
				findItLink.innerHTML = findIt.result;
				var findItContainer = document.getElementById('findIt');
				findItContainer.style.display= 'inline';
				findItContainer.style.visibility = 'visible';
	    }
	
   	} catch(exception) { window.handleException(exception) }
}

var spellCheckCallBack =
{
  success:showSpellCheck//,
  //failure:do nothing iof google spellcheck is done we dosn't want a alert windows 
};

function showSpellCheck(o)
{
	try {
	    var spellCheckResponse = eval("("+o.responseText+")");
	    if (spellCheckResponse.suggestion) {
			var spellCheckContainer = document.getElementById("spellCheck");
			var spellCheckLink = document.getElementById("spellCheckLink");
			spellCheckContainer.style.display= 'inline';
			spellCheckContainer.style.visibility= 'visible';
		    window.spellcheck.setSuggestion(spellCheckResponse.suggestion, spellCheckLink);
		}
	
   	} catch(exception) { window.handleException(exception) }
}

function Spellcheck(currentTab)
{
	try {
		if(currentTab != undefined)
			this.source = currentTab;
	
	} catch(exception) { window.handleException(exception) }
}

Spellcheck.prototype.setSuggestion = function(suggestion, link)
{
	try {
		if (suggestion == null)
			throw('suggestion is null'); 
		this.suggestion = suggestion;
		link.innerHTML = suggestion;
		link.clicked = function(event)
		{
			return window.spellcheck.onclick(event, this);
		}	   	
    } catch(exception) { window.handleException(exception) }
}

Spellcheck.prototype.onclick = function(event, link)
{
	try {
			link.href = '/search2.html?keywords='+this.suggestion+'&source='+this.source;
	    } catch(exception) { window.handleException(exception) }
	   return false;
}


Spellcheck.prototype.setSource = function(source)
{
	try {
	    if (source != undefined)
			this.source = source;
    } catch(exception) { window.handleException(exception) }
}



var querymapCallBack =
{
  success:showQueryMapping//,
  //failure:handleFailure	 we don't want see the error message for example if the DTD is not found on the server 
};

 	

function showQueryMapping(o)
{
	if( o.responseXML.getElementsByTagName( 'ul')[0] != null)
	{
		var queryMappingResult = o.responseXML.getElementsByTagName( 'div')[0];	
		window.queryMapping = new QueryMapping( importNodes(queryMappingResult, true));
		window.activeResult.show();
    } 
}	


function QueryMapping(content)
{
	this.resourceUrl = "";
	this._counter = 0;
	this._content = content;
    var lis = 	this._content.getElementsByTagName( 'li');
    for(var i = 0; i < lis.length; i++) {
    	  lis[i].style.display = 'none';
    	  this.resourceUrl = this.resourceUrl +"&r="+lis[i].id;
    }
    this._callback = {
        success:this.successfulCallback,
        failure:handleFailure,
        argument: {
            queryMapping:this
        }
    }
   this.sendQueryMappingRequest();
}	

QueryMapping.prototype.successfulCallback = function(o) {
    var response = eval("("+o.responseText+")");
   	var queryMapping = o.argument.queryMapping; 
   	window.queryMapping.update(response);      	
    }

QueryMapping.prototype.update = function(response) {
	this.resourceUrl = "";
	this._counter++;
	var lis = 	this._content.getElementsByTagName( 'li');
    var resourceUrl="";
    for(var i = 0; i < lis.length; i++) {
    	var resourceId  = lis[i].id;
 	    if(response.resources[resourceId] != null)
		{
   			var status = response.resources[resourceId].status;
			if(status =='successful')
			{
				lis[i].style.display= 'block';
				lis[i].style.visibility = 'visible';
				var anchor = lis[i].getElementsByTagName('a');
				anchor[0].href = response.resources[resourceId].url;
				var span = document.createTextNode(":"+response.resources[resourceId].hits);
				lis[i].appendChild(span);
			}
			else
			 	this.resourceUrl = this.resourceUrl.concat("&r=").concat(resourceId);
		}
	}
	window.activeResult.show();
   	if(this._counter <7 && this.resourceUrl != "")
   		setTimeout( "window.queryMapping.sendQueryMappingRequest()", 10000);
}


QueryMapping.prototype.sendQueryMappingRequest = function() {
		YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/json?id='+getMetaContent("LW.searchId")+this.resourceUrl, this._callback);
    }



QueryMapping.prototype.getContent = function() {
       	return  this._content;
    }


 function importNodes(importedNode, deep){
        var newNode;
        if(importedNode.nodeType == 1) { // Node.ELEMENT_NODE
            newNode = document.createElement(importedNode.nodeName);
            for(var i = 0; i < importedNode.attributes.length; i++){
                var attr = importedNode.attributes[i];
                if (attr.nodeValue != null && attr.nodeValue != '') {
                    newNode.setAttribute(attr.name, attr.nodeValue);
                    if(attr.name == 'class'){
                		newNode.className = attr.nodeValue;
                	}
                }
            }
        } else if(importedNode.nodeType == 3) { // Node.TEXT_NODE
            newNode = document.createTextNode(importedNode.nodeValue);
        }
        
        if(deep && importedNode.hasChildNodes()){
            for(var i = 0; i < importedNode.childNodes.length; i++) {
                newNode.appendChild(
                    window.importNodes(importedNode.childNodes[i], true)
                );
            }
        }
        return newNode;
    }



function handleFailure(o){
	alert("status: "+o.status+ '\n' +"statusText "+o.statusText  );	
}
