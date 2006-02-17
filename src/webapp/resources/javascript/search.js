//**************************
// create GLOBAL object and attributes
//
var GLOBALS = new Object();
GLOBALS.basePath = '/beta';
GLOBALS.baseImagePath = '/beta/images/templates/default';
GLOBALS.lanewebTemplate = 'default';
GLOBALS.proxyPrefix = 'http://laneproxy.stanford.edu/login?url=';
GLOBALS.needsProxy = getMetaContent(document,'lw_proxyLinks');


//**************************
// slice results into format tabs
//
var eLibraryTabLabels 	= new Array('All','eJournals','Databases','eBooks','Calculators','Lane Services');
var eLibraryTabDivs = new Array('all','ej','database','book','cc','faq');
var eLibraryResultCounts	= new Array();

function geteLibraryTabCount(limitType){
	var staticResults = document.getElementById('eLibraryStaticSearchResults');
	var staticResultDivs = staticResults.getElementsByTagName('div');
	var staticResultLinks = new Array();
	var count = 0;

	for (var i = 0; i < staticResultDivs.length; i++){
		if(staticResultDivs[i].getAttribute('id') == limitType){
			staticResultLinks = staticResultDivs[i].getElementsByTagName('a');
		}
	}

	for (var y = 0; y < staticResultLinks.length; y++){
			count++
	}

	if(document.getElementById('keywordresult')){
		var keyword = document.getElementById('keywordresult').innerHTML;
		var keywordUri = cleanKW(keyword);
	}
	
	//if count is zero, we want to create popIn content
	if(!staticResultLinks.length && !document.getElementById('popIn-' + limitType)){
		var body = document.getElementsByTagName("body").item(0);
		var bC = document.createElement('div');
		bC.className = 'hide';
		bC.innerHTML = '<img border="0" src="' + GLOBALS.baseImagePath + '/smallLaneX.gif" alt="X" /> Also try <a href="' + GLOBALS.basePath + '/search.html?source=clinical&template=' + GLOBALS.lanewebTemplate + '&keywords=' + keywordUri +'">Clinical Search</a>';
		bC.setAttribute('id','popIn-' + limitType);
		body.appendChild(bC);
	}
	return count;
}

function painteLibraryTabs(){
	//var all = 0;
	for(var i = 1; i < eLibraryTabDivs.length; i++){
		eLibraryResultCounts[i] = geteLibraryTabCount(eLibraryTabDivs[i]);
		//all = all + eLibraryResultCounts[i];
	}
	//eLibraryResultCounts[0] = all;
	eLibraryResultCounts[0] = geteLibraryTabCount('all');

	var bar = '';
	for(var i = 0; i < eLibraryTabLabels.length; i++){
		bar = bar + '<li><span class="' + eLibraryTabDivs[i] + '" name="' + eLibraryTabDivs[i] + '" onclick="javascript:showeLibraryTab(\'' + eLibraryTabDivs[i] + '\');">' + eLibraryTabLabels[i] + ': <p class="hitCount">' + eLibraryResultCounts[i] + '</p></span></li>';
	}

	if(document.getElementById('metaSearchResultContainer')){
		document.getElementById('eLibraryTabs').innerHTML = '<ul>' + bar + '</ul><ul id="metaSearchResultContainer">' + document.getElementById('metaSearchResultContainer').innerHTML + '</ul><ul class="metaSearchResultsRight">&nbsp; </ul>';
	}
	else{
		//document.getElementById('eLibraryTabs').innerHTML = '<ul>' + bar + '</ul><ul id="faqSearchResults" class="metaSearchResults"></ul><ul id="loisSearchResults" class="metaSearchResults"></ul><ul id="pubmedSearchResults" class="metaSearchResults"></ul><ul id="googleSearchResults" class="metaSearchResults"></ul><ul class="metaSearchResultsRight">&nbsp; </ul>';
		document.getElementById('eLibraryTabs').innerHTML = '<ul>' + bar + '</ul><ul id="metaSearchResultContainer"><ul id="faqSearchResults" class="metaSearchResults"></ul><ul id="loisSearchResults" class="metaSearchResults"></ul><ul id="pubmedSearchResults" class="metaSearchResults"></ul><ul id="googleSearchResults" class="metaSearchResults"></ul></ul><ul class="metaSearchResultsRight">&nbsp; </ul>';
	}
}

