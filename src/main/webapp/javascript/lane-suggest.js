(function() {
	YAHOO.util.Event.addListener(window, 'load', function(){
		var dataSource,
		acWidget,
		searchtermsElm,
		selectElm;
		
		searchtermsElm = document.getElementById('searchTerms');
		selectElm = document.getElementById('searchSelect');
		
		dataSource = new YAHOO.widget.DS_XHR("/apps/suggest/json", ["suggest"]);
		dataSource.responseType = YAHOO.widget.DS_XHR.TYPE_JSON;
		dataSource.scriptQueryParam = "q";
		dataSource.connTimeout = 3000; 
		dataSource.maxCacheEntries = 100;
		
		acWidget = new YAHOO.widget.AutoComplete(searchtermsElm,"acResults", dataSource);
		acWidget.minQueryLength = 3;
		acWidget.useShadow = true;
		acWidget.animHoriz = false;
		acWidget.animVert = false;
		acWidget.autoHighlight = false;
			
		YAHOO.util.Event.addListener(searchtermsElm, 'focus', function(){
			acWidget.minQueryLength = 3;
			if(selectElm.options[selectElm.selectedIndex].value.match(/all|ej|database|book|software|video|cc|lanesite|bassett|history/)){
				acWidget.dataSource.scriptQueryAppend = 'l='+selectElm.options[selectElm.selectedIndex].value;
			}
			else if(selectElm.options[selectElm.selectedIndex].value.match(/clinical|peds|anesthesia|cardiology|emergency|hematology|internal-medicine|lpch-cerner|pharmacy|pulmonary/)){
				acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
			}
			else if(selectElm.options[selectElm.selectedIndex].value.match(/research/)){
				acWidget.dataSource.scriptQueryAppend = 'l=mesh';
			}
			else {
				acWidget.minQueryLength = -1;
			}
		});
	});
})();
