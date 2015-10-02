(function() {

    // create the backToTop node
    var backToTop = Y.Node.create("<a class=\"back-to-top\" title=\"scroll back to top\"><i class=\"fa fa-chevron-up\"></i></a>"),

        /**
         * @method fadeIn display the backToTop node
         * @private
         */
        fadeIn = function() {
            var a = new Y.Anim({
                node: backToTop,
                to: {opacity: 1 },
                duration: 0.45
            });
            backToTop.addClass("active");
            a.run();
        },

        /**
         * @method fadeOut hide the backToTop node
         * @private
         */
        fadeOut = function() {
            var a = new Y.Anim({
                node: backToTop,
                to: {opacity: 0 },
                duration: 0.45
            });
            a.on("end", function() {
                backToTop.removeClass("active");
            });
            a.run();
        },

        /**
         * @method scrollToTop scroll the browser back to the top
         * @private
         */
        scrollToTop = function() {
            var a = new Y.Anim({
                node: "win",
                to: { scroll: [0, 0] },
                duration: 0.3,
                easing: Y.Easing.easeBoth
            });
            a.run();
        };

    // respond to scroll events and decide if the backToTop node needs to be hidden or displayed
    Y.on("scroll", function() {
        if (window.pageYOffset > 270 && !backToTop.hasClass("active")) {
            fadeIn();
        } else if (window.pageYOffset <= 270 && backToTop.hasClass("active")){
            fadeOut();
        }
    });

    // call the scrollToTop function when backToTop clicked
    backToTop.on("click", scrollToTop);

    // append the backToTop node
    Y.one("body").append(backToTop);

})();