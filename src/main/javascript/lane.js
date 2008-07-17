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
LANE.namespace = function(){
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
LANE.core = LANE.core || function() {
    var E = YAHOO.util.Event, //shorthand
        m = {}; //the meta element name/values
    // initialize on load
    E.addListener(this,'load',function() {
        var d = document,
            meta, //the meta elements
            p, //anchors for finding popup links
            a, //popup anchor
            i;
        meta = d.getElementsByTagName('meta');
        for (i = 0; i < meta.length; i++) {
            m[meta[i].getAttribute('name')] = meta[i].getAttribute('content');
        }
        //calls 'activate' function on target
        E.addListener(d, 'mouseover', function(e) {
            var t = e.srcElement || e.target;
            if (t.activate) {
                t.activate(e);
            }
        });
        //calls 'deactivate' function on target
        E.addListener(d, 'mouseout', function(e) {
            var t = e.srcElement || e.target;
            if (t.deactivate) {
                t.deactivate(e);
            }
        });
        //calls 'clicked' function on target and any parent elements
        E.addListener(d, 'click', function(e){
            var t = e.srcElement || e.target;
            while (t) {
                if (t.clicked) {
                    t.clicked(e);
                }
                t = t.parentNode;
            }
        });
        //handle popup links, currently just open new window.
        //TODO: flesh this out, add parameters, etc.
        E.addListener(d, 'click', function(e) {
            var t = e.srcElement || e.target;
            if (t.rel && t.rel.indexOf('popup') === 0) {
                window.open(t.href);
                E.preventDefault(e);
            }
        });
        //set class to hover for ie
        if (YAHOO.env.ua.ie) {
            if (d.getElementById('otherPortalOptions')) {
                d.getElementById('otherPortalOptions').activate = function() {
                    this.className = 'hover';
                };
                d.getElementById('otherPortalOptions').deactivate = function() {
                    this.className = '';
                };
            }
        }
    });
    return {
        getMetaContent: function(name) {
            return m[name];
        },
        //for backward compatibility, to be removed
        openNewWindow: function(url,features) {
            features = (features) ? features : '';
            window.open(url, 'LaneConnex', features);
        },
        //TODO: urlencode msg, implement onerror to point to this, etc
        log: function(msg, cat, src) {
            YAHOO.log(msg, cat, src);
            if (YAHOO.util.Connect) {
                YAHOO.util.Connect.asyncRequest('HEAD','/././javascriptLogger?' + msg);
            }
        }
    };
}();

//for backward compatibility, to be removed
var openNewWindow = LANE.core.openNewWindow;

LANE.search = LANE.search ||  function() {
    var d = document,
        form, //the form Element
        indicator, //the spinning wheel
        submit, //the submit input
        select, //the select Element
        selected, //the selected option
        E = YAHOO.util.Event, //shorthand for Event
        searching = false, //searching state
        // publicly available functions:
        o = {
            startSearch: function(){
                if (searching) {
                    throw('already searching');
                }
                if (!form.q.value) {
                    throw('nothing to search for');
                }
                searching = true;
                indicator.style.visibility = 'visible';
            },
            stopSearch: function(){
                searching = false;
                indicator.style.visibility = 'hidden';
            },
            isSearching: function(){
                return searching;
            }
        };
    // initialize on load
    E.addListener(this,'load',function() {
        form = d.getElementById('searchForm');
        indicator = d.getElementById('searchIndicator');
        submit = d.getElementById('searchSubmit');
        select = d.getElementById('searchSelect');
        selected = select.options[select.selectedIndex];
        //change submit button image mouseover/mouseout
        submit.activate = function(e){
            this.src = this.src.replace('search_btn.gif', 'search_btn_f2.gif');
        };
        submit.deactivate = function(e){
            this.src = this.src.replace('search_btn_f2.gif', 'search_btn.gif');
            YAHOO.widget.Logger.log('deactivate');
        };

        E.addListener(form, 'submit', function(e) {
            try {
                o.startSearch();
            } catch(ex) {
                alert(ex);
                E.preventDefault(e);
            }
        });
        E.addListener(select, 'change',  function(){
            if (this.options[this.selectedIndex].disabled) {
                this.selectedIndex = selected.index;
            } else {
                selected = this.options[this.selectedIndex];
            }
        });
    });
    return o;
}();

/*

window.onerror = handleMessage;
        

function handleMessage( message, url, line)
{
    var parameter = "userAgent="+navigator.userAgent+"&message=".concat(message).concat("&url=").concat(url).concat("&line=").concat(line);
    if(getMetaContent("LW.debug") == "y")
    {
        if (url != null)
            message = message.concat("\nurl --> ").concat(url);
        if (line != null)
            message = message.concat("\nline --> ").concat(line);
        YAHOO.log(message, "error");
    }
    else
        YAHOO.util.Connect.asyncRequest('GET', '/././javascriptLogger?'+parameter);
        return true;
}




function handleFailure(o){
    handleMessage( "Status: "+o.status+ "statusText: "+o.statusText,  o.argument.file, o.argument.line);
}


function log(message)
{    
    handleMessage(message);
}



 */
