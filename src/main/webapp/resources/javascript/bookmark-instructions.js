(function(){
    //case 70132, bookmark instructions expanded when no bookmarks.
    var bookmarks = Y.all("#bookmarks li"),
        instructions = Y.one("#bookmarks-instructions");
    if (instructions && bookmarks.size() === 0) {
        instructions.one("div").addClass("expanded");
    }
})();