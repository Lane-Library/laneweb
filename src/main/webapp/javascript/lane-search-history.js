(function() {
    var searchString = LANE.search.getEncodedSearchString(),
        YD = YAHOO.util.Dom,
        YE = YAHOO.util.Event,
        YH = YAHOO.util.History,
        tabChangeHandler = function(tabId){
            var r = document.getElementById(tabId+'Tab').result;// result tab to make active
            //TODO: this reads private attributes from Result ... change?
            if (r._state == 'initialized') {
                r.show();
            } else if (r._state == 'searched') {
                LANE.search.eresources.getCurrentResult().hide();
                LANE.search.eresources.setCurrentResult(r);
                r.show();
            } else {
                // TODO: necessary?
                //r._state = 'initialized';
            }
        },
        initializeHistory = function(){
            var initialTab = YH.getBookmarkedState("aTab") || YH.getQueryStringParameter("source");
            YH.onReady(function() {
                tabChangeHandler(YH.getCurrentState("aTab"));
            });
            YH.register("aTab", initialTab, tabChangeHandler);
            YH.initialize("yui-history-field", "yui-history-iframe");
        };

    if ( searchString && YD.inDocument('yui-history-field') && YD.inDocument('yui-history-iframe') ) {
        YE.addListener(this,'load',initializeHistory);
    }
})();