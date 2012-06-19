(function(){
//Show the look here bookmarks marketing div after 1.5 seconds
    //TODO: remove this file when done.
    var marketingNode = Y.one("#look-here");
        if (marketingNode) {
            setTimeout(function() {
                marketingNode.setStyle("display", "block");
            }, 1500);
        }
})();