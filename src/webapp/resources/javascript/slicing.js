//**************************
// slice results into format tabs
//
var labels 	= new Array('All','eJournals','Databases','eBooks','Calculators');
var classes = new Array('all','ej','db','eb','calc');
var resultCounts	= new Array();

function getCount(limitType){
	var results = document.getElementById('searchResults');
	var resultUls = results.getElementsByTagName('ul');
	var resultLinks = new Array();
	var count = 0;

	for (var i = 0; i < resultUls.length; i++){
		if(resultUls[i].className == limitType){
			resultLinks = resultUls[i].getElementsByTagName('a');
		}
	}

	for (var y = 0; y < resultLinks.length; y++){
		if(resultLinks[y].className == '' || resultLinks[y].className == 'proxy'){
			count++
		}
	}

	if(document.getElementById('keywordresult')){
		var keyword = document.getElementById('keywordresult').innerHTML;
		var keywordUri = cleanKW(keyword);
	}
	
	//if count is zero, we want to create berry box content
	if(!resultLinks.length && !document.getElementById('berry-' + limitType)){
		var body = document.getElementsByTagName("body").item(0);
		var bC = document.createElement('div');
		bC.className = 'hide';
		bC.innerHTML = '<img border="0" src="' + GLOBALS.baseImagePath + '/X_med.gif" alt="X" /> Also try <a href="' + GLOBALS.basePath + '/new-search.html?source=clinical&template=' + GLOBALS.lanewebTemplate + '&keywords=' + keywordUri +'">Clinical Portal</a>';
		bC.setAttribute('id','berry-' + limitType);
		body.appendChild(bC);
	}
	return count;
}

function paintTabs(){

	var all = 0;

	for(var i = 1; i < classes.length; i++){
		resultCounts[i] = getCount(classes[i]);
		all = all + resultCounts[i];
	}
		resultCounts[0] = all;

	var bar = '';

	for(var i = 0; i < labels.length; i++){
		bar = bar + '<li><span class="' + classes[i] + '" name="' + classes[i] + '" onclick="javascript:showTab(\'' + classes[i] + '\');">' + labels[i] + ': <p class="hitCount">' + resultCounts[i] + '</p></span></li>';
	}

	document.getElementById('tabs').innerHTML = '<ul>' + bar + '</ul><ul id="faqSearchResults" class="metaSearchResults"></ul><ul id="loisSearchResults" class="metaSearchResults"></ul><ul id="pubmedSearchResults" class="metaSearchResults"></ul><ul id="googleSearchResults" class="metaSearchResults"></ul><ul class="metaSearchResultsRight">&nbsp; </ul>';
}

function showTab(limitType){

	// if slice parameter sent in URL, default to specified slice
	// otherwise, default to 'all'
	if (limitType == 'default'){
		for (var i = 0; i < classes.length; i++){
			if (location.search.indexOf('slice=' + classes[i]) > 0){
				limitType = classes[i];
			}
		}
		if (limitType == 'default') {
			limitType = 'all';
		}
	}

	// swap active tab link out
	var tabs = document.getElementById('tabs');
	tabLinks = tabs.getElementsByTagName('span');
	var activeLabel = '';

	for (var i = 0; i < tabLinks.length; i++){

		// activate link if it has a className of limitType
		if( tabLinks[i].className == 'active' && tabLinks[i].getAttribute('name') == limitType) {
		}
		else if(tabLinks[i].className == limitType){
			tabLinks[i].className = 'active';
			activeLabel = labels[i];
		}
		else{
			tabLinks[i].className = classes[i];
		}
	}

	var results = document.getElementById('searchResults');
	results.className = 'hide';

	var resultUls = results.getElementsByTagName('ul');

	var newResults = document.getElementById('newResults');
	newResults.innerHTML = '';

	if(limitType == 'all'){
			results.className = '';
			newResults.className = 'hide';
	}
	else{
	newResults.className = '';
		for (var i = 0; i < resultUls.length; i++){
			if(resultUls[i].className == limitType){
				newResults.innerHTML = '<br/><ul>' + resultUls[i].innerHTML + '</ul>';
			}
		}
	}

	//get berry box content, if any
	var berryContent;
	if(document.getElementById('berry-' + limitType)){
		berryContent = document.getElementById('berry-' + limitType);
		newResults.innerHTML = '<div class="berryContent">' + berryContent.innerHTML + '</div>' + newResults.innerHTML;
	}

}








