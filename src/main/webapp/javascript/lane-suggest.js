(function(){
    LANE.namespace('suggest');
    LANE.suggest = function(){
    	var acWidget;
        return {
            collapse: function(){
        		var widget = LANE.suggest.getWidget();
        		if(widget.isContainerOpen()){
        			widget.collapseContainer();
        		}
            },
            getWidget: function(){
            	return acWidget;
            },
            setWidget: function(widget){
            	acWidget = widget;
            }
        };
    }();
    
    YAHOO.util.Event.addListener(window, 'load', function(){
        var dataSource,
        acWidget,
        searchForm,
        searchtermsElm,
        searchSource,
        onItemSelect;
        searchForm = document.getElementById('search');
        searchtermsElm = document.getElementById('searchTerms');
        searchSource = document.getElementById('searchSource');

        // when a suggest list item is selected ...
        //  - track the suggest-selection-event
        //  - submit the form
        onItemSelect = function(sType, aArgs) {
            var item, 
                trackingObject = {};
            item = aArgs[2];
            trackingObject.title = searchSource.value + '--suggest-selection-event';
            trackingObject.path = item[0];
            LANE.tracking.track(trackingObject);
            LANE.search.startSearch();
            searchForm.submit();
        };
        
        
        dataSource = new YAHOO.widget.DS_XHR("/././apps/suggest/json", ["suggest"]);
        dataSource.responseType = YAHOO.widget.DS_XHR.TYPE_JSON;
        dataSource.scriptQueryParam = "q";
        dataSource.connTimeout = 3000; 
        dataSource.maxCacheEntries = 100;
        
        acWidget = new YAHOO.widget.AutoComplete(searchtermsElm,"searchTermsAcInput", dataSource);
        LANE.suggest.setWidget(acWidget);
        acWidget.minQueryLength = 3;
        acWidget.useShadow = true;
        acWidget.animHoriz = false;
        acWidget.animVert = false;
        acWidget.autoHighlight = false;
        acWidget.itemSelectEvent.subscribe(onItemSelect);
            
        YAHOO.util.Event.addListener(searchtermsElm, 'focus', function(){
            acWidget.minQueryLength = 3;
            if(searchSource.value.match(/^articles-(all|pubmed|sciencecitation)/)){
            	acWidget.dataSource.scriptQueryAppend = 'l=mesh';
            }
            else if(searchSource.value.match(/^catalog-(all|ej|book|database|software|cc|video)/)){
                acWidget.dataSource.scriptQueryAppend = 'l=' + searchSource.value.substring(8);
            }
            else if(searchSource.value.match(/^specialty-(all|pediatrics|anesthesia|cardiology|emergency|hematology|internal-medicine|lpch-cerner|pharmacy|pulmonary)/)){
                acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
            }
            else if(searchSource.value.match(/^specialty-bioresearch/)){
                acWidget.dataSource.scriptQueryAppend = 'l=mesh';
            }
            else {
                acWidget.minQueryLength = -1;
            }
        });
    });
})();
