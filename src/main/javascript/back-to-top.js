// using YUI().use here so this can be used with the history template
YUI().use("anim-base", "anim-easing", "anim-scroll", "node-base", "node-screen", function(Y) {

    "use strict";

    // create the backToTop node
    var template = document.createElement("div"),
        innerHTML = "<a class=\"back-to-top\" title=\"scroll back to top\"><i class=\"fa fa-chevron-up\"></i></a>",
        backToTop,

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
            backToTop.classList.add("active");
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
                backToTop.classList.remove("active");
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
        if (window.pageYOffset > 270 && !backToTop.classList.contains("active")) {
            fadeIn();
        } else if (window.pageYOffset <= 270 && backToTop.classList.contains("active")){
            fadeOut();
        }
    });

    template.innerHTML = innerHTML;
    backToTop = template.firstChild;
    
    // call the scrollToTop function when backToTop clicked
    backToTop.addEventListener("click", scrollToTop);

    // append the backToTop node
    document.body.appendChild(backToTop);

});
