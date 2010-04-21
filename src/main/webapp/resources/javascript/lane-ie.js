YUI().use('yui2-event', 'node', function(Y){
    if (Y.UA.ie < 7) {
       //instead of :first-child selector remove background image:
       Y.YUI2.util.Event.onAvailable('stanfordMedicineBrand', function() {
           new Y.Node(this).setStyle('backgroundImage','none');
        });
        //set up hover class on somNav menu:
        Y.YUI2.util.Event.onContentReady('somNav', function() {
            var i, uls = new Y.Node(this).getElementsByTagName('UL');
            for (i = 0; i < uls.size(); i++) {
                Y.Node.getDOMNode(uls.item(i).get('parentNode')).activate = function() {
                    new Y.Node(this).addClass('hover');
                };
                Y.Node.getDOMNode(uls.item(i).get('parentNode')).deactivate = function() {
                    new Y.Node(this).removeClass('hover');
                };
            }
        });
        //instead of :first-child selector remove left border on .nav2 menus:
        Y.YUI2.util.Event.onDOMReady(function() {
            var i, uls = Y.all('.nav2, #libraryContact');
            for (i = 0; i < uls.size(); i++) {
                uls.item(i).getElementsByTagName('LI').item(0).setStyle('border','none');
            }
        });
    }
});
