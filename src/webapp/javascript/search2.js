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
        //YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?id='+getMetaContent("LW.searchId"), window.showHitsCallback);
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
        var sortBySelect = document.getElementById('sortBySelect');
        if (sortBySelect) {
            sortBySelect.change = sorteLibraryResults;
             YAHOO.util.Event.addListener(sortBySelect, 'change', handleChange);
        }
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
    this._sortBy = document.getElementById('sortBy');
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
            if (this._count <= 1) {
            	this._sortBy.style.display = 'none';
                this._sortBy.style.visibility = 'hidden';
            } else {
            	this._sortBy.style.display = 'inline';
                this._sortBy.style.visibility = 'visible';
            }
            for (var i = 0; i < this._content.length; i++) {
                this._container.appendChild(this._content[i]);
            }
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
	try {
		var uri = 'http://lane.stanford.edu/search-tab-result/ns';
		var rootNode = window.getElementsByTagName(o.responseXML,"", uri, 'search-tab-results')[0];
		var status = rootNode.getAttribute("status");
		var rows = window.getElementsByTagName(rootNode, "",uri,'resource');
		var tabs = document.getElementById('eLibraryTabs').childNodes;
		
		for (var i = 0; i  < rows.length; i++) 
		{
			var genre = rows[i].getAttribute("id");
			for (var j = 0; j < tabs.length; j++)
			 {
				if ( tabs[j].id != undefined &&  tabs[j].id == genre + 'Tab') 
				{
					var hitSpan = tabs[j].getElementsByTagName('span')[0];
					if(window.getElementsByTagName(rows[i],"",uri,'hits')[0] !=	undefined && window.getElementsByTagName(rows[i],"",uri,'hits')[0].firstChild != undefined)
					{
						hitSpan.innerHTML = window.getElementsByTagName(rows[i],"",uri,'hits')[0].firstChild.nodeValue;
						if(window.getElementsByTagName(rows[i],"",uri,'url')[0] != undefined && window.getElementsByTagName(rows[i],"",uri,'url')[0].firstChild != undefined)
						{
							var linkValue = window.getElementsByTagName(rows[i],"",uri,'url')[0].firstChild.nodeValue;
							if(linkValue != null && tabs[j].getElementsByTagName('a')[0] != undefined)
								tabs[j].getElementsByTagName('a')[0].href = linkValue;
						}
						hitSpan.style.visibility = 'visible';
						break;
					}
				}
			}
		}
		var sleepingTime = 2000;
		var remainingTime = (new Date().getTime())-startTime;
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
	  YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?id='+getMetaContent("LW.searchId"), window.showHitsCallback);

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
  failure:handleFailure	
};


function showQuerymap(o)
{
	var uri = 'http://www.w3.org/1999/xhtml';
	if( window.getElementsByTagName(o.responseXML,"", uri, 'body')[0] != undefined)
	{
		var querymapContainer = window.getElementsByTagName(o.responseXML,"", uri, 'div')[0];	
		window.querymapContent = querymapContainer; 
    	activeResult._container.appendChild(querymapContainer);
    }
}	



// sort results
//  relevance-sort = as returned by erdb 
//  alpha-sort = alpha sort done by sortByAlpha (extension of Array class)
//  relevance-sorted results are stored in relevanceSortedResults variable if alpha-sort is executed
//  the LWeLibNextSort cookie is used to track the appropriate *next* sort scheme
var relevanceSortedResults;
function sorteLibraryResults(){
try {
	var searchResults = document.getElementById('eLibrarySearchResults');
	
	var nextSort = '';
	if(searchResults.getAttribute('name') == 'relevance-sort'){
		nextSort = 'alpha-sort';
		searchResults.innerHTML = relevanceSortedResults;
		showeLibraryTab(eLibraryActiveTab);
	}
	else {
		nextSort = 'relevance-sort';
		relevanceSortedResults = document.getElementById('eLibrarySearchResults').innerHTML;

		for( var i = 0; i < eLibraryTabIDs.length; i++){
			var divID = eLibraryTabIDs[i];
			var div = document.getElementById(divID);
			var dl = document.getElementById(divID).getElementsByTagName('dl');

			var resultsArray = [];
			var resultsHTML = '';

			for(var p = 0; p < div.getElementsByTagName('dt').length; p++){
				resultsArray[p]=[div.getElementsByTagName('dt')[p].innerHTML,'<dt>' + div.getElementsByTagName('dt')[p].innerHTML + '</dt>' + '<dd>' + div.getElementsByTagName('dd')[p].innerHTML + '</dd>'];
			}
			resultsArray = resultsArray.sortByAlpha();

			for(p=0;p<resultsArray.length;p++){
				resultsHTML = resultsHTML + resultsArray[p][1];
			}
			
			var dlClassName = (dl.length) ? dl[0].className : '';
			div.innerHTML = '<dl class="' + dlClassName + '">' + resultsHTML + '</dl>';
		}
	}

	searchResults.setAttribute('name',nextSort);
	setCookie('LWeLibNextSort',nextSort);
	refreshPopInContent();
	
    } catch(exception) { window.handleException(exception) }
}



function handleFailure(o){
	alert("status: "+o.status+ '\n' +"statusText "+o.statusText  );	
}
