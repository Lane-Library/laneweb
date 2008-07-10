/**
 * @author ceyates
 */
if (typeof LANE == "undefined" || !LANE) {
    /**
     * The LANE global namespace object.  If LANE is already defined, the
     * existing LANE object will not be overwritten so that defined
     * namespaces are preserved.
     * @class LANE
     * @static
     */
    var LANE = {};
}

LANE.namespace = function() {
    var a = arguments, o = null, i, j, d;
    for (i = 0; i < a.length; i = i + 1) {
        d = a[i].split(".");
        o = LANE;
        // LANE is implied, so it is ignored if it is included
        for (j = (d[0] == "LANE") ? 1 : 0; j < d.length; j = j + 1) {
            o[d[j]] = o[d[j]] ||
            {};
            o = o[d[j]];
        }
    }
    return o;
};

LANE.core = LANE.core || {
    meta: {},
    initialize: function(){
        var m, i, y = YAHOO.util.Event, c = LANE.core, d = document;
        m = d.getElementsByTagName('meta');
        for (i = 0; i < m.length; i++) {
            c.meta[m[i].getAttribute('name')] = m[i].getAttribute('content');
        }
        y.addListener(document, 'mouseover', c.handleMouseOver);
        y.addListener(document, 'mouseout', c.handleMouseOut);
        y.addListener(document, 'click', c.handleClick);
    },
    //calls 'activate' function on target
    handleMouseOver: function(e){
        var t = e.srcElement || e.target;
        if (t.activate) {
            t.activate(e);
        }
    },
    //calls 'deactivate' function on target
    handleMouseOut: function(e){
        var t = e.srcElement || e.target;
        if (t.deactivate) {
            t.deactivate(e);
        }
    },
    //calls 'clicked' function on target and any parent elements
    handleClick: function(e){
        var t = e.srcElement || e.target;
        while (t) {
            if (t.clicked) {
                t.clicked(e);
            }
            t = t.parentNode;
        }
    }
};


/*
var searching = false;
var metaTags = {};

YAHOO.util.Event.addListener(window, 'load', initialize);
window.onerror = handleMessage;

function handleMessage(message, url, line) {
    var parameter = "userAgent=" + navigator.userAgent + "&message=".concat(message).concat("&url=").concat(url).concat("&line=").concat(line);
    if (getMetaContent("LW.debug") == "y") {
        if (url !== null) {
            message = message.concat("\nurl --> ").concat(url);
        }
        if (line !== null) {
            message = message.concat("\nline --> ").concat(line);
        }
        YAHOO.log(message, "error");
    }
    else {
        YAHOO.util.Connect.asyncRequest('GET', '/././javascriptLogger?' + parameter);
    }
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
    initializeLogger();
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

function initializeLogger() {
    if (getMetaContent("LW.debug") == "y") {
        document.body.className = "yui-skin-sam";
        var myLogReader = new YAHOO.widget.LogReader();
        var logMessage = "context ==> " + context;
        logMessage = logMessage.concat("\nquery_string ==> " + query_string);
        logMessage = logMessage.concat("\nrequest_uri ==> " + request_uri);
        logMessage = logMessage.concat("\nhref ==> " + href);
        logMessage = logMessage.concat("\nticket ==> " + ticket);
        logMessage = logMessage.concat("\nsunetid ==> " + sunetid);
        logMessage = logMessage.concat("\nproxy_links ==> " + proxy_links);
        logMessage = logMessage.concat("\naffiliation ==> " + affiliation);
        logMessage = logMessage.concat("\nsearch_form_select ==> " + search_form_select);
        logMessage = logMessage.concat("\nsource ==> " + source);
        logMessage = logMessage.concat("\nsearchTerms ==> " + searchTerms + "\n");
        YAHOO.log(logMessage, "info");
    }
}

function finalize(e) {
    searching = false;
}

function initializeMetaTags(e) {
}

function getMetaContent(name) {
}

function handleChange(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.change) {
        target.change(e);
    }
}

function handleSubmit(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.submit) {
        target.submit(e);
    }
}

function initializeSearchForm(e) {
    var searchForm = document.getElementById('searchForm');
    var searchIndicator = document.getElementById('searchIndicator');
    YAHOO.util.Event.addListener(searchForm, 'submit', handleSubmit);
    var taglines = document.getElementById('taglines');
    var allTagline = document.getElementById('allTagline');
    var searchSelect = document.getElementById('searchSelect');
    YAHOO.util.Event.addListener(searchSelect, 'change', handleChange);
    var displayTagline = document.getElementById('displayTagline');
    var searchSubmit = document.getElementById('searchSubmit');
    searchSelect.homeOption = searchSelect.options[searchSelect.selectedIndex];
    searchSelect.change = function(e) {
        if (this.options[this.selectedIndex].disabled) {
            this.selectedIndex = this.homeOption.index;
        }
        else {
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
        }
        else {
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

function openNewWindow(url, features) {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if (window.focus) {
        w.focus();
    }
    dcsMultiTrack('WT.ti', 'openNewWindow ==> ' + url);
}

function email(obfuscatedEmail) {
    document.location = obfuscatedEmail.replace(/\|/g, '');
    return false;
}

*/