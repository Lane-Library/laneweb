YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let backToTopTestCase = new Y.Test.Case({

        name : 'back-to-top Test Case',

        "test active at bottom": function() {
            window.scroll(0, 10000);
            this.wait(function() {
                Y.Assert.isTrue(document.querySelector(".back-to-top").classList.contains("active"));
            }, 500);
        },

      "test not active at top": function() {
          window.scroll(0, 0);
          this.wait(function() {
              Y.Assert.isFalse(document.querySelector(".back-to-top").classList.contains("active"));
          }, 500);
      },
      
      "test scrollToTop": function() {
          window.scroll(0, 10000);
          this.wait(function() {
              document.querySelector(".back-to-top").click();
              this.wait(function() {
                  Y.Assert.areEqual(0, window.pageYOffset);
              }, 350)
          }, 500);
      }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(backToTopTestCase);
    Y.Test.Runner.masterSuite.name = "back-to-top-test.js";
    Y.Test.Runner.run();

});
