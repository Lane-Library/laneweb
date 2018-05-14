YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

"use strict";

var bookmarksTestCase = new Y.Test.Case({

    name : 'Bookmarks Editor Test Case',

    editor : L.BookmarksEditor,
    
    "test click add": function() {
        var button = document.querySelector("button[value='add']");
        var prevented = false;
        button.addEventListener("click", function(event) {
            prevented = event.defaultPrevented;
        });
        button.click();
        Y.Assert.isTrue(prevented);
    }

});

new Y.Test.Console().render();

Y.Test.Runner.add(bookmarksTestCase);
Y.Test.Runner.masterSuite.name = "nobookmarks-editor-test.js";
Y.Test.Runner.run();

});
