// based on lane-eresources.js; renaming for use across articles, catalog, clinical interfaces
YUI({filter:'debug'}).use('node','yui2-event','yui2-history','yui2-connection',function(Y){
    LANE.namespace('search.facets');
    LANE.search.facets = function(){
        var currentResult;
        return {
            setCurrentResult: function(result){
                currentResult = result;
            },
            getCurrentResult: function(){
                return currentResult;
            }
        };
    }();
    Y.YUI2.util.Event.onDOMReady(function(){
        var elt = Y.one('#searchFacets'), facets, i, j, type, source, container;
        if (elt) {
            container = Y.one('#searchResults');
            facets = elt.all('.searchFacet');
            for (i = 0; i < facets.size(); i++) {
                if (facets.item(i).getAttribute('id').match("Facet$")) {
                    type = facets.item(i).getAttribute('id').substring(0, facets.item(i).getAttribute('id').indexOf('-'));
                    source = facets.item(i).getAttribute('id').substring(0, facets.item(i).getAttribute('id').indexOf('Facet'));
                    if (type) {
                        facets.item(i).setData('result', new Result(type, source, facets.item(i), container));
                        if (facets.item(i).hasClass('current')) {
                            facets.item(i).getData('result').setContent(container.get('children'));
                            LANE.search.facets.setCurrentResult(facets.item(i).getData('result'));
                        }
//                        Y.Node.getDOMNode(facets.item(i)).activate = function(event){
//                            if (!Y.YUI2.util.Dom.hasClass(this,'current')) {
//                                this.style.textDecoration = 'underline';
//                                this.style.cursor = 'pointer';
//                            }
//                        };
//                        Y.Node.getDOMNode(facets.item(i)).deactivate = function(event){
//                            this.style.textDecoration = 'none';
//                            this.style.cursor = 'default';
//                        };
                          facets.item(i).on('click',function(event) {
						  	var result = this.getData('result');
                            if (Y.YUI2.util.History) {
                                // Browser History Manager may not be initialized (Opera unsupported, hyui-history-iframe not present in content)
								//TODO: dynamically add history tracking markup
                                try {
                                    Y.YUI2.util.History.navigate("facet", this.getData('result').source);
                                } catch (e) {
                                    //log somewhere ... no need to break/alert
                                	result.show();
                                }
                            }
                            else{
                                result.show();
                            }
                            LANE.search.setSearchSource(result._source);
                            Y.YUI2.util.Event.preventDefault(event);
                        });
                    }
                }
            }
        }
    });
    function Result(type, source, facet, container){
        this._type = type;
        this._source = source;
        this._facet = facet;
        this._container = container;
        this._url = '/././plain/search/' + this._type + '/' + this._source + '.html?source=' + this._source + '&q=' + LANE.search.getEncodedSearchString();
        this._state = 'initialized';
        this._callback = {
            success: function(o){
                var result, bodyNodes, content, i;
                result = o.argument.result;
                bodyNodes = new Y.Node(o.responseXML.getElementsByTagName('body')[0]).get('children');
                result.setContent(bodyNodes);
                LANE.search.facets.getCurrentResult().hide();
                LANE.search.facets.setCurrentResult(result);
                result.show();
            },
            failure: function(){
                //TODO: use window.location to set page to href of facet
            },
            argument: {
                result: this
            }
        };
        Result.prototype.setContent = function(content){
            this._content = content;
            //TODO: is this the best place for this?
            this._state = 'searched';
        };
        Result.prototype.show = function(){
            var i;
            if (this._state == 'initialized') {
                this.getContent();
                LANE.search.startSearch();
            } else if (this._state == 'searching') {
                alert('search in progress');
            } else {
                LANE.search.facets.getCurrentResult().hide();
                LANE.search.facets.setCurrentResult(this);
                this._facet.addClass('current');
				for(i = 0; i < this._content.size(); i++) {
					this._container.append(this._content.item(i));
				}
                LANE.search.stopSearch();
                LANE.core.getChangeEvent().fire();
                if (this._type == 'specialty'){
                    LANE.search.metasearch.initialize();
                    LANE.search.metasearch.getResultCounts();
                }
            }
        };
        Result.prototype.getContent = function(){
            var request;
            if (this._state == 'initialized') {
                this._state = 'searching';
                request = Y.YUI2.util.Connect.asyncRequest('GET', this._url, this._callback);
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
});
