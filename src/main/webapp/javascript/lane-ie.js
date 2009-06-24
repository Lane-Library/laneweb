(function(){
    if (YAHOO.env.ua.ie) {
        YAHOO.util.Event.addListener(this, 'load', function(){
            var ie = YAHOO.env.ua.ie,
                lists, i,
                d = document,
                list,
                //the bottom shaded border z-index has to be reduced . . .
                shadedBorderBottom = d.getElementById('sb-bot'),
                otherPortalOptions = d.getElementById('otherPortalOptions'),
                legendDropDown = d.getElementById('legend-drop-down'),
                otherSearches = d.getElementById('otherSearches'),
                superHeader = d.getElementById('shd');
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
                    if (otherSearches) {
                        otherSearches.activate = function(){
                            if (shadedBorderBottom) {
                                shadedBorderBottom.style.zIndex = -1;
                            }
                            if (otherPortalOptions) {
                                otherPortalOptions.style.zIndex = -1;
                            }
                            this.className = 'hover';
                        };
                        otherSearches.deactivate = function(){
                            if (shadedBorderBottom) {
                                shadedBorderBottom.style.zIndex = 1;
                            }
                            if (otherPortalOptions) {
                                otherPortalOptions.style.zIndex = 1;
                            }
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
                            uls[i].parentNode.activate = function() {
                                this.className = 'hover';
                            };
                            uls[i].parentNode.deactivate = function() {
                                this.className = '';
                            };
                        }
                    }
                    //TODO: use onavailable for this?
                    lists = ['shd', 'ft', 'gft','searchTabs'];
                    for (i = 0; i < lists.length; i++) {
                        list = d.getElementById(lists[i]);
                        if (list) {
                            list.getElementsByTagName('li')[0].style.border = 'none';
                        }
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
                        if (otherSearches) {
                            otherSearches.activate = function(){
                                if (shadedBorderBottom) {
                                    shadedBorderBottom.style.zIndex = -1;
                                }
                                if (otherPortalOptions) {
                                    otherPortalOptions.style.zIndex = -1;
                                }
                            };
                            otherSearches.deactivate = function(){
                                if (shadedBorderBottom) {
                                    shadedBorderBottom.style.zIndex = 1;
                                }
                                if (otherPortalOptions) {
                                    otherPortalOptions.style.zIndex = 1;
                                }
                            };
                        }
                    }
            });
        }
})();
