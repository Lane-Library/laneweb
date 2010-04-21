YUI().use('yui2-event','yui2-dom','yui2-autocomplete','yui2-datasource', function(Y){
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
    
    Y.YUI2.util.Event.onContentReady('searchFields', function(){
            var dataSource, acWidget, searchForm, searchTermsElm, searchSource, onItemSelect, searchTermsAcContainer, searchTermsAcInput;
            searchForm = document.getElementById('search');
            searchTermsElm = document.getElementById('searchTerms');
            searchSource = document.getElementById('searchSource');
            
            // when a suggest list item is selected ...
            //  - track the suggest-selection-event
            //  - submit the form
            onItemSelect = function(sType, aArgs) {
                var item, trackingObject = {};
                item = aArgs[2];
                trackingObject.title = searchSource.value + '--suggest-selection-event';
                trackingObject.path = item[0];
                LANE.tracking.track(trackingObject);
                LANE.search.startSearch();
                searchForm.submit();
            };
            
            //create and add auto complete related elements
            searchTermsAcContainer = document.createElement('DIV');
            Y.YUI2.util.Dom.addClass(searchTermsAcContainer, 'acContainer');
            Y.YUI2.util.Dom.insertBefore(searchTermsAcContainer, searchTermsElm);
            searchTermsAcContainer.appendChild(this.removeChild(searchTermsElm));
            searchTermsAcInput = document.createElement('DIV');
            searchTermsAcInput.id = 'searchTermsAcInput';
            searchTermsAcContainer.appendChild(searchTermsAcInput);
            
            dataSource = new Y.YUI2.widget.DS_XHR("/././apps/suggest/json", ["suggest"]);
            dataSource.responseType = Y.YUI2.widget.DS_XHR.TYPE_JSON;
            dataSource.scriptQueryParam = "q";
            dataSource.connTimeout = 3000;
            dataSource.maxCacheEntries = 100;
            
            acWidget = new Y.YUI2.widget.AutoComplete(searchTermsElm, searchTermsAcInput, dataSource);
            LANE.suggest.setWidget(acWidget);
            acWidget.minQueryLength = 3;
            acWidget.useShadow = true;
            acWidget.animHoriz = false;
            acWidget.animVert = false;
            acWidget.autoHighlight = false;
            acWidget.itemSelectEvent.subscribe(onItemSelect);
            
            // for FF, submit form return key strike
            Y.YUI2.util.Event.addListener(searchTermsElm, 'keyup', function(e) {
                if (Y.YUI2.env.ua.gecko && e.keyCode == '13') {
                    LANE.search.startSearch();
                    searchForm.submit();
                }
            });
            
            Y.YUI2.util.Event.addListener(searchTermsElm, 'focus', function() {
                acWidget.minQueryLength = 3;
                if (searchSource.value.match(/all-all/)) {
                    acWidget.dataSource.scriptQueryAppend = 'l=er-mesh';
                } else if (searchSource.value.match(/^articles-(all|pubmed|sciencecitation)/)) {
                    acWidget.dataSource.scriptQueryAppend = 'l=mesh';
                } else if (searchSource.value.match(/^catalog-(all|ej|book|database|software|cc|video)/)) {
                    acWidget.dataSource.scriptQueryAppend = 'l=' + searchSource.value.substring(8);
                } else if (searchSource.value.match(/^specialty-(all|pediatrics|anesthesia|cardiology|emergency|hematology|internal-medicine|lpch-cerner|pharmacy|pulmonary)/)) {
                    acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
                } else if (searchSource.value.match(/^specialty-bioresearch/)) {
                    acWidget.dataSource.scriptQueryAppend = 'l=mesh';
                } else {
                    acWidget.minQueryLength = -1;
                }
            });
    });
});
