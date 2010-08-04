(function() {
    var Y = LANE.Y,
        form = Y.one('#search'),
        searchTerms = Y.one('#searchTerms'),
        eventHandle,
        searchReset = Y.one('#searchReset'),
        syncUI = function(event) {
            if(LANE.Search.searchTermsPresent()){
                searchReset.setStyle("display","block");
            }
            else{
                searchReset.setStyle("display","none");
            }
        },
        resetSearch = function(event) {
            var inputs, title, i;
            form.all('input[type="text"]').set('value','');
            inputs = form.all('input[type="text"]');
            for(i = 0; i < inputs.size(); i++){
                title = inputs.item(i).get('title');
                if (title) {
                    new LANE.TextInput(inputs.item(i), title);
                }
            }
            syncUI();
            Y.fire('lane:searchFormReset');
            event.preventDefault();
        };
    Y.publish("lane:searchFormReset",{broadcast:2,emitFacade: true});
    if (searchReset) {
        syncUI();
        searchReset.on("click", resetSearch);
        eventHandle = searchTerms.on("valueChange", syncUI);
        Y.Global.on("lane:searchPicoChange", syncUI);
        form.on("submit", function(){
            searchReset.setStyle("display","none");
            Y.detach(eventHandle);
        });
        Y.Global.on("lane:beforeSearchSubmit", function(){
            searchReset.setStyle("display","none");
            Y.detach(eventHandle);
        });
    }
})();


        