function showeLibraryTab(limitType){

	// if tab parameter sent in URL, default to specified tab
	// otherwise, default to 'all'
	if (limitType == 'default'){
		for (var i = 0; i < eLibraryTabDivs.length; i++){
			if (getQueryContent('tab') == eLibraryTabDivs[i]){
				limitType = eLibraryTabDivs[i];
			}
		}
		if (limitType == 'default') {
			limitType = 'all';
		}
	}

	// swap active tab link out
	var tabs = document.getElementById('eLibraryTabs');
	tabLinks = tabs.getElementsByTagName('span');
	var activeLabel = '';

	for (var i = 0; i < tabLinks.length; i++){

		// activate link if it has a className of limitType
		if( tabLinks[i].className == 'active' && tabLinks[i].getAttribute('name') == limitType) {
		}
		else if(tabLinks[i].className == limitType){
			tabLinks[i].className = 'active';
			activeLabel = eLibraryTabLabels[i];
		}
		else{
			tabLinks[i].className = eLibraryTabDivs[i];
		}
	}

	var staticResults = document.getElementById('eLibraryStaticSearchResults');
	staticResults.className = 'hide';

	var staticResultDivs = staticResults.getElementsByTagName('div');

	// newResults div is empty placeholder for dynamically sliced results
	var newResults = document.getElementById('eLibraryNewSearchResults');
	newResults.innerHTML = '';

	if(limitType == 'all'){
			staticResults.className = '';
			newResults.className = 'hide';
	}
	else{
		newResults.className = '';
		for (var i = 0; i < staticResultDivs.length; i++){
			if(staticResultDivs[i].getAttribute('id') == limitType){
				newResults.innerHTML = staticResultDivs[i].innerHTML;
			}
		}
	}

	//get popIn content, if any
	var popInContent;
	if(document.getElementById('popIn-' + limitType)){
		popInContent = document.getElementById('popIn-' + limitType);
		newResults.innerHTML = '<div id="popIn" class="popInContent">' + popInContent.innerHTML + '</div>' + newResults.innerHTML;
	}

}
// end slice results into format tabs


//**************************
// XMLHttpRequest handler class
// includes results processing and display
//
function XMLClient() {};

