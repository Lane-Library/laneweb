(function(){
    if (YAHOO.env.ua.ie && YAHOO.env.ua.ie == 6) {
       //instead of :first-child selector remove background image:
       YAHOO.util.Event.onAvailable('stanfordMedicineBrand', function() {
            this.style.backgroundImage = 'none';
        });
        //set up hover class on somNav menu:
        YAHOO.util.Event.onContentReady('somNav', function() {
            var i, uls = this.getElementsByTagName('UL');
            for (i = 0; i < uls.length; i++) {
                uls[i].parentNode.activate = function() {
                    YAHOO.util.Dom.addClass(this, 'hover');
                };
                uls[i].parentNode.deactivate = function() {
                     YAHOO.util.Dom.removeClass(this, 'hover');
                };
            }
        });
        //instead of :first-child selector remove left border on .nav2 menus:
        YAHOO.util.Event.onDOMReady(function() {
            var i, uls = YAHOO.util.Dom.getElementsByClassName('nav2', 'UL');
            for (i = 0; i < uls.length; i++) {
                uls[i].getElementsByTagName('LI')[0].style.border = 'none';
            }
        });
    }
})();
        /*
YAHOO.util.Event.addListener(this, 'load', function(){
            var ie = YAHOO.env.ua.ie,
                lists, i,
                d = document,
                list,
                //the bottom shaded border z-index has to be reduced . . .
                shadedBorderBottom = d.getElementById('sb-bot'),
                otherPortalOptions = d.getElementById('otherPortalOptions'),
                legendDropDown = d.getElementById('legend-drop-down'),
                superHeader = d.getElementById('shd'),
                searchSelect = d.getElementById('searchSelect'),
                searchSubmit = d.getElementById('searchSubmit');
                if (ie <= 6) {
                    //set hover class for these ids
                    if (otherPortalOptions && shadedBorderBottom) {
                        otherPortalOptions.activate = function(){
                            shadedBorderBottom.style.zIndex = -1;
                            this.className = 'hover';
                        };
                        otherPortalOptions.deactivate = function(){
                            shadedBorderBottom.style.zIndex = 1;
                            this.className = '';
                        };
                    }
                    if (legendDropDown) {
                        legendDropDown.activate = function(){
                            this.className = 'hover';
                        };
                        legendDropDown.deactivate = function(){
                            this.className = '';
                        };
                    }
                    if (superHeader) {
                        var uls = superHeader.getElementsByTagName('UL');
                        for (i = 0; i < uls.length; i++) {
                            //hide search select for 1st superheader drop down
                            if (i == 0 ) {
                                uls[i].parentNode.activate = function() {
                                    searchSelect.style.visibility = 'hidden';
                                    this.className = 'hover';
                                };
                                uls[i].parentNode.deactivate = function() {
                                    this.className = '';
                                    searchSelect.style.visibility = 'visible';
                                };
                            } else {
                                uls[i].parentNode.activate = function() {
                                    this.className = 'hover';
                                };
                                uls[i].parentNode.deactivate = function() {
                                    this.className = '';
                                };
                            }
                        }
                    }
                    if (searchSubmit) {
                        searchSubmit.activate = function() {
                            this.className = 'hover';
                        };
                        searchSubmit.deactivate = function() {
                            this.className = '';
                        };
                    }
                    lists = ['ft', 'gft'];
                    for (i = 0; i < lists.length; i++) {
                        list = d.getElementById(lists[i]);
                        if (list) {
                            list.getElementsByTagName('li')[0].style.border = 'none';
                        }
                    }
                    list = d.getElementById('shd');
                    if (list) {
                        list.getElementsByTagName('li')[0].style.backgroundImage = 'none';
                    }
                }
                else 
                    if (ie >= 7 && ie < 8) {
                        //ie 7 stil messes up z-index:
                        if (otherPortalOptions && shadedBorderBottom) {
                            if (otherPortalOptions) {
                                otherPortalOptions.activate = function(){
                                    shadedBorderBottom.style.zIndex = -1;
                                };
                                otherPortalOptions.deactivate = function(){
                                    shadedBorderBottom.style.zIndex = 1;
                                };
                            }
                        }
                    }
            });
*/
