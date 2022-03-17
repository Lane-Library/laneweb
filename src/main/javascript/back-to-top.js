(function() {

    "use strict";

    // backToTop node should be present in footer
    var backToTop = document.querySelector('.back-to-top'),

        /**
         * @method fadeIn display the backToTop node
         * @private
         */
        fadeIn = function() {
            backToTop.style.visibility = "visible";
            backToTop.classList.add("active");
        },

        /**
         * @method fadeOut hide the backToTop node
         * @private
         */
        fadeOut = function() {
            setTimeout(function() {
                backToTop.style.visibility = "hidden";
            }, 450);
            backToTop.classList.remove("active");
        },

        /**
         * @method scrollToTop scroll the browser back to the top
         * @private
         */
        scrollToTop = function() {
            document.documentElement.scrollIntoView();
        };

    if (backToTop) {
        // respond to scroll events and decide if the backToTop node needs to be hidden or displayed
        document.addEventListener("scroll", function() {
            if (window.pageYOffset > 270 && !backToTop.classList.contains("active")) {
                fadeIn();
            } else if (window.pageYOffset <= 270 && backToTop.classList.contains("active")){
                fadeOut();
            }
        });
    
        // call the scrollToTop function when backToTop clicked
        backToTop.addEventListener("click", scrollToTop);
    }

})();
