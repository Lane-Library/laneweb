(function(){
//Show the look here bookmarks marketing div after 1.5 seconds
    //TODO: remove this file when done.
    var marketingNode = Y.one("#look-here");
        if (marketingNode) {
            setTimeout(function() {
                if (Y.UA.ie && Y.UA.ie < 8) {
                    //need to increase z-index in IE, case 71544
                    if (Y.one("#sb")) {
                        Y.one("#sb").setStyle("z-index", "1");
                    }
                }
                marketingNode.setStyle("display", "block");
            }, 1500);
        }
})();