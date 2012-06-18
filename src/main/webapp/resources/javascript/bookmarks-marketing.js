(function(){
//Show the look here bookmarks marketing div after 1.5 seconds
    //TODO: remove this file when done.
        if (Y.one("#look-here")) {
            setTimeout(function() {
                Y.one("#look-here").setStyle("display", "block");
            }, 1500);
        }
})();