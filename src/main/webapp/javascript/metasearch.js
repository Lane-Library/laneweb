var searchTerms,
    searchIndicator,
    searchMode,
    searchStatus,
    searchTemplate,
    searchUrl,
    counter = 0,
    spellCheckCallBack = {
        success: showSpellCheck,
        failure: handleFailure,
        argument: {
            file: "metasearch.js",
            line: "spellCheckCallBack"
        }
    },
    metasearchCallback = {
        success: showMetasearchResults,
        failure: window.handleFailure,
        argument: {
            file: "metasearch.js",
            line: "metasearchCallBack"
        }
    };
    
YAHOO.util.Event.addListener(window, 'load', initializeMetasearch);

function initializeMetasearch(e) {
    window.searchTerms = escape(getMetaContent("LW.searchTerms"));
    window.searchMode = getMetaContent("LW.searchMode");
    window.searchTemplate = (getMetaContent("LW.searchTemplate")) ? getMetaContent("LW.searchTemplate") : location.pathname.replace('/./.', '');
    if (window.searchTerms && window.searchTerms != 'undefined') {
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/filtered/proxy/json?q=' + window.searchTerms + '&source=' + window.searchTemplate, window.metasearchCallback);
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/spellcheck/json?q=' + window.searchTerms, window.spellCheckCallBack);
        if (YAHOO.util.Dom.inDocument('searchIndicator')) {
            window.searchIndicator = new SearchIndicator('searchIndicator', 'Search Starting ... ');
        }
        YAHOO.util.Event.addListener('searchIndicator', 'click', haltMetasearch);
        YAHOO.util.Event.addListener('toggleZeros', 'click', toggleZeros);
    }
}

function showMetasearchResults(o) {
    var metasearchElements, sleepingTime, z, metasearchResult, resourceId;
    searchResponse = YAHOO.lang.JSON.parse(o.responseText);
    window.searchStatus = (window.searchStatus == 'successful') ? window.searchStatus : searchResponse.status;
    metasearchElements = YAHOO.util.Dom.getElementsByClassName('metasearch');
    if (metasearchElements.length && window.searchStatus != 'successful') {
        window.counter++;
        sleepingTime = 2000; //2 seconds
        if (window.counter > 15) {//time sleepingtime (2 seconds) * 15 = 30 seconds

            sleepingTime = 10000;// 10 seconds
        }
        setTimeout("YAHOO.util.Connect.asyncRequest('GET', '" + '/././apps/search/filtered/proxy/json?q=' + window.searchTerms + '&source=' + window.searchTemplate + '&rd=' + Math.random() + "', window.metasearchCallback);", sleepingTime);
    }
    for (z = 0; z < metasearchElements.length; z++) {
        if (metasearchElements[z].className != 'complete') {
            resourceId = metasearchElements[z].getAttribute('id');
            searchResource = searchResponse.resources[resourceId];
            if (searchResource && searchResource.status && searchResource.status != 'running') {
                metasearchResult = new MetasearchResult(metasearchElements[z], searchResource, resourceId);
            }
        }
    }
    if (window.searchIndicator) {
        window.searchIndicator.setProgress(window.searchStatus, YAHOO.util.Dom.getElementsByClassName('metasearch').length, YAHOO.util.Dom.getElementsByClassName('complete').length);
    }
}

function MetasearchResult(metasearchElement, searchResource, id) {
    if (metasearchElement === null) {
        window.log('MetasearchResult():  metasearchElement should not be null');
    }
    if (searchResource === null) {
        window.log('MetasearchResult():  searchResource should not be null');
    }
    this.id = id;
    this.name = (metasearchElement.innerHTML) ? metasearchElement.innerHTML : '';
    this.status = (searchResource.status) ? searchResource.status : 0;
    this.hits = searchResource.hits;
    this.href = searchResource.url || metasearchElement.href;
    this.setContent(metasearchElement);
}

