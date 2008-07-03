var searchTerms;
var startTime = new Date().getTime();
var activeResult;
var spellcheck;
var queryMapping;
var initialTabState;

YAHOO.util.Event.addListener(window,'load',initSearch);
        
function initSearch() {
        if(YAHOO.util.History && YAHOO.util.Dom.inDocument('yui-history-field') && YAHOO.util.Dom.inDocument('yui-history-iframe')){
            YAHOO.util.History.onReady(function () {
                var currentTabState = YAHOO.util.History.getCurrentState("aTab");
                activeTabStateChangeHandler (currentTabState);
            });
            window.initialTabState = YAHOO.util.History.getBookmarkedState("aTab") || getMetaContent("LW.source");
            YAHOO.util.History.register("aTab", window.initialTabState, activeTabStateChangeHandler);
            YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe"); 
        }
        window.searchTerms = escape(getMetaContent("LW.searchTerms"));
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/html?q='+window.searchTerms, window.querymapCallBack);
          YAHOO.util.Connect.asyncRequest('GET', '/././apps/sfx/json?q='+window.searchTerms, window.findItCallBack);
           YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q='+window.searchTerms, window.spellCheckCallBack );
           YAHOO.util.Connect.asyncRequest('GET', '/././content/search-tab-results?q='+window.searchTerms, window.showHitsCallback);
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
                else if (type == window.initialTabState){
                    YAHOO.util.Dom.getElementsByClassName('eLibraryTabActive')[0].result.hide();
                    result.show();
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
                    webtrends(this);
                    this.result.show();
                    (YAHOO.util.History) ? YAHOO.util.History.navigate("aTab", this.id.substring(0,this.id.indexOf('Tab'))) : ''; 
                    YAHOO.util.Event.stopEvent(event);
                }
            }
        }
            spellcheck = new Spellcheck(getMetaContent("LW.source"));
}


function Result(type, tab, container) {
    if (null == type) {
        window.log('Result(): type should not be null');
    }
    if (null == tab) {
        window.log('Result(): tab should not be null');
    }
    if (null == container) {
         window.log('Result(): contianer should not be null');
    }
    this._type = type;
    this._tab = tab;
    this.container = container;
    this._url = '/././plain/search/'+this._type+'.html?source='+this._type+'&q=';
    this._callback = {
        success:this.callbackSuccess,
        failure:window.handleFailure,
        argument: {
            result:this,
              file:"search.js",
              line:"Result"
        }
    }
    this._state = 'initialized';
    this.searchIndicator = document.getElementById('searchIndicator');
}

