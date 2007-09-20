var keywords;
var startTime = new Date().getTime();
var activeResult;
var spellcheck;
var querymapContent;

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
    this._container = container;
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
            content[i] = document.importNode(bodyNodes[i], true);
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
            for (var i = 0; i < this._content.length; i++) {
                this._container.appendChild(this._content[i]);
            }
            if(window.querymapContent  != undefined)
             this._container.appendChild(window.querymapContent );
            window.activeResult = this;
        }
     
    } catch(exception) {
        window.handleException(exception);
    }
}


Result.prototype.hide = function() {
    try {
        while(this._container.childNodes.length > 0) {
            this._container.removeChild(this._container.lastChild);
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
			 tab = document.getElementById(tabName+"Tab")
			 if ( tab != undefined) 
				{
					var hitSpan = tab.getElementsByTagName('span')[0];
					var hits = response.results.tabs[j].hits;
					if(hitSpan != null && hits != "")
						hitSpan.innerHTML = hits;   
					hitSpan.style.visibility = 'visible';
				}
		}
		var sleepingTime = 2000;
		var runningTime = (new Date().getTime())-startTime;
		var status = response.results.status;
		if(status != 'successful' && ( runningTime < 60*1000))
		{	// if time superior at 20 seconds the sleeping time equals 10 seconds 
			if(runningTime > 20 *1000)
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
  success:showSpellCheck,
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


function getElementsByTagName(node, prefix, uri, name)
{
    try
    {
        return node.getElementsByTagNameNS(uri,name);
    }
    catch (e)
    {
        window.handleException(e);
        return node.getElementsByTagName(prefix+':'+name);
    }
}


var querymapCallBack =
{
  success:showQuerymap,
  //failure:handleFailure	 we don't want see the error message for example if the DTD is not found on the server 
};


function showQuerymap(o)
{

	var uri = 'http://www.w3.org/1999/xhtml';
	if( window.getElementsByTagName(o.responseXML,"", uri, 'body')[0] != undefined)
	{
		var querymapContainer = window.getElementsByTagName(o.responseXML,"", uri, 'div')[0];	
		window.querymapContent = querymapContainer;
		window.activeResult._container.appendChild(querymapContainer);
    }
}	


function handleFailure(o){
	alert("status: "+o.status+ '\n' +"statusText "+o.statusText  );	
}
