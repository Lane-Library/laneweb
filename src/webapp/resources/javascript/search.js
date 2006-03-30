//**************************
// create GLOBAL object and attributes
//
var GLOBALS = new Object();
GLOBALS.basePath = '/./.';
GLOBALS.baseImagePath = GLOBALS.basePath + '/images/templates/default';
GLOBALS.incrementalSearchWait = '2500'; //time in ms between start of metasearch and initial response
GLOBALS.needsProxy = getMetaContent(document,'lw_proxyLinks');
GLOBALS.proxyPrefix = 'http://laneproxy.stanford.edu/login?url=';
GLOBALS.searchPath = GLOBALS.basePath + '/search.html';

var date = new Date();
var haltIncremental = false;
var searching = false;
var startTime = date.getTime();

// handle error events with errorLogger method 
window.onerror = errorLogger;
function errorLogger(message, url, line){
	var errorImg = document.createElement('img');
	errorImg.src = GLOBALS.basePath + '/javascript/ErrorLogger.js?url=' + url + '&line=' + line + '&msg=' + message;
	errorImg.className = 'hide';
	return false;
}

//**************************
// slice results into format tabs
//
var eLibraryTabLabels = new Array('All','eJournals','Databases','eBooks','Calculators','Lane Services');
var eLibraryTabIDs = new Array('all','ej','database','book','cc','faq');
var eLibraryResultCounts = [];
var eLibraryActiveTab = null;

function geteLibraryTabCount(tabID){
	var tabResultLinks = document.getElementById(tabID).getElementsByTagName('dt');
	return tabResultLinks.length;
}

function initeLibraryTabs(){
	for(var i = 0; i < eLibraryTabIDs.length; i++){
		eLibraryResultCounts[eLibraryTabIDs[i]] = geteLibraryTabCount(eLibraryTabIDs[i]);
	}

	var bar = '';
	for(var i = 0; i < eLibraryTabLabels.length; i++){
		var elementContainerForDisplayText = '';
		if(document.getElementById(eLibraryTabIDs[i] + "SearchTagline")){
			elementContainerForDisplayText = document.getElementById(eLibraryTabIDs[i] + "SearchTagline").innerHTML;		
		}
		bar = bar + '<div id="' + eLibraryTabIDs[i] + 'Tab" class="eLibraryTab" title="' + elementContainerForDisplayText + '" name="' + eLibraryTabIDs[i] + '" onclick="javascript:showeLibraryTab(\'' + eLibraryTabIDs[i] + '\');">' + eLibraryTabLabels[i] + '<br /><span class="tabHitCount">' + intToNumberString(eLibraryResultCounts[eLibraryTabIDs[i]]) + '</span></div>';
	}
	document.getElementById('eLibraryTabs').innerHTML = bar;
}