XMLClient.prototype = {
	template: null,
	request: null,
	type: null,
	url: null,
//	needsProxy: null,

	init: function (type, url, template) {
		this.template = template;
		this.type = type;
		this.url = url.replace(/&amp;/g,'&'); // JTidy replaces & w/ &amp;
		//this.needsProxy = getMetaContent(document,'lw_proxyLinks');

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
			//
			//
			case "erdb":
				var erdbLabels = new Object();
				erdbLabels['article'] = 'Articles';
				erdbLabels['book review'] = 'Book Reviews';
				erdbLabels['person, male'] = 'Men';
				erdbLabels['organization'] = 'Organizations';
				erdbLabels['person'] = 'People';
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

					var eLibraryStaticSearchResults = document.getElementById("eLibraryStaticSearchResults");
					var erdbContent = document.createElement('div');
					erdbContent.innerHTML = '<h3>' + erdbLabels[type] + '</h3><dl>' + resultsHTML + '</dl>';
					erdbContent.setAttribute('id','erdb-' + type);
					eLibraryStaticSearchResults.appendChild(erdbContent);

					eLibraryTabLabels[eLibraryTabLabels.length] = erdbLabels[type];
					eLibraryTabDivs[eLibraryTabDivs.length] = 'erdb-' + type;
					painteLibraryTabs();
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
					var html = '<li>' +
									'<span>' + 
										'<a target="new" href="' + baseUrl + '">Services/FAQs:&nbsp;<p class="metaHitCount">' + lis.length + '</p></a>' +
									'</span>' +
								'</li>';
					//ie having trouble finding this element ... TODO
					if(document.getElementById('faqSearchResults')){
						document.getElementById('faqSearchResults').innerHTML = html;
					}
				}
			break;


			//**************************
			// Incremental metasearch results
			//
			case "incremental":
				var response = this.request.responseXML.documentElement;
				var newResults = response.getElementsByTagName('div')[3].getElementsByTagName('li');
				var oldResults = document.getElementById('incrementalSearchResults').getElementsByTagName('li');
				document.getElementById('incrementalSearchResults').className = 'unhide'; //display results
								
				var searchStatus = getMetaContent(response,'lw_searchParameters','status');
				var searchTerm = getMetaContent(response,'lw_searchParameters','query');
				var resultsProgressBar = document.getElementById('incrementalResultsProgressBar');
				var resultsDetails = document.getElementById('incrementalResultsDetails');

				var foundCount = 0;
				var doneCount = 0;
				var finished = true;
				
				for (var i = 0; i < newResults.length; i++) {
				  var oldAnchor = oldResults[i].getElementsByTagName('a')[0];
				  var newAnchor = newResults[i].getElementsByTagName('a')[0];
				  var newStatus = newAnchor.getAttribute('class');
				
				  if (newStatus == 'running') {
				    finished = false;
				  }
				
				  if ( newStatus != 'running' ){
				  	doneCount++;
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
				    foundCount++;
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
					if(newResults.length > 0 && doneCount > 0){
				  		var width = 100 * (doneCount / newResults.length);
					}
					else{
						var width = 1;
					}
					resultsProgressBar.innerHTML = '<table><tr><td nowrap>Still searching...</td><td nowrap><div style="position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;"><img width="' + width + '%" height="15" src="' + GLOBALS.baseImagePath + '/incrementalResultsProgressBar.gif" alt="progress bar" /></div></td><td nowrap>&nbsp;' + doneCount + ' of ' + newResults.length + ' sources searched. <a href="javascript:haltIncremental=true;void(0);">Stop Search</a></td></tr></table>';
				}
				else{
					resultsProgressBar.innerHTML = '';
					if(newResults.length > foundCount){
				  		//resultsDetails.innerHTML = 'Results <strong>' + foundCount + '</strong> of <strong><a href="javascript:displayIncrementalZeros(\'true\');">' + newResults.length + '</a></strong> sources contain <strong>' + searchTerm + '</strong> [<a id="zerotoggle" href="javascript:displayIncrementalZeros(\'true\');">Show Details</a>]';
				  		resultsProgressBar.innerHTML = 'Results in <strong>' + foundCount + '</strong> of <strong>' + newResults.length + '</strong> sources for <strong>' + searchTerm + '</strong> [<a id="zerotoggle" href="javascript:displayIncrementalZeros(\'true\');">Show Details</a>]';
				  	}
				  	else if(newResults.length == foundCount){
						resultsProgressBar.innerHTML = 'Results in <strong>' + foundCount + '</strong> of <strong>' + newResults.length + '</strong> sources contain <strong>' + searchTerm + '</strong>';
				  	}
				}
				
				if (!finished) {
					setTimeout("getIncrementalResults();",1500);
					return 0;
				}

			break;


			//**************************
			// metasearch (lane web) results processing
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
					var metaSearchResults = new Array();

					for(var i=0; i<engines.length; i++){
						var resource = engines[i].getElementsByTagName('resource');
						var results = new Array();

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
							document.getElementById(metaSearchResults[j].id + 'SearchResults').innerHTML = 
									'<li>' +
										'<span>' +
											'<a target="new" href="' + metaSearchResults[j].url + '">' + metaSearchResults[j].name + ':&nbsp;<p class="metaHitCount">' + metaSearchResults[j].hits + '</p></a>' +
										'</span>' +
									'</li>';
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
			// sfx results processing
			//
			case "sfx":
				var response = this.request.responseXML.documentElement;
				var openurl = response.getElementsByTagName('openurl')[0].firstChild.data;
				var result = response.getElementsByTagName('result')[0].firstChild.data;

				if(result != 0 ){
					var html = '<img border="0" src="' + GLOBALS.baseImagePath + '/smallLaneX.gif" /> Try FindIt@Stanford: <a target="new" href="' + openurl + '">' + result + '</a>';

					// results can be placed in three different places:
					// 1) popIn-ej div (for when client not on "ej" tab when SFX results returned
					var body = document.getElementsByTagName("body").item(0);
					var bC = document.createElement('div');
					bC.className = 'hide';
					bC.innerHTML = html;
					bC.setAttribute('id','popIn-ej');
					body.appendChild(bC);

					// 2) SFXResults div ("all" tab)
					if(document.getElementById('SFXResults')){
						document.getElementById('SFXResults').className = 'popInContent';
						document.getElementById('SFXResults').innerHTML = html;
					}

					// 3) SFXResultsEjTab div (tab "ej" specified in URL request)
					if(getQueryContent('tab') == 'ej'){
						var newResults = document.getElementById("eLibraryNewSearchResults");
						if(newResults){
							newResults.innerHTML = '<div id="SFXResultsEjTab" class="popInContent">' + html + '</div>' + newResults.innerHTML;
						}
					}
				}
			break;

			//**************************
			// spelling suggestion (google) processing
			//
			case "spell":
				var response = this.request.responseXML.documentElement;

				if(response.firstChild){
					var spellSuggestion = response.firstChild.data;

					// probably only used on er.html page ... can we remove this part?
					var baseUrl = null;
					switch(this.template){ 
						case "eResources":
							baseUrl = './er.html?source=eResources&amp;template=' + GLOBALS.lanewebTemplate + '&amp;keywords=';
						break;
						
						case "eJournals":
							baseUrl = './searchej.html?source=eJournals&amp;template=' + GLOBALS.lanewebTemplate + '&amp;keywords=';
						break;
						
						default:
							baseUrl = './er.html?source=eResources&amp;template=' + GLOBALS.lanewebTemplate + '&amp;keywords=';
						break;				
					}

					var html = 'Did you mean: <a href="' + baseUrl + spellSuggestion + '"><i><strong>' + spellSuggestion + '</strong></i></a>';

					if(document.getElementById('SpellResults')){
						document.getElementById('SpellResults').className = 'popInContent';
						document.getElementById('SpellResults').innerHTML = html;
					}

					// also check to see if we should pop this into a tab specified in query string
					if(getQueryContent('tab')){
						var newResults = document.getElementById("eLibraryNewSearchResults");
						document.getElementById('popIn').className = 'hide';
						if(newResults){
							newResults.innerHTML = '<div id="SpellResults" class="popInContent">' + html + '</div>' + newResults.innerHTML;
						}
					}
				}
			break;

			//**************************
			// SUL database results processing
			//
			case "sul":
				var response = this.request.responseXML.documentElement;
				var names = response.getElementsByTagName('name');
				var links = response.getElementsByTagName('link');

				if(names.length){
					var html = '';

					for(var i=0; i<names.length; i++){
							var link = response.getElementsByTagName('link')[i].firstChild.data;
							var name = response.getElementsByTagName('name')[i].firstChild.data;

							if(GLOBALS.needsProxy != 'false'){
								link = GLOBALS.proxyPrefix + link;
							}

							html += '<a class="indent" target="new" href="' + link + '">' + name + '</a><br />';
					}

					html = '<img border="0" src="' + GLOBALS.baseImagePath + '/smallLaneX.gif" /> Try other Stanford databases:<br/>' + html;

					// results can be placed in three different places:
					// 1) popIn-database div (for when client not on "database" tab when SUL results returned
					var body = document.getElementsByTagName("body").item(0);
					var bC = document.createElement('div');
					bC.className = 'hide';
					bC.innerHTML = html;
					bC.setAttribute('id','popIn-database');
					body.appendChild(bC);
					
					// 2) SULResultsDbTab div (tab "database" specified in query string)
					if(getQueryContent('tab') == 'database'){
						var newResults = document.getElementById("eLibraryNewSearchResults");
						if(newResults){
							newResults.innerHTML = '<div id="SULResultsDbTab" class="popInContent">' + html + '</div>' + newResults.innerHTML;
						}
					}
				}

			break;
		}
	}

}


