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
            while (t) {
                if (t.activate) {
                    t.activate(e);
                }
                t = t.parentNode;
            }
        });
        //calls 'deactivate' function on target
        E.addListener(d, 'mouseout', function(e) {
            var t = e.srcElement || e.target;
            while (t) {
            if (t.deactivate) {
                t.deactivate(e);
            }
                t = t.parentNode;
            }
        });
//        E.addListener(d, 'submit',function(e) {
//            var t = e.srcElement || e.target;
//            if (t.submit !== undefined) {
//                t.submit(e);
//            }
//            if (LANE.track) {
//                LANE.track.trackEvent(e);
//            }
//        });
        //calls 'clicked' function on target and any parent elements
        E.addListener(d, 'click', function(e){
            var t = e.srcElement || e.target, parent, href,
                f;
            while (t) {
                if (t.clicked) {
                    t.clicked(e);
                }
                t = t.parentNode;
            }
            if (LANE.track) {
                LANE.track.trackEvent(e);
            //put in a delay for safari to make the tracking request:
            if (YAHOO.env.ua.webkit && LANE.track.isTrackable(e)) {
                    t = e.target;
                    parent = t;
                    while (parent) {
                        if (parent.clicked !== undefined) {
                            return;
                        }
                        parent = parent.parentNode;
                    }
                    while (t) {
                        if (t.href && (!t.rel && !t.target)) {
                            f = function() {
                                window.location = t.href;
                            };
                            YAHOO.util.Event.preventDefault(e);
                            setTimeout(f, 200);
                            break;
                        }
                        t = t.parentNode;
                    }
            }
            }
        });
    });
    return {
        getMetaContent: function(name) {
            return m[name] === undefined ? undefined : m[name];
        },
        //TODO: urlencode msg, implement onerror to point to this, etc
        log: function(msg, cat, src) {
            YAHOO.log(msg, cat, src);
            if (YAHOO.util.Connect) {
                YAHOO.util.Connect.asyncRequest('HEAD','/././javascriptLogger?' + msg);
            }
        },
        // document.importNode not supported in IE
        importNode: function(importedNode, deep) {
		    var newNode, i, attr;
		    if (importedNode.nodeType == 1) { // Node.ELEMENT_NODE
		        newNode = document.createElement(importedNode.nodeName);
		        for (i = 0; i < importedNode.attributes.length; i++) {
		            attr = importedNode.attributes[i];
		            if (attr.nodeValue !== undefined && attr.nodeValue !== '') {
		                newNode.setAttribute(attr.name, attr.nodeValue);
		                if (attr.name == 'class') {
		                    newNode.className = attr.nodeValue;
		                }
		            }
		        }
		    } else 
		        if (importedNode.nodeType == 3) { // Node.TEXT_NODE
		            newNode = document.createTextNode(importedNode.nodeValue);
		        }
		    if (deep && importedNode.hasChildNodes()) {
		        for (i = 0; i < importedNode.childNodes.length; i++) {
		            newNode.appendChild(LANE.core.importNode(importedNode.childNodes[i], true));
		        }
		    }
		    return newNode;
        }
    };
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
