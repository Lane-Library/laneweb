(function(){

    "use strict";

    //case 70132, bookmark instructions expanded when no bookmarks.
    var bookmarks = document.querySelectorAll("#bookmarks li"),
        instructions = document.querySelector(".bookmark-instructions");
    if (instructions && bookmarks.length === 0) {
        instructions.querySelector("div").className = "expanded";
    }
})();