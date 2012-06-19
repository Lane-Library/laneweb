(function(){
//Show the look here bookmarks marketing div after 1.5 seconds
    //TODO: remove this file when done.
    var marketingNode = Y.one("#look-here");
        if (marketingNode) {
            setTimeout(function() {
                var sb;
                if (Y.UA.ie && Y.UA.ie < 8) {
                    sb = Y.one(".sb-tb");
                    //need to hide shaded background in IE < 8, case 71544
                    if (sb) {
                        sb.setStyle("visibility", "hidden");
                    }
                }
                marketingNode.setStyle("display", "block");
            }, 1500);
        }
})();