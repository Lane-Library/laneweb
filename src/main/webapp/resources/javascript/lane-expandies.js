//YUI().use('accordionview','event','node', function(Y){
//    var createExpandies = function() {
//        var i, j, k, items,
//            expanded, anchors, expandy,
//            anchor = document.location.hash,
//            expandies = Y.all('.expandy');
//        if (anchor) {
//            anchor = anchor.substring(1);
//        }
//        for (i = 0; i < expandies.size(); i++) {
//            //create only if no id (id means already created):
//            if (!expandies.item(i).get('id')) {
//                expanded = [];
//                items = expandies.item(i).get('children');//YUD.getChildren(expandies[i]);
//                for (j = 0; j < items.size(); j++) {
//                    if (anchor) {
//                        anchors = items.item(j).all('A');
//                        for (k = 0; k < anchors.size(); k++) {
//                            if (anchors.item(k).get('name') == anchor) {
//                                items.get(j).addClass('expanded');
//                            }
//                        }
//                    }
//                    if (items.item(j).hasClass('expanded')) {
//                        expanded.push(j);
//                    }
//                }
//                expandy = new LANE.expandy.AccordionView(Y.Node.getDOMNode(expandies.item(i)), {
//                    expandItems: expanded
//                });
//                expandy.addListener('panelOpen', function(object) {
//                    new Y.Node(object.panel).addClass('expanded');
//                });
//                expandy.addListener('afterPanelClose', function(object) {
//                    new Y.Node(object.panel).removeClass('expanded');
//                });
//            }
//        }
//    };
//    createExpandies();
//    Y.Global.on('lane:change', createExpandies);
//});
YUI({    //filter:'debug', 
gallery: 'gallery-2010.04.02-17-26'//, 
//debug:true,logInclude: {
//         attribute: true,
//         Selector: true
//            nodeAccordion:true
//     }

//    modules: {
//        'gallery-node-accordion': {
//            fullpath: 'http://yui.yahooapis.com/gallery-2009.10.27-23/build/gallery-node-accordion/gallery-node-accordion-min.js',
//            requires: ['node-base','node-style','plugin','node-event-delegate','classnamemanager'],
//            optional: ['anim'],
//            supersedes: []
//      }
// 
//    }
}).use('gallery-node-accordion','plugin','node','console','anim', function(Y) {

     var expandies = Y.all('.expandy');
     for (var i = 0; i < expandies.size(); i++) {
         var expandy = expandies.item(i);
         expandy.addClass('yui3-accordion');
         var children = expandy.get('children');
         for (var j = 0; j < children.size(); j++) {
             var panel = children.item(j);
             panel.addClass('yui3-accordion-item');
             if (panel.hasClass('expanded')) {
                 panel.addClass(' yui3-accordion-item-active');
             }
             panel.get('children').item(0).addClass('yui3-accordion-item-trigger yui3-accordion-item-hd');
             panel.get('children').item(1).addClass('yui3-accordion-item-bd');
         }
         expandy.plug(Y.Plugin.NodeAccordion, { 
                anim: Y.Easing.backIn
            });
     }
    //Y.one('#myaccordion').plug(Y.Plugin.NodeAccordion, {anim: Y.Easing.backIn});
 
});
