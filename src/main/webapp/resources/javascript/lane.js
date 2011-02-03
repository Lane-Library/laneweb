/**
 * @author ceyates
 */
YUI({fetchCSS:false}).use("*", function(Y) {
	
	//keep a global reference of this YUI object
	window.Y = Y;
	
	//create the lane namespace
	Y.namespace("lane");
	
    if (typeof LANE == "undefined" || !LANE) {
        /**
         * The LANE global namespace object.  If LANE is already defined, the
         * existing LANE object will not be overwritten so that defined
         * namespaces are preserved.
         * @class LANE
         * @static
         */
        LANE = {};
        LANE.Y = Y;
        Y.publish('lane:change', {broadcast: 2});
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
    
    // add document.importNode functionality if native not present (IE)
    if (!document.ELEMENT_NODE) {
        document.ELEMENT_NODE = 1;
        document.ATTRIBUTE_NODE = 2;
        document.TEXT_NODE = 3;
        document.CDATA_SECTION_NODE = 4;
        document.ENTITY_REFERENCE_NODE = 5;
        document.ENTITY_NODE = 6;
        document.PROCESSING_INSTRUCTION_NODE = 7;
        document.COMMENT_NODE = 8;
        document.DOCUMENT_NODE = 9;
        document.DOCUMENT_TYPE_NODE = 10;
        document.DOCUMENT_FRAGMENT_NODE = 11;
        document.NOTATION_NODE = 12;
    }
    if (!document.importNode) {
        document.importNode = function(node, allChildren) {
            var i, il, newNode;
            /* find the node type to import */
            switch (node.nodeType) {
                case document.ELEMENT_NODE:
                    /* create a new element */
                    newNode = document.createElement(node.nodeName);
                    /* does the node have any attributes to add? */
                    if (node.attributes && node.attributes.length > 0)
                        /* add all of the attributes */
                        for (i = 0, il = node.attributes.length; i < il;) {
                            newNode.setAttribute(node.attributes[i].nodeName, node.getAttribute(node.attributes[i++].nodeName));
                        }
                    /* are we going after children too, and does the node have any? */
                    if (allChildren && node.childNodes && node.childNodes.length > 0)
                        /* recursively get all of the child nodes */
                        for (i = 0, il = node.childNodes.length; i < il;)
                            newNode.appendChild(document.importNode(node.childNodes[i++], allChildren));
                    return newNode;
                    break;
                case document.TEXT_NODE:
                case document.CDATA_SECTION_NODE:
                case document.COMMENT_NODE:
                    return document.createTextNode(node.nodeValue);
                    break;
            }
        };
    }
    
    // add .indexOf functionality to Array if native not present (IE)
    if(!Array.indexOf){
        Array.prototype.indexOf = function(obj){
            for(var i=0; i<this.length; i++){
                if(this[i]==obj){
                    return i;
                }
            }
            return -1;
        }
    }
});
