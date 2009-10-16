(function() {
	YAHOO.util.Event.addListener(window, 'load', function(){
		var dataSource,
		acWidget,
		searchtermsElm,
		selectElm,
		onSearchtermsKeyUp,
		onItemSelect;
		
        //submit form if return key hit
        // otherwsie 2 returns required
        onSearchtermsKeyUp = function(e) {
            var form, keycode;
            keycode = e.keyCode;
            if (13 == keycode){
                form = document.getElementById('searchForm');
                LANE.search.startSearch();
                form.submit();
            }
        };
		
        // when a suggest list item is selected ...
        //  - track the suggest-selection-event
        //  - submit the form
        onItemSelect = function(sType, aArgs) {
            var item, 
                form, 
                trackingObject = {};
            item = aArgs[2];
            form = document.getElementById('searchForm');
    		trackingObject.title = 'suggest-selection-event';
    		trackingObject.path = item[0];
    		LANE.tracking.track(trackingObject);
            LANE.search.startSearch();
            form.submit();
        };
        
		searchtermsElm = document.getElementById('searchTerms');
		YAHOO.util.Event.addListener(searchtermsElm, 'keyup', onSearchtermsKeyUp);
		
		selectElm = document.getElementById('searchSelect');
		
		dataSource = new YAHOO.widget.DS_XHR("/././apps/suggest/json", ["suggest"]);
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
		acWidget.itemSelectEvent.subscribe(onItemSelect);
			
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
