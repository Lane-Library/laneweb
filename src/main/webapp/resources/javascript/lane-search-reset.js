(function() {
    var self,
    
     SearchReset = function() {
        var searchTerms = Y.one('#searchTerms'),
        searchReset = Y.one('#searchReset'),
        form = Y.one("#search"),
        searchContent = Y.one('.search'),
        eventHandle,
        reset;
        if (!searchReset) {
            searchReset = Y.Node.create("<span id='searchReset' title='Clear Search'>×</span>");
            searchReset.setStyle("display","none");
            form.one("#searchFields").append(searchReset);
        }
        Y.publish("lane:searchFormReset",{broadcast:1,emitFacade: true});
        searchReset.on("click", function(e){
            reset.resetSearch(e);
        });
        eventHandle = searchTerms.on("valuechange", function(){
            reset.syncUI();
        });
        Y.on("lane:searchPicoChange", function(){
            reset.syncUI();
        });
        form.on("submit", function(){
            reset.hide();
//            Y.detach(eventHandle);
        });
        Y.on("lane:beforeSearchSubmit", function(){
            reset.hide();
//            Y.detach(eventHandle);
        });
        reset = {
            show: function() {
                searchReset.setStyle("display", "block");
            },
            hide: function() {
                searchReset.setStyle("display", "none");
            },
            syncUI : function() {
                if(Y.lane.Search.searchTermsPresent()){
                    reset.show();
                }
                else{
                    reset.hide();
                }
            },
            resetSearch : function(event) {
                var inputs, title, i;
                form.all('input[type="text"]').set('value','');
                inputs = form.all('input[type="text"]');
                for(i = 0; i < inputs.size(); i++){
                    title = inputs.item(i).get('title');
                    if (title) {
                        (new Y.lane.TextInput(inputs.item(i), title));
                    }
                }
                inputs.item(0).focus();
                reset.syncUI();
                if(searchContent){
                    searchContent.setStyle('visibility','hidden');
                }
                Y.fire('lane:searchFormReset');
                event.preventDefault();
            }        
        };
        return reset;
    };
    Y.lane.SearchReset = SearchReset;
    if (Y.one("#search")) {
        self = new SearchReset();
        self.syncUI();
    }
})();


        