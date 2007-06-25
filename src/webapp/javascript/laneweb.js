var imagePath = '/././images/templates/default/';
var nokeywords = 'Please enter one or more search terms.';
var searching = false;

YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
	YAHOO.util.Event.addListener(window, 'unload', finalize);
	listSearchTagline(document.getElementById("source"));
	resetFocus();
}

function finalize(e) {
	searching = false;
}

function listSearchTagline(select) {
	var elementId = select.options[select.selectedIndex].value;
	var elementContainerForDisplayText = document.getElementById(elementId + "SearchTagline");
	document.getElementById("displaySearchTaglineText").innerHTML = (elementContainerForDisplayText != null) ? elementContainerForDisplayText.innerHTML : document.getElementById("allSearchTagline").innerHTML;
}

function startState(url) {
}

function openNewWindow(url,features) {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if(window.focus){
        w.focus();
    }
}
// allow deprecated openSearchResult calls
var openSearchResult = openNewWindow; 

function loadTab(n, maxTabsPerBox) {
	for (var i=1; i<=maxTabsPerBox; i++) {
		var elem = document.getElementById("tab" + i);
		var elemContent = document.getElementById("tab" + i + "Content");
		var hideClassIndex = elemContent.className.indexOf('hide');
		if (i == n) {
			elem.className = "activeTab";
			if (hideClassIndex != -1) {
				elemContent.className = elemContent.className.substring(0, hideClassIndex - 1);
			}
		} else {
			elem.className = "bgTab";
			if (hideClassIndex == -1) {
				elemContent.className = elemContent.className + ' hide';
			}
		}
	}
}

function resetFocus() {
	window.focus();
}

function setFocusOnSearchBox() {
	document.forms["searchForm"].elements["keywords"].focus();
	document.forms["searchForm"].elements["keywords"].select();
}
