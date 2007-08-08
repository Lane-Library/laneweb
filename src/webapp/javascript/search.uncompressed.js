var date = new Date();
var startTime = date.getTime();

/**************************
// create GLOBAL object and attributes
*/
var GLOBALS = new Object();
GLOBALS.basePath = '/./.';
GLOBALS.httpRequestInterval = '1500';
GLOBALS.incrementalSearchWait = '2500'; //time in ms between start of metasearch and initial response
GLOBALS.metasearchProxyPath = GLOBALS.basePath + '/content/search-proxy';
GLOBALS.needsProxy = getMetaContent(document,'lw_proxyLinks');
//GLOBALS.proxyPrefix = 'http://laneproxy.stanford.edu/login?url=';
GLOBALS.proxyPrefix = GLOBALS.basePath + '/secure/login.html?url=';
GLOBALS.searchPath = GLOBALS.basePath + '/search.html';

/**************************
// send all error events to errorLogger 
// errorLogger writes events to web log
*/
window.onerror = errorLogger;
function errorLogger(message, url, line){
	var errorImg = document.createElement('img');
	errorImg.src = GLOBALS.basePath + '/javascript/ErrorLogger.js?url=' + url + '&line=' + line + '&msg=' + message;
	errorImg.className = 'hide';
	return false;
}

/**************************
// slice eLibrary (LaneConnex) results into format tabs
*/
var eLibraryTabLabels=new Array('All','eJournals','Databases','eBooks','Biotools','Videos','medCalcs','Lane FAQs');
var eLibraryTabIDs=new Array('all','ej','database','book','biotools','video','cc','faq');
var eLibraryResultCounts = [];
var eLibraryActiveTab = null;

// returns number of dt tags foudn within tabID
function geteLibraryTabCount(tabID){
	var tabResultLinks = 0;
	if(document.getElementById(tabID)){
		tabResultLinks = document.getElementById(tabID).getElementsByTagName('dt').length;
	}
	return tabResultLinks;
}

// writes format-based tabs to document
function initeLibraryTabs(){
	for(var i = 0; i < eLibraryTabIDs.length; i++){
		eLibraryResultCounts[eLibraryTabIDs[i]] = geteLibraryTabCount(eLibraryTabIDs[i]);
	}

	var bar = '';
	for(var i = 0; i < eLibraryTabLabels.length; i++){
		var elementContainerForDisplayText = ''; // get text for title attribute
		if(document.getElementById(eLibraryTabIDs[i] + "SearchTagline")){
			elementContainerForDisplayText = document.getElementById(eLibraryTabIDs[i] + "SearchTagline").innerHTML;		
		}
		var webtrendsCall = ''; // add webtrends call only if dcsMultiTrack() is defined
		if(isDefined(window,'dcsMultiTrack')){
			webtrendsCall = "dcsMultiTrack('WT.ti','LaneConnex search " + eLibraryTabIDs[i] + " tab','DCSext.keywords','"+keywords+"','DCSext.tab_view','" + eLibraryTabIDs[i] + "');";
		}
		bar = bar + '<div id="' + eLibraryTabIDs[i] + 'Tab" class="eLibraryTab" title="' + elementContainerForDisplayText + '" name="' + eLibraryTabIDs[i] + '" onclick="javascript:showeLibraryTab(\'' + eLibraryTabIDs[i] + '\');' + webtrendsCall + '">' + eLibraryTabLabels[i] + '<br /><span class="tabHitCount">' + intToNumberString(eLibraryResultCounts[eLibraryTabIDs[i]]) + '</span></div>';
	}
	document.getElementById('eLibraryTabs').innerHTML = bar;
}

