(function(){
    if (YAHOO.env.ua.ie) {
        YAHOO.util.Event.addListener(this, 'load', function(){
            var ie = YAHOO.env.ua.ie, lists, i, d = document, list,
                //the bottom shaded border z-index has to be reduced . . .
                sbb = d.getElementById('sb-bot').style,
                opo = d.getElementById('otherPortalOptions'),
                ldd = d.getElementById('legend-drop-down');
            if (ie <= 6) {
                //set hover class for these ids
                if (opo) {
                    opo.activate = function(){
                        sbb.zIndex = -1;
                        this.className = 'hover';
                    };
                    opo.deactivate = function(){
                        sbb.zIndex = 1;
                        this.className = '';
                    };
                }
                if (ldd) {
                    ldd.activate = function(){
                        this.className = 'hover';
                    };
                    ldd.deactivate = function(){
                        this.className = '';
                    };
                }
                //TODO: use onavailable for this?
                lists = ['shd','ft','gft'];
                for (i = 0; i < lists.length; i++) {
                    list = d.getElementById(lists[i]);
                    list.getElementsByTagName('li')[0].style.border = 'none';
                }
            } else if (ie >= 7 && ie < 8) {
                //ie 7 stil messes up z-index:
                if (opo) {
                    opo.activate = function(){
                        sbb.zIndex = -1;
                    };
                    opo.deactivate = function(){
                        sbb.zIndex = 1;
                    };
                }
            }
        });
    }
})();
