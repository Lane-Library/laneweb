//TODO: this needs some cleaning up
//check if there is a query
if (LANE.search.getEncodedSearchString()) {
    //check if there is id=queryMapping
    YAHOO.util.Event.onAvailable('queryMapping',function() {
            YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/json?q=' + LANE.search.getEncodedSearchString(), {
            success:function(o) {
                    var queryMapContainer = document.getElementById('queryMapping');
                    LANE.search.querymap = YAHOO.lang.JSON.parse(o.responseText);
                    if (LANE.search.querymap.resourceMap) {
                        LANE.search.querymap.getResultCounts = function() {
                            var url = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString(),
                                i;
                            for (i = 0; i < this.resourceMap.resources.length; i++) {
                                if (!this.resourceMap.resources[i].status) {
                                    url += '&r=' + this.resourceMap.resources[i].id;
                                }
                            }
                            url += '&rd=' + Math.random();
                            YAHOO.util.Connect.asyncRequest('GET',url, {
                                success: function(o){
                                    var results = YAHOO.lang.JSON.parse(o.responseText),
                                        rs = LANE.search.querymap.resourceMap.resources,
                                        i, needMore = false, result;
                                    for (i = 0; i < rs.length; i++) {
                                        if (!rs[i].status) {
                                            result = results.resources[rs[i].id];
                                            if (!result.status) {
                                                needMore = true;
                                            }
                                            rs[i].status = result.status;
                                            if (result.status == 'successful') {
                                                rs[i].anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                                            }
                                            rs[i].anchor.href = result.url;
                                        }
                                    }
                                    if (needMore) {
                                        setTimeout("LANE.search.querymap.getResultCounts()",2000);
                                    }
                                    //queryMapContainer.style.display = 'inline';
                                    LANE.search.popin.fire(queryMapContainer);
                                }
                            });
                        };
                        var anchor;
                        var span;
                        for (var i = 0; i < LANE.search.querymap.resourceMap.resources.length; i++) {
                            LANE.search.querymap.resourceMap.resources[i].status = '';
                            span = document.createElement('span');
                            anchor = document.createElement('a');
                            anchor.title = 'QueryMapping: ' + LANE.search.querymap.resourceMap.resources[i].label;
                            span.appendChild(anchor);
                            anchor.appendChild(document.createTextNode(LANE.search.querymap.resourceMap.resources[i].label));
                            queryMapContainer.appendChild(span);
                            LANE.search.querymap.resourceMap.resources[i].anchor = anchor;
                        }
                        if ( document.getElementById('queryMappingDescriptor') ){
                        	document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(LANE.search.querymap.resourceMap.descriptor));
                        }
                        LANE.search.querymap.getResultCounts();
                    }
            }
            });
    });
    /*
LANE.search.QueryMap = function(){
        var evt = new YAHOO.util.CustomEvent('querymap', this), fires = function(){
            alert('success');
            evt.fire();
        }, firef = function(){
            alert('failure');
            evt.fire();
        };
        YAHOO.util.Event.addListener(window, 'load', function(){
            YAHOO.util.Connect.asyncRequest('GET', '/irt3/apps/querymap/html?q=dvt', {
                success: fires,
                failure: firef
            });
        });
        return {
            addListener: function(obj){
                evt.subscribe(obj);
            }
        };
    };
*/
}


/*
YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/html?q=' + window.searchTerms, window.querymapCallBack);
var querymapCallBack = {
    success: showQueryMapping,
    failure: handleFailure,
    argument: {
        file: "search.js",
        line: "queryMapping"
    }
};
function showQueryMapping(o){
    if (o.responseXML.getElementsByTagName('ul')[0] !== null) {
        var queryMappingResult = o.responseXML.getElementsByTagName('div')[0];
        window.queryMapping = new QueryMapping(importNodes(queryMappingResult, true));
        window.activeResult.show();
        var queryMappingContainer = document.getElementById('queryMapping');
        if (queryMappingContainer && window.spellcheck.suggestion === null) {
            hideFindIt();
            queryMappingContainer.appendChild(window.queryMapping.getContent());
        }
    }
}

function QueryMapping(content){
    this.resourceUrl = "";
    this._counter = 0;
    this._content = content;
    new YAHOO.widget.Tooltip(YAHOO.util.Dom.generateId(), {
        context: 'qmTip',
        width: '170px'
    });
    var lis = this._content.getElementsByTagName('li');
    for (var i = 0; i < lis.length; i++) {
        if (lis[i].id) {
            this.resourceUrl = this.resourceUrl + "&r=" + lis[i].id;
        }
    }
    this._callback = {
        success: this.successfulCallback,
        failure: window.handleFailure,
        argument: {
            queryMapping: this,
            file: "search.js",
            line: "QueryMapping()"
        }
    };
    this.sendQueryMappingRequest();
}

QueryMapping.prototype.successfulCallback = function(o){
    var response = YAHOO.lang.JSON.parse(o.responseText);
    var queryMapping = o.argument.queryMapping;
    window.queryMapping.update(response);
};
QueryMapping.prototype.update = function(response){
    this.resourceUrl = "";
    this._counter++;
    var lis = this._content.getElementsByTagName('li');
    var resourceUrl = "";
    for (var i = 0; i < lis.length; i++) {
        var resourceId = lis[i].id;
        if (response.resources[resourceId] !== null) {
            var anchor = lis[i].getElementsByTagName('a');
            anchor[0].href = response.resources[resourceId].url;
            anchor[0].title = 'QueryMapping: ' + anchor[0].innerHTML;
            var status = response.resources[resourceId].status;
            if (status == 'successful') {
                var span = document.createTextNode(": " + response.resources[resourceId].hits);
                lis[i].appendChild(span);
            } else {
                this.resourceUrl = this.resourceUrl.concat("&r=").concat(resourceId);
            }
        }
    }
    window.activeResult.show();
    if (this._counter < 19 && this.resourceUrl !== '') {
        var sleepingTime = 2000;
        if (this._counter > 15) {//time sleepingtime (2 seconds) * 15 = 30 seconds

            sleepingTime = 10000;
            setTimeout("window.queryMapping.sendQueryMappingRequest()", sleepingTime);
        }
    }
};
QueryMapping.prototype.sendQueryMappingRequest = function(){
    YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/proxy/json?q=' + window.searchTerms + this.resourceUrl + '&rd=' + Math.random(), this._callback);
};
QueryMapping.prototype.getContent = function(){
    return this._content;
};
*/