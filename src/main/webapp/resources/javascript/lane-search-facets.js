(function() {
    var elt = Y.one('#searchFacets'),
        Lane = Y.lane,
        searchIndicator = Lane.searchIndicator,
        Model = Lane.Model,
        basePath = Model.get(Model.BASE_PATH) || "",
        encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
        container = Y.one('#searchResults'),
        facets, i, type, source, Result,
        clickHandler = function(event) {
            var result = this.getData('result');
            try {
                Lane.SearchHistory.addValue("facet", this.getData('result')._source);
            } catch (e) {
                //log somewhere ... no need to break/alert
                result.show();
            }
            event.preventDefault();
        },
    SearchFacets = function(){
        var currentResult = null;
        return {
            setCurrentResult: function(result){
                currentResult = result;
            },
            getCurrentResult: function(){
                return currentResult;
            },
            setActiveFacet: function(facetId){
                // result facet to make active
                var result = Y.one('#' + facetId + 'Facet').getData('result');
                if (result !== undefined) {
                    if (result._state === 'initialized') {
                        result.show();
                    } else if (result._state === 'searched') {
                        SearchFacets.getCurrentResult().hide();
                        SearchFacets.setCurrentResult(result);
                        result.show();
                    }
                }
            }
        };
    }(),
    setFacetResult = function(facet) {
        var id = facet.get("id");
        if (id.match("Facet$") && !facet.hasClass('inactiveFacet')) {
            type = id.substring(0, id.indexOf('-'));
            source = id.substring(0, id.indexOf('Facet'));
            if (type) {
                facet.setData('result', new Result(type, source, facet, container));
                if (facet.hasClass('current-facet')) {
                    facet.getData('result').setContent(container.get('innerHTML'));
                    SearchFacets.setCurrentResult(facet.getData('result'));
                }
                Y.on('click', clickHandler, facet);
            }
        }
    };

    //TODO: remove the following line when no longer need global reference
    Y.lane.SearchFacets = SearchFacets;

    Result = function(type, source, facet, container){
        this.publish("new-content");
        this.addTarget(Lane);
        this._type = type;
        this._source = source;
        this._facet = facet;
        this._container = container;
        this._url = basePath + '/plain/search/' + this._type + '/' + this._source + '.html?source=' + this._source + '&q=' + encodedQuery;
        this._state = 'initialized';
        this._callback = {
            on: {
                success: function(id, o, args){
                    var result = args.result;
                    result.setContent(o.responseText);
                    result.show();
                }
            },
            "arguments": {
                result: this
            }
        };
    };
        Result.prototype.setContent = function(content){
            this._content = content;
            this._state = 'searched';
        };
        Result.prototype.show = function(){
            if (this._state === 'initialized') {
                this.getContent();
                searchIndicator.show();
            } else if (this._state === 'searching') {
                alert('search in progress');
            } else {
                SearchFacets.getCurrentResult().hide();
                SearchFacets.setCurrentResult(this);
                this._facet.addClass('current-facet');
                this._container.set("innerHTML", this._content);
                this.fire("new-content");
                searchIndicator.hide();
            }
        };
        Result.prototype.getContent = function(){
            if (this._state === 'initialized') {
                this._state = 'searching';
                Y.io(this._url, this._callback);
            } else
                if (this._state === 'searched') {
                    this.show();
                } else
                    if (this._state === 'searching') {
                        alert('search in progress');
                    }
        };
        Result.prototype.hide = function(){
            this._container.set("innerHTML", "");
            this._facet.removeClass('current-facet');
        };

        // Add EventTarget attributes to the Result prototype
        Y.augment(Result, Y.EventTarget, null, null, {
            emitFacade : true,
            prefix : "result"
        });

        Lane.on("result:new-content", function() {
            this.fire("new-content");
        });

        if (elt) {
            facets = elt.all('.searchFacet');
            for (i = 0; i < facets.size(); i++) {
                setFacetResult(facets.item(i));
            }
        }
})();

(function() {
    var history,
        searchFacets = Y.one('#searchFacets'),
        Lane = Y.lane,
        SearchFacets = Lane.SearchFacets;
    Lane.SearchHistory = function(){
        if(searchFacets){
            history = new Y.HistoryHash();
            if(history.get('facet')){
                SearchFacets.setActiveFacet(history.get('facet'));
            }
            history.on("facetChange",function(e) {
                SearchFacets.setActiveFacet(e.newVal);
            });
            history.on("facetRemove",function() {
                SearchFacets.setActiveFacet(Lane.search.getSource());
            });
        }
        return history;

    }();

})();