Result.prototype.setContent = function(content) {
    if (null == content) {
         window.log('Result.setContent(): content should not  be null');
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
        var result = o.argument.result;
        var bodyNodes = o.responseXML.getElementsByTagName('body')[0].childNodes;
        var content = new Array();
        for (var i = 0; i < bodyNodes.length; i++) {
            content[i] = window.importNodes(bodyNodes[i], true);
        }
        result.setContent(content);
        result.show();
        result.searchIndicator.style.visibility = 'hidden';
}


Result.prototype.getContent = function() {
        if (this._state == 'initialized') {
            this._state = 'searching';
            var request = YAHOO.util.Connect.asyncRequest('GET', this._url+window.searchTerms, this._callback);
            this.searchIndicator.style.visibility = 'visible';
        } else if (this._state == 'searched') {
            this.show();
        } else if (this._state == 'searching') {
            alert('search in progress');
        }
}

Result.prototype.show = function() {
        if (this._state == 'initialized') {
            this.getContent();
        } else if (this._state == 'searching') {
            alert('search in progress');
        } else {
            window.spellcheck.setSource(this._type);
            window.activeResult.hide();
            this._tab.className = 'eLibraryTabActive';
               for (var i = 0; i < this._content.length; i++) {
                   this.container.appendChild(this._content[i]);
           }
            window.activeResult = this;
        }
}


Result.prototype.hide = function() {
      while(this.container.childNodes.length > 0) {
          this.container.removeChild(this.container.lastChild);
      }
      this._tab.className = 'eLibraryTab';
}



Result.prototype.setTabCount = function(count) {
    var hitCount = this._tab.getElementsByTagName('span')[0];
    hitCount.textContent = count;
    hitCount.style.visibility = 'visible';
  
}


function activeTabStateChangeHandler (tabId) {
    var tab = document.getElementById(tabId + "Tab");
    if( tab.result._state == 'initialized' ){
        tab.result.getContent();
    }
    else if( tab.result._state == 'searched' ){
        tab.result.show();
    }
    else{
        tab.result._state = 'initialized';
    }

    var searchSelect = document.getElementById('searchSelect');
    for(var i  = 0; i<searchSelect.options.length; i++){
        if(searchSelect.options[i].value == tabId){
            searchSelect.selectedIndex = i;
            searchSelect.change();
            break;
        }
    }
}

function webtrends(tab)
{
    window.dcsMultiTrack('DCS.dcssip', window.getMetaContent('LW.host') ,'DCS.dcsuri','/plain/search/'+tab.result._type+'.html','DCS.dcsquery','source='+tab.result._type+'&keywords='+window.getMetaContent('LW.searchTerms'),'WT.ti',document.title,'DCSext.searchTerm',window.getMetaContent('LW.searchTerms'),'DCSext.search_type',window.getMetaContent('LW.source'),'WT.seg_1',window.getMetaContent('WT.seg_1'));
}        


var showHitsCallback =
{
  success:showHits,
  failure:handleFailure,
   argument:{file:"search.js", line:"showHitsCallback"} 
};


function showHits(o) {
    var response = eval("("+o.responseText+")");
    for (var j = 0; j < response.results.tabs.length; j++)
     {
         var tabName = response.results.tabs[j].resource;
         var tab = document.getElementById(tabName+"Tab")
         if ( tab != undefined) 
            {
                var hitSpan = tab.getElementsByTagName('span')[0];
                var hits = response.results.tabs[j].hits;
                if(hitSpan != null && hits!='')
                    hitSpan.innerHTML = hits;   
                hitSpan.style.visibility = 'visible';
            }
    }
    var sleepingTime = 2000;
    var remainingTime = (new Date().getTime())-startTime;
    var status = response.results.status;
    if(status != 'successful' && ( remainingTime <= 60*1000))
    {    // if time superior at 20 seconds the sleeping time equals 10 seconds
        if(remainingTime > 20 * 1000)
            sleepingTime = 10000;
        setTimeout( "YAHOO.util.Connect.asyncRequest('GET', '"+'/././content/search-tab-results?q='+window.searchTerms+'&rd='+Math.random()+"', window.showHitsCallback);", sleepingTime);
    }
}


var findItCallBack =
{
  success:showFindIt,
  failure:handleFailure ,
  argument:{file:"search.js", line:"findItCallBack"}    
};


function showFindIt(o)
{
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
}

function hideFindIt()
{
    var findItContainer = document.getElementById('findIt');
    findItContainer.style.display= 'none';
    findItContainer.style.visibility = 'hidden';
}

var spellCheckCallBack =
{
  success:showSpellCheck,
  failure:handleFailure,
  argument:{file:"search.js", line:"spellCheckCallBack"} 
};

function showSpellCheck(o)
{
    var spellCheckResponse = eval("("+o.responseText+")");
    if (spellCheckResponse.suggestion) {
        hideFindIt();
        var spellCheckContainer = document.getElementById("spellCheck");
        var spellCheckLink = document.getElementById("spellCheckLink");
        spellCheckContainer.style.display= 'inline';
        spellCheckContainer.style.visibility= 'visible';
        window.spellcheck.setSuggestion(spellCheckResponse.suggestion, spellCheckLink);
    }
}

function Spellcheck(currentTab)
{
    if(currentTab != undefined)
        this.source = currentTab;
}

Spellcheck.prototype.setSuggestion = function(suggestion, link)
{
    if (suggestion == null)
        window.log('Spellcheck.setSuggestion(): suggestion should not be null'); 
    this.suggestion = suggestion;
    link.innerHTML = suggestion;
    link.clicked = function(event)
    {
        return window.spellcheck.onclick(event, this);
    }
}

Spellcheck.prototype.onclick = function(event, link)
{
    link.href = '/search.html?q='+this.suggestion+'&source='+this.source;
       return false;
}


Spellcheck.prototype.setSource = function(source)
{
    if (source != undefined)
        this.source = source;
}



var querymapCallBack =
{
  success:showQueryMapping,
  failure:handleFailure,
  argument:{file:"search.js", line:"queryMapping"}
};

     

function showQueryMapping(o)
{
    if( o.responseXML.getElementsByTagName( 'ul')[0] != null)
    {
        var queryMappingResult = o.responseXML.getElementsByTagName( 'div')[0];    
        window.queryMapping = new QueryMapping( importNodes(queryMappingResult, true));
        window.activeResult.show();
        var queryMappingContainer = document.getElementById('queryMapping');
        if(queryMappingContainer && window.spellcheck.suggestion == null){
            hideFindIt();
            queryMappingContainer.appendChild(window.queryMapping.getContent());
        }    
    } 
}    


function QueryMapping(content)
{
    this.resourceUrl = "";
    this._counter = 0;
    this._content = content;
    new YAHOO.widget.Tooltip(YAHOO.util.Dom.generateId(), { context:'qmTip', width:'170px' } )
    var lis =     this._content.getElementsByTagName( 'li');
    for(var i = 0; i < lis.length; i++) {
          //lis[i].style.display = 'none';
          this.resourceUrl = this.resourceUrl +"&r="+lis[i].id;
    }
    this._callback = {
        success:this.successfulCallback,
        failure:window.handleFailure,
        argument: {
            queryMapping:this,
            file:"search.js",
              line:"QueryMapping()"
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
    var lis =     this._content.getElementsByTagName( 'li');
    var resourceUrl="";
    for(var i = 0; i < lis.length; i++) {
        var resourceId  = lis[i].id;
         if(response.resources[resourceId] != null)
        {
            var anchor = lis[i].getElementsByTagName('a');
            anchor[0].href = response.resources[resourceId].url;
            anchor[0].title = 'QueryMapping: ' + anchor[0].innerHTML;
               var status = response.resources[resourceId].status;
            if(status =='successful')
            {
                var span = document.createTextNode(": "+response.resources[resourceId].hits);
                lis[i].appendChild(span);
            }
            else
                 this.resourceUrl = this.resourceUrl.concat("&r=").concat(resourceId);
        }
    }
    window.activeResult.show();
       if(this._counter < 19 && this.resourceUrl != "")
       {    
            var sleepingTime = 2000;
        if(this._counter > 15 ) //time sleepingtime (2 seconds) * 15 = 30 seconds
               sleepingTime = 10000;
           setTimeout( "window.queryMapping.sendQueryMappingRequest()", sleepingTime);
       }
}


QueryMapping.prototype.sendQueryMappingRequest = function() {
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/json?q='+window.searchTerms+this.resourceUrl+'&rd='+Math.random(), this._callback);
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
