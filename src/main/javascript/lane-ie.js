(function(){
    if (YAHOO.env.ua.ie) {
        YAHOO.util.Event.addListener(this, 'load', function(){
            var ie = YAHOO.env.ua.ie, lists, i, d = document, list;
            if (ie <= 6) {
                //set hover class for these ids
                if (d.getElementById('otherPortalOptions')) {
                    d.getElementById('otherPortalOptions').activate = function(){
                        this.className = 'hover';
                    };
                    d.getElementById('otherPortalOptions').deactivate = function(){
                        this.className = '';
                    };
                }
                if (d.getElementById('legend-drop-down')) {
                    d.getElementById('legend-drop-down').activate = function(){
                        this.className = 'hover';
                    };
                    d.getElementById('legend-drop-down').deactivate = function(){
                        this.className = '';
                    };
                }
                //TODO: use onavailable for this?
                lists = ['shd','ft','gft'];
                for (i = 0; i < lists.length; i++) {
                    list = d.getElementById(lists[i]);
                    list.getElementsByTagName('li')[0].style.border = 'none';
                }
            }
        });
    }
})();