MetasearchResult.prototype.setContent = function(metasearchElement) {
    var resultSpan, parentHeading, parent;
    if (metasearchElement === null) {
        window.log('MetasearchResult.setContent():  metasearchElement should not be null');
    }
    switch (window.searchMode) {
        case "original":
            if (this.status && this.status != 'running' && this.href) {
                metasearchElement.setAttribute('href', this.href);
                // fix for IE7 (@ in text of element will cause element text to be replaced by href value
                // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                if (YAHOO.env.ua.ie) {
                    metasearchElement.innerHTML = this.name;
                }
                metasearchElement.setAttribute('target', '_blank');
                metasearchElement.className = 'complete';
                resultSpan = document.createElement('span');
                resultSpan.innerHTML = ': ' + this.hits;
                metasearchElement.parentNode.appendChild(resultSpan);
                if (parseInt(this.hits, 10) > 0 || this.status != 'successful') {
                    parentHeading = YAHOO.util.Dom.getAncestorByTagName(this.id, 'div').getElementsByTagName('h3')[0];
                    parentHeading.style.display = 'block';
                    parent = YAHOO.util.Dom.getAncestorByTagName(this.id, 'li');
                    parent.style.display = 'block';
                } else 
                    if (parseInt(this.hits, 10) === 0) {
                        parent = YAHOO.util.Dom.getAncestorByTagName(this.id, 'li');
                        parent.className = 'zero';
                    }
            }
            break;
        default: // merged 
            if (this.status == 'successful' && this.href) {
                metasearchElement.setAttribute('href', this.href);
                // fix for IE7 (@ in text of element will cause element text to be replaced by href value
                // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                if (YAHOO.env.ua.ie) {
                    metasearchElement.innerHTML = this.name;
                }
                metasearchElement.setAttribute('target', '_blank');
                resultSpan = document.createElement('span');
                resultSpan.innerHTML = ': ' + this.hits;
                metasearchElement.parentNode.appendChild(resultSpan);
                metasearchElement.className = 'complete';
            } else 
                if (this.status == 'failed' || this.status == 'canceled') {
                    metasearchElement.className = 'complete';
                }
            break;
    }
};

function haltMetasearch() {
    window.searchStatus = 'successful';
}

function toggleZeros(e) {
    var toggleEl = document.getElementById('toggleZeros'), zeroResources = YAHOO.util.Dom.getElementsByClassName('zero'), display, i, y, searchCats;
    switch (toggleEl.innerHTML) {
        case "Show Details":
            display = 'block';
            toggleEl.innerHTML = 'Hide Details';
            break;
        case "Hide Details":
            display = 'none';
            toggleEl.innerHTML = 'Show Details';
            break;
    }
    for (i = 0; i < zeroResources.length; i++) {
        YAHOO.util.Dom.setStyle(zeroResources[i], 'display', display);
    }
    // toggle h3 header as well if all children in searchCat are zeros
    searchCats = YAHOO.util.Dom.getElementsByClassName('searchCategory');
    for (y = 0; y < searchCats.length; y++) {
        if (YAHOO.util.Dom.getElementsByClassName('zero', '', searchCats[y]).length == searchCats[y].getElementsByTagName('li').length) {
            YAHOO.util.Dom.setStyle(YAHOO.util.Dom.getFirstChild(searchCats[y]), 'display', display);
        }
    }
}

function SearchIndicator(elementId, message) {
    this.elementId = (YAHOO.util.Dom.inDocument(elementId)) ? elementId : null;
    if (null == this.elementId) {
        window.log('SearchIndicator():  SearchIndicator missing elementId');
    }
    this.message = message;
    this.show();
}

SearchIndicator.prototype.hide = function() {
    YAHOO.util.Dom.setStyle(this.elementId, 'visibility', 'hidden');
};

SearchIndicator.prototype.show = function() {
    YAHOO.util.Dom.setStyle(this.elementId, 'visibility', 'visible');
};

SearchIndicator.prototype.setProgress = function(status, pendingResources, completedResources) {
    this.show();
    this.setMessage(completedResources + ' of ' + (pendingResources + completedResources) + ' sources searched');
    if (status == 'successful' || pendingResources === 0) {
        this.hide();
        YAHOO.util.Dom.setStyle('resultsMessage', 'display', 'inline');
        YAHOO.util.Dom.setStyle('metasearchControls', 'display', 'inline');
    }
};

SearchIndicator.prototype.setMessage = function(message) {
    this.message = message;
    document.getElementById(this.elementId).title = this.message;
};

function showSpellCheck(o) {
    var spellCheckResponse = YAHOO.lang.JSON.parse(o.responseText), spellCheckContainer, spellCheckLink;
    if (spellCheckResponse.suggestion) {
        spellCheckContainer = document.getElementById("spellCheck");
        spellCheckLink = document.getElementById("spellCheckLink");
        if (spellCheckContainer && spellCheckLink) {
            spellCheckLink.href = location.href.replace(location.href.match(/\Wq=([^&]*)/)[2], spellCheckResponse.suggestion);
            spellCheckLink.innerHTML = spellCheckResponse.suggestion;
            spellCheckContainer.style.display = 'inline';
            spellCheckContainer.style.visibility = 'visible';
        }
    }
}