function showeLibraryTab(tab){
	// if active tab not specified, cookie trumps, then source parameter in URL, then default to all
	if (!tab){
		tab = 'all'; // default to all
		if( readCookie('LWeLibSource') && eLibraryTabIDs.contains(tab) ){
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
	if(tab == 'all'){
		document.searchForm.source.selectedIndex = 0; //default searchForm to eLibrary
	}
	else{
		for(var i = 0; i < document.searchForm.source.options.length; i++){
			if(document.searchForm.source.options[i].value == tab){
				document.searchForm.source.selectedIndex = i;
			}
		}
	}
	document.searchForm.source.onchange();

	eLibraryActiveTab = tab;
	refreshPopInBar();
	setCookie('LWeLibSource',tab);
}

var relevanceSortedResults;
function sorteLibraryResults(){
	var searchResults = document.getElementById('eLibrarySearchResults');
	
	if(searchResults.getAttribute('name') == 'relevance-sort'){
		var nextSort = 'alpha-sort';
		searchResults.innerHTML = relevanceSortedResults;
		showeLibraryTab(eLibraryActiveTab);
	}
	else {
		var nextSort = 'relevance-sort';
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
			div.innerHTML = '<dl class="' + dl[0].className + '">' + resultsHTML + '</dl>';
		}
	}

	searchResults.setAttribute('name',nextSort);
	setCookie('LWeLibNextSort',nextSort);
	refreshPopInBar();
}

function refreshPopInBar(){
	var popInContent = '';

	if(document.getElementById('popInContent')){ 
		document.getElementById('popInContent').className = 'hide';
	}

	if(document.getElementById(eLibraryActiveTab + "TabBasicText")){
		popInContent = document.getElementById(eLibraryActiveTab + "TabBasicText").innerHTML;
	}

	// if zero results for active tab, display zeroResultsText
	if(eLibraryResultCounts[eLibraryActiveTab] == 0 && document.getElementById(eLibraryActiveTab + "TabZeroResultsText")){
		popInContent = document.getElementById(eLibraryActiveTab + "TabZeroResultsText").innerHTML;
	}

	if(document.getElementById('sfxResults') && (eLibraryActiveTab == 'all' || eLibraryActiveTab == 'ej') ){ 
		popInContent += document.getElementById('sfxResults').innerHTML;
	}
	if(document.getElementById('spellResults')){ 
		popInContent = document.getElementById('spellResults').innerHTML;
	}
	if(document.getElementById('suldbResults') && eLibraryActiveTab == 'database'){ 
		popInContent += document.getElementById('suldbResults').innerHTML;
	}

	// if results, add sort-by drop-down
	if(eLibraryResultCounts[eLibraryActiveTab] != 0){
		var options = new Array('Relevance','A-Z');

		if(document.getElementById('eLibrarySearchResults').getAttribute('name') == 'relevance-sort'){
			var optionsHtml = '<option>' + options[0] + '</option><option selected="true">' + options[1] + '</option>';
		}
		else {
			var optionsHtml = '<option selected="true">' + options[0] + '</option><option>' + options[1] + '</option>';
		}
		popInContent += 'Sorted by <select name="sortBy" onchange="sorteLibraryResults();" style="font-size: 95%; font-weight: 400;">' + optionsHtml + '</select>';
	}

	if(popInContent != ''){
		popInContent = expandSpecialSyntax(popInContent);
		document.getElementById('popInContent').innerHTML = popInContent;
		document.getElementById('popInContent').className = 'popInContent';
	}	

	// show tabTip if any *and* more than zero results
	if(document.getElementById(eLibraryActiveTab + "TabTipText") && eLibraryResultCounts[eLibraryActiveTab] != 0){
		var thisTabText = document.getElementById(eLibraryActiveTab + "TabTipText").innerHTML;
		thisTabText = expandSpecialSyntax(thisTabText);
		document.getElementById("tabTip").innerHTML = thisTabText;
		document.getElementById("tabTip").className = 'tabTip';
	}
	else{
		document.getElementById("tabTip").className = 'hide';
	}
}
// end slice results into format tabs



//**************************
// XMLHttpRequest handler class
// includes results processing and display
//
function XMLClient() {};

XMLClient.prototype = {
	source: null,
	request: null,
	type: null,
	url: null,

	init: function (type, url, source) {
		this.source = source;
		this.type = type;
		this.url = url.replace(/amp;/g,''); // JTidy replaces & w/ &amp; ... &amp; becomes &amp;amp;

		//sniff for IE/Mac
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
				this.processXML();
			}
		}
	},

	processXML: function() {
		switch(this.type){

			//**************************
			// dynamic generation of genre-based tabs
			// TESTING only
			case "erdb":
				var erdbLabels = [];
				erdbLabels['article'] = 'Articles';
				erdbLabels['book review'] = 'Book Reviews';
				erdbLabels['person, male'] = 'Men';
				erdbLabels['organization'] = 'Organizations';
				erdbLabels['person'] = 'People';
				erdbLabels['photograph'] = 'Photographs';
				erdbLabels['person, female'] = 'Women';
				erdbLabels['statistics'] = 'Statistics';

				var response = this.request.responseXML.documentElement;
				var dts = response.getElementsByTagName('dt');
				var dds = response.getElementsByTagName('dd');

				if(response && dds){
					var resultsHTML = '';

					for(var i = 0; i < dds.length; i++){
						var linksHTML = '';
						var links = dds[i].getElementsByTagName('a');
						for(var j = 0; j < links.length; j++){
							var linkText = ''; //link text not always present, so need to check before building into link
							if(links[j].childNodes[0]){
								linkText = links[j].childNodes[0].nodeValue;
							}
							linksHTML += '<li><a href="' + links[j].getAttribute('href') + '" class="' + links[j].getAttribute('class') + '" title="' + links[j].getAttribute('title') + '">' + linkText + '</a></li>';
						}
						resultsHTML += '<dt>' + dts[i].childNodes[0].nodeValue + '</dt>' +
								'<dd>' +
										'<ul>' +
											linksHTML + 
										'</ul>' +
								'</dd>';
					}

					var type = getQueryContent('t',this.url);

					var eLibrarySearchResults = document.getElementById("eLibrarySearchResults");
					var erdbContent = document.createElement('div');
					erdbContent.innerHTML = '<dl>' + resultsHTML + '</dl>';
					erdbContent.setAttribute('id','erdb-' + type);
					eLibrarySearchResults.appendChild(erdbContent);

					eLibraryTabLabels[eLibraryTabLabels.length] = erdbLabels[type];
					eLibraryTabIDs[eLibraryTabIDs.length] = 'erdb-' + type;
					initeLibraryTabs();
					showeLibraryTab('erdb-' + type);
				}
			break;

			//**************************
			// Lane FAQs
			//
			case "faq":
				var response = this.request.responseXML.documentElement;

				if(response.getElementsByTagName('li')){
					var lis = response.getElementsByTagName('li');
					var baseUrl = GLOBALS.basePath + '/howto/index.html?keywords=' + keywords;
					var html = '<a target="new" href="' + baseUrl + '">Lane Services<br /><span class="tabHitCount">' + lis.length + '</span></a>';

					if(document.getElementById('faqSearchResults')){
						document.getElementById('faqSearchResults').innerHTML = html;
					}
				}
			break;

			//**************************
			// Incremental metasearch results (clinical, peds, research searches)
			// 
			case "incremental":
				var response = this.request.responseXML.documentElement;
				var newResults = response.getElementsByTagName('div')[2].getElementsByTagName('li');
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
				  var newStatus = newAnchor.getAttribute('class');
				
				  if (newStatus == 'running') {
				    finished = false;
				  }
				  else{
				  	sourcesCompleteCount++;
				  }
					
				  //hide result items if status is still running or the item returned a zero hit count
				  if ( newStatus == 'running' 
				  	|| newResults[i].getElementsByTagName('span')[0].childNodes[0].nodeValue == "timed out"
				  	|| newResults[i].getElementsByTagName('span')[0].childNodes[0].nodeValue == 0){
				    oldResults[i].className = 'hide';
				  }
				  else{
				    // display parent h3 heading as well as result
				    oldResults[i].parentNode.parentNode.getElementsByTagName('h3')[0].className = '';
				    oldResults[i].className = '';
				    hitsFoundInSourceCount++;
				  }
				
				  if (oldAnchor.className != newStatus) {
				    oldAnchor.className = newStatus;

					if(GLOBALS.needsProxy != 'false'){
						oldAnchor.setAttribute('href',GLOBALS.proxyPrefix + oldAnchor.getAttribute('href') );
					}

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
				
				if(!finished){
					if(newResults.length > 0 && sourcesCompleteCount > 0){
				  		var width = 100 * (sourcesCompleteCount / newResults.length);
					}
					else{
						var width = 1;
					}
					resultsProgressBar.innerHTML = '<table><tr><td nowrap>Still searching...</td><td nowrap><div style="position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;"><img width="' + width + '%" height="15" src="' + GLOBALS.baseImagePath + '/incrementalResultsProgressBar.gif" alt="progress bar" /></div></td><td nowrap>&nbsp;' + sourcesCompleteCount + ' of ' + newResults.length + ' sources searched. <a href="javascript:haltIncremental=true;void(0);">Stop Search</a></td></tr></table>';
					setTimeout("getIncrementalResults();",1500);
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

			//**************************
			// metasearch (lane web search app) results processing for eLibrary search
			//
			case "meta":
				var xmlObj = this.request.responseXML.documentElement;
				var sessionID = xmlObj.getAttribute('id');
				var status = xmlObj.getAttribute('status');
				var queryContents = xmlObj.getElementsByTagName('query')[0].firstChild.data;

				var date = new Date();
				var newMetaUrl = '/search/search?id=' + sessionID + "&secs=" + date.getSeconds();

				if(status == "successful" || status =="running"){
					var engines = xmlObj.getElementsByTagName('engine');
					var metaSearchResults = [];

					for(var i=0; i<engines.length; i++){
						var resource = engines[i].getElementsByTagName('resource');
						var results = [];

						results.id = resource[0].getAttribute('id');
						results.url = resource[0].getElementsByTagName('url')[0].firstChild.data;
						results.name = resource[0].getElementsByTagName('description')[0].firstChild.data;

						if(GLOBALS.needsProxy != 'false'){
							results.url = GLOBALS.proxyPrefix + results.url;
						}

						if ( resource[0].getElementsByTagName('hits').length > 0) {
							results.hits = resource[0].getElementsByTagName('hits')[0].firstChild.data;
							metaSearchResults.push(results);
						}
					}

					var html = '';

					for(var j=0; j<metaSearchResults.length; j++){
						if(document.getElementById(metaSearchResults[j].id + 'SearchResults')){
							document.getElementById(metaSearchResults[j].id + 'SearchResults').innerHTML = "<a target='new' href='" + metaSearchResults[j].url + "'>" + metaSearchResults[j].name + '<br /><span class="tabHitCount">' + intToNumberString(metaSearchResults[j].hits) + '</span></a>';
							if(metaSearchResults[j].id == 'google'){
								document.getElementById(metaSearchResults[j].id + 'SearchResults').className = 'metaSearchResultsRightCorner';
							}
							// TESTING ... remove entire block once lmldb has updated design
							else if(metaSearchResults[j].id == 'lois'){
								document.getElementById(metaSearchResults[j].id + 'SearchResults').innerHTML = "<a href='" + GLOBALS.basePath + '/online/catalog.html?keywords=' + keywords + "'>" + metaSearchResults[j].name + '<br /><span class="tabHitCount">' + intToNumberString(metaSearchResults[j].hits) + '</span></a>';
								document.getElementById(metaSearchResults[j].id + 'SearchResults').className = 'metaSearchResults';
							}
							else{
								document.getElementById(metaSearchResults[j].id + 'SearchResults').className = 'metaSearchResults';
							}
						}
					}
				}

				if (status == "running" ){
					Object.secondRequest = new XMLClient();
					Object.secondRequest.init('meta',newMetaUrl);

					setTimeout("Object.secondRequest.get();",1000);
					return 0;
				}
			break;

			//**************************
			// sfx results processing [eLibrary pop-in]
			//
			case "sfx":
				var response = this.request.responseXML.documentElement;
				var openurl = response.getElementsByTagName('openurl')[0].firstChild.data;
				var result = response.getElementsByTagName('result')[0].firstChild.data;

				if(result != 0 ){
					var html = 'FindIt@Stanford eJournal: <a target="new" href="' + openurl + '"><b>' + result.replace(/ \[.*\]/,'') + '</b></a><br />';

					//create new sfxResults div (rely on refreshPopInBar to display)
					var body = document.getElementsByTagName("body").item(0);
					var sfxDiv = document.createElement('div');
					sfxDiv.className = 'hide';
					sfxDiv.innerHTML = html;
					sfxDiv.setAttribute('id','sfxResults');
					body.appendChild(sfxDiv);

					refreshPopInBar();
				}
			break;

			//**************************
			// spelling suggestion (google) processing  [eLibrary pop-in]
			//
			case "spell":
				var response = this.request.responseXML.documentElement;

				if(response.firstChild){
					var spellSuggestion = response.firstChild.data;
					var html = 'Did you mean: <a href="' + GLOBALS.searchPath + '?keywords=' + spellSuggestion + '"><i><strong>' + spellSuggestion + '</strong></i></a><br />';

					//create new spellResults div (rely on refreshPopInBar to display)
					var body = document.getElementsByTagName("body").item(0);
					var spellDiv = document.createElement('div');
					spellDiv.className = 'hide';
					spellDiv.innerHTML = html;
					spellDiv.setAttribute('id','spellResults');
					body.appendChild(spellDiv);

					refreshPopInBar();

					// hide tip box ... or should this go into refreshPopInBar?
					//                  here only hides at popIn and if user continues to click, tip reappears (good?)
					if (document.getElementById('tabTip')){
						document.getElementById('tabTip').className = 'hide';
					}
				}
			break;

			//**************************
			// SUL database results processing [eLibrary pop-in]
			//
			case "suldb":
				var response = this.request.responseXML.documentElement;
				var names = response.getElementsByTagName('name');
				var links = response.getElementsByTagName('link');

				if(links.length){
					var html = '';

					for(var i=0; i<links.length; i++){
							var link = response.getElementsByTagName('link')[i].firstChild.data;
							var name = response.getElementsByTagName('name')[i].firstChild.data;

							if(GLOBALS.needsProxy != 'false'){
								link = GLOBALS.proxyPrefix + link;
							}
							// if i is 2, add div to hold hidden suldb links for expansion
							if(i == 2){
								html += '<div class="hide">';
							}
							html += '<a class="indent" target="new" href="' + link + '">' + name + '</a><br />';
					}

					// if more than 2 results, present expand-toggle
					if(links.length > 2){
						html = '<a href="javascript:void(0);" onclick="javascript:toggleNode(this, this.parentNode.getElementsByTagName(\'div\')[0],\'+\',\'-\',\'tabTip\');">+</a> Databases beyond biomedicine<br />' + html + '</div>';
					}
					else{
						html = 'Databases beyond biomedicine<br />' + html;
					}

					//create new suldbResults div (rely on refreshPopInBar to display)
					var body = document.getElementsByTagName("body").item(0);
					var suldbDiv = document.createElement('div');
					suldbDiv.className = 'hide';
					suldbDiv.innerHTML = html;
					suldbDiv.setAttribute('id','suldbResults');
					body.appendChild(suldbDiv);

					refreshPopInBar();
				}

			break;
		}
	}
}


//**************************
// useful functions
//

// extend Array class
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

// clean up keywords string: 
// 	replace &amp; w/ & (i.e. undo JTidy)
//  URIescape
function cleanKW(keywords){
	keywords = escape(keywords.replace(/&amp;/g,'&'));
	return keywords;
}

//getMetaContent Usage:
// <meta name="lw_parameters" content="foo=bar;bar=foo;x=y"/>
// <meta name="search_id" content="12"/>
// getMetaContent(document,'search_id')
// getMetaContent(document,'lw_parameters','foo', ';', '=')
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

function getQueryContent(paramName,queryString) {
	if(!queryString){
		queryString = location.search;
	}
	var paramName = paramName + "=";

	if ( queryString.length > 0 ) {
		startParam = queryString.indexOf ( paramName );
		if ( startParam != -1 ) {
			startParam += paramName.length;
			endParam = queryString.indexOf ( "&" , startParam );
			if ( endParam == -1 ) {
				endParam = queryString.length
			}
			return unescape ( queryString.substring ( startParam, endParam) );
		}
	}

	return 0;
}

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

// expand "::::keywords::::  ::::basePath::::" to "dvt /beta/stage", etc.
function expandSpecialSyntax(string){
	if(!string.match(/::::/)){
		return string;
	}

	for (i in GLOBALS){
		var pattern = '::::' + i + '::::';
		while (string.match(pattern)) {
			string = string.replace(pattern,GLOBALS[i]);
		}
	}
	if (string.match('::::keywordsDisplay::::') && keywords){
		string = unescape(string.replace(/::::keywordsDisplay::::/g,keywords));
	}
	if (string.match('::::keywordsUri::::') && keywords){
		string = string.replace(/::::keywordsUri::::/g,keywords);
	}
	return string;
}

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
// end useful functions



//**************************
// additional incremental search functions
//
function getIncrementalResults() {
	var id = getMetaContent(document,'lw_searchParameters','id');
	var source = getMetaContent(document,'lw_searchParameters','source');
	var date = new Date();
	var url = GLOBALS.basePath + '/content/search.html?id='+id+'&source='+source+'&secs='+date.getSeconds();

	var incremental = new XMLClient();
	incremental.init('incremental',url);
	incremental.get();
}

//toggle display of zero results and associated h3 headings
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
}
// end additional incremental functions


