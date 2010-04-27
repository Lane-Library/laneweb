YUI().use('yui2-autocomplete','yui2-datasource','node', function(Y){
        var suggestElms = Y.all('.laneSuggest'), i,
            acWidgets = [],
            trackItemSelect = function(sType, aArgs) {
                var item, trackingObject = {};
                item = aArgs[2];
                trackingObject.title = searchSource.get('value') + '--suggest-selection-event';
                trackingObject.path = item[0];
                LANE.tracking.track(trackingObject);
            },
            onItemSelect = function(sType, aArgs) {
                var inputElm, searchForm;
                inputElm = aArgs[0].getInputEl();
                searchForm = new Y.Node(inputElm).ancestor('form');
                LANE.search.startSearch();
                Y.Node.getDOMNode(searchForm).submit();
            },
            initialize = function(input) {
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
                dataSource.scriptQueryParam = "q";
                dataSource.connTimeout = 3000;
                dataSource.maxCacheEntries = 100;
                
                acWidget = new Y.YUI2.widget.AutoComplete(Y.Node.getDOMNode(searchTermsElm), Y.Node.getDOMNode(searchTermsAcInput), dataSource);
                acWidgets.push(acWidget);
                acWidget.minQueryLength = 3;
                acWidget.useShadow = true;
                acWidget.animHoriz = false;
                acWidget.animVert = false;
                acWidget.autoHighlight = false;
                acWidget.itemSelectEvent.subscribe(trackItemSelect);
                //FIXME: this threw an exception . . .
//                acWidget.itemSelectEvent.subscribe(onItemSelect);
                
                // for FF, submit form return key strike
                if (Y.UA.gecko) {
                    Y.on('keyup', function(event) {
                        if (event.keyCode == '13') {
                            LANE.search.startSearch();
                            searchForm.submit();
                        }
                    }, searchTermsElm);
                }
                
                Y.on('focus', function() {
                    acWidget.minQueryLength = 3;
                    searchSource = (searchSourceElm) ? searchSourceElm.get('value') : null;
                    if (searchSource && searchSource.match(/^(all|articles|catalog)/)) {
                        acWidget.dataSource.scriptQueryAppend = 'l=er-mesh';
                    } else if (null == searchSource) { // assume source-less is metasearch form on peds portal, etc.
                        acWidget.dataSource.scriptQueryAppend = 'l=mesh-di';
                    } else {
                        acWidget.minQueryLength = -1;
                    }
                }, searchTermsElm);
            };
        
        for (i = 0; i < suggestElms.size(); i++) {
            initialize(suggestElms.item(i));
        }
});
