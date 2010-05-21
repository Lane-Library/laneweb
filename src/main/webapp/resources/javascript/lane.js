/**
 * @author ceyates
 */
YUI().add('lane', function(Y) {
    if (typeof LANE == "undefined" || !LANE) {
        /**
         * The LANE global namespace object.  If LANE is already defined, the
         * existing LANE object will not be overwritten so that defined
         * namespaces are preserved.
         * @class LANE
         * @static
         */
        LANE = {};
        
        //create the change event:
        Y.publish('lane:change', {broadcast: 2});
        //tell the server javascript works if we haven't already.
//        if (Y.one('html.lane-js-unknown')) {
//            Y.io('/././enable-js');
//        }
    }
    LANE.namespace = function() {
        var a = arguments, o = null, i, j, d;
        for (i = 0; i < a.length; i = i + 1) {
            d = a[i].split(".");
            o = LANE;
            // LANE is implied, so it is ignored if it is included
            for (j = (d[0] == "LANE") ? 1 : 0; j < d.length; j = j + 1) {
                o[d[j]] = o[d[j]] || {};
                o = o[d[j]];
            }
        }
        return o;
    };
    
    LANE.helper = function() {
        return {
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
                        newNode.appendChild(LANE.helper.importNode(importedNode.childNodes[i], true));
                    }
                }
                return newNode;
            }
        };
    }();

}, '1.11.0-SNAPSHOT',{requires:['node','io']});
