YUI().use('yui2-event','yui2-dom','yui2-autocomplete','yui2-datasource','yui2-connection', function(Y){
    LANE.namespace('suggest');
    LANE.suggest = function(){
        var YE = Y.YUI2.util.Event, // shorthand for Event
        YD = Y.YUI2.util.Dom, // shorthand for Dom
        acWidgets = [];
    
	    YE.onContentReady('search', function() { // TODO: change this to on load listener?
	    	var suggestElms = YD.getElementsByClassName('laneSuggest'),i;
	    	for (i = 0; i < suggestElms.length; i++){
	    		LANE.suggest.initialize(suggestElms[i]);
	    	}
	    });
    
        return {
	        collapse: function(){
	            var i, widgets = LANE.suggest.getWidgets();
	            for(i = 0; i < widgets.length; i++ ){
	                if(widgets[i].isContainerOpen()){
	                    widgets[i].collapseContainer();
	                }
	            }
	        },
	        getWidgets: function(){
	            return acWidgets;
	        },
	        addWidget: function(widget){
	            acWidgets.push(widget);
	        },
	        trackItemSelect: function(sType, aArgs) {
	            var item, trackingObject = {};
	            item = aArgs[2];
	            trackingObject.title = searchSource.value + '--suggest-selection-event';
	            trackingObject.path = item[0];
	            LANE.tracking.track(trackingObject);
	        },
	        onItemSelect: function(sType, aArgs) {
	        	var inputElm, searchForm;
	        	inputElm = aArgs[0].getInputEl();
	        	searchForm = YD.getAncestorByTagName(inputElm,"FORM");
                LANE.search.startSearch();
	            searchForm.submit();
	    	},
	    	initialize: function(input) {
	            var dataSource, 
	            acWidget, 
	            searchForm, 
	            searchFieldset, 
	            searchTermsElm, 
	            searchSourceElm, 
	            searchSource, 
	            path = document.location.pathname,
	            onItemSelect, 
	            searchTermsAcContainer, 
	            searchTermsAcInput;
	            
	            searchTermsElm = input;
	            searchForm = YD.getAncestorByTagName(searchTermsElm,"FORM");
	            searchFieldset = YD.getAncestorByTagName(searchTermsElm,"FIELDSET");
	            searchSourceElm = YD.getChildrenBy(searchFieldset,function(el){
	            	return el.name == "source";
	            	});
	            searchSource = searchSourceElm[0];
	            
	            // create and add auto complete related elements
	            searchTermsAcContainer = document.createElement('DIV');
	            YD.addClass(searchTermsAcContainer, 'acContainer');
	            YD.insertBefore(searchTermsAcContainer, searchTermsElm);
	            searchTermsAcContainer.appendChild(searchFieldset.removeChild(searchTermsElm));
	            searchTermsAcInput = document.createElement('DIV');
	            searchTermsAcInput.className = 'searchTermsAcInput';
	            searchTermsAcContainer.appendChild(searchTermsAcInput);
	            
	            dataSource = new Y.YUI2.util.XHRDataSource("/././apps/suggest/json");
	            dataSource.responseType = Y.YUI2.util.XHRDataSource.TYPE_JSON;
	            dataSource.responseSchema = {resultsList:'suggest'};
	            //dataSource = new Y.YUI2.widget.DS_XHR("/././apps/suggest/json", ["suggest"]);
	            //dataSource.responseType = Y.YUI2.widget.DS_XHR.TYPE_JSON;
	            dataSource.scriptQueryParam = "q";
	            dataSource.connTimeout = 3000;
	            dataSource.maxCacheEntries = 100;
	            
	            acWidget = new Y.YUI2.widget.AutoComplete(searchTermsElm, searchTermsAcInput, dataSource);
	            LANE.suggest.addWidget(acWidget);
	            acWidget.minQueryLength = 3;
	            acWidget.useShadow = true;
	            acWidget.animHoriz = false;
	            acWidget.animVert = false;
	            acWidget.autoHighlight = false;
	            acWidget.itemSelectEvent.subscribe(LANE.suggest.trackItemSelect);
	            acWidget.itemSelectEvent.subscribe(LANE.suggest.onItemSelect);
	            
	            // for FF, submit form return key strike
	            YE.addListener(searchTermsElm, 'keyup', function(e) {
	                if (Y.YUI2.env.ua.gecko && e.keyCode == '13') {
	                    LANE.search.startSearch();
	                    searchForm.submit();
	                }
	            });
	            
	            YE.addListener(searchTermsElm, 'focus', function() {
	                acWidget.minQueryLength = 3;
	                if (searchSource && searchSource.value.match(/^(all|articles|catalog)/)) {
	                    acWidget.dataSource.scriptQueryAppend = 'l=er-mesh';
	                } else if (null == searchSource && path.match(/^\/portals\/.*\.html/)) {
	                	acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
	                } else {
	                    acWidget.minQueryLength = -1;
	                }
	            });
	    	}
        };
    }();

    
    
    
});
