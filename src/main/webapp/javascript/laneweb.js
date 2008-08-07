var searching = false,
    metaTags = {};

YAHOO.util.Event.addListener(window, 'load', initialize);

window.onerror = handleMessage;

function handleMessage(message, url, line) {
    var parameter = "userAgent=" + navigator.userAgent + "&message=".concat(message).concat("&url=").concat(url).concat("&line=").concat(line);
    YAHOO.util.Connect.asyncRequest('GET', '/././javascriptLogger?' + parameter);
    return true;
}

function handleFailure(o) {
    handleMessage("Status: " + o.status + "statusText: " + o.statusText, o.argument.file, o.argument.line);
}

function log(message) {
    handleMessage(message);
}

function initialize(e) {
    initializeMetaTags(e);
    YAHOO.util.Event.addListener(window, 'unload', finalize);
    YAHOO.util.Event.addListener(document, 'mouseover', handleMouseOver);
    YAHOO.util.Event.addListener(document, 'mouseout', handleMouseOut);
    YAHOO.util.Event.addListener(document, 'click', handleClick);
    initializeSearchForm(e);
    if (YAHOO.env.ua.ie) {
        //TODO figure out why this doesn't work with the activate/deactivate business
        var otherPortals = document.getElementById('otherPortalOptions');
        if (otherPortals) {
            YAHOO.util.Event.addListener(otherPortals, 'mouseover', function(e) {
                this.className = 'hover';
            });
            YAHOO.util.Event.addListener(otherPortals, 'mouseout', function(e) {
                this.className = '';
            });
        }
    }
}

function finalize(e) {
    searching = false;
}

function initializeMetaTags(e) {
    var metaTagElements = document.getElementsByTagName('meta'), i, value, key;
    for (i = 0; i < metaTagElements.length; i++) {
        key = metaTagElements[i].getAttribute('name');
        value = metaTagElements[i].getAttribute('content');
        if (key !== undefined && value !== undefined) {
            window.metaTags[key] = value;
        }
    }
}

function getMetaContent(name) {
    if (name !== undefined) {
        return window.metaTags[name];
    }
}

function handleMouseOver(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.activate) {
        target.activate(e);
    }
}

function handleMouseOut(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.deactivate) {
        target.deactivate(e);
    }
}

function handleChange(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.change) {
        target.change(e);
    }
}

function handleClick(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    while (target !== null) {
        if (target.clicked) {
            target.clicked(e);
        }
        target = target.parentNode;
    }
}

function handleSubmit(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.submit) {
        target.submit(e);
    }
}

function initializeSearchForm(e) {
    var searchForm, searchIndicator, searchSelect, searchSubmit;
    searchForm = document.getElementById('searchForm');
    searchIndicator = document.getElementById('searchIndicator');
    YAHOO.util.Event.addListener(searchForm, 'submit', handleSubmit);
    searchSelect = document.getElementById('searchSelect');
    YAHOO.util.Event.addListener(searchSelect, 'change', handleChange);
    searchSubmit = document.getElementById('searchSubmit');
    searchSelect.homeOption = searchSelect.options[searchSelect.selectedIndex];
    searchSelect.change = function(e) {
        if (this.options[this.selectedIndex].disabled) {
            this.selectedIndex = this.homeOption.index;
        } else {
            this.homeOption = this.options[this.selectedIndex];
        }
        if (taglines) {
            this.homeOption.activate(e);
        }
    };
    searchForm.submit = function(e) {
        if (this.q && this.q.value === '') {
            alert('Please enter one or more search terms.');
            YAHOO.util.Event.stopEvent(e);
        } else {
            searchIndicator.style.visibility = 'visible';
            var formTarget = searchSelect.homeOption.value;
            if (formTarget.match(/^http/)) {
                formTarget = formTarget.replace(/\{search-terms\}/g, this.q.value);
                window.location = formTarget;
                YAHOO.util.Event.preventDefault(e);
            }
        }
    };
    searchSubmit.activate = function(e) {
        this.src = this.src.replace('search_btn.gif', 'search_btn_f2.gif');
    };
    searchSubmit.deactivate = function(e) {
        this.src = this.src.replace('search_btn_f2.gif', 'search_btn.gif');
    };
}

//will include this as a reference to LANE.core.openNewWindow
//but deprecate it in favor of using rel="popup ...." in <a>
function openNewWindow(url, features) {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if (window.focus) {
        w.focus();
    }
    dcsMultiTrack('WT.ti', 'openNewWindow ==> ' + url);
}