// makes "tab" active
function showeLibraryTab(tab){
	// if active tab not specified, direct parameter makes source trump over cookie, then LWeLibSource cookie, then source parameter alone in URL, then default to all
	if (!tab){
		tab = 'all'; // default to all
		if( (getQueryContent('direct') == '1' || getQueryContent('direct') == 'true') && eLibraryTabIDs.contains(getQueryContent('source'))  ){
			tab = getQueryContent('source');
		}
		else if( readCookie('LWeLibSource') && eLibraryTabIDs.contains(readCookie('LWeLibSource')) ){
			tab = readCookie('LWeLibSource');
		}
		else if( eLibraryTabIDs.contains(getQueryContent('source')) ){
			tab = getQueryContent('source');
		}
	}

	// swap active tab link out
	tabLinks = document.getElementById('eLibraryTabs').getElementsByTagName('div');
	for (var i = 0; i < tabLinks.length; i++){
		if(tabLinks[i].getAttribute('id') == tab + 'Tab'){
			tabLinks[i].className = 'eLibraryTabActive';
		}
		else{
			tabLinks[i].className = 'eLibraryTab';
		}
	}

	var searchResults = document.getElementById('eLibrarySearchResults');
	var searchResultDivs = document.getElementById('eLibrarySearchResults').getElementsByTagName('div');

	for (var i = 0; i < searchResultDivs.length; i++){
		if(searchResultDivs[i].getAttribute('id') == tab){
			searchResultDivs[i].className = '';
		}
		else{
			searchResultDivs[i].className = 'hide';
		}
	}

	// switch searchForm selected value to appropriate tab value
	searchFormSelect(tab);

	eLibraryActiveTab = tab;
	refreshPopInContent();
	setCookie('LWeLibSource',tab);
}

// sort results
//  relevance-sort = as returned by erdb 
//  alpha-sort = alpha sort done by sortByAlpha (extension of Array class)
//  relevance-sorted results are stored in relevanceSortedResults variable if alpha-sort is executed
//  the LWeLibNextSort cookie is used to track the appropriate *next* sort scheme
var relevanceSortedResults;
function sorteLibraryResults(){
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
}

// determines and displays relevant content below format tabs
//	content includes: SFX and spelling results if any; sort drop-down; TabTipText (box appearing right of results); zeroResultsText 
function refreshPopInContent(){
	var popInContent = '';

	if(document.getElementById('popInContent')){ 
		document.getElementById('popInContent').className = 'hide';
	}

	/* content developers deleted
	if(document.getElementById(eLibraryActiveTab + "TabBasicText")){
		popInContent = document.getElementById(eLibraryActiveTab + "TabBasicText").innerHTML;
	}


	// if zero results for active tab, display zeroResultsText
	if(eLibraryResultCounts[eLibraryActiveTab] == 0 && document.getElementById(eLibraryActiveTab + "TabZeroResultsText")){
		popInContent = document.getElementById(eLibraryActiveTab + "TabZeroResultsText").innerHTML;
	}
	*/
	
	// if results, add sort-by drop-down
	if(eLibraryResultCounts[eLibraryActiveTab] != 0){
		var options = new Array('Relevance','A-Z');

		var optionsHtml = '';
		if(document.getElementById('eLibrarySearchResults').getAttribute('name') == 'relevance-sort'){
			optionsHtml = '<option>' + options[0] + '</option><option selected="true">' + options[1] + '</option>';
		}
		else {
			optionsHtml = '<option selected="true">' + options[0] + '</option><option>' + options[1] + '</option>';
		}
		var webtrendsCall = ''; // add webtrends call only if dcsMultiTrack() is defined
		if(isDefined(window,'dcsMultiTrack')){
			webtrendsCall = "dcsMultiTrack('WT.ti','LaneConnex sort by click: "+  readCookie('LWeLibNextSort') +"','DCSext.keywords','"+keywords+"');";
		}
		popInContent += 'Sorted by <select name="sortBy" onchange="'+webtrendsCall+'sorteLibraryResults();" style="font-size: 95%; font-weight: 400;">' + optionsHtml + '</select>&nbsp;&nbsp;&nbsp;&nbsp;';
	}
	// if zero results for active tab AND no SFX results, display zeroResultsText
	else if ( document.getElementById(eLibraryActiveTab + "TabZeroResultsText") 
			  && !(document.getElementById('sfxResults') && (eLibraryActiveTab == 'all' || eLibraryActiveTab == 'ej') )
			 ){
		popInContent = document.getElementById(eLibraryActiveTab + "TabZeroResultsText").innerHTML;
	}

	if(document.getElementById('sfxResults') && (eLibraryActiveTab == 'all' || eLibraryActiveTab == 'ej') ){ 
		popInContent += document.getElementById('sfxResults').innerHTML;
	}

	// spell results trump all
	if(document.getElementById('spellResults')){ 
		popInContent = document.getElementById('spellResults').innerHTML;
	}

	if(popInContent != ''){
		document.getElementById('popInContent').innerHTML = popInContent;
		document.getElementById('popInContent').className = 'popInContent';
	}	

	// show tabTip if any 
	//if(document.getElementById(eLibraryActiveTab + "TabTipText") && eLibraryResultCounts[eLibraryActiveTab] != 0){ //[*and* more than zero results]
	if(document.getElementById(eLibraryActiveTab + "TabTipText") ){
		var thisTabText = document.getElementById(eLibraryActiveTab + "TabTipText").innerHTML;
		document.getElementById("tabTip").innerHTML = thisTabText;
		document.getElementById("tabTip").className = 'tabTip';
	}
	else{
		document.getElementById("tabTip").className = 'hide';
	}
}
// end slice eLibrary (LaneConnex) results into format tabs



