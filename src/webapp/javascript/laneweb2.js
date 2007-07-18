var keywords;
var results;

YAHOO.util.Event.addListener(window,'load',initSearch);

function initSearch(e)
{
    window.keywords = escape(document.getElementById('keywordresult').innerHTML.replace(/&amp;/g,'&'));
    YAHOO.util.Connect.asyncRequest('GET', '/././eresources/count?q='+window.keywords, window.showHitsCallback);
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
alert('failure');
searchType = null;
}

var showHitsCallback =
{
  success:showHits,
  failure:handleFailure
};

function showHits(o) {
try {
var prefix = 'sql';
var uri = 'http://apache.org/cocoon/SQL/2.0';
var rows = window.getElementsByTagName(o.responseXML,prefix,uri,'row');
var tabs = document.getElementById('eLibraryTabs').childNodes;
for (i = 0; i  < rows.length; i++) {
var genre = window.getElementsByTagName(rows[i],prefix,uri,'genre')[0].firstChild.nodeValue;
for (j = 0; j < tabs.length; j++) {
if (tabs[j].id == genre + 'Tab') {
var hitSpan = tabs[j].getElementsByTagName('span')[0];
hitSpan.innerHTML = window.getElementsByTagName(rows[i],prefix,uri,'hits')[0].firstChild.nodeValue;
hitSpan.style.visibility = 'visible';
break;
}
}
}
} catch (e) { alert(e.message) }
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