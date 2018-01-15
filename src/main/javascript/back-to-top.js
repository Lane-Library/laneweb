(function() {

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

    // respond to scroll events and decide if the backToTop node needs to be hidden or displayed
    document.addEventListener("scroll", function() {
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

})();
