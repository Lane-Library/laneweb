(function() {
	
	var Lane = Y.lane;
	
	var searchSelectWidget = new Lane.SearchSelectWidget({srcNode:Y.one("#searchSource"),render:true});
	
	var selectModel = searchSelectWidget.get("model");
	
	var searchTerms = new Lane.TextInput(Y.one("#searchTerms"), selectModel.getSelectedTitle());
	
	var Search = Y.namespace("lane.Search");
	
	Y.augment(Search, Y.EventTarget, null, null, {
		prefix : "search",
		emitFacade : true
	});
	
	var searchIndicator = Lane.SearchIndicator;
	
	var form = Y.one("#search");
	
	Search._doSubmit = function(event) {
		if (!searchTerms.getValue()) {
            throw ('nothing to search for');
		}
		searchIndicator.show();
		form.submit();
	};
	
	Search.publish("submit",{defaultFn : Search._doSubmit});
	
	Search._selectedChange = function(event) {
		this.fire("sourceChange", {newVal:event.newVal});
	};
	
	Search.submitSearch = function() {
		this.fire("submit");
	};
	
	form.on("submit", Search.submitSearch, Search);
	
	Search.getSearchTerms = function() {
		return searchTerms.getValue();
	};
	
	Search.setSearchTerms = function(terms) {
		searchTerms.setValue(terms);
	};
	
	Search.searchTermsPresent = function() {
		return searchTerms.getValue();
	};
	
	Search.getSearchSource = function() {
		return selectModel.getSelected();
	};
	
	selectModel.addTarget(Search);
	
	Search.on("select:selectedChange", Search._selectedChange);
	
	Search.addTarget(Lane);
	
	var searchTipsLink = Y.one('#searchTips a');
	
	searchTipsLink.set("href", searchTipsLink.get("href") + "#" + selectModel.getSelected());
	
	selectModel.addTarget(searchTipsLink);
	
	searchTipsLink.on("select:selectedChange", function(event) {
		this.set("href", this.get("href").replace(/#.*/, "#" + event.newVal));
	});
	
	Y.augment(searchTerms, Y.EventTarget);
	
	selectModel.addTarget(searchTerms);
	
	searchTerms.after("select:selectedChange", function(event) {
		this.setHintText(event.target.getSelectedTitle());
	});
	
	var searchTermsSuggest = new Y.lane.Suggest(searchTerms.getInput());
	
	searchTermsSuggest.setLimitForSource = function(source) {
        var limit = "";
        if (source.match(/^(all|articles|catalog)/)) {
            limit = "er-mesh";
        } else if (source.match(/^bioresearch/)) {
            limit = "mesh";
        } else if (source.match(/^history/)) {
            limit = "history";
        } else if (source.match(/^bassett/)) {
            limit = "bassett";
        }
        this.setLimit(limit);
    };
    
    searchTermsSuggest.on("select:selectedChange", function(event) {
    	this.setLimitForSource(event.newVal);
    });
    
    selectModel.addTarget(searchTermsSuggest);
    
    searchTermsSuggest.on("select", Search.submitSearch, Search);
	
})();