/**************************
// XMLHttpRequest handler class
// includes result processing and display
*/
function IOClient() {};
IOClient.prototype = {
	request: null,
	type: null,
	url: null,

	init: function (type, url) {
		this.type = type;
		this.url = url.replace(/amp;/g,''); // JTidy replaces & w/ &amp; ... &amp; becomes &amp;amp;

		//sniff for IE on the Mac
		if ( navigator.userAgent.indexOf('Mac') > -1 && (navigator.appVersion.indexOf('MSIE 5') > -1 || navigator.appVersion.indexOf('MSIE 6') > -1) ){
			alert("Unsupported browser");
			window.location.href= GLOBALS.basePath + '/howto/index.html?id=_869';
		}

		if (window.XMLHttpRequest) {
			this.request = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) {
			this.request = new ActiveXObject('Msxml2.XMLHTTP');
		}
		else {
			alert("unsupported browser");
		}
	},

	get: function() {
		var self = this;

        this.request.onreadystatechange = function() {
            self.readyStateChange(self);
        }
		this.request.open("GET", this.url, true);
		if(this.delay){
			setTimeout("self.request.send(null);",this.delay);
		}
		else{
			this.request.send(null);
		}
	},

	readyStateChange: function(client) {
		if (client.request.readyState == 4) {
			if (client.request.status == 200) {
				this.process();
			}
		}
	},

	process: function() {
		switch(this.type){

			/**************************
			// elib-meta:
			//	get spelling suggestions
			//	fetch PubMed, LOIS and Google results
			// 	write hit counts to clinicalMetaCount and researchMetaCount divs
			//	times out after 60 seconds
			*/
			case "elib-meta":
				var xmlObj = this.request.responseXML.documentElement;
				var engines = xmlObj.getElementsByTagName('engine');

				var sessionID = xmlObj.getAttribute('id');
				var searchStatus = xmlObj.getAttribute('status');
				var queryContents = xmlObj.getElementsByTagName('query')[0].firstChild.data;
				var spellSuggestion = (xmlObj.getElementsByTagName('spell').length > 0) ? xmlObj.getElementsByTagName('spell')[0].firstChild.data : null;

				if(spellSuggestion && !document.getElementById('spellResults')){
					var html = 'Did you mean: <a href="' + GLOBALS.searchPath + '?keywords=' + escape(spellSuggestion) + '"><i><strong>' + spellSuggestion + '</strong></i></a><br />';

					//create new spellResults div (rely on refreshPopInContent to display)
					var body = document.getElementsByTagName("body").item(0);
					var spellDiv = document.createElement('div');
					spellDiv.className = 'hide';
					spellDiv.innerHTML = html;
					spellDiv.setAttribute('id','spellResults');
					body.appendChild(spellDiv);

					refreshPopInContent();

					// hide tip box ... or should this go into refreshPopInContent?
					// here only hides at popIn and if user continues to click, tip reappears (good?)
					//if (document.getElementById('tabTip')){
					//	document.getElementById('tabTip').className = 'hide';
					//}
				}
				
				if(searchStatus == "successful" || searchStatus =="running"){
					var clinSuccessCount = 0;
					var resSuccessCount = 0;
					var clinHits = 0;
					var resHits = 0;

					// put metasearch session ID into clin/res links so search doesn't need to be re-executed
					document.getElementById('clinicalMetaCount').parentNode.href = GLOBALS.basePath + '/search.html?source=clinical&id=' + sessionID + '&keywords=' + keywords;
					document.getElementById('researchMetaCount').parentNode.href = GLOBALS.basePath + '/search.html?source=research&id=' + sessionID + '&keywords=' + keywords;

					// put metasearch session ID and keywords into cookie so at browser back button, search session can be reused
					setCookie('LWeLibMetaState','id='+sessionID+'&keywords='+keywords);

					for( var i = 0; i < engines.length; i++){
						var resources = engines[i].getElementsByTagName('resource');
						for( var j = 0; j < resources.length; j++){

							if(resources[j].getElementsByTagName('hits').length > 0 && resources[j].getElementsByTagName('hits')[0].firstChild.data > 0){
								if(GLOBALS.clinicalEngines.contains(resources[j].getAttribute('id'))){
									clinHits += parseInt(resources[j].getElementsByTagName('hits')[0].firstChild.data);
									clinSuccessCount++;
								}
								if(GLOBALS.researchEngines.contains(resources[j].getAttribute('id'))){
									resHits += parseInt(resources[j].getElementsByTagName('hits')[0].firstChild.data);
									resSuccessCount++;
								}
							}

							var status = engines[i].getAttribute('status');
							if(status == 'successful' && (parseInt(clinHits) > 0||parseInt(resHits) > 0) ){
								document.getElementById('clinicalMetaCount').innerHTML = intToNumberString(clinSuccessCount);
								document.getElementById('researchMetaCount').innerHTML = intToNumberString(resSuccessCount);
							}

							if(document.getElementById(resources[j].getAttribute('id') + 'SearchResults') 
								&& resources[j].getElementsByTagName('hits').length > 0 ){
								//&& resources[j].getElementsByTagName('hits')[0].firstChild.data > 0){
								//document.getElementById(resources[j].getAttribute('id') + 'SearchResults').innerHTML = "<a target='new' href='" + resources[j].getElementsByTagName('url')[0].firstChild.data + "'>" + resources[j].getElementsByTagName('description')[0].firstChild.data + '<br /><span class="tabHitCount">' + intToNumberString(resources[j].getElementsByTagName('hits')[0].firstChild.data) + '</span></a>';
								var container = document.getElementById(resources[j].getAttribute('id') + 'SearchResults');
								container.getElementsByTagName('a')[0].href = resources[j].getElementsByTagName('url')[0].firstChild.data;
								container.getElementsByTagName('a')[0].innerHTML = resources[j].getElementsByTagName('description')[0].firstChild.data + '<br /><span class="tabHitCount">' + intToNumberString(resources[j].getElementsByTagName('hits')[0].firstChild.data) + '</span>';

								if(resources[j].getAttribute('id') == 'google'){
									document.getElementById(resources[j].getAttribute('id') + 'SearchResults').className = 'metaSearchResultsRightCorner';
								}
								else{
									document.getElementById(resources[j].getAttribute('id') + 'SearchResults').className = 'metaSearchResults';
								}
							}
						}
					}
				}

				//halt search after 60 seconds
				var newDate = new Date();
				if ( newDate.getTime()-startTime > 60*1000){
					searchStatus = 'halted';
				}

				if (searchStatus == "running" ){
					Object.neweLibMetaRequest = new IOClient();
					Object.neweLibMetaRequest.init('elib-meta',GLOBALS.metasearchProxyPath + '?id=' + sessionID + "&secs=" + newDate.getSeconds());

					setTimeout("Object.neweLibMetaRequest.get();",GLOBALS.httpRequestInterval);
					return 0;
				}
			break;

			/**************************
			// Incremental metasearch results (clinical, peds, research searches)
			//  processing for XHTML version of incremental search results
			//  full display version of results (headers, footers, etc.) used so that proxy prefixes are applied correctly (could just add proxy logic to ajax-search.xsl) 
			//  NOTE: string2dom w/in IE engine requires strict XHTML validity
			//  NOTE: all of this should be replaced with a version that works on XML version of metasearch results (with proxy links added via XSLT)
			*/ 
			case "incremental":
				var dom = string2dom(this.request.responseText);
				var response = dom.documentElement;

				var divs = response.getElementsByTagName('div');

				for (var d = 0; d < divs.length; d++){
					if(divs[d].getAttribute('id') == 'incrementalSearchResults'){
						var newResults = divs[d].getElementsByTagName('li');
						break;
					}
				}

				var oldResults = document.getElementById('incrementalSearchResults').getElementsByTagName('li');
				document.getElementById('incrementalSearchResults').className = 'unhide'; //display results
								
				var searchStatus = getMetaContent(response,'lw_searchParameters','status');
				var searchTerm = getMetaContent(response,'lw_searchParameters','query');
				var resultsProgressBar = document.getElementById('incrementalResultsProgressBar');
				var resultsDetails = document.getElementById('incrementalResultsDetails');

				var hitsFoundInSourceCount = 0;
				var sourcesCompleteCount = 0;
				var finished = true;
				
				for (var i = 0; i < newResults.length; i++) {
				  var oldAnchor = oldResults[i].getElementsByTagName('a')[0];
				  var newAnchor = newResults[i].getElementsByTagName('a')[0];
				  var newStatus = newAnchor.getAttribute('type');
				
				  if (newStatus == 'running') {
				    finished = false;
				  }
				  else{
				  	sourcesCompleteCount++;
				  }

				  // if old and new URLs differ, use new one
				  //oldAnchor.href = (oldAnchor.href != newAnchor.href) ? newAnchor.href : oldAnchor.href;
				  if(oldAnchor.getAttribute('href') != newAnchor.getAttribute('href')){
					// retarded fix for IE 7: http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
					// grab innerHTML of anchor before setting href, then copy back to oldAnchor (below)
					var text = oldAnchor.innerHTML;

					// Safari doesn't decode ampersands in URLs
					if(navigator.appVersion.indexOf("Safari")>-1){
						oldAnchor.setAttribute('href',newAnchor.getAttribute("href").replace(/&#38;/g,'&'));	
					}
					else{
						oldAnchor.setAttribute('href',newAnchor.getAttribute('href'));	
					}
					oldAnchor.innerHTML = text;
				  }

				  // hide result items if engine is still running or it returned a zero hit count
				  // otherwise, show engine name and make sure parent h3 is displayed
				  if ( newStatus == 'running' 
				  	|| newResults[i].getElementsByTagName('span')[0].childNodes[0].nodeValue == 0){
				    oldResults[i].className = 'hide';
				  }
				  else{
				    // otherwise, show engine name and make sure parent h3 is displayed
				    oldResults[i].parentNode.parentNode.getElementsByTagName('h3')[0].className = '';
				    oldResults[i].className = '';
				    hitsFoundInSourceCount++;
				  }

				  // write engine result counts
				  //  only write count when: engine status is successful and status of engine has changed
				  if (newStatus == 'successful' && oldAnchor.getAttribute('type') != newStatus){
					oldAnchor.setAttribute('type',newStatus);// = newStatus;
					if (newResults[i].getElementsByTagName('span').length > 0) {
						var hitCount = newResults[i].getElementsByTagName('span')[0].childNodes[0].nodeValue;
						if(hitCount == '0' && newStatus == 'successful') { 
							hitCount = ' 0';
						}
						oldResults[i].appendChild(document.createTextNode(hitCount));
					}
				  }
				}

				//halt search after 60 seconds or if user halted
				var newDate = new Date();
				if ( newDate.getTime()-startTime > 60*1000 || haltIncremental ){
					finished = true;
				}
				
				// progress bar writing
				if(!finished){
					var width = 0;
					if(newResults.length > 0 && sourcesCompleteCount > 0){
				  		width = 100 * (sourcesCompleteCount / newResults.length);
					}
					else{
						width = 1;
					}
					resultsProgressBar.innerHTML = '<table><tr><td nowrap>Still searching...</td><td nowrap><div style="position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;"><img width="' + width + '%" height="15" src="' + GLOBALS.basePath + '/images/templates/default/incrementalResultsProgressBar.gif" alt="progress bar" /></div></td><td nowrap>&nbsp;' + sourcesCompleteCount + ' of ' + newResults.length + ' sources searched. <a href="javascript:haltIncremental=true;void(0);">Stop Search</a></td></tr></table>';
					setTimeout("getIncrementalResults();",GLOBALS.httpRequestInterval);
					return 0;
				}
				else{
					resultsProgressBar.innerHTML = '';
					if(newResults.length > hitsFoundInSourceCount){
				  		resultsProgressBar.innerHTML = 'Results in <strong>' + hitsFoundInSourceCount + '</strong> of <strong>' + newResults.length + '</strong> sources for <strong>' + searchTerm + '</strong> [<a id="zerotoggle" href="javascript:toggleIncrementalZeros(\'true\');">Show Details</a>]';
				  	}
				  	else if(newResults.length == hitsFoundInSourceCount){
						resultsProgressBar.innerHTML = 'Results in <strong>' + hitsFoundInSourceCount + '</strong> of <strong>' + newResults.length + '</strong> sources contain <strong>' + searchTerm + '</strong>';
				  	}
				}
			break;

			/**************************
			// sfx results processing [eLibrary pop-in]
			*/
			case "sfx":
				var response = this.request.responseXML.documentElement;
				var openurl = response.getElementsByTagName('openurl')[0].firstChild.data;
				var result = response.getElementsByTagName('result')[0].firstChild.data;

				if(result != 0 ){
					var webtrendsCall = ''; // add webtrends call only if dcsMultiTrack() is defined
					if(isDefined(window,'dcsMultiTrack')){
						var dcsquery = openurl.substring(openurl.indexOf('?')+1,openurl.length);
						webtrendsCall = "onclick=\"dcsMultiTrack('DCS.dcssip','sfx.stanford.edu','DCS.dcsuri','local','DCS.dcsquery','" + dcsquery + "','WT.ti','SFX','DCSext.keywords','" + keywords + "','DCSext.offsite_link','1');\"";
					}
					var html = 'FindIt@Stanford eJournal: <a title="Fulltext access to ' + result + '" href="' + openurl + '" ' + webtrendsCall + '><b>' + result.replace(/ \[.*\]/,'') + '</b></a><br />';

					//create new sfxResults div (rely on refreshPopInContent to display)
					var body = document.getElementsByTagName("body").item(0);
					var sfxDiv = document.createElement('div');
					sfxDiv.className = 'hide';
					sfxDiv.innerHTML = html;
					sfxDiv.setAttribute('id','sfxResults');
					body.appendChild(sfxDiv);

					refreshPopInContent();
				}
			break;
		}
	}
}


/**************************
// useful functions
*/
	// extensions to Array class
	Array.prototype.contains = function(searchValue){
		for (var i = 0, elms = this.length; i < elms && this[i] !== searchValue; i++) ;
			return i < elms;
	}
	Array.prototype.sortByAlpha = function(){
		var LCArray = [];
		var hash = [];
	
		var nonFilingChars = /^(a|an|the|de|die|la|le|los|las|les) /;
	
		for(var i = 0; i < this.length; i++){
			LCArray[LCArray.length] = this[i].toString().toLowerCase().replace(nonFilingChars,'');
			hash[this[i].toString().toLowerCase().replace(nonFilingChars,'')] = i;
		}
	
		LCArray.sort();
	
		var newArray = [];
	
		for(var i = 0; i < LCArray.length; i++){
			newArray[i] = this[hash[LCArray[i]]];
		}
		return newArray;
	} 
	
	function isDefined(object, variable){
		return (typeof(eval(object)[variable]) == 'undefined')? false: true;
	}
	
	// clean up keywords string: 
	// 	replace &amp; w/ & (i.e. undo JTidy)
	//  URIescape
	function cleanKW(keywords){
		keywords = escape(keywords.replace(/&amp;/g,'&'));
		return keywords;
	}
	
	// getMetaContent Usage:
	//  <meta name="lw_parameters" content="foo=bar;bar=foo;x=y"/>
	//  <meta name="search_id" content="12"/>
	//  getMetaContent(document,'lw_parameters','foo', ';', '=')
	//  getMetaContent(document,'search_id')
	//
	function getMetaContent(node, name, paramName, paramDelim, valueDelim) {
		var value = '';
		if(!paramDelim){
			paramDelim = ';';
		}
		else if(paramDelim == '&amp;'){
			paramDelim = '&';
		}
		if(!valueDelim){
			valueDelim = '=';
		}
		var metaTags = node.getElementsByTagName('meta');
		for (var i = 0; i < metaTags.length; i++) {
			if (metaTags[i].getAttribute('name') == name) {
				if (paramName){
					var pairs = [];
					pairs = metaTags[i].getAttribute('content').split(paramDelim);
					for (var y = 0; y < pairs.length; y++){
						var pair = [];
						pair = pairs[y].split(valueDelim);
						if (pair[0] == paramName){
							value = pair[1];
						}
					}
				}
				else{
					value = metaTags[i].getAttribute('content');
				}
			}
		}
		return value;
	}
	
	// exract parameter value from a URI-like query string
	// if no string passed, assume query string of document
	function getQueryContent(paramName,queryString) {
		if(!queryString){
			queryString = location.search;
		}
		paramName+='=';

		if ( queryString.length > 0 ) {
			var startParam = queryString.indexOf( paramName );
			if ( startParam != -1 ) {
				startParam += paramName.length;
				var endParam = queryString.indexOf ( "&" , startParam );
				if ( endParam == -1 ) {
					endParam = queryString.length
				}
				return unescape ( queryString.substring ( startParam, endParam) );
			}
		}
		return 0;
	}
	
	// add formatting (commas when > 999) to integers
	function intToNumberString(number){
		number = number.toString();
		var pattern = /(\d+)(\d{3})/;
		while (pattern.test(number)) {
			number = number.replace(pattern, '$1' + ',' + '$2');
		}
		return number;
	}
	
	function toggleNode(linkNode, toggleNode, onText, offText, additionalInvertedToggleID){
		if(toggleNode.className != 'hide'){
			toggleNode.className = 'hide';
			linkNode.innerHTML = onText;
			if(additionalInvertedToggleID){
				document.getElementById(additionalInvertedToggleID).className = '';
			}
		}
		else{
			toggleNode.className = '';
			linkNode.innerHTML = offText;
			if(additionalInvertedToggleID){
				document.getElementById(additionalInvertedToggleID).className = 'hide';
			}
		}
	}
	
	// cookie read, set, remove
	function readCookie(name) {
		var nameEQ = name + "=";
		var cookieArray = document.cookie.split(';');
		for(var i=0;i<cookieArray.length;i++){
			var c = cookieArray[i];
			while (c.charAt(0)==' ') {
				c = c.substring(1,c.length);
			}
			if (c.indexOf(nameEQ) == 0){
				var value = c.substring(nameEQ.length,c.length);
				return value;
			}
		}
		return null;
	}
	function removeCookie(name){
		if(readCookie(name)){
			document.cookie = name + "=" + "; expires=Thu, 01-Jan-70 00:00:01 GMT";
			return true;
		}
		return false;
	}
	function setCookie(name,value) {
		document.cookie = name + "=" + value + "; path=/; ";
	}
	
	// set form "searchForm" to a given value and trigger its onchange event
	function searchFormSelect(value){
		var formName = 'searchForm';
		if(typeof(value) == "number"){
			document[formName].source.selectedIndex = value;
		}
	    else{
	        for(var i = 0; i < document[formName].source.options.length; i++){
	                if(document[formName].source.options[i].value == value || document[formName].source.options[i].text.indexOf(value) > -1){
	                        document[formName].source.selectedIndex = i;
	                }
	        }
		}
		document[formName].source.onchange();
	}
	
	// create a dom object from a string of markup
	// borrowed heavily from dojo.dom.createDocumentFromText
	function string2dom(string, mimetype){
		if(!mimetype) { mimetype = "text/xml"; }
		if(isDefined('window','DOMParser')) {
			var parser = new DOMParser();
			return parser.parseFromString(string, mimetype);
		}
		else if(isDefined('window','ActiveXObject')){
			var domDoc = new ActiveXObject("MSXML.DOMDocument");
			//var domDoc = new ActiveXObject("Microsoft.XMLDOM");
			if(domDoc) {
				domDoc.async = false;
				domDoc.loadXML(string);
				if(domDoc.parseError != 0){
					alert("Document parse error\nCode:" + domDoc.parseError.errorCode + "\nLine:" + domDoc.parseError.line + "\nReason:" + domDoc.parseError.reason );
				}
				return domDoc;
			}else{
				alert("Error: can't create ActiveXObject('MSXML.DOMDocument') object");
			}
		}
		return null;
	}
// end of useful functions



/**************************
// additional incremental search functions
*/
var haltIncremental = false;
function getIncrementalResults() { // kick off IOClient requests
	var id = getMetaContent(document,'lw_searchParameters','id');
	var source = getMetaContent(document,'lw_searchParameters','source');
	var date = new Date();
	var url = GLOBALS.basePath + '/content/search.html?id='+id+'&source='+source+'&secs='+date.getSeconds();

	var incremental = new IOClient();
	incremental.init('incremental',url);
	incremental.get();
}

// toggle display of zero results and associated h3 headings
function toggleIncrementalZeros(toggle){
	var headings = document.getElementById('incrementalSearchResults').getElementsByTagName('h3');
	var results = document.getElementById('incrementalSearchResults').getElementsByTagName('li');
	var zerotoggle = document.getElementById("zerotoggle");
  
	if(toggle == "true"){
		zerotoggle.href = "javascript:toggleIncrementalZeros('false');";
		zerotoggle.innerHTML = "Hide Details";
		for (var i = 0; i < headings.length; i++) {
			if(headings[i].className == 'hide'){
				headings[i].className = 'unhide';
			}
		}
		for (var i = 0; i < results.length; i++) {
			if(results[i].className == 'hide'){
				results[i].className = 'unhide';
			}
		}
	}
	else if(toggle == "false"){
		zerotoggle.href = "javascript:toggleIncrementalZeros('true');";
		zerotoggle.innerHTML = "Show Details";
		for (var i = 0; i < headings.length; i++) {
			if(headings[i].className == 'unhide'){
				headings[i].className = 'hide';
			}
		}
		for (var i = 0; i < results.length; i++) {
			if(results[i].className == 'unhide'){
				results[i].className = 'hide';
			}
		}
	}
	var webtrendsCall = ''; // make webtrends call only if dcsMultiTrack() is defined
	if(isDefined(window,'dcsMultiTrack')){
		dcsMultiTrack("WT.ti","Metasearch Show/Hide Details clicked");
	}
}
// end additional incremental functions


/**************************
// route form submissions according to 'source' value
*/
var searching = false;
function submitSearch() {
  var formName = 'searchForm';
  var source = document[formName].source.options[document[formName].source.selectedIndex].value;
  var keywords = cleanKW(document[formName].keywords.value);
  var nokeywords = 'Please enter one or more search terms.';

  if(eLibraryTabIDs.contains(source)){
  	setCookie('LWeLibSource',source); // set source cookie so we know which format tab to return to
  }
  
  if (keywords == '') {
	alert(nokeywords);
	return false;
  }
  else if (source.match(/(research|clinical|peds)/)) {
	var dest = GLOBALS.searchPath + '?source=' + source + '&keywords=' + keywords + '&w=' + GLOBALS.incrementalSearchWait;
    window.location = dest;
	return false;
  }
  else if (source == 'biomedsem') {
	var dest = 'http://med.stanford.edu/seminars/search?searchString=' + keywords + '&Submit=Go';
    window.location = dest;
	return false;
  }
  else if (source == 'catalog') {
	var dest = 'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&SAB1=' + keywords + '&BOOL1=all+of+these&FLD1=Keyword+Anywhere++%5BLKEY%5D+%28LKEY%29&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&CNT=50';
    window.location = dest;
	return false;
  }
  else if (source == 'google') {
	var dest = 'http://www.google.com/search?hl=en&q=' + keywords;
	if(GLOBALS.needsProxy == 'true'){
		dest = GLOBALS.proxyPrefix + dest;
	}
    window.location = dest;
	return false;
  }
  else if (source == 'pubmed') {
	var dest = 'http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term=' + keywords;
	if(GLOBALS.needsProxy == 'true'){
		dest = GLOBALS.proxyPrefix + dest;
	}
    window.location = dest;
	return false;
  }
  else if (source == 'stanford_who') {
	var dest = 'https://stanfordwho.stanford.edu/lookup?search=' + keywords;
    window.location = dest;
	return false;
  }

  if (searching) {
    alert('a search is already in progress');
    return false;
  }
  searching = true;
  return true;
}

//IE ignores disabled attribute of option tag
// store last index and if current value is a disabled option, return to previous option selected
var lastIndex = 0;
function lastSelectValue(select){
	var val = select.options[select.selectedIndex].value;

	if ( (val == "----------------") || (val == "")) {
		if (lastIndex) {
			select.selectedIndex = lastIndex;
		}
		else{
			select.selectedIndex = 0;
		}
	}
	else{
		if (arguments.length > 1 && arguments[1] == "portals") { /* portals drop-down */
			window.location= GLOBALS.basePath + select.options[select.selectedIndex].value;
		}
	}
}