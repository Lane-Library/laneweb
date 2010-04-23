YUI().use('yui2-event','yui2-autocomplete','yui2-datasource','yui2-connection','node', function(Y){
    LANE.namespace('suggest');
    LANE.suggest = function(){
        var YE = Y.YUI2.util.Event, // shorthand for Event
        acWidgets = [];
    
	    YE.onContentReady('search', function() { // TODO: change this to on load listener?
	    	var suggestElms = Y.all('.laneSuggest'),i;
	    	for (i = 0; i < suggestElms.size(); i++){
	    		LANE.suggest.initialize(suggestElms.item(i));
	    	}
	    });
    
        return {
	        collapse: function(){
	            var i, widgets = LANE.suggest.getWidgets();
	            for(i = 0; i < widgets.size(); i++ ){
	                if(widgets.item(i).isContainerOpen()){
	                    widgets.item(i).collapseContainer();
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
	            trackingObject.title = searchSource.get('value') + '--suggest-selection-event';
	            trackingObject.path = item[0];
	            LANE.tracking.track(trackingObject);
	        },
	        onItemSelect: function(sType, aArgs) {
	        	var inputElm, searchForm;
	        	inputElm = aArgs[0].getInputEl();
	        	searchForm = new Y.Node(inputElm).ancestor('form');
                LANE.search.startSearch();
	            Y.Node.getDOMNode(searchForm).submit();
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
                    searchForm = searchTermsElm.ancestor('form');
                    searchFieldset = searchTermsElm.ancestor('fieldset');
                    searchSourceElm = searchFieldset.one('input[name="source"]');
                    searchSource = (searchSourceElm) ? searchSourceElm.get('value') : null;
                    
                    // create and add auto complete related elements
                    searchTermsAcContainer = Y.Node.create('<div class="acContainer"/>');
                    searchFieldset.insertBefore(searchTermsAcContainer, searchTermsElm);
                    searchTermsAcContainer.appendChild(searchFieldset.removeChild(searchTermsElm));
                    searchTermsAcInput = Y.Node.create('<div class="searchTermsAcInput"/>');
                    searchTermsAcContainer.appendChild(searchTermsAcInput);
                    dataSource = new Y.YUI2.util.XHRDataSource("/././apps/suggest/json");
                    dataSource.responseType = Y.YUI2.util.XHRDataSource.TYPE_JSON;
                    dataSource.responseSchema = {
                        resultsList: 'suggest'
                    };
                    //dataSource = new Y.YUI2.widget.DS_XHR("/././apps/suggest/json", ["suggest"]);
                    //dataSource.responseType = Y.YUI2.widget.DS_XHR.TYPE_JSON;
                    dataSource.scriptQueryParam = "q";
                    dataSource.connTimeout = 3000;
                    dataSource.maxCacheEntries = 100;
                    
                    acWidget = new Y.YUI2.widget.AutoComplete(Y.Node.getDOMNode(searchTermsElm), Y.Node.getDOMNode(searchTermsAcInput), dataSource);
                    LANE.suggest.addWidget(acWidget);
                    acWidget.minQueryLength = 3;
                    acWidget.useShadow = true;
                    acWidget.animHoriz = false;
                    acWidget.animVert = false;
                    acWidget.autoHighlight = false;
                    acWidget.itemSelectEvent.subscribe(LANE.suggest.trackItemSelect);
                    acWidget.itemSelectEvent.subscribe(LANE.suggest.onItemSelect);
                    
                    // for FF, submit form return key strike
                    YE.addListener(Y.Node.getDOMNode(searchTermsElm), 'keyup', function(e) {
                        if (Y.YUI2.env.ua.gecko && e.keyCode == '13') {
                            LANE.search.startSearch();
                            Y.Node.getDOMNode(searchForm).submit();
                        }
                    });
                    
                    YE.addListener(Y.Node.getDOMNode(searchTermsElm), 'focus', function() {
                        acWidget.minQueryLength = 3;
                        if (searchSource && searchSource.match(/^(all|articles|catalog)/)) {
                            acWidget.dataSource.scriptQueryAppend = 'l=er-mesh';
                        } else if (null == searchSource && path.match(/\/portals\/.*\.html/)) {
                            acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
                        } else {
                            acWidget.minQueryLength = -1;
                        }
                    });
	    	}
        };
    }();

    
    
    
});
