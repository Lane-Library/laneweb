(function() {
    var elt = Y.one('#searchFacets'),
        searchIndicator = new Y.lane.SearchIndicator(),
        facets, i, type, source, container;
    Y.namespace('lane.search.facets');
    Y.lane.search.facets = function(){
        var currentResult;
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
                        Y.lane.search.facets.getCurrentResult().hide();
                        Y.lane.search.facets.setCurrentResult(result);
                        result.show();
                    }
                }
            }
        };
    }();
    if (elt) {
        container = Y.one('#searchResults');
        facets = elt.all('.searchFacet');
        for (i = 0; i < facets.size(); i++) {
            if (facets.item(i).get('id').match("Facet$") && !facets.item(i).hasClass('inactiveFacet')) {
                type = facets.item(i).get('id').substring(0, facets.item(i).get('id').indexOf('-'));
                source = facets.item(i).get('id').substring(0, facets.item(i).get('id').indexOf('Facet'));
                if (type) {
                    facets.item(i).setData('result', new Result(type, source, facets.item(i), container));
                    if (facets.item(i).hasClass('current')) {
                        facets.item(i).getData('result').setContent(container.get('children'));
                        Y.lane.search.facets.setCurrentResult(facets.item(i).getData('result'));
                    }
                    if (!(Y.UA.ie && Y.UA.ie < 8)) { // TODO: fix IE < 8 rendering of tooltips after import
                        Y.on('click',function(event) {
                            var result = this.getData('result');
                            try {
                                Y.lane.Search.History.addValue("facet", this.getData('result')._source);
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
    }
    function Result(type, source, facet, container){
        this._type = type;
        this._source = source;
        this._facet = facet;
        this._container = container;
        this._url = '/././plain/search/' + this._type + '/' + this._source + '.html?source=' + this._source + '&q=' + Y.lane.SearchResult.getEncodedSearchTerms();
        this._state = 'initialized';
        this._callback = {
            on: {
                success: function(id, o, arguments){
                    var result, content;
                    result = arguments.result;
                    content = Y.Node.create(o.responseText);
//                  bodyNode = o.responseXML.getElementsByTagName('body')[0];
//                  content = new Y.Node(document.importNode(bodyNode, true));
                    result.setContent(content);
//                    bodyNodes = o.responseXML.getElementsByTagName('body')[0];
//                    content = new Y.Node(document.importNode(bodyNodes, true));
//                    result.setContent(content.get('children'));
                    Y.lane.search.facets.getCurrentResult().hide();
                    Y.lane.search.facets.setCurrentResult(result);
                    result.show();
                    Y.Global.fire('lane:change');
                },
                failure: function(){
                    //TODO: use window.location to set page to href of facet
                }
            },
            arguments: {
                result: this
            }
        };
        Result.prototype.setContent = function(content){
            this._content = content;
            //TODO: is this the best place for this?
            this._state = 'searched';
        };
        Result.prototype.show = function(){
            var i, children;
            if (this._state == 'initialized') {
                this.getContent();
                searchIndicator.show();
            } else if (this._state == 'searching') {
                alert('search in progress');
            } else {
                Y.lane.search.facets.getCurrentResult().hide();
                Y.lane.search.facets.setCurrentResult(this);
                this._facet.addClass('current');
                children = this._content.get("children");
                for(i = 0; i < children.size(); i++) {
                    this._container.append(children.item(i).cloneNode(true));
                }
                searchIndicator.hide();
                Y.fire('lane:change');;
            }
        };
        Result.prototype.getContent = function(){
            var request;
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
            while (this._container.get('childNodes').size() > 0) {
                this._container.get('lastChild').remove();
            }
            this._facet.removeClass('current');
        };
    }
})();
