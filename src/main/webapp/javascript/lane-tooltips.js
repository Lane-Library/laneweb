//anonymous initializer for tooltips no public properties or functions
(function() {
    YAHOO.util.Event.addListener(window,'load', function() {
        var tc, //tooltip container Elements
            tt, //tooltips
            te, //elment to recieve tooltip
            e, //id of te element
            w, //width of tooltip
            i, j;
        tc = YAHOO.util.Dom.getElementsByClassName('tooltips');

        for(i = 0; i < tc.length; i++) {
            tt = tc[i].childNodes;
            for(j = 0; j < tt.length; j++) {
                if(tt[j].nodeType == 1) {
                    e = tt[j].id.replace(/Tooltip$/,'');
                    if(e && YAHOO.util.Dom.inDocument(e)) {
                        document.getElementById(e).trackable = true;
                        w = tt[j].style.width || '25%';
                        var bar = new YAHOO.widget.Tooltip(
                                YAHOO.util.Dom.generateId(), 
                                {
                                    context:e,
                                    width:w,
                                    autodismissdelay:30000,
                                    text:tt[j].innerHTML
                                });
                    }
                }
            }
        }
    });
})();