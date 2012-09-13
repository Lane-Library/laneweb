(function() {
    var elt = Y.one('#searchFacets'),
        searchIndicator = new LANE.SearchIndicator(),
        basePath = Y.lane.Model.get("base-path") || "",
        container = Y.one('#searchResults'),
        facets, i, type, source, Result;
    LANE.search.facets = function(){
        var currentResult = null;
        return {
            setCurrentResult: function(result){
                currentResult = result;
            },
            getCurrentResult: function(){
                return currentResult;
            },
            setActiveFacet: function(facetId){
                var result = Y.one('#' + facetId + 'Facet').getData('result');// result facet to make active
                if (result !== undefined) {
                    if (result._state == 'initialized') {
                        result.show();
                    } else if (result._state == 'searched') {
                        LANE.search.facets.getCurrentResult().hide();
                        LANE.search.facets.setCurrentResult(result);
                        result.show();
                    }
                }
            }
        };
    }();
    Result = function(type, source, facet, container){
        this._type = type;
        this._source = source;
        this._facet = facet;
        this._container = container;
        this._url = basePath + '/plain/search/' + this._type + '/' + this._source + '.html?source=' + this._source + '&q=' + LANE.SearchResult.getEncodedSearchTerms();
        this._state = 'initialized';
        this._callback = {
            on: {
                success: function(id, o, args){
                    var result = args.result;
                    result.setContent(o.responseText);
                    LANE.search.facets.getCurrentResult().hide();
                    LANE.search.facets.setCurrentResult(result);
                    result.show();
                },
                failure: function(){
                    //TODO: use window.location to set page to href of facet
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
            if (this._state == 'initialized') {
                this.getContent();
                searchIndicator.show();
            } else if (this._state == 'searching') {
                alert('search in progress');
            } else {
                LANE.search.facets.getCurrentResult().hide();
                LANE.search.facets.setCurrentResult(this);
                this._facet.addClass('current');
                this._container.set("innerHTML", this._content);
                Result.addShowAbstract(this._container);
                searchIndicator.hide();
            }
        };
        Result.prototype.getContent = function(){
            if (this._state == 'initialized') {
                this._state = 'searching';
                request = Y.io(this._url, this._callback);
            } else 
                if (this._state == 'searched') {
                    this.show();
                } else 
                    if (this._state == 'searching') {
                        alert('search in progress');
                    }
        };
        Result.prototype.hide = function(){
            this._container.set("innerHTML", "");
            this._facet.removeClass('current');
        };
        Result.addShowAbstract = function(container) {
            if (Y.UA.ios && !container.one(".showAbstract")) {
                //add showAbstract links in ios
            	container.all(".hvrTrig").each(function(node) {
                    var label = (node.one(".pmid")) ? 'Abstract' : 'Description';
                    node.one(".hvrTarg").insert("<li class='showAbstract'>[<a href='#'>Show " + label + "</a>]</li>", "before");
                });
            }
        };
        if (container) {
        	Result.addShowAbstract(container);
        }
        if (elt) {
            facets = elt.all('.searchFacet');
            for (i = 0; i < facets.size(); i++) {
                if (facets.item(i).get('id').match("Facet$") && !facets.item(i).hasClass('inactiveFacet')) {
                    type = facets.item(i).get('id').substring(0, facets.item(i).get('id').indexOf('-'));
                    source = facets.item(i).get('id').substring(0, facets.item(i).get('id').indexOf('Facet'));
                    if (type) {
                        facets.item(i).setData('result', new Result(type, source, facets.item(i), container));
                        if (facets.item(i).hasClass('current')) {
                            facets.item(i).getData('result').setContent(container.get('innerHTML'));
                            LANE.search.facets.setCurrentResult(facets.item(i).getData('result'));
                        }
                        Y.on('click',function(event) {
                            var result = this.getData('result');
                            try {
                                LANE.Search.History.addValue("facet", this.getData('result')._source);
                            } catch (e) {
                                //log somewhere ... no need to break/alert
                                result.show();
                            }
                            event.preventDefault();
                        }, facets.item(i));
                    }
                }
            }
        }
})();
