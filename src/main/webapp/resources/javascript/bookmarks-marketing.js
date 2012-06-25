(function(){
//Animate the look here bookmarks marketing div to move into position after 1.5 seconds and fade out after 10
    
    //TODO: remove this file when done.
    var marketingNode = Y.one(".look-here"), 
        bookmarks = Y.all("#bookmarks li"),
        sb = Y.one("#sb"), anim1, anim2;
        if (marketingNode && bookmarks.size() === 0) {
            anim1 = new Y.Anim({node : marketingNode, to: {top:"-35px"}});
            anim2 = new Y.Anim({node : marketingNode, to: {opacity : 0}});
            //set style for IE < 8 so background behind marketingNode.
            if (Y.UA.ie > 0 && Y.UA.ie < 8 && sb) {
                sb.setStyle("zoom", "1");
                sb.setStyle("z-index", "1");
            }
            anim2.on("end", function() {
                marketingNode.remove(true);
                //restore:
                if (Y.UA.ie > 0 && Y.UA.ie < 8 && sb) {
                    sb.setStyle("z-index", "0");
                }
            });
            setTimeout(function() {
                anim1.run();
            }, 1500);
            setTimeout(function() {
                anim2.run();
            }, 10000);
        }
})();