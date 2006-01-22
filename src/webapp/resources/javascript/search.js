var GLOBALS = new Object();
GLOBALS.basePath = '/stage/beta';
GLOBALS.baseImagePath = '/stage/beta/images';
GLOBALS.lanewebTemplate = 'irt';
GLOBALS.proxyPrefix = 'http://laneproxy.stanford.edu/login?url=';

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



function XMLClient() {};

XMLClient.prototype = {
	template: null,
	request: null,
	type: null,
	url: null,
	needsProxy: null,

	init: function (type, url, template) {
		this.template = template;
		this.type = type;
		this.url = url.replace(/&amp;/g,'&'); // JTidy replaces & w/ &amp;
		this.needsProxy = getMetaContent(document,'LW.proxy');

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
								
				var searchStatus = getMetaContent(response,'search_status');
				var searchTerm = getMetaContent(response, 'search_query');
				var resultsProgressBar = document.getElementById('resultsProgressBar');
				var resultsDetails = document.getElementById('resultsDetails');

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

					if(this.needsProxy){
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
					resultsProgressBar.innerHTML = '<table><tr><td nowrap>Still searching...</td><td nowrap><div style="position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;"><img width="' + width + '%" height="15" src="' + GLOBALS.baseImagePath + '/progress_bar_grad.gif" alt="progress bar" /></div></td><td nowrap>&nbsp;' + doneCount + ' of ' + newResults.length + ' sources searched. <a href="javascript:haltIncremental=true;void(0);">Stop Search</a></td></tr></table>';
				}
				else{
					resultsProgressBar.innerHTML = '';
					if(newResults.length > foundCount){
				  		resultsDetails.innerHTML = 'Results <strong>' + foundCount + '</strong> of <strong><a href="javascript:displayIncrementalZeros(\'true\');">' + newResults.length + '</a></strong> sources contain <strong>' + searchTerm + '</strong> [<a id="zerotoggle" href="javascript:displayIncrementalZeros(\'true\');">Show Details</a>]';
				  	}
				  	else if(newResults.length == foundCount){
						resultsDetails.innerHTML = 'Results <strong>' + foundCount + '</strong> of <strong>' + newResults.length + '</strong> sources contain <strong>' + searchTerm + '</strong>';
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

				var newMetaUrl = '/search/search?id=' + sessionID;

				if(status == "successful" || status =="running"){
					var engines = xmlObj.getElementsByTagName('engine');
					var metaSearchResults = new Array();

					for(var i=0; i<engines.length; i++){
						var resource = engines[i].getElementsByTagName('resource');
						var results = new Array();

						results.id = resource[0].getAttribute('id');
						results.url = resource[0].getElementsByTagName('url')[0].firstChild.data;
						results.name = resource[0].getElementsByTagName('description')[0].firstChild.data;

						if(this.needsProxy){
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
					var html = '<img border="0" src="' + GLOBALS.baseImagePath + '/X_med.gif" /> Try FindIt@Stanford: <a target="new" href="' + openurl + '">' + result + '</a>';

					var body = document.getElementsByTagName("body").item(0);
					var bC = document.createElement('div');
					bC.className = 'hide';
					bC.innerHTML = html;
					bC.setAttribute('id','berry-ej');
					body.appendChild(bC);

					if(document.getElementById('SFXResults')){
						document.getElementById('SFXResults').className = 'berryContent';
						document.getElementById('SFXResults').innerHTML = html;
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
						document.getElementById('SpellResults').className = 'berryContent';
						document.getElementById('SpellResults').innerHTML = html;
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

							if(this.needsProxy){
								link = GLOBALS.proxyPrefix + link;
							}

							html += '<a class="indent" target="new" href="' + link + '">' + name + '</a><br />';
					}

					html = '<img border="0" src="' + GLOBALS.baseImagePath + '/X_med.gif" /> Try other Stanford databases:<br/>' + html;

					var body = document.getElementsByTagName("body").item(0);
					var bC = document.createElement('div');
					bC.className = 'hide';
					bC.innerHTML = html;
					bC.setAttribute('id','berry-db');
					body.appendChild(bC);

					if(document.getElementById('SULResults')){
						document.getElementById('SULResults').className = 'berryContent';
						document.getElementById('SULResults').innerHTML = html;
					}
				}

			break;
		}
	}

}






//additional incremental vars and funcs
var haltIncremental = false;
var date = new Date();
var startTime = date.getTime();

function getIncrementalResults() {
	var id = getMetaContent(document,'search_id');
	var template = getMetaContent(document,'search_template');
	var date = new Date();
	var url = GLOBALS.basePath + '/content/new-search.html?id='+id+'&source='+template+'&secs='+date.getSeconds();

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
// end additional incremental vars and funcs



// from temp.js: move to laneweb.js when ready
function submitSearchTemp() {
  var source = document.searchForm.source.options[document.searchForm.source.selectedIndex].value;
  var keywords = document.searchForm.keywords.value;

  if (keywords == '') {
    alert(nokeywords);
    return false;
  }

  else if (source == 'db') {
	var dest = GLOBALS.basePath + '/online/er.html?source=eResources&template=' + GLOBALS.lanewebTemplate + '&slice=db&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'eb') {
	var dest = GLOBALS.basePath + '/online/er.html?source=eResources&template=' + GLOBALS.lanewebTemplate + '&slice=eb&keywords=' + keywords;
    window.location.replace(dest);
	return false;
  }
  else if (source == 'calc') {
	var dest = GLOBALS.basePath + '/online/er.html?source=eResources&template=' + GLOBALS.lanewebTemplate + '&slice=calc&keywords=' + keywords;
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
	var dest = GLOBALS.basePath + '/howto/index.html?template=' + GLOBALS.lanewebTemplate + '&keywords=' + keywords;
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
// store last index and if current value is a disbaled option, return to previous option selected
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

// end temp.js 
