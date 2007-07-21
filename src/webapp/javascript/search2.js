var keywords;
var results;
var searchId;
var startTime = new Date().getTime();

var spellcheck;


function initSearch()
{
    window.keywords = escape(getMetaContent("LW.keywords"));
    YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck?q='+window.keywords, window.spellCheckCallBack);
    YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?q='+window.keywords, window.showHitsCallback);
    window.results = new Array();
    var tabs = document.getElementById('eLibraryTabs').getElementsByTagName('div');
    for (i = 0; i  < tabs.length; i++)
    {
        if (tabs[i].className == 'eLibraryTab')
        {
            var tab = tabs[i];
            var id = tab.id;
            var type = id.substring(0,id.indexOf('Tab'));
            var container = document.getElementById(type);
            var zeroHits = document.getElementById(type+'NoHitsText');
            var result = tabs[i].result = new Result(type, tab, zeroHits, container);
            window.results.push(result);
            tab.result = result;
            tab.onclick = function(event)
            {
                return this.result.onclick(event);
            }
            
        }        
    }
    spellcheck = new Spellcheck();
}



function Result(type, tab, zeroHits, container)
{
    if (type != undefined)
    {
        this._type = type;
        if (this._type == 'biotools')
        {
            this._baseUrl = '/././eresources/erdb?s=biotools&q=';
        }
        else
        {
            this._baseUrl = '/././eresources/erdb?t='+this._type+'&q=';
        }
    }
    if (tab != undefined)
    {
        this._tab = tab;
    }
    if (zeroHits != undefined)
    {
        this._zeroHits = zeroHits;
    }
    if (container != undefined)
    {
        this._container = container;
    }
    this._callback =
    {
        success:this.callbackSuccess,
        failure:this.callbackFailure,
        argument:
        {
            result:this
        }
    }
    this._state = 'initialized';
}
/*
Result.prototype.initialize() = function()
{
    if (this.currentList())
    {
        this._container.removeChild(currentList());
    }
    this.hide();
    this._state = 'initialized';
}
    */
Result.prototype.callbackSuccess = function(o)
{
    var result = o.argument.result;
    var container = result._container;
    var currentList = result.currentList();
    var newList = o.responseXML.getElementsByTagName('dl')[0];
    window.hideResults();
    if (currentList)
    {
        container.removeChild(currentList);
    }
    if (newList && newList.nodeName == 'dl')
    {
        container.appendChild(newList);
        result.setTabCount(newList.getElementsByTagName('dt').length);
    }
    else
    {
        result.setTabCount(0);
    }
    result._state = 'searched';
    result.show();
}
Result.prototype.callbackFailure = function(o)
{
    alert('callbackFailure');
}
Result.prototype.onclick = function(event)
{
    if (this._state == 'initialized')
    {
        var url = this._baseUrl+keywords;
        this._state = 'searching';
        var request = YAHOO.util.Connect.asyncRequest('GET', url, this._callback);
    }
    else if (this._state == 'searched')
    {
        window.hideResults();
        this.show();
    }
    else if (this._state == 'searching')
    {
        alert('search in progress');
    }
    window.spellcheck.setSource(this._type);
    return false;
}


Result.prototype.show = function()
{
    if (this.currentList())
    {
        this._zeroHits.style.display = 'none';
    }
    else
    {
        this._zeroHits.style.display = 'inline';
    }
    this._tab.className = 'eLibraryTabActive';
    this._container.style.display = 'block';
}
Result.prototype.hide = function()
{
    this._tab.className = 'eLibraryTab';
    this._container.style.display = 'none';
    this._zeroHits.style.display = 'none';
}
Result.prototype.currentList = function()
{
    return this._container.getElementsByTagName('dl')[0];
}
Result.prototype.setTabCount = function(count)
{
    var hitCount = this._tab.getElementsByTagName('span')[0];
    hitCount.textContent = count;
    hitCount.style.visibility = 'visible';
}
function hideResults()
{
    for (i = 0; i < results.length; i++)
    {
        results[i].hide();
    }
}



var handleFailure = function(o){
alert('tab callback failure');
searchType = null;
}


var showHitsCallback =
{
  success:showHits,
  failure:handleFailure
};


function showHits(o) {
//try {
	var uri = 'http://lane.stanford.edu/search-tab-result/ns';
	var rootNode = window.getElementsByTagName(o.responseXML,"", uri, 'search-tab-results')[0];
	searchId = rootNode.getAttribute("id");
	var status = rootNode.getAttribute("status");
	var rows = window.getElementsByTagName(rootNode, "",uri,'resource');
	var tabs = document.getElementById('eLibraryTabs').childNodes;
	
	for (i = 0; i  < rows.length; i++) 
	{
		var genre = rows[i].getAttribute("id");
		for (j = 0; j < tabs.length; j++)
		 {
			if ( tabs[j].id != undefined &&  tabs[j].id == genre + 'Tab') 
			{
				var hitSpan = tabs[j].getElementsByTagName('span')[0];
				if(window.getElementsByTagName(rows[i],"",uri,'hits')[0] != undefined && window.getElementsByTagName(rows[i],"",uri,'hits')[0].firstChild != undefined)
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
//} catch (e) { alert(e.message) }
}

function getTabResult()
{
	  YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?id='+window.searchId, window.showHitsCallback);
}



var spellCheckCallBack =
{
  success:showSpellCheck,
  //do nothing iof google spellcheck is done we dosn't want a alert windows 
};

function showSpellCheck(o)
{
	var uri = 'http://lane.stanford.edu/spellcheck/ns';
	if( window.getElementsByTagName(o.responseXML,"", uri, 'suggestion')[0] != undefined)
	{
		suggestion = window.getElementsByTagName(o.responseXML,"", uri, 'suggestion')[0].firstChild.nodeValue;	
		var spellCheckContainer = document.getElementById("spellCheck");
		var link = spellCheckContainer.getElementsByTagName('a')[0];
		link.innerHTML = suggestion;
		spellCheckContainer.style.display= 'inline';
		var initTab = getMetaContent("LW.source");
        window.spellcheck.init(initTab,suggestion, link);
    }
	
}

function Spellcheck()
{
}

Spellcheck.prototype.init = function(currentTab, suggestion, link)
{
	if(currentTab != undefined)
		this.source = currentTab;		
	 if (suggestion != undefined)
		this.suggestion = suggestion;
	link.clicked = function(event)
	{
		return window.spellcheck.onclick(event, this);
	}	   	
}

Spellcheck.prototype.onclick = function(event, link)
{
	link.href = '/search2.html?keywords='+this.suggestion+'&source='+this.source;
    return false;
}


Spellcheck.prototype.setSource = function(source)
{
    if (source != undefined)
		this.source = source;
}


function getElementsByTagName(node, prefix, uri, name)
{
    try
    {
        return node.getElementsByTagNameNS(uri,name);
    }
    catch (e)
    {
        return node.getElementsByTagName(prefix+':'+name);
    }

}