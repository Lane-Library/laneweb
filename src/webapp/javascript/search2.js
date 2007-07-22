var keywords;
var results;
var searchId;
var startTime = new Date().getTime();

var spellcheck;

YAHOO.util.Event.addListener(window,'load',initSearch);

function initSearch() {
try {
    window.keywords = escape(getMetaContent("LW.keywords"));
    YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck?q='+window.keywords, window.spellCheckCallBack);
    YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?q='+window.keywords, window.showHitsCallback);
    window.results = new Array();
    var tabs = document.getElementById('eLibraryTabs').getElementsByTagName('li');
    for (i = 0; i  < tabs.length; i++)
    {
        if (tabs[i].className == 'eLibraryTab' || tabs[i].className == 'eLibraryTabActive')
        {
            var tab = tabs[i];
            var id = tab.id;
            var type = id.substring(0,id.indexOf('Tab'));
            var result = new Result(type, tab, window.keywords);
            window.results.push(result);
            tab.result = result;
            var anchor = tab.getElementsByTagName('a')[0];
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
                this.result.clicked(event);
                YAHOO.util.Event.stopEvent(event);
            }
/*            tab.onclick = function(event)
            {
                return this.result.onclick(event);
            }*/
        }
    }
    spellcheck = new Spellcheck();
    var sortBySelect = document.getElementById('sortBySelect');
    if (sortBySelect) {
         sortBySelect.change = sorteLibraryResults;
         YAHOO.util.Event.addListener(sortBySelect, 'change', handleChange);
    }
}
    catch(e) {alert(e.message) }
    }



function Result(type, tab, keywords)
{
    try {
    if (type != undefined)
    {
        this._type = type;
        this._baseUrl = '/././plain/search2/'+this._type+'.html?source='+this._type+'&keywords=';
    }
    if (tab != undefined)
    {
        this._tab = tab;
    }

    this._keywords = keywords;
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
    } catch(exception) { alert(exception.message) }
}

Result.prototype.callbackSuccess = function(o)
{
try {
    var result = o.argument.result;
    
    result.bodyNodes = o.responseXML.getElementsByTagName('body')[0].childNodes;
    result._state = 'searched';
    result.show();
    
    } catch(exception) { alert(exception.message) }
}
Result.prototype.callbackFailure = function(o)
{
    alert('callbackFailure');
}
Result.prototype.clicked = function(event)
{
try {
    if (this._state == 'initialized')
    {
        var url = this._baseUrl+this._keywords;
        this._state = 'searching';
        var request = YAHOO.util.Connect.asyncRequest('GET', url, this._callback);
    }
    else if (this._state == 'searched')
    {
        this.show();
    }
    else if (this._state == 'searching')
    {
        alert('search in progress');
    }
    
    } catch(exception) { alert(exception.message) }
}


Result.prototype.show = function()
{
try {
    var container = document.getElementById('eLibrarySearchResults');
    for (var i = 0; i < this.bodyNodes.length; i++) {
        var node = document.importNode(this.bodyNodes[i],true);
        container.appendChild(node);
    }
    this._tab.className = 'eLibraryTabActive';
    
    } catch(exception) { alert(exception.message) }
}
Result.prototype.hide = function()
{
try {
    this._tab.className = 'eLibraryTab';
    
    } catch(exception) { alert(exception.message) }
}
Result.prototype.currentContent = function()
{
try {

    } catch(exception) { alert(exception.message) }
}
Result.prototype.setTabCount = function(count)
{
try {
    var hitCount = this._tab.getElementsByTagName('span')[0];
    hitCount.textContent = count;
    hitCount.style.visibility = 'visible';
    
    } catch(exception) { alert(exception.message) }
}



var handleFailure = function(o){
alert('tab callback failure');
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
} catch (e) { alert(e.message) }
}

function getTabResult()
{
try {
	  YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results.xml?id='+window.searchId, window.showHitsCallback);

    } catch(exception) { alert(exception.message) }
}



var spellCheckCallBack =
{
  success:showSpellCheck,
  failure:function() {alert('spellcheck failure')}
  //do nothing iof google spellcheck is done we dosn't want a alert windows 
};

function showSpellCheck(o)
{
try {
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
	
    } catch(exception) { alert(exception.message) }
}

function Spellcheck()
{
}

Spellcheck.prototype.init = function(currentTab, suggestion, link)
{
try {
	if(currentTab != undefined)
		this.source = currentTab;		
	 if (suggestion != undefined)
		this.suggestion = suggestion;
	link.clicked = function(event)
	{
		return window.spellcheck.onclick(event, this);
	}	   	
	
    } catch(exception) { alert(exception.message) }
}

Spellcheck.prototype.onclick = function(event, link)
{
try {
	link.href = '/search2.html?keywords='+this.suggestion+'&source='+this.source;
    return false;
    
    } catch(exception) { alert(exception.message) }
}


Spellcheck.prototype.setSource = function(source)
{
try {
    if (source != undefined)
		this.source = source;
		
    } catch(exception) { alert(exception.message) }
}


function getElementsByTagName(node, prefix, uri, name)
{
    try
    {
        return node.getElementsByTagNameNS(uri,name);
    }
    catch (e)
    {
        alert(e.message);
        return node.getElementsByTagName(prefix+':'+name);
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
	
    } catch(exception) { alert(exception.message) }
}