// based on lane-eresources.js; renaming for use across articles, catalog, clinical interfaces
(function(){
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
    YAHOO.util.Event.addListener(this, 'load', function(){
        var elt = document.getElementById('searchFacets'), facets, i, j, type, source, container;
        if (elt) {
            container = document.getElementById('searchResults');
            facets = YAHOO.util.Dom.getElementsByClassName('searchFacet',null,elt);
            for (i = 0; i < facets.length; i++) {
                if (facets[i].id.match("Facet$")) {
                    type = facets[i].id.substring(0, facets[i].id.indexOf('-'));
                    source = facets[i].id.substring(0, facets[i].id.indexOf('Facet'));
                    if (type) {
                        facets[i].result = new Result(type, source, facets[i], container);
                        if (YAHOO.util.Dom.hasClass(facets[i],'current')) {
                            content = [];
                            for (j = 0; j < container.childNodes.length; j++) {
                                content[j] = container.childNodes[j];
                            }
                            facets[i].result.setContent(content);
                            LANE.search.facets.setCurrentResult(facets[i].result);
                        }
                        facets[i].activate = function(event){
                            if (!YAHOO.util.Dom.hasClass(this,'current')) {
//                                this.style.textDecoration = 'underline';
//                                this.style.cursor = 'pointer';
                            }
                        };
                        facets[i].deactivate = function(event){
                            this.style.textDecoration = 'none';
                            this.style.cursor = 'default';
                        };
                        facets[i].clicked = function(event){
                            if (YAHOO.util.History) {
                                // Browser History Manager may not be initialized (Opera unsupported, hyui-history-iframe not present in content)
                                try {
                                    YAHOO.util.History.navigate("facet", this.result._source);
                                } catch (e) {
                                    //log somewhere ... no need to break/alert
                                }
                            }
                            else{
                                this.result.show();
                            }
                            LANE.search.setSearchSource(this.result._source);
                            YAHOO.util.Event.preventDefault(event);
                        };
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
                bodyNodes = o.responseXML.getElementsByTagName('body')[0].childNodes;
                content = [];
                for (i = 0; i < bodyNodes.length; i++) {
                    content[i] = LANE.core.importNode(bodyNodes[i], true);
                }
                result.setContent(content);
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
            if (this._content === undefined) {
                this._content = content;
            } else {
                this._content = this._content.concat(content);
            }
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
                YAHOO.util.Dom.addClass(this._facet,'current');
                for (i = 0; i < this._content.length; i++) {
                    this._container.appendChild(this._content[i]);
                }
                LANE.search.stopSearch();
                LANE.tooltips.initialize();
                LANE.popups.initialize(document);
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
                request = YAHOO.util.Connect.asyncRequest('GET', this._url, this._callback);
            } else 
                if (this._state == 'searched') {
                    this.show();
                } else 
                    if (this._state == 'searching') {
                        alert('search in progress');
                    }
        };
        Result.prototype.hide = function(){
            while (this._container.childNodes.length > 0) {
                this._container.removeChild(this._container.lastChild);
            }
            YAHOO.util.Dom.removeClass(this._facet,'current');
        };
    }
})();