//**************************
// useful functions
//

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
		var pairs = new Array();
		pairs = metaTags[i].getAttribute('content').split(paramDelim);
		for (var y = 0; y < pairs.length; y++){
			var pair = new Array();
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
// end useful functions



//**************************
// additional vars and methods used w/ incremental search
//
var haltIncremental = false;
var date = new Date();
var startTime = date.getTime();

function getIncrementalResults() {
	var id = getMetaContent(document,'lw_searchParameters','id');
	var template = getMetaContent(document,'lw_searchParameters','template');
	var date = new Date();
	var url = GLOBALS.basePath + '/content/search.html?id='+id+'&source='+template+'&secs='+date.getSeconds();

	var incremental = new XMLClient();
	incremental.init('incremental',url);
	incremental.get();
}

//toggle display of zero results and associated h3 headings
function displayIncrementalZeros(toggle){
  var headings = document.getElementById('incrementalSearchResults').getElementsByTagName('h3');
  var results = document.getElementById('incrementalSearchResults').getElementsByTagName('li');
  var zerotoggle = document.getElementById("zerotoggle");
  
  if(toggle == "true"){
    zerotoggle.href = "javascript:displayIncrementalZeros('false');";
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
    zerotoggle.href = "javascript:displayIncrementalZeros('true');";
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
// end additional incremental vars and methods


//**************************
// move to laneweb.js when ready
function submitSearchTemp() {
  var source = document.searchForm.source.options[document.searchForm.source.selectedIndex].value;
  var keywords = document.searchForm.keywords.value;
  var nokeywords = 'Please enter one or more search terms.';

  if (keywords == '') {
    alert(nokeywords);
    return false;
  }
  else if (source == 'ej') {
	var dest = GLOBALS.basePath + '/online/er.html?tab=ej&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'database') {
	var dest = GLOBALS.basePath + '/online/er.html?tab=database&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'book') {
	var dest = GLOBALS.basePath + '/online/er.html?tab=book&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'cc') {
	var dest = GLOBALS.basePath + '/online/er.html?tab=cc&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'pubmed') {
    openLink('http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term=' + keywords);
    return false;
  }
  else if (source == 'google') {
    openLink('http://www.google.com/search?hl=en&q=' + keywords);
    return false;
  }
  else if (source == 'faq') {
	var dest = GLOBALS.basePath + '/howto/index.html?keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'eResources') {
	var dest = GLOBALS.basePath + '/online/er.html?keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'catalog') {
	var dest = 'http://traindb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&Search_Arg=' + keywords + '&SL=None&Search_Code=FT*&CNT=50';
    window.location.replace(dest);
	return false;
  }
  else if (source == 'stanford_who') {
    openLink('https://stanfordwho.stanford.edu/lookup?search=' + keywords);
    return false;
  }
  else if (source == 'biomedsem') {
    openLink('http://med.stanford.edu/seminars/searchresults.jsp?searchString=' + keywords + '&Submit=Go');
    return false;
  }
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
// end adds to laneweb.js 
