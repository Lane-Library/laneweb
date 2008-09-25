
(function() {
    LANE.namespace('search.eresources');

LANE.search.eresources = function () {
    var currentResult;
    return {
        setCurrentResult: function(result) {
            currentResult = result;
        },
        getCurrentResult: function() {
            return currentResult;
        }
    };
}();

    YAHOO.util.Event.addListener(this,'load',function() {
        var elt = document.getElementById('eLibraryTabs'),
            tabs, i, j, type, container;
        if (elt) {
            container = document.getElementById('eLibrarySearchResults');
            tabs = elt.getElementsByTagName('li');
            for (i = 0; i < tabs.length; i++) {
                if (tabs[i].className == 'eLibraryTab' || tabs[i].className == 'eLibraryTabActive') {
                    type = tabs[i].id.substring(0, tabs[i].id.indexOf('Tab'));
                    if (type) {
                        tabs[i].result = new Result(type, tabs[i], container);
                        if (tabs[i].className == 'eLibraryTabActive') {
                            content = [];
                            for (j = 0; j < container.childNodes.length; j++) {
                                content[j] = container.childNodes[j];
                            }
                            tabs[i].result.setContent(content);
                            LANE.search.eresources.setCurrentResult(tabs[i].result);
                        }
                        tabs[i].activate = function(event) {
                            if (this.className != 'eLibraryTabActive') {
                                this.style.textDecoration = 'underline';
                                this.style.cursor = 'pointer';
                            }
                        };
                        tabs[i].deactivate = function(event) {
                            this.style.textDecoration = 'none';
                            this.style.cursor = 'default';
                        };
                        tabs[i].clicked = function(event) {
                            this.result.show();
							if (YAHOO.util.History) {
								// Browser History Manager may not be initiliazed (Opera unsupported, hyui-history-iframe not present in content)
								try {
								    YAHOO.util.History.navigate("aTab", this.result._type);
								} catch (e) {
								    //log somewhere ... no need to break/alert
								}
							}
                            YAHOO.util.Event.stopEvent(event);
                        };
                    }
                }
            }
        }
    });
    function Result(type, tab, container) {
        this._type = type;
        this._tab = tab;
        this._container = container;
        this._url = '/././plain/search/' + this._type + '.html?source=' + this._type + '&q='
            + LANE.search.getEncodedSearchString();
        this._state = 'initialized';
        this._callback = {
            success: function(o) {
                var result, bodyNodes, content, i;
                result = o.argument.result;
                bodyNodes = o.responseXML.getElementsByTagName('body')[0].childNodes;
                content = [];
                for (i = 0; i < bodyNodes.length; i++) {
                    content[i] = LANE.core.importNode(bodyNodes[i], true);
                }
                result.setContent(content);
                LANE.search.eresources.getCurrentResult().hide();
                LANE.search.eresources.setCurrentResult(result);
                result.show();
            },
            failure: function() { alert('failure'); },
            argument: {result:this}
    };
   Result.prototype.setContent = function(content) {
    var i;
    if (this._content === undefined) {
        this._content = content;
    } else {
        this._content = this._content.concat(content);
    }
    this._count = 0;
    for (i = 0; i < this._content.length; i++) {
        if (this._content[i].getElementsByTagName) {
            this._count = this._content[i].getElementsByTagName('dt').length;
            if (this._count > 0) {
                break;
            }
        }
    }
    this.setTabCount(this._count);
    //TODO: is this the best place for this?
    this._state = 'searched';
};
//TODO: does this really need a separate function?
Result.prototype.setTabCount = function(count) {
    var hitCount = this._tab.getElementsByTagName('span')[0];
    //TODO: textContent probably not implemented in Safari
    hitCount.textContent = count;
};
Result.prototype.show = function() {
    var i;
    if (this._state == 'initialized') {
        this.getContent();
    } else if (this._state == 'searching') {
            alert('search in progress');
        } else {
                LANE.search.eresources.getCurrentResult().hide();
                LANE.search.eresources.setCurrentResult(this);
            this._tab.className = 'eLibraryTabActive';
            for (i = 0; i < this._content.length; i++) {
                this._container.appendChild(this._content[i]);
            }
        }
};
Result.prototype.getContent = function() {
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
Result.prototype.hide = function() {
    while (this._container.childNodes.length > 0) {
        this._container.removeChild(this._container.lastChild);
    }
    this._tab.className = 'eLibraryTab';
};
}
})();