//**************************
// 
function submitSearch() {
  var source = document.searchForm.source.options[document.searchForm.source.selectedIndex].value;
  var keywords = document.searchForm.keywords.value;
  var nokeywords = 'Please enter one or more search terms.';

  if(eLibraryTabIDs.contains(source)){
  	setCookie('LWeLibSource',source);
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
	openLink('http://med.stanford.edu/seminars/searchresults.jsp?searchString=' + keywords + '&Submit=Go');
	return false;
  }
  else if (source == 'catalog') {
	var dest = GLOBALS.basePath + '/online/catalog.html?keywords=' + keywords;
    	window.location = dest;
	return false;
  }
  else if (source == 'google') {
	openLink('http://www.google.com/search?hl=en&q=' + keywords);
	return false;
  }
  else if (source == 'pubmed') {
	openLink('http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term=' + keywords);
	return false;
  }
  else if (source == 'stanford_who') {
	openLink('https://stanfordwho.stanford.edu/lookup?search=' + keywords);
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
		lastIndex = select.selectedIndex
	}
}

// catalog.html iframe
function loadCatalogIframe(){
        var q = getQueryContent('keywords',location.href);
        var frame = document.getElementById('catalog');
        frame.src = 'http://traindb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&SAB1=' + q + '&BOOL1=all+of+these&FLD1=Keyword+Anywhere++%5BLKEY%5D+%28LKEY%29&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&CNT=50';
        frame.className = '';
}