///temp- for online/rms-test.html
//**************************
// slice results into format tabs
//
var labels 	= new Array('All','eJournals','Databases','eBooks','Calculators');
var divs = new Array('all','ej','db','eb','cc');
var resultCounts	= new Array();

function getCount2(limitType){
	var results = document.getElementById('searchResults');
	var resultUls = results.getElementsByTagName('div');
	var resultLinks = new Array();
	var count = 0;

	for (var i = 0; i < resultUls.length; i++){
		if(resultUls[i].getAttribute('id') == limitType){
			resultLinks = resultUls[i].getElementsByTagName('a');
		}
	}

	for (var y = 0; y < resultLinks.length; y++){
//		if(resultLinks[y].className == '' || resultLinks[y].className == 'proxy'){
			count++
//		}
	}

	if(document.getElementById('keywordresult')){
		var keyword = document.getElementById('keywordresult').innerHTML;
		var keywordUri = cleanKW(keyword);
	}
	
	//if count is zero, we want to create berry box content
	if(!resultLinks.length && !document.getElementById('berry-' + limitType)){
		var body = document.getElementsByTagName("body").item(0);
		var bC = document.createElement('div');
		bC.className = 'hide';
		bC.innerHTML = '<img border="0" src="' + GLOBALS.baseImagePath + '/X_med.gif" alt="X" /> Also try <a href="' + GLOBALS.basePath + '/new-search.html?source=clinical&template=' + GLOBALS.lanewebTemplate + '&keywords=' + keywordUri +'">Clinical Portal</a>';
		bC.setAttribute('id','berry-' + limitType);
		body.appendChild(bC);
	}
	return count;
}

function paintTabs2(){

	var all = 0;

	for(var i = 1; i < divs.length; i++){
		resultCounts[i] = getCount2(divs[i]);
		all = all + resultCounts[i];
	}
		resultCounts[0] = all;

	var bar = '';

	for(var i = 0; i < labels.length; i++){
		bar = bar + '<li><span class="' + divs[i] + '" name="' + divs[i] + '" onclick="javascript:showTab2(\'' + divs[i] + '\');">' + labels[i] + ': <p class="hitCount">' + resultCounts[i] + '</p></span></li>';
	}

	document.getElementById('tabs').innerHTML = '<ul>' + bar + '</ul><ul id="faqSearchResults" class="metaSearchResults"></ul><ul id="loisSearchResults" class="metaSearchResults"></ul><ul id="pubmedSearchResults" class="metaSearchResults"></ul><ul id="googleSearchResults" class="metaSearchResults"></ul><ul class="metaSearchResultsRight">&nbsp; </ul>';
}

function showTab2(limitType){

	// if slice parameter sent in URL, default to specified slice
	// otherwise, default to 'all'
	if (limitType == 'default'){
		for (var i = 0; i < divs.length; i++){
			if (location.search.indexOf('slice=' + divs[i]) > 0){
				limitType = divs[i];
			}
		}
		if (limitType == 'default') {
			limitType = 'all';
		}
	}

	// swap active tab link out
	var tabs = document.getElementById('tabs');
	tabLinks = tabs.getElementsByTagName('span');
	var activeLabel = '';

	for (var i = 0; i < tabLinks.length; i++){

		// activate link if it has a className of limitType
		if( tabLinks[i].className == 'active' && tabLinks[i].getAttribute('name') == limitType) {
		}
		else if(tabLinks[i].className == limitType){
			tabLinks[i].className = 'active';
			activeLabel = labels[i];
		}
		else{
			tabLinks[i].className = divs[i];
		}
	}

	var results = document.getElementById('searchResults');
	results.className = 'hide';

	var resultUls = results.getElementsByTagName('div');

	var newResults = document.getElementById('newResults');
	newResults.innerHTML = '';

	if(limitType == 'all'){
			results.className = '';
			newResults.className = 'hide';
	}
	else{
		newResults.className = '';
		for (var i = 0; i < resultUls.length; i++){
			if(resultUls[i].getAttribute('id') == limitType){
				newResults.innerHTML = '<br/><ul>' + resultUls[i].innerHTML + '</ul>';
			}
		}
	}

	//get berry box content, if any
	var berryContent;
	if(document.getElementById('berry-' + limitType)){
		berryContent = document.getElementById('berry-' + limitType);
		newResults.innerHTML = '<div class="berryContent">' + berryContent.innerHTML + '</div>' + newResults.innerHTML;
	}

}





