 //check if there is a query
if (LANE.search.getEncodedSearchString()) {
    YAHOO.util.Event.onAvailable('queryMapping',function() {
            YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/json?q=' + LANE.search.getEncodedSearchString(), {
            success:function(o) {
                    var queryMap = YAHOO.lang.JSON.parse(o.responseText),
                        queryMapContainer = document.getElementById('queryMapping');
                    alert(queryMap);
                    queryMapContainer.style.display = 'inline';
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