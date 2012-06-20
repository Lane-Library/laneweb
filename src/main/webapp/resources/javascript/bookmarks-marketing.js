(function(){
//Animate the look here bookmarks marketing div to move into position after 1.5 seconds and fade out after 10
    
    //TODO: remove this file when done.
    var marketingNode = Y.one(".look-here"), anim1, anim2;
        if (marketingNode) {
            anim1 = new Y.Anim({node : marketingNode, to: {top:"-35px"}});
            anim2 = new Y.Anim({node : marketingNode, to: {opacity : 0}});
            anim2.on("end", function() {
                marketingNode.remove(true);
            });
            setTimeout(function() {
                anim1.run();
            }, 1500);
            setTimeout(function() {
                anim2.run();
            }, 10000);
        }
})();